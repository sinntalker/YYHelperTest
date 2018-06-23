package com.sinntalker.sinntest20180503_yy.Fragment.DrugBox;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.sinntalker.sinntest20180503_yy.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class PublicDrugDetailActivity extends Activity {

    private ImageView mBackPDDAIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_public_drug_box);

        mBackPDDAIV = findViewById(R.id.id_imageView_back_publicDrugDetail);

        mBackPDDAIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final String genericName = getIntent().getStringExtra("drug_genericName");//药品通用名称 genericName

        // 获取特定的视图
        final TextView genericName_textView = (TextView) findViewById(R.id.drug_genericName_textView_publicDrugDetail);
        final TextView indications_textView = (TextView) findViewById(R.id.drug_indications_textView_publicDrugDetail);
        final TextView dosage_textView = (TextView) findViewById(R.id.drug_dosage_textView_publicDrugDetail);
        final TextView adverseReactions_textView = (TextView) findViewById(R.id.drug_adverseReactions_textView_publicDrugDetail);
        final TextView taboo_textView = (TextView) findViewById(R.id.drug_taboo_textView_publicDrugDetail);
        final TextView precautions_textView = (TextView) findViewById(R.id.drug_precautions_textView_publicDrugDetail);
        final TextView traits_textView = (TextView) findViewById(R.id.drug_traits_textView_publicDrugDetail);
        final TextView ingredients_textView = (TextView) findViewById(R.id.drug_ingredients_textView_publicDrugDetail);
        final TextView interaction_textView = (TextView) findViewById(R.id.drug_interaction_textView_publicDrugDetail);
        final TextView clinicalTrials_textView = (TextView) findViewById(R.id.drug_clinicalTrials_textView_publicDrugDetail);
        final TextView tResearch_textView = (TextView) findViewById(R.id.drug_tResearch_textView_publicDrugDetail);
        final TextView approvalNumber_textView = (TextView) findViewById(R.id.drug_approvalNumber_textView_publicDrugDetail);
        final TextView manufacturer_textView = (TextView) findViewById(R.id.drug_manufacturer_textView_publicDrugDetail);
        final TextView classification_textView = (TextView) findViewById(R.id.drug_classification_textView_publicDrugDetail);

        final TextView packingSpecifications_textView = (TextView) findViewById(R.id.drug_packingSpecifications_textView_publicDrugDetail);
        final TextView productionDate_textView = (TextView) findViewById(R.id.drug_productionDate_textView_publicDrugDetail);
//        final TextView drugNumber_textView = (TextView) findViewById(R.id.drug_drugNumber_textView_publicDrugDetail);
        final TextView validityPeriod_textView = (TextView) findViewById(R.id.drug_validityPeriod_textView_publicDrugDetail);
        final TextView other_textView = (TextView) findViewById(R.id.drug_other_textView_publicDrugDetail);

        if (genericName != null) {
            genericName_textView.setText(genericName);

            BmobQuery<DrugsBeanLib> query = new BmobQuery<DrugsBeanLib>();
            query.addWhereEqualTo("genericName", genericName);
            query.findObjects(new FindListener<DrugsBeanLib>() {
                @Override
                public void done(List<DrugsBeanLib> list, BmobException e) {
                    if (e == null) {
                        Log.i("bmob", "查询成功，共有"+list.size()+"条");

                        for (int i = 0; i < list.size(); i ++) {
                            indications_textView.setText(list.get(i).getIndications());
                            dosage_textView.setText(list.get(i).getDosage());
                            adverseReactions_textView.setText(list.get(i).getAdverseReactions());
                            taboo_textView.setText(list.get(i).getTaboo());
                            precautions_textView.setText(list.get(i).getPrecautions());
                            traits_textView.setText(list.get(i).getTraits());
                            ingredients_textView.setText(list.get(i).getIngredients());
                            interaction_textView.setText(list.get(i).getInteraction());
                            clinicalTrials_textView.setText(list.get(i).getClinicalTrials());
                            tResearch_textView.setText(list.get(i).getTResearch());
                            approvalNumber_textView.setText(list.get(i).getApprovalNumber());
                            manufacturer_textView.setText(list.get(i).getManufacturer());
                            classification_textView.setText(list.get(i).getClassification());

                            packingSpecifications_textView.setText(list.get(i).getPackingS());
                            validityPeriod_textView.setText(list.get(i).getValidityPeriod());
                            other_textView.setText(list.get(i).getOther());
                            productionDate_textView.setText(list.get(i).getProductionDate());
//                        drugNumber_textView.setText(list.get(i).getDrugNumber());
                        }
                    }else {
                        Log.i("bmob", "查询失败，错误码："+e.getErrorCode());
                    }
                }
            });
        } else {
            genericName_textView.setText("传输数据错误，请重新打开APP。");
            indications_textView.setText("传输数据错误，请重新打开APP。");
            dosage_textView.setText("传输数据错误，请重新打开APP。");
            adverseReactions_textView.setText("传输数据错误，请重新打开APP。");
            taboo_textView.setText("传输数据错误，请重新打开APP。");
            precautions_textView.setText("传输数据错误，请重新打开APP。");
            traits_textView.setText("传输数据错误，请重新打开APP。");
            ingredients_textView.setText("传输数据错误，请重新打开APP。");
            interaction_textView.setText("传输数据错误，请重新打开APP。");
            clinicalTrials_textView.setText("传输数据错误，请重新打开APP。");
            tResearch_textView.setText("传输数据错误，请重新打开APP。");
            approvalNumber_textView.setText("传输数据错误，请重新打开APP。");
            manufacturer_textView.setText("传输数据错误，请重新打开APP。");
            classification_textView.setText("传输数据错误，请重新打开APP。");

            packingSpecifications_textView.setText("传输数据错误，请重新打开APP。");
            validityPeriod_textView.setText("传输数据错误，请重新打开APP。");
            other_textView.setText("传输数据错误，请重新打开APP。");
            productionDate_textView.setText("传输数据错误，请重新打开APP。");
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }
}
