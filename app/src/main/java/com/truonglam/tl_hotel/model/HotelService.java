package com.truonglam.tl_hotel.model;

import com.google.gson.annotations.SerializedName;

public class HotelService {
    @SerializedName("icon")
    private String icon;

    @SerializedName("color_icon")
    private String colorIcon;

    @SerializedName("tittle")
    private String tittle;

    @SerializedName("link")
    private String link;

    public HotelService() {
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColorIcon() {
        return colorIcon;
    }

    public void setColorIcon(String colorIcon) {
        this.colorIcon = colorIcon;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "HotelService{" +
                "icon='" + icon + '\'' +
                ", colorIcon='" + colorIcon + '\'' +
                ", tittle='" + tittle + '\'' +
                ", link='" + link + '\'' +
                '}'+"\n";
    }
}
