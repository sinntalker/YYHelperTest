package com.sinntalker.sinntest20180503_yy.Weibo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.NetUtils;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.utils.LogUtil;
import com.sinntalker.sinntest20180503_yy.Activity.LoginActivity;
import com.sinntalker.sinntest20180503_yy.Activity.MainActivity;
import com.sinntalker.sinntest20180503_yy.Activity.RegisterActivity;
import com.sinntalker.sinntest20180503_yy.AllUserBean;
import com.sinntalker.sinntest20180503_yy.Common.CommonUnits;
import com.sinntalker.sinntest20180503_yy.Common.Constant;
import com.sinntalker.sinntest20180503_yy.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

public class WBAuthActivity extends Activity {

    private static final String TAG_WB = "weibo";
    /**
     * 显示认证后的信息，如 AccessToken
     */
    private TextView mTokenText;
    /**
     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
     */
    private Oauth2AccessToken mAccessToken;
    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    private SsoHandler mSsoHandler;

    private TextView uid_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_wbauth);
        // 获取 Token View，并让提示 View 的内容可滚动（小屏幕可能显示不全）
        mTokenText = (TextView) findViewById(R.id.token_text_view);

        uid_view = findViewById(R.id.userInfo_view);
        // 创建微博实例
        mSsoHandler = new SsoHandler(WBAuthActivity.this);
        // SSO 授权, ALL IN ONE   如果手机安装了微博客户端则使用客户端授权,没有则进行网页授权
        mSsoHandler.authorize(new SelfWbAuthListener());

//        // SSO 授权, 仅客户端
//        findViewById(R.id.obtain_token_via_sso).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSsoHandler.authorizeClientSso(new SelfWbAuthListener());
//            }
//        });
//
//        // SSO 授权, 仅Web
//        findViewById(R.id.obtain_token_via_web).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSsoHandler.authorizeWeb(new SelfWbAuthListener());
//            }
//        });
//
//        // SSO 授权, ALL IN ONE   如果手机安装了微博客户端则使用客户端授权,没有则进行网页授权
//        findViewById(R.id.obtain_token_via_signature).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSsoHandler.authorize(new SelfWbAuthListener());
//            }
//        });
//
//        // 用户登出
//        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AccessTokenKeeper.clear(getApplicationContext());
//                mAccessToken = new Oauth2AccessToken();
//                updateTokenView(false);
//            }
//        });

        //更新token
        findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(mAccessToken.getRefreshToken())) {
                    AccessTokenKeeper.refreshToken(Constant.APP_KEY, WBAuthActivity.this, new RequestListener() {
                        @Override
                        public void onComplete(String response) {

                        }

                        @Override
                        public void onWeiboException(WeiboException e) {

                        }
                    });
                }
            }
        });

        // 从 SharedPreferences 中读取上次已保存好 AccessToken 等信息，
        // 第一次启动本应用，AccessToken 不可用
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        if (mAccessToken.isSessionValid()) {
            updateTokenView(true);
        }
    }

    /**
     * 当 SSO 授权 Activity 退出时，该函数被调用。
     *
     * @see {@link Activity#onActivityResult}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }

    }

    private class SelfWbAuthListener implements com.sina.weibo.sdk.auth.WbAuthListener {
        @Override
        public void onSuccess(final Oauth2AccessToken token) {
            WBAuthActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAccessToken = token;
                    if (mAccessToken.isSessionValid()) {
                        // 显示 Token
                        updateTokenView(false);
                        // 保存 Token 到 SharedPreferences
                        AccessTokenKeeper.writeAccessToken(WBAuthActivity.this, mAccessToken);
                        Toast.makeText(WBAuthActivity.this,
                                "授权成功", Toast.LENGTH_SHORT).show();

                        final String openId = mAccessToken.getUid();

                        //首先查询该用户有无注册 -- 查询对应的username
                        BmobQuery<AllUserBean> bmobQuery = new BmobQuery<AllUserBean>();
                        //查询mobile叫mPhoneStr的数据
                        bmobQuery.addWhereEqualTo("userId", openId);
                        bmobQuery.findObjects(new FindListener<AllUserBean>() {
                            @Override
                            public void done(List<AllUserBean> list, BmobException e) {
                                if (e == null) {
                                    if (list.size() > 0) { //已经注册到用户系统，直接登陆
                                        Toast.makeText(WBAuthActivity.this, "该微博已注册，请直接登录", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(WBAuthActivity.this, MainActivity.class).putExtra("LoginAccountType", "weibo"));
                                        WBAuthActivity.this.finish();
                                    } else { //没有注册到系统，现在注册
//                                        Toast.makeText(WBAuthActivity.this, "该微博尚未注册", Toast.LENGTH_SHORT).show();
                                        //注册对应用户
                                        AllUserBean userBean = new AllUserBean();
                                        //设置登陆模式
                                        userBean.setSnsType("weibo");
                                        //设置登陆信息
                                        userBean.setPassword("weibo123456"); // 设置密码
                                        userBean.setUsername("WB"+openId); //设置用户名（唯一不变）
//                                        userBean.setMobilePhoneNumber(""); //设置用户登陆手机号
                                        userBean.setCode(0); //设置用户注册验证码
                                        //设置第三方登陆信息
                                        userBean.setAccessToken(mAccessToken.getToken());
                                        userBean.setExpiresIn(mAccessToken.getExpiresTime());
                                        userBean.setUserId(mAccessToken.getUid());
                                        //设置个人信息
                                        userBean.setUserNick("WB"+mAccessToken.getUid());
                                        userBean.setUserAvatar("");
                                        userBean.setSignature("null");
                                        //设置详细信息
                                        userBean.setBirth("1990-01-01");
                                        userBean.setSex("男");
                                        userBean.setArea("北京市-东城区");
                                        userBean.setHeight("170");
                                        userBean.setIDCardType("身份证");
                                        userBean.setIDNumber("");
                                        userBean.signUp(new SaveListener<AllUserBean>() {
                                            @Override
                                            public void done(AllUserBean userBean, cn.bmob.v3.exception.BmobException e) {
                                                if (e == null) {
//                                    LogUtil.i("TAG", "reg success");
                                                    Toast.makeText(getApplicationContext(), "注册成功，已登录", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                    finish();
                                                } else {
                                                    if (e.getErrorCode() == 202) {
                                                        CommonUnits.showToast(getApplicationContext(), "该用户已注册");
                                                    } else {
                                                        CommonUnits.showToast(getApplicationContext(), "注册失败"+e.toString());
//                                                        LogUtil.i(TAG_WB, "reg error=" + e.getMessage());
                                                    }
//                                                    LogUtil.i(TAG_WB, "reg error=" + e.getMessage());
                                                }
                                            }
                                        });
                                        startActivity(new Intent(WBAuthActivity.this, MainActivity.class));
                                        WBAuthActivity.this.finish();
                                    }
                                } else { //注册到系统失败 -- 无动作 --查询失败
                                    Toast.makeText(WBAuthActivity.this, "注册失败，请稍后重试"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });
        }

        @Override
        public void cancel() {
            Toast.makeText(WBAuthActivity.this,
                    "取消授权", Toast.LENGTH_LONG).show();
            startActivity(new Intent(WBAuthActivity.this, LoginActivity.class));
            WBAuthActivity.this.finish();
        }

        @Override
        public void onFailure(WbConnectErrorMessage errorMessage) {
            Toast.makeText(WBAuthActivity.this, errorMessage.getErrorMessage(), Toast.LENGTH_LONG).show();
            startActivity(new Intent(WBAuthActivity.this, LoginActivity.class));
            WBAuthActivity.this.finish();
        }
    }

    /**
     * 显示当前 Token 信息。
     *
     * @param hasExisted 配置文件中是否已存在 token 信息并且合法
     */
    private void updateTokenView(boolean hasExisted) {
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                new java.util.Date(mAccessToken.getExpiresTime()));
        String format = getString(R.string.weibosdk_demo_token_to_string_format_1);
        mTokenText.setText(String.format(format, mAccessToken.getToken(), date));

        String message = String.format(format, mAccessToken.getToken(), date);
        if (hasExisted) {
            message = getString(R.string.weibosdk_demo_token_has_existed) + "\n" + message;
        }
        mTokenText.setText(message);
    }

    // 依次类推，想要获取QQ或者新浪微博其他的信息，开发者可自行根据官方提供的API文档，传入对应的参数即可
    // QQ的API文档地址：http://wiki.open.qq.com/wiki/website/API%E5%88%97%E8%A1%A8
    // 微博的API文档地址：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
    JSONObject obj;

    /**
     * 获取微博的资料
     *
     * @Title: getWeiboInfo
     * @Description: TODO
     * @param
     * @return void
     * @throws
     */
    public void getWeiboInfo() {
        // 根据http://open.weibo.com/wiki/2/users/show提供的API文档
        new Thread() {
            @Override
            public void run() {
                try {
                    obj = new JSONObject();
                    Map<String, String> params = new HashMap<String, String>();
                    if (obj != null) {
                        params.put("access_token", obj.getJSONObject("weibo").getString("access_token"));// 此为微博登陆成功之后返回的access_token
                        params.put("uid",obj.getJSONObject("weibo").getString("uid"));// 此为微博登陆成功之后返回的uid
                    }
                    String result = com.sinntalker.sinntest20180503_yy.Activity.NetUtils.getRequest("https://api.weibo.com/2/users/show.json", params);
                    Log.d("login", "微博的个人信息：" + result);
                    Message msg = new Message();
                    msg.obj = result;
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
//                    handler.sendMessage(msg);

                } catch (Exception e) {
                    // TODO: handle exception
                }
            }

        }.start();
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            String result = (String) msg.obj;
            if (result != null) {
//                tv_info.setText((String) msg.obj);
            } else {
//                tv_info.setText("暂无个人信息");
            }
        };
    };

}
