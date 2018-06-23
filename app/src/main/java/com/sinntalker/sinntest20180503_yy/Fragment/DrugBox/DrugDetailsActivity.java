package com.sinntalker.sinntest20180503_yy.Fragment.DrugBox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.sinntalker.sinntest20180503_yy.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class DrugDetailsActivity extends Activity {

    private ImageView imageView;

    String strUserName;
    String boxNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_drug_details);

        imageView = findViewById(R.id.id_imageView_back_drugDetail);

        BmobUser mCurrentUser = BmobUser.getCurrentUser();
        strUserName = mCurrentUser.getUsername();
        Log.i("bmob", "DrugBoxActivity:当前用户：" + mCurrentUser.toString());
        Log.i("bmob", "DrugBoxActivity:当前用户名称：" + strUserName);

        // 从Intent获取数据
//        String username = getIntent().getStringExtra("drug_user"); //当前用户 username
        boxNum = getIntent().getStringExtra("drug_boxNum"); //当前药箱 boxNum
        final String genericName = getIntent().getStringExtra("drug_genericName");//药品通用名称 genericName

        // 获取特定的视图
        final TextView genericName_textView = (TextView) findViewById(R.id.drug_genericName_textView);
        final TextView indications_textView = (TextView) findViewById(R.id.drug_indications_textView);
        final TextView dosage_textView = (TextView) findViewById(R.id.drug_dosage_textView);
        final TextView adverseReactions_textView = (TextView) findViewById(R.id.drug_adverseReactions_textView);
        final TextView taboo_textView = (TextView) findViewById(R.id.drug_taboo_textView);
        final TextView precautions_textView = (TextView) findViewById(R.id.drug_precautions_textView);
        final TextView traits_textView = (TextView) findViewById(R.id.drug_traits_textView);
        final TextView ingredients_textView = (TextView) findViewById(R.id.drug_ingredients_textView);
        final TextView interaction_textView = (TextView) findViewById(R.id.drug_interaction_textView);
        final TextView clinicalTrials_textView = (TextView) findViewById(R.id.drug_clinicalTrials_textView);
        final TextView tResearch_textView = (TextView) findViewById(R.id.drug_tResearch_textView);
        final TextView approvalNumber_textView = (TextView) findViewById(R.id.drug_approvalNumber_textView);
        final TextView manufacturer_textView = (TextView) findViewById(R.id.drug_manufacturer_textView);
        final TextView classification_textView = (TextView) findViewById(R.id.drug_classification_textView);

        final TextView packingSpecifications_textView = (TextView) findViewById(R.id.drug_packingSpecifications_textView);
        final TextView productionDate_textView = (TextView) findViewById(R.id.drug_productionDate_textView);
        final TextView drugNumber_textView = (TextView) findViewById(R.id.drug_drugNumber_textView);
        final TextView validityPeriod_textView = (TextView) findViewById(R.id.drug_validityPeriod_textView);
        final TextView other_textView = (TextView) findViewById(R.id.drug_other_textView);

        // 根据数据设置视图展现
        genericName_textView.setText(genericName);

        //根据用户名和药箱编号和药品名称查询数据
        //查询条件1 用户名
        BmobQuery<DrugCommonDataBean> query_eq1 = new BmobQuery<DrugCommonDataBean>();
        query_eq1.addWhereEqualTo("userName", strUserName);
        //查询条件2 药箱编号
        BmobQuery<DrugCommonDataBean> query_eq2 = new BmobQuery<DrugCommonDataBean>();
        query_eq2.addWhereEqualTo("boxNumber", boxNum);
        //查询条件3 药品名称
        BmobQuery<DrugCommonDataBean> query_eq3 = new BmobQuery<DrugCommonDataBean>();
        query_eq3.addWhereEqualTo("genericName", genericName);

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
                        indications_textView.setText(object.get(i).getIndications());
                        dosage_textView.setText(object.get(i).getDosage());
                        adverseReactions_textView.setText(object.get(i).getAdverseReactions());
                        taboo_textView.setText(object.get(i).getTaboo());
                        precautions_textView.setText(object.get(i).getPrecautions());
                        traits_textView.setText(object.get(i).getTraits());
                        ingredients_textView.setText(object.get(i).getIngredients());
                        interaction_textView.setText(object.get(i).getInteraction());
                        clinicalTrials_textView.setText(object.get(i).getClinicalTrials());
                        tResearch_textView.setText(object.get(i).getTResearch());
                        approvalNumber_textView.setText(object.get(i).getApprovalNumber());
                        manufacturer_textView.setText(object.get(i).getManufacturer());
                        classification_textView.setText(object.get(i).getClassification());
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
        query_eq6.addWhereEqualTo("genericName", genericName);

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
                        packingSpecifications_textView.setText(list.get(i).getPackingS());
                        validityPeriod_textView.setText(list.get(i).getValidityPeriod());
                        other_textView.setText(list.get(i).getOther());
                        productionDate_textView.setText(list.get(i).getProductionDate());
                        drugNumber_textView.setText(list.get(i).getDrugNumber());
                    }
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DrugDetailsActivity.this, DrugBoxActivity.class).putExtra("DrugBoxNum", boxNum));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DrugDetailsActivity.this, DrugBoxActivity.class).putExtra("DrugBoxNum", boxNum));
        finish();
    }
}
