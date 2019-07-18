package com.truonglam.tl_hotel.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.truonglam.tl_hotel.model.HotelService;
import com.truonglam.tl_hotel.model.HotelServiceResponse;
import com.truonglam.tl_hotel.webservice.Client;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServicesViewModel extends AndroidViewModel {

    private MutableLiveData<List<HotelService>> hotelServices;

    public ServicesViewModel(@NonNull Application application) {
        super(application);
        hotelServices = new MutableLiveData<>();
    }

    public LiveData<List<HotelService>> getHotelServices(String token, String hotelId) {
        Client.getService().getServices(token,hotelId).enqueue(new Callback<HotelServiceResponse>() {
            @Override
            public void onResponse(Call<HotelServiceResponse> call, Response<HotelServiceResponse> response) {
                hotelServices.setValue(response.body().getHotelServices());
            }

            @Override
            public void onFailure(Call<HotelServiceResponse> call, Throwable t) {

            }
        });
        return hotelServices;
    }

    public void setHotelServices(List<HotelService> listService) {
        hotelServices.setValue(listService);
    }
}
