package com.truonglam.tl_hotel.webservice.reponsitories;

import android.arch.lifecycle.MutableLiveData;

import com.truonglam.tl_hotel.model.Room;
import com.truonglam.tl_hotel.webservice.Client;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomRepository {
    private static RoomRepository repository;

    private MutableLiveData<List<Room>> mLiveData;

    private boolean check;

    public static RoomRepository getInstance() {
        if (repository == null) {
            repository = new RoomRepository();
        }
        return repository;
    }

    private RoomRepository() {
        mLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<Room>> getListRoom(String token, String idcp) {
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
        return mLiveData;
    }

    public boolean addRoom(String token, Room room) {
        Client.getService().createRoom(token, room).enqueue(new Callback<Void>() {
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

    public boolean deleteRoom(String token, String phongId) {
        Client.getService().deleteRoom(token, phongId).enqueue(new Callback<Void>() {
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
