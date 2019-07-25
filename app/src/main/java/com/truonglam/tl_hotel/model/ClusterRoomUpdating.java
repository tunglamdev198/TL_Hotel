package com.truonglam.tl_hotel.model;

import com.google.gson.annotations.SerializedName;

public class ClusterRoomUpdating {
    @SerializedName("cumphong_id")
    private String id;

    @SerializedName("name")
    private String name;

    public ClusterRoomUpdating() {
    }

    public ClusterRoomUpdating(String id, String name) {
        this.id = id;
        this.name = name;
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
}
