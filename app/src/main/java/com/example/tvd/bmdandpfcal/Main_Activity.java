package com.example.tvd.bmdandpfcal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.example.tvd.bmdandpfcal.Fragment.Bulk_Billing;
import com.example.tvd.bmdandpfcal.Fragment.Consumer_Billing;
import com.example.tvd.bmdandpfcal.values.FunctionCall;

import java.util.Objects;

import static com.example.tvd.bmdandpfcal.values.Constant.BILL_GENERATE;
import static com.example.tvd.bmdandpfcal.values.Constant.BULK_BILL;
import static com.example.tvd.bmdandpfcal.values.Constant.LAYOUT;

public class Main_Activity extends AppCompatActivity {
    Fragment fragment;
    FragmentManager fragmentManager;
    public static String layout = "";
    Database database;
    int maxcount;
    FunctionCall functionCall;

    public enum Steps1 {
        FORM0(Consumer_Billing.class),
        FORM1(Bulk_Billing.class);
        private Class clazz;

        Steps1(Class clazz) {
            this.clazz = clazz;
        }

        public Class getFragClass() {
            return clazz;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        initialize();

        Intent intent = getIntent();
        layout = Objects.requireNonNull(intent.getExtras()).getString(LAYOUT);

        if (!TextUtils.isEmpty(layout)) {
            switch (layout) {
                case BILL_GENERATE:
                    switchPopBackContent(Steps1.FORM0);
                    break;
                case BULK_BILL:
                    switchPopBackContent(Steps1.FORM1);
                    break;
            }
        }else finish();
    }

    public void initialize(){
        fragmentManager = getSupportFragmentManager();
        database = new Database(this);
        database.openDataBase();
        functionCall = new FunctionCall();

        maxcount = functionCall.convert_int(database.getTotal_Records());

    }


    public void switchPopBackContent(Steps1 currentForm) {
        try {
            fragment = (Fragment) currentForm.getFragClass().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.main_container, fragment, currentForm.name());
        ft.commit();
    }
    public void setActionBarTitle(String title) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
    }

}
