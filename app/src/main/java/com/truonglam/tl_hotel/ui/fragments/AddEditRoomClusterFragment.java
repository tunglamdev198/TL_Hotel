package com.truonglam.tl_hotel.ui.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.truonglam.tl_hotel.R;
import com.truonglam.tl_hotel.common.Key;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddEditRoomClusterFragment extends AppCompatDialogFragment {


    private String mode;

    private TextInputEditText edtClusterName;

    private ClusterRoomDialogListener listener;

    public AddEditRoomClusterFragment() {

    }

    public static AddEditRoomClusterFragment newInstance(String mode,String clusterName) {

        Bundle args = new Bundle();
        args.putString(Key.KEY_MODE,mode);
        args.putString(Key.KEY_ROOM_CLUSTER_NAME,clusterName);
        AddEditRoomClusterFragment fragment = new AddEditRoomClusterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mode = getArguments().getString(Key.KEY_MODE);
        String clusterName = getArguments().getString(Key.KEY_ROOM_CLUSTER_NAME);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_add_edit_room_cluster,null);
        edtClusterName = view.findViewById(R.id.edt_cruter_name);
        if(mode.equals(Key.MODE_EDIT_CLUSTER_ROOM)){
            edtClusterName.setText(clusterName);
            edtClusterName.setSelection(edtClusterName.getText().length());
        }

        builder.setView(view)
                .setTitle(mode)
                .setCancelable(false)
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Xong", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String clusterName = edtClusterName.getText().toString().trim();
                        listener.applyText(clusterName,mode);
                        dialog.dismiss();
                    }
                });
        return builder.create();

    }

    public void setListener(ClusterRoomDialogListener listener) {
        this.listener = listener;
    }

    public interface ClusterRoomDialogListener{
        void applyText(String clusterName, String mode);
    }



}
