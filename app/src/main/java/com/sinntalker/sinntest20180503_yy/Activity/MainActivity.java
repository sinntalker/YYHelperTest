package com.sinntalker.sinntest20180503_yy.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinntalker.sinntest20180503_yy.Fragment.family.FamilyFragment;
import com.sinntalker.sinntest20180503_yy.Fragment.health.HealthFragment;
import com.sinntalker.sinntest20180503_yy.Fragment.home.HomeFragment;
import com.sinntalker.sinntest20180503_yy.R;
import com.sinntalker.sinntest20180503_yy.Fragment.user.UserFragment;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    //RadioButton
    private ImageView rb_home;    //home button
    private ImageView rb_health;  //health button
    private ImageView rb_relation;//relation button
    private ImageView rb_user;     //user button

    private ImageView message_image;
    //TextView titleBar
    private TextView title_bar_txt;

    private FragmentManager manager;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_main);

        initView();//实例化

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.frameLayout_content, new HomeFragment());
        transaction.commit();
//        rb_home.setChecked(true);
        title_bar_txt.setText("医药库");
        rb_home.setImageResource(R.drawable.home_blue);
        rb_health.setImageResource(R.drawable.health_gray);
        rb_relation.setImageResource(R.drawable.relatives_gray);
        rb_user.setImageResource(R.drawable.user_gray);
    }

    private void initView() {
        rb_home = findViewById(R.id.rbHome);
        rb_health = findViewById(R.id.rbHealth);
        rb_relation = findViewById(R.id.rbRelation);
        rb_user = findViewById(R.id.rbUser);

        message_image = findViewById(R.id.message_image);
        title_bar_txt = findViewById(R.id.title_bar_txt);

        rb_home.setOnClickListener(this);
        rb_health.setOnClickListener(this);
        rb_relation.setOnClickListener(this);
        rb_user.setOnClickListener(this);

        message_image.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == message_image) {
            Toast.makeText(MainActivity.this, "消息提示", Toast.LENGTH_LONG).show();
            startActivity(new Intent(MainActivity.this, MessageActivity.class)); //打开通知消息界面
        }else {
            transaction = manager.beginTransaction();

            switch (v.getId()) {
                case R.id.rbHome:
                    transaction.replace(R.id.frameLayout_content, new HomeFragment());
                    title_bar_txt.setText("医药库");
                    rb_home.setImageResource(R.drawable.home_blue);
                    rb_health.setImageResource(R.drawable.health_gray);
                    rb_relation.setImageResource(R.drawable.relatives_gray);
                    rb_user.setImageResource(R.drawable.user_gray);
//                    Toast.makeText(MainActivity.this, "医药库", Toast.LENGTH_LONG).show();
                    break;
                case R.id.rbHealth:
                    transaction.replace(R.id.frameLayout_content, new HealthFragment());
                    title_bar_txt.setText("健康管理");
                    rb_home.setImageResource(R.drawable.home_gray);
                    rb_health.setImageResource(R.drawable.health_blue);
                    rb_relation.setImageResource(R.drawable.relatives_gray);
                    rb_user.setImageResource(R.drawable.user_gray);
//                    Toast.makeText(MainActivity.this, "健康", Toast.LENGTH_LONG).show();
                    break;
                case R.id.rbRelation:
                    transaction.replace(R.id.frameLayout_content, new FamilyFragment());
                    title_bar_txt.setText("家人");
                    rb_home.setImageResource(R.drawable.home_gray);
                    rb_health.setImageResource(R.drawable.health_gray);
                    rb_relation.setImageResource(R.drawable.relatives_blue);
                    rb_user.setImageResource(R.drawable.user_gray);
//                    Toast.makeText(MainActivity.this, "家人", Toast.LENGTH_LONG).show();
                    break;
                case R.id.rbUser:
                    transaction.replace(R.id.frameLayout_content, new UserFragment());
                    title_bar_txt.setText("用户中心");
                    rb_home.setImageResource(R.drawable.home_gray);
                    rb_health.setImageResource(R.drawable.health_gray);
                    rb_relation.setImageResource(R.drawable.relatives_gray);
                    rb_user.setImageResource(R.drawable.user_blue);
//                    Toast.makeText(MainActivity.this, "用户中心", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
            transaction.commit();
        }
    }

//    @Override
//    public void onBackPressed() {
//        CommonUtils.showExitDialog(MainActivity.this);
//    }

}
