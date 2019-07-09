package com.truonglam.tl_hotel.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RoomClusterResponse {
    @SerializedName("data")
    private List<RoomCluster> roomClusters;

    @SerializedName("messeage")
    private String messeage;

    public RoomClusterResponse() {
    }

    public RoomClusterResponse(List<RoomCluster> roomClusters, String messeage) {
        this.roomClusters = roomClusters;
        this.messeage = messeage;
    }

    public List<RoomCluster> getRoomClusters() {
        return roomClusters;
    }

    public void setRoomClusters(List<RoomCluster> roomClusters) {
        this.roomClusters = roomClusters;
    }

    public String getMesseage() {
        return messeage;
    }

    public void setMesseage(String messeage) {
        this.messeage = messeage;
    }

    @Override
    public String toString() {
        return "RoomClusterResponse{" +
                "roomClusters=" + roomClusters +
                ", messeage='" + messeage + '\'' +
                '}'+"\n";
    }

}
