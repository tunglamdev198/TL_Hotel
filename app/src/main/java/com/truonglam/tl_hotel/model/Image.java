package com.truonglam.tl_hotel.model;

import com.google.gson.annotations.SerializedName;

public class Image {
    @SerializedName("id")
    private String backgroundId;

    @SerializedName("link")
    private String link;

    public Image() {
    }

    public Image(String backgroundId, String link) {
        this.backgroundId = backgroundId;
        this.link = link;
    }

    public String getBackgroundId() {
        return backgroundId;
    }

    public void setBackgroundId(String backgroundId) {
        this.backgroundId = backgroundId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
