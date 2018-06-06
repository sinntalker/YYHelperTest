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
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.sinntalker.sinntest20180503_yy.Activity.LoginActivity;
import com.sinntalker.sinntest20180503_yy.Activity.MainActivity;
import com.sinntalker.sinntest20180503_yy.Fragment.family.model.UserModel;
import com.sinntalker.sinntest20180503_yy.Fragment.user.SettingsActivity;
import com.sinntalker.sinntest20180503_yy.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import cn.bmob.newim.BmobIM;

/**
 *  添加设备，由健康管理处进入
 */

public class AddDeviceActivity extends Activity implements AdapterView.OnItemClickListener {

    ImageView mBackIV;
    ListView mDeviceListLV;
    Button mSearchDeviceBtn;

    boolean clickNum = false; // 判断是否打开蓝牙

    private ArrayList<BluetoothDevice> strArr; //设备列表
    private ListViewAdapter adapter;
    BluetoothAdapter bluetoothAdapter;
    private UUID uuid = UUID.fromString("00001106-0000-1000-8000-00805F9B34FB");

    private static final int REQUEST_PERMISSION_ACCESS_LOCATION = 1;

    private TextView state;

    private static final int startService = 0;
    private static final int getMessageOk = 1;
    private static final int sendOver = 2;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case startService:
                    state.setText("服务已打开");
                    break;
                case getMessageOk:
                    state.setText(msg.obj.toString());
                    break;
                case sendOver:
                    Toast.makeText(AddDeviceActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_add_device);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        state = (TextView) findViewById(R.id.state);
        mBackIV = findViewById(R.id.id_imageView_back_addDevice);
        mDeviceListLV = findViewById(R.id.id_listview_deviceResult_addDevice);
        mSearchDeviceBtn = findViewById(R.id.id_button_search_addDevice);

        //检测当前蓝牙是否开启
        if (!bluetoothAdapter.isEnabled()) {
            clickNum = false;
            mSearchDeviceBtn.setText("搜索设备");
            requestPermission();
        } else {
            clickNum = true;
            mSearchDeviceBtn.setText("搜索中...");
            requestPermission();
        }

        strArr = new ArrayList<>();
        adapter = new ListViewAdapter();
        adapter.notifyDataSetChanged();
        mDeviceListLV.setAdapter(adapter);
//        mDeviceListLV.notifyAll();
        mDeviceListLV.setOnItemClickListener(this);

        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(new BluetoothReceiver(), intentFilter);

        mBackIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothAdapter.disable();
                finish();
            }
        });

//        设置按钮的监听方法
        mSearchDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickNum = !clickNum; //转换关系
                if (clickNum) { //开启蓝牙，进行搜索
                    if (bluetoothAdapter == null) {
                        // Device does not support Bluetooth
                        Toast.makeText(AddDeviceActivity.this, "当前设备不支持蓝牙", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!bluetoothAdapter.isEnabled()) { //蓝牙未开启
                        bluetoothAdapter.enable();//异步的，不会等待结果，直接返回。
                        bluetoothAdapter.startDiscovery();
                        mSearchDeviceBtn.setText("搜索中...");
                        Log.i("bmobx", "开启蓝牙");
                        adapter.notifyDataSetChanged();
                        requestPermission();
                    } else { //蓝牙已开启
                        bluetoothAdapter.startDiscovery();
                        mSearchDeviceBtn.setText("搜索中...");
                        adapter.notifyDataSetChanged();
                        Log.i("bmobx", "搜索中...");
                        requestPermission();
                    }

                } else { //关闭蓝牙
                    customDialog();
                }
            }
        });

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
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("bmob", "关闭蓝牙");
                mSearchDeviceBtn.setText("正在关闭蓝牙...");
                bluetoothAdapter.disable();
                mSearchDeviceBtn.setText("搜索设备");
                adapter.notifyDataSetChanged();
                Log.i("bmobx", "搜索设备");
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkAccessFinePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            if (checkAccessFinePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_PERMISSION_ACCESS_LOCATION);
                Log.e(getPackageName(), "没有权限，请求权限");
                return;
            }
            Log.e(getPackageName(), "已有定位权限");
            search();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e(getPackageName(), "开启权限permission granted!");
                    //做下面该做的事
                    search();
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e(getPackageName(), "没有定位权限，请先开启!");
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void search() {
        if (bluetoothAdapter.isDiscovering())
            bluetoothAdapter.cancelDiscovery();

        bluetoothAdapter.startDiscovery();
        Log.e(getPackageName(), "开始搜索");
    }

    public void getBindDevice() {
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        strArr.clear();
        strArr.addAll(bondedDevices);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

        Log.i("bmob", "单击某个蓝牙：" + strArr.get(i).getName());
        if (bluetoothAdapter.isDiscovering())
            bluetoothAdapter.cancelDiscovery();
        if (strArr.get(i).getBondState() == BluetoothDevice.BOND_NONE) {
            bond(i);
        } else if (strArr.get(i).getBondState() == BluetoothDevice.BOND_BONDED) {
            sendMessage(i);
        }
    }

    private void getMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream is = null;
                try {
                    BluetoothServerSocket serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("serverSocket", uuid);
                    mHandler.sendEmptyMessage(startService);
                    BluetoothSocket accept = serverSocket.accept();
                    is = accept.getInputStream();

                    byte[] bytes = new byte[1024];
                    int length = is.read(bytes);

                    Message msg = new Message();
                    msg.what = getMessageOk;
                    msg.obj = new String(bytes, 0, length);
                    mHandler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void sendMessage(final int i) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OutputStream os = null;
                try {
                    BluetoothSocket socket = strArr.get(i).createRfcommSocketToServiceRecord(uuid);
                    socket.connect();
                    os = socket.getOutputStream();
                    os.write("testMessage".getBytes());
                    os.flush();
                    mHandler.sendEmptyMessage(sendOver);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void bond(int i) {
        try {
            Method method = BluetoothDevice.class.getMethod("createBond");
            Log.e(getPackageName(), "开始配对");
            method.invoke(strArr.get(i));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return strArr.size();
        }

        @Override
        public Object getItem(int i) {
            return strArr.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(AddDeviceActivity.this).inflate(android.R.layout.simple_list_item_1, viewGroup, false);
            BluetoothDevice device = strArr.get(i);
            ((TextView) view).setText(device.getName() + " " + device.getAddress() + " ----- " + (device.getBondState() == BluetoothDevice.BOND_BONDED ? "已绑定" : "未绑定"));
            Log.i("bmob", "当前设备信息：" + i + "\n" + device.getName() + "\n"
                    + device.getAddress() + "\n"
                    + device.getUuids() + "\n"
                    + device.getBondState() + "\n"
                    + device.getBluetoothClass() + "\n"
                    + device.getType() + "\n"
                    + device.toString() + "\n" );

            return view;
        }
    }

    class BluetoothReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(BluetoothDevice.ACTION_FOUND)) {
                Log.e(getPackageName(), "找到新设备了");

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.i("bmob", "找到新设备了"  + device.getName() + "\n"
                        + device.getAddress() + "\n"
                        + device.getUuids() + "\n"
                        + device.getBondState() + "\n"
                        + device.getBluetoothClass() + "\n"
                        + device.toString() + "\n" );
                boolean addFlag = true;
                for (BluetoothDevice bluetoothDevice : strArr) {
                    if (device.getAddress().equals(bluetoothDevice.getAddress())) {
                        addFlag = false;
                    }
                }

                if (addFlag) {
                    strArr.add(device);
                    adapter.notifyDataSetChanged();
                }
            } else if (intent.getAction().equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_NONE:
                        Log.e(getPackageName(), "取消配对");
                        Log.i("bmob", "取消配对");
                        break;
                    case BluetoothDevice.BOND_BONDING:
                        Log.e(getPackageName(), "配对中");
                        Log.i("bmob", "配对中");
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        Log.e(getPackageName(), "配对成功");
                        Log.i("bmob", "配对成功");
                        break;
                }

            } else if (intent.getAction().equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                Log.i("bmob", "搜索结束");
                mSearchDeviceBtn.setText("搜索结束");
            }
        }
    }

}
