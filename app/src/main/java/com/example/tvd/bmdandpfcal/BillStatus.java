package com.example.tvd.bmdandpfcal;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;

public class BillStatus extends Activity {
    TextView tvrdgdt;
    Database database;
    Cursor c1=null,c=null;
    String reading,s1,s2;
    ArrayList<String> list1;
    ArrayList<String> list2;
    RecyclerView recyclerView;
    MyListAdapter myListAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.billstatus);
        tvrdgdt = findViewById(R.id.textView2);
        recyclerView =  findViewById(R.id.recyclerView);
        database = new Database(this);
        database.openDataBase();
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        c1 = database.collects1();
        if (c1 != null) {
            c1.moveToNext();
            reading = c1.getString(c1.getColumnIndex("COL1"));
        }
        tvrdgdt.setText(reading);

        c = database.billstatus();
        if (c != null) {
            while (c.moveToNext()) {
                s1 = c.getString(c.getColumnIndex("COL1"));
                s2 = c.getString(c.getColumnIndex("COL2"));
                list1.add(s1);
                list2.add(s2);
            }
        }
        myListAdapter = new MyListAdapter(list1,list2,this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myListAdapter);
    }
}
