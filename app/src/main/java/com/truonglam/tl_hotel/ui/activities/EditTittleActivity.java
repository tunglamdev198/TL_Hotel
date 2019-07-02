package com.truonglam.tl_hotel.ui.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.truonglam.tl_hotel.R;
import com.truonglam.tl_hotel.common.Key;
import com.truonglam.tl_hotel.ui.fragments.NotificationFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class EditTittleActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btnDone)
    Button btnDone;

    @BindView(R.id.btnCancel)
    Button btnCancel;

    @BindView(R.id.edtTittle)
    TextInputEditText edtTittle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tittle);
        ButterKnife.bind(this);
        initViews();
        registerListener();
    }

    private void initViews() {
        String tittle = getIntent().getStringExtra(Key.KEY_TITTLE);
        edtTittle.setText(tittle);
        edtTittle.setSelection(tittle.length());
    }

    private void registerListener() {
        btnDone.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void editTittle() {
        String tittle = edtTittle.getText().toString().trim();
        if (tittle.isEmpty()) {
            Toasty.error(this, "Tiêu đề không được để trống!",
                    Toast.LENGTH_SHORT, true)
                    .show();

            return;
        } else {
            NotificationFragment notificationFragment = NotificationFragment.newInstance(tittle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container,notificationFragment)
                    .commit();
            finish();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDone:
                editTittle();
                break;

            case R.id.btnCancel:
                finish();
                break;
        }
    }
}
