package com.sinntalker.sinntest20180503_yy.Fragment.health;

import android.app.Activity;
import android.content.Context;
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

import com.sinntalker.sinntest20180503_yy.AllUserBean;
import com.sinntalker.sinntest20180503_yy.Fragment.DrugBox.DrugUsingDataBean;
import com.sinntalker.sinntest20180503_yy.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class DrugUsingActivity extends Activity {

    ImageView mBackDUIV; //返回
    ListView mDrugUsingLV;

    DrugUsingAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_drug_using);

        initView();
        initClik();

        AllUserBean mCurrent = BmobUser.getCurrentUser(AllUserBean.class);

        BmobQuery<DrugUsingDataBean> query = new BmobQuery<DrugUsingDataBean>();
        query.addWhereEqualTo("userName", mCurrent.getMobilePhoneNumber());
        query.findObjects(new FindListener<DrugUsingDataBean>() {
            @Override
            public void done(List<DrugUsingDataBean> list, BmobException e) {
                if (e == null) {
                    Log.i("bmob", "用药信息查询成功。共" + list.size() + "条数据。");
                    if (list.size() == 0) {
                        setEmptyView(mDrugUsingLV);
                    } else {
                        adapter = new DrugUsingAdapter(DrugUsingActivity.this, list);
                        mDrugUsingLV.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Log.i("bmob", "用药信息查询失败，" + e.getMessage() + e.getErrorCode());
                }
            }
        });
    }

    public void initView() {
        mBackDUIV = findViewById(R.id.id_imageView_back_drugUsing);
        mDrugUsingLV = findViewById(R.id.id_listView_using_drugUsing);
    }

    public void initClik() {
        mBackDUIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    class DrugUsingAdapter extends BaseAdapter {
        private Context context ;
        private List<DrugUsingDataBean> list;
        public DrugUsingAdapter(Context context){
            this.context = context;
        }
        public DrugUsingAdapter(Context context, List<DrugUsingDataBean> list){
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // 创建布局
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_view_drug_using_history, parent, false);
//                            viewHolder = new ViewHolder();
//                            viewHolder.time = convertView.findViewById(R.id.id_item_textView_dataTime_historyData);
//                            viewHolder.weight = convertView.findViewById(R.id.id_item_textView_dataStep_historyData);

//                            TextView tv_date= CommonViewHolder.get(item,R.id.id_item_textView_dataTimePressure_historyData);
//                            TextView tv_Max= CommonViewHolder.get(item,R.id.id_item_textView_dataMaxPressure_historyData);
//                            TextView tv_min= CommonViewHolder.get(item,R.id.id_item_textView_dataMinPressure_historyData);
            TextView drugName= convertView.findViewById(R.id.drugName);  //药品名
            TextView date= convertView.findViewById(R.id.date); //时间
            TextView usage= convertView.findViewById(R.id.usage); //用量

            drugName.setText(list.get(position).getGenericName());
            date.setText(list.get(position).getCreatedAt());
            usage.setText("每次" + list.get(position).getUsingDrugNumber() + "片");

            return convertView;
        }
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
