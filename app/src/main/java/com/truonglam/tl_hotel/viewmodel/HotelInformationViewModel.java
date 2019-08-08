package com.truonglam.tl_hotel.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.truonglam.tl_hotel.model.HotelInformation;
import com.truonglam.tl_hotel.webservice.Client;
import com.truonglam.tl_hotel.webservice.reponsitories.HotelInformationRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelInformationViewModel extends AndroidViewModel {
    private MutableLiveData<HotelInformation> mLiveData;

    public HotelInformationViewModel(@NonNull Application application) {
        super(application);
        mLiveData = new MutableLiveData<>();
    }

//    public void init(String username, String password){
//        repository = HotelInformationRepository.getInstance();
//        mLiveData = repository.getHotelInformations(username,password);
//    }

    public MutableLiveData<HotelInformation> getHotelInformation(String username, String password) {
        Client.getService().getInformation(username, password).enqueue(new Callback<HotelInformation>() {
            @Override
            public void onResponse(Call<HotelInformation> call, Response<HotelInformation> response) {
                if (response.isSuccessful()) {
                    mLiveData.setValue(response.body());
                }
                else {
                    mLiveData.setValue(null);
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
