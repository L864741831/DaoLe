package com.tck.daole.fragment;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tck.daole.R;

public class HongBaoFragment extends DialogFragment implements View.OnClickListener{

    //private Button release_type;
    AlertDialog.Builder builder;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //1、通过inflate，根据layout XML定义，创建view
        View v = inflater.inflate(R.layout.dialog_hongbao, container,false);
        //release_type = (Button)v.findViewById(R.id.release_type);
        //release_type.setOnClickListener(this);
        return v;
    }








    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           // case R.id.release_type:
            //    dismiss();
            //    break;

        }
    }
}




