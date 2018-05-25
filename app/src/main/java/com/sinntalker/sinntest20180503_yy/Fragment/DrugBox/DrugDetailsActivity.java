package com.sinntalker.sinntest20180503_yy.Fragment.DrugBox;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinntalker.sinntest20180503_yy.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class DrugDetailsActivity extends Activity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_drug_details);

        imageView = findViewById(R.id.id_imageView_back_drugDetail);

        // 从Intent获取数据
        String username = getIntent().getStringExtra("drug_user"); //当前用户 username
        String boxNum = getIntent().getStringExtra("drug_boxNum"); //当前药箱 boxNum
        final String genericName = getIntent().getStringExtra("drug_genericName");//药品通用名称 genericName

        // 获取特定的视图
        final TextView genericName_textView = (TextView) findViewById(R.id.drug_genericName_textView);
        final TextView indications_textView = (TextView) findViewById(R.id.drug_indications_textView);
        final TextView dosage_textView = (TextView) findViewById(R.id.drug_dosage_textView);
        final TextView adverseReactions_textView = (TextView) findViewById(R.id.drug_adverseReactions_textView);
        final TextView taboo_textView = (TextView) findViewById(R.id.drug_taboo_textView);
        final TextView precautions_textView = (TextView) findViewById(R.id.drug_precautions_textView);
        final TextView packingSpecifications_textView = (TextView) findViewById(R.id.drug_packingSpecifications_textView);
        final TextView traits_textView = (TextView) findViewById(R.id.drug_traits_textView);
        final TextView validityPeriod_textView = (TextView) findViewById(R.id.drug_validityPeriod_textView);
        final TextView other_textView = (TextView) findViewById(R.id.drug_other_textView);

        // 根据数据设置视图展现
        genericName_textView.setText(genericName);

        //根据用户名和药箱编号和药品名称查询数据
        //查询条件1 用户名
        BmobQuery<DrugCommonDataBean> query_eq1 = new BmobQuery<DrugCommonDataBean>();
        query_eq1.addWhereEqualTo("username", username);
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
                        packingSpecifications_textView.setText(object.get(i).getPackingS());
                        traits_textView.setText(object.get(i).getTraits());
                        validityPeriod_textView.setText(object.get(i).getValidityPeriod());
                        other_textView.setText(object.get(i).getOther());
                    }
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
