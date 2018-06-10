package com.sinntalker.sinntest20180503_yy.Fragment.Device;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinntalker.sinntest20180503_yy.AllUserBean;
import com.sinntalker.sinntest20180503_yy.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 *我的设备 在设置界面中进入
 */
public class MyDeviceActivity extends Activity {

    ImageView mBackMDIV;
    ListView mDeviceMDLV;
    TextView mAddMDTV;

    private BluetoothDevice mDevice;
    private BluetoothSocket mSocket;
    private InputStream btIs;
    private OutputStream btOs;

    String [] name;
    String [] type;
    String [] address;
    Boolean [] state;
    String msg;

    int idNumber = 0;

    public static final int REQUEST_BT_ENABLE_CODE = 200;
    public static final String BT_UUID = "00001101-0000-1000-8000-00805F9B34FB";//uuid

    private BluetoothAdapter mBluetoothAdapter;//蓝牙适配器
    private BlueToothStateReceiver mReceiver;//广播接收器
    private ConnectThread mConnectThread; //客户端线程
    private AcceptThread mAcceptThread; //服务端线程

    BluetoothDevice device;
    DeviceListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_my_device);

        mBackMDIV = findViewById(R.id.id_imageView_back_myDevice);
        mDeviceMDLV = findViewById(R.id.id_listView_myDeviceList_myDevice);
        mAddMDTV = findViewById(R.id.id_textView_add_myDevice);

        mAddMDTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyDeviceActivity.this, AddDeviceActivity.class));
                finish();
            }
        });

        mBackMDIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        registerRec();

        AllUserBean mCurrent = BmobUser.getCurrentUser(AllUserBean.class);
        BmobQuery<DeviceBean> query = new BmobQuery<DeviceBean>();
        query.addWhereEqualTo("user", mCurrent);
        query.findObjects(new FindListener<DeviceBean>() {
            @Override
            public void done(List<DeviceBean> list, BmobException e) {
                if (e == null) {
                    Log.i("bmob", "设备表查询成功，当前设备数目：" + list.size());
                    if (list.size() == 0) { //数据为空
                        setEmptyView(mDeviceMDLV);
                    } else {
                        name = new String[list.size()];
                        type = new String[list.size()];
                        address = new String[list.size()];
                        state = new Boolean[list.size()];

                        for (int i = 0; i < list.size(); i++) {
                            name[i] = list.get(i).getDeviceName();
                            type[i] = list.get(i).getDeviceType();
                            address[i] = list.get(i).getAddress();
                            state[i] = false;
                        }

                        adapter = new DeviceListAdapter(MyDeviceActivity.this);
                        mDeviceMDLV.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Log.i("bmob", "设备表查询失败，错误原因：" + e.getMessage() + e.getErrorCode());
                    setFaultView(mDeviceMDLV);
                }
            }
        });

    }



    class DeviceListAdapter extends BaseAdapter {
        private Context context;

//                            // 用来存放输入的数据
//                            public HashMap<Integer, Object> inputContainer;
//                            // 这是我继承写的两个监听，没用匿名内部类，首先是为了方便对position这个变量进行操作，再者
//                            // Adapter中我只分别实例化了一个监听对象，这样对内存开支应该会小些，好吧，我是对Java的垃圾回收机制信不过。
//                            private MyFoucus myFoucus;
//                            private MyWatch myWatch;

        public DeviceListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return name.length;
        }

        @Override
        public Object getItem(int position) {
            return name[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            // 创建布局
//                                convertView = LayoutInflater.from(context).inflate(R.layout.list_device_details, parent, false);
//                                convertView = View.inflate(getApplicationContext(), R.layout.list_device_details, null);

            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(context, R.layout.list_device_details, null);
                viewHolder.dName = convertView.findViewById(R.id.id_textView_deviceName_deviceDetail);
                viewHolder.dType = convertView.findViewById(R.id.id_textView_deviceType_deviceDetail);
                viewHolder.dAddress = convertView.findViewById(R.id.id_textView_deviceAddress_deviceDetail);
                viewHolder.dState = convertView.findViewById(R.id.id_textView_deviceBond_deviceDetail);
                viewHolder.dContext = convertView.findViewById(R.id.id_editText_context_deviceDetails);
                viewHolder.dBond = convertView.findViewById(R.id.id_button_isBond_deviceDetail);
                viewHolder.dRefresh = convertView.findViewById(R.id.id_button_refresh_deviceDetails);
                viewHolder.dSend = convertView.findViewById(R.id.id_button_send_deviceDetails);
//                                    viewHolder.dContext.setOnFocusChangeListener(myFoucus);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
//                                convertView.setTag(viewHolder);
//                                viewHolder.dContext.setOnTouchListener(new View.OnTouchListener() {
//
//                                    public boolean onTouch(View view, MotionEvent event) {
//
//                                        if (event.getAction() == MotionEvent.ACTION_UP) {
//                                            index = position;
//                                        }
//                                        return false;
//                                    }
//                                });

//                                viewHolder.dContext.addTextChangedListener(new TextWatcher() {
//
//                                    public void afterTextChanged(Editable editable) {
//                                    }
//
//                                    public void beforeTextChanged(CharSequence text, int start, int count, int after) {
//                                    }
//
//                                    public void onTextChanged(CharSequence text, int start, int before, int count) {
//                                        //如果该edittext有默认内容，还要在if那里进行过滤
//                                        if (index>=0 && text.length() > 0 && index == position ) {
//                                            mData.get(index).put("input", text.toString());
//                                        }
//                                    }
//
//                                });

//                                TestBean bean = new TestBean();
//                                final TestBean bean = name.get(position);
            //把Bean与输入框进行绑定

//                                // setTag是个好东西呀，把position放上去，一会用
//                                viewHolder.dContext.setTag(bean);
//
//                                //清除焦点
//                                viewHolder.dContext.clearFocus();
//
//                                viewHolder.dContext.addTextChangedListener(null); //清除上个item的监听，防止oom
//                                viewHolder.dContext.addTextChangedListener(new TextWatcher() {
//                                    @Override
//                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//                                    @Override
//                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//
//                                        //获得Edittext所在position里面的Bean，并设置数据
//                                        TestBean bean = (TestBean) viewHolder.dContext.getTag();
//
//
//                                        bean.setEditContext( s + "");
//                                    }
//                                    @Override
//                                    public void afterTextChanged(Editable s) { }
//                                });
//
//                                //大部分情况下，Adapter里面有if必须有else
//                                if(!TextUtils.isEmpty(bean.getEditContext())){
//                                    viewHolder.dContext.setText(bean.getEditContext());
//                                }else{
//                                    viewHolder.dContext.setText("");
//                                }

//                                View currentFocus = ((Activity) context).getCurrentFocus();
//                                if (currentFocus != null) {
//                                    currentFocus.clearFocus();
//                                }

            // 为了实现最小的内存开销，复用两个不同的监听对象，通过每次点击的事件来修正mywatch中的position；s

            // 使用remove和add来区别开复用修正和手动添加；之所以费劲的加个remove又加个add也是为了能尽量减少些
            // 思考量，剔除修正EditText时的TextChange监听事件，整个世界都清净了。。。
//                                viewHolder.dContext.removeTextChangedListener(myWatch);
//                                if (inputContainer.containsKey(position)) {
//                                    viewHolder.dContext.setText(inputContainer.get(position).toString());
//                                } else {
//                                    viewHolder.dContext.setText("");
//                                }
//                                viewHolder.dContext.addTextChangedListener(myWatch);

            viewHolder.dName.setText(name[position]);
            viewHolder.dType.setText(type[position]);
            viewHolder.dAddress.setText(address[position]);

            if (!mSocket.isConnected()) {
                state[position] = true;
            }

            //将输入框与msg相绑定
//                                viewHolder.dContext.setTag(msg);
//                                msg = viewHolder.dContext.getText().toString().trim();
//                                viewHolder.dContext.addTextChangedListener(null); //清除上个item的监听，防止oom
//                                viewHolder.dContext.addTextChangedListener(new TextWatcher() {
//                                    @Override
//                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                                    }
//
//                                    @Override
//                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//
//                                        //获得Edittext所在position里面的Bean，并设置数据
////                                        Bean bean = (Bean) vh.mEditText.getTag();
////                                        msg = viewHolder.dContext.getTag();
//
//
////                                        bean.setInput(s+"");
//                                    }
//
//                                    @Override
//                                    public void afterTextChanged(Editable s) {
//                                    }
//                                });

            if (state[position]) {
                viewHolder.dState.setText("已连接");
            } else {
                viewHolder.dState.setText("未连接");
            }

            viewHolder.dBond.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("bmob", "连接我的当前设备" + name[position]);
                    if (!state[position]) {
                        //连接设备： 打开蓝牙搜索当前设备，匹配当前蓝牙地址、 然后进行连接、连接后state[position] 为真，改变当前状态
                        idNumber = position;
                        openBT();
//                                            mMessageAdapter.addMessage("打开蓝牙");
                        Log.i("bmob", "打开蓝牙");
                        Toast.makeText(MyDeviceActivity.this, "打开蓝牙", Toast.LENGTH_SHORT).show();
                        if (mAcceptThread == null && mBluetoothAdapter != null) {
                            mAcceptThread = new AcceptThread();
                            mAcceptThread.start();
//                                                mMessageAdapter.addMessage("启动服务线程");
                            Log.i("bmob", "启动服务线程");
                        }
                        if (mBluetoothAdapter != null) {
//                                                mRvAdapter.clearDevices();//开始搜索前清空上一次的列表
                            mBluetoothAdapter.startDiscovery();
//                                                mMessageAdapter.addMessage("开始搜索蓝牙");
                            Log.i("bmob", "开始搜索蓝牙");
                            Toast.makeText(MyDeviceActivity.this, "开始搜索设备", Toast.LENGTH_SHORT).show();
                        } else {
                            openBT();
                            if (mBluetoothAdapter != null) {
//                                                    mRvAdapter.clearDevices();//开始搜索前清空上一次的列表
                                mBluetoothAdapter.startDiscovery();
//                                                    mMessageAdapter.addMessage("开始搜索蓝牙");
                                Toast.makeText(MyDeviceActivity.this, "开始搜索设备", Toast.LENGTH_SHORT).show();
                                Log.i("bmob", "蓝牙未打开时，开始搜索蓝牙");
                            }
                        }
//                                            mConnectThread = new ConnectThread(device);
//                                            mConnectThread.start();
                        if (state[position]) {
                            viewHolder.dState.setText("已连接");
                        } else {
                            viewHolder.dState.setText("未连接");
                        }
                    }
                }
            });

            viewHolder.dRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (state[position]) {
                        Log.i("bmob", "刷新我的当前设备的数据" + name[position]);
                        Toast.makeText(MyDeviceActivity.this, "刷新当前设备数据", Toast.LENGTH_SHORT).show();
//                                            msg = viewHolder.dContext.getText().toString().trim();
                        msg = "id002\r\n";
                        Log.i("bmob", "发送数据" + msg);
//                        mConnectThread = new ConnectThread(device);
                        Log.i("bmob", "当前设备" + device.getName() + "\n"
                            + "设备地址" + device.getAddress() + "\n"
                            + "设备uuid" + device.getUuids() + "\n"
                            + "设备绑定状态" + device.getBondState() + "\n");
//                        mConnectThread.start();

                        if (TextUtils.isEmpty(msg)) {
                            Toast.makeText(MyDeviceActivity.this, "消息为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        new SendInfoTask().execute(msg);

//                        mConnectThread.write(msg);
//                        if (mConnectThread != null) {//证明我主动去链接别人了
//                            mConnectThread.write(msg);
//                        } else if (mAcceptThread != null) {
//                            mAcceptThread.write(msg);
//                        }
                    } else {
                        Log.i("bmob", "设备未连接，无法更新数据" + name[position]);
                        Toast.makeText(MyDeviceActivity.this, "设备未连接", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            viewHolder.dSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    msg = viewHolder.dContext.getText().toString();
                    if (TextUtils.isEmpty(msg)) {
                        Toast.makeText(MyDeviceActivity.this, "消息为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    msg += "\r\n";

                    new SendInfoTask().execute(msg);
//                    mConnectThread = new ConnectThread(device);
//                    mConnectThread.start();
////                    Log.i("bmob", "当前发送数据：" + msg);
////                    Log.i("bmob", "当前设备" + device.getName() + "\n"
////                            + "设备地址" + device.getAddress() + "\n"
////                            + "设备uuid" + device.getUuids() + "\n"
////                            + "设备绑定状态" + device.getBondState() + "\n");
//                    if (mConnectThread != null) {//证明我主动去链接别人了
//                        mConnectThread.write(msg);
//                    } else if (mAcceptThread != null) {
//                        mAcceptThread.write(msg);
//                    }
                }
            });

//                                viewHolder.dContext.clearFocus();
//
//                                if (index != -1 && index == position) {
//
//                                    viewHolder.dContext.requestFocus();
//                                }

            return convertView;
        }

//                            class MyFoucus implements View.OnFocusChangeListener {
//                                // 当获取焦点时修正myWatch中的position值,这是最重要的一步!
//                                @Override
//                                public void onFocusChange(View v, boolean hasFocus) {
//                                    if (hasFocus) {
//                                        int position = (int) v.getTag();
//                                        myWatch.position = position;
//                                    }
//                                }
//                            }
//
//                            class MyWatch implements TextWatcher {
//                                // 不得不吐槽一下这里，java的内部类机制怎么就和我想的不一样呢，外部依然能很轻松的访问这个“私有
//                                // 化的”position，我是不是该去看看《think in java》了。
//                                private int position;
//
//                                @Override
//                                public void afterTextChanged(Editable s) {
//                                    inputContainer.put(position, s.toString());
//                                }
//
//                                @Override
//                                public void beforeTextChanged(CharSequence s, int start, int count,
//                                                              int after) {
//                                    // TODO Auto-generated method stub
//
//                                }
//
//                                @Override
//                                public void onTextChanged(CharSequence s, int start, int before,
//                                                          int count) {
//                                    // TODO Auto-generated method stub
//
//                                }
//
//                            }

        class ViewHolder {
            EditText dContext;
            TextView dName;
            TextView dType;
            TextView dAddress;
            TextView dState;
            Button dBond;
            Button dRefresh;
            Button dSend;
//                                public ViewHolder(View convertView) {
//
//                                }
//                            TextView time;  //测量体重时间
//                            TextView weight; //测量体重数据
        }
    }

    protected <T extends View> T setEmptyView(ListView listView) {
        TextView emptyView = new TextView(this);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        emptyView.setText("暂无数据！");
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) listView.getParent()).addView(emptyView);
        listView.setEmptyView(emptyView);
        return (T) emptyView;
    }

    protected <T extends View> T setFaultView(ListView listView) {
        TextView emptyView = new TextView(this);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        emptyView.setText("查询数据库失败！");
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) listView.getParent()).addView(emptyView);
        listView.setEmptyView(emptyView);
        return (T) emptyView;
    }

    private void openBT() {
        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        //1.设备不支持蓝牙，结束应用
        if (mBluetoothAdapter == null) {
            finish();
            return;
        }
        //2.判断蓝牙是否打开
        if (!mBluetoothAdapter.enable()) {
            //没打开请求打开
            Intent btEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(btEnable, REQUEST_BT_ENABLE_CODE);
        }
    }

    private void registerRec() {
        //3.注册蓝牙广播
        mReceiver = new BlueToothStateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);//搜多到蓝牙
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//搜索结束
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_BT_ENABLE_CODE) {
            if (resultCode == RESULT_OK) {
                //用户允许打开蓝牙
//                mMessageAdapter.addMessage("用户同意打开蓝牙");
                Log.i("bmob", "用户同意打开蓝牙");
            } else if (resultCode == RESULT_CANCELED) {
                //用户取消打开蓝牙
//                mMessageAdapter.addMessage("用户拒绝打开蓝牙");
                Log.i("bmob", "用户拒绝打开蓝牙");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    class BlueToothStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Toast.makeText(AddDeviceActivity.this, "触发广播", Toast.LENGTH_SHORT).show();
            String action = intent.getAction();
//            BluetoothDevice btDevice = null;
//            btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            switch (action) {
                case BluetoothDevice.ACTION_FOUND:

//                    Toast.makeText(AddDeviceActivity.this, "找到设备" + device.getName(), Toast.LENGTH_SHORT).show();
//                    mMessageAdapter.addMessage("找到设备" + device.getName() + device.getAddress());

                    if (device.getAddress().equals(address[idNumber])) { //找到设备

                        if (device.getBondState() == BluetoothDevice.BOND_NONE) {

                            Log.e("ywq", "attemp to bond:"+"["+device.getName()+"]");
                            try {
                                //ͨ��������ClsUtils,����createBond����
                                ClsUtils.createBond(device.getClass(), device);
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                        Log.i("bmob", "找到设备" + device.getName() + device.getAddress());
//                        switch (device.getBondState()) {
//                            case BluetoothDevice.BOND_NONE:
//                                Log.e(getPackageName(), "取消配对");
//                                Log.i("bmob", "取消配对");
//                                break;
//                            case BluetoothDevice.BOND_BONDING:
//                                Log.e(getPackageName(), "配对中");
//                                Log.i("bmob", "配对中");
//                                break;
//                            case BluetoothDevice.BOND_BONDED:
//                                Log.e(getPackageName(), "配对成功");
//                                Log.i("bmob", "配对成功");
//                                break;
//                        }
                        mConnectThread = new ConnectThread(device);
                        mConnectThread.start();

                        state[idNumber] = true;
                        adapter.notifyDataSetChanged();

//                        mDeviceMDLV.notify();

//                    if (mRvAdapter != null) {
//                        mRvAdapter.addDevice(device);
                        Log.i("bmob", "蓝牙基本信息，蓝牙名称：" + device.getName() + "\n"
                                +  "device.getAddress()：" + device.getAddress() + "\n"
//                                +  "device.getType()：" + device.getType() + "\n"
                                +  " device.getBluetoothClass()：" + device.getBluetoothClass() + "\n"
                                +  "device.getBondState()：" + device.getBondState() + "\n"
                                +  "device.getUuids()：" + device.getUuids());
//                    }
                    }
//                    else if (device.getName().contains("HC-05")){
//                        if (device.getBondState() == BluetoothDevice.BOND_NONE) {
//
//                            Log.i("bmob", "找到设备，尝试进行配对" + device.getName() + device.getAddress());
//                            try {
//                                ClsUtils.createBond(device.getClass(), device);
//
//                                mConnectThread = new ConnectThread(device);
//                                mConnectThread.start();
//
//                                state[idNumber] = true;
//                            } catch (Exception e) {
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                            }
//                        }
//                    }
                    else {
                        Log.i("bmob", "没有找到设备");
                    }

                    break;
                case "android.bluetooth.device.action.PAIRING_REQUEST":
//                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if(device.getName().contains("HC-05"))
                    {
                        try {
                            ClsUtils.setPairingConfirmation(device.getClass(), device, true);
                            Log.i("order...", "isOrderedBroadcast:"+isOrderedBroadcast()+",isInitialStickyBroadcast:"+isInitialStickyBroadcast());
                            abortBroadcast();
                            boolean ret = ClsUtils.setPin(device.getClass(), device, "1234");
                            mConnectThread = new ConnectThread(device);
                            mConnectThread.start();

                            state[idNumber] = true;

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
//                    mMessageAdapter.addMessage("搜索结束");
                    Log.i("bmob", "搜索结束");
                    Toast.makeText(MyDeviceActivity.this, "搜索结束", Toast.LENGTH_SHORT).show();
//                    mSearchDeviceBtn.setText("搜索结束");
                    break;
            }
        }
    }

    //发送数据到蓝牙设备的异步任务
    class SendInfoTask extends AsyncTask<String,String,String>
    {

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

//            statusLabel.setText(result);
            Log.i("bmob", "发送信息" + result);
            //将发送框清空
//            etSend.setText("");
        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub

            if(mSocket==null)
            {
                return "还没有创建连接";
            }

            if(arg0[0].length()>0)//不是空白串
            {
                //String target=arg0[0];

                byte[] msgBuffer = arg0[0].getBytes();

                try {
                    //  将msgBuffer中的数据写到outStream对象中
                    btOs.write(msgBuffer);

                } catch (IOException e) {
                    Log.e("error", "ON RESUME: Exception during write.", e);
                    return "发送失败";
                }

            }

            return "发送成功";
        }

    }

    class ConnectThread extends Thread {

        private boolean canRecv;
        private PrintWriter writer;

        public ConnectThread(BluetoothDevice device) {
            mDevice = device;
            canRecv = true;
        }

        @Override
        public void run() {
            if (mDevice != null) {
                try {
                    //获取套接字
                    BluetoothSocket temp = mDevice.createInsecureRfcommSocketToServiceRecord(UUID.fromString(BT_UUID));
                    //mDevice.createRfcommSocketToServiceRecord(UUID.fromString(BT_UUID));//sdk 2.3以下使用
                    mSocket = temp;
                    //发起连接请求
                    if (mSocket != null) {
                        mSocket.connect();
                    }
                    sendHandlerMsg("连接 " + mDevice.getName() + "成功！");
                    Log.i("bmob", "连接蓝牙成功：" + mDevice.getName());
//                    state = 1;
//                    Toast.makeText(AddDeviceActivity.this, "连接 " + mDevice.getName() + "成功！", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(AddDeviceActivity.this, "成功创建设备：" + mDevice.getName(), Toast.LENGTH_SHORT).show();
                    //获取输入输出流
                    btIs = mSocket.getInputStream();
                    btOs = mSocket.getOutputStream();

                    //通讯-接收消息
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(btIs, "UTF-8"));
                    BufferedReader reader = new BufferedReader(new InputStreamReader(btIs, "GBK"));
                    String content = null;
                    while (canRecv) {
                        content = reader.readLine();
                        sendHandlerMsg("收到消息：" + content);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    sendHandlerMsg("错误：" + e.getMessage());
                } finally {
                    try {
                        if (mSocket != null) {
                            mSocket.close();
                        }
                        //btIs.close();//两个输出流都依赖socket，关闭socket即可
                        //btOs.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        sendHandlerMsg("错误：" + e.getMessage());
                    }
                }
            }
        }

        private void sendHandlerMsg(String content) {
            Message msg = mHandler.obtainMessage();
            msg.what = 1001;
            msg.obj = content;
            mHandler.sendMessage(msg);
        }

        public void write(String msg) {
            if (btOs != null) {
                try {
                    if (writer == null) {
//                        writer = new PrintWriter(new OutputStreamWriter(btOs, "UTF-8"), true);
                        writer = new PrintWriter(new OutputStreamWriter(btOs, "GBK"), true);
                    }
                    writer.println(msg);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    writer.close();
                    sendHandlerMsg("错误：" + e.getMessage());
                }
            }
        }
    }

    private static Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
//            mMessageAdapter.addMessage((String) msg.obj);
        }
    };

    class AcceptThread extends Thread {
        private BluetoothServerSocket mServerSocket;
        private BluetoothSocket mSocket;
        private InputStream btIs;
        private OutputStream btOs;
        private PrintWriter writer;
        private boolean canAccept;
        private boolean canRecv;

        public AcceptThread() {
            canAccept = true;
            canRecv = true;
        }

        @Override
        public void run() {
            try {
                //获取套接字
                BluetoothServerSocket temp = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("TEST", UUID.fromString(BT_UUID));
                mServerSocket = temp;
                //监听连接请求 -- 作为测试，只允许连接一个设备
                if (mServerSocket != null) {
                    // while (canAccept) {
                    mSocket = mServerSocket.accept();
                    sendHandlerMsg("有客户端连接");
                    // }
                }
                //获取输入输出流
                btIs = mSocket.getInputStream();
                btOs = mSocket.getOutputStream();
                //通讯-接收消息
//                BufferedReader reader = new BufferedReader(new InputStreamReader(btIs, "UTF-8"));
                BufferedReader reader = new BufferedReader(new InputStreamReader(btIs, "GBK"));
                String content = null;
                while (canRecv) {
                    content = reader.readLine();
                    sendHandlerMsg("收到消息：" + content);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (mSocket != null) {
                        mSocket.close();
                    }
                    // btIs.close();//两个输出流都依赖socket，关闭socket即可
                    // btOs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    sendHandlerMsg("错误：" + e.getMessage());
                }
            }
        }

        private void sendHandlerMsg(String content) {
            Message msg = mHandler.obtainMessage();
            msg.what = 1001;
            msg.obj = content;
            mHandler.sendMessage(msg);
        }

        public void write(String msg) {
            if (btOs != null) {
                try {
                    if (writer == null) {
//                        writer = new PrintWriter(new OutputStreamWriter(btOs, "UTF-8"), true);
                        writer = new PrintWriter(new OutputStreamWriter(btOs, "GBK"), true);
                    }
                    writer.println(msg);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    writer.close();
                    sendHandlerMsg("错误：" + e.getMessage());
                }
            }
        }
    }

}
