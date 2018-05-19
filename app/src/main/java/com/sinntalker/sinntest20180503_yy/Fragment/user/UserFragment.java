package com.sinntalker.sinntest20180503_yy.Fragment.user;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinntalker.sinntest20180503_yy.Fragment.Device.MyDeviceActivity;
import com.sinntalker.sinntest20180503_yy.Fragment.health.HealthDocuActivity;
import com.sinntalker.sinntest20180503_yy.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment implements View.OnClickListener{

    private ImageView userIcon_image;
    private TextView userName_txt;
    private TextView healthDocument_txt;
    private TextView myFamily_txt;
    private TextView settings_txt;
    private TextView myDevice_txt;

    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = View.inflate(getContext(), R.layout.fragment_user, null);

        userIcon_image = view.findViewById(R.id.userIcon_image);
        userName_txt = view.findViewById(R.id.userName_txt);
        healthDocument_txt = view.findViewById(R.id.healthDocument_txt);
        myFamily_txt = view.findViewById(R.id.myFamily_txt);
        settings_txt = view.findViewById(R.id.settings_txt);
        myDevice_txt = view.findViewById(R.id.myDevice_txt);

        userIcon_image.setOnClickListener(this);
//        userName_txt.setOnClickListener(this);
        healthDocument_txt.setOnClickListener(this);
        myFamily_txt.setOnClickListener(this);
        settings_txt.setOnClickListener(this);
        myDevice_txt.setOnClickListener(this);
        userName_txt.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == userIcon_image) {
            Toast.makeText(getContext(), "用户头像", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getContext(), UserInfoActivity.class));
        }
        if (v == healthDocument_txt) {
            Toast.makeText(getContext(), "健康档案", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getContext(), HealthDocuActivity.class));
        }
        if (v == myFamily_txt) {
            Toast.makeText(getContext(), "我的家人", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getContext(), MyFamilyActivity.class));
        }
        if (v == settings_txt) {
            Toast.makeText(getContext(), "设置", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getContext(), SettingsActivity.class));
        }
        if (v == myDevice_txt) {
            Toast.makeText(getContext(), "我的设备", Toast.LENGTH_LONG).show();
            //打开我的设备
            startActivity(new Intent(getContext(), MyDeviceActivity.class));
        }
        if (v == userName_txt) {
            Toast.makeText(getContext(), "用户名", Toast.LENGTH_LONG).show();
            //打开我的个人信息界面
            startActivity(new Intent(getContext(), UserInfoActivity.class));
        }
    }

}
