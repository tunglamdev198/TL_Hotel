package com.truonglam.tl_hotel.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HotelServiceResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<HotelService> hotelServices;

    public HotelServiceResponse() {
    }

    public HotelServiceResponse(String message, List<HotelService> hotelServices) {
        this.message = message;
        this.hotelServices = hotelServices;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<HotelService> getHotelServices() {
        return hotelServices;
    }

    @Override
    public String toString() {
        return "HotelServiceResponse{" +
                "message='" + message + '\'' +
                ", hotelServices=" + hotelServices +
                '}';
    }

    public void setHotelServices(List<HotelService> hotelServices) {
        this.hotelServices = hotelServices;
    }
}
