package com.truonglam.tl_hotel.ui.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.squareup.picasso.Picasso;
import com.truonglam.tl_hotel.R;
import com.truonglam.tl_hotel.common.Key;
import com.truonglam.tl_hotel.model.HotelBackground;
import com.truonglam.tl_hotel.model.HotelInformation;
import com.truonglam.tl_hotel.webservice.Client;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelInformationFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "HotelInformationFragment";

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

    @BindView(R.id.txtHotelName)
    TextView txtHotelName;

    @BindView(R.id.txtLocation)
    TextView txtLocation;

    @BindView(R.id.btn_exit)
    ImageView btnExit;

    @BindView(R.id.btn_logout)
    ImageView btnLogout;

    private HotelInformation hotelInformation;

    private  HotelBackground hotelBackground;


    public HotelInformationFragment() {
    }

    public static HotelInformationFragment newInstance(HotelInformation hotelInformation,String username) {
        Bundle args = new Bundle();
        args.putSerializable(Key.KEY_HOTEL_INFORMATION, hotelInformation);
        args.putString(Key.KEY_USER,username);
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
        initViews();
        registerListener();
    }

    private void initViews() {
        hotelInformation =
                (HotelInformation) getArguments().getSerializable(Key.KEY_HOTEL_INFORMATION);
        Log.d("AAAA", hotelInformation.toString());
        txtHotelName.setText(hotelInformation.getName());
        txtLocation.setText(hotelInformation.getTittle());

        Client.getService().getBackground(hotelInformation.getAccessToken(),hotelInformation.getHotelId())
                .enqueue(new Callback<HotelBackground>() {
            @Override
            public void onResponse(Call<HotelBackground> call, Response<HotelBackground> response) {
                hotelBackground = response.body();
                List<String> images = hotelBackground.getLinks();
                for (String image : images) {
                    slideImages(Client.BASE_URL + image);
                }
            }

            @Override
            public void onFailure(Call<HotelBackground> call, Throwable t) {

            }
        });
    }

    private void registerListener() {
        cvNotification.setOnClickListener(this);
        cvService.setOnClickListener(this);
        cvRoom.setOnClickListener(this);
        cvAccount.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    private void slideImages(String url) {
        ImageView imageView = new ImageView(getActivity());
        Picasso.with(getActivity()).load(url).into(imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        sliderView.addView(imageView);
        sliderView.setFlipInterval(2000);
        sliderView.setAutoStart(true);
        sliderView.setOutAnimation(getActivity(), android.R.anim.slide_out_right);
        sliderView.setInAnimation(getActivity(), android.R.anim.slide_in_left);
    }

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

    private void exitApp(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false)
                .setMessage("Bạn có muốn thoát ứng dụng không?")
                .setTitle("Thoát")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void logout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false)
                .setMessage("Bạn có muốn đăng xuất không?")
                .setTitle("Đăng xuất")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.anim_slide_in_left,R.anim.anim_slide_out_left)
                                .replace(R.id.container,new LoginFragment())
                                .commit();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cvNotification:
                NotificationFragment notificationFragment =
                        NotificationFragment.newInstance(hotelBackground, hotelInformation);
                loadFragment(notificationFragment);
                break;

            case R.id.cvService:
                ServiceFragment serviceFragment = ServiceFragment.newInstance(hotelInformation);
                loadFragment(serviceFragment);
                break;


            case R.id.cvAccount:
                String username = getArguments().getString(Key.KEY_USER);
                AccountFragment accountFragment = AccountFragment.newInstance(hotelInformation,username);
                loadFragment(accountFragment);
                break;

            case R.id.cvRoom:
                RoomsFragment roomsFragment = RoomsFragment.newInstance(hotelInformation);
                loadFragment(roomsFragment);
                break;

            case R.id.btn_logout:
                logout();
                break;

            case R.id.btn_exit:
                exitApp();
                break;

            default:
                break;
        }
    }
}
