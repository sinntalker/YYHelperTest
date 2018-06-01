package com.sinntalker.sinntest20180503_yy.Fragment.family;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Config;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Toast;

import com.github.promeg.pinyinhelper.Pinyin;
import com.orhanobut.logger.Logger;
import com.sinntalker.sinntest20180503_yy.AllUserBean;
import com.sinntalker.sinntest20180503_yy.Fragment.family.adapter.ContactAdapter;
import com.sinntalker.sinntest20180503_yy.Fragment.family.adapter.IMutlipleItem;
import com.sinntalker.sinntest20180503_yy.Fragment.family.adapter.OnRecyclerViewListener;
import com.sinntalker.sinntest20180503_yy.Fragment.family.base.ParentWithNaviActivity;
import com.sinntalker.sinntest20180503_yy.Fragment.family.base.ParentWithNaviFragment;
import com.sinntalker.sinntest20180503_yy.Fragment.family.bean.Friend;
import com.sinntalker.sinntest20180503_yy.Fragment.family.db.NewFriendDao;
import com.sinntalker.sinntest20180503_yy.Fragment.family.event.RefreshEvent;
import com.sinntalker.sinntest20180503_yy.Fragment.family.model.UserModel;
import com.sinntalker.sinntest20180503_yy.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.litesuits.orm.db.utils.DataUtil.NULL;


public class FamilyFragment extends Fragment {

    @BindView(R.id.rc_view)
    RecyclerView rc_view;
    @BindView(R.id.sw_refresh)
    SwipeRefreshLayout sw_refresh;
    @BindView(R.id.id_addRelationShip_button_familyFragment)
    Button addRelation_btn;
    ContactAdapter adapter;
    LinearLayoutManager layoutManager;
    View rootView;
    private Toast toast;
    protected void runOnMain(Runnable runnable) {
        getActivity().runOnUiThread(runnable);
    }

//    @Override
//    protected String title() {
//        return "联系人";
//    }
//
//    @Override
//    public Object right() {
//        return R.drawable.base_action_bar_add_bg_selector;
//    }
//
//    @Override
//    public ParentWithNaviActivity.ToolBarListener setToolBarListener() {
//        return new ParentWithNaviActivity.ToolBarListener() {
//            @Override
//            public void clickLeft() {
//
//            }
//
//            @Override
//            public void clickRight() {
//                startActivity(AddFamilyMembersActivity.class, null);
//            }
//        };
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_conversation, container, false);
//        initNaviView();

        ButterKnife.bind(this, rootView);
        IMutlipleItem<Friend> mutlipleItem = new IMutlipleItem<Friend>() {

            @Override
            public int getItemViewType(int postion, Friend friend) {
                if (postion == 0) {
                    return ContactAdapter.TYPE_NEW_FRIEND;
                } else {
                    return ContactAdapter.TYPE_ITEM;
                }
            }

            @Override
            public int getItemLayoutId(int viewtype) {
                if (viewtype == ContactAdapter.TYPE_NEW_FRIEND) {
                    return R.layout.header_new_friend;
                } else {
                    return R.layout.item_contact;
                }
            }

            @Override
            public int getItemCount(List<Friend> list) {
                return list.size() + 1;
            }
        };

        Drawable family = getResources().getDrawable(R.drawable.family);
        family.setBounds(0, 0, 75, 75);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        addRelation_btn.setCompoundDrawables(family, null, null, null);//只放左边

        addRelation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), "添加家人", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getContext(), AddFamilyMembersActivity.class));
            }
        });

        adapter = new ContactAdapter(getActivity(), mutlipleItem, null);
        rc_view.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity());
        rc_view.setLayoutManager(layoutManager);
        sw_refresh.setEnabled(true);
        setListener();
        return rootView;
    }

    private void setListener() {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                sw_refresh.setRefreshing(true);
                query();
            }
        });
        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                if (position == 0) {//跳转到新朋友页面
//                    startActivity(NewFriendActivity.class, null);
                    startActivity(new Intent(getContext(), NewFriendActivity.class));
                } else {
                    Friend friend = adapter.getItem(position);
                    AllUserBean user = friend.getFriendUser();
                    BmobIMUserInfo info = new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getUserAvatar());
                    //TODO 会话：4.1、创建一个常态会话入口，好友聊天
                    BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(info, null);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("c", conversationEntrance);
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), ChatActivity.class);
                    if (bundle != null)
                        intent.putExtra(getActivity().getPackageName(), bundle);
                    getActivity().startActivity(intent);
//                    startActivity(ChatActivity.class, bundle);
                }
            }

            @Override
            public boolean onItemLongClick(final int position) {
                log("长按" + position);
                if (position == 0) {
                    return true;
                }

                Friend friend = adapter.getItem(position);

                UserModel.getInstance().deleteFriend(friend,
                        new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    toast("好友删除成功");
                                    adapter.remove(position);
                                } else {
                                    toast("好友删除失败：" + e.getErrorCode() + ",s =" + e.getMessage());
                                }
                            }
                        });


                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        sw_refresh.setRefreshing(true);
        query();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     * 注册自定义消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
        //重新刷新列表
        log("---联系人界面接收到自定义消息---");
        adapter.notifyDataSetChanged();
    }

    /**
     * 查询本地会话
     */
    public void query() {
        UserModel.getInstance().queryFriends(

                new FindListener<Friend>() {
                    @Override
                    public void done(List<Friend> list, BmobException e) {
                        if (e == null) {
                            List<Friend> friends = new ArrayList<>();
                            friends.clear();
                            //添加首字母
                            for (int i = 0; i < list.size(); i++) {
                                Friend friend = list.get(i);
                                String username = friend.getFriendUser().getUsername();
                                if (username != null) {
                                    String pinyin = Pinyin.toPinyin(username.charAt(0));
                                    friend.setPinyin(pinyin.substring(0, 1).toUpperCase());
                                    friends.add(friend);
                                }
                            }
                            adapter.bindDatas(friends);
                            adapter.notifyDataSetChanged();
                            sw_refresh.setRefreshing(false);
                        } else {
                            adapter.bindDatas(null);
                            adapter.notifyDataSetChanged();
                            sw_refresh.setRefreshing(false);
                            Logger.e(String.valueOf(e));
                        }
                    }
                }


        );
    }

    /**Log日志
     * @param msg
     */
    public void log(String msg){
        if(Config.DEBUG){
            Logger.i(msg);
        }
    }

    public void toast(final Object obj) {
        try {
            runOnMain(new Runnable() {

                @Override
                public void run() {
                    if (toast == null)
                        toast = Toast.makeText(getActivity(), NULL,Toast.LENGTH_SHORT);
                    toast.setText(obj.toString());
                    toast.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }






//    private Button addRelation_btn;
//    private Button newFriend_btn;
//
//    public FamilyFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_family, container, false);
//
//        //添加好友图标大小
//        Button addFamilyMember_btn = view.findViewById(R.id.addRelationShip_button);
//        newFriend_btn = view.findViewById(R.id.id_button_newFriend_familyFragment);
//
//        Drawable family = getResources().getDrawable(R.drawable.family);
//        family.setBounds(0, 0, 75, 75);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
//        addFamilyMember_btn.setCompoundDrawables(family, null, null, null);//只放左边
//
//        addRelation_btn = view.findViewById(R.id.addRelationShip_button);
//
//        addRelation_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Toast.makeText(getContext(), "添加家人", Toast.LENGTH_LONG).show();
//                startActivity(new Intent(getContext(), AddFamilyMembersActivity.class));
//            }
//        });
//
//        newFriend_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getContext(), NewFriendActivity.class));
//            }
//        });
//
//        return view;
//    }


}
