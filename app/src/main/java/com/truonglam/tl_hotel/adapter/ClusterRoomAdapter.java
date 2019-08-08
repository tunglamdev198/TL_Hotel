package com.truonglam.tl_hotel.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.truonglam.tl_hotel.R;
import com.truonglam.tl_hotel.model.RoomCluster;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClusterRoomAdapter extends RecyclerView.Adapter<ClusterRoomAdapter.ViewHolder> {
    private List<RoomCluster> roomClusters;
    private LayoutInflater inflater;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private OnLongItemClickListener onLongItemClickListener;

    private int index;

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
        index = holder.getAdapterPosition();
        holder.swipeMenu.setShowMode(SwipeLayout.ShowMode.PullOut);
        holder.swipeMenu.addDrag(SwipeLayout.DragEdge.Right, holder.swipeMenu.findViewById(R.id.swipe_rtl));

        holder.llContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClicked(holder.getAdapterPosition(), holder.llContent);
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClicked(holder.getAdapterPosition(), holder.btnEdit);
                holder.swipeMenu.close();
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClicked(holder.getAdapterPosition(), holder.btnDelete);
                holder.swipeMenu.close();
            }
        });

        holder.llContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onLongItemClickListener.onLongItemClicked(holder.getAdapterPosition(), v);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomClusters.size();
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnLongItemClickListener(OnLongItemClickListener onLongItemClickListener) {
        this.onLongItemClickListener = onLongItemClickListener;
    }

    public void setRoomClusters(List<RoomCluster> roomClusters) {
        this.roomClusters = roomClusters;
    }

    public void updateData(List<RoomCluster> listRoomCluster) {
        setRoomClusters(listRoomCluster);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_cluster_room_name)
        TextView txtClusterRoomName;

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
        void onLongItemClicked(int position, View view);
    }
}
