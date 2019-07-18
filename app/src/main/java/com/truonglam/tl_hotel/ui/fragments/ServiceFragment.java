package com.truonglam.tl_hotel.ui.fragments;


import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
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

    @BindView(R.id.btnBack)
    ImageView btnBack;

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
        registerListener();
    }

    private void registerListener() {
        btnBack.setOnClickListener(this);
    }

    private void openEditServiceFragment() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new EditServiceFragment())
                .addToBackStack(null)
                .commit();
    }

    private void showPopupMenu() {
        adapter.setOnItemClickListener(new HotelServiceAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position, View view) {
                PopupMenu menu = new PopupMenu(getActivity(), view);
                MenuInflater inflater = menu.getMenuInflater();
                inflater.inflate(R.menu.menu_edit_delete, menu.getMenu());
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.mnu_view:
                                break;

                            case R.id.mnu_edit:
                                openEditServiceFragment();
                                break;

                            case R.id.mnu_delete:
                                break;
                        }
                        return true;
                    }
                });
                menu.show();
            }
        });

    }

    private void configRecyclerView() {
        pbLoading.setVisibility(View.VISIBLE);
        hotelInformation =
                (HotelInformation) getArguments().getSerializable(Key.KEY_HOTEL_INFORMATION);
        String id = hotelInformation.getHotelId();
        String token = hotelInformation.getAccessToken();

                mViewModel = ViewModelProvider.AndroidViewModelFactory
                        .getInstance(getActivity().getApplication())
                        .create(ServicesViewModel.class);
                 mViewModel.getHotelServices(token,id).observe(this, new Observer<List<HotelService>>() {
                    @Override
                    public void onChanged(@Nullable List<HotelService> services) {
                        adapter = new HotelServiceAdapter(services, getActivity());
                        rvListService.setAdapter(adapter);
                        pbLoading.setVisibility(View.INVISIBLE);
                        showPopupMenu();
                    }
                });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabAddService:
                break;

            case R.id.btnBack:
                getActivity().getSupportFragmentManager().popBackStack();
                break;

            default:
                break;
        }
    }
}
