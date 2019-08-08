package com.truonglam.tl_hotel.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.truonglam.tl_hotel.R;
import com.truonglam.tl_hotel.common.Key;
import com.truonglam.tl_hotel.model.HotelInformation;
import com.truonglam.tl_hotel.model.HotelService;
import com.truonglam.tl_hotel.webservice.Client;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import petrov.kristiyan.colorpicker.ColorPicker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditServiceFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.edt_tittle)
    TextInputEditText edtTittle;

    @BindView(R.id.edt_link)
    TextInputEditText edtLink;

    @BindView(R.id.edt_icon_code)
    TextInputEditText edtIconCode;

    @BindView(R.id.edt_color_code)
    TextInputEditText edtColorCode;

    @BindView(R.id.btn_done)
    Button btnDone;

    @BindView(R.id.btn_cancel)
    Button btnCancel;

    @BindView(R.id.btn_back)
    ImageView btnBack;

    private HotelService service;

    private HotelInformation hotelInformation;


    public EditServiceFragment() {
    }

    public static EditServiceFragment newInstance(HotelService hotelService, HotelInformation hotelInformation) {
        Bundle args = new Bundle();
        args.putSerializable(Key.KEY_HOTEL_SERVICE, hotelService);
        args.putSerializable(Key.KEY_HOTEL_INFORMATION, hotelInformation);
        EditServiceFragment fragment = new EditServiceFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initViews();
        registerListener();
    }

    private void initViews() {
        service = (HotelService) getArguments().getSerializable(Key.KEY_HOTEL_SERVICE);
        hotelInformation = (HotelInformation) getArguments().getSerializable(Key.KEY_HOTEL_INFORMATION);
        edtTittle.setText(service.getTittle());
        edtTittle.setSelection(service.getTittle().length());
        edtColorCode.setText(service.getColorIcon());
        edtColorCode.setSelection(service.getColorIcon().length());
        edtIconCode.setText(service.getIcon());
        edtIconCode.setSelection(service.getIcon().length());

    }

    private void registerListener() {
        btnDone.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    private void openServiceFragment() {
        ServiceFragment fragment = ServiceFragment.newInstance(hotelInformation);
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.container, fragment)
                .commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_done:
                Client.getService().updateService(hotelInformation.getAccessToken()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
                openServiceFragment();
                break;

            case R.id.btn_cancel:
                getActivity().getSupportFragmentManager().popBackStack();
                break;

            case R.id.btn_back:
                openServiceFragment();
                break;


            default:
                break;
        }
    }
}
