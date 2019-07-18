package com.truonglam.tl_hotel.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.truonglam.tl_hotel.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddEditServiceFragment extends Fragment {


    public AddEditServiceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_edit, container, false);
    }

}
