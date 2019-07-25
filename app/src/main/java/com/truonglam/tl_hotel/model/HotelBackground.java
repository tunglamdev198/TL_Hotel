package com.truonglam.tl_hotel.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class HotelBackground implements Serializable {
    @SerializedName("id")
    private String id;

    @SerializedName("link")
    private List<String> links;

    public HotelBackground() {
    }

    public HotelBackground(String id, List<String> links) {
        this.id = id;
        this.links = links;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return "HotelBackground{" +
                "id='" + id + '\'' +
                ", links=" + links +
                '}'+"\n";
    }
}
