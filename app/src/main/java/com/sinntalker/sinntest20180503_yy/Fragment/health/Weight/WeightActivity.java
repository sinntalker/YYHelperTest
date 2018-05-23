package com.sinntalker.sinntest20180503_yy.Fragment.health.Weight;

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

public class WeightActivity extends Activity {

    //声明控件
    ImageView mBackWAIV;
    TextView mMessureHWATV;
    ListView mWTrendWALV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_weight);

        mBackWAIV = findViewById(R.id.id_imageView_back_weight);
        mMessureHWATV = findViewById(R.id.id_textView_measureHand_weight);
        mWTrendWALV = findViewById(R.id.id_listView_weightTrend_weight);

        mBackWAIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mMessureHWATV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddDataWeightActivity.class));
                finish();
            }
        });

        setEmptyView(mWTrendWALV);
        if(DbUtils.getLiteOrm()==null){
            DbUtils.createDb(this, "jingzhi");
        }
        List<WeightData> weightData =DbUtils.getQueryAll(WeightData.class);
        Logger.d("WeightData="+weightData);
        mWTrendWALV.setAdapter(new CommonAdapter<WeightData>(this,weightData,R.layout.listview_item_step_history_data) {
            @Override
            protected void convertView(View item, WeightData weightData) {
                TextView tv_date= CommonViewHolder.get(item,R.id.id_item_textView_dataTime_historyData);
                TextView tv_Max= CommonViewHolder.get(item,R.id.id_item_textView_dataStep_historyData);
                tv_date.setText(weightData.getTime());
                tv_Max.setText(weightData.getWeight()+"kg");
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
