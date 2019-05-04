package com.example.tvd.bmdandpfcal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{
    private ArrayList<String> myListDataArrayList1;
    private ArrayList<String> myListDataArrayList2;
    private Context context;

    // RecyclerView recyclerView;
    public MyListAdapter(ArrayList<String> myListDataArrayList1,ArrayList<String> myListDataArrayList2,Context context) {
        this.myListDataArrayList1 = myListDataArrayList1;
        this.myListDataArrayList2 = myListDataArrayList2;
        this.context = context;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.billstatext, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.textView1.setText(myListDataArrayList1.get(position));
        holder.textView3.setText(myListDataArrayList2.get(position));
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String billsts = myListDataArrayList1.get(position);
                int value = Integer.parseInt(myListDataArrayList2.get(position));
                if( value > 0){
                    if(!billsts.equals("TOTAL")) {
                        Intent next = new Intent(context, Billstatusext.class);
                        next.putExtra("status", billsts);
                        context.startActivity(next);
                    }else Toast.makeText(context,"Sorry Total Cannot be shown..  ",Toast.LENGTH_LONG).show();
                }else Toast.makeText(context,"No values are Present",Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return myListDataArrayList1.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView1,textView3;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);

            this.textView1 =  itemView.findViewById(R.id.textView1);
            this.textView3 = itemView.findViewById(R.id.textView3);
            this.relativeLayout = itemView.findViewById(R.id.relativelayout);
        }
    }
}
