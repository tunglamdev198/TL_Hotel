package com.truonglam.tl_hotel.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.truonglam.tl_hotel.R;
import com.truonglam.tl_hotel.model.Room;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListRoomAdapter extends RecyclerView.Adapter<ListRoomAdapter.ViewHolder> {
    private List<Room> rooms;
    private LayoutInflater inflater;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public ListRoomAdapter(List<Room> rooms, Context context) {
        this.rooms = rooms;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_room, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Room room = rooms.get(position);
        holder.txtRoomName.setText(room.getName());
        holder.cvRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClicked(holder.getAdapterPosition(), v);
            }
        });

        holder.imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClicked(holder.getAdapterPosition(),v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cv_room)
        CardView cvRoom;

        @BindView(R.id.txt_room_name)
        TextView txtRoomName;

        @BindView(R.id.img_menu)
        ImageView imgMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(int position, View view);
    }
}
