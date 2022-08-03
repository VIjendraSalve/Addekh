package com.wht.addekho.Activties;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.wht.addekho.R;

public class BottomDialogFragment extends BottomSheetDialogFragment {
    public static Context context;
    public static final String TAG = "EmailOrdersDialogFrag";
    private Button okBt;
    private ImageButton imageButton_close;


    public static BottomDialogFragment newInstance(Context context) {
        //this.context = context;
        return new BottomDialogFragment();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        imageButton_close = view.findViewById(R.id.btnCancel);

        imageButton_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

}