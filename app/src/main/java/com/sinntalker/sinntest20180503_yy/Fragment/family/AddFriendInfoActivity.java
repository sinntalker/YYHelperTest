package com.sinntalker.sinntest20180503_yy.Fragment.family;

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

import com.sinntalker.sinntest20180503_yy.AllUserBean;
import com.sinntalker.sinntest20180503_yy.Fragment.family.base.BaseActivity;
import com.sinntalker.sinntest20180503_yy.Fragment.family.bean.AddFriendMessage;
import com.sinntalker.sinntest20180503_yy.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class AddFriendInfoActivity extends BaseActivity {

    String strPhone;
    String strNickName;

    //用户
    AllUserBean user;
    //用户信息
    BmobIMUserInfo info;

    ImageView mBackAFIIV; //返回
    ImageView mAvatarAFIIV; //头像
    TextView mUserNickAFITV; //用户昵称
    TextView mPhoneAFITV; //用户手机号
    TextView mSignatureAFITV; //用户个性签名
    Button mAddAFIBtn; //添加好友
    Button mTempContactAFIBtn; //临时会话

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_add_friend_info);

        //实例化
        mBackAFIIV = findViewById(R.id.id_imageView_back_AddFriendInfo);
        mAvatarAFIIV = findViewById(R.id.id_imageView_avatar_addFriendInfo);
        mUserNickAFITV = findViewById(R.id.id_textView_userNick_addFriendInfo);
        mPhoneAFITV = findViewById(R.id.id_textView_phone_addFriendInfo);
        mSignatureAFITV = findViewById(R.id.id_textView_signature_addFriendInfo);
        mAddAFIBtn = findViewById(R.id.id_button_add_addFriendInfo);
        mTempContactAFIBtn = findViewById(R.id.id_button_tempContact_addFriendInfo);

//        strNickName = getIntent().getStringExtra("nickname");
//        strPhone = getIntent().getStringExtra("phone");
        //用户
        user = (AllUserBean) getBundle().getSerializable("u");
        if (user.getObjectId().equals(getCurrentUid())) {//用户为登录用户
            mAddAFIBtn.setVisibility(View.GONE);
            mTempContactAFIBtn.setVisibility(View.GONE);
            Log.i("bmob", "当前用户应该为登陆用户，当前定义user为：" + user);
        } else {//用户为非登录用户
            mAddAFIBtn.setVisibility(View.VISIBLE);
            mTempContactAFIBtn.setVisibility(View.VISIBLE);
            Log.i("bmob", "当前用户应该为非登陆用户，当前定义user为：" + user);
        }

        mUserNickAFITV.setText(user.getUserNick());
        mPhoneAFITV.setText(user.getMobilePhoneNumber());
        mSignatureAFITV.setText(user.getSignature());

        //构造聊天方的用户信息:传入用户id、用户名和用户头像三个参数
        info = new BmobIMUserInfo(user.getObjectId(), user.getUserNick(), user.getUserAvatar());
        Log.i("bmob", "构建聊天方的数据--要添加的好友：objectId:" + user.getObjectId() + "， 用户昵称：" + user.getUserNick()  + "， 用户头像：" + user.getUserAvatar());

        //返回按键点击事件
        mBackAFIIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAddAFIBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("bmob", "AddUserInfoActivity:添加好友");
                sendAddFriendMessage();
            }
        });

        mTempContactAFIBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("bmob", "AddUserInfoActivity:临时会话");
                chat();
            }
        });
    }

    /**
     * 发送添加好友的请求
     */
    //TODO 好友管理：9.7、发送添加好友请求
    private void sendAddFriendMessage() {
        if (BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
            Log.i("bmob","尚未连接IM服务器");
            return;
        }
        //TODO 会话：4.1、创建一个暂态会话入口，发送好友请求
        BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(info, true, null);
        //TODO 消息：5.1、根据会话入口获取消息管理，发送好友请求
        BmobIMConversation messageManager = BmobIMConversation.obtain(BmobIMClient.getInstance(), conversationEntrance);
        AddFriendMessage msg = new AddFriendMessage();
        AllUserBean currentUser = BmobUser.getCurrentUser(AllUserBean.class);
        msg.setContent("很高兴认识你，可以加个好友吗?");//给对方的一个留言信息
        Map<String, Object> map = new HashMap<>();
        map.put("name", currentUser.getUserNick());//发送者姓名(昵称)
        map.put("avatar", currentUser.getUserAvatar());//发送者的头像
        map.put("uid", currentUser.getObjectId());//发送者的uid
        msg.setExtraMap(map);
        messageManager.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage msg, BmobException e) {
                if (e == null) {//发送成功
                    Toast.makeText(AddFriendInfoActivity.this, "好友请求发送成功，等待验证", Toast.LENGTH_SHORT).show();
                } else {//发送失败
                    Toast.makeText(AddFriendInfoActivity.this,"发送失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 与陌生人聊天
     */
    private void chat() {
        if (BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
            Log.i("bmob","尚未连接IM服务器");
            return;
        }
        //TODO 会话：4.1、创建一个常态会话入口，陌生人聊天
        BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(info, null);
        Bundle bundle = new Bundle();
        bundle.putSerializable("c", conversationEntrance);
        Intent intent = new Intent();
        intent.setClass(AddFriendInfoActivity.this, ChatActivity.class);
        if (bundle != null)
            intent.putExtra(getPackageName(), bundle);
        startActivity(intent);
//        startActivity(ChatActivity.class, bundle, false);
    }

    public String getCurrentUid(){
        return BmobUser.getCurrentUser(AllUserBean.class).getObjectId();
    }


}
