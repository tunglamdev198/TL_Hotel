package com.truonglam.tl_hotel.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogUtil {

    private static ProgressDialog dialog;


    public static void showDialog(Context context, String message) {
        dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setMessage(message);
        dialog.show();
    }

    public static void closeDialog() {
        dialog.dismiss();
    }
}
