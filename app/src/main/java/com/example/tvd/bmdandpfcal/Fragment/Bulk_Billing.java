package com.example.tvd.bmdandpfcal.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tvd.bmdandpfcal.R;

public class Bulk_Billing extends Fragment {
View view;
    public Bulk_Billing(){

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.bulkbilling, container, false);




        return view;
    }
}
