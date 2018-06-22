package com.sinntalker.sinntest20180503_yy.Fragment.family.all;

import android.app.Activity;
import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.orhanobut.logger.Logger;
import com.sinntalker.sinntest20180503_yy.Fragment.family.base.UniversalImageLoader;
import com.sinntalker.sinntest20180503_yy.Sql.util.DaoMaster;
import com.sinntalker.sinntest20180503_yy.Sql.util.DaoSession;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;

import static com.sinntalker.sinntest20180503_yy.Common.Constant_Java.DB_NAME;

/**
 * Created by Administrator on 2018/5/31.
 */
//TODO 集成：1.7、自定义Application，并在AndroidManifest.xml中配置
public class BmobIMApplication extends Application {

    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private List<Activity> oList;//用于存放所有启动的Activity的集合

    private static BmobIMApplication INSTANCE;

    public static BmobIMApplication INSTANCE() {
        return INSTANCE;
    }

    private void setInstance(BmobIMApplication app) {
        setBmobIMApplication(app);
    }

    private static void setBmobIMApplication(BmobIMApplication a) {
        BmobIMApplication.INSTANCE = a;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
        //TODO 集成：1.8、初始化IM SDK，并注册消息接收器，只有主进程运行的时候才需要初始化
        if (getApplicationInfo().packageName.equals(getMyProcessName())) {
            BmobIM.init(this);
            BmobIM.registerDefaultMessageHandler(new DemoMessageHandler(this));
        }
        Logger.init("BmobNewIMDemo");
        UniversalImageLoader.initImageLoader(this);
        oList = new ArrayList<Activity>();
        setDatabase();
    }

    /**
     * 获取当前运行的进程名
     *
     * @return
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 添加Activity
     */
    public void addActivity_(Activity activity) {
        // 判断当前集合中不存在该Activity
        if (!oList.contains(activity)) {
            oList.add(activity);//把当前Activity添加到集合中
        }
    }

    /**
     * 销毁单个Activity
     */
    public void removeActivity_(Activity activity) {
        //判断当前集合中存在该Activity
        if (oList.contains(activity)) {
            oList.remove(activity);//从集合中移除
            activity.finish();//销毁当前Activity
        }
    }

    /**
     * 销毁所有的Activity
     */
    public void removeALLActivity_() {
        //通过循环，把集合中的所有Activity销毁
        for (Activity activity : oList) {
            activity.finish();
        }
    }

    /**
     * 设置greenDao
     */
    private void setDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        mHelper = new DaoMaster.DevOpenHelper(this, DB_NAME, null);
        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }
    public DaoSession getDaoSession() {
        return mDaoSession;
    }
    public SQLiteDatabase getDb() {
        return db;
    }

}
