package com.truonglam.tl_hotel.ui.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.truonglam.tl_hotel.R;
import com.truonglam.tl_hotel.common.Key;
import com.truonglam.tl_hotel.model.HotelInformation;
import com.truonglam.tl_hotel.model.PasswordChanging;
import com.truonglam.tl_hotel.ui.activities.HomePageActivity;
import com.truonglam.tl_hotel.utils.ProgressDialogUtil;
import com.truonglam.tl_hotel.utils.SavingUPSharePref;
import com.truonglam.tl_hotel.webservice.Client;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {

    public static final String TAG = "AccountFragment";

    @BindView(R.id.edt_old_password)
    TextInputEditText edtOldPassword;

    @BindView(R.id.edt_new_password)
    TextInputEditText edtNewPassword;

    @BindView(R.id.edt_confirm_password)
    TextInputEditText edtConfirmPassword;

    @BindView(R.id.btn_done)
    Button btnDone;

    @BindView(R.id.img_logout)
    ImageView imgLogout;

    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

    private HotelInformation hotelInformation;


    public AccountFragment() {
    }

    public static AccountFragment newInstance(HotelInformation hotelInformation, String username) {

        Bundle args = new Bundle();
        args.putSerializable(Key.KEY_HOTEL_INFORMATION, hotelInformation);
        args.putString(Key.KEY_USER, username);
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

    private void initViews() {
        hotelInformation = (HotelInformation) getArguments().getSerializable(Key.KEY_HOTEL_INFORMATION);
        String password = SavingUPSharePref.getPassword(getActivity());
        edtOldPassword.setText(password);
    }

    private void registerListener() {
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatePassword();
            }
        });
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutAccount();
            }
        });
    }

    private void validatePassword() {
        oldPassword = edtOldPassword.getText().toString().trim();
        newPassword = edtNewPassword.getText().toString().trim();
        confirmPassword = edtConfirmPassword.getText().toString().trim();
        if(newPassword.length()<6){
            Toasty.warning(getActivity(), "Mật khẩu mới phải dài hơn 6 kí tự"+newPassword.length(),
                    Toast.LENGTH_SHORT, false).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toasty.warning(getActivity(), R.string.msg_validate_confirm_new_password,
                    Toast.LENGTH_SHORT, false).show();
            return;
        }

        if (oldPassword.equals(newPassword)) {
            Toasty.warning(getActivity(), R.string.msg_validate_password_not_change,
                    Toast.LENGTH_SHORT, false).show();
            return;
        }
        changePassword();
    }

    private void changePassword() {
        String token = hotelInformation.getAccessToken();
        String username = getArguments().getString(Key.KEY_USER);
        PasswordChanging changing = new PasswordChanging(username, oldPassword, newPassword);
        ProgressDialogUtil.showDialog(getActivity(),"Đang thay đổi mật khẩu");
        Client.getService().changePassword(token, changing).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.isSuccessful()) {
                    relogin();
                    ProgressDialogUtil.closeDialog();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                ProgressDialogUtil.closeDialog();
            }
        });
    }

    private void relogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.msg_relogin)
                .setCancelable(false)
                .setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doLogout();
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void logoutAccount() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false)
                .setMessage(R.string.msg_logout_account)
                .setTitle(R.string.msg_logout)
                .setPositiveButton(R.string.msg_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doLogout();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.msg_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void doLogout() {
        SavingUPSharePref.saveUsername(null, getActivity());
        SavingUPSharePref.savePassword(null, getActivity());

        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left)
                .replace(R.id.container, new LoginFragment())
                .commit();
        HomePageActivity.getInstance().bottomNav.setVisibility(View.GONE);
    }
}
