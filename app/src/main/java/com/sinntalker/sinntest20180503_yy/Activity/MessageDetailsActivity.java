package com.sinntalker.sinntest20180503_yy.Activity;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sinntalker.sinntest20180503_yy.AllUserBean;
import com.sinntalker.sinntest20180503_yy.Fragment.DrugBox.DrugDataBean;
import com.sinntalker.sinntest20180503_yy.Fragment.family.all.BmobIMApplication;
import com.sinntalker.sinntest20180503_yy.Fragment.health.StepCounter.CommonAdapter;
import com.sinntalker.sinntest20180503_yy.Fragment.health.StepCounter.CommonViewHolder;
import com.sinntalker.sinntest20180503_yy.R;
import com.sinntalker.sinntest20180503_yy.Sql.MessageDataBean;
import com.sinntalker.sinntest20180503_yy.Sql.util.MessageDataBeanDao;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class MessageDetailsActivity extends Activity {

    ImageView mBackMDIV;
    TextView mMessageTypeMDTV;

    CommonAdapter<MessageDataBean> adapter;
    CommonAdapter<DrugDataBean> adapterDrugDataBean;
    CommonViewHolder viewHolder;

    ListView mAllMDLV;

    private List<MessageDataBean> mDatas;
    private List<DrugDataBean> mDrugDataBeanDatas;
    AllUserBean user = BmobUser.getCurrentUser(AllUserBean.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_message_details);

        int messageTypeId = (getIntent().getIntExtra("MessageType",0));

        mBackMDIV = findViewById(R.id.id_imageView_back_messageDetail);
        mMessageTypeMDTV = findViewById(R.id.id_textView_messageType_messageDetail);

        mAllMDLV = findViewById(R.id.id_list_item_message_details);

        init(messageTypeId);

        mBackMDIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initDate_drugUsingHistory() {
        BmobQuery<MessageDataBean> query = new BmobQuery<MessageDataBean>();
        query.addWhereEqualTo("user", user.getUsername());
        query.findObjects(new FindListener<MessageDataBean>() {
            @Override
            public void done(List<MessageDataBean> list, BmobException e) {
                if (e == null) {
                    mDatas = list;
                    Log.i("bmob","MessageDataBean查询成功，mDatas获得数据：" + mDatas.size());
                    if (list.size() == 0) {
                        setEmptyView(mAllMDLV);
                    } else {
                        adapter = new CommonAdapter<MessageDataBean>(MessageDetailsActivity.this, mDatas, R.layout.listview_drug_using) {
                            @Override
                            public MessageDataBean getItem(int position) {
                                return super.getItem(position);
                            }

                            @Override
                            protected void convertView(View item, MessageDataBean messageDataBean) {
                                LinearLayout mLayout = CommonViewHolder.get(item, R.id.id_layout_linearLayout_drugUsing);
                                ImageView mIsReadIV = CommonViewHolder.get(item, R.id.id_contact_tips);
                                TextView mDateTV = CommonViewHolder.get(item, R.id.id_date);
                                TextView mMsgTV = CommonViewHolder.get(item, R.id.id_msg);
                                final boolean[] touched = {messageDataBean.getIsTouched()};
                                final String objectId = messageDataBean.getObjectId();

                                String createDate = messageDataBean.getCreatedAt();
                                mDateTV.setText(createDate);

                                mMsgTV.setText("该吃" + messageDataBean.getDrug() + "药了，用法：" + messageDataBean.getDosage() + "。");
                                if (touched[0]) {
                                    mIsReadIV.setVisibility(View.GONE);
                                } else {
                                    mIsReadIV.setVisibility(View.VISIBLE);
                                }
                                mLayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
//                                messageDataBean.setIsTouched(true);
//                                mIsReadIV.setVisibility(View.GONE);
                                        if (!touched[0]) {
                                            touched[0] = true;
                                            //当前时间
                                            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
//                String comment = "Added on " + df.format(new Date());
                                            String date = df.format(new Date());
                                            MessageDataBean updateMessageDate = new MessageDataBean();
                                            updateMessageDate.setIsTouched(true);
                                            updateMessageDate.setTouchedTime(date);
                                            updateMessageDate.update(objectId, new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if(e==null){
                                                        Log.i("bmob","updateMessageDate更新成功");
                                                        initDate_drugUsingHistory();
                                                    }else{
                                                        Log.i("bmob","updateMessageDate更新失败："+e.getMessage()+","+e.getErrorCode());
                                                        initDate_drugUsingHistory();
                                                    }
                                                }
                                            });

                                        }
                                    }
                                });
                            }
                        };

                        mAllMDLV.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Log.i("bmob","MessageDataBean查询失败，mDatas未获得数据："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    private void init(int Id){
        if(Id == 0) {
            mMessageTypeMDTV.setText("用药消息");
            //向服务器请求信息
            mDatas = new ArrayList<MessageDataBean>();
            initDate_drugUsingHistory();

        }else if(Id == 1) {
            mMessageTypeMDTV.setText("健康消息");
            //向服务器请求信息
            setEmptyView(mAllMDLV);

        }else if(Id == 2) {
            mMessageTypeMDTV.setText("药品过期提醒");
            //向服务器请求信息
            mDrugDataBeanDatas = new ArrayList<DrugDataBean>();
            initDate_drugValited();

        }else if(Id == 3) {
            mMessageTypeMDTV.setText("添加消息");
            //向服务器请求信息

        }else if(Id == 4) {
            mMessageTypeMDTV.setText("系统消息");
            //向服务器请求信息
            setEmptyView(mAllMDLV);
        }else {
            mMessageTypeMDTV.setText("用药消息");
            //向服务器请求信息
            mDatas = new ArrayList<MessageDataBean>();
            initDate_drugUsingHistory();

        }
    }

    private void initDate_drugValited() {
        BmobQuery<DrugDataBean> query = new BmobQuery<>();
        query.addWhereEqualTo("userName", user.getUsername());
        query.findObjects(new FindListener<DrugDataBean>() {
            @Override
            public void done(List<DrugDataBean> list, BmobException e) {
                if (e == null) {
                    Log.i("bmob", "查询药品保质期信息成功。");
                    if (list.size() == 0) {
                        setEmptyView(mAllMDLV);
//                        mDrugValidityMessageNewTV.setText("药品箱中没有药品。");
                    } else {
                        mDrugDataBeanDatas = list;
                        adapterDrugDataBean = new CommonAdapter<DrugDataBean>(MessageDetailsActivity.this, mDrugDataBeanDatas, R.layout.listview_item_drug_valued) {
                            @Override
                            public DrugDataBean getItem(int position) {
                                return super.getItem(position);
                            }

                            @Override
                            protected void convertView(View item, DrugDataBean drugDataBean) {
                                TextView drugName = CommonViewHolder.get(item, R.id.id_drugName);
                                TextView drugBox = CommonViewHolder.get(item, R.id.id_drugBox);
                                TextView productionDate = CommonViewHolder.get(item, R.id.id_productionDate);
                                TextView valuedDay = CommonViewHolder.get(item, R.id.id_valuedDay);
                                TextView deadline = CommonViewHolder.get(item, R.id.id_deadline);

                                drugName.setText(drugDataBean.getGenericName());
                                drugBox.setText(drugDataBean.getBoxNumber());
                                productionDate.setText(drugDataBean.getProductionDate());
                                valuedDay.setText(drugDataBean.getValidityPeriod());
                                try {
                                    deadline.setText(drugDataBean.getDeadDay());
                                } catch (Exception err) {
                                    err.printStackTrace();
                                }

                            }
                        };

                        mAllMDLV.setAdapter(adapterDrugDataBean);
                        adapterDrugDataBean.notifyDataSetChanged();
                    }
                } else {
                    setEmptyView(mAllMDLV);
                    Log.i("bmob", "查询药品保质期信息失败：" + e.getMessage() + e.getErrorCode());
                }
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

    public MessageDataBeanDao getMessageDataBeanDao() {
        // 通过 MyApplication 类提供的 getDaoSession() 获取具体 Dao
        return ((BmobIMApplication) this.getApplicationContext()).getDaoSession().getMessageDataBeanDao();
    }

    public SQLiteDatabase getDb() {
        // 通过 MyApplication 类提供的 getDb() 获取具体 db
        return ((BmobIMApplication) this.getApplicationContext()).getDb();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        initDate_drugUsingHistory();
//        mDatas = new ArrayList<MessageDataBean>();
//        //查询 并且出生在1970的用户
//        Query query = getMessageDataBeanDao().queryBuilder()
//                .where(MessageDataBeanDao.Properties.User.eq(user.getUsername()))
//                .orderAsc(MessageDataBeanDao.Properties.Id)
//                .build();
//        mDatas = query.list();
    }
}
