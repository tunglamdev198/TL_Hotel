package com.truonglam.tl_hotel.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.truonglam.tl_hotel.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import petrov.kristiyan.colorpicker.ColorPicker;

public class EditServiceFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.edt_tittle)
    TextInputEditText edtTittle;

    @BindView(R.id.edt_link)
    TextInputEditText edtLink;

    @BindView(R.id.btnDone)
    Button btnDone;

    @BindView(R.id.btnCancel)
    Button btnCancel;

    @BindView(R.id.btnBack)
    ImageView btnBack;

    @BindView(R.id.btn_change_color)
    Button btnChangeColor;


    public EditServiceFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        registerListener();
    }

    private void initViews() {

    }

    private void registerListener() {
        btnDone.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnChangeColor.setOnClickListener(this);
    }

    private void selectColor() {
        final ColorPicker colorPicker = new ColorPicker(getActivity());
        final ArrayList<String> colors = new ArrayList<>();
        colors.add("#CCCCCC");
        colors.add("#666666");
        colors.add("#333333");
        colors.add("#000000");
        colors.add("#E74C3C");
        colors.add("#E67E22");
        colors.add("#F1C40F");
        colors.add("#2ECC71");
        colors.add("#1ABC9C");
        colors.add("#9B59B6");
        colors.add("#3498DB");
        colors.add("#ECF0F1");
        colors.add("#95A5A6");
        colors.add("#34495E");

        colorPicker
                .setColumns(5)
                .setColors(colors)
                .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position, int color) {
                        btnChangeColor.setBackgroundColor(color);
                        Log.d("AAA", "onChooseColor: "+colors.get(position));
                        colorPicker.dismissDialog();
                    }

                    @Override
                    public void onCancel() {
                        colorPicker.dismissDialog();
                    }
                })

                .show();
    }

    private void editFragment() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .remove(this)
                .commitAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDone:
                editFragment();
                break;

            case R.id.btnCancel:
                break;

            case R.id.btnBack:
                break;

            case R.id.btn_change_color:
                selectColor();
                break;

            default:
                break;
        }
    }
}
