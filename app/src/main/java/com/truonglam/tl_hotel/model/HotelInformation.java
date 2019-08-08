package com.truonglam.tl_hotel.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HotelInformation implements Serializable {
    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("hotel_id")
    private String hotelId;

    @SerializedName("name")
    private String name;

    @SerializedName("logo")
    private String logo;

    @SerializedName("tittle")
    private String tittle;


    public HotelInformation(String accessToken, String hotelId, String name, String logo, String tittle) {
        this.accessToken = accessToken;
        this.hotelId = hotelId;
        this.name = name;
        this.logo = logo;
        this.tittle = tittle;

    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
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


    @Override
    public String toString() {
        return "HotelInformation{" +
                "accessToken='" + accessToken + '\'' +
                ", hotelId='" + hotelId + '\'' +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", tittle='" + tittle + '\'' +
                '}' + "\n";
    }
}
