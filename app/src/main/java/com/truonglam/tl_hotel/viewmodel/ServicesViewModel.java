package com.truonglam.tl_hotel.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.truonglam.tl_hotel.model.HotelService;

import java.util.List;

public class ServicesViewModel extends AndroidViewModel {

    private MutableLiveData<List<HotelService>> hotelServices;

    public ServicesViewModel(@NonNull Application application) {
        super(application);
        hotelServices = new MutableLiveData<>();
    }

    public LiveData<List<HotelService>> getHotelServices() {
        return hotelServices;
    }

    public void setHotelServices(List<HotelService> listService) {
        hotelServices.setValue(listService);
    }
}
