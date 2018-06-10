package com.sinntalker.sinntest20180503_yy.Fragment.Device;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
import cn.bmob.v3.listener.SaveListener;

/**
 *  添加设备，由健康管理处进入
 */

public class AddDeviceActivity extends Activity implements View.OnClickListener {

    ImageView mBackIV;
    ListView mDeviceListLV;
    Button mSearchDeviceBtn;

    public int state = 0; //连接蓝牙成功时置1

    public static final int REQUEST_BT_ENABLE_CODE = 200;
    public static final String BT_UUID = "00001101-0000-1000-8000-00805F9B34FB";//uuid

    private BluetoothAdapter mBluetoothAdapter;//蓝牙适配器
    private BlueToothStateReceiver mReceiver;//广播接收器
    private ConnectThread mConnectThread; //客户端线程
    private AcceptThread mAcceptThread; //服务端线程

    private RecyclerView mRecyclerView;
    private RvAdapter mRvAdapter;

    private RecyclerView mMessageView;
    private static MsgAdapter mMessageAdapter;

    private static Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            mMessageAdapter.addMessage((String) msg.obj);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_add_device);

        mBackIV = findViewById(R.id.id_imageView_back_addDevice);
//        mDeviceListLV = findViewById(R.id.id_listview_deviceResult_addDevice);
//        mSearchDeviceBtn = findViewById(R.id.id_button_search_addDevice);

        mBackIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                bluetoothAdapter.disable();
                finish();
            }
        });

        //这是我是为了6.0以上的设备能搜索到结果，动态申请了位置权限。但是没有处理结果，因为我测试肯定点同意~- -
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
        }

        initUI();
        registerRec();
//        设置按钮的监听方法
//        mSearchDeviceBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clickNum = !clickNum; //转换关系
//                if (clickNum) { //开启蓝牙，进行搜索
//                    if (bluetoothAdapter == null) {
//                        // Device does not support Bluetooth
//                        Toast.makeText(AddDeviceActivity.this, "当前设备不支持蓝牙", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    if (!bluetoothAdapter.isEnabled()) { //蓝牙未开启
//                        bluetoothAdapter.enable();//异步的，不会等待结果，直接返回。
//                        bluetoothAdapter.startDiscovery();
//                        mSearchDeviceBtn.setText("搜索中...");
//                        Log.i("bmobx", "开启蓝牙");
//                        adapter.notifyDataSetChanged();
//                        requestPermission();
//                    } else { //蓝牙已开启
//                        bluetoothAdapter.startDiscovery();
//                        mSearchDeviceBtn.setText("搜索中...");
//                        adapter.notifyDataSetChanged();
//                        Log.i("bmobx", "搜索中...");
//                        requestPermission();
//                    }

//                } else { //关闭蓝牙
//                    customDialog();
//                }
//            }
//        });

    }

    private void initUI() {

        findViewById(R.id.open).setOnClickListener(this);
        findViewById(R.id.close).setOnClickListener(this);
        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.stop).setOnClickListener(this);
//        findViewById(R.id.send).setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.devices);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRvAdapter = new RvAdapter(this);
        mRecyclerView.setAdapter(mRvAdapter);
        mRvAdapter.setOnItemClickListener(new RvAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onClick(final BluetoothDevice device) {
                BmobQuery<BluetoothDeviceBean> query = new BmobQuery<BluetoothDeviceBean>();
                query.addWhereEqualTo("address", device.getAddress());
                query.findObjects(new FindListener<BluetoothDeviceBean>() {
                    @Override
                    public void done(List<BluetoothDeviceBean> list, BmobException e) {
                        if (e == null) {
                            if (list.size() == 0) {
                                Toast.makeText(AddDeviceActivity.this, "当前设备不可用", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.i("bmob", "开始连接蓝牙" + device.getName() + device.toString());
                                Log.i("bmob", "蓝牙基本信息，蓝牙名称：" + device.getName() + "\n"
                                        +  "device.getAddress()：" + device.getAddress() + "\n"
                                        +  "device.getType()：" + device.getType() + "\n"
                                        +  " device.getBluetoothClass()：" + device.getBluetoothClass() + "\n"
                                        +  "device.getBondState()：" + device.getBondState() + "\n"
                                        +  "device.getUuids()：" + device.getUuids() + "\n"
                                        +  "device.getUuids()：" + device.getUuids().toString().trim()
                                );
                                mConnectThread = new ConnectThread(device);
                                mConnectThread.start();

                                if (state == 1) {
                                    String msg = "id001\r\n";
                                    mConnectThread.write(msg);
                                    mMessageAdapter.addMessage("发送消息：" + msg);
                                    Log.i("bmob", "蓝牙发送消息：" + msg);
                                }

                                AllUserBean mCurrent = BmobUser.getCurrentUser(AllUserBean.class);
                                DeviceBean deviceBean = new DeviceBean();
                                deviceBean.setDeviceName("我的智能药箱");
                                deviceBean.setUser(mCurrent);
                                deviceBean.setBluetoothName(device.getName());
                                deviceBean.setAddress(device.getAddress());
                                deviceBean.setDeviceType("智能药箱");
                                deviceBean.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e == null) {
                                            Log.i("bmob", "设备信息保存成功。");
                                        } else {
                                            Log.i("bmob", "当前设备信息保存失败" + e.getMessage() + e.getErrorCode());
                                        }
                                    }
                                });
                            }
                        } else {
                            Log.i("bmob", "当前设备不可用");
                        }
                    }
                });


//                startActivity(new Intent(AddDeviceActivity.this, DeviceDetailActivity.class)
//                    .putExtra("name", device.getName())
//                    .putExtra("address", device.getAddress()));
            }
        });

        mMessageView = (RecyclerView) findViewById(R.id.msglist);
        mMessageView.setLayoutManager(new LinearLayoutManager(this));
        mMessageAdapter = new MsgAdapter(this);
        mMessageView.setAdapter(mMessageAdapter);
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
                mMessageAdapter.addMessage("用户同意打开蓝牙");
                Log.i("bmob", "用户同意打开蓝牙");
            } else if (resultCode == RESULT_CANCELED) {
                //用户取消打开蓝牙
                mMessageAdapter.addMessage("用户拒绝打开蓝牙");
                Log.i("bmob", "用户拒绝打开蓝牙");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 自定义对话框
     */
    private void customDialog() {
        final Dialog dialog = new Dialog(this, R.style.NormalDialogStyle);
        View view = View.inflate(this, R.layout.dialog_normal, null);
        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        TextView confirm = (TextView) view.findViewById(R.id.confirm);
        TextView context = view.findViewById(R.id.context);
        dialog.setContentView(view);
        //使得点击对话框外部不消失对话框
        dialog.setCanceledOnTouchOutside(false);
        context.setText("您要关闭蓝牙吗？");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("bmobx", "取消关闭蓝牙");
                Log.i("bmob", "取消关闭蓝牙");
//                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("bmob", "关闭蓝牙");
                mSearchDeviceBtn.setText("正在关闭蓝牙...");
//                bluetoothAdapter.disable();
                mSearchDeviceBtn.setText("搜索设备");
//                adapter.notifyDataSetChanged();
                Log.i("bmobx", "搜索设备");
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.open:
                openBT();
                mMessageAdapter.addMessage("打开蓝牙");
                Log.i("bmob", "打开蓝牙");
                Toast.makeText(AddDeviceActivity.this, "打开蓝牙", Toast.LENGTH_SHORT).show();
                if (mAcceptThread == null && mBluetoothAdapter != null) {
                    mAcceptThread = new AcceptThread();
                    mAcceptThread.start();
                    mMessageAdapter.addMessage("启动服务线程");
                    Log.i("bmob", "启动服务线程");
                }
                break;
            case R.id.close:
                Log.i("bmob", "关闭蓝牙");
                if (mBluetoothAdapter.isDiscovering()) {
                    mBluetoothAdapter.cancelDiscovery();
                    Toast.makeText(AddDeviceActivity.this, "停止搜索", Toast.LENGTH_SHORT).show();
                }
                mBluetoothAdapter.disable();
                Toast.makeText(AddDeviceActivity.this, "关闭蓝牙", Toast.LENGTH_SHORT).show();
                Log.i("bmob", "关闭蓝牙");
                break;
            case R.id.start:
                if (!mBluetoothAdapter.isEnabled()) {
                    openBT();
                }
                if (mAcceptThread == null && mBluetoothAdapter != null) {
                    mAcceptThread = new AcceptThread();
                    mAcceptThread.start();
                    mMessageAdapter.addMessage("启动服务线程");
                    Log.i("bmob", "启动服务线程");
                }
                if (mBluetoothAdapter != null) {
                    mRvAdapter.clearDevices();//开始搜索前清空上一次的列表
                    mBluetoothAdapter.startDiscovery();
                    mMessageAdapter.addMessage("开始搜索蓝牙");
                    Log.i("bmob", "开始搜索蓝牙");
                    Toast.makeText(AddDeviceActivity.this, "开始搜索蓝牙", Toast.LENGTH_SHORT).show();
                } else {
                    openBT();
                    if (mBluetoothAdapter != null) {
                        mRvAdapter.clearDevices();//开始搜索前清空上一次的列表
                        mBluetoothAdapter.startDiscovery();
                        mMessageAdapter.addMessage("开始搜索蓝牙");
                        Toast.makeText(AddDeviceActivity.this, "开始搜索蓝牙", Toast.LENGTH_SHORT).show();
                        Log.i("bmob", "蓝牙未打开时，开始搜索蓝牙");
                    }
                }
                break;
            case R.id.stop:
                if (mBluetoothAdapter != null && mBluetoothAdapter.isDiscovering()) {
                    mBluetoothAdapter.cancelDiscovery();
                    Toast.makeText(AddDeviceActivity.this, "停止搜索", Toast.LENGTH_SHORT).show();
                    Log.i("bmob", "停止搜索");
                }
                break;
//            case R.id.send:
//                /**
//                 * 如何区分我是使用服务端的socket还是使用客户端的socket发送消息
//                 *  1. 在单一环境下，手机一般作为客户端，外围设备是服务器。所以手机完全可以不用创建服务器，不存在这个问题。
//                 *  2. 假如是两台手机用来聊天，可分别充当服务器和客户端，那就是发起连接方（即点击设备列表连接）作为客户端。
//                 *  3. 假如我链接了别人，另一个人又连接了我，那我怎么区分？那你写两个界面啊~你要回复其他客户端发来的消息就用服务器的socket
//                 *  否则就用客户端的。我这里偷懒了，一但我主动连接别人，相当于我就关闭服务端了，不给别人连我了。
//                 *  4. 那那些蓝牙对战游戏都怎么区分的？你发现蓝牙对战需要一个人先创建房间没？那个人就是服务端，其他都是客户端，没这个问题。
//                 */
////                String msg = inputEt.getText().toString();
//                String msg = "Test Message";
//                if (TextUtils.isEmpty(msg)) {
//                    Toast.makeText(this, "消息为空", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (mConnectThread != null) {//证明我主动去链接别人了
//                    mConnectThread.write(msg);
//                } else if (mAcceptThread != null) {
//                    mAcceptThread.write(msg);
//                }
//                mMessageAdapter.addMessage("发送消息：" + msg);
//                break;
        }
    }

    class BlueToothStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Toast.makeText(AddDeviceActivity.this, "触发广播", Toast.LENGTH_SHORT).show();
            String action = intent.getAction();
            switch (action) {
                case BluetoothDevice.ACTION_FOUND:
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                    Toast.makeText(AddDeviceActivity.this, "找到设备" + device.getName(), Toast.LENGTH_SHORT).show();
                    mMessageAdapter.addMessage("找到设备" + device.getName() + device.getAddress());
                    Log.i("bmob", "找到设备" + device.getName() + device.getAddress());
                    if (mRvAdapter != null) {
                        mRvAdapter.addDevice(device);
                        Log.i("bmob", "蓝牙基本信息，蓝牙名称：" + device.getName() + "\n"
                                +  "device.getAddress()：" + device.getAddress() + "\n"
//                                +  "device.getType()：" + device.getType() + "\n"
                                +  " device.getBluetoothClass()：" + device.getBluetoothClass() + "\n"
                                +  "device.getBondState()：" + device.getBondState() + "\n"
                                +  "device.getUuids()：" + device.getUuids());
                    }
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    mMessageAdapter.addMessage("搜索结束");
                    Log.i("bmob", "搜索结束");
                    Toast.makeText(AddDeviceActivity.this, "搜索结束", Toast.LENGTH_SHORT).show();
//                    mSearchDeviceBtn.setText("搜索结束");
                    break;
            }
        }
    }

    class ConnectThread extends Thread {
        private BluetoothDevice mDevice;
        private BluetoothSocket mSocket;
        private InputStream btIs;
        private OutputStream btOs;
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
                    state = 1;
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
                    state = 0;
                    e.printStackTrace();
                    sendHandlerMsg("错误：" + e.getMessage());
                } finally {
                    try {
                        if (mSocket != null) {
                            mSocket.close();
                        }
                        state = 0;
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

//    protected void onDestroy()
//    {
//        super.onDestroy();
//        if (this.mBtAdapter != null) {
//            this.mBtAdapter.cancelDiscovery();
//        }
//        unregisterReceiver(this.mReceiver);
//    }

    //    private void requestPermission() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            int checkAccessFinePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
//            if (checkAccessFinePermission != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        REQUEST_PERMISSION_ACCESS_LOCATION);
//                Log.e(getPackageName(), "没有权限，请求权限");
//                return;
//            }
//            Log.e(getPackageName(), "已有定位权限");
//            search();
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case 1: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Log.e(getPackageName(), "开启权限permission granted!");
//                    //做下面该做的事
//                    search();
//                    adapter.notifyDataSetChanged();
//                } else {
//                    Log.e(getPackageName(), "没有定位权限，请先开启!");
//                }
//            }
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//
//    public void search() {
//        if (bluetoothAdapter.isDiscovering())
//            bluetoothAdapter.cancelDiscovery();
//
//        bluetoothAdapter.startDiscovery();
//        Log.e(getPackageName(), "开始搜索");
//    }
//
//    public void getBindDevice() {
//        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
//        strArr.clear();
//        strArr.addAll(bondedDevices);
//        adapter.notifyDataSetChanged();
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
//
//        Log.i("bmob", "单击某个蓝牙：" + strArr.get(i).getName());
//        if (bluetoothAdapter.isDiscovering())
//            bluetoothAdapter.cancelDiscovery();
//        if (strArr.get(i).getBondState() == BluetoothDevice.BOND_NONE) {
//            bond(i);
//        } else if (strArr.get(i).getBondState() == BluetoothDevice.BOND_BONDED) {
//            sendMessage(i);
//        }
//    }
//
//    private void getMessage() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                InputStream is = null;
//                try {
//                    BluetoothServerSocket serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("serverSocket", uuid);
//                    mHandler.sendEmptyMessage(startService);
//                    BluetoothSocket accept = serverSocket.accept();
//                    is = accept.getInputStream();
//
//                    byte[] bytes = new byte[1024];
//                    int length = is.read(bytes);
//
//                    Message msg = new Message();
//                    msg.what = getMessageOk;
//                    msg.obj = new String(bytes, 0, length);
//                    mHandler.sendMessage(msg);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//
//    private void sendMessage(final int i) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                OutputStream os = null;
//                try {
//                    BluetoothSocket socket = strArr.get(i).createRfcommSocketToServiceRecord(uuid);
//                    socket.connect();
//                    os = socket.getOutputStream();
//                    os.write("testMessage".getBytes());
//                    os.flush();
//                    mHandler.sendEmptyMessage(sendOver);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//
//
//    private void bond(int i) {
//        try {
//            Method method = BluetoothDevice.class.getMethod("createBond");
//            Log.e(getPackageName(), "开始配对");
//            method.invoke(strArr.get(i));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    class ListViewAdapter extends BaseAdapter {
//
//        @Override
//        public int getCount() {
//            return strArr.size();
//        }
//
//        @Override
//        public Object getItem(int i) {
//            return strArr.get(i);
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return i;
//        }
//
//        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
//        @Override
//        public View getView(int i, View view, ViewGroup viewGroup) {
//            view = LayoutInflater.from(AddDeviceActivity.this).inflate(android.R.layout.simple_list_item_1, viewGroup, false);
//            BluetoothDevice device = strArr.get(i);
//            ((TextView) view).setText(device.getName() + " " + device.getAddress() + " ----- " + (device.getBondState() == BluetoothDevice.BOND_BONDED ? "已绑定" : "未绑定"));
//            Log.i("bmob", "当前设备信息：" + i + "\n" + device.getName() + "\n"
//                    + device.getAddress() + "\n"
//                    + device.getUuids() + "\n"
//                    + device.getBondState() + "\n"
//                    + device.getBluetoothClass() + "\n"
//                    + device.getType() + "\n"
//                    + device.toString() + "\n" );
//
//            return view;
//        }
//    }
//
//    class BluetoothReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            if (intent.getAction().equals(BluetoothDevice.ACTION_FOUND)) {
//                Log.e(getPackageName(), "找到新设备了");
//
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                Log.i("bmob", "找到新设备了"  + device.getName() + "\n"
//                        + device.getAddress() + "\n"
//                        + device.getUuids() + "\n"
//                        + device.getBondState() + "\n"
//                        + device.getBluetoothClass() + "\n"
//                        + device.toString() + "\n" );
//                boolean addFlag = true;
//                for (BluetoothDevice bluetoothDevice : strArr) {
//                    if (device.getAddress().equals(bluetoothDevice.getAddress())) {
//                        addFlag = false;
//                    }
//                }
//
//                if (addFlag) {
//                    strArr.add(device);
//                    adapter.notifyDataSetChanged();
//                }
//            } else if (intent.getAction().equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                switch (device.getBondState()) {
//                    case BluetoothDevice.BOND_NONE:
//                        Log.e(getPackageName(), "取消配对");
//                        Log.i("bmob", "取消配对");
//                        break;
//                    case BluetoothDevice.BOND_BONDING:
//                        Log.e(getPackageName(), "配对中");
//                        Log.i("bmob", "配对中");
//                        break;
//                    case BluetoothDevice.BOND_BONDED:
//                        Log.e(getPackageName(), "配对成功");
//                        Log.i("bmob", "配对成功");
//                        break;
//                }
//
//            } else if (intent.getAction().equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
//                Log.i("bmob", "搜索结束");
//                mSearchDeviceBtn.setText("搜索结束");
//            }
//        }
//    }

}
