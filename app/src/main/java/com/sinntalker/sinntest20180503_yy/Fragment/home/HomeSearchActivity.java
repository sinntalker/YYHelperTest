package com.sinntalker.sinntest20180503_yy.Fragment.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.sinntalker.sinntest20180503_yy.Fragment.DrugBox.DrugBoxActivity;
import com.sinntalker.sinntest20180503_yy.Fragment.DrugBox.DrugDataBean;
import com.sinntalker.sinntest20180503_yy.Fragment.health.StepCounter.CommonAdapter;
import com.sinntalker.sinntest20180503_yy.Fragment.health.StepCounter.CommonViewHolder;
import com.sinntalker.sinntest20180503_yy.Fragment.health.StepCounter.DbUtils;
import com.sinntalker.sinntest20180503_yy.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class HomeSearchActivity extends Activity {

    ImageView mBackHomeIV;
    EditText mSearchContextHomeET;
    TextView mSearchHomeTV;
    ListView mSearchContextHomeLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_home_search);

        String searchContext = getIntent().getStringExtra("searchContext");
//        String strUser = getIntent().getStringExtra("strUser");
//        final String strDrug = getIntent().getStringExtra("strDrug");
//        final String strBox = getIntent().getStringExtra("strBox");

        mBackHomeIV = findViewById(R.id.id_imageView_back_home);
        mSearchContextHomeET = findViewById(R.id.id_editText_searchContext_home);
        mSearchHomeTV = findViewById(R.id.id_textView_search_home);
        mSearchContextHomeLV = findViewById(R.id.id_listView_search_results_home);

        mBackHomeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HomeSearchData.class != null){
                    DbUtils.deleteAll(HomeSearchData.class);
                }
                finish();
            }
        });

        mSearchContextHomeET.setText(searchContext);

        mSearchHomeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("bmob","点击搜索activity中-搜索按键");
                DbUtils.deleteAll(HomeSearchData.class);

                List<HomeSearchData> mHomeSearchData =DbUtils.getQueryAll(HomeSearchData.class);
                Logger.d("SearchResults="+mHomeSearchData);
                mSearchContextHomeLV.setAdapter(new CommonAdapter<HomeSearchData>(HomeSearchActivity.this,mHomeSearchData,R.layout.listview_item_search_results) {
                    @Override
                    protected void convertView(View item, HomeSearchData mHomeSearchData) {
                        TextView tv_drug= CommonViewHolder.get(item,R.id.id_item_textView_search_results_name);
                        TextView tv_box= CommonViewHolder.get(item,R.id.id_item_textView_search_results_box_num);
                        tv_drug.setText(mHomeSearchData.getDrugName());
                        tv_box.setText(mHomeSearchData.getDrugBoxNum());
                    }
                });

                String search = mSearchContextHomeET.getText().toString().trim();
                BmobUser mCurrentUser = BmobUser.getCurrentUser();

                Log.i("bmob","当前用户为："+mCurrentUser.getUsername());
                Log.i("bmob","搜索内容为："+search);
                //根据用户名和药品名称查询数据
                //查询条件1 用户名
                BmobQuery<DrugDataBean> query_eq1 = new BmobQuery<DrugDataBean>();
                query_eq1.addWhereEqualTo("userName", mCurrentUser.getUsername());
                //查询条件2 药品名称
                BmobQuery<DrugDataBean> query_eq2 = new BmobQuery<DrugDataBean>();
                query_eq2.addWhereEqualTo("genericName", search);

                //最后组装完整的and条件
                List<BmobQuery<DrugDataBean>> queries = new ArrayList<BmobQuery<DrugDataBean>>();
                queries.add(query_eq1);
                queries.add(query_eq2);

                BmobQuery<DrugDataBean> query = new BmobQuery<DrugDataBean>();
                query.and(queries);
                query.findObjects(new FindListener<DrugDataBean>() {
                    @Override
                    public void done(List<DrugDataBean> list, BmobException e) {
                        if(e==null){
                            if (list.size() > 0) {
                                final String [] strUser = new String[list.size()];
                                final String [] strDrug = new String[list.size()];
                                final String [] strBox = new String[list.size()];

                                for (int i = 0; i < list.size(); i ++) {
                                    strUser[i] = list.get(i).getUserName().toString().trim();
                                    strDrug[i] = list.get(i).getGenericName().toString().trim();
                                    strBox[i] = list.get(i).getBoxNumber().toString().trim();
                                    Log.i("bmob","第"+i+"条搜索结果为："+strUser[i]);
                                    Log.i("bmob","第"+i+"条搜索结果为："+strDrug[i]);
                                    Log.i("bmob","第"+i+"条搜索结果为："+strBox[i]);

                                    HomeSearchData data = new HomeSearchData();
                                    data.setCurrentUser(String.valueOf(strUser[i]));
                                    data.setDrugName(String.valueOf(strDrug[i]));
                                    data.setDrugBoxNum(String.valueOf(strBox[i]));
                                    DbUtils.insert(data);
                                    Log.i("bmob","插入数据成功，strUser："+data.getCurrentUser());
                                    Log.i("bmob","插入数据成功，strDrug："+data.getDrugName());
                                    Log.i("bmob","插入数据成功，strBox："+data.getDrugBoxNum());
                                    Log.i("bmob","插入数据成功，data："+data);
                                    Log.i("bmob","搜索数据成功，结果："+list.size());

                                }
                            }else {

                            }
                            List<HomeSearchData> mHomeSearchData =DbUtils.getQueryAll(HomeSearchData.class);
                            Logger.d("SearchResults="+mHomeSearchData);
                            mSearchContextHomeLV.setAdapter(new CommonAdapter<HomeSearchData>(HomeSearchActivity.this,mHomeSearchData,R.layout.listview_item_search_results) {
                                @Override
                                protected void convertView(View item, final HomeSearchData mHomeSearchData) {
                                    TextView tv_drug= CommonViewHolder.get(item,R.id.id_item_textView_search_results_name);
                                    TextView tv_box= CommonViewHolder.get(item,R.id.id_item_textView_search_results_box_num);
                                    tv_drug.setText("药品名称： " + mHomeSearchData.getDrugName());
                                    tv_box.setText("所在药箱： " + mHomeSearchData.getDrugBoxNum());
                                    tv_drug.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
//                                    Toast.makeText(getApplicationContext(), "药品名称： " + mHomeSearchData.getDrugName()+ "所在药箱： " + mHomeSearchData.getDrugBoxNum(), Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), DrugBoxActivity.class).putExtra("DrugBoxNum", mHomeSearchData.getDrugBoxNum()));
                                        }
                                    });
                                }
                            });

                        }else{
                            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
            }
        });
        setEmptyView(mSearchContextHomeLV);
        if(DbUtils.getLiteOrm()==null){
            DbUtils.createDb(this, "jingzhi");
        }else {
            DbUtils.deleteAll(HomeSearchData.class);
        }

        String search = mSearchContextHomeET.getText().toString().trim();
        BmobUser mCurrentUser = BmobUser.getCurrentUser();

        Log.i("bmob","当前用户为："+mCurrentUser.getUsername());
        Log.i("bmob","搜索内容为："+search);
        //根据用户名和药品名称查询数据
        //查询条件1 用户名
        BmobQuery<DrugDataBean> query_eq1 = new BmobQuery<DrugDataBean>();
        query_eq1.addWhereEqualTo("userName", mCurrentUser.getUsername());
        //查询条件2 药品名称
        BmobQuery<DrugDataBean> query_eq2 = new BmobQuery<DrugDataBean>();
        query_eq2.addWhereEqualTo("genericName", search);

        //最后组装完整的and条件
        List<BmobQuery<DrugDataBean>> queries = new ArrayList<BmobQuery<DrugDataBean>>();
        queries.add(query_eq1);
        queries.add(query_eq2);

        BmobQuery<DrugDataBean> query = new BmobQuery<DrugDataBean>();
        query.and(queries);
        query.findObjects(new FindListener<DrugDataBean>() {
            @Override
            public void done(List<DrugDataBean> list, BmobException e) {
                if(e==null){
                    if (list.size() > 0) {
                        final String [] strUser = new String[list.size()];
                        final String [] strDrug = new String[list.size()];
                        final String [] strBox = new String[list.size()];

                        for (int i = 0; i < list.size(); i ++) {
                            strUser[i] = list.get(i).getUserName().toString().trim();
                            strDrug[i] = list.get(i).getGenericName().toString().trim();
                            strBox[i] = list.get(i).getBoxNumber().toString().trim();
                            Log.i("bmob","第"+i+"条搜索结果为："+strUser[i]);
                            Log.i("bmob","第"+i+"条搜索结果为："+strDrug[i]);
                            Log.i("bmob","第"+i+"条搜索结果为："+strBox[i]);

                            HomeSearchData data = new HomeSearchData();
                            data.setCurrentUser(String.valueOf(strUser[i]));
                            data.setDrugName(String.valueOf(strDrug[i]));
                            data.setDrugBoxNum(String.valueOf(strBox[i]));
                            DbUtils.insert(data);
                            Log.i("bmob","插入数据成功，strUser："+data.getCurrentUser());
                            Log.i("bmob","插入数据成功，strDrug："+data.getDrugName());
                            Log.i("bmob","插入数据成功，strBox："+data.getDrugBoxNum());
                            Log.i("bmob","插入数据成功，data："+data);
                            Log.i("bmob","搜索数据成功，结果："+list.size());

                        }
                    }else {
                        Log.i("bmob","搜索数据成功，结果："+list.size());
                    }
                    List<HomeSearchData> mHomeSearchData =DbUtils.getQueryAll(HomeSearchData.class);
                    Logger.d("SearchResults="+mHomeSearchData);
                    mSearchContextHomeLV.setAdapter(new CommonAdapter<HomeSearchData>(HomeSearchActivity.this,mHomeSearchData,R.layout.listview_item_search_results) {
                        @Override
                        protected void convertView(View item, final HomeSearchData mHomeSearchData) {
                            TextView tv_drug= CommonViewHolder.get(item,R.id.id_item_textView_search_results_name);
                            TextView tv_box= CommonViewHolder.get(item,R.id.id_item_textView_search_results_box_num);
                            tv_drug.setText("药品名称： " + mHomeSearchData.getDrugName());
                            tv_box.setText("所在药箱： " + mHomeSearchData.getDrugBoxNum());
                            tv_drug.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                                    Toast.makeText(getApplicationContext(), "药品名称： " + mHomeSearchData.getDrugName()+ "所在药箱： " + mHomeSearchData.getDrugBoxNum(), Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), DrugBoxActivity.class).putExtra("DrugBoxNum", mHomeSearchData.getDrugBoxNum()));
                                }
                            });
//                            LinearLayout linearLayout = CommonViewHolder.get(item, R.id.id_layout_linearLayout_search_results);
//                            linearLayout.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Toast.makeText(getApplicationContext(), "药品名称： " + mHomeSearchData.getDrugName()+ "所在药箱： " + mHomeSearchData.getDrugBoxNum(), Toast.LENGTH_SHORT).show();
//                                }
//                            });
                        }
                    });

                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
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

}
