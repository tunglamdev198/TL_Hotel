package com.truonglam.tl_hotel.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.truonglam.tl_hotel.model.HotelBackground;
import com.truonglam.tl_hotel.webservice.Client;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BackgroundViewModel extends AndroidViewModel {

    private MutableLiveData<HotelBackground> mLiveData;

    public BackgroundViewModel(@NonNull Application application) {
        super(application);
        mLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<HotelBackground> getHotelBackground(String token, String hotelId) {
        Client.getService().getBackground(token, hotelId).enqueue(new Callback<HotelBackground>() {
            @Override
            public void onResponse(Call<HotelBackground> call, Response<HotelBackground> response) {
                if (response.isSuccessful()) {
                    mLiveData.setValue(response.body());
                } else {
                    mLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<HotelBackground> call, Throwable t) {

            }
        });

        return mLiveData;
    }
}
