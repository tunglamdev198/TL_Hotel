package com.truonglam.tl_hotel.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.truonglam.tl_hotel.model.Room;
import com.truonglam.tl_hotel.webservice.Client;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomViewModel extends AndroidViewModel {
    private MutableLiveData<List<Room>> roomLiveData;

    public RoomViewModel(@NonNull Application application) {
        super(application);
        roomLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<Room>> getRoomLiveData(String token, String idcp) {
        Client.getService().getRoomByIdCP(token, idcp).enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                roomLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {

            }
        });
        return roomLiveData;
    }

    public void addRoom(String token,Room room){
        Client.getService().createRoom(token,room).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void setRoomLiveData(List<Room> rooms) {
        roomLiveData.setValue(rooms);
    }

    public MutableLiveData<List<Room>> updateList(String token, String idcp) {
        final List<Room> roomList = roomLiveData.getValue();
        roomList.clear();
        Client.getService().getRoomByIdCP(token, idcp).enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                List<Room> rooms = response.body();
                roomList.addAll(rooms);
                roomLiveData.setValue(roomList);
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {

            }
        });
        return roomLiveData;
    }
}
