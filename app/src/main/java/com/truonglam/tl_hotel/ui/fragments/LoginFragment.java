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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.truonglam.tl_hotel.R;
import com.truonglam.tl_hotel.handler.MyHandler;
import com.truonglam.tl_hotel.model.HotelInformation;
import com.truonglam.tl_hotel.ui.activities.HomePageActivity;
import com.truonglam.tl_hotel.utils.SavingUPSharePref;
import com.truonglam.tl_hotel.webservice.Client;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment implements View.OnClickListener {

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
        initData();
        registerListener();
    }

    private void initData() {
//        hotelInfoViewModel = ViewModelProvider.AndroidViewModelFactory
//                .getInstance(TLApp.getInstance())
//                .create(HotelInformationViewModel.class);
//        // hotelInfoViewModel.init(username, password);
    }

    private void registerListener() {
        btnLogin.setOnClickListener(this);
    }

    private void validateLogin() {
        username = edtUserName.getText().toString().trim();
        password = edtPassword.getText().toString().trim();
        if (username.isEmpty() && password.isEmpty()) {
            Toasty.error(getActivity(), R.string.msg_validate_information,
                    Toast.LENGTH_SHORT, false).show();
            return;
        } else {
            if (username.isEmpty()) {
                Toasty.error(getActivity(), R.string.msg_validate_username,
                        Toast.LENGTH_SHORT, false).show();
                return;
            }

            if (password.isEmpty()) {
                Toasty.error(getActivity(), R.string.msg_validate_password,
                        Toast.LENGTH_SHORT, false).show();
                return;
            }
        }
        doLogin();
    }

    private void doLogin() {
        loadProgressBar();
        Client.getService().getInformation(username, password).enqueue(new Callback<HotelInformation>() {
            @Override
            public void onResponse(Call<HotelInformation> call, Response<HotelInformation> response) {
                if (response.isSuccessful()) {
                    HotelInformation hotelInformation = response.body();
                    Log.d("A", "onResponse: " + hotelInformation.toString());
                    HomePageActivity.getInstance().setHotelInfo(hotelInformation);
                    if (hotelInformation.getAccessToken() == null) {
                        stopProgressBar();
                        Toasty.error(getActivity(), R.string.msg_validate_info_not_correct,
                                Toast.LENGTH_SHORT, false).show();
                        return;
                    }
                    HotelInformationFragment fragment = HotelInformationFragment.newInstance(hotelInformation);
                    SavingUPSharePref.savePassword(password, getActivity());
                    SavingUPSharePref.saveUsername(username, getActivity());
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, fragment)
                            .setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left)
                            .commit();
                    HomePageActivity.getInstance().bottomNav.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<HotelInformation> call, Throwable t) {
                Toasty.error(getActivity(), R.string.msg_validate_login_fail,
                        Toast.LENGTH_SHORT, false).show();
                stopProgressBar();
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

        new MyHandler(runnable, (long) 2000).start();
    }

    private void stopProgressBar() {
        pbLogin.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {
        validateLogin();
    }
}
