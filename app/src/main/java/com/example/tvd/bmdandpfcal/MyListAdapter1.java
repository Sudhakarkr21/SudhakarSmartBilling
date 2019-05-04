package com.example.tvd.bmdandpfcal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class MyListAdapter1 extends RecyclerView.Adapter<MyListAdapter1.ViewHolder> {
   private ArrayList<String> list1, list2, list3, list4, list5, list6, list7, list8;
    private Context context;

    // RecyclerView recyclerView;
    public MyListAdapter1(ArrayList<String> list1,ArrayList<String> list2,ArrayList<String> list3,
                          ArrayList<String> list4,ArrayList<String> list5,ArrayList<String> list6
            ,ArrayList<String> list7,ArrayList<String> list8,Context context) {
        this.list1 = list1;
        this.list2 = list2;
        this.list3 = list3;
        this.list4 = list4;
        this.list5 = list5;
        this.list6 = list6;
        this.list7 = list7;
        this.list8 = list8;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.billstat2, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.name.setText(list2.get(position));
        holder.accountID.setText(list3.get(position));
        holder.rrno.setText(list4.get(position));
        holder.units.setText(list5.get(position));
        holder.arrears.setText(list7.get(position));
        holder.payable.setText(list8.get(position));
        holder.textView.setText(list1.get(position));

    }

    @Override
    public int getItemCount() {
        return list1.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView accountID, name , rrno,units,arrears,payable,textView;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.textView1);
            this.accountID = itemView.findViewById(R.id.textView5);
            this.name = itemView.findViewById(R.id.textView3);
            this.rrno = itemView.findViewById(R.id.textView7);
            this.units = itemView.findViewById(R.id.textView9);
            this.arrears = itemView.findViewById(R.id.txt_arrears_val);
            this.payable = itemView.findViewById(R.id.textView11);

            this.relativeLayout = itemView.findViewById(R.id.relativelayout);

        }
    }
}
