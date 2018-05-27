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
import com.journeyapps.barcodescanner.CaptureActivity;
import com.sinntalker.sinntest20180503_yy.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class AddDrugScanActivity extends Activity {

    ImageView mBackADSIV; //返回
    TextView mScanResultADSTV; //扫描结果显示
    Button mScanADSBtn; //扫描按键
    LinearLayout mLinearLayout; //扫描结果显示布局
    Button mSaveToBox; //保存药品到当前药箱

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_add_drug_scan);

//        String scanResult = getIntent().getStringExtra("ScanResult");

        //实例化
        mBackADSIV = findViewById(R.id.id_imageView_back_addDrugScan);
        mScanResultADSTV = findViewById(R.id.id_textView_scanResult_addDrugScan);
        mScanADSBtn = findViewById(R.id.id_button_scan_addDrugScan);
        mLinearLayout = findViewById(R.id.id_linearLayout_scanMatchResults_addDrugScan);
        mSaveToBox = findViewById(R.id.id_button_addDrugToBox_addDrugScan);

        mLinearLayout.setVisibility(View.GONE);

//        mScanResultADSTV.setText(scanResult);

        mBackADSIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                integrator.setPrompt("请扫描"); //底部的提示文字，设为""可以置空
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
            } else {
                Toast.makeText(this, "扫描成功，条码值: " + result.getContents(), Toast.LENGTH_LONG).show();

                mScanADSBtn.setVisibility(View.GONE); //显示按键消失
                mLinearLayout.setVisibility(View.VISIBLE); //扫描结果显示布局显示
                mScanResultADSTV.setText(result.getContents());

                //查找是否含有该条形码的数据
                BmobQuery<DrugsBeanLib> query = new BmobQuery<DrugsBeanLib>();
                query.addWhereEqualTo("Barcode", result.getContents());
                //返回50条数据，如果不加上这条语句，默认返回10条数据
                query.setLimit(10);
                //执行查询方法
                query.findObjects(new FindListener<DrugsBeanLib>() {
                    @Override
                    public void done(final List<DrugsBeanLib> object, BmobException e) {
                        if (e == null) {
//                    Toast.makeText(getApplicationContext(), "查询成功：共"+object.size()+"条数据。", Toast.LENGTH_LONG).show();

                            final String[] drugName = new String[object.size()];
                            final Boolean[] isChecked_drug = new Boolean[object.size()];

                            for (int i = 0; i < object.size(); i++) {
                                drugName[i] = object.get(i).getGenericName(); //获取药品名称
                                isChecked_drug[i] = false; //所有药品均为选中
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
                            //通过ID获取listView
                            final ListView listView = (ListView) findViewById(R.id.id_listView_scanSearchResults_addDrugScan);

                            if (object.size() > 0) {
                                //设置listView的Adapter
                                listView.setAdapter(new DrugScanMatchListAdapter(getApplicationContext()));
                                mSaveToBox.setVisibility(View.VISIBLE);
                                mSaveToBox.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        for (int i = 0; i < object.size(); i ++) {
                                            if (isChecked_drug[i]) {
                                                Log.i("bmob", " 将当前药品"+drugName[i]+"添加到药箱中");
                                            }
                                        }
                                    }
                                });
                            } else {
                                setEmptyView(listView);
                                mSaveToBox.setVisibility(View.GONE);
                            }

                        } else {
                            Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                            Toast.makeText(getApplicationContext(), "查询失败：" + e.getMessage(), Toast.LENGTH_LONG).show();
                            Log.i("bmob", " 查询失败");
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
