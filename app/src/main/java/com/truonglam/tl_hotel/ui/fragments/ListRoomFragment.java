package com.truonglam.tl_hotel.ui.fragments;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.content.DialogInterface;
import android.os.Bundle;
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

import com.truonglam.tl_hotel.R;
import com.truonglam.tl_hotel.TLApp;
import com.truonglam.tl_hotel.adapter.HotelServiceAdapter;
import com.truonglam.tl_hotel.adapter.ListRoomAdapter;
import com.truonglam.tl_hotel.common.Key;
import com.truonglam.tl_hotel.model.Box;
import com.truonglam.tl_hotel.model.HotelInformation;
import com.truonglam.tl_hotel.model.Room;
import com.truonglam.tl_hotel.model.RoomCluster;
import com.truonglam.tl_hotel.model.RoomUpdating;
import com.truonglam.tl_hotel.ui.activities.HomePageActivity;
import com.truonglam.tl_hotel.ui.widgets.IconTextView;
import com.truonglam.tl_hotel.utils.ProgressDialogUtil;
import com.truonglam.tl_hotel.viewmodel.RoomViewModel;
import com.truonglam.tl_hotel.webservice.Client;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListRoomFragment extends Fragment implements View.OnClickListener,
        AddEditRoomFragment.RoomDialogListener, ListRoomAdapter.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.rv_list_room)
    RecyclerView rvListRoom;

    @BindView(R.id.btn_back)
    ImageView btnBack;

    @BindView(R.id.spinkit_loading)
    ProgressBar spinkitLoading;

    @BindView(R.id.txt_cluster_room_name)
    TextView txtClusterRoomName;

    @BindView(R.id.tb_list_room)
    Toolbar tbListRoom;

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

    private boolean check = true;

    private DividerItemDecoration dividerHorizontal;

    private RoomCluster roomCluster;

    private HotelInformation information;

    private String token;

    private String id;

    private ListRoomAdapter adapter;

    private List<Room> listRoom;

    private RoomViewModel roomViewModel;

    private int index;

    public ListRoomFragment() {
    }

    public static ListRoomFragment newInstance(RoomCluster roomCluster, String token, HotelInformation hotelInformation) {

        Bundle args = new Bundle();
        args.putSerializable(Key.KEY_ROOM_CLUSTER, roomCluster);
        args.putString(Key.KEY_TOKEN, token);
        args.putSerializable(Key.KEY_HOTEL_INFORMATION, hotelInformation);
        ListRoomFragment fragment = new ListRoomFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_room, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        HomePageActivity.getInstance().currentFragment = this;
        initViews();
        registerListener();
        configRecyclerView();
        search();
    }

    private void initViews() {
        roomCluster = (RoomCluster) getArguments().getSerializable(Key.KEY_ROOM_CLUSTER);
        information = (HotelInformation) getArguments().getSerializable(Key.KEY_HOTEL_INFORMATION);
        token = getArguments().getString(Key.KEY_TOKEN);
        id = roomCluster.getId();
        txtClusterRoomName.setText(roomCluster.getName());
        roomViewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance(TLApp.getInstance())
                .create(RoomViewModel.class);
    }

    private void registerListener() {
        btnBack.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnHide.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        refreshLayout.setOnRefreshListener(this);
    }

    private void configRecyclerView() {
        spinkitLoading.setVisibility(View.VISIBLE);
        roomViewModel.getListRoom(getActivity(), token, id).observe(getActivity(), new Observer<List<Room>>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onChanged(@Nullable List<Room> rooms) {
                listRoom = rooms;
                if (rooms == null) {
                    txtStatus.setVisibility(View.VISIBLE);
                    spinkitLoading.setVisibility(View.GONE);
                    return;
                }
                adapter = new ListRoomAdapter(listRoom, getActivity());
                dividerHorizontal =
                        new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
                dividerHorizontal.setDrawable(ContextCompat.getDrawable(getActivity(),
                        R.drawable.devider_recyclerview));
                rvListRoom.addItemDecoration(dividerHorizontal);
                rvListRoom.setAdapter(adapter);
                spinkitLoading.setVisibility(View.GONE);
                btnAdd.setVisibility(View.VISIBLE);
                if (check == true) {
                    adapter.setOnItemClickListener(ListRoomFragment.this);
                } else return;
                check = false;
                adapter.setOnLongItemClickListener(new ListRoomAdapter.OnLongItemClickListener() {
                    @Override
                    public void onLongItemClicked(int position) {
                        index = position;
                    }
                });
                registerForContextMenu(rvListRoom);
            }
        });
    }

    private void addRoom(String roomName) {
        Room room = new Room(information.getHotelId(), roomName, roomCluster.getId());
        roomViewModel.addRoom(getActivity(), token, room);
        updateData();
    }

    private void updateData() {
        spinkitLoading.setVisibility(View.VISIBLE);
        roomViewModel.getListRoom(getActivity(), token, id).observe(getActivity(),
                new Observer<List<Room>>() {
                    @Override
                    public void onChanged(@Nullable List<Room> rooms) {
                        adapter.updateData(rooms);
                        spinkitLoading.setVisibility(View.GONE);
                    }
                });
    }

    private void openEditDialog(final Room room) {
        AddEditRoomFragment addEditRoomFragment =
                AddEditRoomFragment.newInstance(Key.MODE_EDIT_ROOM, room.getName());
        addEditRoomFragment.show(getFragmentManager(), "AddEditRoomFragment");
        addEditRoomFragment.setListener(ListRoomFragment.this);
    }

    private void showDialog(final String id, String roomName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false)
                .setMessage("Bạn có chắc chắn muốn xóa phòng " + roomName + " này không?")
                .setTitle("Xóa")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        roomViewModel.deleteRoom(getActivity(), token, id);
                        updateData();
                        dialog.dismiss();
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.menu_edit_delete_room, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Room room = listRoom.get(index);
        switch (item.getItemId()) {
            case R.id.mnu_edit:
                openEditDialog(room);
                break;

            case R.id.mnu_delete:
                showDialog(room.getId(), room.getName());
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                getActivity().getSupportFragmentManager().popBackStack();
                break;

            case R.id.btn_add:
                AddEditRoomFragment fragment = AddEditRoomFragment.newInstance(Key.MODE_ADD_ROOM, "");
                fragment.show(getFragmentManager(), "AddEditRoomFragment");
                fragment.setListener(this);
                break;

            case R.id.btn_search:
                searchView.setVisibility(View.VISIBLE);
                tbListRoom.setVisibility(View.GONE);
                break;

            case R.id.btn_hide:
                searchView.setVisibility(View.GONE);
                tbListRoom.setVisibility(View.VISIBLE);
                break;
        }
    }


    @Override
    public void applyText(String roomName, String mode) {
        switch (mode) {
            case Key.MODE_ADD_ROOM:
                addRoom(roomName);
                txtStatus.setVisibility(View.GONE);
                break;

            case Key.MODE_EDIT_ROOM:
                RoomUpdating roomUpdating = new RoomUpdating(listRoom.get(index).getId(), roomName);
                ProgressDialogUtil.showDialog(getActivity(), "Đang sửa");
                Client.getService().updateRoom(token, roomUpdating).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            updateData();
                            ProgressDialogUtil.closeDialog();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
                break;
        }
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
                List<Room> filter = searchRoomByKey(s.toString());
                adapter.updateData(filter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private List<Room> searchRoomByKey(String key) {
        List<Room> result = new ArrayList<>();
        for (Room room : listRoom) {
            if (room.getName().toLowerCase().contains(key.toLowerCase())) {
                result.add(room);
            }
        }
        return result;
    }

    private void displayListBox(List<Box> boxes) {
        String message = "";
        if (boxes.size() == 0) {
            message = "Hiện tại không có Box nào";
        }

        for (Box box : boxes) {
            String mac = "MAC : " + box.getMac();
            String serial = "Serial : " + box.getSerial();
            message += mac + "\n" + serial + "\n\n";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Thông tin Box")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onItemClicked(int position, View view) {
        index = position;
        Room room = listRoom.get(position);

        switch (view.getId()) {
            case R.id.ll_content:
                ProgressDialogUtil.showDialog(getActivity(),"Đang tải");
                Client.getService().getBoxByRoomId(token, room.getId()).enqueue(new Callback<List<Box>>() {
                    @Override
                    public void onResponse(Call<List<Box>> call, Response<List<Box>> response) {
                        if (response.isSuccessful()) {
                            displayListBox(response.body());
                            ProgressDialogUtil.closeDialog();
                        }

                    }

                    @Override
                    public void onFailure(Call<List<Box>> call, Throwable t) {
                        ProgressDialogUtil.closeDialog();
                    }
                });

                break;

            case R.id.btn_edit:
                openEditDialog(room);
                break;

            case R.id.btn_delete:
                showDialog(room.getId(), room.getName());
                break;

        }
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
