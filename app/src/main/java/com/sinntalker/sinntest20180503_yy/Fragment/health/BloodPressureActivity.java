package com.sinntalker.sinntest20180503_yy.Fragment.health;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.sinntalker.sinntest20180503_yy.Fragment.health.StepCounter.SharedPreferencesUtils_StepCounter;
import com.sinntalker.sinntest20180503_yy.Fragment.health.StepCounter.StepData;
import com.sinntalker.sinntest20180503_yy.R;

import java.util.List;

public class BloodPressureActivity extends Activity {

    //声明控件变量
    ImageView mBackBPAIV; //返回
    TextView mMeasureHBPATV; //手动测量
    ListView mBPDataBPALV; //血压数据

    private SharedPreferencesUtils_StepCounter mSharedPreferencesUtils_BloodPressure;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_blood_pressure);

        //实例化控件
        mBackBPAIV = findViewById(R.id.id_imageView_back_bloodPressure);
        mMeasureHBPATV = findViewById(R.id.id_textView_measureHand_bloodPressure);
        mBPDataBPALV = findViewById(R.id.id_listView_bloodPressureTrend_bloodPressure);

        mBackBPAIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mMeasureHBPATV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddDataBloodPressureActivity.class));
                finish();
            }
        });

        setEmptyView(mBPDataBPALV);
        if(DbUtils.getLiteOrm()==null){
            DbUtils.createDb(this, "jingzhi");
        }
        List<BloodPressureData> bloodPressureData =DbUtils.getQueryAll(BloodPressureData.class);
        Logger.d("BloodPressureData="+bloodPressureData);
        mBPDataBPALV.setAdapter(new CommonAdapter<BloodPressureData>(this,bloodPressureData,R.layout.listview_item_blood_pressure_measure_history_data) {
            @Override
            protected void convertView(View item, BloodPressureData bloodPressureData) {
                TextView tv_date= CommonViewHolder.get(item,R.id.id_item_textView_dataTimePressure_historyData);
                TextView tv_Max= CommonViewHolder.get(item,R.id.id_item_textView_dataMaxPressure_historyData); //收缩压
                TextView tv_min= CommonViewHolder.get(item,R.id.id_item_textView_dataMinPressure_historyData); //舒张压
                tv_date.setText(bloodPressureData.getToday());
                tv_Max.setText("收缩压" + bloodPressureData.getMaxPressure());
                tv_min.setText("舒张压" + bloodPressureData.getMinPressure());
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
