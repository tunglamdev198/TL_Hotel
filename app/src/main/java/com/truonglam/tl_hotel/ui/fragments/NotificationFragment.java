package com.truonglam.tl_hotel.ui.fragments;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.truonglam.tl_hotel.R;
import com.truonglam.tl_hotel.adapter.ImageListAdapter;
import com.truonglam.tl_hotel.common.Key;
import com.truonglam.tl_hotel.model.HotelBackground;
import com.truonglam.tl_hotel.model.HotelInformation;
import com.truonglam.tl_hotel.ui.widgets.IconTextView;
import com.truonglam.tl_hotel.webservice.Client;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_CODE_EDIT_TITTLE = 1;
    private static final int REQUEST_CODE_PERMISSIONS = 10;


    @BindView(R.id.tbNotification)
    Toolbar tbNotification;

    @BindView(R.id.btnBack)
    ImageView btnBack;

    @BindView(R.id.btnEdit)
    IconTextView btnEdit;

    @BindView(R.id.btnAddPhoto)
    ImageView btnAddPhoto;

    @BindView(R.id.txt_tittle)
    TextView txtTittle;

    @BindView(R.id.txt_hotel_name)
    TextView txtHotelName;

    @BindView(R.id.rvListImage)
    RecyclerView rvListImage;

    @BindView(R.id.btn_edit_avatar)
    ImageView btnEditAvatar;

    @BindView(R.id.img_avatar)
    CircleImageView imgAvatar;

    private ImageListAdapter adapter;
    private List<String> images;

    private HotelInformation hotelInfo;

    private int index;


    public static final String TAG = "NotificationFragment";

    public NotificationFragment() {

    }

    public static NotificationFragment newInstance(HotelBackground hotelBackground, HotelInformation hotelInformation) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putSerializable(Key.KEY_HOTEL_BACKGROUND, hotelBackground);
        args.putSerializable(Key.KEY_HOTEL_INFORMATION, hotelInformation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initViews();
        configRecyclerView();
        registerListener();
    }

    private void initViews() {
        if (getArguments() != null) {
            HotelBackground hotelBackground = (HotelBackground) getArguments()
                    .getSerializable(Key.KEY_HOTEL_BACKGROUND);
            images = hotelBackground.getLinks();
            hotelInfo = (HotelInformation) getArguments()
                    .getSerializable(Key.KEY_HOTEL_INFORMATION);
            String tittle = hotelInfo.getTittle();
            String hotelName = hotelInfo.getName();
            if (tittle.length() > 20) {
                txtTittle.setText(tittle.substring(0, 20) + "...");
                return;
            }

            if (hotelName.length() > 20) {
                txtHotelName.setText(hotelName.substring(0, 20) + "...");
            }
            txtTittle.setText(tittle);
            txtHotelName.setText(hotelName);
        }
    }

    private void registerListener() {
        btnBack.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        btnAddPhoto.setOnClickListener(this);
        btnEditAvatar.setOnClickListener(this);
    }

    private void closeFragment() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private void configRecyclerView() {
        adapter = new ImageListAdapter(images, getActivity());
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        rvListImage.setLayoutManager(llm);
        rvListImage.setAdapter(adapter);
        registerForContextMenu(rvListImage);
        adapter.setOnItemClickListener(new ImageListAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position, View view) {
                index = position;
                switch (view.getId()) {
                    case R.id.cvImage:
                        break;

                    case R.id.btnDeleteImage:
                        deleteImage();
                        break;
                }
            }
        });
    }

    private void deleteImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("Xóa ảnh")
                .setMessage("Bạn có muốn xóa ảnh này không?")
                .setCancelable(false)
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        images.remove(index);
                        adapter.notifyItemRemoved(index);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void editTittle() {

        EditTittleFragment editTittleFragment = EditTittleFragment.newInstance(hotelInfo, Key.MODE_EDIT_ROOM);
        editTittleFragment.show(getFragmentManager(), "EditTittleFragment");
        editTittleFragment.setTittleDialogListener(new EditTittleFragment.TittleDialogListener() {
            @Override
            public void applyText(String tittle, String hotelName) {
                txtTittle.setText(tittle);
                txtHotelName.setText(hotelName);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            addPhoto();
            return;
        }

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission_group.STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            addPhoto();
            return;
        }

        String[] permissions = new String[1];
        permissions[0] = Manifest.permission.READ_EXTERNAL_STORAGE;

        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_CODE_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length == 2
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                addPhoto();
            } else {
                Toast.makeText(getActivity(),
                        "Vui lòng cấp quyền để sử dụng",
                        Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }
    }

    private void addPhoto() {
        Toast.makeText(getActivity(), "Đã cấp quyền", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                closeFragment();
                break;

            case R.id.btnEdit:
                editTittle();
                break;

            case R.id.btnAddPhoto:
                checkPermissions();
                break;

            default:
                break;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo
            menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.menu_edit_delete_room, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnu_view:
                break;

            case R.id.mnu_delete:
                break;
        }
        return super.onContextItemSelected(item);
    }
}
