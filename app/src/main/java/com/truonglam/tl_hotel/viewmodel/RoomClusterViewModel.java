package com.truonglam.tl_hotel.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.truonglam.tl_hotel.TLApp;
import com.truonglam.tl_hotel.model.ClusterRoomUpdating;
import com.truonglam.tl_hotel.model.Room;
import com.truonglam.tl_hotel.model.RoomCluster;
import com.truonglam.tl_hotel.model.RoomClusterResponse;
import com.truonglam.tl_hotel.receiver.NetworkChangedReceiver;
import com.truonglam.tl_hotel.utils.OpenWifiSetting;
import com.truonglam.tl_hotel.utils.ProgressDialogUtil;
import com.truonglam.tl_hotel.webservice.Client;
import com.truonglam.tl_hotel.webservice.reponsitories.RoomClusterRepository;
import com.truonglam.tl_hotel.webservice.reponsitories.RoomRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomClusterViewModel extends AndroidViewModel {

    private MutableLiveData<List<RoomCluster>> mLiveData;

    private boolean check;

    // private RoomClusterRepository repository;

    public RoomClusterViewModel(@NonNull Application application) {
        super(application);
        mLiveData = new MutableLiveData<>();
    }

//    public void init(String token, String id){
//        repository = RoomClusterRepository.getInstance();
//        mLiveData = repository.getRoomCluster(token,id);
//    }

    public MutableLiveData<List<RoomCluster>> getRoomClusters(Context context, String token, String id) {
        if (NetworkChangedReceiver.isConnected(context)) {
            Client.getService().getClusterRooms(token, id).enqueue(new Callback<RoomClusterResponse>() {
                @Override
                public void onResponse(Call<RoomClusterResponse> call, Response<RoomClusterResponse> response) {
                    if (response.isSuccessful()) {
                        mLiveData.setValue(response.body().getRoomClusters());
                    } else {
                        mLiveData.setValue(null);
                    }
                }

                @Override
                public void onFailure(Call<RoomClusterResponse> call, Throwable t) {
                    mLiveData.setValue(null);
                }
            });
        } else {
            OpenWifiSetting.open(context);
        }

        return mLiveData;
    }

    public void addRoomCluster(Context context, String token, RoomCluster roomCluster) {
        ProgressDialogUtil.showDialog(context, "Đang thêm");
        //return repository.addRoomCluster(token,roomCluster);
        Client.getService().createRoomCluster(token, roomCluster).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    ProgressDialogUtil.closeDialog();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }

    public void deleteRoomCluster(Context context, String token, String roomClusterId) {
        ProgressDialogUtil.showDialog(context, "Đang xóa");
        //return repository.deleteRoomCluster(token,roomClusterId);

        Client.getService().deleteClusterRoom(token, roomClusterId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    ProgressDialogUtil.closeDialog();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                check = false;
            }
        });

    }

    public void updateRoomCluster(Context context, String token, ClusterRoomUpdating updating) {
        ProgressDialogUtil.showDialog(context, "Đang sửa");

        //return repository.updateRoomCluster(token,updating);
        Client.getService().updateClusterRoom(token, updating).enqueue(new Callback<Void>() {
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
}
