package com.truonglam.tl_hotel.ui.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.truonglam.tl_hotel.R;
import com.truonglam.tl_hotel.common.Key;
import com.truonglam.tl_hotel.model.HotelInformation;
import com.truonglam.tl_hotel.model.HotelService;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceDetailFragment extends Fragment {

    @BindView(R.id.wv_service_content)
    WebView wvContent;

    @BindView(R.id.btn_back)
    ImageView btnBack;

    @BindView(R.id.txt_tittle)
    TextView txtTittle;

    private HotelService service;

    private HotelInformation information;


    public ServiceDetailFragment() {
    }

    public static ServiceDetailFragment newInstance(HotelService service, HotelInformation information) {

        Bundle args = new Bundle();
        args.putSerializable(Key.KEY_HOTEL_SERVICE, service);
        args.putSerializable(Key.KEY_HOTEL_INFORMATION, information);
        ServiceDetailFragment fragment = new ServiceDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_service_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewContentHtml();
        registerListener();
    }

    private void viewContentHtml() {
        service = (HotelService) getArguments().getSerializable(Key.KEY_HOTEL_SERVICE);
        information = (HotelInformation) getArguments().getSerializable(Key.KEY_HOTEL_INFORMATION);
        WebSettings ws = wvContent.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setLoadWithOverviewMode(true);
        ws.setUseWideViewPort(true);
        txtTittle.setText(service.getTittle());
        wvContent.loadData(service.getLink(), "text/html", null);
    }

    private void registerListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceFragment fragment = ServiceFragment.newInstance(information);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.container, fragment)
                        .commit();
            }
        });
    }
}


