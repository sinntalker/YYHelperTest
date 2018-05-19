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
        final String genericName = getIntent().getStringExtra("drug_genericName");//药品通用名称 genericName
        String drug_user = getIntent().getStringExtra("drug_name"); //获取用户名称用以匹配用户医药库
//        intent.putExtra("drug_user", phone);//药品通用名称 genericName

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

        BmobQuery<DrugsBean> eq_phone = new BmobQuery<DrugsBean>();
        BmobQuery<DrugsBean> eq_genericName = new BmobQuery<DrugsBean>();
        //查询playerName叫“比目”的数据
        //查询当前登陆用户
        eq_phone.addWhereEqualTo("phone", drug_user);
        //查询当前药品名称
        eq_genericName.addWhereEqualTo("genericName", genericName);
        List<BmobQuery<DrugsBean>> mainQuery = new ArrayList<BmobQuery<DrugsBean>>();
        mainQuery.add(eq_phone);
        mainQuery.add(eq_genericName);
        BmobQuery<DrugsBean> query = new BmobQuery<DrugsBean>();
        query.and(mainQuery);
        query.findObjects(new FindListener<DrugsBean>() {
            @Override
            public void done(List<DrugsBean> object, BmobException e) {
                if(e==null){
                    Toast.makeText(getApplicationContext(), "当前用户的医药库中包含该种药品共："+object.size()+"种。", Toast.LENGTH_LONG).show();
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
        //返回50条数据，如果不加上这条语句，默认返回10条数据
//        query.setLimit(50);
        //执行查询方法
//        query.findObjects( new QueryListener<DrugsBean>() {
//
//            @Override
//            public void done(DrugsBean object, BmobException e) {
//                // 根据数据设置视图展现
////                genericName_textView.setText((CharSequence) object.getGenericName());
//                indications_textView.setText(object.getIndications());
//                dosage_textView.setText(object.getDosage());
//                adverseReactions_textView.setText(object.getAdverseReactions());
//                taboo_textView.setText(object.getTaboo());
//                precautions_textView.setText(object.getPrecautions());
//                packingSpecifications_textView.setText(object.getPackingS());
//                traits_textView.setText(object.getTraits());
//                validityPeriod_textView.setText(object.getValidityPeriod());
//                other_textView.setText(object.getOther());
//            }
//        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
