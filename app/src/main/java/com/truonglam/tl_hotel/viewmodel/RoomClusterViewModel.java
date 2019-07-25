package com.truonglam.tl_hotel.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.truonglam.tl_hotel.model.ClusterRoomUpdating;
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
        roomCLusterLiveData = new MutableLiveData<>();
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

    public void addRoomCluster(String token, final RoomCluster roomCluster){
        Client.getService().createRoomCluster(token, roomCluster).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void deleteRoomCluster(String token, String id){
        Client.getService().deleteClusterRoom(token, id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void editRoomCluster(String token, ClusterRoomUpdating clusterRoomUpdating){
        Client.getService().updateClusterRoom(token,clusterRoomUpdating).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<List<RoomCluster>> updateList(String id, String token){

        Client.getService().getClusterRooms(token,id).enqueue(new Callback<RoomClusterResponse>() {
            @Override
            public void onResponse(Call<RoomClusterResponse> call, Response<RoomClusterResponse> response) {
                roomCLusterLiveData.setValue(response.body().getRoomClusters());
            }

            @Override
            public void onFailure(Call<RoomClusterResponse> call, Throwable t) {

            }
        });
        return roomCLusterLiveData;
    }
}
