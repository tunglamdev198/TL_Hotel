package com.truonglam.tl_hotel.webservice;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IService {
    @GET("/api/getToken")
    Call<String> getToken();
}
