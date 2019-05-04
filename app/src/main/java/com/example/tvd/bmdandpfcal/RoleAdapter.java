package com.example.tvd.bmdandpfcal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class RoleAdapter extends BaseAdapter {
    private ArrayList<Mast_Cust> mylist;
    private Context context;
    private LayoutInflater inflater;

    public RoleAdapter(Context context, ArrayList<Mast_Cust> arraylist) {
        this.mylist = arraylist;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return mylist.size();
    }

    @Override
    public Object getItem(int pos) {
        return mylist.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int pos, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.spinner_items, null);
        TextView tvitem = view.findViewById(R.id.textView1);
        tvitem.setText(mylist.get(pos).getRole());
        return view;
    }
}
