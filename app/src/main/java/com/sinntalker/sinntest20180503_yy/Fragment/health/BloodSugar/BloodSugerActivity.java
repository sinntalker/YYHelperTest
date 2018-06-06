package com.sinntalker.sinntest20180503_yy.Fragment.health.BloodSugar;

import android.app.Activity;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.sinntalker.sinntest20180503_yy.AllUserBean;
import com.sinntalker.sinntest20180503_yy.Fragment.health.BloodPressure.BloodDataBean;
import com.sinntalker.sinntest20180503_yy.Fragment.health.BloodPressure.BloodPressureActivity;
import com.sinntalker.sinntest20180503_yy.Fragment.health.StepCounter.CommonAdapter;
import com.sinntalker.sinntest20180503_yy.Fragment.health.StepCounter.CommonViewHolder;
import com.sinntalker.sinntest20180503_yy.Fragment.health.StepCounter.DbUtils;
import com.sinntalker.sinntest20180503_yy.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

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

        AllUserBean mCurrentUser = BmobUser.getCurrentUser(AllUserBean.class);
        BmobQuery<SugarValueBean> query = new BmobQuery<SugarValueBean>();
        query.addWhereEqualTo("user", mCurrentUser);
        query.findObjects(new FindListener<SugarValueBean>() {
            @Override
            public void done(List<SugarValueBean> list, BmobException e) {
                if (e == null) {
                    Log.i("bmob", "查询成功，数据共有" + list.size() + "条");

                    final String[] strMax = new String[list.size()];
                    final String[] strMin = new String[list.size()];
                    final String[] strTime = new String[list.size()];

                    for (int i = 0; i < list.size(); i ++) {
                        strMax[i] = list.get(i).getDuring(); //收缩压
                        strMin[i] = list.get(i).getSugarValue(); //舒张压
                        strTime[i] = list.get(i).getSetTime();
                    }

                    class PressureDataAdapter extends BaseAdapter {
                        private Context context ;
                        public PressureDataAdapter(Context context){
                            this.context = context;
                        }

                        @Override
                        public int getCount() {
                            return strMax.length;
                        }

                        @Override
                        public Object getItem(int position) {
                            return strMax[position];
                        }

                        @Override
                        public long getItemId(int position) {
                            return position;
                        }
                        ViewHolder viewHolder;

                        @Override
                        public View getView(final int position, View convertView, ViewGroup parent) {
                            // 创建布局
                            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_blood_pressure_measure_history_data, parent, false);
//                            viewHolder = new ViewHolder();
//                            viewHolder.time = convertView.findViewById(R.id.id_item_textView_dataTime_historyData);
//                            viewHolder.weight = convertView.findViewById(R.id.id_item_textView_dataStep_historyData);

//                            TextView tv_date= CommonViewHolder.get(item,R.id.id_item_textView_dataTimePressure_historyData);
//                            TextView tv_Max= CommonViewHolder.get(item,R.id.id_item_textView_dataMaxPressure_historyData);
//                            TextView tv_min= CommonViewHolder.get(item,R.id.id_item_textView_dataMinPressure_historyData);
                            TextView tv_date= convertView.findViewById(R.id.id_item_textView_dataTimePressure_historyData);  //时间
                            TextView tv_Max= convertView.findViewById(R.id.id_item_textView_dataMaxPressure_historyData); //收缩压
                            TextView tv_min= convertView.findViewById(R.id.id_item_textView_dataMinPressure_historyData); //舒张压
                            tv_date.setText(strTime[position]);
                            tv_Max.setText("收缩压" + strMax[position]);
                            tv_min.setText("舒张压" + strMin[position]);

                            return convertView;
                        }

                        class ViewHolder{
//                            TextView time;  //测量体重时间
//                            TextView weight; //测量体重数据
                        }
                    }

                    if (list.size() == 0) {
                        setEmptyView(mBSTrendBSALV);
                    } else {
                        //设置listView的Adapter
                        mBSTrendBSALV.setAdapter(new PressureDataAdapter(BloodSugerActivity.this));
                    }

                } else {
                    Log.i("bmob", "查询失败，错误原因：" + e.getMessage() + e.getErrorCode());
                }
            }
        });

//        setEmptyView(mBSTrendBSALV);
//        AllUserBean mCurrentUser = BmobUser.getCurrentUser(AllUserBean.class);
//        BmobQuery<SugarValueBean> query = new BmobQuery<SugarValueBean>();
//        query.addWhereEqualTo("user", mCurrentUser);
//        query.findObjects(new FindListener<SugarValueBean>() {
//            @Override
//            public void done(List<SugarValueBean> list, BmobException e) {
//                if (e == null) {
//                    Log.i("bmob", "查询成功，数据共有" + list.size() + "条");
//                    if (list.size() == 0) {
//                        Log.i("bmob", "查询成功，数据不存在。");
//                    } else {
//                        Log.i("bmob", "查询成功，数据共有" + list.size() + "条");
//                    }
//
//                    //设定时间，血糖时间、血糖值
//                    final String[] strSetTime = new String[list.size()];
//                    final String[] strDuring = new String[list.size()];
//                    final String[] strSugar = new String[list.size()];
//
//                    for (int i = 0; i < list.size(); i ++) {
//                        strSetTime[i] = list.get(i).getSetTime(); //舒张压
//                        strDuring[i] = list.get(i).getDuring();
//                        strSugar[i] = list.get(i).getSugarValue();
//                    }
//
//                    class SugarDataAdapter extends BaseAdapter {
//                        private Context context ;
//                        public SugarDataAdapter(Context context){
//                            this.context = context;
//                        }
//
//                        @Override
//                        public int getCount() {
//                            return strSetTime.length;
//                        }
//
//                        @Override
//                        public Object getItem(int position) {
//                            return strSetTime[position];
//                        }
//
//                        @Override
//                        public long getItemId(int position) {
//                            return position;
//                        }
//                        ViewHolder viewHolder;
//
//                        @Override
//                        public View getView(final int position, View convertView, ViewGroup parent) {
//                            // 创建布局
//                            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_blood_pressure_measure_history_data, parent, false);
////                            viewHolder = new ViewHolder();
////                            viewHolder.time = convertView.findViewById(R.id.id_item_textView_dataTime_historyData);
////                            viewHolder.weight = convertView.findViewById(R.id.id_item_textView_dataStep_historyData);
//
////                            TextView tv_date= CommonViewHolder.get(item,R.id.id_item_textView_dataTimePressure_historyData);
////                            TextView tv_Max= CommonViewHolder.get(item,R.id.id_item_textView_dataMaxPressure_historyData);
////                            TextView tv_min= CommonViewHolder.get(item,R.id.id_item_textView_dataMinPressure_historyData);
//                            TextView tv_date= convertView.findViewById(R.id.id_item_textView_dataTimePressure_historyData);  //时间
//                            TextView tv_Max= convertView.findViewById(R.id.id_item_textView_dataMaxPressure_historyData); //收缩压
//                            TextView tv_min= convertView.findViewById(R.id.id_item_textView_dataMinPressure_historyData); //舒张压
//                            tv_date.setText(strSetTime[position]);
//                            tv_Max.setText(strDuring[position]);
//                            tv_min.setText("血糖 "  + strSugar[position]);
//
//                            return convertView;
//                        }
//
//                        class ViewHolder{
////                            TextView time;  //测量体重时间
////                            TextView weight; //测量体重数据
//                        }
//                    }
//
//                    if (list.size() == 0) {
//                        setEmptyView(mBSTrendBSALV);
//                    } else {
//                        //设置listView的Adapter
//                        mBSTrendBSALV.setAdapter(new SugarDataAdapter(BloodSugerActivity.this));
//                    }
//
//                } else {
//                    Log.i("bmob", "血糖值查询失败，错误原因：" + e.getMessage() + e.getErrorCode());
//                }
//            }
//        });

//        setEmptyView(mBSTrendBSALV);
//        if(DbUtils.getLiteOrm()==null){
//            DbUtils.createDb(this, "jingzhi");
//        }
//        List<BloodSugarData> bloodSugarData =DbUtils.getQueryAll(BloodSugarData.class);
//        Logger.d("BloodSugarData="+bloodSugarData);
//        mBSTrendBSALV.setAdapter(new CommonAdapter<BloodSugarData>(this,bloodSugarData,R.layout.listview_item_blood_pressure_measure_history_data) {
//            @Override
//            protected void convertView(View item, BloodSugarData bloodSugarData) {
//                TextView tv_date= CommonViewHolder.get(item,R.id.id_item_textView_dataTimePressure_historyData);
//                TextView tv_Max= CommonViewHolder.get(item,R.id.id_item_textView_dataMaxPressure_historyData); //收缩压
//                TextView tv_min= CommonViewHolder.get(item,R.id.id_item_textView_dataMinPressure_historyData); //舒张压
//                tv_date.setText(bloodSugarData.getTime());
//                tv_Max.setText(bloodSugarData.getDuring());
//                tv_min.setText("血糖 " + bloodSugarData.getSugarValue());
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
