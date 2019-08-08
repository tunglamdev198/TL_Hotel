package com.truonglam.tl_hotel.model;

import com.google.gson.annotations.SerializedName;

public class Box {
    @SerializedName("id")
    private String boxId;

    @SerializedName("serial")
    private String serial;

    @SerializedName("mac")
    private String mac;

    @SerializedName("is_active")
    private String isActive;

    @SerializedName("tv_package_name")
    private String packageName;

    @SerializedName("phong_id")
    private String phongId;

    public Box() {
    }

    public Box(String boxId, String serial, String mac, String isActive, String packageName, String phongId) {
        this.boxId = boxId;
        this.serial = serial;
        this.mac = mac;
        this.isActive = isActive;
        this.packageName = packageName;
        this.phongId = phongId;
    }

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPhongId() {
        return phongId;
    }

    public void setPhongId(String phongId) {
        this.phongId = phongId;
    }

    @Override
    public String toString() {
        return "Box{" +
                "boxId='" + boxId + '\'' +
                ", serial='" + serial + '\'' +
                ", mac='" + mac + '\'' +
                ", isActive='" + isActive + '\'' +
                ", packageName='" + packageName + '\'' +
                ", phongId='" + phongId + '\'' +
                '}'+"\n";
    }
}
