package com.truonglam.tl_hotel.handler;

import android.os.Handler;

public class MyHandler extends Handler {
    private Runnable mRunnable;
    private Long timeDelay;

    public  MyHandler(){
    }

    public MyHandler(Runnable mRunnable, Long timeDelay) {
        this.mRunnable = mRunnable;
        this.timeDelay = timeDelay;
    }

    public void start(){
        postDelayed(mRunnable,timeDelay);
    }

}
