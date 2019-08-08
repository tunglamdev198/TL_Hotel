package com.truonglam.tl_hotel.adapter;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.truonglam.tl_hotel.R;
import com.truonglam.tl_hotel.model.HotelService;
import com.truonglam.tl_hotel.ui.widgets.IconTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HotelServiceAdapter extends RecyclerView.Adapter<HotelServiceAdapter.ViewHolder> {

    private List<HotelService> services;
    private LayoutInflater inflater;
    private Context context;
    public boolean check;
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
        String serviceIcon = hotelService.getIcon().trim();
        String temp = new String(Character.toChars(Integer.parseInt(
                serviceIcon, 16)));
        String codeColor = hotelService.getColorIcon();
        holder.imgAvatar.setText(temp);
        holder.imgAvatar.setTextColor(Color.parseColor(codeColor));
        holder.swipeMenu.setShowMode(SwipeLayout.ShowMode.PullOut);
        holder.swipeMenu.addDrag(SwipeLayout.DragEdge.Right, holder.swipeMenu.findViewById(R.id.swipe_rtl));

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClicked(holder.getAdapterPosition(), v);
                holder.swipeMenu.close();
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClicked(holder.getAdapterPosition(), v);
                holder.swipeMenu.close();
            }
        });

        if (check == true) {
            holder.llContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClicked(holder.getAdapterPosition(), v);
                }
            });

        } else return;


    }

    @Override
    public int getItemCount() {
        return services.size();
    }




    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setServices(List<HotelService> services) {
        this.services = services;
    }

    public void updateData(List<HotelService> listService) {
        setServices(listService);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtServiceName)
        TextView txtServiceName;

        @BindView(R.id.img_avatar)
        IconTextView imgAvatar;

        @BindView(R.id.swipe_menu)
        SwipeLayout swipeMenu;

        @BindView(R.id.btn_edit)
        LinearLayout btnEdit;

        @BindView(R.id.btn_delete)
        LinearLayout btnDelete;

        @BindView(R.id.ll_content)
        LinearLayout llContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(int position, View view);
    }
}
