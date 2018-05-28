package com.sinntalker.sinntest20180503_yy.Fragment.DrugBox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sinntalker.sinntest20180503_yy.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class AddDrugScanActivity extends Activity {

    ImageView mBackADSIV; //返回
    TextView mScanResultADSTV; //扫描结果显示
    Button mScanADSBtn; //扫描按键
    LinearLayout mLinearLayout; //扫描结果显示布局
    Button mSaveToBox; //保存药品到当前药箱
    ListView mScanSearchResultADSLV; //匹配数据库结果

    String strDrugBoxNum;
    String strUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_add_drug_scan); //对应布局

        strDrugBoxNum = getIntent().getStringExtra("DrugBoxNum");
        Log.i("bmob", "AddDrugScan：从drugBox中获取数据：当前盒子为：" + strDrugBoxNum);
        BmobUser mCurrentUser = BmobUser.getCurrentUser();
        strUserName = mCurrentUser.getUsername();
        Log.i("bmob", "AddDrugHandy：当前用户为 " + strUserName);

        //实例化
        mBackADSIV = findViewById(R.id.id_imageView_back_addDrugScan);
        mScanADSBtn = findViewById(R.id.id_button_scan_addDrugScan);
        mScanResultADSTV = findViewById(R.id.id_textView_scanResult_addDrugScan);
        mLinearLayout = findViewById(R.id.id_linearLayout_scanMatchResults_addDrugScan);
        mScanSearchResultADSLV = findViewById(R.id.id_listView_scanSearchResults_addDrugScan);
        mSaveToBox = findViewById(R.id.id_button_addDrugToBox_addDrugScan);

        //设置扫描结果等控件不可见
        mLinearLayout.setVisibility(View.GONE);
        mScanResultADSTV.setVisibility(View.GONE);
        mSaveToBox.setVisibility(View.GONE);
        mScanSearchResultADSLV.setVisibility(View.GONE);

        //返回
        mBackADSIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddDrugScanActivity.this, DrugBoxActivity.class).putExtra("DrugBoxNum", strDrugBoxNum));
                Log.i("bmob", "AddDrugScan：向drugBox中传递数据成功：传递数据为 " + strDrugBoxNum);
                finish();
            }
        });

        mScanADSBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(AddDrugScanActivity.this);
                // 设置要扫描的条码类型，ONE_D_CODE_TYPES：一维码，QR_CODE_TYPES-二维码
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                integrator.setCaptureActivity(ScanActivity.class);
                integrator.setPrompt("请扫描药品条形码"); //底部的提示文字，设为""可以置空
                integrator.setCameraId(0); //前置或者后置摄像头
                integrator.setBeepEnabled(false); //扫描成功的「哔哔」声，默认开启
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "扫码取消！", Toast.LENGTH_LONG).show();
                Log.i("bmob", "AddDrugScan：扫码取消");
            } else {
                Toast.makeText(this, "扫描成功，条码值: " + result.getContents(), Toast.LENGTH_LONG).show();
                Log.i("bmob", "AddDrugScan：扫描成功，条码值: " + result.getContents());

                //扫码成功后，隐藏扫码按键，显示扫码结果布局和ListView、button
                mScanADSBtn.setVisibility(View.GONE);
                mScanResultADSTV.setText(result.getContents());
                mScanResultADSTV.setVisibility(View.GONE);
                mLinearLayout.setVisibility(View.VISIBLE);
                mScanSearchResultADSLV.setVisibility(View.VISIBLE);
                mSaveToBox.setVisibility(View.VISIBLE);

                BmobQuery<DrugsBeanLib> query = new BmobQuery<DrugsBeanLib>();
                query.addWhereEqualTo("Barcode", result.getContents());
                query.findObjects(new FindListener<DrugsBeanLib>() {
                    @Override
                    public void done(List<DrugsBeanLib> object, BmobException e) {
                        if (e == null) {
                            Log.i("bmob", "查询成功，共查询到 " + object.size() + " 条数据。");
                            if (object.size() == 0) { //查询成功，无数据
                                setEmptyView(mScanSearchResultADSLV);
                                mSaveToBox.setVisibility(View.GONE);
                            } else { //查询成功，有数据
                                final String [] drugName = new String[object.size()]; // 1
                                final Boolean[] isChecked_drug = new Boolean[object.size()]; // 2
                                final String [] traits = new String[object.size()]; // 3
                                final String [] ingredients = new String[object.size()]; // 4
                                final String [] indications = new String[object.size()]; // 5
                                final String [] dosage = new String[object.size()]; // 6
                                final String [] adverseReactions = new String[object.size()]; // 7
                                final String [] taboo = new String[object.size()]; // 8
                                final String [] precautions = new String[object.size()]; // 9
                                final String [] interaction = new String[object.size()]; // 10
                                final String [] clinicalTrials = new String[object.size()]; // 11
                                final String [] tResearch = new String[object.size()]; // 12
                                final String [] approvalNumber = new String[object.size()]; // 13
                                final String [] manufacturer = new String[object.size()]; // 14
                                final String [] classification = new String[object.size()]; // 15
                                final String [] productionDate = new String[object.size()]; // 1
                                final String [] validityPeriod = new String[object.size()]; // 2
                                final String [] packingS = new String[object.size()]; // 3
                                final String [] drugNumber = new String[object.size()]; // 4
                                final String [] other = new String[object.size()]; // 5

                                for (int i = 0; i < object.size(); i ++) {
                                    drugName[i] = object.get(i).getGenericName();
                                    isChecked_drug[i] = false;
                                    indications[i] = object.get(i).getIndications();
                                    dosage[i] = object.get(i).getDosage();
                                    adverseReactions[i] = object.get(i).getAdverseReactions();
                                    taboo[i] = object.get(i).getTaboo();
                                    ingredients[i] = object.get(i).getIngredients();
                                    traits[i] = object.get(i).getTraits();
                                    interaction[i] = object.get(i).getInteraction();
                                    clinicalTrials[i] = object.get(i).getClinicalTrials();
                                    tResearch[i] = object.get(i).getTResearch();
                                    approvalNumber[i] = object.get(i).getApprovalNumber();
                                    manufacturer[i] = object.get(i).getManufacturer();
                                    classification[i] = object.get(i).getClassification();
                                    packingS[i] = object.get(i).getPackingS();
                                    validityPeriod[i] = object.get(i).getValidityPeriod();
                                    other[i] = object.get(i).getOther();
                                    productionDate[i] = object.get(i).getProductionDate();
                                    precautions[i] = object.get(i).getPrecautions();
                                    drugNumber[i] = "暂不明确";
                                }

                                class DrugScanMatchListAdapter extends BaseAdapter {
                                    private Context context;

                                    public DrugScanMatchListAdapter(Context context) {
                                        this.context = context;
                                    }

                                    @Override
                                    public int getCount() {
                                        return drugName.length;
                                    }

                                    @Override
                                    public Object getItem(int position) {
                                        return drugName[position];
                                    }

                                    @Override
                                    public long getItemId(int position) {
                                        return position;
                                    }

                                    ViewHolder viewHolder;

                                    @Override
                                    public View getView(final int position, View convertView, ViewGroup parent) {
                                        // 创建布局
                                        convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_item_scan_match_results, parent, false);
                                        viewHolder = new ViewHolder();
                                        viewHolder.drug_check = convertView.findViewById(R.id.id_checkBox_item_choseMatchResults_scanAddDrug);
                                        viewHolder.drugName_text = convertView.findViewById(R.id.id_textView_item_choseMatchResults_scanAddDrug);
                                        viewHolder.drugInfo_image = convertView.findViewById(R.id.id_textView_item_details_scanAddDrug);

                                        viewHolder.drugName_text.setText(drugName[position]);
                                        viewHolder.drug_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                                            @Override
                                            public void onCheckedChanged(CompoundButton buttonView,
                                                                         boolean isChecked) {
                                                // TODO Auto-generated method stub
                                                if(isChecked){ //选中
                                                    isChecked_drug[position] = true;
                                                    Log.i("bmob", drugName[position] + " 药品被选中，"+isChecked_drug[position]);
                                                }else{ //取消选中
                                                    isChecked_drug[position] = false;
                                                    Log.i("bmob", drugName[position] + " 药品未被选中，"+isChecked_drug[position]);
                                                }
                                            }
                                        });
                                        viewHolder.drugInfo_image.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
//                                            Toast.makeText(getApplicationContext(), "设置提醒", Toast.LENGTH_LONG).show();
                                                Log.i("bmob", drugName[position] + " 药品详情");
                                                startActivity(new Intent(getApplicationContext(), PublicDrugDetailActivity.class).putExtra("drug_genericName", drugName[position]));
                                            }
                                        });
                                        return convertView;
                                    }

                                    class ViewHolder {
                                        CheckBox drug_check; //是否选中药品
                                        TextView drugName_text; //药品名称
                                        TextView drugInfo_image; //药品详细信息
                                    }

                                }

                                mScanSearchResultADSLV.setAdapter(new DrugScanMatchListAdapter(AddDrugScanActivity.this));

                                mSaveToBox.setVisibility(View.VISIBLE);
                                mSaveToBox.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        for (int i = 0; i < drugName.length; i ++) {
                                            if (isChecked_drug[i]) {
                                                Log.i("bmob", " 将当前药品"+drugName[i]+"添加到药箱中");
                                                DrugDataBean mDrugDataBean = new DrugDataBean();
                                                mDrugDataBean.setUserName(strUserName);
                                                mDrugDataBean.setBoxNumber(strDrugBoxNum);
                                                mDrugDataBean.setGenericName(drugName[i]);
                                                if (dosage[i].length() !=  0) { mDrugDataBean.setDosage(dosage[i]); }else {  mDrugDataBean.setDosage("暂不明确"); }
                                                if (productionDate[i].length() !=  0) { mDrugDataBean.setProductionDate(productionDate[i]); }else { mDrugDataBean.setProductionDate("暂不明确"); }
                                                if (validityPeriod[i].length() !=  0) { mDrugDataBean.setValidityPeriod(validityPeriod[i]); }else { mDrugDataBean.setValidityPeriod("暂不明确"); }
                                                if (packingS[i].length() !=  0) { mDrugDataBean.setPackingS(packingS[i]); }else { mDrugDataBean.setPackingS("暂不明确"); }
                                                if (drugNumber[i].length() !=  0) { mDrugDataBean.setDrugNumber(drugNumber[i]); }else { mDrugDataBean.setDrugNumber("暂不明确"); }
                                                if (other[i].length() !=  0) { mDrugDataBean.setOther(other[i]); }else { mDrugDataBean.setOther("暂不明确"); }
                                                mDrugDataBean.save(new SaveListener<String>() {
                                                    @Override
                                                    public void done(String s, BmobException e) {
                                                        if(e==null){
                                                            Log.i("bmob","DrugDataBean保存成功");
                                                        }else{
                                                            Toast.makeText(AddDrugScanActivity.this, "保存失败，请检查网络设置。", Toast.LENGTH_SHORT).show();
                                                            Log.i("bmob","保存失败，请检查网络设置："+e.getMessage()+","+e.getErrorCode());
                                                        }
                                                    }
                                                });

                                                DrugCommonDataBean mDrugCommonDataBean = new DrugCommonDataBean();
                                                mDrugCommonDataBean.setUserName(strUserName);
                                                mDrugCommonDataBean.setBoxNumber(strDrugBoxNum);
                                                mDrugCommonDataBean.setGenericName(drugName[i]);
                                                if (traits[i].length() !=  0) { mDrugCommonDataBean.setTraits(traits[i]); }else { mDrugCommonDataBean.setTraits("暂不明确"); }
                                                if (ingredients[i].length() !=  0) { mDrugCommonDataBean.setIngredients(ingredients[i]); }else { mDrugCommonDataBean.setIngredients("暂不明确"); }
                                                if (indications[i].length() !=  0) { mDrugCommonDataBean.setIndications(indications[i]); }else { mDrugCommonDataBean.setIndications("暂不明确"); }
                                                if (dosage[i].length() !=  0) { mDrugCommonDataBean.setDosage(dosage[i]); }else {  mDrugCommonDataBean.setDosage("暂不明确"); }
                                                if (adverseReactions[i].length() !=  0) { mDrugCommonDataBean.setAdverseReactions(adverseReactions[i]); }else { mDrugCommonDataBean.setAdverseReactions("暂不明确"); }
                                                if (taboo[i].length() !=  0) { mDrugCommonDataBean.setTaboo(taboo[i]); }else { mDrugCommonDataBean.setTaboo("暂不明确"); }
                                                if (precautions[i].length() !=  0) { mDrugCommonDataBean.setPrecautions(precautions[i]); }else { mDrugCommonDataBean.setPrecautions("暂不明确"); }
                                                if (interaction[i].length() !=  0) { mDrugCommonDataBean.setInteraction(interaction[i]); }else { mDrugCommonDataBean.setInteraction("暂不明确"); }
                                                if (clinicalTrials[i].length() !=  0) { mDrugCommonDataBean.setClinicalTrials(clinicalTrials[i]); }else { mDrugCommonDataBean.setClinicalTrials("暂不明确"); }
                                                if (tResearch[i].length() !=  0) { mDrugCommonDataBean.setTResearch(tResearch[i]); }else { mDrugCommonDataBean.setTResearch("暂不明确"); }
                                                if (approvalNumber[i].length() !=  0) { mDrugCommonDataBean.setApprovalNumber(approvalNumber[i]); }else { mDrugCommonDataBean.setApprovalNumber("暂不明确"); }
                                                if (manufacturer[i].length() !=  0) { mDrugCommonDataBean.setManufacturer(manufacturer[i]); }else { mDrugCommonDataBean.setManufacturer("暂不明确"); }
                                                if (classification[i].length() !=  0) { mDrugCommonDataBean.setClassification(classification[i]); }else { mDrugCommonDataBean.setClassification("暂不明确"); }
                                                mDrugCommonDataBean.save(new SaveListener<String>() {
                                                    @Override
                                                    public void done(String s, BmobException e) {
                                                        if(e==null){
                                                            Log.i("bmob","DrugCommonDataBean保存成功.");
                                                            Toast.makeText(AddDrugScanActivity.this, "保存成功!", Toast.LENGTH_SHORT).show();
                                                        }else{
                                                            Toast.makeText(AddDrugScanActivity.this, "保存失败，请检查网络设置。", Toast.LENGTH_SHORT).show();
                                                            Log.i("bmob","保存失败，请检查网络设置："+e.getMessage()+","+e.getErrorCode());
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                        startActivity(new Intent(AddDrugScanActivity.this, DrugBoxActivity.class).putExtra("DrugBoxNum", strDrugBoxNum));
                                        Log.i("bmob","保存成功，返回drugBox");
                                        finish();
                                    }
                                });

                            }
                        } else {
                            Log.i("bmob", "匹配医药库数据失败：" + e.getMessage() + "," + e.getErrorCode());
                            Toast.makeText(AddDrugScanActivity.this, "查询失败，请稍后重试", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    protected <T extends View> T setEmptyView(ListView listView) {
        TextView emptyView = new TextView(this);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        emptyView.setText("暂无数据！");
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) listView.getParent()).addView(emptyView);
        listView.setEmptyView(emptyView);
        return (T) emptyView;
    }

}
