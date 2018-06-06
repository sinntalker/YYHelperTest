package com.sinntalker.sinntest20180503_yy.Fragment.user.about;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.sinntalker.sinntest20180503_yy.R;

import org.apache.http.util.EncodingUtils;

import java.io.InputStream;

public class FunctionActivity extends Activity {

    ImageView mBackFIV;
    TextView mIntroductionFTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_function);

        mBackFIV = findViewById(R.id.id_imageView_back_function);
        mIntroductionFTV = findViewById(R.id.id_textView_introduction_function);

        mBackFIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String res = "";
        try{
            InputStream in = getAssets().open("function.txt");
            int length = in.available();
            byte [] buffer = new byte[length];
            in.read(buffer);
//            res = EncodingUtils.getString(buffer,"UTF-8");//选择合适的编码，如果不调整会乱码
            res = EncodingUtils.getString(buffer,"GBK");//选择合适的编码，如果不调整会乱码
            in.close();

            String text = new String(buffer, "GBK");
            mIntroductionFTV.setText(text);
            mIntroductionFTV.setTextSize(16);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
