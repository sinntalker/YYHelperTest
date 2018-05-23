package com.sinntalker.sinntest20180503_yy.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.sinntalker.sinntest20180503_yy.Common.Constant_Java;
import com.sinntalker.sinntest20180503_yy.R;
import com.sinntalker.sinntest20180503_yy.UserBean;

import cn.bmob.v3.BmobUser;

public class SplashActivity extends Activity {

    Handler mHandler;
    SharedPreferences mSharedPreferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_splash);

        mHandler = new Handler();
        //获取屏幕的大小和密度--采用DisplayMetrics类

        //检查版本更新

        //进入主程序
        afterVersionCheck();
    }

    private void afterVersionCheck() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(mainIntent);
                finish();
            }
        },500);
//        UserBean userInfo = BmobUser.getCurrentUser(UserBean.class);
        //检查是否为第一次登陆
//        boolean isFirstTime = false;
//        mSharedPreferences = getSharedPreferences(
//                Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
//        isFirstTime = mSharedPreferences.getBoolean("isFirstTime", true);

//
//        if (userInfo != null && !isFirstTime) {  //自动登陆
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                    Toast.makeText(SplashActivity.this, "1主界面", Toast.LENGTH_LONG).show();
//                    SplashActivity.this.finish();
//                }
//            }, 3000);
//        } else {
//            if (isFirstTime) {
//                // 如果是第一次进入应用，停留1.5秒进入引导页面
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        startActivity(new Intent(SplashActivity.this, GuideActivity.class));
//                        Toast.makeText(SplashActivity.this, "2引导界面", Toast.LENGTH_LONG).show();
////                        startActivity(new Intent(SplashActivity.this, GuideActivity.class));
//                        SplashActivity.this.finish();
//                    }
//                }, 3000);
//            } else {
//                // 否则，停留1.5秒进入登陆页面
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//                        Toast.makeText(SplashActivity.this, "3登陆界面", Toast.LENGTH_LONG).show();
////                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//                        SplashActivity.this.finish();
//                    }
//                }, 3000);
//            }
//        }
    }
}
