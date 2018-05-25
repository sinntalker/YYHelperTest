package com.sinntalker.sinntest20180503_yy.Fragment.DrugBox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sinntalker.sinntest20180503_yy.AllUserBean;
import com.sinntalker.sinntest20180503_yy.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class DrugBoxActivity extends Activity {

    private ImageView mBackDBIV;
    private TextView scan;
    private TextView manualAddDrug;
    private TextView mDrugBoxNumTV;
    private TextView mDrugNumShowTV;

    String num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_drug_box);

        mBackDBIV = findViewById(R.id.backToHealth_tools_image);
        scan = findViewById(R.id.scan_textView);
        manualAddDrug = findViewById(R.id.manualAdd_textView);
        mDrugBoxNumTV = findViewById(R.id.drugBoxNum_textView);
        mDrugNumShowTV = findViewById(R.id.drugNumShow_textView);

        //获取当前药箱编号
        num = getIntent().getStringExtra("DrugBoxNum");
        mDrugBoxNumTV.setText("药箱 "+ num + " ");

        //--获取当前用户名，方便查找对应数据
        final AllUserBean userBean = BmobUser.getCurrentUser(AllUserBean.class);
        final String boxNum = num; //获取当前药箱编号
        final String username = userBean.getUsername(); //获取当前用户名称

        //初始化 获取 当前药箱药品种类和数目
        initDrugBoxCurrent(username, boxNum);
        mDrugNumShowTV.setText("当前共有 种药物");

        //按键操作
        mBackDBIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DrugBoxActivity.this, "扫描药品条形码", Toast.LENGTH_LONG).show();
//                startActivity(new Intent(getApplicationContext(), AddDrugScanActivity.class));
//                IntentIntegrator integrator = new IntentIntegrator(DrugBoxActivity.this);
//                // 设置要扫描的条码类型，ONE_D_CODE_TYPES：一维码，QR_CODE_TYPES-二维码
//                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
//                integrator.setCaptureActivity(ScanActivity.class);
//                integrator.setPrompt("请扫描"); //底部的提示文字，设为""可以置空
//                integrator.setCameraId(0); //前置或者后置摄像头
//                integrator.setBeepEnabled(false); //扫描成功的「哔哔」声，默认开启
//                integrator.setBarcodeImageEnabled(true);
//                integrator.initiateScan();
            }
        });

        manualAddDrug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DrugBoxActivity.this, "手动添加药品", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), AddDrugHandyActivity.class);
                intent.putExtra("drug_user", username);
                intent.putExtra("drug_boxNum", boxNum);
                getApplicationContext().startActivity(intent);
                finish();
            }
        });

    }

    public void initDrugBoxCurrent(final String username, final String boxNum) {
        //获取药品数据

        //根据用户名和药箱编号查询数据
        //查询条件1 用户名
        BmobQuery<DrugDataBean> query_eq1 = new BmobQuery<DrugDataBean>();
        query_eq1.addWhereEqualTo("userName", username);
        //查询条件2 药箱编号
        BmobQuery<DrugDataBean> query_eq2 = new BmobQuery<DrugDataBean>();
        query_eq2.addWhereEqualTo("boxNumber", boxNum);

        //最后组装完整的and条件
        List<BmobQuery<DrugDataBean>> queries = new ArrayList<BmobQuery<DrugDataBean>>();
        queries.add(query_eq1);
        queries.add(query_eq2);

        BmobQuery<DrugDataBean> query = new BmobQuery<DrugDataBean>();
        query.and(queries);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(10);
        //执行查询方法
        query.findObjects(new FindListener<DrugDataBean>() {
            @Override
            public void done(List<DrugDataBean> object, BmobException e) {
                if(e==null){
//                    Toast.makeText(getApplicationContext(), "查询成功：共"+object.size()+"条数据。", Toast.LENGTH_LONG).show();

                    mDrugNumShowTV.setText("当前共有 " + object.size() + " 种药物");

                    final String [] drugName = new String[object.size()];
                    final String [] drugDosage = new String[object.size()];

                    for (int i = 0; i < object.size(); i ++) {
                        drugName[i] = object.get(i).getGenericName(); //获取药品名称
                        drugDosage[i] = object.get(i).getDosage(); //获取药品用法用量
                    }

                    class DrugListAdapter extends BaseAdapter {
                        private Context context ;
                        public DrugListAdapter(Context context){
                            this.context = context;
                        }

                        @Override
                        public int getCount() {
                            return drugName.length;
                        }

                        @Override
                        public Object getItem(int position) {
                            return drugName[position];
                        }

                        @Override
                        public long getItemId(int position) {
                            return position;
                        }
                        ViewHolder viewHolder;

                        @Override
                        public View getView(final int position, View convertView, ViewGroup parent) {
//                            LayoutInflater inflater = LayoutInflater.from(context);
//                            convertView = inflater.inflate(R.layout.listview_item_drug, null);//实例化一个布局文件
                            // 创建布局
                            convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_item_drug, parent, false);
                            viewHolder = new ViewHolder();
                            viewHolder.drugName_text = (TextView) convertView.findViewById(R.id.drug_name_textView);
                            viewHolder.drugInfo_image = (ImageView) convertView.findViewById(R.id.info_drug_image);
                            viewHolder.setAlarm_btn = (Button) convertView.findViewById(R.id.settingAlarm_button);
                            viewHolder.drugName_text.setText(drugName[position]);
                            viewHolder.drugInfo_image.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(getApplicationContext(), "药品信息", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), DrugDetailsActivity.class); //打开药品详细信息页面
                                    intent.putExtra("drug_user", username);//当前用户 username
                                    intent.putExtra("drug_boxNum", boxNum);//当前药箱 boxNum
                                    intent.putExtra("drug_genericName", drugName[position]);//药品通用名称 genericName
                                    getApplicationContext().startActivity(intent);
                                }
                            });
                            viewHolder.setAlarm_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(getApplicationContext(), "设置提醒", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), SetAlarmActivity.class); //打开设置提醒页面
//                                    intent.putExtra("drug_user", phone);//传送数据 药品所有者 -- phone
                                    intent.putExtra("drug_user", username);//当前用户 username
                                    intent.putExtra("drug_boxNum", boxNum);//当前药箱 boxNum
                                    intent.putExtra("drug_genericName", drugName[position]);//药品通用名称 genericName
                                    intent.putExtra("drug_dosage", drugDosage[position]); //传送数据 药品用法用量 dosage
                                    getApplicationContext().startActivity(intent);
                                }
                            });
                            return convertView;
                        }

                        class ViewHolder{
                            TextView drugName_text; //药品名称
                            ImageView drugInfo_image; //药品详细信息
                            Button setAlarm_btn; //设置提醒按键
                        }
                    }
                    //通过ID获取listView
                    ListView listView = (ListView) findViewById(R.id.drugs_listView);

                    //设置listView的Adapter
                    listView.setAdapter(new DrugListAdapter(getApplicationContext()));
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                    Toast.makeText(getApplicationContext(), "查询失败："+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "扫码取消！", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "扫描成功，条码值: " + result.getContents(), Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), AddDrugScanActivity.class).putExtra("ScanResult", result.getContents()));
//                withdrawResult.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
