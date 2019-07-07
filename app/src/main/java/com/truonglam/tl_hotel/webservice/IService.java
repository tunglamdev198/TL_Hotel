package com.truonglam.tl_hotel.webservice;

import com.truonglam.tl_hotel.model.HotelInformation;
import com.truonglam.tl_hotel.model.HotelServiceResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IService {

    @GET("/api/user/login")
    Call<HotelInformation> getInformation(@Query("username") String username,
                                          @Query("password") String password);

    @GET("/api/user/getService")
    Call<HotelServiceResponse> getServices(@Header("token") String token,
                                           @Query("id") String hotelId);

    @POST("/api/user/changePassword")
    Call<HotelInformation> changePassword(@Header("token") String token,
                                          @Query("username") String username,
                                          @Query("password")String password,
                                          @Query("newpassword")String newPassword);

}