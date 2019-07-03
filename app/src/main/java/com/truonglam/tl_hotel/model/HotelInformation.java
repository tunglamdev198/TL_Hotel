package com.truonglam.tl_hotel.model;

import com.google.gson.annotations.SerializedName;

public class HotelInformation {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;

    @SerializedName("logo")
    private String logo;

    @SerializedName("tittle")
    private String tittle;

    @SerializedName("type")
    private boolean type;

    public HotelInformation(String id, String name, String logo, String tittle, boolean type) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.tittle = tittle;
        this.type = type;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "HotelInformation{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", tittle='" + tittle + '\'' +
                ", type=" + type +
                '}' + "\n";
    }
}
