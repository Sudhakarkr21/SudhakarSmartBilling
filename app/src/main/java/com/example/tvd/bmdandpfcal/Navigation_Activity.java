package com.example.tvd.bmdandpfcal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.tvd.bmdandpfcal.Fragment.Main_Fragement;
import com.example.tvd.bmdandpfcal.Fragment.Report_Fragment;
import com.example.tvd.bmdandpfcal.values.FunctionCall;

import static com.example.tvd.bmdandpfcal.values.Constant.LAYOUT;

public class Navigation_Activity extends AppCompatActivity {
    Fragment fragment;
    private FragmentManager fragmentManager;
    private Toolbar toolbar;
    BottomNavigationView navigation;
    long backpressed;
    Database database;
    FunctionCall functionCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_activity);
        toolbar = findViewById(R.id.toolbar1);

        fragmentManager = getSupportFragmentManager();
        navigation = findViewById(R.id.navigation);
        functionCall = new FunctionCall();
        database = functionCall.check_billing_database(Navigation_Activity.this);
        switchContent(Steps.FORM0, "Billing");
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        toolbar.setTitle("Billing");

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.billing:
                    switchContent(Steps.FORM0, "Billing");
                    return true;
                case R.id.report:
                    switchContent(Steps.FORM1, "Reports");
                    return true;
                case R.id.setting:
                    toolbar.setTitle("Setting");
                    return true;
            }
            return false;
        }
    };

    public enum Steps {
        FORM0(Main_Fragement.class),
        FORM1(Report_Fragment.class);

        public Class clazz;

        Steps(Class clazz) {
            this.clazz = clazz;
        }

        public Class getFragClass() {
            return clazz;
        }
    }

    public void switchContent(Steps currentForm, String title) {
        try {
            fragment = (Fragment) currentForm.getFragClass().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        toolbar.setTitle(title);
        ft.replace(R.id.frame_container, fragment, currentForm.name());
        ft.commit();
    }

    public void switchAddonContent(Steps currentForm, String title) {
        try {
            fragment = (Fragment) currentForm.getFragClass().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentTransaction ft = fragmentManager.beginTransaction();
        toolbar.setTitle(title);
        ft.replace(R.id.frame_container, fragment, currentForm.name());
        ft.addToBackStack(currentForm.name());
        ft.commit();
    }

    public void startIntent(Context context, String layout) {
        Intent intent = new Intent(context, Main_Activity.class);
        intent.putExtra(LAYOUT, layout);
        startActivity(intent);
    }
    public Database getDatabasehelper() {
        return this.database;
    }

    @Override
    public void onBackPressed() {
        database.db_close();
        if (backpressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getApplicationContext(), "Press back again to Exit...", Toast.LENGTH_SHORT).show();
        }
        backpressed = System.currentTimeMillis();
    }


}
