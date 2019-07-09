package com.truonglam.tl_hotel.ui.fragments;


import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.truonglam.tl_hotel.R;
import com.truonglam.tl_hotel.adapter.HotelServiceAdapter;
import com.truonglam.tl_hotel.common.Key;
import com.truonglam.tl_hotel.model.HotelInformation;
import com.truonglam.tl_hotel.model.HotelService;
import com.truonglam.tl_hotel.model.HotelServiceResponse;
import com.truonglam.tl_hotel.viewmodel.ServicesViewModel;
import com.truonglam.tl_hotel.webservice.Client;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "ServiceFragment";

    @BindView(R.id.rvListService)
    RecyclerView rvListService;

    @BindView(R.id.pbLoading)
    ProgressBar pbLoading;

    @BindView(R.id.fabAddService)
    FloatingActionButton fabAddService;

    private HotelInformation hotelInformation;


    private HotelServiceAdapter adapter;

    private ServicesViewModel mViewModel;


    public ServiceFragment() {
    }

    public static ServiceFragment newInstance(HotelInformation hotelInformation) {

        Bundle args = new Bundle();
        args.putSerializable(Key.KEY_HOTEL_INFORMATION, hotelInformation);
        ServiceFragment fragment = new ServiceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        configRecyclerView();
    }

    private void configRecyclerView() {
        pbLoading.setVisibility(View.VISIBLE);
        hotelInformation =
                (HotelInformation) getArguments().getSerializable(Key.KEY_HOTEL_INFORMATION);
        //Log.d(TAG, hotelInformation.toString());
        String id = hotelInformation.getHotelId();
        String token = hotelInformation.getAccessToken();
        Client.getService().getServices(token, id).enqueue(new Callback<HotelServiceResponse>() {
            @Override
            public void onResponse(Call<HotelServiceResponse> call, Response<HotelServiceResponse> response) {
                List<HotelService> services = response.body().getHotelServices();
                mViewModel = ViewModelProvider.AndroidViewModelFactory
                        .getInstance(getActivity().getApplication())
                        .create(ServicesViewModel.class);
                mViewModel.setHotelServices(services);
                adapter = new HotelServiceAdapter(services, getActivity());
                rvListService.setAdapter(adapter);
                pbLoading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<HotelServiceResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabAddService:
                break;

            default:
                break;
        }
    }
}
