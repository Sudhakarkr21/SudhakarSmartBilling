package com.example.tvd.bmdandpfcal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Settings extends AppCompatActivity {
    public static final int CUST_DLG1 = 1;
    Button billReports;
    Database database;
    Cursor c = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        billReports = findViewById(R.id.billreports);
        database = new Database(this);
        database.openDataBase();

        billReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                c = database.billed();
                if(c.getCount() > 0){   
                    Intent next = new Intent(Settings.this, BillStatus.class);
                    startActivity(next);
                }else showdialog(CUST_DLG1);

            }
        });


        if(isConnectingToInternet()){
            Toast.makeText(getApplicationContext(),"Internet is Connected",Toast.LENGTH_LONG).show();
        }else Toast.makeText(getApplicationContext(),"No Internet",Toast.LENGTH_LONG).show();

    }

   public void showdialog(int id){
       Dialog dialog;
       switch (id){
           case CUST_DLG1:
               AlertDialog.Builder b = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
               b.setTitle("Bill Collection");
               b.setMessage("No Bill Reports Details Because No Billed Values");
               b.setNeutralButton("OK",null);
               dialog = b.create();
               dialog.show();
               break;

       }
    }


    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }
}
