package com.truonglam.tl_hotel.ui.fragments;


import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
public class RoomsFragment extends Fragment implements View.OnClickListener, AddEditRoomClusterFragment.ClusterRoomDialogListener {

    public static final String TAG = "RoomsFragment";

    @BindView(R.id.rv_list_room_cluster)
    RecyclerView rvRoomCluster;

    @BindView(R.id.btnBack)
    ImageView btnBack;

    @BindView(R.id.spinkit_loading)
    ProgressBar spinkitLoading;

    @BindView(R.id.btn_add)
    FloatingActionButton btnAdd;

    private ClusterRoomAdapter adapter;

    private ServicesViewModel mViewModel;

    private HotelInformation hotelInformation;

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
//        mViewModel = ViewModelProvider.AndroidViewModelFactory
//                .getInstance(getActivity().getApplication())
//                .create(ServicesViewModel.class);
//        mViewModel.getHotelServices().observe(getActivity(), new Observer<List<HotelService>>() {
//            @Override
//            public void onChanged(@Nullable List<HotelService> services) {
//                Log.d(TAG, services.toString());
//            }
//        });
    }

    private void registerListener() {
        btnBack.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
    }


    private void configRecyclerView() {
        spinkitLoading.setVisibility(View.VISIBLE);
        hotelInformation =
                (HotelInformation) getArguments().getSerializable(Key.KEY_HOTEL_INFORMATION);

        final String token = hotelInformation.getAccessToken();

        Client.getService().getClusterRooms(token, hotelInformation.getHotelId()).enqueue(new Callback<RoomClusterResponse>() {

            @SuppressLint("RestrictedApi")
            @Override
            public void onResponse(Call<RoomClusterResponse> call, Response<RoomClusterResponse> response) {
                final List<RoomCluster> roomClusters = response.body().getRoomClusters();
                adapter = new ClusterRoomAdapter(roomClusters, getActivity());
                rvRoomCluster.setAdapter(adapter);
                spinkitLoading.setVisibility(View.GONE);
                btnAdd.setVisibility(View.VISIBLE);
                adapter.setOnItemClickListener(new HotelServiceAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClicked(int position, View view) {
                        RoomCluster roomCluster = roomClusters.get(position);
                        ListRoomFragment listRoomFragment = ListRoomFragment.newInstance(roomCluster, token, hotelInformation);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, listRoomFragment)
                                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                                .addToBackStack(null)
                                .commit();
                    }
                });
            }

            @Override
            public void onFailure(Call<RoomClusterResponse> call, Throwable t) {

            }
        });
    }

    private void loadProgressBar() {
        spinkitLoading.setVisibility(View.VISIBLE);
        Sprite doubleBounce = new DoubleBounce();
        spinkitLoading.setIndeterminateDrawable(doubleBounce);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 1500);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                getActivity().getSupportFragmentManager().popBackStack();
                break;

            case R.id.btn_add:
                AddEditRoomClusterFragment fragment = AddEditRoomClusterFragment
                        .newInstance(Key.MODE_ADD_CLUSTER_ROOM, "");

                fragment.show(getFragmentManager(), "AddEditClusterRoomFragment");
                fragment.setListener(this);
                break;

            default:
                break;
        }
    }

    private void addClusterRoom(String clusterName) {
        RoomCluster roomCluster = new RoomCluster(hotelInformation.getHotelId(),clusterName , "1");
        Client.getService().createRoomCluster(hotelInformation.getAccessToken(), roomCluster).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    private void resetData(){

    }

    @Override
    public void applyText(String clusterName, String mode) {
        switch (mode) {
            case Key.MODE_ADD_CLUSTER_ROOM:
                addClusterRoom(clusterName);
                break;

            case Key.MODE_EDIT_CLUSTER_ROOM:
                break;


        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
