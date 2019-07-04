package com.truonglam.tl_hotel.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.truonglam.tl_hotel.model.HotelInformation;
import com.truonglam.tl_hotel.webservice.Client;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelInformationViewModel extends AndroidViewModel {
    private MutableLiveData<HotelInformation> mHotelInformationLiveData;

    public HotelInformationViewModel(@NonNull Application application) {
        super(application);
        mHotelInformationLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<HotelInformation> getHotelInformation(String username, String password) {
        Client.getService().getInformation(username, password).enqueue(new Callback<HotelInformation>() {
            @Override
            public void onResponse(Call<HotelInformation> call, Response<HotelInformation> response) {
                HotelInformation hotelInformation = response.body();
                mHotelInformationLiveData.setValue(hotelInformation);
            }

            @Override
            public void onFailure(Call<HotelInformation> call, Throwable t) {

            }
        });
        return mHotelInformationLiveData;
    }

    public void setHotelInformation(HotelInformation hotelInformation) {
        mHotelInformationLiveData.setValue(hotelInformation);
    }
}
