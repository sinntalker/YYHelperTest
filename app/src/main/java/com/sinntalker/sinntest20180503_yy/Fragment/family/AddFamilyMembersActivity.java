package com.sinntalker.sinntest20180503_yy.Fragment.family;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.sinntalker.sinntest20180503_yy.R;

public class AddFamilyMembersActivity extends Activity {

    ImageView mBackAFMAIV;
    EditText mPhoneSearchAFMAET;
    Button mPhoneSearchAFMABtn;
    ListView mFamilyMemListAFMALV;

    String strPhoneSearch = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_add_family_members);

        mBackAFMAIV = findViewById(R.id.id_imageView_back_addFamilyMembers);
        mPhoneSearchAFMAET = findViewById(R.id.id_editView_phoneSearch_addFamilyMembers);
        mPhoneSearchAFMABtn = findViewById(R.id.id_button_phoneSearch_addFamilyMembers);
        mFamilyMemListAFMALV = findViewById(R.id.id_listView_familyMembersList_addFamilyMembers);

        mBackAFMAIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mPhoneSearchAFMABtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strPhoneSearch = mPhoneSearchAFMAET.getText().toString().trim();

                if (strPhoneSearch.length() == 0) {
                    Toast.makeText(getApplicationContext(), "请输入添加好友手机号", Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });
    }
}
