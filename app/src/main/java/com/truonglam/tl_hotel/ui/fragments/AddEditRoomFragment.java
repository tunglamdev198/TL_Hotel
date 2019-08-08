package com.truonglam.tl_hotel.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.truonglam.tl_hotel.R;
import com.truonglam.tl_hotel.common.Key;


public class AddEditRoomFragment extends AppCompatDialogFragment {
    private TextInputEditText edtRoomName;
    private TextView txtMessage;

    private String mode;

    private String roomName;

    private RoomDialogListener listener;


    public AddEditRoomFragment() {
    }

    public static AddEditRoomFragment newInstance(String mode, String roomName) {
        AddEditRoomFragment fragment = new AddEditRoomFragment();
        Bundle args = new Bundle();
        args.putString(Key.KEY_MODE,mode);
        args.putString(Key.KEY_ROOM_NAME,roomName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mode = getArguments().getString(Key.KEY_MODE);
        roomName = getArguments().getString(Key.KEY_ROOM_NAME);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_add_edit_room,null);
        edtRoomName = view.findViewById(R.id.edt_room_name);

        txtMessage = view.findViewById(R.id.txt_message);
        if(mode.equals(Key.MODE_EDIT_ROOM)){
            edtRoomName.setText(roomName);
            edtRoomName.setSelection(edtRoomName.getText().length());
            edtRoomName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.equals(roomName)){
                        txtMessage.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        builder.setView(view)
                .setTitle(mode)
                .setCancelable(false)
                .setNegativeButton(R.string.option_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.option_done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String roomName = edtRoomName.getText().toString().trim();
                        listener.applyText(roomName,mode);
                        dialog.dismiss();
                    }
                });
        return builder.create();

    }

    public void setListener(RoomDialogListener listener) {
        this.listener = listener;
    }

    public interface RoomDialogListener{
        void applyText(String roomName, String mode);
    }

}
