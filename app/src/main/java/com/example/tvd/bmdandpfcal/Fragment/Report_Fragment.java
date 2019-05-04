package com.example.tvd.bmdandpfcal.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tvd.bmdandpfcal.Database;
import com.example.tvd.bmdandpfcal.R;

public class Report_Fragment extends Fragment implements View.OnClickListener{
    View view;
    Database database;

    public Report_Fragment(){

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.report_fragment, container, false);




        return view;
    }

    @Override
    public void onClick(View v) {

    }
}
