package com.sinntalker.sinntest20180503_yy.Fragment.DrugBox;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.sinntalker.sinntest20180503_yy.Activity.ClockActivity;

/**
 * Created by Administrator on 2018/6/15.
 */

public class ReceiveAlarm extends BroadcastReceiver {
//    AllUserBean userBean = BmobUser.getCurrentUser(AllUserBean.class);

    @Override
    public void onReceive(final Context context, Intent intent) {

        String message = intent.getStringExtra("msg");
        final String username = intent.getStringExtra("user");
        String drugName = intent.getStringExtra("drugName");
        String drugNum = intent.getStringExtra("drugNum");
        String timeH = intent.getStringExtra("timeH");
        String timeM = intent.getStringExtra("timeM");
        //TODO Auto-generated method stub
        if(intent.getAction().equals("short")) {
//            Toast.makeText(context, "short alarm", Toast.LENGTH_LONG).show();
            Log.i("bmob", "short alarm");

            Intent clockIntent = new Intent(context, ClockActivity.class);
            clockIntent.putExtra("msg", message);
            clockIntent.putExtra("drugNum", drugNum);
            clockIntent.putExtra("drugName", drugName);
            clockIntent.putExtra("user", username);
            clockIntent.putExtra("timeH", timeH);
            clockIntent.putExtra("timeM", timeM);
            context.startActivity(clockIntent);

        } else {
            Toast.makeText(context, "repeating alarm", Toast.LENGTH_LONG).show();
        }
    }

}
