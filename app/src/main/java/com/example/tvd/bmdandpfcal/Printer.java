package com.example.tvd.bmdandpfcal;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.analogics.thermalAPI.Bluetooth_Printer_3inch_prof_ThermalAPI;
import com.analogics.thermalprinter.AnalogicsThermalPrinter;
import com.example.tvd.bmdandpfcal.services.BluetoothServices;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.lvrenyang.io.Canvas;
import com.lvrenyang.io.Pos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Printer extends AppCompatActivity implements View.OnClickListener {
    private static final int RequestPermissionCode = 1;
    AnalogicsThermalPrinter conn;
    Paint paint;
    BluetoothAdapter deviceadapter;
    Bluetooth_Printer_3inch_prof_ThermalAPI api;
    Canvas mCanvas = BluetoothServices.mCanvas;
    ExecutorService es = BluetoothServices.es;
    Button btn_text_record, btn_image_record;
    Pos mPos = BluetoothServices.mPos;
    public static int nPrintWidth = 570;
    public static int nPrintHeight = 600;
    StringBuilder report = new StringBuilder();
    private float yaxis = 0;
    private float xaxis = 0;
    private String splchar = "";
    private Bitmap barcode;
    private ArrayList<String> res;
    private String rep_address_1 = "", rep_address_2 = "";
    TextView fixedcharge, energyCharge, pf, bmd, prvReading, tax, sanctload, accountID,
            name, tarrif, constant1, average, consumption1, RRNO1, RecordedMD, preReading,
            powerFactor, CurrentAmtBill, NetAmtDue,arrears;
    String fixedcharge1 = "null", energycharge1 = "null", pf1 = "null", bmd1 = "null", tax1 = "",
            customerID = "", currentAmt = "", netBillAmt = "", bmdValue = "", pfValue = "", consumption = "", rebate1;
    String ec1 = " ", ec2 = " ", ec3 = " ", ec4 = " ", fc1 = " ", fc2 = " ";

    Database database;
    Context context;
    String RRNO, CustomerName, previousReading, load, constant, presentReading;
    List<Mast_Cust> mast_custs;

    Printer() {
    }

    Printer(Database database, Context context) {
        this.database = database;
        this.context = context;
    }

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.printer);
        init();
        btn_text_record = findViewById(R.id.print_text_btn);
        btn_text_record.setOnClickListener(this);
        btn_image_record = findViewById(R.id.print_image_btn);
        btn_image_record.setOnClickListener(this);

        deviceadapter = BluetoothAdapter.getDefaultAdapter();
        deviceadapter.enable();


//        conn = new AnalogicsThermalPrinter();
        api = new Bluetooth_Printer_3inch_prof_ThermalAPI();

        res = new ArrayList<>();

        String address = "#3B-11, Ist Floor, 3rd Block, VITC Export Bhavan, KIADB, Sub-Registrar, Office Building, 3rd Main,14th Cross";
        String regex = "a-z~@#$%^&*:;<>.,/}{+";
        if (address.substring(0, 1).matches("[" + regex + "]+")) {
            splchar = address.substring(0, 1);
        }
        splitString(address, 30, res);

        if (res.size() > 0) {
            rep_address_1 = "  " + res.get(0);
            if (res.size() > 1) {
                rep_address_2 = "  " + res.get(1);
            }
        }
        barcode = getBitmap("1110101030468" + "123456.00");

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                checkPermissionsMandAbove();
//            }
//        }, 500);
        intent();
        display();

        energycharge1 = energycharge1.replaceAll("\\r|\\n", "   ");
        String[] param = energycharge1.split("   ");
        for (int j = 0; j < param.length; j++) {
            switch (j) {
                case 0:
                    ec1 = param[0];
                    break;
                case 1:
                    ec2 = param[1];
                    break;
                case 2:
                    ec3 = param[2];
                    break;
                case 3:
                    ec4 = param[3];
            }
        }
//        fixedcharge1 = fixedcharge1.replaceAll("\\r|\\n", "   ");
        String[] param1 = fixedcharge1.split("\\r|\\n");
        for (int k = 0; k < param1.length; k++) {
            switch (k) {
                case 0:
                    fc1 = param1[0];
                    break;
                case 1:
                    fc2 = param1[1];
            }
        }
        try {
            database.openDataBase();
            // after open data  base u can read write data base
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("ddddd", customerID);
        mast_custs.clear();
    }

    void init() {
        powerFactor = findViewById(R.id.pf);
        preReading = findViewById(R.id.presentReading);
        prvReading = findViewById(R.id.previousReading);
        accountID = findViewById(R.id.CunsumerID);
        tarrif = findViewById(R.id.tarrif);
        name = findViewById(R.id.name);
        RRNO1 = findViewById(R.id.rrno);
        sanctload = findViewById(R.id.SanctKW);
        constant1 = findViewById(R.id.constant);
        consumption1 = findViewById(R.id.consumption);
        average = findViewById(R.id.average);
        RecordedMD = findViewById(R.id.RecordedMD);
        fixedcharge = findViewById(R.id.fcharge);
        energyCharge = findViewById(R.id.echarge);
        bmd = findViewById(R.id.bmdpenalty1);
        pf = findViewById(R.id.pfpenalty1);
        tax = findViewById(R.id.tax);
        CurrentAmtBill = findViewById(R.id.cur_bill_Amt);
        NetAmtDue = findViewById(R.id.net_Amt_Due);
        arrears = findViewById(R.id.arrears);
    }

    public void intent(){
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        fixedcharge1 = i.getStringExtra("fixedcharge");
        energycharge1 = i.getStringExtra("energycharges");
        bmd1 = i.getStringExtra("BmdPenalty");
        pf1 = i.getStringExtra("PFPenalty");
        customerID = i.getStringExtra("customerID");
        presentReading = i.getStringExtra("presentReading");
        previousReading = i.getStringExtra("previousReadind");
        load = i.getStringExtra("load");
        constant = i.getStringExtra("constant");
        RRNO = i.getStringExtra("rrno");
        tax1 = i.getStringExtra("tax");
        currentAmt = i.getStringExtra("cur1");
        netBillAmt = i.getStringExtra("netAmtDue2");
        bmdValue = i.getStringExtra("BmdValue");
        pfValue = i.getStringExtra("pfValue");
        consumption = i.getStringExtra("Consumption");
        rebate1 = i.getStringExtra("rebate");
        mast_custs = (List<Mast_Cust>) bundle.getSerializable("value");
        Log.d("sfsss", String.valueOf(mast_custs.get(0).getNAME()));
    }

    public void display(){
        accountID.setText(mast_custs.get(0).getCONSNO());
        sanctload.setText(mast_custs.get(0).getSANCKW());
        name.setText(mast_custs.get(0).getNAME());
        tarrif.setText(mast_custs.get(0).getTARIFF_NAME());
        constant1.setText(mast_custs.get(0).getMF());
        average.setText(mast_custs.get(0).getAVGCON());
        consumption1.setText(consumption);
        RRNO1.setText(mast_custs.get(0).getRRNO());
        RecordedMD.setText(bmdValue);
        preReading.setText(presentReading);
        prvReading.setText(previousReading);
        powerFactor.setText(pfValue);
        fixedcharge.setText(fixedcharge1);
        energyCharge.setText(energycharge1);
        bmd.setText(bmd1);
        pf.setText(pf1);
        tax.setText(tax1);
        arrears.setText(mast_custs.get(0).getARREARS());
        CurrentAmtBill.setText(currentAmt);
        NetAmtDue.setText(netBillAmt);

        report.append("AccountID").append("\n")
                .append(mast_custs.get(0).getCONSNO()).append("\n")
                .append("Tariff      :").append(mast_custs.get(0).getTARIFF_NAME()).append("   ").append("SanctKW   :").append(mast_custs.get(0).getSANCKW()).append("\n")
                .append("RRNO        :").append(mast_custs.get(0).getRRNO()).append("\n")
                .append("Name        :").append(mast_custs.get(0).getNAME()).append("\n")
                .append("PreRding    :").append(presentReading).append("\n")
                .append("PrvRding    :").append(previousReading).append("\n")
                .append("Constant    :").append(mast_custs.get(0).getMF()).append("\n")
                .append("Consumption :").append(consumption).append("\n")
                .append("Average     :").append(mast_custs.get(0).getAVGCON()).append("\n")
                .append("Recorded MD :").append(bmdValue).append("\n")
                .append("Power Factor:").append(pfValue).append("\n")
                .append("--------------Fixed Charge--------------").append("\n")
                .append(fixedcharge1).append("\n")
                .append("--------------Energy Charges-----------").append("\n")
                .append(energycharge1).append("\n")
                .append("MD Penalty               :").append(bmd1).append("\n")
                .append("PF Penalty               :").append(pf1).append("\n")
                .append("Interest          @1%    :").append("0.0").append("\n")
                .append("Tax               @9%    :").append(tax1).append("\n")
                .append("Curr_Bill_Amt            :").append(currentAmt).append("\n")
                .append("Arrears                  :").append(mast_custs.get(0).getARREARS()).append("\n")
                .append("\n")
                .append("Net_Amt_Due              :").append(netBillAmt).append("\n");
        fcall.Calculation_textfile(report.toString(),mast_custs.get(0).getCONSNO());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.print_text_btn:
                break;

            case R.id.print_image_btn:
                int GPT_printer_height = 1800;
                deviceadapter.startDiscovery();
                paint = new Paint();
                printanalogics();
//                es.submit(new TaskPrint(mCanvas, GPT_printer_height,mPos));
                break;
        }
    }

//    private void enable_buttons(boolean value) {
////        btn_text_record.setEnabled(value);
//        btn_image_record.setEnabled(value);
//    }

//    private void startService() {
//        Intent intent = new Intent(Printer.this, BluetoothServices.class);
//        startService(intent);
//    }
//
//    private void stopService() {
//        Intent intent = new Intent(Printer.this, BluetoothServices.class);
//        stopService(intent);
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        registerReceiver(mReceiver, new IntentFilter(BLUETOOTH_RESULT));
//    }
//
//    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String status = intent.getStringExtra(PRINTER_MSG);
//            switch (status) {
//                case CONNECTED:
//                    enable_buttons(true);
//                    Toast.makeText(Printer.this, "Connected", Toast.LENGTH_SHORT).show();
//                    break;
//
//                case DISCONNECTED:
//                    enable_buttons(false);
//                    Toast.makeText(Printer.this, "Disconnected", Toast.LENGTH_SHORT).show();
//                    break;
//            }
//        }
//    };

//    @TargetApi(23)
//    private void checkPermissionsMandAbove() {
//        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
//        if (currentapiVersion >= 23) {
//            if (!checkPermission()) {
//                requestPermission();
//            } else startService();
//        } else startService();
//    }
//
//    private void requestPermission() {
//        ActivityCompat.requestPermissions(Printer.this, new String[]
//                {
//                        ACCESS_FINE_LOCATION
//                }, RequestPermissionCode);
//    }
//
//    private boolean checkPermission() {
//        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
//        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED;
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case RequestPermissionCode:
//                if (grantResults.length > 0) {
//                    boolean ReadLocationPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                    if (!ReadLocationPermission)
//                        checkPermissionsMandAbove();
//                    else startService();
//                }
//                break;
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        stopService();
//        unregisterReceiver(mReceiver);
    }

    //******************************************************** TaskPrint **********************************************************************
    private class TaskPrint implements Runnable {
        Canvas canvas;
        int print_height;
        Pos pos2 = null;

        private TaskPrint(Canvas pos, int height, Pos pos2) {
            this.canvas = pos;
            this.print_height = height;
            this.pos2 = pos2;
        }

        @Override
        public void run() {
            final boolean bPrintResult = PrintTicket(canvas, print_height);
            final boolean bIsOpened = canvas.GetIO().IsOpened();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), bPrintResult ? getResources().getString(R.string.printsuccess) :
                            getResources().getString(R.string.printfailed), Toast.LENGTH_SHORT).show();
                    if (bIsOpened) {
                        yaxis = 0;
                    }
                }
            });
        }

        private boolean PrintTicket(Canvas canvas, int nPrintHeight) {
            boolean bPrintResult;
            Typeface tfNumber = Typeface.createFromAsset(getAssets(), "fonts/DroidSansMono.ttf");
            canvas.CanvasBegin(576, nPrintHeight);
            canvas.SetPrintDirection(0);
            int maxlength = 38;
            /*printboldtext(canvas, space("HUBLI ELECTRICITY SUPPLY COMPANY LTD", maxlength), tfNumber, 25);
            printtext_center(canvas, "", tfNumber, 20, 4);*/
            //pos2.POS_FeedLine();
            pos2.POS_S_Align(0);
            Bitmap bit = BitmapFactory.decodeResource(getResources(), R.drawable.img5);
            pos2.POS_PrintPicture(bit, nPrintWidth, 0, 0);


            printtext(canvas, "ಉಪ ವಿಭಾಗ/Sub Division", centeralign2(":", 0), "540038", tfNumber, 20, 4);
            printtext(canvas, "ಆರ್.ಆರ್.ಸಂಖ್ಯೆ/RRNO", centeralign2(":", 0), "IP57.228", tfNumber, 20, 4);
            printtext(canvas, "ಖಾತೆ ಸಂಖ್ಯೆ/Account ID", centeralign2(":", 0), "9913164549", tfNumber, 20, 4);

//            printtext_center(canvas, centeralign2("ಹೆಸರು ಮತ್ತು ವಿಳಾಸ/Name and Address", 10), tfNumber, 20, 6);
//            printtext_center(canvas, centeralign2(rep_address_1, 10), tfNumber, 20, 6);
//            printtext_center(canvas, centeralign2(rep_address_2, 10), tfNumber, 20, 6);
//            printtext(canvas, "ಜಕಾತಿ/Tariff", centeralign2(":", 0), fixedcharge1, tfNumber, 20, 4);
//            printtext(canvas, "ಮಂ.ಪ್ರಮಾಣ/Sanct Load", centeralign2(":", 0), "HP: 3  KW 2", tfNumber, 20, 4);
//            printtext(canvas, "Billing Period", centeralign2(":", 0), "01/07/2018" + "-" + "01/08/2018", tfNumber, 20, 4);
//            printtext(canvas, "ರೀಡಿಂಗ ದಿನಾಂಕ/Reading Date", centeralign2(":", 0), "01/08/2018", tfNumber, 20, 4);
//            printtext(canvas, "ಬಿಲ್ ಸಂಖ್ಯೆ/Bill No", centeralign2(":", 0), "991316454908201801", tfNumber, 20, 4);
//            printtext(canvas, "ಮೀಟರ್ ಸಂಖ್ಯೆ/Meter SlNo", centeralign2(":", 0), "500010281098" + 0, tfNumber, 20, 4);
//            printtext(canvas, "ಇಂದಿನ ಮಾಪನ/Pres Rdg", centeralign2(":", 0), "658 / NOR", tfNumber, 20, 4);
//            printtext(canvas, "ಹಿಂದಿನ ಮಾಪನ/Prev Rdg", centeralign2(":", 0), "600 / NOR", tfNumber, 20, 4);
//            printtext(canvas, "ಮಾಪನ ಸ್ಥಿರಾಂಕ/Constant", centeralign2(":", 0), rightspacing("1", 5), tfNumber, 20, 4);
//            printtext(canvas, "ಬಳಕೆ/Consumption", centeralign2(":", 0), rightspacing("58", 5), tfNumber, 20, 4);
//            printtext(canvas, "ಸರಾಸರಿ/Average", centeralign2(":", 0), rightspacing("51", 5), tfNumber, 20, 4);
//            printtext(canvas, "ದಾಖಲಿತ ಬೇಡಿಕೆ/Recorded MD", centeralign2(":", 0), rightspacing("10", 5), tfNumber, 20, 4);
//            printtext(canvas, "ಪವರ ಫ್ಯಾಕ್ಟರ/Power Factor", centeralign2(":", 0), rightspacing("0.85", 5), tfNumber, 20, 4);
//            printtext_center(canvas, "", tfNumber, 20, 4);
//
//            printtext_center(canvas, centeralign2("ನಿಗದಿತ ಶುಲ್ಕ/Fixed Charges", 10), tfNumber, 20, 6);
//            printtext_center(canvas, "", tfNumber, 20, 4);
//            printtext_center(canvas, centeralign2(fc1, 10) , tfNumber, 20, 6);
//            printtext_center(canvas, centeralign2(fc2, 10) , tfNumber, 20, 6);
//            printtext_center(canvas, "", tfNumber, 20, 4);
////
//            printtext_center(canvas, centeralign2("ವಿದ್ಯುತ್ ಶುಲ್ಕ/Energy Charges", 10), tfNumber, 20, 6);
//            printtext_center(canvas, "", tfNumber, 20, 4);
//            printtext_center(canvas, centeralign2(ec1, 10) , tfNumber, 20, 6);
//            printtext_center(canvas, centeralign2(ec2, 10) , tfNumber, 20, 6);
//            printtext_center(canvas, centeralign2(ec3, 10) , tfNumber, 20, 6);
//            printtext_center(canvas, centeralign2(ec4, 10) , tfNumber, 20, 6);


//
//            printtext2(canvas, "ಎಫ್.ಎ.ಸಿ/FAC", centeralign2(":", 0), rightspacing("1258.00", 14), tfNumber, 20, 6);
//            printtext2(canvas, "ರಿಯಾಯಿತಿ/Rebates/TOD", centeralign2(":", 0), rightspacing("10.00", 14), tfNumber, 20, 6);
            printtext(canvas, "ನಿಗದಿತ ಶುಲ್ಕ/Fixed Charges", centeralign2(":", 0), fc1, tfNumber, 20, 4);
            printtext(canvas, " ", centeralign2(":", 0), fc2, tfNumber, 20, 4);
            printtext(canvas, "ವಿದ್ಯುತ್ ಶುಲ್ಕ/Energy Charges", centeralign2(":", 0), ec1, tfNumber, 20, 4);
            printtext(canvas, " ", centeralign2(":", 0), ec2, tfNumber, 20, 4);
            printtext(canvas, " ", centeralign2(":", 0), ec3, tfNumber, 20, 4);
            printtext(canvas, " ", centeralign2(":", 0), ec4, tfNumber, 20, 4);


            printtext2(canvas, "ಪಿ.ಎಫ್ ದಂಡ/PF Penalty", centeralign2(":", 0), rightspacing(pf1, 14), tfNumber, 20, 6);
            printtext2(canvas, "ಎಂ.ಡಿ.ದಂಡ/MD Penalty", centeralign2(":", 0), rightspacing(bmd1, 14), tfNumber, 20, 6);
//            printtext2(canvas, "ಬಡ್ಡಿ/Interest @1%", centeralign2(":", 0), rightspacing("3.64", 14), tfNumber, 20, 6);
//            printtext2(canvas, "ಇತರೆ/Others", centeralign2(":", 0), rightspacing("0.00", 14), tfNumber, 20, 6);
//            printtext2(canvas, "ತೆರಿಗೆ/Tax @9%", centeralign2(":", 0), rightspacing("25.47", 14), tfNumber, 20, 6);
//            printtext2(canvas, "ಒಟ್ಟು ಬಿಲ್ ಮೊತ್ತ/Cur Bill Amt", centeralign2(":", 0), rightspacing("620.01", 14), tfNumber, 20, 6);


//            printtext2(canvas, "ಬಾಕಿ/Arrears", centeralign2(":", 0), rightspacing("1258.00", 14), tfNumber, 20, 6);
//            printtext2(canvas, "ಜಮಾ/Credits & Adj", centeralign2(":", 0), rightspacing("320.01", 14), tfNumber, 20, 6);
//            printtext2(canvas, "ಐ.ಒ.ಡಿ/IOD", centeralign2(":", 0), rightspacing("520.01", 14), tfNumber, 20, 6);
//            printtext2(canvas, "ಸರ್ಕಾರದ ಸಹಾಯಧನ/GOK Subsidy", centeralign2(":", 0), rightspacing("3.64", 14), tfNumber, 20, 6);
//            printboldtext1(canvas, "Net Amt Due", centeralign2(":", 0), rightspacing("978950.00", 11), tfNumber, 25, 6);


            printtext2(canvas, "ಪಾವತಿ ಕೊನೆ ದಿನಾಂಕ/Due Date", centeralign2(":", 0), space1("15/08/2018", 5), tfNumber, 20, 6);
            printtext2(canvas, "ಬಿಲ್ ದಿನಾಂಕ/Billed On", centeralign2(":", 0), space3(currentDateandTime(), 3), tfNumber, 20, 6);
            printtext2(canvas, "ಮಾ.ಓ.ಸಂಕೇತ/Mtr.Rdr.Code", centeralign2(":", 0), space3("54003818", 4), tfNumber, 20, 6);

            canvas.DrawBitmap(barcode, 0, yaxis, 0);
            canvas.CanvasEnd();
            canvas.CanvasPrint(1, 0);
            bPrintResult = canvas.GetIO().IsOpened();
            return bPrintResult;

        }


        private void printtext(Canvas canvas, String text, String text1, String text2, Typeface tfNumber, float textsize, float axis) {
            yaxis++;
            canvas.DrawText(text, 0, yaxis, 0, tfNumber, textsize, Canvas.DIRECTION_LEFT_TO_RIGHT);
            canvas.DrawText(text1, 310, yaxis, 0, tfNumber, textsize, Canvas.DIRECTION_LEFT_TO_RIGHT);
            canvas.DrawText(text2 + "\r\n", 325, yaxis, 0, tfNumber, textsize, Canvas.DIRECTION_LEFT_TO_RIGHT);
            if (textsize == 20) {
                yaxis = yaxis + textsize + 8;
            } else yaxis = yaxis + textsize + 6;
            yaxis = yaxis + axis;
        }

        private void printtext2(Canvas canvas, String text, String text2, String text3, Typeface tfNumber, float textsize, float axis) {
            yaxis++;
            canvas.DrawText(text, 0, yaxis, 0, tfNumber, textsize, Canvas.DIRECTION_LEFT_TO_RIGHT);
            canvas.DrawText(text2, 310, yaxis, 0, tfNumber, textsize, Canvas.DIRECTION_LEFT_TO_RIGHT);
            canvas.DrawText(text3 + "\r\n", 350, yaxis, 0, tfNumber, textsize, Canvas.DIRECTION_LEFT_TO_RIGHT);
            if (textsize == 20) {
                yaxis = yaxis + textsize + 8;
            } else yaxis = yaxis + textsize + 6;
            yaxis = yaxis + axis;
        }


        private void printtext_center(Canvas canvas, String text, Typeface tfNumber, float textsize, float axis) {
            yaxis++;
            canvas.DrawText(text + "\r\n", 120, yaxis, 0, tfNumber, textsize, Canvas.DIRECTION_LEFT_TO_RIGHT);
            if (textsize == 20) {
                yaxis = yaxis + textsize + 8;
            } else yaxis = yaxis + textsize + 6;
            yaxis = yaxis + axis;
        }

        private void printboldtext(Canvas canvas, String text, Typeface tfNumber, float textsize) {
            yaxis++;
            canvas.DrawText(text + "\r\n", 0, yaxis, 0, tfNumber, textsize, Canvas.FONTSTYLE_BOLD);
            if (textsize == 20) {
                yaxis = yaxis + textsize + 8;
            } else yaxis = yaxis + textsize + 6;
            yaxis = yaxis + (float) 6;
        }

        private void printboldtext1(Canvas canvas, String text, String text1, String text2, Typeface tfNumber, float textsize, float axis) {
            yaxis++;
            canvas.DrawText(text, 0, yaxis, 0, tfNumber, textsize, Canvas.FONTSTYLE_BOLD);
            canvas.DrawText(text1, 0, yaxis, 0, tfNumber, textsize, Canvas.FONTSTYLE_BOLD);
            canvas.DrawText(text2 + "\r\n", 350, yaxis, 0, tfNumber, textsize, Canvas.FONTSTYLE_BOLD);
            if (textsize == 20) {
                yaxis = yaxis + textsize + 8;
            } else yaxis = yaxis + textsize + 6;
            yaxis = yaxis + axis;
        }

        private String rightspacing(String s1, int len) {
            int i;
            StringBuilder s1Builder = new StringBuilder(s1);
            for (i = 0; i < len - s1Builder.length(); i++) {
            }
            s1Builder.insert(0, String.format("%" + i + "s", ""));
            s1 = s1Builder.toString();
            return (s1);
        }

        private String rightspacing2(String s1, int len) {
            StringBuilder s1Builder = new StringBuilder(s1);
            for (int i = 0; i < len - s1Builder.length(); i++) {
                s1Builder.insert(0, " ");
            }
            s1 = s1Builder.toString();
            return (s1);
        }

        private String centeralign(String text, int width) {
            int count = text.length();
            int value = width - count;
            int append = (value / 2);
            return space(" ", append) + text;
        }

        private String centeralign2(String text, int width) {
            int count = text.length();
            int value = width - count;
            int append = (value / 2);
            //return space1(" ", append) + text;
            return space1(String.format("%s", ""), append) + text;
        }

        private String space(String s, int length) {
            int temp;
            StringBuilder spaces = new StringBuilder();
            temp = length - s.length();
            for (int i = 0; i < temp; i++) {
                spaces.append(" ");
            }
            return (s + spaces);
        }

        private String space1(String s, int length) {
            int temp;
            StringBuilder spaces = new StringBuilder();
            temp = length - s.length();
            for (int i = 0; i < temp; i++) {
                spaces.insert(0, String.format("%" + i + "s", ""));
            }
            return (s + spaces);
        }

        private String space3(String s, int length) {
            StringBuilder spaces = new StringBuilder();
            spaces.insert(0, String.format("%" + length + "s", ""));
            return (s + spaces);
        }

        private String currentDateandTime() {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            return sdf.format(new Date());
        }

    }

    public void splitString(String msg, int lineSize, ArrayList<String> arrayList) {
        arrayList.clear();
        Pattern p = Pattern.compile("\\b.{0," + (lineSize - 1) + "}\\b\\W?");
        Matcher m = p.matcher(msg);
        while (m.find()) {
            arrayList.add(m.group().trim());
        }
    }

    private Bitmap getBitmap(String barcode) {
        Bitmap barcodeBitmap = null;
        BarcodeFormat barcodeFormat = convertToZXingFormat();
        try {
            barcodeBitmap = encodeAsBitmap(barcode, barcodeFormat);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return barcodeBitmap;
    }

    private BarcodeFormat convertToZXingFormat() {
        switch (1) {
            case 8:
                return BarcodeFormat.CODABAR;
            case 1:
                return BarcodeFormat.CODE_128;
            case 2:
                return BarcodeFormat.CODE_39;
            case 4:
                return BarcodeFormat.CODE_93;
            case 32:
                return BarcodeFormat.EAN_13;
            case 64:
                return BarcodeFormat.EAN_8;
            case 128:
                return BarcodeFormat.ITF;
            case 512:
                return BarcodeFormat.UPC_A;
            case 1024:
                return BarcodeFormat.UPC_E;
            default:
                return BarcodeFormat.CODE_128;
        }
    }


    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    private Bitmap encodeAsBitmap(String contents, BarcodeFormat format) throws WriterException {
        if (contents == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contents);
        if (encoding != null) {
            hints = new EnumMap(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contents, format, 450, 45, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

    private void printanalogics() {
        StringBuilder stringBuilder = new StringBuilder();
        analogicsprint(aligncenter("Bangalore", 30), 6);
        analogicsprint(aligncenter("(" + "540038" + ")", 30), 6);
        stringBuilder.append(space("RRNO", 16) + ":" + " " + RRNO + "\n");
        stringBuilder.append(space("Account ID", 16) + ":" + " " + customerID);
        analogics_double_print(stringBuilder.toString(), 6);
        analogicsprint(space("Mtr.Rdr.Code", 16) + ":" + " " + "54008301", 6);
        stringBuilder.setLength(0);
        stringBuilder.append("\n");
        analogicsprint(stringBuilder.toString(), 6);
        analogics_48_print("Transvision Software", 3);
        analogics_48_print("Peenya 2nd Stage", 3);
        analogics_48_print("Bengaluru", 6);
        analogicsprint(space("Tariff", 16) + ":" + " " + "5LT2A2", 6);
        analogicsprint(space("Sanct Load", 14) + ":" + "HP:" + alignright("0", 4) + " " + "KW:" + alignright(load, 4), 6);
//        analogicsprint(space("Billing", 8) + ":" + "10/10/2017" + "-" + "10/11/2017", 6);
//        analogicsprint(space("Reading Date", 12) + ":" + " " + "10/11/2017", 6);
//        analogicsprint(space("BillNo", 7) + ":" + " " + "1234567890" + "-" + "10/11/2017", 6);
        analogicsprint(space("Meter SlNo.", 16) + ":" + " " + "5000101010", 6);
        analogicsprint(space("Pres Rdg", 11) + ":" + " " + presentReading, 6);
        analogicsprint(space("Prev Rdg", 11) + ":" + " " + previousReading, 6);
        analogicsprint(space("Constant", 16) + ":" + " " + constant, 6);
        analogicsprint(space("Consumption", 16) + ":" + " " + consumption, 6);
        analogicsprint(space("Recorded MD", 16) + ":" + " " + bmdValue, 6);
        analogicsprint(space("PF Value", 16) + ":" + " " + pfValue, 6);


//        analogicsprint(space("Consumption", 16) + ":" + " " + "250", 6);
//        analogicsprint(space("Average", 16) + ":" + " " + "250", 6);
//        analogicsprint(space("Recorded MD", 16) + ":" + " " + "1", 6);
//        analogicsprint(space("Power Factor", 16) + ":" + " " + "0.75", 6);
        stringBuilder.setLength(0);
        stringBuilder.append("\n");

        analogicsprint(aligncenter("Fixed Charges", 30), 6);
        analogicsprint(aligncenter(fc1, 30), 6);
        analogicsprint(aligncenter(fc2, 30), 6);

        analogicsprint(aligncenter("Energy Charges", 30), 5);
        analogicsprint(aligncenter(ec1, 30), 6);
        analogicsprint(aligncenter(ec2, 30), 6);
        analogicsprint(aligncenter(ec3, 30), 6);
        analogicsprint(aligncenter(ec4, 30), 6);

        analogicsprint(space("BMD Penalty", 16) + ":" + " " + bmd1, 6);
        analogicsprint(space("PF Penalty", 16) + ":" + " " + pf1, 6);
        analogicsprint(space("Tax (9%)", 16) + ":" + " " + tax1, 6);
        analogicsprint(space("Rebate(-)", 16) + ":" + " " + rebate1, 6);
        analogicsprint(space("Cur Bill Amt", 16) + ":" + " " + currentAmt, 6);


//        stringBuilder.append(space("PaymentMode", 10) + ":" + " " + "CASH" + "\n");
//        stringBuilder.append(space("ReceiptDate", 10) + ":" + " " + currentDateandTime() + "\n");
//        stringBuilder.append(space("MeterReader Name", 10) + ":" + " " + "TVD" + "\n");
        analogics_double_print(space("Net Amt Due", 12) + ":" + " " + alignright("₹" + netBillAmt, 16), 0);
        print_bar_code("ANA1234567");
        stringBuilder.append("234567854" + "-" + currentDateandTime() + "\n");

        stringBuilder.setLength(0);
        stringBuilder.append("\n");
        stringBuilder.append("\n");
        stringBuilder.append("\n");
        analogicsprint(stringBuilder.toString(), 4);


    }

    public void analogicsprint(String Printdata, int feed_line) {
        conn.printData(api.font_Courier_30_VIP(Printdata));
        text_line_spacing(feed_line);
    }

    public void analogics_double_print(String Printdata, int feed_line) {
        conn.printData(api.font_Double_Height_On_VIP());
        analogicsprint(Printdata, feed_line);
        conn.printData(api.font_Double_Height_Off_VIP());
    }

    public void analogics_48_print(String Printdata, int feed_line) {
        conn.printData(api.font_Courier_48_VIP(Printdata));
        text_line_spacing(feed_line);
    }

    public void text_line_spacing(int space) {
        conn.printData(api.variable_Size_Line_Feed_VIP(space));
    }

    private void print_bar_code(String msg) {
        String feeddata = "";
        feeddata = api.barcode_Code_128_Alpha_Numerics_VIP(msg);
        conn.printData(feeddata);
    }

    private String alignright(String msg, int len) {
        for (int i = 0; i < len - msg.length(); i++) {
            msg = " " + msg;
        }
        msg = String.format("%" + len + "s", msg);
        return msg;
    }

    private String aligncenter(String msg, int len) {
        int count = msg.length();
        int value = len - count;
        int append = (value / 2);
        return space(" ", append) + msg + space(" ", append);
    }

    private String space(String s, int len) {
        int temp;
        StringBuilder spaces = new StringBuilder();
        temp = len - s.length();
        for (int i = 0; i < temp; i++) {
            spaces.append(" ");
        }
        return (s + spaces);
    }

    private String currentDateandTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String cdt = sdf.format(new Date());
        return cdt;
    }


}
