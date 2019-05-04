package com.example.tvd.bmdandpfcal.Fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tvd.bmdandpfcal.Calculation.GetSet_MastValues;
import com.example.tvd.bmdandpfcal.Database;
import com.example.tvd.bmdandpfcal.GetSetValues;
import com.example.tvd.bmdandpfcal.Main_Activity;
import com.example.tvd.bmdandpfcal.Mast_Cust;
import com.example.tvd.bmdandpfcal.R;
import com.example.tvd.bmdandpfcal.RoleAdapter;
import com.example.tvd.bmdandpfcal.Subdiv_Details;
import com.example.tvd.bmdandpfcal.Tariff_config;
import com.example.tvd.bmdandpfcal.fcall;
import com.example.tvd.bmdandpfcal.values.FunctionCall;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.Manifest.permission.CAMERA;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.example.tvd.bmdandpfcal.values.Constant.DIR_IMAGE_STORE;
import static com.example.tvd.bmdandpfcal.values.Constant.FILE_IMAGE_FORMAT;
import static com.example.tvd.bmdandpfcal.values.Constant.IMAGE_FILE_URI;
import static com.example.tvd.bmdandpfcal.values.Constant.ZERO;

public class Consumer_Billing extends Fragment {
    Toolbar toolbar;
    AutoCompleteTextView autoCompleteTextView;
    Mast_Cust mast_custs;
    List<Mast_Cust> mast_cust;
    List<Subdiv_Details> subdivDetails;
    ArrayList<Mast_Cust> status_list;
    Tariff_config tariff_configs;
    List<Tariff_config> tariff_config;
    ArrayList<String> customer;
    GetSet_MastValues getSet_mastValues;
    GetSetValues setValues;
    Cursor c;
    Database database;
    Spinner sp_curr_status;
    RoleAdapter status_adapter;
    FunctionCall functionsCall;
    String bill_billed_status;
    int billing_movement = 0, cons_pfflag = 0, cons_todflag = 0, cons_Present_STS = 0;
    double cons_mf = 0, cons_prev_read = 0, cons_tod_prev1 = 0, cons_tod_prev3 = 0, cons_dlcount = 0, cons_deposit = 0, cons_int_amt = 0,
            cons_arrears = 0, cons_curr_pf_val = 0, cons_curr_bmd_val = 0;
    boolean id_billed = false, imagetaken = false;
    TextView tv_accountid, tv_rrno, tv_name, tv_address, tv_tariff, tv_prev_status, tv_prev_reading, tv_mf, tv_txt_billed_date,
            tv_billing_date, tv_billing_status, tv_billing_count, tv_load_hp, tv_load_kw, tv_tod_prev_onpeak, tv_tod_prev_normalpeak,
            tv_tod_prev_offpeak;
    TextInputLayout til_curr_reading, til_mtr_digit, til_pf, til_bmd, til_kvah, til_weeks, til_pdrecorded, til_onpeak, til_offpeak,
            til_search_account_id, til_search_rrno, til_normalpeak;
    EditText et_curr_reading, et_mtr_digit, et_pf, et_bmd, et_kvah, et_days, et_pdrecorded, et_onpeak, et_offpeak, et_normalpeak;
    LinearLayout text_input_layouts, search_layout, full_billing_layout, tod_reading_layout, feder_dtc_layout, prev_read_layout;
    ImageView bill_capture;
    private static final int RequestPermissionCode = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int MEDIA_TYPE_IMAGE = 2;
    private static final int SUB_NORMAL_DLG = 3;
    private static final int AB_NORMAL_DLG = 4;
    // directory name to store captured images
    private static final String IMAGE_DIRECTORY_NAME = "MyCamera";
    private static String account_id_image = "", billing_MRCode = "", pathname = "", path_extension = "", filename = "";
    String cons_ImgAdd = "", cons_imageextension = "";
    private static Uri fileUri;
    static File mediaFile;
    BottomNavigationView navigationView;
    View view;

    public Consumer_Billing() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.consumer_billing, container, false);
        navigationView = view.findViewById(R.id.billing_navigation1);
        database = new Database(getContext());

        navigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initializtion();
        check_editTexts();
        c = database.getTarrifDataCus();
        if (c.moveToFirst()) {
            do {
                customer.add(c.getString(23));
            } while (c.moveToNext());
        }
        c.close();

        sp_curr_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cons_Present_STS = position + 1;
                selected_Spinner_Item(cons_Present_STS);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });
        return view;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int i = item.getItemId();
            if (i == R.id.navigation_backward) {
                billing_movement = 1;
//                backward();
            } else if (i == R.id.navigation_next) {
                billing_movement = 0;
//                upward();
            } else if (i == R.id.navigation_preview) {
                validate_image();
            }
            return true;
        }
    };

    private void validate_image() {
        if (cons_Present_STS == 3)
            imagetaken = true;
        if (imagetaken)
            onPreview();
        else
            functionsCall.showtoast(getActivity(), getResources().getString(R.string.validate_image));
    }


    private void onPreview() {
//        -------------Check Current Reading value-------------
        if (TextUtils.isEmpty(et_curr_reading.getText())) {
            setEditTextError(et_curr_reading, "Enter Current Reading");
            return;
        }

        if (functionsCall.convert_decimal(mast_cust.get(0).getPRESSTS()) != 5) {
            if (functionsCall.convert_decimal(et_curr_reading.getText().toString()) >=
                    functionsCall.convert_decimal(mast_cust.get(0).getPRVRED())) {
                mast_cust.get(0).setPRES_RDG(et_curr_reading.getText().toString());
            } else {
                setEditTextError(et_curr_reading, getResources().getString(R.string.et_enter_current_reading));
                return;
            }
        } else {
            if (functionsCall.convert_decimal(et_curr_reading.getTransitionName()) <
                    functionsCall.convert_decimal(mast_cust.get(0).getPRVRED())) {
                mast_cust.get(0).setPRES_RDG(et_curr_reading.getText().toString());
            } else {
                setEditTextError(et_curr_reading, getResources().getString(R.string.et_enter_current_reading));
                return;
            }
        }

        if (functionsCall.convert_decimal(mast_cust.get(0).getTARIFF()) != 70 ||
                functionsCall.convert_decimal(mast_cust.get(0).getTARIFF()) != 71) {
            /*--------------- Checking Meter Digit value ---------------*/
            if (TextUtils.isEmpty(et_mtr_digit.getText())) {
                setEditTextError(et_mtr_digit, getResources().getString(R.string.et_enter_meter_digit));
                return;
            }
            /*--------------- Validating Meter Digit ---------------*/
            if (mast_cust.get(0).getPRES_RDG().length() <= functionsCall.convert_int(et_mtr_digit.getText().toString()))
                mast_cust.get(0).setMTRDIGIT(et_mtr_digit.getText().toString());
            else {
                setEditTextError(et_curr_reading, getResources().getString(R.string.et_error_meter_digit));
                return;
            }
        } else mast_cust.get(0).setMTRDIGIT(mast_cust.get(0).getMTRDIGIT());

        if (til_pf.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(et_pf.getText())) {
                setEditTextError(et_pf, getResources().getString(R.string.et_enter_pf));
                return;
            } else pf_reading();
        }

        if (tod_reading_layout.getVisibility() == View.VISIBLE)
            tod_reading();
        else {
            mast_cust.get(0).setTOD_CURRENT1(ZERO);
            mast_cust.get(0).setTOD_CURRENT2(ZERO);
            mast_cust.get(0).setTOD_CURRENT3(ZERO);
            mast_cust.get(0).setTOD_CURRENT4(ZERO);
        }

        if (!TextUtils.isEmpty(cons_imageextension))
            mast_cust.get(0).setIMGADD(cons_imageextension);
        else mast_cust.get(0).setIMGADD(getResources().getString(R.string.no_image_captured));

        sub_ab_normal_readings();
    }

    public void sub_ab_normal_readings() {
        double avg_cons = functionsCall.convert_decimal(mast_cust.get(0).getAVGCON());
        if (functionsCall.convert_decimal(mast_cust.get(0).getPRESSTS()) != 5) {
            double units = functionsCall.convert_decimal(mast_cust.get(0).getPRES_RDG()) -
                    functionsCall.convert_decimal(mast_cust.get(0).getPRVRED());
            if (units > (avg_cons * 2.5)) {
                showdailog(AB_NORMAL_DLG);
            } else if (units < (avg_cons * 0.5))
                showdailog(SUB_NORMAL_DLG);
        }
    }

    private void tod_reading() {
        /*--------------- Checking TOD ON PEAK value ---------------*/
        if (TextUtils.isEmpty(et_onpeak.getText())) {
            setEditTextError(et_onpeak, getResources().getString(R.string.et_enter_on_peak));
            return;
        }
        /*--------------- Validating TOD ON PEAK value with previous ---------------*/
        if (functionsCall.convert_decimal(et_onpeak.getText().toString()) <
                functionsCall.convert_decimal(mast_cust.get(0).getTOD_PREVIOUS1())) {
            setEditTextError(et_onpeak, getResources().getString(R.string.et_error_on_peak));
            return;
        }
        /*--------------- Setting TOD ON PEAK value to object ---------------*/
        mast_cust.get(0).setTOD_CURRENT1(et_onpeak.getText().toString());
        /*--------------- Checking TOD NORMAL PEAK value ---------------*/
        if (TextUtils.isEmpty(et_normalpeak.getText())) {
            setEditTextError(et_normalpeak, getResources().getString(R.string.et_enter_normal_peak));
            return;
        }
        /*--------------- Validating TOD NORMAL PEAK value with previous ---------------*/
        if (functionsCall.convert_decimal(et_normalpeak.getText().toString()) <
                functionsCall.convert_decimal(mast_cust.get(0).getTOD_PREVIOUS2())) {
            setEditTextError(et_normalpeak, getResources().getString(R.string.et_error_normal_peak));
            return;
        }
        /*--------------- Setting TOD NORMAL PEAK value to object ---------------*/
        mast_cust.get(0).setTOD_CURRENT2(et_normalpeak.getText().toString());
        /*--------------- Setting TOD ON PEAK EXTRA value to object ---------------*/
        mast_cust.get(0).setTOD_CURRENT3(ZERO);
        /*--------------- Checking TOD OFF PEAK value ---------------*/
        if (TextUtils.isEmpty(et_offpeak.getText())) {
            setEditTextError(et_offpeak, getResources().getString(R.string.et_enter_off_peak));
            return;
        }
        /*--------------- Validating TOD OFF PEAK value with previous ---------------*/
        if (functionsCall.convert_decimal(et_offpeak.getText().toString()) <
                functionsCall.convert_decimal(mast_cust.get(0).getTOD_PREVIOUS4())) {
            setEditTextError(et_offpeak, getResources().getString(R.string.et_error_off_peak));
            return;
        }
        /*--------------- Setting TOD OFF PEAK value to object ---------------*/
        mast_cust.get(0).setTOD_CURRENT4(et_offpeak.getText().toString());
    }

    private void pf_reading() {
        cons_curr_pf_val = functionsCall.convert_decimal(functionsCall.decimalroundoff(et_pf.getText().toString()));
        if (cons_Present_STS == 1 || cons_Present_STS == 2 || cons_Present_STS == 7 || cons_Present_STS == 15) {
            if (cons_curr_pf_val < 0.70) {
                if (cons_curr_pf_val == 0)
                    bmd_reading();
                else {
                    cons_curr_pf_val = 0.70;
                    bmd_reading();
                }
            } else bmd_reading();
        } else {
            if (cons_curr_pf_val >= 0 && cons_curr_pf_val < 1) {
                if (cons_curr_pf_val < 0.70) {
                    if (cons_curr_pf_val == 0)
                        bmd_reading();
                    else {
                        cons_curr_pf_val = 0.70;
                        bmd_reading();
                    }
                } else bmd_reading();
            } else {
                setEditTextError(et_pf, getResources().getString(R.string.et_error_pf));
                return;
            }
        }
        mast_cust.get(0).setPFVAL(et_pf.getText().toString());
    }

    private void bmd_reading() {
        /*--------------- Checking BMD value ---------------*/
        if (TextUtils.isEmpty(et_bmd.getText())) {
            setEditTextError(et_bmd, getResources().getString(R.string.et_enter_bmd));
            return;
        }
        cons_curr_bmd_val = functionsCall.convert_decimal(et_bmd.getText().toString());
        /*--------------- Validating BMD value ---------------*/
        if (cons_curr_bmd_val <= 100) {
            /*--------------- Checking KVAH value ---------------*/
            if (TextUtils.isEmpty(et_kvah.getText())) {
                setEditTextError(et_kvah, getResources().getString(R.string.et_enter_kvah));
                return;
            } else mast_cust.get(0).setPREADKVAH(et_kvah.getText().toString());
        } else {
            setEditTextError(et_bmd, getResources().getString(R.string.et_error_bmd));
            return;
        }
        mast_cust.get(0).setBMDVAL(et_bmd.getText().toString());
    }


    private void setEditTextError(EditText editText, String value) {
        editText.setError(value);
        editText.setSelection(editText.getText().length());
        editText.requestFocus();
    }

    public void initializtion() {
        toolbar = view.findViewById(R.id.main_toolbar);
//        toolbar.setTitle("Consumer Billing");
//        toolbar.inflateMenu(R.menu.clear);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
            Objects.requireNonNull(activity.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        ((Main_Activity) Objects.requireNonNull(getActivity())).setActionBarTitle(getResources().getString(R.string.billing));

        try {
            if (database.checkDataBase()) {
                Toast.makeText(getActivity(), "Data Base Already Exists", Toast.LENGTH_LONG).show();
                Log.e("debug", "Data Base Already Exists");
            } else {
                database.CopyDataBaseFromAsset();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            database.openDataBase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getSet_mastValues = new GetSet_MastValues(database, setValues);
        setValues = new GetSetValues();
        functionsCall = new FunctionCall();
        mast_cust = new ArrayList<>();
        subdivDetails = new ArrayList<>();
        mast_custs = new Mast_Cust();
        tariff_config = new ArrayList<>();
        tariff_configs = new Tariff_config();
        customer = new ArrayList<String>();
        autoCompleteTextView = view.findViewById(R.id.actv);
        autoCompleteTextView.setVisibility(View.GONE);
        status_list = new ArrayList<>();

        bill_capture = view.findViewById(R.id.im_current_read_image);


//-----------LinearLayout----------------------------
        tod_reading_layout = view.findViewById(R.id.tod_reading_layout);

//        ------------TextView-----------------------
        tv_accountid = view.findViewById(R.id.billing_account_id);
        tv_rrno = view.findViewById(R.id.billing_rrno);
        tv_name = view.findViewById(R.id.billing_name);
        tv_address = view.findViewById(R.id.billing_address);
        tv_tariff = view.findViewById(R.id.billing_tariff);
        tv_prev_status = view.findViewById(R.id.billing_prev_sts);
        tv_prev_reading = view.findViewById(R.id.billing_prev_reading);
        tv_mf = view.findViewById(R.id.billing_mf);
        tv_load_hp = view.findViewById(R.id.billing_load_hp);
        tv_load_kw = view.findViewById(R.id.billing_load_kw);
        tv_billing_status = view.findViewById(R.id.billing_status);
        tv_billing_date = view.findViewById(R.id.billing_curr_date);
        tv_tod_prev_onpeak = view.findViewById(R.id.tod_previous_on_peak);
        tv_tod_prev_normalpeak = view.findViewById(R.id.tod_previous_normal_peak);
        tv_tod_prev_offpeak = view.findViewById(R.id.tod_previous_off_peak);

//        ---------------TextLayout------------------
        til_curr_reading = view.findViewById(R.id.til_billing_current_reading);
        til_mtr_digit = view.findViewById(R.id.til_billing_meter_digit);
        til_pf = view.findViewById(R.id.til_billing_pf);
        til_bmd = view.findViewById(R.id.til_billing_bmd);
        til_kvah = view.findViewById(R.id.til_billing_kvah);
        til_onpeak = view.findViewById(R.id.til_billing_tod_on_peak);
        til_offpeak = view.findViewById(R.id.til_billing_tod_off_peak);
        til_normalpeak = view.findViewById(R.id.til_billing_tod_normal_peak);


        et_curr_reading = view.findViewById(R.id.et_billing_current_reading);
        et_mtr_digit = view.findViewById(R.id.et_billing_meter_digit);
        et_pf = view.findViewById(R.id.et_billing_pf);
        et_bmd = view.findViewById(R.id.et_billing_bmd);
        et_kvah = view.findViewById(R.id.et_billing_kvah);
        et_onpeak = view.findViewById(R.id.et_billing_tod_on_peak);
        et_offpeak = view.findViewById(R.id.et_billing_tod_off_peak);
        et_normalpeak = view.findViewById(R.id.et_billing_tod_normal_peak);


        sp_curr_status = view.findViewById(R.id.sp_current_status);
        status_adapter = new RoleAdapter(getActivity(), status_list);
        sp_curr_status.setAdapter(status_adapter);
        mast_cust = fcall.getTariffConfig1(database.getMastCust_details());
        subdivDetails = fcall.getSubdiv(database.getSubdiv_details());
        getbillData();

    }

    public void getbillData() {
        status_details();
        check_bill_days();
        check_billed();
        setData();
        camera_record();
    }

    public void check_bill_days() {
        Cursor dl_data = database.getBillingRecordData();
        if (dl_data.getCount() > 0) {
            dl_data.moveToNext();
            String Prevdate = String.valueOf(functionsCall.getCursorValue(dl_data, "PREV_READ_DATE"));
            String presdate = String.valueOf(functionsCall.getCursorValue(dl_data, "READDATE"));

            double days_diff = Double.parseDouble(functionsCall.calculateDays(Prevdate, presdate));

            if (days_diff > 31) {
                double dl_diff = days_diff / 30;
                dl_diff = dl_diff - 1;
                database.update_dlrecord(dl_diff);
            } else if (days_diff < 28) {
                double dl_diff = (days_diff + 1) / 30;
                dl_diff = dl_diff - 1;
                database.update_dlrecord(dl_diff);
            }
        }
    }

    public void Tarrif_configuration() {
        mast_cust = (fcall.getTariffConfig1(database.getTarrifData12(mast_custs.getCONSNO())));
        tariff_config = (fcall.getTariffConfig(database.getTarrifData11(mast_cust.get(0).getTARIFF())));
    }

    private void status_details() {
        status_list.clear();
        Cursor data = database.spinnerData();
        if (data.getCount() > 0) {
            while (data.moveToNext()) {
                Mast_Cust mast_cust = new Mast_Cust();
                mast_cust.setRole(functionsCall.getCursorValue(data, "STATUS_NAME"));
                status_list.add(mast_cust);
            }
            sp_curr_status.setAdapter(status_adapter);
            sp_curr_status.setSelection(2);
            if (mast_cust.get(0).getPREVSTAT().contentEquals("2") || mast_cust.get(0).getPREVSTAT().contentEquals("7")) {
                mast_cust.get(0).setPRESSTS(mast_cust.get(0).getPREVSTAT());
                if (mast_cust.get(0).getPREVSTAT().contentEquals("2"))
                    sp_curr_status.setSelection(1);
                if (mast_cust.get(0).getPREVSTAT().contentEquals("7"))
                    sp_curr_status.setSelection(6);
            }
        }
        data.close();
    }

    private void selected_Spinner_Item(int status) {
        if (!id_billed) {
            if (status == 3 || status == 4 || status == 5 || status == 6 || status == 8 || status == 9 || status == 10
                    || status == 11 || status == 12 || status == 13 || status == 14 || status == 16 || status == 17
                    || status == 18 || status == 19 || status == 20 || status == 21 || status == 22 || status == 23
                    || status == 24 || status == 25 || status == 26 || status == 27 || status == 28 || status == 29
                    || status == 30 || status == 31) {
                til_curr_reading.setVisibility(View.VISIBLE);
                et_curr_reading.setText("");
                et_curr_reading.setEnabled(true);
                pfbmdtodvisible(true, "");

            } else if (status == 1 || status == 2 || status == 7 || status == 15) {
                if (mast_cust.get(0).getPREVSTAT().contentEquals("1") || mast_cust.get(0).getPREVSTAT().contentEquals("2") ||
                        mast_cust.get(0).getPREVSTAT().contentEquals("7") || mast_cust.get(0).getPREVSTAT().contentEquals("15"))
                    database.updateDLrecord(0);
                til_curr_reading.setVisibility(View.VISIBLE);
                et_curr_reading.setText(mast_cust.get(0).getPRVRED());
                et_curr_reading.setEnabled(false);
                pfbmdtodvisible(false, "0");
            }
        }
        mast_cust.get(0).setPRESSTS("" + status);
    }

    private void pfbmdtodvisible(boolean value, String text) {
        if (cons_pfflag == 2) {
            til_pf.setVisibility(View.VISIBLE);
            til_bmd.setVisibility(View.VISIBLE);
            et_pf.setText(text);
            et_pf.setEnabled(value);
            et_bmd.setText(text);
            et_bmd.setEnabled(value);
            if (!value)
                et_kvah.setText(mast_cust.get(0).getPREADKVAH());
            else et_kvah.setText(text);
            et_kvah.setEnabled(value);
        }
        if (cons_pfflag == 1) {
            til_pf.setVisibility(View.VISIBLE);
            til_bmd.setVisibility(View.VISIBLE);
            et_pf.setText(mast_cust.get(0).getPFVAL());
            et_pf.setEnabled(false);
            et_bmd.setText(mast_cust.get(0).getBMDVAL());
            et_bmd.setEnabled(false);
            et_kvah.setText(mast_cust.get(0).getPREADKVAH());
            et_kvah.setEnabled(false);
        }
        if (cons_todflag == 1) {
            tod_reading_layout.setVisibility(View.VISIBLE);
            if (!value) {
                et_onpeak.setText(mast_cust.get(0).getTOD_PREVIOUS1());
                et_normalpeak.setText(mast_cust.get(0).getTOD_PREVIOUS2());
                et_offpeak.setText(mast_cust.get(0).getTOD_PREVIOUS4());
            } else {
                et_onpeak.setText(text);
                et_normalpeak.setText(text);
                et_offpeak.setText(text);
            }
            et_onpeak.setEnabled(value);
            et_normalpeak.setEnabled(value);
            et_offpeak.setEnabled(value);
        }
    }

    private void check_billed() {
        Cursor data_billed = database.getBilled_details(mast_cust.get(0).getCONSNO());
        if (data_billed.getCount() > 0) {
            data_billed.moveToNext();
            bill_billed_status = functionsCall.getCursorValue(data_billed, "PRES_STS");
            billed();
        } else un_billed();
        data_billed.close();
    }

    private void un_billed() {
        id_billed = false;
        tv_billing_status.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//        setenabled(0);
//        MenuItem preview = navigationView.getMenu().findItem(R.id.navigation_preview);
//        preview.setVisible(true);
        sp_curr_status.setEnabled(true);
    }

    @SuppressWarnings("deprecation")
    private void billed() {
        id_billed = true;
        tv_billing_status.setBackgroundColor(Color.RED);
//        setenabled(1);
//        MenuItem preview = navigationView.getMenu().findItem(R.id.navigation_preview);
//        preview.setVisible(false);
        sp_curr_status.setSelection(functionsCall.convert_int(bill_billed_status) - 1);
        sp_curr_status.setEnabled(false);
    }

    private void setTextValues(TextView textView, String value) {
        textView.setText(value);
        if (id_billed)
            textView.setTextColor(getResources().getColor(R.color.billed_text_color));
        else textView.setTextColor(getResources().getColor(R.color.billing_text_color));
        tv_billing_status.setTextColor(Color.WHITE);
//        textView.startAnimation(getAnimation());
    }

    public void setData() {
        if (id_billed)
            setTextValues(tv_billing_status, "Billed");
        else setTextValues(tv_billing_status, "Unbilled");
        setTextValues(tv_accountid, mast_cust.get(0).getCONSNO());
        setTextValues(tv_rrno, mast_cust.get(0).getRRNO());
        setTextValues(tv_name, mast_cust.get(0).getNAME());
        setTextValues(tv_address, mast_cust.get(0).getADD1());
        setTextValues(tv_tariff, mast_cust.get(0).getTARIFF());
        setTextValues(tv_prev_status, (mast_cust.get(0).getPREVSTAT()));
        setTextValues(tv_prev_reading, mast_cust.get(0).getPRVRED());
        setTextValues(tv_mf, mast_cust.get(0).getMF());
        setTextValues(tv_load_hp, mast_cust.get(0).getDISPHP());
        setTextValues(tv_load_kw, mast_cust.get(0).getDISPKW());
        setTextValues(tv_billing_date, functionsCall.dateSet());

        data_convertion();

        switch (cons_pfflag) {
            case 0:
                til_pf.setVisibility(View.GONE);
                til_bmd.setVisibility(View.GONE);
                til_kvah.setVisibility(View.GONE);
                break;

            case 1:
                til_pf.setVisibility(View.VISIBLE);
                til_bmd.setVisibility(View.VISIBLE);
                til_kvah.setVisibility(View.VISIBLE);
                et_pf.setText(mast_cust.get(0).getPFVAL());
                et_bmd.setText(mast_cust.get(0).getBMDVAL());
                et_kvah.setText(mast_cust.get(0).getREADKVAH());
                et_pf.setEnabled(false);
                et_bmd.setEnabled(false);
                et_kvah.setEnabled(false);
                break;

            case 2:
                til_pf.setVisibility(View.VISIBLE);
                til_bmd.setVisibility(View.VISIBLE);
                til_kvah.setVisibility(View.VISIBLE);
                if (!id_billed) {
                    et_pf.setText("");
                    et_bmd.setText("");
                    et_kvah.setText("");
                    et_pf.setEnabled(true);
                    et_bmd.setEnabled(true);
                    et_kvah.setEnabled(true);
                }
                break;

            default:
                til_pf.setVisibility(View.GONE);
                til_bmd.setVisibility(View.GONE);
                til_kvah.setVisibility(View.GONE);
                break;
        }

        if (cons_todflag == 1) {
            tod_reading_layout.setVisibility(View.VISIBLE);
            tv_tod_prev_onpeak.setText(mast_cust.get(0).getTOD_PREVIOUS1());
            tv_tod_prev_normalpeak.setText(mast_cust.get(0).getTOD_PREVIOUS2());
            tv_tod_prev_offpeak.setText(mast_cust.get(0).getTOD_PREVIOUS4());
        } else tod_reading_layout.setVisibility(View.GONE);


    }

    private void data_convertion() {
        cons_pfflag = functionsCall.convert_int(mast_cust.get(0).getPF_FLAG());
        cons_todflag = functionsCall.convert_int(mast_cust.get(0).getTOD_FLAG());
        cons_mf = functionsCall.convert_decimal(mast_cust.get(0).getMF());
        cons_prev_read = functionsCall.convert_decimal(mast_cust.get(0).getPRVRED());
        cons_tod_prev1 = functionsCall.convert_decimal(mast_cust.get(0).getTOD_PREVIOUS1());
        cons_tod_prev3 = functionsCall.convert_decimal(mast_cust.get(0).getTOD_PREVIOUS3());
        cons_dlcount = functionsCall.convert_decimal(mast_cust.get(0).getDLCOUNT());
        cons_deposit = functionsCall.convert_decimal(mast_cust.get(0).getDEPOSIT());
        cons_int_amt = functionsCall.convert_decimal(mast_cust.get(0).getINTEREST_AMT());
        cons_arrears = functionsCall.convert_decimal(mast_cust.get(0).getARREARS());
    }

    void camera_record() {
        bill_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!functionsCall.isDeviceSupportCamera(getActivity())) {
                    functionsCall.showtoast(getActivity(), "");
//                        Objects.requireNonNull(getApplicationContext()).finish();
                } else checkforCameraPermissionMandAbove();
            }
        });
    }

    @TargetApi(23)
    public void checkforCameraPermissionMandAbove() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission())
                captureImage();
            else requestPermission();
        } else captureImage();
    }

    @TargetApi(23)
    private void requestPermission() {
        ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]
                {
                        CAMERA
                }, RequestPermissionCode);
    }

    @TargetApi(23)
    private boolean checkPermission() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), CAMERA);
        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    @TargetApi(24)
    private void captureImage() {
        account_id_image = mast_cust.get(0).getCONSNO();
        billing_MRCode = mast_cust.get(0).getMRCODE();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE, getActivity());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public Uri getOutputMediaFileUri(int type, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider",
                    Objects.requireNonNull(getOutputMediaFile(type)));
        } else return Uri.fromFile(getOutputMediaFile(type));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(IMAGE_FILE_URI, fileUri);
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        try {
//            if ((savedInstanceState != null ? savedInstanceState.getParcelable(IMAGE_FILE_URI) : null) != null) {
//                fileUri = savedInstanceState.getParcelable(IMAGE_FILE_URI);
//                previewCapturedImage();
//            }
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
//    }


//    private void previewCapturedImage() {
//        try {
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
//            bill_capture.setImageBitmap(rotateImage(bitmap, fileUri.getPath()));
//            functionsCall.logStatus("Image Size: " + sizeOf(bitmap));
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        } catch (OutOfMemoryError e) {
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            // downsizing image as it throws OutOfMemory Exception for larger images
//            options.inSampleSize = 8;
//            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
//            bill_capture.setImageBitmap(rotateImage(bitmap, fileUri.getPath()));
//            functionsCall.logStatus("OME Image Size: " + sizeOf(bitmap));
//        }
//    }

//    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
//    protected int sizeOf(Bitmap data) {
//        return data.getByteCount();
//    }
//
//    public static Bitmap rotateImage(Bitmap src, String Imagepath) {
//        Bitmap bmp;
//        // create new matrix
//        Matrix matrix = new Matrix();
//        ExifInterface exif = null;
//        try {
//            exif = new ExifInterface(Imagepath);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        int orientation = 0;
//        if (exif != null) {
//            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//        }
//        new FunctionCall().logStatus("Orientation: " + orientation);
//        if (orientation == 1) {
//            bmp = src;
//            /*matrix.postRotate(270);
//            bmp = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);*/
//        } else if (orientation == 3) {
//            matrix.postRotate(180);
//            bmp = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
//        } else if (orientation == 8) {
//            matrix.postRotate(270);
//            bmp = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
//        } else {
//            matrix.postRotate(90);
//            bmp = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
//        }
//        return bmp;
//    }

    private static File getOutputMediaFile(int type) {
        FunctionCall functionsCall = new FunctionCall();
        // External sdcard location
        File mediaStorageDir = new File(android.os.Environment.getExternalStorageDirectory(),
                "TRM" + File.separator + IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault()).format(new Date());
        if (type == MEDIA_TYPE_IMAGE) {
//            functionsCall.checkimage_and_delete(IMAGE_DIRECTORY_NAME, account_id_image);
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + account_id_image + "_" + billing_MRCode + "_" +
                    timeStamp + FILE_IMAGE_FORMAT);
            pathname = mediaStorageDir.getPath() + File.separator + account_id_image + "_" + billing_MRCode + "_" + timeStamp +
                    FILE_IMAGE_FORMAT;
            path_extension = account_id_image + "_" + billing_MRCode + "_" + timeStamp + FILE_IMAGE_FORMAT;
            filename = account_id_image + "_" + billing_MRCode + "_" + timeStamp;
        } else {
            return null;
        }
        return mediaFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                /*previewCapturedImage();*/
                cons_ImgAdd = pathname;
                cons_imageextension = path_extension;
                Bitmap bitmap = null;
                try {
                    bitmap = functionsCall.getImage(cons_ImgAdd, getActivity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                functionsCall.checkimage_and_delete(DIR_IMAGE_STORE, account_id_image);
                File destination = functionsCall.filestorepath(DIR_IMAGE_STORE, cons_imageextension);
                if (bitmap != null) {
                    saveExternalPrivateStorage(destination, timestampItAndSave(bitmap));
                }
                String destination_file = functionsCall.filepath(DIR_IMAGE_STORE) + File.separator + cons_imageextension;
                Bitmap bitmap1 = null;
                try {
                    bitmap1 = functionsCall.getImage(destination_file, getActivity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bill_capture.setImageBitmap(bitmap1);
                imagetaken = true;
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                functionsCall.showtoast(getActivity(), getResources().getString(R.string.user_cancelled_image));
            } else {
                // failed to capture image
                functionsCall.showtoast(getActivity(), getResources().getString(R.string.image_capture_failed));
            }
        }
    }

    private void saveExternalPrivateStorage(File folderDir, Bitmap bitmap) {
        if (folderDir.exists()) {
            folderDir.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(folderDir);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap timestampItAndSave(Bitmap bitmap) {
        Bitmap watermarkimage = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // Bitmap bitmap = BitmapFactory.decodeFile(getOutputMediaFile().getAbsolutePath());

        // Bitmap src = BitmapFactory.decodeResource(); // the original file is cuty.jpg i added in resources
        Bitmap dest = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas cs = new Canvas(dest);
        Paint tPaint = new Paint();
        tPaint.setTextSize(42);
        tPaint.setColor(Color.RED);
        tPaint.setStyle(Paint.Style.FILL);
        cs.drawBitmap(bitmap, 0f, 0f, null);

        float height = tPaint.measureText("yY");
        cs.drawText(filename, 20f, height + 15f, tPaint);
        // cs.drawText(dateTime, 2000f, 1500f, tPaint);

        try {
            dest.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new File(pathname)));
            watermarkimage = BitmapFactory.decodeStream(new FileInputStream(new File(pathname)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return watermarkimage;
    }

    public void showdailog(int id) {
        final AlertDialog alertDialog;
        @SuppressLint("InflateParams")
        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.dailog_message_layout, null);
        TextView textView = linearLayout.findViewById(R.id.dialog_message);
        TextView textView1 = linearLayout.findViewById(R.id.dialog_title);
        Button btn_positive = linearLayout.findViewById(R.id.dialog_positive_btn);
        Button btn_negative = linearLayout.findViewById(R.id.dialog_negative_btn);

        switch (id) {
            case SUB_NORMAL_DLG:
                AlertDialog.Builder sub_normal = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
//                sub_normal.setTitle("Sub_Normal_REading");
                sub_normal.setCancelable(false);
                sub_normal.setView(linearLayout);
                textView1.setText("Sub_Normal_REading");
                textView.setText(String.format("%s %s", getResources().getString(R.string.dlg_check_reading), mast_cust.get(0).getAVGCON()));
                btn_negative.setText(R.string.skip);
                alertDialog = sub_normal.create();
                btn_positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        et_curr_reading.requestFocus();
                        et_curr_reading.setSelection(et_curr_reading.getText().length());
                    }
                });
                btn_negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        setValues();
                    }
                });
                Objects.requireNonNull(alertDialog.getWindow()).requestFeature(Window.FEATURE_RIGHT_ICON);
                alertDialog.getWindow().setFeatureDrawableResource(Window.FEATURE_RIGHT_ICON,R.drawable.ic_clear);
                Objects.requireNonNull(alertDialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
//                Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                alertDialog.show();
                break;
            case AB_NORMAL_DLG:
                AlertDialog.Builder ab_normal = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
//                ab_normal.setTitle(getResources().getString(R.string.dlg_ab_normal_title));
                ab_normal.setCancelable(false);

                ab_normal.setView(linearLayout);
                textView1.setText(getResources().getString(R.string.dlg_ab_normal_title));
                textView.setText(String.format("%s %s", getResources().getString(R.string.dlg_check_reading), mast_cust.get(0).getAVGCON()));
                btn_negative.setText(R.string.skip);
                alertDialog = ab_normal.create();
                btn_positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        et_curr_reading.requestFocus();
                        et_curr_reading.setSelection(et_curr_reading.getText().length());
                    }
                });
                btn_negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        setValues();
                    }
                });
                Objects.requireNonNull(alertDialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
//                Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
                break;
        }
    }

    private void setValues() {
        getSet_mastValues.setValues(mast_cust, subdivDetails, false);
    }

    private void check_editTexts() {
        if(autoCompleteTextView.getVisibility() == View.VISIBLE){
            autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_NEXT){
                        et_curr_reading.requestFocus();
                    }
                    return false;

                }
            });
        }

        et_curr_reading.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    et_mtr_digit.requestFocus();
                    et_mtr_digit.setSelection(et_mtr_digit.getText().length());
                }
                return false;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.clear, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_id:
                autoCompleteTextView.setVisibility(View.VISIBLE);
                autoCompleteTextView.requestFocus();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), android.R.layout.simple_list_item_1, customer);
                autoCompleteTextView.setAdapter(adapter);
                autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        mast_custs.setCONSNO(parent.getItemAtPosition(position).toString());
                        Toast.makeText(getActivity(), "Selected  is: \t" + parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
                        Tarrif_configuration();
                        getbillData();

                    }
                });
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

}

