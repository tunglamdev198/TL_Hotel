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
import android.widget.ViewFlipper;

import com.truonglam.tl_hotel.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HotelInformationFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.sliderView)
    ViewFlipper sliderView;

    @BindView(R.id.cvNotification)
    CardView cvNotification;

    @BindView(R.id.cvService)
    CardView cvService;

    @BindView(R.id.cvRoom)
    CardView cvRoom;

    @BindView(R.id.cvAccount)
    CardView cvAccount;

    private int[] images = {R.drawable.hotel1, R.drawable.hotel2, R.drawable.hotel3};


    public HotelInformationFragment() {
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
        for (int image : images) {
            slideImages(image);
        }
        registerListener();
    }

    private void registerListener() {
        cvNotification.setOnClickListener(this);
        cvService.setOnClickListener(this);
        cvRoom.setOnClickListener(this);
        cvAccount.setOnClickListener(this);
    }

    private void slideImages(int image) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(image);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        sliderView.addView(imageView);
        sliderView.setFlipInterval(4000);
        sliderView.setAutoStart(true);
        sliderView.setOutAnimation(getActivity(), android.R.anim.slide_out_right);
        sliderView.setInAnimation(getActivity(), android.R.anim.slide_in_left);
    }

    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
                    .replace(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cvNotification:
                loadFragment(new NotificationFragment());
                break;

            case R.id.cvService:
                loadFragment(new ServiceFragment());
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

    @Override
    public void onResume() {
        super.onResume();
        for (int image : images) {
            slideImages(image);
        }
    }
}
