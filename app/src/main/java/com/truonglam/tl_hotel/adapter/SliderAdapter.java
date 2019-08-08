package com.truonglam.tl_hotel.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.truonglam.tl_hotel.webservice.Client;

import java.util.List;

public class SliderAdapter extends PagerAdapter {

    private Context context;

    private List<String> images;

    private LayoutInflater inflater;

    public SliderAdapter(Context context, List<String> images) {
        this.context = context;
        this.images = images;
    }


    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imgBackground = new ImageView(context);
        imgBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.with(context)
                .load(Client.BASE_URL  +images.get(position))
                .fit()
                .centerCrop()
                .into(imgBackground);
        container.addView(imgBackground);
        return imgBackground;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public void updateData(List<String> listImage){
        images.clear();
        images.addAll(listImage);
        notifyDataSetChanged();
    }
}
