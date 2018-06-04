package com.sinntalker.sinntest20180503_yy.Fragment.user;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.sinntalker.sinntest20180503_yy.AllUserBean;
import com.sinntalker.sinntest20180503_yy.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

public class FeedbackActivity extends Activity {

    ImageView mBackFBIV; //返回
    EditText mContextFBET; //反馈内容
    Button mSubmitFBBtn; //提交按键

    String strContext;
    String strCurrentDate;
    String strCurrentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_feedback);

        //实例化
        mBackFBIV = findViewById(R.id.id_imageView_back_feedback);
        mContextFBET =findViewById(R.id.id_editText_context_feedback);
        mSubmitFBBtn = findViewById(R.id.id_button_submit_feedback);

        mBackFBIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSubmitFBBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strContext = mContextFBET.getText().toString().trim();

                Calendar calendar = Calendar.getInstance();
                //获取系统的日期
                //年
                int year = calendar.get(Calendar.YEAR);
                //月
                int month = calendar.get(Calendar.MONTH)+1;
                //日
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                //获取系统时间
                //小时
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                //分钟
                int minute = calendar.get(Calendar.MINUTE);

                strCurrentDate = year + "-" + month + "-" + day;
                strCurrentTime = hour + ":" + minute;

                if (strContext.length() == 0) {
                    Toast.makeText(FeedbackActivity.this, "反馈内容不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    AllUserBean currentUser = BmobUser.getCurrentUser(AllUserBean.class);

                    FeedbackBean mFeedbackBean = new FeedbackBean();
                    mFeedbackBean.setUser(currentUser);
                    mFeedbackBean.setSubmitTime(strCurrentDate + strCurrentTime);
                    mFeedbackBean.setFeedbackContext(strContext);
                    mFeedbackBean.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                Toast.makeText(FeedbackActivity.this, "反馈成功，请等待我们的处理结果", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(FeedbackActivity.this, "反馈失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                                Log.i("bmob", "反馈失败，错误原因：" + e.getErrorCode() + e.getMessage());
                            }
                        }
                    });
                }

            }
        });

    }
}
