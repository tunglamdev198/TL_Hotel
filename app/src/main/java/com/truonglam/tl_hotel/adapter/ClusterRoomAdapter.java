package com.truonglam.tl_hotel.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.truonglam.tl_hotel.R;
import com.truonglam.tl_hotel.model.HotelService;
import com.truonglam.tl_hotel.model.RoomCluster;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClusterRoomAdapter  extends RecyclerView.Adapter<ClusterRoomAdapter.ViewHolder>{
    private List<RoomCluster> roomClusters;
    private LayoutInflater inflater;
    private Context context;
    private HotelServiceAdapter.OnItemClickListener onItemClickListener;

    public ClusterRoomAdapter(List<RoomCluster> roomClusters, Context context) {
        this.roomClusters = roomClusters;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_cluster_room, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        RoomCluster roomCluster = roomClusters.get(position);
        holder.txtClusterRoomName.setText(roomCluster.getName());
        holder.cvClusterRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClicked(holder.getAdapterPosition(),v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomClusters.size();
    }


    public void setOnItemClickListener(HotelServiceAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cv_cluster_room)
        CardView cvClusterRoom;

        @BindView(R.id.txt_cluster_room_name)
        TextView txtClusterRoomName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(int position, View view);
    }
}
