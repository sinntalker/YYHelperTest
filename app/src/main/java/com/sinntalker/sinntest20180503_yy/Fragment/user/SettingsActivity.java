package com.sinntalker.sinntest20180503_yy.Fragment.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinntalker.sinntest20180503_yy.Activity.LoginActivity;
import com.sinntalker.sinntest20180503_yy.Activity.MainActivity;
import com.sinntalker.sinntest20180503_yy.Fragment.family.base.BaseActivity;
import com.sinntalker.sinntest20180503_yy.Fragment.family.model.UserModel;
import com.sinntalker.sinntest20180503_yy.R;

import cn.bmob.newim.BmobIM;

public class SettingsActivity extends Activity implements View.OnClickListener {

    private ImageView back_image; //返回按键
    private TextView infoSet_text; //个人信息
    private TextView universal_text; //通用设置
    private TextView about_text; //关于
    private TextView cancelUser_text; //注销登陆

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_settings);

        back_image = findViewById(R.id.backToUser_settings_image);
        infoSet_text = findViewById(R.id.infoSet_textView);
        universal_text = findViewById(R.id.universal_textView);
        about_text = findViewById(R.id.about_textView);
        cancelUser_text = findViewById(R.id.cancelUser_textView);

        //设置监听
        back_image.setOnClickListener(this);
        infoSet_text.setOnClickListener(this);
        universal_text.setOnClickListener(this);
        about_text.setOnClickListener(this);
        cancelUser_text.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == back_image) { //返回按键
            finish();
        }
        if(v == infoSet_text) { //绑定手机号
            Toast.makeText(getApplicationContext(), "个人信息", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), PersonalInfoActivity.class));
        }
        if(v == universal_text) {
            Toast.makeText(getApplicationContext(), "通用", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), GeneralSettingsActivity.class));
        }
        if(v == about_text) {
            Toast.makeText(getApplicationContext(), "关于", Toast.LENGTH_LONG).show();
            //打开 “关于” 界面
            startActivity(new Intent(getApplicationContext(), AboutUsActivity.class));
        }
        if(v == cancelUser_text) {
            Toast.makeText(getApplicationContext(), "注销", Toast.LENGTH_LONG).show();
            UserModel.getInstance().logout();
            //TODO 连接：3.2、退出登录需要断开与IM服务器的连接
            BmobIM.getInstance().disConnect();
            startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
            finish();
//            showLogoutDialog(SettingActivity.this);
        }
    }

}
