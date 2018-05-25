package com.sinntalker.sinntest20180503_yy.Fragment.home;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinntalker.sinntest20180503_yy.AllUserBean;
import com.sinntalker.sinntest20180503_yy.Common.StringUnits;
import com.sinntalker.sinntest20180503_yy.Fragment.DrugBox.DrugBoxActivity;
import com.sinntalker.sinntest20180503_yy.Fragment.DrugBox.DrugCommonDataBean;
import com.sinntalker.sinntest20180503_yy.Fragment.DrugBox.DrugDataBean;
import com.sinntalker.sinntest20180503_yy.Fragment.health.StepCounter.DbUtils;
import com.sinntalker.sinntest20180503_yy.Fragment.health.Weight.WeightData;
import com.sinntalker.sinntest20180503_yy.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener{

    private Button search_btn;
    private EditText search_edit;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = View.inflate(getContext(), R.layout.fragment_home, null);
        search_btn = view.findViewById(R.id.search_button);
        search_edit = view.findViewById(R.id.search_bar);
        search_btn.setOnClickListener(this);
        initGriDView(view);
        return view;
    }

    private void initGriDView(View view) {

        final String [] textId = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        final int [] imageId = { R.drawable.drugbox, R.drawable.drugbox, R.drawable.drugbox,
                R.drawable.drugbox, R.drawable.drugbox, R.drawable.drugbox,
                R.drawable.drugbox, R.drawable.drugbox, R.drawable.drugbox };

        class GridViewYYAdapter extends BaseAdapter {
            private Context context ;
            public GridViewYYAdapter(Context context){
                this.context = context;
            }

            @Override
            public int getCount() {
                return textId.length;
            }

            @Override
            public Object getItem(int position) {
                return textId[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }
            ViewHolder viewHolder;

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                // 创建布局
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_grid_view_home, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.textView = convertView.findViewById(R.id.ItemText);
                viewHolder.imageView = convertView.findViewById(R.id.ItemImage);

                viewHolder.textView.setText("药箱"+textId[position]);
                viewHolder.imageView.setImageResource(imageId[position]);

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "当前药箱编号："+textId[position], Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getContext(), DrugBoxActivity.class).putExtra("DrugBoxNum", textId[position]));
                    }
                });

                return convertView;
            }

            class ViewHolder{
                TextView textView;  //List 文章 标题
                ImageView imageView; //List 文章 图片
            }
        }

        //通过ID获取listView
        GridView gridView = (GridView) view.findViewById(R.id.GridView_YY);

        //设置listView的Adapter
        gridView.setAdapter(new GridViewYYAdapter(getContext()));

    }

    @Override
    public void onClick(View v) {
        if(v == search_btn) {
            String search_str = search_edit.getText().toString().trim();
//            Toast.makeText(getContext(), "搜索", Toast.LENGTH_LONG).show();
            String searchContext = search_edit.getText().toString().trim();
            if (searchContext.length() != 0) {
                Toast.makeText(getContext(), "搜索"+searchContext, Toast.LENGTH_LONG).show();
                AllUserBean mCurrentUser = BmobUser.getCurrentUser(AllUserBean.class);
                Intent intent = new Intent(getContext(), HomeSearchActivity.class);
                intent.putExtra("searchContext", searchContext);
                getContext().startActivity(intent);
            }else {
                Toast.makeText(getContext(), "请输入药品名称", Toast.LENGTH_LONG).show();
            }
        }
    }
}
