package com.truonglam.tl_hotel.tasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class LoadImageAsynctask extends AsyncTask<String, Void, Bitmap> {
    private ImageView imageView;

    public LoadImageAsynctask(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String url = strings[0];
        Bitmap bitmap = null;
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
