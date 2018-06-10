package com.sinntalker.sinntest20180503_yy.Fragment.Device;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2018/6/10.
 */

public class BluetoothDeviceBean extends BmobObject {

    private String bluetoothName;
    private String address;
    private String uuid;
    private String bluetoothClass;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBluetoothName() {
        return bluetoothName;
    }

    public void setBluetoothName(String bluetoothName) {
        this.bluetoothName = bluetoothName;
    }

    public String getBluetoothClass() {
        return bluetoothClass;
    }

    public void setBluetoothClass(String bluetoothClass) {
        this.bluetoothClass = bluetoothClass;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
