package com.sinntalker.sinntest20180503_yy.Activity;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.sinntalker.sinntest20180503_yy.AllUserBean;
import com.sinntalker.sinntest20180503_yy.Fragment.DrugBox.AlarmBean;
import com.sinntalker.sinntest20180503_yy.Fragment.DrugBox.SimpleDialog;
import com.sinntalker.sinntest20180503_yy.Fragment.family.all.BmobIMApplication;
import com.sinntalker.sinntest20180503_yy.R;
import com.sinntalker.sinntest20180503_yy.Sql.MessageDataBean;
import com.sinntalker.sinntest20180503_yy.Sql.util.MessageDataBeanDao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class ClockActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_clock);

        final String message = this.getIntent().getStringExtra("msg");
        String username = this.getIntent().getStringExtra("user");
        String drugName = this.getIntent().getStringExtra("drugName");
        String drugNum = this.getIntent().getStringExtra("drugNum");
        String timeH = this.getIntent().getStringExtra("timeH");
        String timeM = this.getIntent().getStringExtra("timeM");

        final AllUserBean userBean = BmobUser.getCurrentUser(AllUserBean.class);
        //查询条件一：用户名称
        BmobQuery<AlarmBean> queryUserName = new BmobQuery<AlarmBean>();
        queryUserName.addWhereEqualTo("user",userBean);

        //查询条件二：药箱编号
        BmobQuery<AlarmBean> queryDrugBoxNum = new BmobQuery<AlarmBean>();
        queryDrugBoxNum.addWhereEqualTo("drugNum",drugNum);

        //查询条件三：药箱名称
        BmobQuery<AlarmBean> queryDrugName = new BmobQuery<AlarmBean>();
        queryDrugName.addWhereEqualTo("drug",drugName);

        //查询条件四：小时
        BmobQuery<AlarmBean> queryHour = new BmobQuery<AlarmBean>();
        queryHour.addWhereEqualTo("timeH", Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        Log.i("bmob", "AlarmBean中查询符合时间的数据-HOUR_OF_DAY:" + Calendar.getInstance().get(Calendar.HOUR_OF_DAY));

        //查询条件五：分钟
        BmobQuery<AlarmBean> queryMinute = new BmobQuery<AlarmBean>();
        queryMinute.addWhereEqualTo("timeM", Calendar.getInstance().get(Calendar.MINUTE));
        Log.i("bmob", "AlarmBean中查询符合时间的数据-minute:" + Calendar.getInstance().get(Calendar.MINUTE));

        //最后查询时完整的条件
        List<BmobQuery<AlarmBean>> allQueries = new ArrayList<BmobQuery<AlarmBean>>();
        allQueries.add(queryUserName);
        allQueries.add(queryDrugBoxNum);
        allQueries.add(queryDrugName);
        allQueries.add(queryHour);
        allQueries.add(queryMinute);

        //查询
        BmobQuery<AlarmBean> query = new BmobQuery<AlarmBean>();
        query.and(allQueries);
        query.findObjects(new FindListener<AlarmBean>() {
            @Override
            public void done(List<AlarmBean> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i ++) {
                            MessageDataBean messageDataBean = new MessageDataBean(
                                    userBean.getUsername(),
                                    list.get(i).getDrugNum(),
                                    list.get(i).getDrug(),
                                    list.get(i).getDosage(),
                                    list.get(i).getNum(),
                                    list.get(i).getTimeH(),
                                    list.get(i).getTimeM(),
                                    list.get(i).getTouched(),
                                    list.get(i).getTouchedTime(),
                                    list.get(i).getRequestCode());
                            messageDataBean.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null) {
                                        Log.i("bmob", "MessageDataBean插入数据成功：" + s);
                                    } else {
                                        Log.i("bmob", "MessageDataBean插入数据失败：" + e.getMessage() + e.getErrorCode());
                                    }
                                }
                            });
//                            getMessageDataBeanDao().insert(messageDataBean);
                            Log.i("bmob", "MessageDataBean插入数据成功：" + messageDataBean.toString());
                        }
                        Log.i("bmob", "MessageDataBean插入数据完成");
                    } else {
                        Log.i("bmob", "查询成功，AlarmBean中数据为空");
                    }
                } else {
                    Log.i("bmob", "MessageDataBean插入数据失败：" + e.getMessage() + e.getErrorCode());
                }
            }
        });

        final SimpleDialog dialog = new SimpleDialog(this, R.style.FullScreenDialog);
        dialog.show();
        dialog.setTitle("闹钟提醒");
        dialog.setMessage(message);
        dialog.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.bt_confirm == v || dialog.bt_cancel == v) {
//                        if (flag == 1 || flag == 2) {
//                            mediaPlayer.stop();
//                            mediaPlayer.release();
//                        }
//                        if (flag == 0 || flag == 2) {
//                            vibrator.cancel();
//                        }
                    dialog.dismiss();
                    finish();
                }
            }
        });
    }

    private MessageDataBeanDao getMessageDataBeanDao() {
        // 通过 MyApplication 类提供的 getDaoSession() 获取具体 Dao
        return ((BmobIMApplication) this.getApplicationContext()).getDaoSession().getMessageDataBeanDao();
    }

    private SQLiteDatabase getDb() {
        // 通过 MyApplication 类提供的 getDb() 获取具体 db
        return ((BmobIMApplication) this.getApplicationContext()).getDb();
    }
}
