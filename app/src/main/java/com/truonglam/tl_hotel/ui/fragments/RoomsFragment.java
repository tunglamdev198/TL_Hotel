package com.truonglam.tl_hotel.ui.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.truonglam.tl_hotel.R;
import com.truonglam.tl_hotel.adapter.ClusterRoomAdapter;
import com.truonglam.tl_hotel.adapter.HotelServiceAdapter;
import com.truonglam.tl_hotel.common.Key;
import com.truonglam.tl_hotel.model.HotelInformation;
import com.truonglam.tl_hotel.model.HotelService;
import com.truonglam.tl_hotel.model.RoomCluster;
import com.truonglam.tl_hotel.model.RoomClusterResponse;
import com.truonglam.tl_hotel.viewmodel.ServicesViewModel;
import com.truonglam.tl_hotel.webservice.Client;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoomsFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "RoomsFragment";

    @BindView(R.id.rv_list_room_cluster)
    RecyclerView rvRoomCluster;

    @BindView(R.id.btnBack)
    ImageView btnBack;

    @BindView(R.id.spinkit_loading)
    ProgressBar spinkitLoading;

    private ClusterRoomAdapter adapter;

    private ServicesViewModel mViewModel;

    public RoomsFragment() {
    }

    public static RoomsFragment newInstance(HotelInformation hotelInformation) {

        Bundle args = new Bundle();
        args.putSerializable(Key.KEY_HOTEL_INFORMATION, hotelInformation);
        RoomsFragment fragment = new RoomsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rooms, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        configRecyclerView();
        registerListener();
        mViewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())
                .create(ServicesViewModel.class);
        mViewModel.getHotelServices().observe(getActivity(), new Observer<List<HotelService>>() {
            @Override
            public void onChanged(@Nullable List<HotelService> services) {
                Log.d(TAG, services.toString());
            }
        });
    }

    private void registerListener(){
        btnBack.setOnClickListener(this);
    }

    private void configRecyclerView() {
        HotelInformation hotelInformation =
                (HotelInformation) getArguments().getSerializable(Key.KEY_HOTEL_INFORMATION);

        String token = hotelInformation.getAccessToken();

        Client.getService().getClusterRooms(token, hotelInformation.getHotelId()).enqueue(new Callback<RoomClusterResponse>() {
            @Override
            public void onResponse(Call<RoomClusterResponse> call, Response<RoomClusterResponse> response) {
                List<RoomCluster> roomClusters = response.body().getRoomClusters();
                adapter = new ClusterRoomAdapter(roomClusters, getActivity());
                rvRoomCluster.setAdapter(adapter);
                adapter.setOnItemClickListener(new HotelServiceAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClicked(int position, View view) {
                        Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<RoomClusterResponse> call, Throwable t) {

            }
        });
    }

    private void loadProgressBar(){
        spinkitLoading.setVisibility(View.VISIBLE);
        Sprite doubleBounce = new DoubleBounce();
        spinkitLoading.setIndeterminateDrawable(doubleBounce);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        },1500);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                loadProgressBar();
                getActivity().getSupportFragmentManager().popBackStack();
                break;

            default:
                break;
        }
    }
}
