package com.sinntalker.sinntest20180503_yy.Fragment.family.adapter;

/**
 *
 * 为RecycleView添加点击事件
 *
 * Created by Administrator on 2018/5/31.
 */

public interface OnRecyclerViewListener {

    void onItemClick(int position);
    boolean onItemLongClick(int position);

}
