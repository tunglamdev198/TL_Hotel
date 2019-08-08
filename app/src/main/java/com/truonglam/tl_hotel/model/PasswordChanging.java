package com.truonglam.tl_hotel.model;

import com.google.gson.annotations.SerializedName;

public class PasswordChanging {


    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("newpassword")
    private String newPassword;

    public PasswordChanging() {
    }

    public PasswordChanging(String username, String password, String newPassword) {
        this.username = username;
        this.password = password;
        this.newPassword = newPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
