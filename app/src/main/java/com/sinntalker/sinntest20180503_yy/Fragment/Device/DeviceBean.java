package com.sinntalker.sinntest20180503_yy.Fragment.Device;

import com.sinntalker.sinntest20180503_yy.AllUserBean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2018/6/10.
 */

public class DeviceBean extends BmobObject {

    private AllUserBean user;
    private String deviceName;
    private String bluetoothName;
    private String address;
    private String uuid;
    private String bluetoothClass;
    private String deviceType; //设备类型：智能药箱

    public AllUserBean getUser() {
        return user;
    }

    public void setUser(AllUserBean user) {
        this.user = user;
    }

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

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
