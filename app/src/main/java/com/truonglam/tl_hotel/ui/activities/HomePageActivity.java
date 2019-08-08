package com.truonglam.tl_hotel.ui.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.truonglam.tl_hotel.R;
import com.truonglam.tl_hotel.TLApp;
import com.truonglam.tl_hotel.model.HotelInformation;
import com.truonglam.tl_hotel.receiver.NetworkChangedReceiver;
import com.truonglam.tl_hotel.ui.fragments.AccountFragment;
import com.truonglam.tl_hotel.ui.fragments.HotelInformationFragment;
import com.truonglam.tl_hotel.ui.fragments.LoginFragment;
import com.truonglam.tl_hotel.ui.fragments.RoomsFragment;
import com.truonglam.tl_hotel.ui.fragments.ServiceFragment;
import com.truonglam.tl_hotel.utils.SavingUPSharePref;
import com.truonglam.tl_hotel.viewmodel.HotelInformationViewModel;
import com.truonglam.tl_hotel.webservice.Client;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePageActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
        , NetworkChangedReceiver.ConnectivityReceiverListener {


    public BottomNavigationView bottomNav;

    private HotelInformation hotelInfo;

    public Fragment currentFragment;

//    private HotelInformationFragment hotelInformationFragment;
//
//    private RoomsFragment roomsFragment;
//
//    private ServiceFragment serviceFragment;
//
//    private AccountFragment accountFragment;
//
//    private FragmentManager fm;

//    private Fragment active = hotelInformationFragment;

    private HotelInformationViewModel hiViewModel;

    private String username;

    private String password;

    private static HomePageActivity instance;

    private NetworkChangedReceiver receiver;

    public static HomePageActivity getInstance() {
        return instance;
    }

    public HotelInformation getHotelInfo() {
        return hotelInfo;
    }

    public void setHotelInfo(HotelInformation hotelInfo) {
        this.hotelInfo = hotelInfo;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        //checkConnection();
        // registerReceiver();
        instance = this;
        initViews();

    }

    private void initViews() {
        if (!NetworkChangedReceiver.isConnected(this)) {
            openWifiSetting();
            return;
        }
        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setVisibility(View.GONE);
        username = SavingUPSharePref.getUsername(this);
        password = SavingUPSharePref.getPassword(this);

        hiViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(TLApp.getInstance())
                .create(HotelInformationViewModel.class);

        if (username == null && password == null) {
            bottomNav.setVisibility(View.GONE);
            loadFragment(new LoginFragment());
        }

        if (username != null && password != null) {
            hiViewModel.getHotelInformation(username, password).observe(this, new Observer<HotelInformation>() {
                @Override
                public void onChanged(@Nullable HotelInformation hotelInformation) {
                    hotelInfo = hotelInformation;
                    if (hotelInfo == null) {
                        return;
                    }
                    HotelInformationFragment fragment = HotelInformationFragment.newInstance(hotelInfo);
                    loadFragment(fragment);
                    bottomNav.setVisibility(View.VISIBLE);
                }
            });
        }
        bottomNav.setOnNavigationItemSelectedListener(this);
    }

//    private void initFragment(){
//        fm = getSupportFragmentManager();
//        fm.beginTransaction().add(R.id.container,accountFragment,"fragment_account").hide(accountFragment).commit();
//        fm.beginTransaction().add(R.id.container,serviceFragment,"fragment_service").hide(accountFragment).commit();
//        fm.beginTransaction().add(R.id.container,roomsFragment,"fragment_rooms").hide(accountFragment).commit();
//        fm.beginTransaction().add(R.id.container,hotelInformationFragment,"fragment_hotelinfo").commit();
//    }

    private void registerReceiver() {
        receiver = new NetworkChangedReceiver();
        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(receiver, filter);
    }

    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.container, fragment)
                    .commit();
        }
    }

    private void openWifiSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Không có kết nối Internet. Vui lòng thiết lập lại trong mục Cài đặt")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.mnu_notification:
                HotelInformationFragment informationFragment = HotelInformationFragment.newInstance(hotelInfo);
                currentFragment = informationFragment;
                if (NetworkChangedReceiver.isConnected(this)) {
                    loadFragment(informationFragment);
                } else {
                    openWifiSetting();
                }

                break;

            case R.id.mnu_room:
                RoomsFragment roomsFragment = RoomsFragment.newInstance(hotelInfo);
                currentFragment = roomsFragment;
                if (NetworkChangedReceiver.isConnected(this)) {
                    loadFragment(roomsFragment);
                } else {
                    openWifiSetting();
                }

                break;

            case R.id.mnu_service:
                ServiceFragment serviceFragment = ServiceFragment.newInstance(hotelInfo);
                currentFragment = serviceFragment;
                if (NetworkChangedReceiver.isConnected(this)) {
                    loadFragment(serviceFragment);
                } else {
                    openWifiSetting();
                }
                break;

            case R.id.mnu_account:
                AccountFragment accountFragment = AccountFragment.newInstance(hotelInfo, username);
                currentFragment = accountFragment;
                if (NetworkChangedReceiver.isConnected(this)) {
                    loadFragment(accountFragment);
                } else {
                    openWifiSetting();
                }
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        TLApp.getInstance().setConnectivityListener(this);
        // checkConnection();
        if (NetworkChangedReceiver.isConnected(this)) {
            loadFragment(currentFragment);
        } else {
            openWifiSetting();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        unregisterReceiver(receiver);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        String message;
        if (isConnected) {
            message = "Đã kết nối Internet";
        } else {
            message = "Lỗi kết nối Internet";
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
