package com.truonglam.tl_hotel.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.truonglam.tl_hotel.model.RoomCluster;
import com.truonglam.tl_hotel.model.RoomClusterResponse;
import com.truonglam.tl_hotel.webservice.Client;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomClusterViewModel extends AndroidViewModel {
    private MutableLiveData<List<RoomCluster>> roomCLusterLiveData;
    public RoomClusterViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<RoomCluster>> getRoomCLusterLiveData(String token, String id) {
        Client.getService().getClusterRooms(token,id).enqueue(new Callback<RoomClusterResponse>() {
            @Override
            public void onResponse(Call<RoomClusterResponse> call, Response<RoomClusterResponse> response) {
                List<RoomCluster> roomClusters = response.body().getRoomClusters();
                roomCLusterLiveData.setValue(roomClusters);

            }

            @Override
            public void onFailure(Call<RoomClusterResponse> call, Throwable t) {

            }
        });
        return roomCLusterLiveData;
    }

    public void setRoomCLusterLiveData(List<RoomCluster> roomClusters) {
        roomCLusterLiveData.setValue(roomClusters);
    }

    public void addRoomCluster(RoomCluster roomCluster){
        List<RoomCluster> roomClusters = roomCLusterLiveData.getValue();
        roomClusters.add(roomCluster);
        roomCLusterLiveData.setValue(roomClusters);
    }
}
