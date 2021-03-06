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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class AddDrugHandyActivity extends Activity implements View.OnClickListener {

    ImageView mBackADHIV;
    TextView mSaveSetADHTV;

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
    TextView mValuedDayTV; // 有效期至

    LinearLayout mMoreDetailsLL; //更多详情
    ImageView mMoreDetailIV; //动作
    LinearLayout mDetailLL;

    String strDrugBoxNum;
    String strUserName;

    String genericName;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_add_drug_handy); //对应布局

        //从DrugBox中获取传递过来的数据
        strDrugBoxNum = getIntent().getStringExtra("DrugBoxNum");
        Log.i("bmob", "AddDrugHandy：从drugBox中获取数据：当前盒子为：" + strDrugBoxNum);
        BmobUser mCurrentUser = BmobUser.getCurrentUser();
        strUserName = mCurrentUser.getUsername();
        Log.i("bmob", "AddDrugHandy：当前用户为 " + strUserName);
        
        initView();

        if (mDetailLL.getVisibility() == View.VISIBLE) {
            mDetailLL.setVisibility(View.VISIBLE);
            mMoreDetailIV.setImageResource(R.drawable.arrow_down_more_details_drug_add);
        } else {
            mDetailLL.setVisibility(View.GONE);
            mMoreDetailIV.setImageResource(R.drawable.arrow_right_more_details_drug_add);
        }

        mProductionDateET.setOnClickListener(this);

        mBackADHIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddDrugHandyActivity.this, DrugBoxActivity.class).putExtra("DrugBoxNum", strDrugBoxNum));
                Log.i("bmob", "AddDrugHandy：向drugBox中传递数据成功：传递数据为：" + strDrugBoxNum);
                finish();
            }
        });

        mMoreDetailsLL.setOnClickListener(this);

        mSaveSetADHTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Boolean[] SaveOk = {false};
                genericName = mGenericNameET.getText().toString().trim();
                if (genericName.length() == 0) {
                    Toast.makeText(AddDrugHandyActivity.this, "药品名称不能为空！", Toast.LENGTH_SHORT).show();
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
                    mDrugDataBean.setBoxNumber(strDrugBoxNum);
                    mDrugDataBean.setGenericName(genericName);
                    if (dosage.length() !=  0) { mDrugDataBean.setDosage(dosage); }else {  mDrugDataBean.setDosage("暂不明确"); }
                    if (productionDate.length() !=  0 || productionDate.equals("请设置")) { mDrugDataBean.setProductionDate(productionDate); }else { mDrugDataBean.setProductionDate("暂不明确"); }
                    if (validityPeriod.length() !=  0) { mDrugDataBean.setValidityPeriod(validityPeriod); }else { mDrugDataBean.setValidityPeriod("暂不明确"); }
                    if (packingS.length() !=  0) { mDrugDataBean.setPackingS(packingS); }else { mDrugDataBean.setPackingS("暂不明确"); }
                    if (drugNumber.length() !=  0) { mDrugDataBean.setDrugNumber(drugNumber); }else { mDrugDataBean.setDrugNumber("暂不明确"); }
                    if (other.length() !=  0) { mDrugDataBean.setOther(other); }else { mDrugDataBean.setOther("暂不明确"); }
                    if (deadline.length() !=  0 || deadline != null) { mDrugDataBean.setDeadDay(deadline); }else { mDrugDataBean.setDeadDay("暂不明确"); }
                    mDrugDataBean.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if(e==null){
                                SaveOk[0] = true;
                                Log.i("bmob","DrugDataBean保存成功");
                                Log.i("bmob","SaveOk[0]状态：" + SaveOk[0]);
                            }else{
                                SaveOk[0] = false;
                                Toast.makeText(AddDrugHandyActivity.this, "保存失败，请检查网络设置。", Toast.LENGTH_SHORT).show();
                                Log.i("bmob","保存失败，请检查网络设置："+e.getMessage()+","+e.getErrorCode());
                                Log.i("bmob","SaveOk[0]状态：" + SaveOk[0]);
                            }
                        }
                    });

                    DrugCommonDataBean mDrugCommonDataBean = new DrugCommonDataBean();
                    mDrugCommonDataBean.setUserName(strUserName);
                    mDrugCommonDataBean.setBoxNumber(strDrugBoxNum);
                    mDrugCommonDataBean.setGenericName(genericName);
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
                    mDrugCommonDataBean.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if(e==null){
                                SaveOk[0] = true;
                                Log.i("bmob","DrugCommonDataBean保存成功.");
                                Log.i("bmob","SaveOk[0]状态：" + SaveOk[0]);
                                Toast.makeText(AddDrugHandyActivity.this, "保存成功!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddDrugHandyActivity.this, DrugBoxActivity.class).putExtra("DrugBoxNum", strDrugBoxNum));
                                Log.i("bmob","保存成功，返回drugBox");
                                finish();
                            }else{
                                SaveOk[0] = false;
                                Toast.makeText(AddDrugHandyActivity.this, "保存失败，请检查网络设置。", Toast.LENGTH_SHORT).show();
                                Log.i("bmob","保存失败，请检查网络设置："+e.getMessage()+","+e.getErrorCode());
                                Log.i("bmob","SaveOk[0]状态：" + SaveOk[0]);
                            }
                        }
                    });
                }
            }
        });
    }

    private void initView() {
        //实例化
        mBackADHIV = findViewById(R.id.id_imageView_back_addDrugHandy);
        mSaveSetADHTV = findViewById(R.id.id_textView_saveInfo_addDrugHandy);
        mGenericNameET = findViewById(R.id.id_editText_genericName_addDrugHandy);
        mTraitsET = findViewById(R.id.id_editText_traits_addDrugHandy);
        mIngredientsET = findViewById(R.id.id_editText_ingredients_addDrugHandy);
        mIndicationsET = findViewById(R.id.id_editText_indications_addDrugHandy);
        mDosageET = findViewById(R.id.id_editText_dosage_addDrugHandy);
        mAdverseReactionsET = findViewById(R.id.id_editText_adverseReactions_addDrugHandy);
        mTabooET = findViewById(R.id.id_editText_taboo_addDrugHandy);
        mPrecautionsET = findViewById(R.id.id_editText_precautions_addDrugHandy);
        mInteractionET = findViewById(R.id.id_editText_interaction_addDrugHandy);
        mClinicalTrialsET = findViewById(R.id.id_editText_clinicalTrials_addDrugHandy);
        mTResearchET = findViewById(R.id.id_editText_tResearch_addDrugHandy);
        mApprovalNumberET = findViewById(R.id.id_editText_approvalNumber_addDrugHandy);
        mManufacturerET = findViewById(R.id.id_editText_manufacturer_addDrugHandy);
        mClassificationET = findViewById(R.id.id_editText_classification_addDrugHandy);
        mProductionDateET = findViewById(R.id.id_editText_productionDate_addDrugHandy);
        mValidityPeriodET = findViewById(R.id.id_editText_validityPeriod_addDrugHandy);
        mPackingSET = findViewById(R.id.id_editText_packingS_addDrugHandy);
        mDrugNumberET = findViewById(R.id.id_editText_drugNumber_addDrugHandy);
        mOtherET = findViewById(R.id.id_editText_other_addDrugHandy);
        mMoreDetailsLL = findViewById(R.id.id_linearLayout_moreDetails_addDrugHandy);
        mMoreDetailIV = findViewById(R.id.id_imageView_moreDetails_addDrugHandy);
        mDetailLL = findViewById(R.id.id_linearLayout_details);
        mValuedDayTV = findViewById(R.id.id_textView_valuedDay_addDrugHandy);
    }

    @Override
    public void onClick(View v) {
        if (v == mMoreDetailsLL) {
            if (mDetailLL.getVisibility() == View.VISIBLE) {
                mDetailLL.setVisibility(View.GONE);
                mMoreDetailIV.setImageResource(R.drawable.arrow_right_more_details_drug_add);
            } else {
                mDetailLL.setVisibility(View.VISIBLE);
                mMoreDetailIV.setImageResource(R.drawable.arrow_down_more_details_drug_add);
            }
        }
        if (v == mProductionDateET) {
            showTimeDialog_date();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();//注释掉这行,back键不退出activity
        startActivity(new Intent(AddDrugHandyActivity.this, DrugBoxActivity.class).putExtra("DrugBoxNum", strDrugBoxNum));
        Log.i("bmob", "AddDrugHandy：向drugBox中传递数据成功：传递数据为：" + strDrugBoxNum);
        finish();
    }
}
