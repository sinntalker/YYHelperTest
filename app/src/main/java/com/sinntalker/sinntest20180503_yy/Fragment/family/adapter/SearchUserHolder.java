package com.sinntalker.sinntest20180503_yy.Fragment.family.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sinntalker.sinntest20180503_yy.AllUserBean;
import com.sinntalker.sinntest20180503_yy.Fragment.family.AddFriendInfoActivity;
import com.sinntalker.sinntest20180503_yy.Fragment.family.base.ImageLoaderFactory;
import com.sinntalker.sinntest20180503_yy.R;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/31.
 */

public class SearchUserHolder extends BaseViewHolder {

    @BindView(R.id.avatar)
    public ImageView avatar;
    @BindView(R.id.name)
    public TextView name;
    @BindView(R.id.btn_add)
    public Button btn_add;

    public SearchUserHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, R.layout.item_search_user,onRecyclerViewListener);
    }

    @Override
    public void bindData(Object o) {
        final AllUserBean user =(AllUserBean)o;
        ImageLoaderFactory.getLoader().loadAvator(avatar,user.getUserAvatar(), R.drawable.avatar);
        name.setText(user.getUserNick()); //显示用户昵称
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//查看个人详情
                Bundle bundle = new Bundle();
                bundle.putSerializable("u", user);
                startActivity(AddFriendInfoActivity.class,bundle);
            }
        });
    }
}