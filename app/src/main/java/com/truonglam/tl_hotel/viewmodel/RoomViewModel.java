package com.truonglam.tl_hotel.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.truonglam.tl_hotel.model.Box;
import com.truonglam.tl_hotel.model.Room;
import com.truonglam.tl_hotel.receiver.NetworkChangedReceiver;
import com.truonglam.tl_hotel.utils.OpenWifiSetting;
import com.truonglam.tl_hotel.utils.ProgressDialogUtil;
import com.truonglam.tl_hotel.webservice.Client;
import com.truonglam.tl_hotel.webservice.reponsitories.RoomRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomViewModel extends AndroidViewModel {
    private MutableLiveData<List<Room>> mLiveData;

    private boolean check;

    private List<Box> boxes;

    //private RoomRepository repository;

    public RoomViewModel(@NonNull Application application) {
        super(application);
        mLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<Room>> getListRoom(Context context, String token, String idcp) {
        if (NetworkChangedReceiver.isConnected(context)) {
            Client.getService().getRoomByIdCP(token, idcp).enqueue(new Callback<List<Room>>() {
                @Override
                public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                    if (response.isSuccessful()) {
                        mLiveData.setValue(response.body());
                    } else {
                        mLiveData.setValue(null);
                    }
                }

                @Override
                public void onFailure(Call<List<Room>> call, Throwable t) {
                    mLiveData.setValue(null);
                }
            });
        } else {
            OpenWifiSetting.open(context);
        }

        return mLiveData;
    }

    public void addRoom(Context context, String token, Room room) {
        ProgressDialogUtil.showDialog(context, "Đang thêm");
        Client.getService().createRoom(token, room).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    ProgressDialogUtil.closeDialog();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                ProgressDialogUtil.closeDialog();
            }
        });

    }

    public void deleteRoom(Context context, String token, String phongId) {
        ProgressDialogUtil.showDialog(context, "Đang xóa");
        Client.getService().deleteRoom(token, phongId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    ProgressDialogUtil.closeDialog();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                ProgressDialogUtil.closeDialog();
            }
        });

    }

    public List<Box> getBox(String token, String roomId) {
        Client.getService().getBoxByRoomId(token, roomId).enqueue(new Callback<List<Box>>() {
            @Override
            public void onResponse(Call<List<Box>> call, Response<List<Box>> response) {
                boxes = response.body();
            }

            @Override
            public void onFailure(Call<List<Box>> call, Throwable t) {

            }
        });
        return boxes;
    }


}
