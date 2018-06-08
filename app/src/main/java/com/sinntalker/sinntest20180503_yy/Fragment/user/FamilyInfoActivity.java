package com.sinntalker.sinntest20180503_yy.Fragment.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.sinntalker.sinntest20180503_yy.AllUserBean;
import com.sinntalker.sinntest20180503_yy.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class FamilyInfoActivity extends Activity {

    ImageView mBackUIIV; //返回
//    ImageView mEditUIIV; //编辑
    ImageView mAvatarUIIV; //头像
    TextView mNickUITV; //昵称
    TextView mPhoneUITV; //手机号
    TableRow mDetailUITR; //详细信息

    String strObjectId;
    String strUsernick;
    String strSignature;
    String strPhone;

    Boolean isEdit;

    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_family_info);

        type = getIntent().getStringExtra("family");

        mBackUIIV = findViewById(R.id.id_imageView_back_familyInfo);
//        mEditUIIV = findViewById(R.id.id_imageView_edit_familyInfo);
        mAvatarUIIV = findViewById(R.id.id_imageView_avatar_familyInfo);
        mNickUITV = findViewById(R.id.id_textView_nick_familyInfo);
        mPhoneUITV = findViewById(R.id.id_textView_phone_familyInfo);
        mDetailUITR = findViewById(R.id.id_tableRow_detail_familyInfo);

        mNickUITV.setText(type);

        mBackUIIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mDetailUITR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FamilyInfoActivity.this, PersonalInfoActivity.class)
                    .putExtra("family", type));
            }
        });

        AllUserBean user = BmobUser.getCurrentUser(AllUserBean.class);
        //查询条件一：用户名称
        BmobQuery<FamilyBean> queryUserName = new BmobQuery<FamilyBean>();
        queryUserName.addWhereEqualTo("user",user);

        //查询条件二：药箱编号
        BmobQuery<FamilyBean> queryDrugBoxNum = new BmobQuery<FamilyBean>();
        queryDrugBoxNum.addWhereEqualTo("relations",type);

        //最后查询时完整的条件
        List<BmobQuery<FamilyBean>> allQueries = new ArrayList<BmobQuery<FamilyBean>>();
        allQueries.add(queryUserName);
        allQueries.add(queryDrugBoxNum);

        //查询
        BmobQuery<FamilyBean> query = new BmobQuery<FamilyBean>();
        query.and(allQueries);
        query.findObjects(new FindListener<FamilyBean>() {
            @Override
            public void done(List<FamilyBean> list, BmobException e) {
                if (e == null) {
                    Log.i("bmob", "查询家人信息成功！");
                    mPhoneUITV.setText(list.get(0).getPhone());
                } else {
                    Log.i("bmob", "查询家人信息失败！" + e.getMessage() + e.getErrorCode());
                }
            }
        });
    }
}
