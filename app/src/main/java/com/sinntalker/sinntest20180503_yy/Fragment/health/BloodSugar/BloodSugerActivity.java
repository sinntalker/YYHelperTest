package com.sinntalker.sinntest20180503_yy.Fragment.health.BloodSugar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.sinntalker.sinntest20180503_yy.Fragment.health.StepCounter.CommonAdapter;
import com.sinntalker.sinntest20180503_yy.Fragment.health.StepCounter.CommonViewHolder;
import com.sinntalker.sinntest20180503_yy.Fragment.health.StepCounter.DbUtils;
import com.sinntalker.sinntest20180503_yy.R;

import java.util.List;

public class BloodSugerActivity extends Activity {

    //声明控件
    ImageView mBackBSAIV;
    TextView mMessureHBSATV;
    ListView mBSTrendBSALV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_blood_suger);

        //实例化
        mBackBSAIV = findViewById(R.id.id_imageView_back_bloodSugar);
        mMessureHBSATV = findViewById(R.id.id_textView_measureHand_bloodSugar);
        mBSTrendBSALV = findViewById(R.id.id_listView_bloodSugarTrend_bloodSugar);

        mBackBSAIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mMessureHBSATV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddDataBloodSugarActivity.class));
                finish();
            }
        });

        setEmptyView(mBSTrendBSALV);
        if(DbUtils.getLiteOrm()==null){
            DbUtils.createDb(this, "jingzhi");
        }
        List<BloodSugarData> bloodSugarData =DbUtils.getQueryAll(BloodSugarData.class);
        Logger.d("BloodSugarData="+bloodSugarData);
        mBSTrendBSALV.setAdapter(new CommonAdapter<BloodSugarData>(this,bloodSugarData,R.layout.listview_item_blood_pressure_measure_history_data) {
            @Override
            protected void convertView(View item, BloodSugarData bloodSugarData) {
                TextView tv_date= CommonViewHolder.get(item,R.id.id_item_textView_dataTimePressure_historyData);
                TextView tv_Max= CommonViewHolder.get(item,R.id.id_item_textView_dataMaxPressure_historyData); //收缩压
                TextView tv_min= CommonViewHolder.get(item,R.id.id_item_textView_dataMinPressure_historyData); //舒张压
                tv_date.setText(bloodSugarData.getTime());
                tv_Max.setText(bloodSugarData.getDuring());
                tv_min.setText("血糖 " + bloodSugarData.getSugarValue());
            }
        });
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
