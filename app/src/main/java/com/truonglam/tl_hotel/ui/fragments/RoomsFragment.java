package com.truonglam.tl_hotel.ui.fragments;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.truonglam.tl_hotel.R;
import com.truonglam.tl_hotel.TLApp;
import com.truonglam.tl_hotel.adapter.ClusterRoomAdapter;
import com.truonglam.tl_hotel.common.Key;
import com.truonglam.tl_hotel.model.ClusterRoomUpdating;
import com.truonglam.tl_hotel.model.HotelInformation;
import com.truonglam.tl_hotel.model.Room;
import com.truonglam.tl_hotel.model.RoomCluster;
import com.truonglam.tl_hotel.utils.TLProgressDialog;
import com.truonglam.tl_hotel.viewmodel.RoomClusterViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoomsFragment extends Fragment implements View.OnClickListener, AddEditRoomClusterFragment.ClusterRoomDialogListener
        , SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "RoomsFragment";

    @BindView(R.id.rv_list_room_cluster)
    RecyclerView rvRoomCluster;

    @BindView(R.id.btnBack)
    ImageView btnBack;

    @BindView(R.id.spinkit_loading)
    ProgressBar spinkitLoading;

    @BindView(R.id.btn_add)
    FloatingActionButton btnAdd;

    @BindView(R.id.txt_status)
    TextView txtStatus;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    private ClusterRoomAdapter adapter;

    private HotelInformation hotelInformation;

    private List<RoomCluster> roomClusterList;

    private RoomClusterViewModel mViewModel;

    private int index;

    private TLProgressDialog dialog;

    public RoomsFragment() {
    }

    public static RoomsFragment newInstance(HotelInformation hotelInformation) {

        Bundle args = new Bundle();
        args.putSerializable(Key.KEY_HOTEL_INFORMATION, hotelInformation);
        RoomsFragment fragment = new RoomsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rooms, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(TLApp.getInstance())
                .create(RoomClusterViewModel.class);
        dialog = new TLProgressDialog(getActivity());
        configRecyclerView();
        registerListener();
    }

    private void registerListener() {
        btnBack.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        refreshLayout.setOnRefreshListener(this);
    }


    private void configRecyclerView() {
        spinkitLoading.setVisibility(View.VISIBLE);
        hotelInformation =
                (HotelInformation) getArguments().getSerializable(Key.KEY_HOTEL_INFORMATION);

        final String token = hotelInformation.getAccessToken();

        mViewModel.getRoomCLusterLiveData(token, hotelInformation.getHotelId())
                .observe(getActivity(), new Observer<List<RoomCluster>>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onChanged(@Nullable List<RoomCluster> roomClusters) {
                        if (roomClusters.isEmpty()) {
                            txtStatus.setVisibility(View.VISIBLE);
                        }
                        roomClusterList = roomClusters;
                        adapter = new ClusterRoomAdapter(roomClusterList, getActivity());
                        rvRoomCluster.setAdapter(adapter);
                        spinkitLoading.setVisibility(View.GONE);
                        btnAdd.setVisibility(View.VISIBLE);
                        showOption();
                    }
                });

    }

    private void loadProgressBar() {
        spinkitLoading.setVisibility(View.VISIBLE);
        Sprite doubleBounce = new DoubleBounce();
        spinkitLoading.setIndeterminateDrawable(doubleBounce);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 1500);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                getActivity().getSupportFragmentManager().popBackStack();
                break;

            case R.id.btn_add:
                AddEditRoomClusterFragment fragment = AddEditRoomClusterFragment
                        .newInstance(Key.MODE_ADD_CLUSTER_ROOM, "");
                fragment.show(getFragmentManager(), "AddEditClusterRoomFragment");
                fragment.setListener(this);
                break;

            default:
                break;
        }
    }


    private void showOption() {
        adapter.setOnItemClickListener(new ClusterRoomAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(final int position, View view) {
                index = position;
                PopupMenu menu = new PopupMenu(getActivity(), view);
                MenuInflater inflater = menu.getMenuInflater();
                inflater.inflate(R.menu.menu_cluster_room, menu.getMenu());
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        RoomCluster roomCluster = roomClusterList.get(position);

                        switch (item.getItemId()) {
                            case R.id.mnu_view:
                                openListRoomFragment(roomCluster
                                        , hotelInformation.getAccessToken()
                                        , hotelInformation);
                                break;

                            case R.id.mnu_edit:
                                openEditDialog(roomCluster.getName());
                                break;

                            case R.id.mnu_delete:
                                Log.d(TAG, roomCluster.toString());
                                if (roomCluster.getCheck().equals("1")) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setMessage(R.string.msg_hotel_delete_confirm)
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                } else {
                                    showDialog(roomCluster.getId(), roomCluster.getName());
                                }
                                break;
                        }
                        return true;
                    }
                });
                menu.show();
            }
        });

    }

    private void openListRoomFragment(RoomCluster roomCluster,
                                      String token,
                                      HotelInformation hotelInformation) {
        ListRoomFragment listRoomFragment = ListRoomFragment
                .newInstance(roomCluster, token, hotelInformation);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, listRoomFragment)
                .setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left)
                .addToBackStack(null)
                .commit();
    }

    public void addClusterRoom(HotelInformation hotelInfo, String clusterName) {
        RoomCluster roomCluster = new RoomCluster(hotelInfo.getHotelId(), clusterName, "1");
        mViewModel.addRoomCluster(hotelInfo.getAccessToken(), roomCluster);

    }

    public void deleteClusterRoom(HotelInformation hotelInfo, String id) {
        mViewModel.deleteRoomCluster(hotelInfo.getAccessToken(), id);

    }

    public void editClusterRoom(HotelInformation hotelInfo, ClusterRoomUpdating updating) {
        mViewModel.editRoomCluster(hotelInfo.getAccessToken(), updating);
    }

    private void openEditDialog(String clusterRoomName) {
        AddEditRoomClusterFragment fragment =
                AddEditRoomClusterFragment.newInstance(Key.MODE_EDIT_CLUSTER_ROOM, clusterRoomName);
        fragment.show(getFragmentManager(), "AddEditRoomClusterFragment");
        fragment.setListener(RoomsFragment.this);
    }

    private void updateData() {
        mViewModel.getRoomCLusterLiveData(hotelInformation.getAccessToken(), hotelInformation.getHotelId())
                .observe(getActivity(), new Observer<List<RoomCluster>>() {
                    @Override
                    public void onChanged(@Nullable List<RoomCluster> roomClusters) {
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void loadDialog(String message) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(message);
        progressDialog.setProgress(0);
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

        }).start();
        updateData();
    }

    private void showDialog(final String id, String clusterRoomName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false)
                .setMessage("Bạn có chắc chắn muốn xóa cụm phòng " + clusterRoomName + " không?")
                .setTitle("Xóa")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteClusterRoom(hotelInformation, id);
                        dialog.dismiss();
                        loadDialog("Đang xóa Cụm phòng...");
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void applyText(String clusterName, String mode) {
        switch (mode) {
            case Key.MODE_ADD_CLUSTER_ROOM:
                addClusterRoom(hotelInformation, clusterName);
                loadDialog("Đang thêm mới...");
                break;

            case Key.MODE_EDIT_CLUSTER_ROOM:
                String id = roomClusterList.get(index).getId();
                ClusterRoomUpdating updating = new ClusterRoomUpdating(id, clusterName);
                editClusterRoom(hotelInformation, updating);
                loadDialog("Đang sửa...");
                updateData();
                break;


        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        updateData();
        refreshLayout.setRefreshing(false);
    }
}
