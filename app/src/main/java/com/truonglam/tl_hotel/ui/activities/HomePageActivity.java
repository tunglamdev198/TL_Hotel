package com.truonglam.tl_hotel.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.truonglam.tl_hotel.R;
import com.truonglam.tl_hotel.model.HotelInformation;
import com.truonglam.tl_hotel.ui.fragments.HotelInformationFragment;
import com.truonglam.tl_hotel.ui.fragments.LoginFragment;
import com.truonglam.tl_hotel.utils.SavingUPSharePref;
import com.truonglam.tl_hotel.webservice.Client;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePageActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        final String username = SavingUPSharePref.getUsername(this);
        String password = SavingUPSharePref.getPassword(this);
        if (username == null && password  == null) {
            loadFragment(new LoginFragment());
        }

        if (username != null && password  != null) {
            Client.getService().getInformation(username, password).enqueue(new Callback<HotelInformation>() {
                @Override
                public void onResponse(Call<HotelInformation> call, Response<HotelInformation> response) {
                    HotelInformation hotelInfo = response.body();
                    HotelInformationFragment fragment = HotelInformationFragment.newInstance(hotelInfo, username);
                    loadFragment(fragment);
                }

                @Override
                public void onFailure(Call<HotelInformation> call, Throwable t) {

                }
            });
        }
    }

    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_in_right)
                    .replace(R.id.container, fragment)
                    .commit();
        }
    }
}
