package com.truonglam.tl_hotel.webservice.reponsitories;

import android.arch.lifecycle.MutableLiveData;

import com.truonglam.tl_hotel.model.ClusterRoomUpdating;
import com.truonglam.tl_hotel.model.Room;
import com.truonglam.tl_hotel.model.RoomCluster;
import com.truonglam.tl_hotel.model.RoomClusterResponse;
import com.truonglam.tl_hotel.webservice.Client;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomClusterRepository {
    private static RoomClusterRepository repository;

    private MutableLiveData<List<RoomCluster>> mLiveData;

    private boolean check;

    public static RoomClusterRepository getInstance() {
        if (repository == null) {
            repository = new RoomClusterRepository();
        }
        return repository;
    }

    private RoomClusterRepository() {
        mLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<RoomCluster>> getRoomCluster(String token, String id){
        Client.getService().getClusterRooms(token,id).enqueue(new Callback<RoomClusterResponse>() {
            @Override
            public void onResponse(Call<RoomClusterResponse> call, Response<RoomClusterResponse> response) {
                if (response.isSuccessful()){
                    mLiveData.setValue(response.body().getRoomClusters());
                }
                else {
                    mLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<RoomClusterResponse> call, Throwable t) {
                mLiveData.setValue(null);
            }
        });
        return mLiveData;
    }

    public boolean addRoomCluster(String token,RoomCluster roomCluster){
        Client.getService().createRoomCluster(token,roomCluster).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    check = true;
                }
                else {
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

    public boolean deleteRoomCluster(String token,String roomClusterId){
        Client.getService().deleteClusterRoom(token,roomClusterId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    check = true;
                }
                else {
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

    public boolean updateRoomCluster(String token, ClusterRoomUpdating updating){
        Client.getService().updateClusterRoom(token,updating).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    check = true;
                }
                else {
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
