package com.truonglam.tl_hotel.ui.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.truonglam.tl_hotel.R;
import com.truonglam.tl_hotel.common.Time;
import com.truonglam.tl_hotel.handler.MyHandler;
import com.truonglam.tl_hotel.model.HotelInformation;
import com.truonglam.tl_hotel.viewmodel.HotelInformationViewModel;
import com.truonglam.tl_hotel.webservice.Client;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "LoginFragment";

    @BindView(R.id.edtUsername)
    TextInputEditText edtUserName;

    @BindView(R.id.edtPassword)
    TextInputEditText edtPassword;

    @BindView(R.id.btnLogin)
    Button btnLogin;

    @BindView(R.id.pbLogin)
    ProgressBar pbLogin;

    private String username;
    private String password;

    private HotelInformationViewModel hotelInfoViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        registerListener();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        edtPassword.setText("123456");
        edtUserName.setText("anh");
        registerListener();
    }

    private void registerListener() {
        btnLogin.setOnClickListener(this);
    }

    private void validateLogin() {
        username = edtUserName.getText().toString().trim();
        password = edtPassword.getText().toString().trim();

        if (username.isEmpty() && password.isEmpty()) {
            Toasty.error(getActivity(), "Vui lòng nhập đủ thông tin",
                    Toast.LENGTH_SHORT, false).show();
            return;
        } else {
            if (username.isEmpty()) {
                Toasty.error(getActivity(), "Vui lòng nhập tên tài khoản",
                        Toast.LENGTH_SHORT, false).show();
                return;
            }

            if (password.isEmpty()) {
                Toasty.error(getActivity(), "Vui lòng nhập mật khẩu",
                        Toast.LENGTH_SHORT, false).show();
                return;
            }
        }
        doLogin();
    }

    private void doLogin() {
        loadProgressBar();
//        hotelInfoViewModel = ViewModelProvider.AndroidViewModelFactory
//                .getInstance(getActivity().getApplication())
//                .create(HotelInformationViewModel.class);
//        hotelInfoViewModel.getHotelInformation(username,password).observe(getActivity(), new Observer<HotelInformation>() {
//            @Override
//            public void onChanged(@Nullable HotelInformation hotelInformation) {
//                HotelInformationFragment fragment = HotelInformationFragment.newInstance(hotelInformation);
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.container, fragment)
//                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
//                        .commit();
//            }
//        });
        Client.getService().getInformation(username, password).enqueue(new Callback<HotelInformation>() {
            @Override
            public void onResponse(Call<HotelInformation> call, Response<HotelInformation> response) {
                HotelInformation hotelInformation = response.body();
                HotelInformationFragment fragment = HotelInformationFragment.newInstance(hotelInformation);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .commit();
            }

            @Override
            public void onFailure(Call<HotelInformation> call, Throwable t) {
            }
        });
    }

    private void loadProgressBar() {
        pbLogin.setVisibility(View.VISIBLE);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
            }
        };

        new MyHandler(runnable, Time.SERVER_TIME_OUT).start();
    }

    @Override
    public void onClick(View v) {
        validateLogin();
    }
}
