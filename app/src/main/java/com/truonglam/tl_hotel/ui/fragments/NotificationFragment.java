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
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.truonglam.tl_hotel.R;
import com.truonglam.tl_hotel.adapter.ImageListAdapter;
import com.truonglam.tl_hotel.common.Key;
import com.truonglam.tl_hotel.model.HotelInformation;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_CODE_EDIT_TITTLE = 1;
    private static final int REQUEST_CODE_PERMISSIONS = 10;

    private static final String TITLE = "title";
    private String tittle;

    @BindView(R.id.tbNotification)
    Toolbar tbNotification;

    @BindView(R.id.btnBack)
    ImageView btnBack;

    @BindView(R.id.btnEdit)
    ImageView btnEdit;

    @BindView(R.id.btnDelete)
    ImageView btnDelete;

    @BindView(R.id.btnAddPhoto)
    ImageView btnAddPhoto;

    @BindView(R.id.txtTittle)
    TextView txtTittle;

    @BindView(R.id.rvListImage)
    RecyclerView rvListImage;

    private ImageListAdapter adapter;
    private List<Integer> images;

    private int index;

    public static final String TAG = "NotificationFragment";

    public NotificationFragment() {
        images = new ArrayList<>();
        images.add(R.drawable.hotel1);
        images.add(R.drawable.hotel2);
        images.add(R.drawable.hotel3);
        images.add(R.drawable.img_demo);

    }

    public static NotificationFragment newInstance(String title, HotelInformation hotelInformation) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putString(Key.KEY_TITTLE, title);
        args.putSerializable(Key.KEY_HOTEL_INFORMATION,hotelInformation);
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
        if (getArguments() != null) {
            tittle = getArguments().getString(Key.KEY_TITTLE);
            txtTittle.setText(tittle);
        }
        configRecyclerView();
        registerListener();
    }

    private void registerListener() {
        btnBack.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnAddPhoto.setOnClickListener(this);
    }

    private void closeFragment() {
        HotelInformation hotelInformation = (HotelInformation) getArguments().getSerializable(Key.KEY_HOTEL_INFORMATION);
        HotelInformationFragment fragment = HotelInformationFragment.newInstance(hotelInformation,"anh");
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,fragment)
                .commit();
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
        String tittle = txtTittle.getText().toString().trim();
        EditTittleFragment editTittleFragment = EditTittleFragment.newInstance(tittle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, editTittleFragment)
                .addToBackStack(null)
                .commit();
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


    private void deleteTittle() {
        txtTittle.setText("");
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

            case R.id.btnDelete:
                deleteTittle();
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
        getActivity().getMenuInflater().inflate(R.menu.menu_edit_delete, menu);
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
