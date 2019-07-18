package com.truonglam.tl_hotel.adapter;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.truonglam.tl_hotel.R;
import com.truonglam.tl_hotel.model.HotelService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HotelServiceAdapter extends RecyclerView.Adapter<HotelServiceAdapter.ViewHolder> {

    private List<HotelService> services;
    private LayoutInflater inflater;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public HotelServiceAdapter(List<HotelService> services, Context context) {
        this.services = services;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_hotel_service, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        HotelService hotelService = services.get(position);
        holder.txtServiceName.setText(hotelService.getTittle());
        holder.imgShowOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClicked(holder.getAdapterPosition(),v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return services.size();
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cvHotelService)
        CardView cvHotelService;

        @BindView(R.id.txtServiceName)
        TextView txtServiceName;

        @BindView(R.id.img_show_optoion)
        ImageView imgShowOption;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(int position, View view);
    }
}
