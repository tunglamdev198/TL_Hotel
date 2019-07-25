package com.truonglam.tl_hotel.webservice;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
    public static final String BASE_URL = "http://664fccfe.ngrok.io";
    private static Client instance;
    private static IService service;

    public static IService getService(){
        if(instance == null){
            instance = new Client();
        }
        return service;
    }

    private Client(){
        service = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(IService.class);
    }
}
