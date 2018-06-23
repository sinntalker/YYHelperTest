
package com.sinntalker.sinntest20180503_yy.Fragment.DrugBox;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sinntalker.sinntest20180503_yy.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class DrugEditActivity extends Activity implements View.OnClickListener {

    ImageView mBackDEIV;
    ImageView mSaveSetDETV;

    TextView mProductionDateET; //生产日期        1
    EditText mValidityPeriodET;  //有效期         2
    EditText mPackingSET;  //包装规格              3
    EditText mDrugNumberET; //药品数量             4
    EditText mOtherET;  //其他                      5

    EditText mGenericNameET;  //药品通用名称     1
    EditText mTraitsET;   //药品性状             2
    EditText mIngredientsET; //药品成份         3
    EditText mIndicationsET;  //适应症          4
    EditText mDosageET;        //用法用量        5
    EditText mAdverseReactionsET;  //不良反应   6
    EditText mTabooET;  //禁忌                   7
    EditText mPrecautionsET;  //注意事项        8
    EditText mInteractionET; //药物相互作用     9
    EditText mClinicalTrialsET;  //临床试验    10
    EditText mTResearchET; //毒理研究           11
    EditText mApprovalNumberET; //批准文号     12
    EditText mManufacturerET; //生产企业       13
    EditText mClassificationET; //药物分类     14

//    String strDrugBoxNum;
    String strUserName;

//    String genericName;
    String traits;
    String ingredients;
    String indications;
    String dosage;
    String adverseReactions;
    String taboo;
    String precautions;
    String interaction;
    String clinicalTrials;
    String tResearch;
    String approvalNumber;
    String manufacturer;
    String classification;
    String productionDate;
    String validityPeriod;
    String packingS;
    String drugNumber;
    String other;

    String objectId_common;
    String objectId_specfic;

    String boxNum;

    LinearLayout mDrugDetailLL;
    ImageView mDrugDetailIV;
    LinearLayout mDetailLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_drug_edit);

        initView();

        BmobUser mCurrentUser = BmobUser.getCurrentUser();
        strUserName = mCurrentUser.getUsername();
        Log.i("bmob", "DrugBoxActivity:当前用户：" + mCurrentUser.toString());
        Log.i("bmob", "DrugBoxActivity:当前用户名称：" + strUserName);

        // 从Intent获取数据
//        String username = getIntent().getStringExtra("drug_user"); //当前用户 username
        boxNum = getIntent().getStringExtra("drug_boxNum"); //当前药箱 boxNum
        final String[] genericName = {getIntent().getStringExtra("drug_genericName")};//药品通用名称 genericName

        final String[] drugName = {genericName[0]};
        // 根据数据设置视图展现
        mGenericNameET.setText(genericName[0]);

        //根据用户名和药箱编号和药品名称查询数据
        //查询条件1 用户名
        BmobQuery<DrugCommonDataBean> query_eq1 = new BmobQuery<DrugCommonDataBean>();
        query_eq1.addWhereEqualTo("userName", strUserName);
        //查询条件2 药箱编号
        BmobQuery<DrugCommonDataBean> query_eq2 = new BmobQuery<DrugCommonDataBean>();
        query_eq2.addWhereEqualTo("boxNumber", boxNum);
        //查询条件3 药品名称
        BmobQuery<DrugCommonDataBean> query_eq3 = new BmobQuery<DrugCommonDataBean>();
        query_eq3.addWhereEqualTo("genericName", genericName[0]);

        //最后组装完整的and条件
        List<BmobQuery<DrugCommonDataBean>> queries = new ArrayList<BmobQuery<DrugCommonDataBean>>();
        queries.add(query_eq1);
        queries.add(query_eq2);
        queries.add(query_eq3);

        BmobQuery<DrugCommonDataBean> query = new BmobQuery<DrugCommonDataBean>();
        query.and(queries);

        query.findObjects(new FindListener<DrugCommonDataBean>() {
            @Override
            public void done(List<DrugCommonDataBean> object, BmobException e) {
                if(e==null){
//                    Toast.makeText(getApplicationContext(), "当前用户的医药库中包含该种药品共："+object.size()+"种。", Toast.LENGTH_LONG).show();
                    for (int i = 0; i < object.size(); i ++) {
                        mIndicationsET.setText(object.get(i).getIndications());
                        mDosageET.setText(object.get(i).getDosage());
                        mAdverseReactionsET.setText(object.get(i).getAdverseReactions());
                        mTabooET.setText(object.get(i).getTaboo());
                        mPrecautionsET.setText(object.get(i).getPrecautions());
                        mTraitsET.setText(object.get(i).getTraits());
                        mIngredientsET.setText(object.get(i).getIngredients());
                        mInteractionET.setText(object.get(i).getInteraction());
                        mClinicalTrialsET.setText(object.get(i).getClinicalTrials());
                        mTResearchET.setText(object.get(i).getTResearch());
                        mApprovalNumberET.setText(object.get(i).getApprovalNumber());
                        mManufacturerET.setText(object.get(i).getManufacturer());
                        mClassificationET.setText(object.get(i).getClassification());
                        objectId_common = object.get(i).getObjectId();
                    }
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });

        //查询条件1 用户名
        BmobQuery<DrugDataBean> query_eq4 = new BmobQuery<DrugDataBean>();
        query_eq4.addWhereEqualTo("userName", strUserName);
        //查询条件2 药箱编号
        BmobQuery<DrugDataBean> query_eq5 = new BmobQuery<DrugDataBean>();
        query_eq5.addWhereEqualTo("boxNumber", boxNum);
        //查询条件3 药品名称
        BmobQuery<DrugDataBean> query_eq6 = new BmobQuery<DrugDataBean>();
        query_eq6.addWhereEqualTo("genericName", genericName[0]);

        //最后组装完整的and条件
        List<BmobQuery<DrugDataBean>> queries2 = new ArrayList<BmobQuery<DrugDataBean>>();
        queries2.add(query_eq4);
        queries2.add(query_eq5);
        queries2.add(query_eq6);

        BmobQuery<DrugDataBean> query2 = new BmobQuery<DrugDataBean>();
        query2.and(queries2);
        query2.findObjects(new FindListener<DrugDataBean>() {
            @Override
            public void done(List<DrugDataBean> list, BmobException e) {
                if(e==null){
//                    Toast.makeText(getApplicationContext(), "当前用户的医药库中包含该种药品共："+object.size()+"种。", Toast.LENGTH_LONG).show();
                    for (int i = 0; i < list.size(); i ++) {
                        mPackingSET.setText(list.get(i).getPackingS());
                        mValidityPeriodET.setText(list.get(i).getValidityPeriod());
                        mOtherET.setText(list.get(i).getOther());
                        mProductionDateET.setText(list.get(i).getProductionDate());
                        mDrugNumberET.setText(list.get(i).getDrugNumber());
                        objectId_specfic = list.get(i).getObjectId();
                    }
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });

        mBackDEIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DrugEditActivity.this, DrugBoxActivity.class).putExtra("DrugBoxNum", boxNum));
                Log.i("bmob", "DrugEdit：向drugBox中传递数据成功：传递数据为：" + boxNum);
                finish();
            }
        });

        mSaveSetDETV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Boolean[] SaveOk = {false};
                drugName[0] = mGenericNameET.getText().toString().trim();
                if (drugName[0].length() == 0) {
                    Toast.makeText(DrugEditActivity.this, "药品名称不能为空！", Toast.LENGTH_SHORT).show();
                    Log.i("bmob", "AddDrugHandy：添加药品失败，药品名称不能为空！");
                } else {
                    traits = mTraitsET.getText().toString().trim();
                    ingredients = mIngredientsET.getText().toString().trim();
                    indications = mIndicationsET.getText().toString().trim();
                    dosage = mDosageET.getText().toString().trim();
                    adverseReactions = mAdverseReactionsET.getText().toString().trim();
                    taboo = mTabooET.getText().toString().trim();
                    precautions = mPrecautionsET.getText().toString().trim();
                    interaction = mInteractionET.getText().toString().trim();
                    clinicalTrials = mClinicalTrialsET.getText().toString().trim();
                    tResearch = mTResearchET.getText().toString().trim();
                    approvalNumber = mApprovalNumberET.getText().toString().trim();
                    manufacturer = mManufacturerET.getText().toString().trim();
                    classification = mClassificationET.getText().toString().trim();
                    productionDate = mProductionDateET.getText().toString().trim();
                    validityPeriod = mValidityPeriodET.getText().toString().trim();
                    packingS = mPackingSET.getText().toString().trim();
                    drugNumber = mDrugNumberET.getText().toString().trim();
                    other = mOtherET.getText().toString().trim();

                    String deadline = null;
                    try {
                        Integer time = Integer.valueOf(validityPeriod); //有效期XX个月
//                        String mProduce = mSettingTimeTV.getText().toString().trim(); // productionDate
                        Integer year = Integer.valueOf(productionDate.substring(0, 4));
                        Integer month = Integer.valueOf(productionDate.substring(5, 7));
                        Integer day = Integer.valueOf(productionDate.substring(8));

                        if (time >= 12) {
                            year += time / 12;
                            time = time - time / 12 * 12;
                        }
                        month += time;
                        if (month <= 12) {
//                            mResultTV.setText(year + "-" + month + "-" + day);
                            deadline = year + "-" + month + "-" + day;
                        } else {
                            year += month / 12; //有多少个22 就有多少年
                            month = month % 12;
                            deadline = year + "-" + month + "-" + day;
//                            mResultTV.setText(year + "-" + month + "-" + day);
                        }

                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }

                    DrugDataBean mDrugDataBean = new DrugDataBean();
                    mDrugDataBean.setUserName(strUserName);
                    mDrugDataBean.setBoxNumber(boxNum);
                    mDrugDataBean.setGenericName(drugName[0]);
                    if (dosage.length() !=  0) { mDrugDataBean.setDosage(dosage); }else {  mDrugDataBean.setDosage("暂不明确"); }
                    if (productionDate.length() !=  0) { mDrugDataBean.setProductionDate(productionDate); }else { mDrugDataBean.setProductionDate("暂不明确"); }
                    if (validityPeriod.length() !=  0) { mDrugDataBean.setValidityPeriod(validityPeriod); }else { mDrugDataBean.setValidityPeriod("暂不明确"); }
                    if (packingS.length() !=  0) { mDrugDataBean.setPackingS(packingS); }else { mDrugDataBean.setPackingS("暂不明确"); }
                    if (drugNumber.length() !=  0) { mDrugDataBean.setDrugNumber(drugNumber); }else { mDrugDataBean.setDrugNumber("暂不明确"); }
                    if (other.length() !=  0) { mDrugDataBean.setOther(other); }else { mDrugDataBean.setOther("暂不明确"); }
                    if (deadline.length() !=  0 || deadline != null) { mDrugDataBean.setDeadDay(deadline); }else { mDrugDataBean.setDeadDay("暂不明确"); }
                    mDrugDataBean.update(objectId_specfic, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                SaveOk[0] = true;
                                Log.i("bmob","DrugDataBean更新成功");
                                Log.i("bmob","SaveOk[0]状态：" + SaveOk[0]);
                            }else{
                                SaveOk[0] = false;
                                Toast.makeText(DrugEditActivity.this, "更新失败，请检查网络设置。", Toast.LENGTH_SHORT).show();
                                Log.i("bmob","更新失败，请检查网络设置："+e.getMessage()+","+e.getErrorCode());
                                Log.i("bmob","SaveOk[0]状态：" + SaveOk[0]);
                            }
                        }
                    });

                    DrugCommonDataBean mDrugCommonDataBean = new DrugCommonDataBean();
                    mDrugCommonDataBean.setUserName(strUserName);
                    mDrugCommonDataBean.setBoxNumber(boxNum);
                    mDrugCommonDataBean.setGenericName(drugName[0]);
                    if (traits.length() !=  0) { mDrugCommonDataBean.setTraits(traits); }else { mDrugCommonDataBean.setTraits("暂不明确"); }
                    if (ingredients.length() !=  0) { mDrugCommonDataBean.setIngredients(ingredients); }else { mDrugCommonDataBean.setIngredients("暂不明确"); }
                    if (indications.length() !=  0) { mDrugCommonDataBean.setIndications(indications); }else { mDrugCommonDataBean.setIndications("暂不明确"); }
                    if (dosage.length() !=  0) { mDrugCommonDataBean.setDosage(dosage); }else {  mDrugCommonDataBean.setDosage("暂不明确"); }
                    if (adverseReactions.length() !=  0) { mDrugCommonDataBean.setAdverseReactions(adverseReactions); }else { mDrugCommonDataBean.setAdverseReactions("暂不明确"); }
                    if (taboo.length() !=  0) { mDrugCommonDataBean.setTaboo(taboo); }else { mDrugCommonDataBean.setTaboo("暂不明确"); }
                    if (precautions.length() !=  0) { mDrugCommonDataBean.setPrecautions(precautions); }else { mDrugCommonDataBean.setPrecautions("暂不明确"); }
                    if (interaction.length() !=  0) { mDrugCommonDataBean.setInteraction(interaction); }else { mDrugCommonDataBean.setInteraction("暂不明确"); }
                    if (clinicalTrials.length() !=  0) { mDrugCommonDataBean.setClinicalTrials(clinicalTrials); }else { mDrugCommonDataBean.setClinicalTrials("暂不明确"); }
                    if (tResearch.length() !=  0) { mDrugCommonDataBean.setTResearch(tResearch); }else { mDrugCommonDataBean.setTResearch("暂不明确"); }
                    if (approvalNumber.length() !=  0) { mDrugCommonDataBean.setApprovalNumber(approvalNumber); }else { mDrugCommonDataBean.setApprovalNumber("暂不明确"); }
                    if (manufacturer.length() !=  0) { mDrugCommonDataBean.setManufacturer(manufacturer); }else { mDrugCommonDataBean.setManufacturer("暂不明确"); }
                    if (classification.length() !=  0) { mDrugCommonDataBean.setClassification(classification); }else { mDrugCommonDataBean.setClassification("暂不明确"); }
                    mDrugCommonDataBean.update(objectId_common, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                SaveOk[0] = true;
                                Log.i("bmob","DrugCommonDataBean更新成功.");
                                Log.i("bmob","SaveOk[0]状态：" + SaveOk[0]);
                                Toast.makeText(DrugEditActivity.this, "更新成功!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(DrugEditActivity.this, DrugBoxActivity.class).putExtra("DrugBoxNum", boxNum));
                                Log.i("bmob","更新成功，返回drugBox" + boxNum);
                                finish();
                            }else{
                                SaveOk[0] = false;
                                Toast.makeText(DrugEditActivity.this, "更新失败，请检查网络设置。", Toast.LENGTH_SHORT).show();
                                Log.i("bmob","更新失败，请检查网络设置："+e.getMessage()+","+e.getErrorCode());
                                Log.i("bmob","SaveOk[0]状态：" + SaveOk[0]);
                            }
                        }
                    });
                }
            }
        });

        mDrugDetailLL.setOnClickListener(this);

        if (mDetailLL.getVisibility() == View.VISIBLE) {
            mDetailLL.setVisibility(View.VISIBLE);
            mDrugDetailIV.setImageResource(R.drawable.arrow_down_more_details_drug_add);
        } else {
            mDetailLL.setVisibility(View.GONE);
            mDrugDetailIV.setImageResource(R.drawable.arrow_right_more_details_drug_add);
        }

    }

    private void initView() {
        //实例化
        mBackDEIV = findViewById(R.id.id_imageView_back_drugEdit);
        mSaveSetDETV = findViewById(R.id.id_textView_saveInfo_drugEdit);
        mGenericNameET = findViewById(R.id.id_editText_genericName_drugEdit);
        mTraitsET = findViewById(R.id.id_editText_traits_drugEdit);
        mIngredientsET = findViewById(R.id.id_editText_ingredients_drugEdit);
        mIndicationsET = findViewById(R.id.id_editText_indications_drugEdit);
        mDosageET = findViewById(R.id.id_editText_dosage_drugEdit);
        mAdverseReactionsET = findViewById(R.id.id_editText_adverseReactions_drugEdit);
        mTabooET = findViewById(R.id.id_editText_taboo_drugEdit);
        mPrecautionsET = findViewById(R.id.id_editText_precautions_drugEdit);
        mInteractionET = findViewById(R.id.id_editText_interaction_drugEdit);
        mClinicalTrialsET = findViewById(R.id.id_editText_clinicalTrials_drugEdit);
        mTResearchET = findViewById(R.id.id_editText_tResearch_drugEdit);
        mApprovalNumberET = findViewById(R.id.id_editText_approvalNumber_drugEdit);
        mManufacturerET = findViewById(R.id.id_editText_manufacturer_drugEdit);
        mClassificationET = findViewById(R.id.id_editText_classification_drugEdit);
        mProductionDateET = findViewById(R.id.id_editText_productionDate_drugEdit);
        mValidityPeriodET = findViewById(R.id.id_editText_validityPeriod_drugEdit);
        mPackingSET = findViewById(R.id.id_editText_packingS_drugEdit);
        mDrugNumberET = findViewById(R.id.id_editText_drugNumber_drugEdit);
        mOtherET = findViewById(R.id.id_editText_other_drugEdit);
        mDrugDetailLL = findViewById(R.id.id_linearLayout_moreDetails_drugEdit);
        mDetailLL = findViewById(R.id.id_linearLayout_details_drugEdit);
        mDrugDetailIV = findViewById(R.id.id_imageView_moreDetails_drugEdit);

        mProductionDateET.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DrugEditActivity.this, DrugBoxActivity.class).putExtra("DrugBoxNum", boxNum));
        Log.i("bmob", "DrugEdit：向drugBox中传递数据成功：传递数据为：" + boxNum);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v == mProductionDateET) {
            showTimeDialog_date();
        }
        if (v == mDrugDetailLL) {
            if (mDetailLL.getVisibility() == View.VISIBLE) {
                mDetailLL.setVisibility(View.GONE);
                mDrugDetailIV.setImageResource(R.drawable.arrow_right_more_details_drug_add);
            } else {
                mDetailLL.setVisibility(View.VISIBLE);
                mDrugDetailIV.setImageResource(R.drawable.arrow_down_more_details_drug_add);
            }
        }
    }

    private void showTimeDialog_date() {
        final Calendar calendar = Calendar.getInstance(Locale.CHINA);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = dateFormat.parse(year + "-" + (++month) + "-" + dayOfMonth);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mProductionDateET.setText(dateFormat.format(date));
            }
        }, year, month, day).show();
    }
}
