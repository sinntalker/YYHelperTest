package com.sinntalker.sinntest20180503_yy.Fragment.health.Weight;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.sinntalker.sinntest20180503_yy.AllUserBean;
import com.sinntalker.sinntest20180503_yy.Fragment.DrugBox.DrugBoxActivity;
import com.sinntalker.sinntest20180503_yy.Fragment.DrugBox.DrugCommonDataBean;
import com.sinntalker.sinntest20180503_yy.Fragment.DrugBox.DrugDataBean;
import com.sinntalker.sinntest20180503_yy.Fragment.DrugBox.DrugDetailsActivity;
import com.sinntalker.sinntest20180503_yy.Fragment.DrugBox.DrugEditActivity;
import com.sinntalker.sinntest20180503_yy.Fragment.DrugBox.SetAlarmActivity;
import com.sinntalker.sinntest20180503_yy.Fragment.health.StepCounter.CommonAdapter;
import com.sinntalker.sinntest20180503_yy.Fragment.health.StepCounter.CommonViewHolder;
import com.sinntalker.sinntest20180503_yy.Fragment.health.StepCounter.DbUtils;
import com.sinntalker.sinntest20180503_yy.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class WeightActivity extends Activity {

    //声明控件
    ImageView mBackWAIV;
    TextView mMessureHWATV;
    ListView mWTrendWALV;

    List<WeightBean> weightData = null;

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

        AllUserBean mCurrentUser = BmobUser.getCurrentUser(AllUserBean.class);
        BmobQuery<WeightBean> query = new BmobQuery<WeightBean>();
        query.addWhereEqualTo("user", mCurrentUser);
        query.findObjects(new FindListener<WeightBean>() {
            @Override
            public void done(List<WeightBean> list, BmobException e) {
                if (e == null) {
                    Log.i("bmob", "query success. total data :" + list.size());
                    final String[] strWeight = new String[list.size()];
                    final String[] strTime = new String[list.size()];

                    for (int i = 0; i < list.size(); i ++) {
                        strWeight[i] = list.get(i).getWeight();
                        strTime[i] = list.get(i).getSetTime();
                    }

                    class WeightDataAdapter extends BaseAdapter {
                        private Context context ;
                        public WeightDataAdapter(Context context){
                            this.context = context;
                        }

                        @Override
                        public int getCount() {
                            return strWeight.length;
                        }

                        @Override
                        public Object getItem(int position) {
                            return strWeight[position];
                        }

                        @Override
                        public long getItemId(int position) {
                            return position;
                        }
                        ViewHolder viewHolder;

                        @Override
                        public View getView(final int position, View convertView, ViewGroup parent) {
                            // 创建布局
                            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_step_history_data, parent, false);
                            viewHolder = new ViewHolder();
                            viewHolder.time = convertView.findViewById(R.id.id_item_textView_dataTime_historyData);
                            viewHolder.weight = convertView.findViewById(R.id.id_item_textView_dataStep_historyData);

                            viewHolder.weight.setText(strWeight[position] + "kg");
                            viewHolder.time.setText(strTime[position]);

                            return convertView;
                        }

                        class ViewHolder{
                            TextView time;  //测量体重时间
                            TextView weight; //测量体重数据
                        }
                    }

                    if (list.size() == 0) {
                        setEmptyView(mWTrendWALV);
                    } else {
                        //设置listView的Adapter
                        mWTrendWALV.setAdapter(new WeightDataAdapter(WeightActivity.this));
                    }

                } else {
                    Log.i("bmob", "query failed. error message:" + e.getMessage() + e.getErrorCode());
                }
            }
        });
////        if(DbUtils.getLiteOrm()==null){
////            DbUtils.createDb(this, "jingzhi");
////        }
////        List<WeightData> weightData =DbUtils.getQueryAll(WeightData.class);
////        Logger.d("WeightData="+weightData);
////        mWTrendWALV.setAdapter(new CommonAdapter<WeightData>(this,weightData,R.layout.listview_item_step_history_data) {
////            @Override
////            protected void convertView(View item, WeightData weightData) {
////                TextView tv_date= CommonViewHolder.get(item,R.id.id_item_textView_dataTime_historyData);
////                TextView tv_Max= CommonViewHolder.get(item,R.id.id_item_textView_dataStep_historyData);
////                tv_date.setText(weightData.getTime());
////                tv_Max.setText(weightData.getWeight()+"kg");
////            }
////        });
//        mWTrendWALV.setAdapter(new CommonAdapter<WeightBean>(this, weightData, R.layout.listview_item_step_history_data) {
////        mWTrendWALV.setAdapter(new CommonAdapter<WeightBean>(this) {
//            @Override
//            protected void convertView(View item, WeightBean weightBean) {
////                TextView tv_date= CommonViewHolder.get(item,R.id.id_item_textView_dataTime_historyData);
////                TextView tv_Max= CommonViewHolder.get(item,R.id.id_item_textView_dataStep_historyData);
////                tv_date.setText(weightBean.getSetTime());
////                tv_Max.setText(weightBean.getWeight()+"kg");
//            }
//
//            @Override
//            public int getCount() {
//                Log.i("bmob", "当前数据个数：" + this.getCount());
//                return this.getCount();
//            }
//
//            @Override
//            public WeightBean getItem(int position) {
//                return super.getItem(position);
//            }
//
//            @Override
//            public long getItemId(int position) {
//                return super.getItemId(position);
//            }
//
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
////                convertView = LayoutInflater.from(Context).inflate(R.layout.listview_item_step_history_data, parent, false);
//                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_item_step_history_data, parent, false);
////                TextView tv_date= CommonViewHolder.get(item,R.id.id_item_textView_dataTime_historyData);
//                TextView tv_date= convertView.findViewById(R.id.id_item_textView_dataTime_historyData);
//                TextView tv_Max= convertView.findViewById(R.id.id_item_textView_dataStep_historyData);
//                tv_date.setText(weightData.get(position).getSetTime());
//                tv_Max.setText(weightData.get(position).getWeight()+"kg");
//
//                return convertView;
//            }
//        });
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
