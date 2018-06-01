package com.sinntalker.sinntest20180503_yy.Fragment.family;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.sinntalker.sinntest20180503_yy.AllUserBean;
import com.sinntalker.sinntest20180503_yy.Fragment.DrugBox.AddDrugScanActivity;
import com.sinntalker.sinntest20180503_yy.Fragment.DrugBox.PublicDrugDetailActivity;
import com.sinntalker.sinntest20180503_yy.Fragment.family.adapter.OnRecyclerViewListener;
import com.sinntalker.sinntest20180503_yy.Fragment.family.adapter.SearchUserAdapter;
import com.sinntalker.sinntest20180503_yy.Fragment.family.base.BaseActivity;
import com.sinntalker.sinntest20180503_yy.Fragment.family.model.BaseModel;
import com.sinntalker.sinntest20180503_yy.Fragment.family.model.QueryUserListener;
import com.sinntalker.sinntest20180503_yy.Fragment.family.model.UserModel;
import com.sinntalker.sinntest20180503_yy.R;

import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class AddFamilyMembersActivity extends BaseActivity {

    ImageView mBackAFMAIV;
    EditText mPhoneSearchAFMAET;
    Button mPhoneSearchAFMABtn;
    ListView mFamilyMemListAFMALV;

    @BindView(R.id.sw_refresh)
    SwipeRefreshLayout sw_refresh;
    @BindView(R.id.rc_view)
    RecyclerView rc_view;

    SearchUserAdapter adapter;
    LinearLayoutManager layoutManager;

    String strPhoneSearch = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_add_family_members);

        mBackAFMAIV = findViewById(R.id.id_imageView_back_addFamilyMembers);
        mPhoneSearchAFMAET = findViewById(R.id.id_editView_phoneSearch_addFamilyMembers);
        mPhoneSearchAFMABtn = findViewById(R.id.id_button_phoneSearch_addFamilyMembers);
        mFamilyMemListAFMALV = findViewById(R.id.id_listView_familyMembersList_addFamilyMembers);

        adapter = new SearchUserAdapter();
        layoutManager = new LinearLayoutManager(this);
        rc_view.setLayoutManager(layoutManager);
        rc_view.setAdapter(adapter);
        sw_refresh.setEnabled(true);
        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                AllUserBean user = adapter.getItem(position);
                bundle.putSerializable("u", user);
                startActivity(AddFriendInfoActivity.class, bundle, false);
            }

            @Override
            public boolean onItemLongClick(int position) {
                return true;
            }
        });

        mBackAFMAIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mPhoneSearchAFMABtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sw_refresh.setRefreshing(true);
                query();
//                strPhoneSearch = mPhoneSearchAFMAET.getText().toString().trim();
//
//                if (strPhoneSearch.length() == 0) {
//                    Toast.makeText(getApplicationContext(), "请输入添加好友手机号", Toast.LENGTH_SHORT).show();
//                } else {
//                    //查询当前输入用户手机号
//                    BmobQuery<AllUserBean> query = new BmobQuery<AllUserBean>();
//                    query.addWhereEqualTo("mobilePhoneNumber", strPhoneSearch);
//                    query.findObjects(new FindListener<AllUserBean>() {
//                        @Override
//                        public void done(List<AllUserBean> object, BmobException e) {
//                            if (e == null) {
//                                if (object.size() == 0) {
//                                    setEmptyView(mFamilyMemListAFMALV);
//                                } else {
//                                    final String [] nickName = new String[object.size()];
//                                    String [] avatar = new String[object.size()];
//
//                                    for (int i = 0; i < object.size(); i ++) {
//                                        nickName[i] = object.get(i).getUserNick();
//                                        avatar[i] = object.get(i).getUserAvatar();
//                                    }
//
//                                    class FriendFindListAdapter extends BaseAdapter {
//                                        private Context context;
//
//                                        public FriendFindListAdapter(Context context) {
//                                            this.context = context;
//                                        }
//
//                                        @Override
//                                        public int getCount() {
//                                            return nickName.length;
//                                        }
//
//                                        @Override
//                                        public Object getItem(int position) {
//                                            return nickName[position];
//                                        }
//
//                                        @Override
//                                        public long getItemId(int position) {
//                                            return position;
//                                        }
//
//                                        ViewHolder viewHolder;
//
//                                        @Override
//                                        public View getView(final int position, View convertView, ViewGroup parent) {
//                                            // 创建布局
//                                            convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_item_add_friend, parent, false);
//                                            viewHolder = new ViewHolder();
//                                            viewHolder.userAvatar = convertView.findViewById(R.id.id_imageView_avatar_addFriend);
//                                            viewHolder.userNickName = convertView.findViewById(R.id.id_textView_item_username_addFriend);
//                                            viewHolder.userDetail = convertView.findViewById(R.id.id_textView_item_add_addFriend);
//
//                                            viewHolder.userNickName.setText(nickName[position]);
//
//                                            viewHolder.userDetail.setOnClickListener(new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View v) {
////                                            Toast.makeText(getApplicationContext(), "设置提醒", Toast.LENGTH_LONG).show();
//                                                    Log.i("bmob", nickName[position] + " 用户详情");
//                                                    //打开用户详情页，并包含添加按键
//                                                    startActivity(new Intent(AddFamilyMembersActivity.this, AddFriendInfoActivity.class)
//                                                        .putExtra("nickname", nickName[position])
//                                                        .putExtra("phone", strPhoneSearch));
////                                                    startActivity(new Intent(getApplicationContext(), PublicDrugDetailActivity.class).putExtra("drug_genericName", drugName[position]));
//                                                }
//                                            });
//                                            return convertView;
//                                        }
//
//                                        class ViewHolder {
//                                            ImageView userAvatar; //好友头像
//                                            TextView userNickName; //好友名称
//                                            TextView userDetail; //好友信息
//                                        }
//
//                                    }
//
//                                    mFamilyMemListAFMALV.setAdapter(new FriendFindListAdapter(AddFamilyMembersActivity.this));
//                                }
//                            } else {
//                                Toast.makeText(AddFamilyMembersActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });

//                }
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

    public void query() {
        String strPhoneSearch = mPhoneSearchAFMAET.getText().toString();
        if (TextUtils.isEmpty(strPhoneSearch)) {
            Toast.makeText(AddFamilyMembersActivity.this, "请填写搜索账户手机号", Toast.LENGTH_SHORT).show();
            sw_refresh.setRefreshing(false);
            return;
        }

        BmobQuery<AllUserBean> query = new BmobQuery<>();
        query.addWhereEqualTo("mobilePhoneNumber", strPhoneSearch);
        query.findObjects(
                new FindListener<AllUserBean>() {
                    @Override
                    public void done(List<AllUserBean> list, BmobException e) {
                        if (e == null) {
                            sw_refresh.setRefreshing(false);
                            adapter.setDatas(list);
                            adapter.notifyDataSetChanged();
                        } else {
                            sw_refresh.setRefreshing(false);
                            adapter.setDatas(null);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(AddFamilyMembersActivity.this, "找不到对应的联系人", Toast.LENGTH_SHORT).show();
                            Logger.e(String.valueOf(e));
                        }
                    }
                });
    }
}
