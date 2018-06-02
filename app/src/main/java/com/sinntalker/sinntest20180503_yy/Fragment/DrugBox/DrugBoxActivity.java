package com.sinntalker.sinntest20180503_yy.Fragment.DrugBox;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinntalker.sinntest20180503_yy.Activity.MainActivity;
import com.sinntalker.sinntest20180503_yy.Fragment.family.bean.Friend;
import com.sinntalker.sinntest20180503_yy.Fragment.family.model.UserModel;
import com.sinntalker.sinntest20180503_yy.Fragment.health.BloodPressure.BloodPressureData;
import com.sinntalker.sinntest20180503_yy.Fragment.health.StepCounter.CommonAdapter;
import com.sinntalker.sinntest20180503_yy.Fragment.health.StepCounter.CommonViewHolder;
import com.sinntalker.sinntest20180503_yy.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class DrugBoxActivity extends Activity {

    //声明控件
    ImageView mBackDBAIV; //返回
    TextView mTitleDBATV; //标题-- 药箱X
    ListView mDrugListDBALV; //药箱中药品列表
    TextView mCountDrugDBATV; //药箱中所有药品总数显示 :当前药箱中共有X种药品
    TextView mScanAddDrugDBABtn; //扫码添加药品
    TextView mHandyAddDrugDBABtn; //手动添加药品

    String strDrugBoxNum; //当前药箱编号 -- 由Intent传递
    String strUserName; //当前用户名称，由BmobUser获取
    String [] strDrugName; //药品名称字符串数组
    String [] strDrugDosage; //药品用法用量数组
    String [] strObjectId; //药品objectId -- 药品信息分两个表存储：第一个表（具有5个特殊信息）
    String [] strObjectIdCommon; //药品objectId -- 药品信息分两个表存储：第二个表（具有14个普通信息）
    int drugCount; //当前药箱中药品总数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_drug_box); //对应布局

        //实例化控件
        mBackDBAIV = findViewById(R.id.id_imageView_back_drugBox);
        mTitleDBATV = findViewById(R.id.id_textView_drugBoxNum_drugBox);
        mDrugListDBALV = findViewById(R.id.id_listView_drugList_drugBox);
        mCountDrugDBATV = findViewById(R.id.id_textView_drugCount_drugBox);
        mScanAddDrugDBABtn = findViewById(R.id.id_textView_scan_drugBox);
        mHandyAddDrugDBABtn = findViewById(R.id.id_textView_handy_drugBox);

        //获取当前药箱编号数据
        strDrugBoxNum = getIntent().getStringExtra("DrugBoxNum");
        Log.i("bmob", "药箱activity：当前药箱编号："+strDrugBoxNum);

        //设置活动窗口标题
        mTitleDBATV.setText("药箱 " + strDrugBoxNum);

        //扫描条形码添加药品 -- 打开对应的窗口、并传递数据，关闭当前窗口，以便于对应动作完成后能够自动刷新当前窗口数据
        mScanAddDrugDBABtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("bmob", "药箱activity：点击了扫一扫按键。");
                startActivity(new Intent(DrugBoxActivity.this, AddDrugScanActivity.class).putExtra("DrugBoxNum", strDrugBoxNum));
                finish();
            }
        });

        //手动添加药品 -- 打开对应的窗口、并传递数据，关闭当前窗口，以便于对应动作完成后能够自动刷新当前窗口数据
        mHandyAddDrugDBABtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("bmob", "药箱activity：点击了手动添加按键。");
                startActivity(new Intent(DrugBoxActivity.this, AddDrugHandyActivity.class).putExtra("DrugBoxNum", strDrugBoxNum));
                finish();
            }
        });

        //返回 -- 回退到MainActivity --> HomeFragment
        mBackDBAIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("bmob", "药箱activity：点击了返回按键。");
                finish();
            }
        });

        //向服务器请求数据
        //请求字符串： 当前用户、当前药箱
        //返回数据： 对应药品名称
        BmobUser mCurrentUser = BmobUser.getCurrentUser();
        strUserName = mCurrentUser.getUsername();
        Log.i("bmob", "DrugBoxActivity:当前用户：" + mCurrentUser.toString());
        Log.i("bmob", "DrugBoxActivity:当前用户名称：" + strUserName);

        RequestDrugListDataBmob(strUserName, strDrugBoxNum);

    }

    public void RequestDrugListDataBmob(final String username, final String drugBoxNum) {
        //查询条件一：用户名称
        BmobQuery<DrugDataBean> queryUserName = new BmobQuery<DrugDataBean>();
        queryUserName.addWhereEqualTo("userName",username);

        //查询条件二：药箱编号
        BmobQuery<DrugDataBean> queryDrugBoxNum = new BmobQuery<DrugDataBean>();
        queryDrugBoxNum.addWhereEqualTo("boxNumber",drugBoxNum);

        //最后查询时完整的条件
        List<BmobQuery<DrugDataBean>> allQueries = new ArrayList<BmobQuery<DrugDataBean>>();
        allQueries.add(queryUserName);
        allQueries.add(queryDrugBoxNum);

        //查询
        BmobQuery<DrugDataBean> query = new BmobQuery<DrugDataBean>();
        query.and(allQueries);
        query.findObjects(new FindListener<DrugDataBean>() {
            @Override
            public void done(List<DrugDataBean> object, BmobException e) {
                if (e == null) {
                    Log.i("bmob", "DrugBoxActivity:查询药箱数据成功，共返回 " + object.size() + " 条数据。");
                    //获取药品名称
                    strDrugName = new String [object.size()];
                    strDrugDosage = new String[object.size()];
                    strObjectId = new String[object.size()];
                    for (int i = 0; i < object.size(); i ++) {
                        strDrugName[i] = object.get(i).getGenericName();
                        strDrugDosage[i] = object.get(i).getDosage();
                        strObjectId[i] = object.get(i).getObjectId();
                        Log.i("bmob", "DrugBoxActivity:第 "+ i + " 条数据： " + strDrugName[i]);
                    }

                    Log.i("bmob", "DrugBoxActivity:提取数据结束");

                    drugCount = object.size();

                    mCountDrugDBATV.setText("当前共有 " + drugCount + " 种药品");

                    class DrugListAdapter extends BaseAdapter {
                        private Context context ;
                        public DrugListAdapter(Context context){
                            this.context = context;
                        }

                        @Override
                        public int getCount() {
                            return strDrugName.length;
                        }

                        @Override
                        public Object getItem(int position) {
                            return strDrugName[position];
                        }

                        @Override
                        public long getItemId(int position) {
                            return position;
                        }
                        ViewHolder viewHolder;

                        @Override
                        public View getView(final int position, View convertView, ViewGroup parent) {
                            // 创建布局
                            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_drug, parent, false);
                            viewHolder = new ViewHolder();
                            viewHolder.drugName = convertView.findViewById(R.id.drug_name_textView);
                            viewHolder.drugEdit = convertView.findViewById(R.id.edit_drug_image);
                            viewHolder.drugInfo = convertView.findViewById(R.id.info_drug_image);
                            viewHolder.drugSetAlarm = convertView.findViewById(R.id.settingAlarm_button);

                            viewHolder.drugName.setText(strDrugName[position]);

                            viewHolder.drugEdit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.i("bmob","DrugBoxActivity：药品编辑：" + strDrugName[position]);
                                    startActivity(new Intent(DrugBoxActivity.this, DrugEditActivity.class)
                                            .putExtra("drug_boxNum", strDrugBoxNum)
                                            .putExtra("drug_genericName", strDrugName[position])
                                    );
                                    finish();
                                }
                            });

                            viewHolder.drugInfo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.i("bmob","DrugBoxActivity：药品详情：" + strDrugName[position]);
//                                    Intent intent = new Intent(DrugBoxActivity.this, DrugDetailsActivity.class); //打开药品详细信息页面
//                                    intent.putExtra("drug_boxNum", strDrugBoxNum);//当前药箱 boxNum
//                                    intent.putExtra("drug_genericName", strDrugName[position]);//药品通用名称 genericName
//                                    getApplicationContext().startActivity(intent);
                                    startActivity(new Intent(DrugBoxActivity.this, DrugDetailsActivity.class)
                                        .putExtra("drug_boxNum", strDrugBoxNum)
                                        .putExtra("drug_genericName", strDrugName[position])
                                    );
                                    finish();
                                }
                            });

                            viewHolder.drugSetAlarm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.i("bmob","DrugBoxActivity：药品设置提醒：" + strDrugName[position]);
//                                    Intent intent = new Intent(DrugBoxActivity.this, SetAlarmActivity.class); //打开药品设置提醒页面
//                                    intent.putExtra("drug_boxNum", strDrugBoxNum);//当前药箱 boxNum
//                                    intent.putExtra("drug_genericName", strDrugName[position]);//药品通用名称 genericName
//                                    intent.putExtra("drug_dosage", strDrugDosage[position]);//药品通用名称 genericName
//                                    getApplicationContext().startActivity(intent);
                                    startActivity(new Intent(DrugBoxActivity.this, SetAlarmActivity.class)
                                        .putExtra("drug_boxNum", strDrugBoxNum)
                                        .putExtra("drug_genericName", strDrugName[position])
                                        .putExtra("drug_dosage", strDrugDosage[position])
                                    );
                                    finish();
                                }
                            });

                            viewHolder.drugName.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    Log.i("bmob", "长按删除：" + strDrugName[position]);

                                    AlertDialog dialog = new AlertDialog.Builder(DrugBoxActivity.this)
//                                            .setIcon(R.mipmap.icon)//设置标题的图片
                                            .setTitle("温馨提示")//设置对话框的标题
                                            .setMessage("您确定要删除该药品吗？")//设置对话框的内容
                                            //设置对话框的按钮
                                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
//                                                    Toast.makeText(DrugBoxActivity.this, "点击了取消按钮", Toast.LENGTH_SHORT).show();
                                                    Log.i("bmob", "点击了取消按钮，不删除该药品");
                                                    RequestDrugListDataBmob(strUserName, strDrugBoxNum);
                                                    dialog.dismiss();
                                                }
                                            })
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
//                                                    Toast.makeText(DrugBoxActivity.this, "点击了确定的按钮", Toast.LENGTH_SHORT).show();
                                                    Log.i("bmob", "点击了确定的按钮，删除该药品");
                                                    //查询条件一：用户名称
                                                    BmobQuery<DrugCommonDataBean> queryUserName_common = new BmobQuery<DrugCommonDataBean>();
                                                    queryUserName_common.addWhereEqualTo("userName",strUserName);

                                                    Log.i("bmob","查询名称：" + strUserName);

                                                    //查询条件二：药箱编号
                                                    BmobQuery<DrugCommonDataBean> queryDrugBoxNum_common = new BmobQuery<DrugCommonDataBean>();
                                                    queryDrugBoxNum_common.addWhereEqualTo("boxNumber",strDrugBoxNum);

                                                    Log.i("bmob","查询药箱编号：" + strDrugBoxNum);

                                                    //查询条件三：药箱编号
                                                    BmobQuery<DrugCommonDataBean> queryDrugName_common = new BmobQuery<DrugCommonDataBean>();
                                                    queryDrugName_common.addWhereEqualTo("genericName",strDrugName[position]);

                                                    Log.i("bmob","查询药品名称：" + strDrugName[position]);

                                                    //最后查询时完整的条件
                                                    List<BmobQuery<DrugCommonDataBean>> allQueries = new ArrayList<BmobQuery<DrugCommonDataBean>>();
                                                    allQueries.add(queryUserName_common);
                                                    allQueries.add(queryDrugBoxNum_common);
                                                    allQueries.add(queryDrugName_common);

                                                    //查询
                                                    BmobQuery<DrugCommonDataBean> query = new BmobQuery<DrugCommonDataBean>();
                                                    query.and(allQueries);
                                                    query.findObjects(new FindListener<DrugCommonDataBean>() {
                                                        @Override
                                                        public void done(List<DrugCommonDataBean> object, BmobException e) {
                                                            if (e == null) {
                                                                Log.i("bmob", "DrugCommonDataBean查询成功，共 " + object.size() + " 条结果");

                                                                strObjectIdCommon = new String[object.size()];

                                                                for (int i = 0; i < object.size(); i ++) {
                                                                    strObjectIdCommon[i] = object.get(i).getObjectId();
                                                                    Log.i("bmob","设置strObjectIdCommon ObjectID成功，数据为：" + strObjectIdCommon[i]);
                                                                    DrugCommonDataBean drugCommonDataBean = new DrugCommonDataBean();
                                                                    drugCommonDataBean.setObjectId(strObjectIdCommon[0]);
                                                                    drugCommonDataBean.delete(new UpdateListener() {

                                                                        @Override
                                                                        public void done(BmobException e) {
                                                                            if(e == null){
                                                                                Log.i("bmob","DrugCommonDataBean数据表删除成功");
                                                                            }else{
                                                                                Log.i("bmob","DrugCommonDataBean数据表删除失败："+e.getMessage()+","+e.getErrorCode());
                                                                            }
                                                                        }
                                                                    });
                                                                }

                                                            } else {
                                                                Log.i("bmob", "查询失败，错误码：" + e.getMessage() + e.getErrorCode());
                                                                Log.i("bmob","设置strObjectIdCommon ObjectID失败");
                                                            }
                                                        }
                                                    });

                                                    Log.i("bmob","开始删除该药品在DrugDataBean和DrugCommonDataBean数据表中的数据");

                                                    DrugDataBean drugDataBean = new DrugDataBean();
                                                    drugDataBean.setObjectId(strObjectId[position]);
                                                    drugDataBean.delete(new UpdateListener() {

                                                        @Override
                                                        public void done(BmobException e) {
                                                            if(e == null){
                                                                Log.i("bmob","DrugDataBean数据表删除成功");
                                                            }else{
                                                                Log.i("bmob","DrugDataBean数据表删除失败："+e.getMessage()+","+e.getErrorCode());
                                                            }
                                                        }
                                                    });
                                                    RequestDrugListDataBmob(strUserName, strDrugBoxNum);
                                                    dialog.dismiss();
                                                }
                                            }).create();
                                    dialog.show();

                                    return true;
                                }
                            });

                            return convertView;
                        }

                        class ViewHolder{
                            TextView drugName;  //List 药品名称
                            ImageView drugEdit; //编辑药品信息
                            ImageView drugInfo; //List 药品详情
                            Button drugSetAlarm; //List 药品设置提醒
                        }
                    }

                    //设置listView的Adapter
                    mDrugListDBALV.setAdapter(new DrugListAdapter(DrugBoxActivity.this));

                } else {
                    Log.i("bmob", "DrugBoxActivity:查询药箱数据失败，错误原因：" + e.getErrorCode());
                }
            }
        });
    }
}
