package com.truonglam.tl_hotel.ui.fragments;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.daimajia.swipe.SwipeLayout;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;
import com.truonglam.tl_hotel.R;
import com.truonglam.tl_hotel.adapter.ImageListAdapter;
import com.truonglam.tl_hotel.adapter.SliderAdapter;
import com.truonglam.tl_hotel.common.Key;
import com.truonglam.tl_hotel.model.HotelBackground;
import com.truonglam.tl_hotel.model.HotelInformation;
import com.truonglam.tl_hotel.model.Image;
import com.truonglam.tl_hotel.ui.activities.HomePageActivity;
import com.truonglam.tl_hotel.utils.ProgressDialogUtil;
import com.truonglam.tl_hotel.utils.SavingUPSharePref;
import com.truonglam.tl_hotel.viewmodel.BackgroundViewModel;
import com.truonglam.tl_hotel.webservice.Client;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelInformationFragment extends Fragment implements View.OnClickListener {

    public static final int REQUEST_CODE_UPLOAD_PHOTO = 1000;
    public static final int REQUEST_CODE_CHANGE_AVATAR = 1005;
    private static final int REQUEST_CODE_PERMISSIONS = 1002;

    private String imagePath;

//    @BindView(R.id.sliderView)
//    ViewFlipper sliderView;

    @BindView(R.id.slide_background)
    ViewPager slideBackground;

    @BindView(R.id.swipe_menu)
    SwipeLayout swipeMenu;

    @BindView(R.id.btn_view)
    LinearLayout btnView;

    @BindView(R.id.btn_edit)
    LinearLayout btnEdit;

    @BindView(R.id.rv_list_image)
    RecyclerView rvListImage;

    @BindView(R.id.btn_add_photo)
    ImageView btnAddPhoto;

    @BindView(R.id.img_avatar)
    CircleImageView imgAvtar;

    @BindView(R.id.btn_edit_avatar)
    ImageView btnEditAvatar;

    @BindView(R.id.txt_hotel_name)
    TextView txtHotelName;

    @BindView(R.id.btn_exit)
    ImageView btnExit;

    @BindView(R.id.btn_logout)
    ImageView btnLogout;

    @BindView(R.id.btn_upload)
    Button btnUpload;

    private ImageListAdapter adapter;

    private SliderAdapter sliderAdapter;

    private HotelInformation hotelInformation;

    private HotelBackground hotelBg;

    private Uri uri;

    private BackgroundViewModel bgViewModel;

    private List<String> images;

    private Bitmap bitmap;

    private int index;


    public HotelInformationFragment() {
    }

    public static HotelInformationFragment newInstance(HotelInformation hotelInformation) {
        Bundle args = new Bundle();
        args.putSerializable(Key.KEY_HOTEL_INFORMATION, hotelInformation);
        HotelInformationFragment fragment = new HotelInformationFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hotel_infomation,
                container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initViews();
        registerListener();
    }

    private void initViews() {
        hotelInformation =
                (HotelInformation) getArguments().getSerializable(Key.KEY_HOTEL_INFORMATION);
        Log.d("AAAA", hotelInformation.toString());
        txtHotelName.setText(hotelInformation.getName());

        Picasso.with(getActivity())
                .load(Client.BASE_URL + '/' + hotelInformation.getLogo())
                .into(imgAvtar);

        Client.getService().getBackground(hotelInformation.getAccessToken(), hotelInformation.getHotelId())
                .enqueue(new Callback<HotelBackground>() {
                    @Override
                    public void onResponse(Call<HotelBackground> call, Response<HotelBackground> response) {
                        hotelBg = response.body();
                        images = response.body().getLinks();
                        if (images == null) {
                            return;
                        }
                        configRecyclerView(images);
                        sliderAdapter = new SliderAdapter(getActivity(), images);
                        slideBackground.setAdapter(sliderAdapter);
                    }

                    @Override
                    public void onFailure(Call<HotelBackground> call, Throwable t) {

                    }
                });

        swipeMenu.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipeMenu.addDrag(SwipeLayout.DragEdge.Right, swipeMenu.findViewById(R.id.swipe_rtl));
    }
    private void registerListener() {
        btnExit.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnAddPhoto.setOnClickListener(this);
        btnEditAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
                openGalley(v);
                swipeMenu.close();
            }
        });
        btnView.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        imgAvtar.setOnClickListener(this);
    }

//    private void slideImages(String url) {
//        ImageView imageView = new ImageView(getActivity());
//        Picasso.with(getActivity())
//                .load(url)
//                .into(imageView);
//
//        sliderView.addView(imageView);
//        sliderView.setFlipInterval(2000);
//        sliderView.setAutoStart(true);
//        sliderView.setOutAnimation(getActivity(), android.R.anim.slide_out_right);
//        sliderView.setInAnimation(getActivity(), android.R.anim.slide_in_left);
//    }

    private void configRecyclerView(final List<String> images) {
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
                        String token = hotelInformation.getAccessToken();
                        String url = images.get(position);
                        String backgroundId = hotelBg.getId();
                        Image image = new Image(backgroundId, url);
                        deleteImage(token, image);
                        break;
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");

            try {
                startActivityForResult(intent, REQUEST_CODE_UPLOAD_PHOTO);

            } catch (ActivityNotFoundException e) {

                e.printStackTrace();
            }
            return;
        }

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission_group.STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");

            try {
                startActivityForResult(intent, REQUEST_CODE_UPLOAD_PHOTO);

            } catch (ActivityNotFoundException e) {

                e.printStackTrace();
            }
            return;
        }

        String[] permissions = new String[2];
        permissions[0] = Manifest.permission.READ_EXTERNAL_STORAGE;
        permissions[1] = Manifest.permission.WRITE_EXTERNAL_STORAGE;

        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_CODE_PERMISSIONS);
    }


    private void deleteImage(final String token, final Image image) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.msg_delete_image)
                .setMessage(R.string.msg_delete_image_confirm)
                .setCancelable(false)
                .setPositiveButton(R.string.msg_yes, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ProgressDialogUtil.showDialog(getActivity(), "Đang xóa");
                        Client.getService().deleteBackground(token, image).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    updateListImage();
                                    ProgressDialogUtil.closeDialog();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.msg_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void uploadImage(Uri uri) {
        imagePath = getImagePathFromUri(uri);
        File file = new File(imagePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        RequestBody id = RequestBody.create(MultipartBody.FORM, hotelInformation.getHotelId());
//        String id = hotelInformation.getHotelId();
//        String image = parseImageToString();
        Client.getService().uploadPhoto(hotelInformation.getAccessToken(), id, body).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                ProgressDialogUtil.showDialog(getContext(), "Đang upload");
                if (response.isSuccessful()) {
                    updateListImage();
                    ProgressDialogUtil.closeDialog();
                    Toasty.success(getActivity(), "Upload thành công",
                            Toasty.LENGTH_SHORT, false).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                //  ProgressDialogUtil.closeDialog();
                Log.d("EEEE", "onFailure: " + t.getMessage());
                Toasty.error(getActivity(), t.getMessage(),
                        Toasty.LENGTH_SHORT, false).show();
            }
        });
    }

    private void changeAvatar(Uri uri) {
        imagePath = getImagePathFromUri(uri);
        File file = new File(imagePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        RequestBody id = RequestBody.create(MultipartBody.FORM, hotelInformation.getHotelId());
//        String id = hotelInformation.getHotelId();
//        String image = parseImageToString();
        Client.getService().changeAvatar(hotelInformation.getAccessToken(), id, body).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                ProgressDialogUtil.showDialog(getActivity(), "Đang thay đổi avatar");
                if (response.isSuccessful()) {
                    ProgressDialogUtil.closeDialog();
                    Toasty.success(getActivity(), "Thay đổi thành công",
                            Toasty.LENGTH_SHORT, false).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                ProgressDialogUtil.closeDialog();
                Toasty.error(getActivity(), t.getMessage(),
                        Toasty.LENGTH_SHORT, false).show();
            }
        });

    }

    private void openGalley(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        if (view.getId() == R.id.btn_add_photo) {
            try {
                startActivityForResult(intent, REQUEST_CODE_UPLOAD_PHOTO);

            } catch (ActivityNotFoundException e) {

                e.printStackTrace();
            }
        }
        if (view.getId() == R.id.btn_edit_avatar) {
            try {
                startActivityForResult(intent, REQUEST_CODE_CHANGE_AVATAR);

            } catch (ActivityNotFoundException e) {

                e.printStackTrace();
            }
        }

    }

    private void exitApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false)
                .setMessage(R.string.msg_exit_app)
                .setTitle(R.string.msg_exit)
                .setPositiveButton(R.string.msg_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.msg_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false)
                .setMessage(R.string.msg_logout_account)
                .setTitle(R.string.msg_logout)
                .setPositiveButton(R.string.msg_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doLogout();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.msg_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void doLogout() {
        ProgressDialogUtil.showDialog(getActivity(), "Đang đăng xuất");
        SavingUPSharePref.saveUsername(null, getActivity());
        SavingUPSharePref.savePassword(null, getActivity());

        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left)
                .replace(R.id.container, new LoginFragment())
                .commit();
        ProgressDialogUtil.closeDialog();
        HomePageActivity.getInstance().bottomNav.setVisibility(View.GONE);
    }


    private void updateListImage() {
        Client.getService().getBackground(hotelInformation.getAccessToken(),hotelInformation.getHotelId())
                .enqueue(new Callback<HotelBackground>() {
                    @Override
                    public void onResponse(Call<HotelBackground> call, Response<HotelBackground> response) {
                        adapter.updateData(response.body().getLinks());
                        sliderAdapter.updateData(response.body().getLinks());
                    }

                    @Override
                    public void onFailure(Call<HotelBackground> call, Throwable t) {

                    }
                });

    }

    private String getImagePathFromUri(Uri uri) {
        String projections[] = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getActivity(), uri, projections, null, null, null);
        Cursor cursor = loader.loadInBackground();
        cursor.moveToFirst();
        int index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
        String result = cursor.getString(index);
        cursor.close();
        return result;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // openGalley();
            } else {
                Toast.makeText(getActivity(),
                        R.string.msg_request_permission,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_UPLOAD_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    uri = data.getData();
                    uploadImage(uri);
                }
                break;

            case REQUEST_CODE_CHANGE_AVATAR:
                if (resultCode == Activity.RESULT_OK) {
                    uri = data.getData();
                    changeAvatar(uri);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_logout:
                logout();
                break;

            case R.id.btn_exit:
                exitApp();
                break;

            case R.id.btn_upload:

                break;

            case R.id.btn_add_photo:
//                checkPermissions();
                openGalley(btnAddPhoto);
                break;


            case R.id.img_avatar:
                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse(Client.BASE_URL + '/' + hotelInformation.getLogo()));
                startActivity(intent1);
                break;

            case R.id.btn_view:
                swipeMenu.close();

                break;

            case R.id.btn_edit:
                swipeMenu.close();
                break;

            default:
                break;
        }
    }
}
