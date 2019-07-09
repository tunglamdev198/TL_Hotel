package com.truonglam.tl_hotel.model;

import com.google.gson.annotations.SerializedName;

public class RoomCluster {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("check")
    private String check;

    public RoomCluster() {
    }

    public RoomCluster(String id, String name, String check) {
        this.id = id;
        this.name = name;
        this.check = check;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    @Override
    public String toString() {
        return "RoomCluster{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", check='" + check + '\'' +
                '}'+"\n";
    }
}
