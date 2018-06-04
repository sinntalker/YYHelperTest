package com.sinntalker.sinntest20180503_yy.Fragment.user;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.sinntalker.sinntest20180503_yy.Activity.LoginActivity;
import com.sinntalker.sinntest20180503_yy.Activity.MainActivity;
import com.sinntalker.sinntest20180503_yy.Activity.ResetActivity;
import com.sinntalker.sinntest20180503_yy.AllUserBean;
import com.sinntalker.sinntest20180503_yy.Fragment.family.model.UserModel;
import com.sinntalker.sinntest20180503_yy.R;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class UserInfoActivity extends Activity {

    ImageView mBackUIIV; //返回
    ImageView mEditUIIV; //编辑
    ImageView mAvatarUIIV; //头像
    EditText mNickUITV; //昵称
    TextView mPhoneUITV; //手机号
    EditText mSignatureUIET; //个性签名
    TextView mResetPasswordUITV; //重置密码
    TableRow mDetailUITR; //详细信息

    String strObjectId;
    String strUsernick;
    String strSignature;
    String strPhone;

    Boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_user_info);

        isEdit = false;

        AllUserBean mCurrentUser = BmobUser.getCurrentUser(AllUserBean.class);
        strObjectId = mCurrentUser.getObjectId(); //更新数据使用
        strUsernick = mCurrentUser.getUserNick();
        strSignature = mCurrentUser.getSignature();
        strPhone = mCurrentUser.getMobilePhoneNumber();

        mBackUIIV = findViewById(R.id.id_imageView_back_userInfo);
        mEditUIIV = findViewById(R.id.id_imageView_edit_userInfo);
        mAvatarUIIV = findViewById(R.id.id_imageView_avatar_userInfo);
        mNickUITV = findViewById(R.id.id_editText_nick_userInfo);
        mPhoneUITV = findViewById(R.id.id_textView_phone_userInfo);
        mSignatureUIET = findViewById(R.id.id_editText_signature_userInfo);
        mResetPasswordUITV = findViewById(R.id.id_textView_reset_userInfo);
        mDetailUITR = findViewById(R.id.id_tableRow_detail_userInfo);

        mNickUITV.setText(strUsernick);
        mNickUITV.setFocusable(false);
        mNickUITV.setFocusableInTouchMode(false);

        mSignatureUIET.setText(strSignature);
        mSignatureUIET.setFocusable(false);
        mSignatureUIET.setFocusableInTouchMode(false);

        mPhoneUITV.setText(strPhone);

        mBackUIIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mEditUIIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) { //当前可编辑时点击，结果变为不可编辑，更新数据
                    isEdit = !isEdit;

                    mAvatarUIIV.setFocusable(true);
                    mAvatarUIIV.setFocusableInTouchMode(true);

                    mPhoneUITV.setFocusable(true);
                    mPhoneUITV.setFocusableInTouchMode(true);

                    mResetPasswordUITV.setFocusable(true);
                    mResetPasswordUITV.setFocusableInTouchMode(true);

                    mDetailUITR.setFocusable(true);
                    mDetailUITR.setFocusableInTouchMode(true);

                    mNickUITV.setFocusable(false);
                    mNickUITV.setFocusableInTouchMode(false);

                    mSignatureUIET.setFocusable(false);
                    mSignatureUIET.setFocusableInTouchMode(false);

                    mEditUIIV.setImageResource(R.drawable.edit_drug_white);

                    strUsernick = mNickUITV.getText().toString().trim();
                    strSignature = mSignatureUIET.getText().toString().trim();

                    AllUserBean mUser = new AllUserBean();
                    mUser.setUserNick(strUsernick);
                    mUser.setSignature(strSignature);
                    mUser.update(strObjectId, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Toast.makeText(UserInfoActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                                Log.i("bmob","更新成功前ObjectId：" + strObjectId);
                                Log.i("bmob","更新成功");

                                AllUserBean mCurrentUser = BmobUser.getCurrentUser(AllUserBean.class);
                                strObjectId = mCurrentUser.getObjectId(); //更新数据使用
                                strUsernick = mCurrentUser.getUserNick();
                                strSignature = mCurrentUser.getSignature();

                                Log.i("bmob","更新成功后ObjectId：" + strObjectId);

                                mNickUITV.setText(strUsernick);

                                mSignatureUIET.setText(strSignature);
                            }else{
                                Toast.makeText(UserInfoActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
                                Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });

                } else { //当前不可编辑，结果变为可编辑，编辑数据
                    isEdit = !isEdit;

                    mNickUITV.setFocusable(true);
                    mNickUITV.setFocusableInTouchMode(true);

                    mSignatureUIET.setFocusable(true);
                    mSignatureUIET.setFocusableInTouchMode(true);

                    mEditUIIV.setImageResource(R.drawable.finish_drug);

                    mAvatarUIIV.setFocusable(false);
                    mAvatarUIIV.setFocusableInTouchMode(false);

                    mPhoneUITV.setFocusable(false);
                    mPhoneUITV.setFocusableInTouchMode(false);

                    mResetPasswordUITV.setFocusable(false);
                    mResetPasswordUITV.setFocusableInTouchMode(false);

                    mDetailUITR.setFocusable(false);
                    mDetailUITR.setFocusableInTouchMode(false);
                }

            }
        });

        mAvatarUIIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更换头像 -- 拍照或照片
                dialogCamera();
            }
        });

        mPhoneUITV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserInfoActivity.this, "手机号作为唯一标识，不支持修改。", Toast.LENGTH_SHORT).show();
            }
        });

        mResetPasswordUITV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserInfoActivity.this, ResetActivity.class)
                    .putExtra("inputActivity", "userInfo"));
            }
        });

        mDetailUITR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserInfoActivity.this, PersonalInfoActivity.class));
            }
        });
    }

    public void dialogCamera() {
        final Dialog dialog = new Dialog(this, R.style.NormalDialogStyle);
        View view = View.inflate(this, R.layout.dialog_bottom, null);
        TextView camera = view.findViewById(R.id.id_button_camera);
        TextView album = view.findViewById(R.id.id_button_album);
        TextView cancel = view.findViewById(R.id.id_button_cancel);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
//                view.setMinimumHeight((int) (ScreenSizeUtils.getInstance(UserInfoActivity.this).getScreenHeight() * 0.23f));
//                Window dialogWindow = dialog.getWindow();
//                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//                lp.width = (int) (ScreenSizeUtils.getInstance(UserInfoActivity.this).getScreenWidth() * 0.9f);
//                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//                lp.gravity = Gravity.BOTTOM;
//                dialogWindow.setAttributes(lp);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("bmob", "打开相机");
                dialog.dismiss();
            }
        });
        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("bmob", "打开相册");
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("bmob", "取消");
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
