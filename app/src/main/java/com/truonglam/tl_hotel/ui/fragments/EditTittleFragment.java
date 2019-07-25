package com.truonglam.tl_hotel.ui.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.truonglam.tl_hotel.R;
import com.truonglam.tl_hotel.common.Key;
import com.truonglam.tl_hotel.model.HotelInformation;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditTittleFragment extends AppCompatDialogFragment {

    private TextInputEditText edtTittle;
    private TextInputEditText edtHotelName;

    private String mode;

    private TittleDialogListener tittleDialogListener;

    private HotelInformation hotelInfo;


    public EditTittleFragment() {
    }

    public static EditTittleFragment newInstance(HotelInformation hotelInfo, String mode) {

        Bundle args = new Bundle();
        args.putSerializable(Key.KEY_HOTEL_INFORMATION, hotelInfo);
        args.putString(Key.KEY_MODE,mode);
        EditTittleFragment fragment = new EditTittleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mode = getArguments().getString(Key.KEY_MODE);
        hotelInfo =(HotelInformation) getArguments().getSerializable(Key.KEY_HOTEL_INFORMATION);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_edit_tittle,null);
        edtTittle = view.findViewById(R.id.edt_tittle);
        edtHotelName = view.findViewById(R.id.edt_hotel_name);

        if(mode.equals(Key.MODE_EDIT_ROOM)){
            edtTittle.setText(hotelInfo.getTittle());
            edtTittle.setSelection(hotelInfo.getTittle().length());

            edtHotelName.setText(hotelInfo.getName());
            edtHotelName.setSelection(hotelInfo.getName().length());
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
                        String tittle = edtTittle.getText().toString().trim();
                        String hotelName = edtHotelName.getText().toString().trim();

                        if(tittle.equals(hotelInfo.getTittle())
                                &&hotelName.equals(hotelInfo.getName())){
                            Toasty.warning(getActivity(),R.string.msg_hotel_info_validate,
                                    Toast.LENGTH_SHORT,true).show();
                            return;
                        }

                        tittleDialogListener.applyText(tittle,hotelName);
                        dialog.dismiss();
                    }
                });
        return builder.create();

    }

    public void setTittleDialogListener(TittleDialogListener tittleDialogListener) {
        this.tittleDialogListener = tittleDialogListener;
    }

    public interface TittleDialogListener{
        void applyText(String tittle, String hotelInfo);
    }
}
