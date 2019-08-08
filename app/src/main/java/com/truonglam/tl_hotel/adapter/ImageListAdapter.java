package com.truonglam.tl_hotel.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.truonglam.tl_hotel.R;
import com.truonglam.tl_hotel.webservice.Client;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ViewHolder> {

    private List<String> images;
    private LayoutInflater inflater;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public ImageListAdapter(List<String> images, Context context) {
        this.images = images;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_image_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        String image = images.get(position);
        Picasso.with(context).load(Client.BASE_URL+ image).into(holder.imgBackground);
        holder.cvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClicked(holder.getAdapterPosition(), v);
            }
        });

        holder.btnDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClicked(holder.getAdapterPosition(), v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void updateData(List<String> listImage){
        images.clear();
        images.addAll(listImage);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cvImage)
        CardView cvImage;

        @BindView(R.id.imgBackground)
        ImageView imgBackground;

        @BindView(R.id.btnDeleteImage)
        ImageView btnDeleteImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(int position, View view);
    }
}
