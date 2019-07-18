package com.truonglam.tl_hotel.model;

import com.google.gson.annotations.SerializedName;

public class RoomUpdating {
    @SerializedName("phong_id")
    private String room;

    @SerializedName("name")
    private String name;

    public RoomUpdating() {
    }

    public RoomUpdating(String room, String name) {
        this.room = room;
        this.name = name;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
