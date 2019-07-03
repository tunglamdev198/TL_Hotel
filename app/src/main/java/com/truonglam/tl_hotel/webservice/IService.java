package com.truonglam.tl_hotel.webservice;

import com.truonglam.tl_hotel.model.HotelInformation;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IService {

    @GET("/api/user/login")
    Call<HotelInformation> getInformation(@Query("username") String username,
                                          @Query("password") String password);
}