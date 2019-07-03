package com.truonglam.tl_hotel.webservice;

import com.truonglam.tl_hotel.model.HotelInformation;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.POST;

public interface IService {
    @POST("/getHotelInfomationl")
    Call<HotelInformation> getHotelInfomaation(@Field("username") String username,
                                               @Field("password") String password);
}