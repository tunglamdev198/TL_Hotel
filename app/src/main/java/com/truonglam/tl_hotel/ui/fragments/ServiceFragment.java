package com.truonglam.tl_hotel.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.truonglam.tl_hotel.R;
import com.truonglam.tl_hotel.common.Key;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceFragment extends Fragment {


    public ServiceFragment() {
    }

    public static ServiceFragment newInstance() {

        Bundle args = new Bundle();
        ServiceFragment fragment = new ServiceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_service, container, false);
    }

}
