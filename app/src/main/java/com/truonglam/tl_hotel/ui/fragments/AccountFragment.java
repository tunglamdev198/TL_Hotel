package com.truonglam.tl_hotel.ui.fragments;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.truonglam.tl_hotel.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountFragment extends Fragment {

    @BindView(R.id.edtOldPassword)
    TextInputEditText edtOldPassword;

    @BindView(R.id.edtNewPassword)
    TextInputEditText edtNewPassword;

    @BindView(R.id.edtConfirmPassword)
    TextInputEditText edtConfirmPassword;


    public AccountFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

}
