package com.truonglam.tl_hotel.ui.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.truonglam.tl_hotel.R;
import com.truonglam.tl_hotel.common.Key;
import com.truonglam.tl_hotel.model.HotelInformation;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class AccountFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.edtOldPassword)
    TextInputEditText edtOldPassword;

    @BindView(R.id.edtNewPassword)
    TextInputEditText edtNewPassword;

    @BindView(R.id.edtConfirmPassword)
    TextInputEditText edtConfirmPassword;

    @BindView(R.id.btnDone)
    Button btnDone;

    private HotelInformation hotelInformation;


    public AccountFragment() {
    }

    public static AccountFragment newInstance(HotelInformation hotelInformation) {

        Bundle args = new Bundle();
        args.putSerializable(Key.KEY_HOTEL_INFORMATION,hotelInformation);
        AccountFragment fragment = new AccountFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initViews();
        registerListener();
    }

    private void initViews(){
        hotelInformation = (HotelInformation) getArguments().getSerializable(Key.KEY_HOTEL_INFORMATION);
    }

    private void registerListener(){
        btnDone.setOnClickListener(this);
    }

    private void changPassword() {
        String oldPassword = edtOldPassword.getText().toString().trim();
        String newPassword = edtNewPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        if(!newPassword.equals(confirmPassword)){
            Toasty.warning(getActivity(),"Vui lòng nhập đúng mật khẩu xác nhận",
                    Toast.LENGTH_SHORT,false);
            return;
        }

        if(oldPassword.equals(newPassword)){
            Toasty.warning(getActivity(),"Mật khẩu không thay đổi",
                    Toast.LENGTH_SHORT,false);
            return;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDone:
                changPassword();
                break;

            default:
                break;
        }
    }


}
