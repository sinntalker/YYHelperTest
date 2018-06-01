package com.sinntalker.sinntest20180503_yy.Fragment.family;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Config;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.orhanobut.logger.Logger;
import com.sinntalker.sinntest20180503_yy.Fragment.family.base.ParentWithNaviActivity;
import com.sinntalker.sinntest20180503_yy.Fragment.family.db.NewFriend;
import com.sinntalker.sinntest20180503_yy.Fragment.family.db.NewFriendManager;
import com.sinntalker.sinntest20180503_yy.R;
import com.sinntalker.sinntest20180503_yy.Fragment.family.adapter.NewFriendAdapter;
import com.sinntalker.sinntest20180503_yy.Fragment.family.adapter.OnRecyclerViewListener;
import com.sinntalker.sinntest20180503_yy.Fragment.family.adapter.IMutlipleItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewFriendActivity extends Activity {

    @BindView(R.id.ll_root)
    LinearLayout ll_root;
    @BindView(R.id.rc_view)
    RecyclerView rc_view;
    @BindView(R.id.sw_refresh)
    SwipeRefreshLayout sw_refresh;
    @BindView(R.id.id_imageView_back_newFriend)
    ImageView mBackNFIV;
//    @BindView(R.id.id_addRelationShip_button_familyFragment)
//    Button addRelation_btn;
    NewFriendAdapter adapter;
    LinearLayoutManager layoutManager;

//    @Override
//    protected String title() {
//        return "新朋友";
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
//                finish();
//            }
//
//            @Override
//            public void clickRight() {
//                startActivity(AddFamilyMembersActivity.class,null);
//            }
//        };
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);
//        initNaviView();

        ButterKnife.bind(this);
        //单一布局
        IMutlipleItem<NewFriend> mutlipleItem = new IMutlipleItem<NewFriend>() {

            @Override
            public int getItemViewType(int position, NewFriend c) {
                return 0;
            }

            @Override
            public int getItemLayoutId(int viewtype) {
                return R.layout.item_new_friend;
            }

            @Override
            public int getItemCount(List<NewFriend> list) {
                return list.size();
            }
        };
        adapter = new NewFriendAdapter(this,mutlipleItem,null);
        rc_view.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this);
        rc_view.setLayoutManager(layoutManager);
        sw_refresh.setEnabled(true);
        //批量更新未读未认证的消息为已读状态
        NewFriendManager.getInstance(this).updateBatchStatus();
        setListener();
        mBackNFIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setListener(){
        ll_root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ll_root.getViewTreeObserver().removeGlobalOnLayoutListener(this);
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
                log("点击："+position);
            }

            @Override
            public boolean onItemLongClick(int position) {
                NewFriendManager.getInstance(NewFriendActivity.this).deleteNewFriend(adapter.getItem(position));
                adapter.remove(position);
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
    public void onPause() {
        super.onPause();
    }

    /**
     查询本地会话
     */
    public void query(){
        adapter.bindDatas(NewFriendManager.getInstance(this).getAllNewFriend());
        adapter.notifyDataSetChanged();
        sw_refresh.setRefreshing(false);
    }

    /**Log日志
     * @param msg
     */
    public void log(String msg){
        if(Config.DEBUG){
            Logger.i(msg);
        }
    }

}
