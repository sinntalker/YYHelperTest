package com.sinntalker.sinntest20180503_yy.Fragment.user;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.sinntalker.sinntest20180503_yy.Activity.LoginActivity;
import com.sinntalker.sinntest20180503_yy.Activity.MainActivity;
import com.sinntalker.sinntest20180503_yy.Fragment.family.all.BmobIMApplication;
import com.sinntalker.sinntest20180503_yy.Fragment.family.model.UserModel;
import com.sinntalker.sinntest20180503_yy.Fragment.user.about.AboutUsActivity;
import com.sinntalker.sinntest20180503_yy.R;

import cn.bmob.newim.BmobIM;

public class SettingsActivity extends Activity implements View.OnClickListener {

    private ImageView back_image; //返回按键
    private TextView infoSet_text; //个人信息
    private TextView universal_text; //通用设置
    private TextView about_text; //关于
    private TextView cancelUser_text; //注销登陆

    public BmobIMApplication mBmobIMApplication;

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
//            Toast.makeText(getApplicationContext(), "个人信息", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), PersonalInfoActivity.class));
        }
        if(v == universal_text) {
//            Toast.makeText(getApplicationContext(), "通用", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), GeneralSettingsActivity.class));
        }
        if(v == about_text) {
//            Toast.makeText(getApplicationContext(), "关于", Toast.LENGTH_LONG).show();
            //打开 “关于” 界面
            startActivity(new Intent(getApplicationContext(), AboutUsActivity.class));
        }
        if(v == cancelUser_text) {
//            Toast.makeText(getApplicationContext(), "注销", Toast.LENGTH_LONG).show();

            customDialog();

//            showLogoutDialog(SettingActivity.this);
        }
    }

    /**
     * 自定义对话框
     */
    private void customDialog() {
        final Dialog dialog = new Dialog(this, R.style.NormalDialogStyle);
        View view = View.inflate(this, R.layout.dialog_normal, null);
        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        TextView confirm = (TextView) view.findViewById(R.id.confirm);
        dialog.setContentView(view);
        //使得点击对话框外部不消失对话框
        dialog.setCanceledOnTouchOutside(false);
        //设置对话框的大小
//        view.setMinimumHeight((int) (ScreenSizeUtils.getInstance(this).getScreenHeight() * 0.23f));
//        Window dialogWindow = dialog.getWindow();
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        lp.width = (int) (ScreenSizeUtils.getInstance(this).getScreenWidth() * 0.75f);
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.gravity = Gravity.CENTER;
//        dialogWindow.setAttributes(lp);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("bmob", "取消注销");
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("bmob", "注销用户");
                UserModel.getInstance().logout();
                //TODO 连接：3.2、退出登录需要断开与IM服务器的连接
                BmobIM.getInstance().disConnect();
                MainActivity.test_a.finish();
                startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                finish();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
