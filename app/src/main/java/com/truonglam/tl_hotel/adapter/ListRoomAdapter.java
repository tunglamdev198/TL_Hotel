package com.truonglam.tl_hotel.adapter;

import android.content.Context;
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
import com.truonglam.tl_hotel.model.Room;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListRoomAdapter extends RecyclerView.Adapter<ListRoomAdapter.ViewHolder> {
    private List<Room> rooms;
    private LayoutInflater inflater;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private  OnLongItemClickListener onLongItemClickListener;

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
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Room room = rooms.get(position);
        holder.txtRoomName.setText(room.getName());
        holder.swipeMenu.setShowMode(SwipeLayout.ShowMode.PullOut);
        holder.swipeMenu.addDrag(SwipeLayout.DragEdge.Right, holder.swipeMenu.findViewById(R.id.swipe_rtl));

        holder.llContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemClickListener.onItemClicked(position,v);
                return false;
            }
        });

        holder.llContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onLongItemClickListener.onLongItemClicked(holder.getAdapterPosition());
                return false;
            }
        });

        holder.llContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClicked(holder.getAdapterPosition(),v);
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClicked(holder.getAdapterPosition(),v);
            }
        });


        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
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

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnLongItemClickListener(OnLongItemClickListener onLongItemClickListener) {
        this.onLongItemClickListener = onLongItemClickListener;
    }

    public void updateData(List<Room> roomList){
        setRooms(roomList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.txt_room_name)
        TextView txtRoomName;

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

    public interface OnLongItemClickListener {
        void onLongItemClicked(int position);
    }
}
