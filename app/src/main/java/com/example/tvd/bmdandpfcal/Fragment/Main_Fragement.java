package com.example.tvd.bmdandpfcal.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tvd.bmdandpfcal.Database;
import com.example.tvd.bmdandpfcal.Navigation_Activity;
import com.example.tvd.bmdandpfcal.R;

import java.util.Objects;

import static com.example.tvd.bmdandpfcal.values.Constant.BILL_GENERATE;
import static com.example.tvd.bmdandpfcal.values.Constant.BULK_BILL;


public class Main_Fragement extends Fragment implements View.OnClickListener {
    CardView cv_lt_billing;
    TextView billing_records;
    Database database;
    public Main_Fragement() {
        // Required empty public constructor
    }

    View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_billing, container, false);
        billing_records = view.findViewById(R.id.billing_records);
        cv_lt_billing = view.findViewById(R.id.card_view_lt_billing);
        cv_lt_billing.setOnClickListener(this);
        database = ((Navigation_Activity) Objects.requireNonNull(getActivity())).getDatabasehelper();
        billing_records.setText(database.getTotal_Records());
        return view;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        switch (i) {
            case R.id.card_view_lt_billing:
                ((Navigation_Activity) Objects.requireNonNull(getActivity())).startIntent(getActivity(), BILL_GENERATE);
                break;
            case R.id.card_view_bulk_billing:
                ((Navigation_Activity) Objects.requireNonNull(getActivity())).startIntent(getActivity(), BULK_BILL);
                break;

        }
    }
    
}
