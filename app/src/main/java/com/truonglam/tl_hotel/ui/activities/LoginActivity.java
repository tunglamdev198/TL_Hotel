package com.truonglam.tl_hotel.ui.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.truonglam.tl_hotel.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

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
        String username = edtUserName.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (username.isEmpty() && password.isEmpty()) {
            Toasty.error(this, "Vui lòng nhập đủ thông tin",
                    Toast.LENGTH_SHORT, true).show();
            return;
        }
        else {
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
       pbLogin.setVisibility(View.VISIBLE);
       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               startActivity(new Intent(LoginActivity.this,HomePageActivity.class));
               overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
               finish();
           }
       },2000);
    }

    @Override
    public void onClick(View v) {
        validateLoginInformation();
    }
}
