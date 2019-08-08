package com.truonglam.tl_hotel.ui.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.truonglam.tl_hotel.R;
import com.truonglam.tl_hotel.adapter.HotelServiceAdapter;
import com.truonglam.tl_hotel.common.Key;
import com.truonglam.tl_hotel.model.HotelInformation;
import com.truonglam.tl_hotel.model.HotelService;
import com.truonglam.tl_hotel.viewmodel.ServicesViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceFragment extends Fragment implements View.OnClickListener
        , HotelServiceAdapter.OnItemClickListener {

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.rv_list_service)
    RecyclerView rvListService;

    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;

    @BindView(R.id.txt_status)
    TextView txtStatus;


    private List<HotelService> listService;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())
                .create(ServicesViewModel.class);
        configRecyclerView();
        registerListener();
    }

    private void registerListener() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                updateData();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void openEditServiceFragment(HotelService hotelService) {
        EditServiceFragment fragment = EditServiceFragment.newInstance(hotelService, hotelInformation);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .addToBackStack(null)
                .commit();
    }

    private void configRecyclerView() {
        pbLoading.setVisibility(View.VISIBLE);
        hotelInformation =
                (HotelInformation) getArguments().getSerializable(Key.KEY_HOTEL_INFORMATION);
        String id = hotelInformation.getHotelId();
        String token = hotelInformation.getAccessToken();

        mViewModel.getServices(token, id).observe(this, new Observer<List<HotelService>>() {
            @Override
            public void onChanged(@Nullable List<HotelService> services) {
                listService = services;
                if (services == null) {
                    txtStatus.setVisibility(View.VISIBLE);
                    return;
                }
                adapter = new HotelServiceAdapter(services, getActivity());
                rvListService.setAdapter(adapter);
                DividerItemDecoration dividerHorizontal =
                        new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
                dividerHorizontal.
                        setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.devider_recyclerview));
                rvListService.addItemDecoration(dividerHorizontal);
                pbLoading.setVisibility(View.INVISIBLE);
                adapter.setOnItemClickListener(ServiceFragment.this);

            }
        });


    }

    private void deleteService(String token, String serviceId) {
        mViewModel.deleteService(getActivity(), token, serviceId);
        updateData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    private void updateData() {
        mViewModel.getServices(hotelInformation.getAccessToken(), hotelInformation.getHotelId())
                .observe(getActivity(), new Observer<List<HotelService>>() {
                    @Override
                    public void onChanged(@Nullable List<HotelService> services) {
                        pbLoading.setVisibility(View.VISIBLE);
                        if (services == null) {
                            txtStatus.setVisibility(View.VISIBLE);
                            return;
                        }
                        adapter.updateData(services);
                        pbLoading.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void onItemClicked(int position, View view) {
        HotelService service = listService.get(position);
        switch (view.getId()) {
            case R.id.btn_edit:
                openEditServiceFragment(service);
                break;

            case R.id.btn_delete:
                deleteService(hotelInformation.getAccessToken(), service.getId());
                break;

            case R.id.ll_content:
                ServiceDetailFragment detailFragment = ServiceDetailFragment.newInstance(service, hotelInformation);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, detailFragment)
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }
}
