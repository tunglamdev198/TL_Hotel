package com.truonglam.tl_hotel.webservice.reponsitories;

import android.arch.lifecycle.MutableLiveData;

import com.truonglam.tl_hotel.model.HotelService;
import com.truonglam.tl_hotel.model.HotelServiceResponse;
import com.truonglam.tl_hotel.webservice.Client;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServicesRepository {
    private MutableLiveData<List<HotelService>> mLiveData;

    private static ServicesRepository repository;

    private boolean check;

    public static ServicesRepository getInstance() {
        if (repository == null) {
            repository = new ServicesRepository();
        }
        return repository;
    }

    private ServicesRepository() {
        mLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<HotelService>> getServices(String token, String hotelId) {
        Client.getService().getServices(token, hotelId).enqueue(new Callback<HotelServiceResponse>() {
            @Override
            public void onResponse(Call<HotelServiceResponse> call, Response<HotelServiceResponse> response) {
                mLiveData.setValue(response.body().getHotelServices());
            }

            @Override
            public void onFailure(Call<HotelServiceResponse> call, Throwable t) {
                mLiveData.setValue(null);
            }
        });
        return mLiveData;
    }

    public boolean deleteService(String token, String serviceId) {
        Client.getService().deleteService(token, serviceId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    check = true;
                } else {
                    check = false;
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                check = false;
            }
        });
        return check;
    }

}
