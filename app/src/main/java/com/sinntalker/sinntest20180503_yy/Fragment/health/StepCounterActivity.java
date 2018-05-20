package com.sinntalker.sinntest20180503_yy.Fragment.health;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.sinntalker.sinntest20180503_yy.R;

import org.w3c.dom.Text;

public class StepCounterActivity extends Activity implements View.OnClickListener{

    //声明控件
    ImageView mBcakSCAIV; //返回
    TextView mIsSupportSCATV; //是否支持计步
    TextView mHistoryDataSCATV; //历史计步数据
    TextView mSetWalkPlanSCATV; //设置锻炼计划
    StepArcView mStepViewSCASAV; //计步显示

    //声明sharePreference数据
    private SharedPreferencesUtils_StepCounter mSharePreferencesUtils_StepCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_step_counter);

        //实例化
        mBcakSCAIV = findViewById(R.id.id_imageView_back_stepCounter);
        mIsSupportSCATV = findViewById(R.id.id_textView_isSupport_stepCounter);
        mHistoryDataSCATV = findViewById(R.id.id_textView_historyData_stepCounter);
        mSetWalkPlanSCATV = findViewById(R.id.id_textView_walkPlanSet_stepCounter);
        mStepViewSCASAV = findViewById(R.id.id_stepArcView_stepView_stepCounter);

        //功能调用
        mBcakSCAIV.setOnClickListener(this);
        mHistoryDataSCATV.setOnClickListener(this);
        mSetWalkPlanSCATV.setOnClickListener(this);
        mSharePreferencesUtils_StepCounter = new SharedPreferencesUtils_StepCounter(this);
        String mWalkPlanStr = (String) mSharePreferencesUtils_StepCounter.getParam("WalkPlan","5000");
        mStepViewSCASAV.setCurrentCount(Integer.parseInt(mWalkPlanStr),0);
        mIsSupportSCATV.setText("计步中...");
        setupService();
    }

    @Override
    public void onClick(View v) {
        if (v == mBcakSCAIV) {
            finish();
        }
        if (v == mSetWalkPlanSCATV) {
            //打开设置锻炼计划activity
            startActivity(new Intent(getApplicationContext(), WalkPlanSetActivity.class));
        }
        if (v == mHistoryDataSCATV) {
            //打开历史计步文件数据activity
            startActivity(new Intent(getApplicationContext(), StepHistoryDataActivity.class));
        }
    }

    private boolean isBind = false;

    /**
     * 开启计步服务
     */
    private void setupService() {
        Intent intent = new Intent(this, StepService.class);
        isBind = bindService(intent, conn, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    /**
     * 用于查询应用服务（application Service）的状态的一种interface，
     * 更详细的信息可以参考Service 和 context.bindService()中的描述，
     * 和许多来自系统的回调方式一样，ServiceConnection的方法都是进程的主线程中调用的。
     */
    ServiceConnection conn = new ServiceConnection() {
        /**
         * 在建立起于Service的连接时会调用该方法，目前Android是通过IBind机制实现与服务的连接。
         * @param name 实际所连接到的Service组件名称
         * @param service 服务的通信信道的IBind，可以通过Service访问对应服务
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StepService stepService = ((StepService.StepBinder) service).getService();
            //设置初始化数据
            String mWalkPlanStr = (String) mSharePreferencesUtils_StepCounter.getParam("WalkPlan", "5000");
            mStepViewSCASAV.setCurrentCount(Integer.parseInt(mWalkPlanStr), stepService.getStepCount());

            //设置步数监听回调
            stepService.registerCallback(new UpdateUiCallBack() {
                @Override
                public void updateUi(int stepCount) {
                    String mWalkPlanStr = (String) mSharePreferencesUtils_StepCounter.getParam("WalkPlan", "5000");
                    mStepViewSCASAV.setCurrentCount(Integer.parseInt(mWalkPlanStr), stepCount);
                }
            });
        }

        /**
         * 当与Service之间的连接丢失的时候会调用该方法，
         * 这种情况经常发生在Service所在的进程崩溃或者被Kill的时候调用，
         * 此方法不会移除与Service的连接，当服务重新启动的时候仍然会调用 onServiceConnected()。
         * @param name 丢失连接的组件名称
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
