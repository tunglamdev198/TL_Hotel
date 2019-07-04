package com.truonglam.tl_hotel.ui.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.truonglam.tl_hotel.R;
import com.truonglam.tl_hotel.common.Key;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditTittleFragment extends DialogFragment   implements View.OnClickListener {

    @BindView(R.id.btnDone)
    Button btnDone;

    @BindView(R.id.btnCancel)
    Button btnCancel;

    @BindView(R.id.edtTittle)
    TextInputEditText edtTittle;


    public EditTittleFragment() {
    }

    public static EditTittleFragment newInstance(String tittle) {

        Bundle args = new Bundle();
        args.putString(Key.KEY_TITTLE, tittle);
        EditTittleFragment fragment = new EditTittleFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_tittle, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        edtTittle.setText(getArguments().getString(Key.KEY_TITTLE));
        registerListener();
    }

    private void registerListener() {
        btnDone.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void editTittle() {
        String tittle = edtTittle.getText().toString().trim();
        if (tittle.isEmpty()) {
            Toasty.error(getActivity(), "Tiêu đề không được để trống!",
                    Toast.LENGTH_SHORT, true)
                    .show();

            return;
        }
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(this);
        trans.commit();
        manager.popBackStack();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDone:
                editTittle();
                break;

            case R.id.btnCancel:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }
    }
}
