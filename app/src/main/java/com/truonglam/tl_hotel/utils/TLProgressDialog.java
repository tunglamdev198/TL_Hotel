package com.truonglam.tl_hotel.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class TLProgressDialog {

    private ProgressDialog dialog;


    public TLProgressDialog(Context context) {
        dialog = new ProgressDialog(context);
    }

    public void showDialog(String message) {
        dialog.setMessage(message);
        dialog.show();
    }

    public void closeDialog() {
        dialog.dismiss();
    }
}
