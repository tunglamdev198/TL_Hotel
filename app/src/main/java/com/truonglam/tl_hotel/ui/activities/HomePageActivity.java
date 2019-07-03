package com.truonglam.tl_hotel.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.truonglam.tl_hotel.R;
import com.truonglam.tl_hotel.common.Key;
import com.truonglam.tl_hotel.model.HotelInformation;
import com.truonglam.tl_hotel.ui.fragments.HotelInformationFragment;
import com.truonglam.tl_hotel.webservice.Client;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePageActivity extends AppCompatActivity {
    public static final String TAG = "HomePageActivity";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        String username = getIntent().getStringExtra(Key.KEY_USER);
        String password = getIntent().getStringExtra(Key.KEY_PASSWORD);
        Client.getService().getInformation(username,password).enqueue(new Callback<HotelInformation>() {
            @Override
            public void onResponse(Call<HotelInformation> call, Response<HotelInformation> response) {
                HotelInformation hotelInformation = response.body();
                HotelInformationFragment fragment = HotelInformationFragment.newInstance(hotelInformation);
                loadFragment(fragment);
            }

            @Override
            public void onFailure(Call<HotelInformation> call, Throwable t) {

            }
        });


    }

    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
    }
}
