package com.sinntalker.sinntest20180503_yy.Fragment.family.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.sinntalker.sinntest20180503_yy.AllUserBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/31.
 */

public class SearchUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<AllUserBean> users = new ArrayList<>();

    public SearchUserAdapter() {
    }

    public void setDatas(List<AllUserBean> list) {
        users.clear();
        if (null != list) {
            users.addAll(list);
        }
    }

    /**获取用户
     * @param position
     * @return
     */
    public AllUserBean getItem(int position){
        return users.get(position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchUserHolder(parent.getContext(), parent, onRecyclerViewListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BaseViewHolder)holder).bindData(users.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
