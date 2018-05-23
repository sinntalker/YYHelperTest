package com.sinntalker.sinntest20180503_yy.Fragment.health;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinntalker.sinntest20180503_yy.Fragment.health.BloodPressure.BloodPressureActivity;
import com.sinntalker.sinntest20180503_yy.Fragment.health.BloodSugar.BloodSugerActivity;
import com.sinntalker.sinntest20180503_yy.Fragment.health.StepCounter.StepCounterActivity;
import com.sinntalker.sinntest20180503_yy.Fragment.health.Weight.WeightActivity;
import com.sinntalker.sinntest20180503_yy.Fragment.user.PersonalInfoActivity;
import com.sinntalker.sinntest20180503_yy.R;
import com.sinntalker.sinntest20180503_yy.UserBean;

import cn.bmob.v3.BmobUser;

public class HealthDocuActivity extends Activity implements View.OnClickListener{

    Button open_dosage_btn;
    Button open_bloodSuger_btn;
    Button open_bloodPressure_btn;
    Button open_weight_btn;
    Button open_steps_btn;

    ImageView back_image;
    ImageView more_info_image;
    TextView mInfoMoreHDATV;

    TextView userName_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_health_docu);

        open_dosage_btn = findViewById(R.id.open_dosage_button);
        open_bloodSuger_btn = findViewById(R.id.open_bloodSuger_button);
        open_bloodPressure_btn = findViewById(R.id.open_bloodPressure_button);
        open_weight_btn = findViewById(R.id.open_weight_button);
        open_steps_btn = findViewById(R.id.open_steps_button);
        back_image = findViewById(R.id.backToUser_document_image);
        more_info_image = findViewById(R.id.more_info_healthdocu_image);
        userName_txt = findViewById(R.id.userName_docu_txt);
        mInfoMoreHDATV = findViewById(R.id.info_document_textView);

        final UserBean userBean = BmobUser.getCurrentUser(UserBean.class);
        final String[] phone = new String[1];
        phone[0] = userBean.getUsername();
        userName_txt.setText(phone[0]);

        open_dosage_btn.setOnClickListener(this);
        open_bloodSuger_btn.setOnClickListener(this);
        open_bloodPressure_btn.setOnClickListener(this);
        open_weight_btn.setOnClickListener(this);
        open_steps_btn.setOnClickListener(this);
        back_image.setOnClickListener(this);
        more_info_image.setOnClickListener(this);
        mInfoMoreHDATV.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == open_dosage_btn) {
            Toast.makeText(getApplicationContext(), "开启药物", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), DrugUsingActivity.class));
        }
        if(v == open_bloodSuger_btn) {
//            Toast.makeText(getApplicationContext(), "开启血糖功能", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), BloodSugerActivity.class));
        }
        if(v == open_bloodPressure_btn) {
//            Toast.makeText(getApplicationContext(), "开启血压功能", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), BloodPressureActivity.class));
        }
        if(v == open_weight_btn) {
            Toast.makeText(getApplicationContext(), "开启称重功能", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), WeightActivity.class));
        }
        if(v == open_steps_btn) {
//            Toast.makeText(getApplicationContext(), "开启计步功能", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), StepCounterActivity.class));
        }
        if(v == back_image) {
            finish();
        }
        if(v == more_info_image) {
//            Toast.makeText(getApplicationContext(), "更多信息", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), PersonalInfoActivity.class));
        }
        if(v == mInfoMoreHDATV) {
//            Toast.makeText(getApplicationContext(), "更多信息", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), PersonalInfoActivity.class));
        }
    }

}
