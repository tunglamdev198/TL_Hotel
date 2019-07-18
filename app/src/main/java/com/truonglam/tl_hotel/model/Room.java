package com.truonglam.tl_hotel.model;

import com.google.gson.annotations.SerializedName;

public class Room {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("cumphong_id")
    private String cumphongId;

    public Room() {
    }

    public Room(String id, String name, String cumphongId) {
        this.id = id;
        this.name = name;
        this.cumphongId = cumphongId;
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

    public String getCumphong_id() {
        return cumphongId;
    }

    public void setCumphong_id(String cumphong_id) {
        this.cumphongId = cumphong_id;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", cumphong_id='" + cumphongId + '\'' +
                '}'+"\n";
    }
}
