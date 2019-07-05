package com.truonglam.tl_hotel.ui.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.truonglam.tl_hotel.R;
import com.truonglam.tl_hotel.common.Key;
import com.truonglam.tl_hotel.model.HotelInformation;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HotelInformationFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "HotelInformationFragment";

//    @BindView(R.id.sliderView)
//    ViewFlipper sliderView;

    @BindView(R.id.imgLogo)
    ImageView imgLogo;

    @BindView(R.id.cvNotification)
    CardView cvNotification;

    @BindView(R.id.cvService)
    CardView cvService;

    @BindView(R.id.cvRoom)
    CardView cvRoom;

    @BindView(R.id.cvAccount)
    CardView cvAccount;

    @BindView(R.id.txtHotelName)
    TextView txtHotelName;

    @BindView(R.id.txtLocation)
    TextView txtLocation;

    private int[] images = {R.drawable.hotel1, R.drawable.hotel2, R.drawable.hotel3};
    private HotelInformation hotelInformation;


    public HotelInformationFragment() {
    }

    public static HotelInformationFragment newInstance(HotelInformation hotelInformation) {
        Bundle args = new Bundle();
        args.putSerializable(Key.KEY_HOTEL_INFORMATION, hotelInformation);
        HotelInformationFragment fragment = new HotelInformationFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hotel_infomation,
                container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        for (int image : images) {
//            slideImages(image);
//        }
        initViews();
        registerListener();
    }

    private void initViews() {
        hotelInformation =
                (HotelInformation) getArguments().getSerializable(Key.KEY_HOTEL_INFORMATION);
        txtHotelName.setText(hotelInformation.getName());
        txtLocation.setText(hotelInformation.getTittle());
        Picasso.with(getActivity()).load(hotelInformation.getLogo()).into(imgLogo);
    }

    private void registerListener() {
        cvNotification.setOnClickListener(this);
        cvService.setOnClickListener(this);
        cvRoom.setOnClickListener(this);
        cvAccount.setOnClickListener(this);
    }

//    private void slideImages(int image) {
//        ImageView imageView = new ImageView(getContext());
//        imageView.setImageResource(image);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//
//        sliderView.addView(imageView);
//        sliderView.setFlipInterval(4000);
//        sliderView.setAutoStart(true);
//        sliderView.setOutAnimation(getActivity(), android.R.anim.slide_out_right);
//        sliderView.setInAnimation(getActivity(), android.R.anim.slide_in_left);
//    }

    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cvNotification:
                NotificationFragment notificationFragment = NotificationFragment.newInstance("Hello", hotelInformation);
                loadFragment(notificationFragment);
                break;

            case R.id.cvService:
                ServiceFragment serviceFragment = ServiceFragment.newInstance(hotelInformation);
                loadFragment(serviceFragment);
                break;


            case R.id.cvAccount:
                loadFragment(new AccountFragment());
                break;

            case R.id.cvRoom:
                loadFragment(new RoomsFragment());
                break;

            default:
                break;
        }
    }
}
