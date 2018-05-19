package com.sinntalker.sinntest20180503_yy.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.EditText;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sinntalker.sinntest20180503_yy.Common.CommonUnits;
import com.sinntalker.sinntest20180503_yy.Common.Constant;
import com.sinntalker.sinntest20180503_yy.Common.StringUnits;
import com.sinntalker.sinntest20180503_yy.R;
import com.sinntalker.sinntest20180503_yy.UserAuthBean;
import com.sinntalker.sinntest20180503_yy.UserBean;
import com.sinntalker.sinntest20180503_yy.Weibo.WBAuthActivity;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static com.sinntalker.sinntest20180503_yy.Common.Constant.APP_ID_QQ;

public class LoginActivity extends Activity {

    //声明：账号输入框、密码输入框
    EditText mUserNameET; //UserName edit
    EditText mPasswordET; //Password edit

    //声明：登陆按键、忘记密码、注册
    Button mLoginBtn; //Login button
    TextView mResetTV; //Reset textView
    TextView mRegisterTV; //Register textView

    //声明第三方登陆控件
    ImageView mQQ_image;
    ImageView mWX_image;
    ImageView mWB_image;

    //QQ登陆
    public Tencent mTencent;
    public UserInfo mUserInfo;
    private IUiListener loginListener;

    private static final String TAG_QQ = "qq";

    //微信登陆
    public IWXAPI api_wx; //第三方APP和微信通信的openapi接口

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_login);

        //Bmob 第一：默认初始化 -- 应用名称“ThirdLogin
        Bmob.initialize(this, "e9572e950523fd526c4750e301ac444f");
        //初始化微博SDK
        WbSdk.install(this,new AuthInfo(this, Constant.APP_KEY, Constant.REDIRECT_URL,
                Constant.SCOPE));

        //将应用注册到微信



        mUserNameET = findViewById(R.id.id_userName_tv);
        mPasswordET = findViewById(R.id.id_userPassword_tv);

        mLoginBtn = findViewById(R.id.id_login_btn);
        mResetTV = findViewById(R.id.id_reset_tv);
        mRegisterTV = findViewById(R.id.id_register_tv);

        mQQ_image = findViewById(R.id.qq_login_image);
        mWX_image = findViewById(R.id.weixin_login_image);
        mWB_image = findViewById(R.id.weibo_login_image);

        //QQ登陆
        mTencent = Tencent.createInstance(APP_ID_QQ, LoginActivity.this.getApplicationContext());

        //通过WXAPIFactory工厂获取IWXApI的示例
        api_wx = WXAPIFactory.createWXAPI(this, Constant.APP_ID_WX,true);
        //将应用的appid注册到微信
        api_wx.registerApp(Constant.APP_ID_WX);

        //登陆
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "登陆", Toast.LENGTH_SHORT).show();
                String mUserNameStr = mUserNameET.getText().toString().trim().replaceAll("", "");
                String mPasswordStr = mPasswordET.getText().toString().trim();

                if (StringUnits.isEmpty(mUserNameStr)) {
                    CommonUnits.showToast(LoginActivity.this, "用户名不能为空");
                } else if (StringUnits.isEmpty(mPasswordStr)) {
                    CommonUnits.showToast(LoginActivity.this, "密码不能为空");
                } else {
                    CommonUnits.showToast(LoginActivity.this, "用户名： "+mUserNameStr + "\n密码： " + mPasswordStr);

                    UserBean bu = new UserBean();
                    bu.setUsername(mUserNameStr);
                    bu.setPassword(mPasswordStr);
                    bu.login(new SaveListener<BmobUser>() {
                        @Override
                        public void done(BmobUser bmobUser, BmobException e) {
                            if (e == null) {
                                Toast.makeText(getApplicationContext(), "登陆成功！", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                LoginActivity.this.finish();
                            }else {
                                Toast.makeText(getApplicationContext(), "登陆失败", Toast.LENGTH_SHORT).show();
                                mUserNameET.setText("");
                                mUserNameET.setText("");
                            }
                        }
                    });
                }
            }
        });

        //重置密码
        mResetTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "重置密码", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), ResetActivity.class));
                LoginActivity.this.finish();
            }
        });

        //注册
        mRegisterTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "注册", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                LoginActivity.this.finish();
            }
        });

        //QQ登陆
        mQQ_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "QQ登陆", Toast.LENGTH_SHORT).show();
                //get_simple_userinfo
                qqLogin();
            }
        });

        //微信登陆
        mWX_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "微信登陆", Toast.LENGTH_SHORT).show();
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
//                req.scope = "snsapi_login";//提示 scope参数错误，或者没有scope权限
                req.state = "wechat_sdk_微信登录";
                api_wx.sendReq(req);

            }
        });

        //微博登陆
        mWB_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "微博登陆", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, WBAuthActivity.class));
            }
        });

    }

    private void qqLogin() {
        mTencent.login(this, "all", loginListener);
        loginListener = new IUiListener() {
            @Override
            public void onComplete(Object o) { //登录成功后回调该方法,可以跳转相关的页面
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

                JSONObject object = (JSONObject) o;

                try {

                    String accessToken = object.getString("access_token");

                    String expires = object.getString("expires_in");

                    String openID = object.getString("openid");

                    mTencent.setAccessToken(accessToken, expires);

                    mTencent.setOpenId(openID);

                } catch (JSONException e) {

                    e.printStackTrace();

                }
            }

            @Override
            public void onError(UiError uiError) {
                Toast.makeText(LoginActivity.this, "授权出错！", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "授权取消！", Toast.LENGTH_LONG).show();
            }
        };
    }

    /**
     * 在调用Login的Activity或者Fragment中重写onActivityResult方法
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_LOGIN) {

            if (resultCode == -1) {
                Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);

                Tencent.handleResultData(data, loginListener);

                UserInfo info = new UserInfo(this, mTencent.getQQToken());
                info.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object o) {

                        try {
                            JSONObject info = (JSONObject) o;
                            String nickName = info.getString("nickname");//获取用户昵称
                            String iconUrl = info.getString("figureurl_qq_2");//获取用户头像的url

                            Log.i("TAG", "Username" + nickName);

                            UserAuthBean userAuthBean = new UserAuthBean();
                            userAuthBean.setSnsType(TAG_QQ);
                            userAuthBean.setAccessToken(String.valueOf(mTencent.getQQToken()));
                            userAuthBean.setExpiresIn(mTencent.getExpiresIn());
                            userAuthBean.setUserId(mTencent.getOpenId());
                            userAuthBean.setUserName(TAG_QQ);
                            userAuthBean.setUserPassword(mTencent.getOpenId() + "123456");
                            userAuthBean.save(new SaveListener<String>() {
                                @Override
                                public void done(String objectId, BmobException e) {
                                    if (e == null) {
                                        Toast.makeText(getApplicationContext(), "创建数据成功：" + objectId, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                                    }
                                }
                            });

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));


//                            tvUsername.setText(nickName);
//                            Glide.with(MainActivity.this).load(iconUrl).into(icon_image);//Glide解析获取用户头像
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(UiError uiError) {

                        Log.e("GET_QQ_INFO_ERROR", "获取qq用户信息错误");
                        Toast.makeText(LoginActivity.this, "获取qq用户信息错误", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {

                        Log.e("GET_QQ_INFO_CANCEL", "获取qq用户信息取消");
                        Toast.makeText(LoginActivity.this, "获取qq用户信息取消", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Glide.with(LoginActivity.this).load(App.getShared().getString("headUrl","")).into(ivHead);
    }

}
