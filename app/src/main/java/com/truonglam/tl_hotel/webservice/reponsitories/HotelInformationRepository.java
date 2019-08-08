package com.truonglam.tl_hotel.webservice.reponsitories;

import android.arch.lifecycle.MutableLiveData;

import com.truonglam.tl_hotel.model.HotelInformation;
import com.truonglam.tl_hotel.webservice.Client;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelInformationRepository {

    private static HotelInformationRepository repository;

    private MutableLiveData<HotelInformation> mLiveData;

    public static HotelInformationRepository getInstance() {
        if (repository == null) {
            repository = new HotelInformationRepository();
        }
        return repository;
    }

    public HotelInformationRepository() {
        mLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<HotelInformation> getHotelInformations(String username, String password){
        Client.getService().getInformation(username,password).enqueue(new Callback<HotelInformation>() {
            @Override
            public void onResponse(Call<HotelInformation> call, Response<HotelInformation> response) {
                if (response.isSuccessful()){
                    mLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<HotelInformation> call, Throwable t) {
                    mLiveData.setValue(null);
            }
        });
        return mLiveData;
    }
}
