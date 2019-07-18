package com.truonglam.tl_hotel.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.truonglam.tl_hotel.common.Key;

public class UsernameSavingSharePref {
    public UsernameSavingSharePref(){

    }
    public static void saveUsername(String username, Context context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Key.KEY_USERNAME,username);
        editor.apply();
    }

    public static String getUsername(Context context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(Key.KEY_USERNAME,null);
    }

    public static void savePassword(String password, Context context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Key.KEY_PASSWORD,password);
        editor.apply();
    }

    public static String getPassword(Context context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(Key.KEY_PASSWORD,null);
    }

}
