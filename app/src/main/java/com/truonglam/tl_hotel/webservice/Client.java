package com.truonglam.tl_hotel.webservice;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
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
                .baseUrl("http://http://bcc90fc1.ngrok.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(IService.class);
    }
}
