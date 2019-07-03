package com.truonglam.tl_hotel.ui.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.truonglam.tl_hotel.R;
import com.truonglam.tl_hotel.common.Key;
import com.truonglam.tl_hotel.model.HotelInformation;
import com.truonglam.tl_hotel.webservice.Client;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "LoginActivity";

    @BindView(R.id.edtUsername)
    TextInputEditText edtUserName;

    @BindView(R.id.edtPassword)
    TextInputEditText edtPassword;

    @BindView(R.id.btnLogin)
    Button btnLogin;

    @BindView(R.id.pbLogin)
    ProgressBar pbLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        registerListener();
    }


    private void registerListener() {
        btnLogin.setOnClickListener(this);
    }

    private void validateLoginInformation() {
        final String username = edtUserName.getText().toString().trim();
        final String password = edtPassword.getText().toString().trim();

        if (username.isEmpty() && password.isEmpty()) {
            Toasty.error(this, "Vui lòng nhập đủ thông tin",
                    Toast.LENGTH_SHORT, true).show();
            return;
        } else {
            if (username.isEmpty()) {
                Toasty.error(this, "Vui lòng nhập tên tài khoản",
                        Toast.LENGTH_SHORT, true)
                        .show();
                return;
            }

            if (password.isEmpty()) {
                Toasty.error(this, "Vui lòng nhập mật khẩu",
                        Toast.LENGTH_SHORT, true).show();
                return;
            }
        }

        Client.getService().getInformation(username, password).enqueue(new Callback<HotelInformation>() {
            @Override
            public void onResponse(Call<HotelInformation> call, Response<HotelInformation> response) {
                pbLogin.setVisibility(View.VISIBLE);
                HotelInformation hotelInformation = response.body();
                if(response.body()==null){
                    Toast.makeText(LoginActivity.this,
                            "Đăng nhập không thành công",Toast.LENGTH_SHORT).show();
                    pbLogin.setVisibility(View.INVISIBLE);
                    return;
                }
                Log.d(TAG, hotelInformation.toString());
                Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                intent.putExtra(Key.KEY_USER, username);
                intent.putExtra(Key.KEY_PASSWORD, password);
                startActivity(new Intent(LoginActivity.this, HomePageActivity.class));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
            }

            @Override
            public void onFailure(Call<HotelInformation> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        validateLoginInformation();
    }
}
