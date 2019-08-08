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
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.truonglam.tl_hotel.ui.widgets.IconTextView;
import com.truonglam.tl_hotel.utils.ProgressDialogUtil;
import com.truonglam.tl_hotel.viewmodel.RoomClusterViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoomsFragment extends Fragment implements View.OnClickListener,
        AddEditRoomClusterFragment.ClusterRoomDialogListener
        , ClusterRoomAdapter.OnItemClickListener
        , SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "RoomsFragment";

    @BindView(R.id.rv_list_room_cluster)
    RecyclerView rvRoomCluster;

    @BindView(R.id.spinkit_loading)
    ProgressBar spinkitLoading;

    @BindView(R.id.btn_add)
    FloatingActionButton btnAdd;

    @BindView(R.id.txt_status)
    TextView txtStatus;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.search_view)
    CardView searchView;

    @BindView(R.id.btn_hide)
    ImageView btnHide;

    @BindView(R.id.btn_search)
    IconTextView btnSearch;

    @BindView(R.id.edt_search)
    EditText edtSearch;

    @BindView(R.id.tb_rooms)
    Toolbar tbRooms;

    private ClusterRoomAdapter adapter;

    private HotelInformation hotelInformation;

    private List<RoomCluster> roomClusterList;

    private RoomClusterViewModel mViewModel;

    private int index;

    private String token;

    private ProgressDialogUtil dialog;

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
        configRecyclerView();
        registerListener();
        search();
    }

    private void registerListener() {
        btnAdd.setOnClickListener(this);
        refreshLayout.setOnRefreshListener(this);
        btnHide.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
    }


    private void configRecyclerView() {
        spinkitLoading.setVisibility(View.VISIBLE);
        hotelInformation =
                (HotelInformation) getArguments().getSerializable(Key.KEY_HOTEL_INFORMATION);

        token = hotelInformation.getAccessToken();

        // mViewModel.init(token,hotelInformation.getHotelId());

        mViewModel.getRoomClusters(getActivity(), token, hotelInformation.getHotelId())
                .observe(getActivity(), new Observer<List<RoomCluster>>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onChanged(@Nullable List<RoomCluster> roomClusters) {
                        if (roomClusters == null) {
                            txtStatus.setVisibility(View.VISIBLE);
                            return;
                        }
                        roomClusterList = roomClusters;
                        adapter = new ClusterRoomAdapter(roomClusterList, getActivity());
                        rvRoomCluster.setAdapter(adapter);
                        DividerItemDecoration dividerHorizontal =
                                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
                        dividerHorizontal.
                                setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.devider_recyclerview));
                        rvRoomCluster.addItemDecoration(dividerHorizontal);
                        spinkitLoading.setVisibility(View.GONE);
                        btnAdd.setVisibility(View.VISIBLE);
                        adapter.setOnItemClickListener(RoomsFragment.this);
                        adapter.setOnLongItemClickListener(new ClusterRoomAdapter.OnLongItemClickListener() {
                            @Override
                            public void onLongItemClicked(int position, View view) {
                                index = position;
                            }
                        });
                        registerForContextMenu(rvRoomCluster);
                        // showOption();
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

            case R.id.btn_add:
                AddEditRoomClusterFragment fragment = AddEditRoomClusterFragment
                        .newInstance(Key.MODE_ADD_CLUSTER_ROOM, "");
                fragment.show(getFragmentManager(), "AddEditClusterRoomFragment");
                fragment.setListener(this);
                break;

            case R.id.btn_search:
                searchView.setVisibility(View.VISIBLE);
                tbRooms.setVisibility(View.GONE);
                break;

            case R.id.btn_hide:
                searchView.setVisibility(View.GONE);
                tbRooms.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }
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
        RoomCluster roomCluster = new RoomCluster(hotelInfo.getHotelId(), clusterName, "0");
        mViewModel.addRoomCluster(getActivity(), hotelInfo.getAccessToken(), roomCluster);
        updateData();
    }

    public void deleteClusterRoom(HotelInformation hotelInfo, String id) {
        mViewModel.deleteRoomCluster(getActivity(), hotelInfo.getAccessToken(), id);
        updateData();
    }

    public void editClusterRoom(HotelInformation hotelInfo, ClusterRoomUpdating updating) {
        mViewModel.updateRoomCluster(getActivity(), hotelInfo.getAccessToken(), updating);
        updateData();
    }

    private void openEditDialog(String clusterRoomName) {
        AddEditRoomClusterFragment fragment =
                AddEditRoomClusterFragment.newInstance(Key.MODE_EDIT_CLUSTER_ROOM, clusterRoomName);
        fragment.show(getFragmentManager(), "AddEditRoomClusterFragment");
        fragment.setListener(RoomsFragment.this);
    }

    private void deleteRoomClusterDialog(RoomCluster roomCluster) {
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
    }

    private void updateData() {
        mViewModel.getRoomClusters(getActivity(), token, hotelInformation.getHotelId())
                .observe(getActivity(), new Observer<List<RoomCluster>>() {
                    @Override
                    public void onChanged(@Nullable List<RoomCluster> roomClusters) {
                        loadProgressBar();
                        adapter.updateData(roomClusters);
                        spinkitLoading.setVisibility(View.GONE);
                    }
                });
    }

    private void search() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.equals("")) {
                    return;
                }
                List<RoomCluster> filter = searchRoomByKey(s.toString());
                adapter.updateData(filter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private List<RoomCluster> searchRoomByKey(String key) {
        List<RoomCluster> result = new ArrayList<>();
        for (RoomCluster room : roomClusterList) {
            if (room.getName().toLowerCase().contains(key.toLowerCase())) {
                result.add(room);
            }
        }
        return result;
    }


    private void showDialog(final String id, String clusterRoomName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false)
                .setMessage("Bạn có chắc chắn muốn xóa cụm phòng " + clusterRoomName + " không?")
                .setTitle("Xóa")
                .setPositiveButton(R.string.option_done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteClusterRoom(hotelInformation, id);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.option_cancel, new DialogInterface.OnClickListener() {
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
                break;

            case Key.MODE_EDIT_CLUSTER_ROOM:
                String id = roomClusterList.get(index).getId();
                ClusterRoomUpdating updating = new ClusterRoomUpdating(id, clusterName);
                editClusterRoom(hotelInformation, updating);
                break;


        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.menu_cluster_room, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        RoomCluster roomCluster = roomClusterList.get(index);
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
                deleteRoomClusterDialog(roomCluster);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        ProgressDialogUtil.showDialog(getActivity(), "Đang tải");
        updateData();
        refreshLayout.setRefreshing(false);
        ProgressDialogUtil.closeDialog();
    }

    @Override
    public void onItemClicked(int position, View view) {
        index = position;
        RoomCluster roomCluster = roomClusterList.get(position);
        switch (view.getId()) {
            case R.id.ll_content:
                openListRoomFragment(roomCluster
                        , hotelInformation.getAccessToken()
                        , hotelInformation);
                break;

            case R.id.btn_edit:
                openEditDialog(roomCluster.getName());
                break;

            case R.id.btn_delete:
                deleteRoomClusterDialog(roomCluster);
                break;
        }
    }
}
