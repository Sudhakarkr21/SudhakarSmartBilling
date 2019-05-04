package com.example.tvd.bmdandpfcal;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.tvd.bmdandpfcal.values.Constant.DIR_DATABASE;
import static com.example.tvd.bmdandpfcal.values.Constant.DIR_FTP_DOWNLOAD;
import static com.example.tvd.bmdandpfcal.values.Constant.DOWNLOAD_COMPLETED;
import static com.example.tvd.bmdandpfcal.values.Constant.DOWNLOAD_FAILED;
import static com.example.tvd.bmdandpfcal.values.Constant.DOWNLOAD_FILE_FOUND;
import static com.example.tvd.bmdandpfcal.values.Constant.DOWNLOAD_FILE_NOT_FOUND;
import static com.example.tvd.bmdandpfcal.values.Constant.FILE_TRM_DATABASE;

public class LoginActivity extends AppCompatActivity {

    private static final int DLG_LOGIN_BT_VALIDATION = 19;
    private static final int DLG_SELECTION = 20;
    private static final int DLG_FTPFILE_SELECTION = 21;
    private static final int LOGIN_SUCCESS = 22;

    ProgressDialog progress;
    Spinner spinner;
    Button login;
    Toolbar toolbar;
    EditText etmrid,etmrdeviceid;
    ArrayList<Mast_Cust> userrolelist;
    ArrayList<String> ftpfiles_list;
    String[] userList;
    Mast_Cust mast_cust;
    RoleAdapter userroleadapter;
    String main_role,main_selected_date,mr_code,main_mr_password,serverpath,
            mobileFilePathDwnld,Zipformat,file_name_DWN;
    int year, month, date;

    Download download;
    FTPAPI ftpapi;
    Dialog dialog;
    ArrayAdapter<String> ftpfiles_adapter;
    Database database;


    Handler handler =new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case DOWNLOAD_FILE_FOUND:
                    Log.d("Download","Starting to download file");
                    String namefile = mast_cust.getDownloadFileName();
                    ftpfiles_list.add(namefile);
                    dialog.dismiss();
                    showdialog(DLG_FTPFILE_SELECTION);
//                    showprogressdialog("Downloading file please wait..", progress, "Download");
//                    download.runDownLoadTask(mobileFilePathDwnld,mast_cust.getDownloadFileName(),Zipformat,serverpath,handler);
                    Log.d("Downloaded","DB is Downloaded");
                    break;

                case DOWNLOAD_FILE_NOT_FOUND:
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(),"File Not Found",Toast.LENGTH_LONG).show();
                    break;

                case DOWNLOAD_COMPLETED:
                    ftpfiles_list.clear();
                    showdialog(LOGIN_SUCCESS);
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(),"Download Completed",Toast.LENGTH_LONG).show();
                    break;

                case DOWNLOAD_FAILED:
                    ftpfiles_list.clear();
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(),"Download failed",Toast.LENGTH_LONG).show();
                    break;

            }
            return true;
        }
    });


    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.loginactivity);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Smart Billing");

        download = new Download();
        ftpfiles_list = new ArrayList<>();
        ftpapi = new FTPAPI(this);
        progress = new ProgressDialog(LoginActivity.this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_DARK);

        init();
        mobileFilePathDwnld = fcall.filepath("DownloadFolder") + File.separator;
        Zipformat = ".zip";

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(main_role)) {
                    showdialog(LOGIN_SUCCESS);

                } else Toast.makeText(getApplicationContext(),"Please Select role and Proceed",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void init(){
        spinner = findViewById(R.id.user);
        login = findViewById(R.id.login);
        userrolelist = new ArrayList<>();
        userList = getResources().getStringArray(R.array.login_role);


        userroleadapter = new RoleAdapter(this, userrolelist);
        spinner.setAdapter(userroleadapter);
        for (String role : userList) {
            mast_cust = new Mast_Cust();
            mast_cust.setRole(role);
            userrolelist.add(mast_cust);
            userroleadapter.notifyDataSetChanged();
        }


        spinner.setSelection(1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int spos, long dpos) {
                TextView tvrole = view.findViewById(R.id.textView1);
                String role = tvrole.getText().toString();
                if (!role.equals("--SELECT--")) {
                    main_role = role;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        try {
            File MYDB = fcall.filestorepath(DIR_DATABASE, FILE_TRM_DATABASE);
            if (MYDB.exists()) {
                database = new Database(this);
                database.openDataBase();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @SuppressLint("SetTextI18n")
    protected void showdialog(int id) {
        switch (id){
            case DLG_LOGIN_BT_VALIDATION:
                switch (main_role){
                    case "MR":
                        AlertDialog.Builder mrlogin = new AlertDialog.Builder(this);
                        mrlogin.setTitle("MR Details");
                        LinearLayout v = (LinearLayout) getLayoutInflater().inflate(R.layout.mrlogin,null);
                        etmrid = v.findViewById(R.id.editText1);
                        etmrdeviceid = v.findViewById(R.id.editText2);
                        etmrdeviceid.setShowSoftInputOnFocus(false);
                        final EditText et_mr_password = v.findViewById(R.id.et_mr_password);
                        LinearLayout date_layout = v.findViewById(R.id.dlg_date_layout);

                        etmrdeviceid.setText(fcall.dateSet());
                        etmrdeviceid.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Calendar cal = Calendar.getInstance();
                                year = cal.get(Calendar.YEAR);
                                month = cal.get(Calendar.MONTH);
                                date = cal.get(Calendar.DAY_OF_MONTH);
                                DatePickerDialog dialog = new DatePickerDialog(LoginActivity.this,
                                        dateSetListener, year, month, date);
                                if (date > 1) {

                                    Calendar min_cal = Calendar.getInstance();
                                    min_cal.set(year, month, 1);
                                    dialog.getDatePicker().setMinDate(min_cal.getTimeInMillis());
                                }
                                dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
                                dialog.show();
                            }
                        });

                        mrlogin.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mr_code = etmrid.getText().toString();
                                if(!mr_code.equals("")){
                                    if(mr_code.length() == 8){
                                        main_selected_date = etmrdeviceid.getText().toString();
                                        if(!TextUtils.isEmpty(et_mr_password.getText().toString())){
                                            main_mr_password = et_mr_password.getText().toString();
                                            showdialog(DLG_SELECTION);
                                        }else Toast.makeText(getApplicationContext(),"Please Enter Password",Toast.LENGTH_LONG).show();
                                    }else Toast.makeText(getApplicationContext(),"Please Enter Valid MR CODE",Toast.LENGTH_LONG).show();

                                }else Toast.makeText(getApplicationContext(),"Please Enter MR CODE",Toast.LENGTH_LONG).show();
                            }
                        });
                        mrlogin.setView(v);
                        dialog = mrlogin.create();
                        dialog.show();

                        break;
                    case "AEE":
                        break;
                    case "AAO":
                        break;
                    case "ADMIN":
                        break;
                }
                break;

            case DLG_SELECTION:
                AlertDialog.Builder download1 = new AlertDialog.Builder(this);
                download1.setTitle("Download Selection");
                RelativeLayout dwldlayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.selectionstatus, null);
                download1.setView(dwldlayout);
                Button xmlfile_btn = dwldlayout.findViewById(R.id.button1);
                xmlfile_btn.setText("Billing File Download");

                xmlfile_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        serverpath = DIR_FTP_DOWNLOAD + mr_code.substring(0, 6) + "/" + fcall.changedateformat(main_selected_date, "") + "/";
                        new Thread(ftpapi.new Check_download_available_file(mr_code,main_selected_date,handler,mast_cust)).start();
                    }
                });

                download1.setView(dwldlayout);
                dialog = download1.create();
                dialog.show();
                break;

            case DLG_FTPFILE_SELECTION:
                android.app.AlertDialog.Builder ftpfiles = new android.app.AlertDialog.Builder(this);
                ftpfiles.setTitle("Download Available");
                LinearLayout ftplayout = (LinearLayout) getLayoutInflater().inflate(R.layout.ftpfileslayout, null);
                ftpfiles.setView(ftplayout);
                final ListView ftplist = ftplayout.findViewById(R.id.ftp_listview);
                ftpfiles_adapter = new ArrayAdapter<>(LoginActivity.this, android.R.layout.simple_list_item_single_choice, ftpfiles_list);
                ftplist.setAdapter(ftpfiles_adapter);
                ftpfiles.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ftplist.getCheckedItemPosition() >= 0) {
                            file_name_DWN = downloadfile(ftpfiles_list.get(ftplist.getCheckedItemPosition()));
                            showprogressdialog("Downloading file please wait..", progress, "Download");
                            download.runDownLoadTask(mobileFilePathDwnld,mast_cust.getDownloadFileName(),Zipformat,serverpath,handler);

                        } else {
                            Toast.makeText(getApplicationContext(),"Please select file to download...",Toast.LENGTH_LONG).show();

                        }
                    }
                });
                dialog = ftpfiles.create();
                dialog.show();
                break;

            case LOGIN_SUCCESS:
                Intent intent = new Intent(LoginActivity.this,Navigation_Activity.class);
                startActivity(intent);
                break;

        }
    }

    private void showprogressdialog(String Message, ProgressDialog dialog, String Title) {
        dialog.setTitle(Title);
        dialog.setMessage(Message);
        dialog.setCancelable(false);
        dialog.setIndeterminate(true);
        dialog.show();
    }
    private String downloadfile(String result) {
        String response;
        response = result.substring(0, 5) + mr_code + "_" + result.substring(5);
        fcall.logStatus("Download file: " + response);
        return response;
    }

        public DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Date Starttime = null;
                etmrdeviceid.setText("");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                try {
                    Starttime = new SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(("" + dayOfMonth + "/" + "" + (monthOfYear + 1) + "/" + "" + year));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String dateselected = sdf.format(Starttime);
                Log.d("date",dateselected);
                etmrdeviceid.setText(dateselected);
                etmrdeviceid.setSelection(etmrdeviceid.getText().length());
                main_selected_date = etmrdeviceid.getText().toString();
            }
        };
}
