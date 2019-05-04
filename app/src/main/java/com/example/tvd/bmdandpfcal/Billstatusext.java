package com.example.tvd.bmdandpfcal;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Billstatusext extends Activity {
    TextView tvsts;
    String stsname;
    Database database;
    Cursor c = null;
    ArrayList<String> list1, list2, list3, list4, list5, list6, list7, list8;
    int slno;
    String rrno,custid,name;
    Double units1,arrears,payable1;
    DecimalFormat num;
    RecyclerView recyclerView;
    MyListAdapter1 myListAdapter1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.billstat);
        tvsts = findViewById(R.id.textView1);
        database = new Database(this);
        database.openDataBase();
        recyclerView =  findViewById(R.id.recyclerView);
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        list3 = new ArrayList<>();
        list4 = new ArrayList<>();
        list5 = new ArrayList<>();
        list6 = new ArrayList<>();
        list7 = new ArrayList<>();
        list8 = new ArrayList<>();
        num = new DecimalFormat("##0.00");
        Intent in = getIntent();
        if (in.getExtras() != null) {
            Bundle sts = in.getExtras();
                stsname = sts.getString("status");
        }
        tvsts.setText(stsname);

        if(stsname.equals("BILLED")){
            c = database.billed();
            view_records(c);
        }
        if (stsname != null && !stsname.equals("BILLED") && !stsname.equals("NOT BILLED") && !stsname.equals("TOTAL")) {
            c = database.status(stsname);
            view_records(c);
        }

        if(stsname.equals("NOT BILLED")){
            c = database.notbilled();
            view_records(c);
        }

        myListAdapter1 = new MyListAdapter1(list1,list2,list3,list4,list5,list6,list7,list8,this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myListAdapter1);
    }
    public void view_records(Cursor data){
        if(data.getCount()>0){
            while (data.moveToNext()) {
                slno++;
                rrno = data.getString(data.getColumnIndex("RRNO"));
                custid = data.getString(data.getColumnIndex("CONSNO"));
                name = data.getString(data.getColumnIndex("NAME"));
                arrears = Double.parseDouble(data.getString(data.getColumnIndex("ARREARS")));
                list1.add("" + slno);
                list2.add(name);
                list3.add(custid);
                list4.add(rrno);
                if(!stsname.equals("NOT BILLED")) {
                    list8.add(num.format(fcall.convert_decimal(fcall.getCursorValue(data, "PAYABLE"))));
                    units1 = Double.parseDouble(data.getString(data.getColumnIndex("UNITS")));
                    payable1 = Double.parseDouble(data.getString(data.getColumnIndex("DEM_REVENUE")));
                    list5.add("" + units1);
                    list6.add("" + payable1);
                }else{
                    list8.add("---");
                    list5.add("---");
                    list6.add("---");
                }

                list7.add(""+ arrears);
            }

        }
    }
}
