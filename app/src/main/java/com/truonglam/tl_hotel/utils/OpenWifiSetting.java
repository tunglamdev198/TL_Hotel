package com.truonglam.tl_hotel.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

public class OpenWifiSetting {

    public OpenWifiSetting() {
    }

    public static  void open(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Không có kết nối Internet. Vui lòng thiết lập lại trong mục Cài đặt")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
