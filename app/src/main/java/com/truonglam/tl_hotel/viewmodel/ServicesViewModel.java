package com.truonglam.tl_hotel.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.truonglam.tl_hotel.model.HotelService;
import com.truonglam.tl_hotel.model.HotelServiceResponse;
import com.truonglam.tl_hotel.utils.ProgressDialogUtil;
import com.truonglam.tl_hotel.webservice.Client;
import com.truonglam.tl_hotel.webservice.reponsitories.ServicesRepository;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServicesViewModel extends AndroidViewModel {

    private MutableLiveData<List<HotelService>> mLiveData;

    //private ServicesRepository repository;
    private boolean check;


    public ServicesViewModel(@NonNull Application application) {
        super(application);
        mLiveData = new MutableLiveData<>();
    }

//    public void init(String token, String hotelId) {
//        if (mLiveData != null) {
//            return;
//        }
//        repository = ServicesRepository.getInstance();
//        mLiveData = repository.getServices(token, hotelId);
//    }

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

    public void deleteService(final Context context, String token, String serviceId) {
        ProgressDialogUtil.showDialog(context,"Đang xóa");
        Client.getService().deleteService(token, serviceId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    ProgressDialogUtil.closeDialog();
                    Toasty.success(context,"Xóa thành công",Toasty.LENGTH_SHORT,false).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                ProgressDialogUtil.closeDialog();
                Toasty.success(context,"Xóa không thành công",Toasty.LENGTH_SHORT,false).show();
            }
        });
    }

}
