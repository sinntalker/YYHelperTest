package com.sinntalker.sinntest20180503_yy.Fragment.health;

import android.app.Activity;
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
import com.sinntalker.sinntest20180503_yy.R;

import java.util.List;

public class StepHistoryDataActivity extends Activity {

    ImageView mBackSHAIV;
    ListView mHistoryDataSHALV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_step_history_data);

        mBackSHAIV = findViewById(R.id.id_imageView_back_historyData);
        mHistoryDataSHALV = findViewById(R.id.id_listView_historyDataShow_historyData);
//        mDataSHATV = findViewById(R.id.id_item_textView_dataTime_historyData);
//        mStepSHATV = findViewById(R.id.id_item_textView_dataStep_historyData);

        setEmptyView(mHistoryDataSHALV);
        if(DbUtils.getLiteOrm()==null){
            DbUtils.createDb(this, "jingzhi");
        }
        List<StepData> stepDatas =DbUtils.getQueryAll(StepData.class);
        Logger.d("stepDatas="+stepDatas);
        mHistoryDataSHALV.setAdapter(new CommonAdapter<StepData>(this,stepDatas,R.layout.listview_item_step_history_data) {
            @Override
            protected void convertView(View item, StepData stepData) {
                TextView tv_date= CommonViewHolder.get(item,R.id.id_item_textView_dataStep_historyData);
                TextView tv_step= CommonViewHolder.get(item,R.id.id_item_textView_dataStep_historyData);
                tv_date.setText(stepData.getToday());
                tv_step.setText(stepData.getStep()+"步");
            }
        });

        //返回
        mBackSHAIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
