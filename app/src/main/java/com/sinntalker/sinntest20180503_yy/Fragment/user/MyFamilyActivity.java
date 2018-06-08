package com.sinntalker.sinntest20180503_yy.Fragment.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sinntalker.sinntest20180503_yy.AllUserBean;
import com.sinntalker.sinntest20180503_yy.Fragment.health.BloodPressure.AddDataBloodPressureActivity;
import com.sinntalker.sinntest20180503_yy.Fragment.health.BloodPressure.BloodPressureActivity;
import com.sinntalker.sinntest20180503_yy.Fragment.health.BloodSugar.AddDataBloodSugarActivity;
import com.sinntalker.sinntest20180503_yy.Fragment.health.BloodSugar.BloodSugerActivity;
import com.sinntalker.sinntest20180503_yy.Fragment.health.Weight.AddDataWeightActivity;
import com.sinntalker.sinntest20180503_yy.Fragment.health.Weight.WeightActivity;
import com.sinntalker.sinntest20180503_yy.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MyFamilyActivity extends Activity {

    ImageView mBackMFIV; // 返回
    TextView mAddMFTV; //添加
    Button mAddMFBtn; //添加
    ListView mMemberMFLV; //家人列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_my_family);

        mBackMFIV = findViewById(R.id.id_imageView_back_myFamily);
        mAddMFTV = findViewById(R.id.id_textView_add_myFamily);
        mAddMFBtn = findViewById(R.id.id_button_add_myFamily);
        mMemberMFLV = findViewById(R.id.id_listView_familyMember_myFamily);

        mBackMFIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAddMFTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyFamilyActivity.this, CreateFamilyMemberActivity.class));
            }
        });

        mAddMFBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyFamilyActivity.this, CreateFamilyMemberActivity.class));
            }
        });

        AllUserBean user = BmobUser.getCurrentUser(AllUserBean.class);
        BmobQuery<FamilyBean> queryFamily = new BmobQuery<FamilyBean>();
        queryFamily.addWhereEqualTo("user", user);
        queryFamily.findObjects(new FindListener<FamilyBean>() {
            @Override
            public void done(List<FamilyBean> list, BmobException e) {
                if (e == null) {
                    Log.i("bmob", "家庭成员查询成功，一共包括" + list.size() + "个家人。");

                    final String [] relations = new String [list.size()];
                    for (int i = 0; i < list.size(); i ++) {
                        relations[i] = list.get(i).getRelations();
                    }

                    class FamilyListAdapter extends BaseAdapter {
                        private Context context ;
                        public FamilyListAdapter(Context context){
                            this.context = context;
                        }

                        @Override
                        public int getCount() {
                            return relations.length;
                        }

                        @Override
                        public Object getItem(int position) {
                            return relations[position];
                        }

                        @Override
                        public long getItemId(int position) {
                            return position;
                        }
                        ViewHolder viewHolder;

                        @Override
                        public View getView(final int position, View convertView, ViewGroup parent) {
                            // 创建布局
                            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_myfamily, parent, false);
                            viewHolder = new ViewHolder();
                            viewHolder.avatar = convertView.findViewById(R.id.id_imageView_avatar_myFamilyListView);
                            viewHolder.relation = convertView.findViewById(R.id.id_textView_relation_myFamilyListView);
                            viewHolder.pressureRL = convertView.findViewById(R.id.relativeLayout_blood);
                            viewHolder.pressure = convertView.findViewById(R.id.blood);
                            viewHolder.sugarRL = convertView.findViewById(R.id.relativeLayout_sugar);
                            viewHolder.sugar = convertView.findViewById(R.id.sugar);
                            viewHolder.weightRL = convertView.findViewById(R.id.relativeLayout_weight);
                            viewHolder.weight = convertView.findViewById(R.id.weight);

                            viewHolder.avatar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.i("bmob", "点击了：viewHolder.avatar" + relations[position]);
                                    startActivity(new Intent(MyFamilyActivity.this, FamilyInfoActivity.class)
                                        .putExtra("family", relations[position]));
                                }
                            });
                            viewHolder.relation.setText(relations[position]);

                            viewHolder.pressureRL.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.i("bmob", "点击了：viewHolder.pressureRL" + relations[position]);
                                    startActivity(new Intent(MyFamilyActivity.this, BloodPressureActivity.class)
                                            .putExtra("family", relations[position]));
                                }
                            });

                            viewHolder.pressure.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.i("bmob", "点击了：viewHolder.pressure" + relations[position]);
                                    startActivity(new Intent(MyFamilyActivity.this, AddDataBloodPressureActivity.class)
                                            .putExtra("family", relations[position]));
                                }
                            });

                            viewHolder.sugarRL.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.i("bmob", "点击了：viewHolder.sugarRL" + relations[position]);
                                    startActivity(new Intent(MyFamilyActivity.this, BloodSugerActivity.class)
                                            .putExtra("family", relations[position]));
                                }
                            });

                            viewHolder.sugar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.i("bmob", "点击了：viewHolder.sugar" + relations[position]);
                                    startActivity(new Intent(MyFamilyActivity.this, AddDataBloodSugarActivity.class)
                                            .putExtra("family", relations[position]));
                                }
                            });

                            viewHolder.weightRL.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.i("bmob", "点击了：viewHolder.weightRL" + relations[position]);
                                    startActivity(new Intent(MyFamilyActivity.this, WeightActivity.class)
                                            .putExtra("family", relations[position]));
                                }
                            });

                            viewHolder.weight.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.i("bmob", "点击了：viewHolder.weight" + relations[position]);
                                    startActivity(new Intent(MyFamilyActivity.this, AddDataWeightActivity.class)
                                            .putExtra("family", relations[position]));
                                }
                            });

                            return convertView;
                        }

                        class ViewHolder{
                            ImageView avatar; //头像
                            TextView relation; //关系
                            RelativeLayout pressureRL; //血压
                            RelativeLayout sugarRL; //血糖
                            RelativeLayout weightRL; //体重
                            Button pressure;
                            Button sugar;
                            Button weight;
                        }
                    }

                    //设置listView的Adapter
                    mMemberMFLV.setAdapter(new FamilyListAdapter(MyFamilyActivity.this));

                } else {
                    Log.i("bmob", "家庭成员查询失败：" + e.getErrorCode() + e.getMessage());
                }
            }
        });

    }
}
