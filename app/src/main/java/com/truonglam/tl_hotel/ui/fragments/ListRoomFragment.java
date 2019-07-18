package com.truonglam.tl_hotel.ui.fragments;


import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.truonglam.tl_hotel.R;
import com.truonglam.tl_hotel.adapter.HotelServiceAdapter;
import com.truonglam.tl_hotel.adapter.ListRoomAdapter;
import com.truonglam.tl_hotel.common.Key;
import com.truonglam.tl_hotel.model.HotelInformation;
import com.truonglam.tl_hotel.model.Room;
import com.truonglam.tl_hotel.model.RoomCluster;
import com.truonglam.tl_hotel.model.RoomUpdating;
import com.truonglam.tl_hotel.viewmodel.RoomViewModel;
import com.truonglam.tl_hotel.webservice.Client;

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
        AddEditRoomFragment.RoomDialogListener, ListRoomAdapter.OnItemClickListener{

    public static final String TAG = "RoomsFragment";

    @BindView(R.id.rv_list_room)
    RecyclerView rvListRoom;

    @BindView(R.id.btn_back)
    ImageView btnBack;

    @BindView(R.id.spinkit_loading)
    ProgressBar spinkitLoading;

    @BindView(R.id.txt_cluster_room_name)
    TextView txtClusterRoomName;

    @BindView(R.id.btn_add)
    FloatingActionButton btnAdd;

    private RoomCluster roomCluster;

    private HotelInformation information;

    private String token;

    private ListRoomAdapter adapter;

    private List<Room> rooms;

    private RoomViewModel roomViewModel;

    private int index ;


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
        initViews();
        registerListener();
        configRecyclerView();
    }

    private void initViews() {
        roomCluster = (RoomCluster) getArguments().getSerializable(Key.KEY_ROOM_CLUSTER);
        information = (HotelInformation) getArguments().getSerializable(Key.KEY_HOTEL_INFORMATION);
        token = getArguments().getString(Key.KEY_TOKEN);
        txtClusterRoomName.setText(roomCluster.getName());
        roomViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())
                .create(RoomViewModel.class);
    }

    private void registerListener() {
        btnBack.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
    }

    private void configRecyclerView() {
        spinkitLoading.setVisibility(View.VISIBLE);
        String id = roomCluster.getId();
        Client.getService().getRoomByIdCP(token, id).enqueue(new Callback<List<Room>>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                rooms = response.body();
                roomViewModel.setRoomLiveData(rooms);
                adapter = new ListRoomAdapter(rooms, getActivity());
                rvListRoom.setAdapter(adapter);
                spinkitLoading.setVisibility(View.GONE);
                btnAdd.setVisibility(View.VISIBLE);
                adapter.setOnItemClickListener(ListRoomFragment.this);
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {

            }
        });
    }

    private void addRoom(String roomName) {
        Room room = new Room(information.getHotelId(), roomName, roomCluster.getId());
        roomViewModel.addRoom(token,room);
    }

    private void updateData(){
        spinkitLoading.setVisibility(View.VISIBLE);

        roomViewModel.updateList(token,roomCluster.getId()).observe(getActivity(),
                new Observer<List<Room>>() {
                    @Override
                    public void onChanged(@Nullable List<Room> rooms) {
                        adapter.notifyDataSetChanged();
                        spinkitLoading.setVisibility(View.GONE);
                    }
                });
    }

    private void openEditDialog(final Room room){
        AddEditRoomFragment addEditRoomFragment =
                AddEditRoomFragment.newInstance(Key.MODE_EDIT_ROOM,room.getName());
        addEditRoomFragment.show(getFragmentManager(),"AddEditRoomFragment");
        addEditRoomFragment.setListener(ListRoomFragment.this);
    }

    private void showPopupMenu() {
        adapter.setOnItemClickListener(new ListRoomAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(final int position, View view) {
                PopupMenu menu = new PopupMenu(getActivity(), view);
                MenuInflater inflater = menu.getMenuInflater();
                inflater.inflate(R.menu.menu_edit_delete, menu.getMenu());
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.mnu_view:
                                break;

                            case R.id.mnu_edit:
                                Toast.makeText(getActivity(), "Cliked", Toast.LENGTH_SHORT).show();
                                final Room room = rooms.get(position);
                                openEditDialog(room);

                                break;

                            case R.id.mnu_delete:
                                break;
                        }
                        return true;
                    }
                });
                menu.show();
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                getActivity().getSupportFragmentManager().popBackStack();
                break;

            case R.id.btn_add:
                AddEditRoomFragment fragment = AddEditRoomFragment.newInstance(Key.MODE_ADD_ROOM,"");
                fragment.show(getFragmentManager(),"AddEditRoomFragment");
                fragment.setListener(this);
                break;
        }
    }


    @Override
    public void applyText(String roomName, String mode) {
        switch (mode) {
            case Key.MODE_ADD_ROOM:
                addRoom(roomName);
                updateData();
                break;

            case Key.MODE_EDIT_ROOM:
                RoomUpdating roomUpdating = new RoomUpdating(rooms.get(index).getId(),roomName);
                Client.getService().updateRoom(token,roomUpdating).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
                updateData();
                break;
        }
    }

    @Override
    public void onItemClicked(int position, View view) {
        index = position;
        switch (view.getId()){
            case R.id.img_menu:
                showPopupMenu();
                break;
        }
    }
}
