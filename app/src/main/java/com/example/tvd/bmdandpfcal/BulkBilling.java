package com.example.tvd.bmdandpfcal;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BulkBilling extends AppCompatActivity {
    TextView totalRecords, tobill, completed;
    Button bulkbilling;
    Database database;
    List<Mast_Cust> mast_custs;
    List<tariff_conf> tariff_confs;
    fcall fcall1;
    tariff_conf tariffConf;
    Mast_Cust mast_cust;
    int _id = 1, a = 0;
    Cursor c2 = null;
    int i;
    Calucation calucation;
    double consumption;

    AlertDialog progress_dialog;
    double rmdvalue1 = 0,
            constant1 = 0,
            load1 = 0,
            presentReading1 = 0,
            previousReading1 = 0,
            e = 0,
            f = 0,
            pfvalue1 = 0,
            rebate1 = 0,
            bmdpenalty = 0;
    String bmdpenalty1 = "0",
            t1 = "0",
            pfP10 = "0",
            rebate = "0",
            tariff2 = "0",
            constant2 = "0",
            currOnPeak2 = "0",
            currNormalPeak2 = "0",
            currOffPeak2 = "0",
            netAmtDue2 = "0",
            gok3 = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bulkbilling);
        database = new Database(this);
        fcall1 = new fcall();
        mast_custs = new ArrayList<Mast_Cust>();
        tariff_confs = new ArrayList<tariff_conf>();
        tariffConf = new tariff_conf();
        mast_cust = new Mast_Cust();

        try {
            database.openDataBase();
            // after open data  base u can read write data base
        } catch (Exception e) {
            e.printStackTrace();
        }
        init();
        c2 = database.getTarrifDataCus();
        a = c2.getCount();
        Log.d("fwfaf", String.valueOf(a));
        totalRecords.setText(String.valueOf(a));
        bulkbilling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Tarrif_configuration();
            }
        });


    }

    private void update_count(int max_count) {
        tobill.setText("" + max_count);
    }

    public void init() {
        totalRecords = findViewById(R.id.totalrecods);
        tobill = findViewById(R.id.tobill);
        completed = findViewById(R.id.completed);
        bulkbilling = findViewById(R.id.startbilling);
    }

    public void Tarrif_configuration() {
        int c = _id;

        for (i = 0; i < a; i++) {


            update_count(_id - i);
            mast_custs = (fcall.getTariffConfig1(database.getTarrifDataID1(_id)));
            String S = mast_custs.get(0).getTARIFF();
//            tariff_confs = (fcall.getTariffConfig(database.getTarrifData11(S)));
            gettotalCalculation();
            put();
            _id++;


        }

    }

    public void gettotalCalculation() {

        calucation = new Calucation(tariff_confs, mast_custs);
        rmdvalue1 = Double.parseDouble(mast_custs.get(0).getBMDVAL());
        constant1 = Double.parseDouble(mast_custs.get(0).getMF());
        load1 = Double.parseDouble(mast_custs.get(0).getSANCKW());
        presentReading1 = Double.parseDouble(mast_custs.get(0).getPRES_RDG());
        previousReading1 = Double.parseDouble(mast_custs.get(0).getPRVRED());
        pfvalue1 = Double.parseDouble(mast_custs.get(0).getPFVAL());
        rebate1 = 0;
        bmdpenalty1 = "0";
        t1 = "0";
        pfP10 = "0";
        rebate = mast_custs.get(0).getRREBATE();
        tariff2 = mast_custs.get(0).getTARIFF();
        constant2 = mast_custs.get(0).getMF();
        currOnPeak2 = mast_custs.get(0).getTOD_CURRENT1();
        currNormalPeak2 = "0";
        currOffPeak2 = mast_custs.get(0).getTOD_CURRENT3();

        int tariff1 = Integer.parseInt(mast_custs.get(0).getTARIFF());

        bmdpenalty = calucation.bmdPenalty(rmdvalue1, constant1, load1);
        bmdpenalty1 = String.format("%.2f", bmdpenalty);
        Log.d("debug1", bmdpenalty1);


        consumption = (presentReading1 - previousReading1) * constant1;

        e = calucation.EC(consumption);

        f = calucation.fixedRate1(load1);


        double t = calucation.tax(e);
        t1 = String.format("%.2f", t);

//        double prePaid=0.0;
//        if(noOfdays1>0) {
//            prePaid = calucation.prepaidRent(noOfdays1);
//
//        }
//        double gst1=0.0;
//        if(prePaid>0) {
//            gst1 = calucation.GST(prePaid);
//
//        }

        double pfPenalty = calucation.pfPenalty(pfvalue1, consumption);
        pfP10 = String.format("%.2f", pfPenalty);


        double e23 = 0.0, gok2 = 0.0;
        if (tariff1 == 23 || tariff1 == 31 || rebate == "5" || rebate == "10" || rebate == "12") {
            e23 = fcall.GOK2(consumption, tariff1);//23 Tariff
            gok2 = calucation.GOK(f, consumption, tariff1, e, e23, rebate, t, bmdpenalty, pfPenalty);
            gok3 = String.format("%.2f", gok2);

        }

        if (!rebate.equals("0")) {
            rebate1 = calucation.rebates_bill(consumption, e, f, rebate);


        }

        if (tariff2.equals("50") || tariff2.equals("52") || tariff2.equals("51") || tariff2.equals("53")) {
            String TodCal = String.valueOf(calucation.todCalucalation(constant2, currOnPeak2, currNormalPeak2, currOffPeak2));

        }

        double curbillAmt = fcall.curBillAmt(e, f, bmdpenalty, pfPenalty, t, rebate1);
        String cur1 = String.format("%.2f", curbillAmt);


        double arrears = Double.parseDouble(mast_custs.get(0).getARREARS());

        double netAmtDue1 = fcall.netAmtDue(curbillAmt, arrears, gok2);
        netAmtDue2 = String.format("%.2f", netAmtDue1);
    }

    public void put() {

        ContentValues cv = new ContentValues();
        cv.put("MONTH", mast_custs.get(0).getMONTH());
        cv.put("READDATE", mast_custs.get(0).getREADDATE());
        if (TextUtils.isEmpty(mast_custs.get(0).getRRNO()))
            cv.put("RRNO", "0");
        else cv.put("RRNO", mast_custs.get(0).getRRNO());
        cv.put("NAME", mast_custs.get(0).getNAME());
        if (TextUtils.isEmpty(mast_custs.get(0).getADD1()))
            cv.put("ADD1", " ");
        else cv.put("ADD1", mast_custs.get(0).getADD1());

        cv.put("TARIFF", mast_custs.get(0).getTARIFF());

        cv.put("MF", mast_custs.get(0).getMF());

        cv.put("PREVSTAT", mast_custs.get(0).getPREVSTAT());

        cv.put("AVGCON", mast_custs.get(0).getAVGCON());

        cv.put("LINEMIN", mast_custs.get(0).getLINEMIN());

        cv.put("SANCHP", mast_custs.get(0).getSANCHP());
        cv.put("SANCKW", mast_custs.get(0).getSANCKW());
        cv.put("PRVRED", mast_custs.get(0).getPRVRED());
        cv.put("FR", mast_custs.get(0).getFR());
        cv.put("IR", mast_custs.get(0).getIR());

        cv.put("DLCOUNT", mast_custs.get(0).getDLCOUNT());
        cv.put("ARREARS", mast_custs.get(0).getARREARS());

        if (TextUtils.isEmpty(mast_custs.get(0).getPF_FLAG()))
            cv.put("PF_FLAG", "0");
        else cv.put("PF_FLAG", mast_custs.get(0).getPF_FLAG());
        cv.put("BILLFOR", "0");
        cv.put("MRCODE", mast_custs.get(0).getMRCODE());
        cv.put("LEGFOL", mast_custs.get(0).getLEGFOL());
        if (TextUtils.isEmpty(mast_custs.get(0).getODDEVEN()))
            cv.put("ODDEVEN", "0");
        else cv.put("ODDEVEN", mast_custs.get(0).getODDEVEN());
        cv.put("SSNO", 0);
        cv.put("CONSNO", mast_custs.get(0).getCONSNO());
        cv.put("PH_NO", mast_custs.get(0).getPH_NO());
        cv.put("REBATE_FLAG", mast_custs.get(0).getREBATE_FLAG());
        cv.put("RREBATE", mast_custs.get(0).getRREBATE());

        cv.put("EXTRA1", mast_custs.get(0).getEXTRA1());
        cv.put("DATA1", mast_custs.get(0).getDATA1());

        cv.put("EXTRA2", mast_custs.get(0).getEXTRA2());
        cv.put("DATA2", mast_custs.get(0).getDATA2());
        cv.put("DEPOSIT", mast_custs.get(0).getDEPOSIT());
        cv.put("MTRDIGIT", mast_custs.get(0).getMTRDIGIT());
        cv.put("ASDAMT", mast_custs.get(0).getASDAMT());
        cv.put("IODAMT", mast_custs.get(0).getIODAMT());
        cv.put("PFVAL", mast_custs.get(0).getPFVAL());
        cv.put("BMDVAL", mast_custs.get(0).getBMDVAL());
        cv.put("BILL_NO", "0");
        cv.put("INTEREST_AMT", mast_custs.get(0).getINTEREST_AMT());
        cv.put("CAP_FLAG", mast_custs.get(0).getCAP_FLAG());
        cv.put("TOD_FLAG", mast_custs.get(0).getTOD_FLAG());
        cv.put("TOD_PREVIOUS1", mast_custs.get(0).getTOD_PREVIOUS1());
        cv.put("TOD_PREVIOUS3", mast_custs.get(0).getTOD_PREVIOUS3());
        cv.put("INT_ON_DEP", mast_custs.get(0).getINT_ON_DEP());

        cv.put("SO_FEEDER_TC_POLE", "0");
        cv.put("TARIFF_NAME", mast_custs.get(0).getTARIFF_NAME());


        cv.put("CHQ_DISHONOUR_DATE", "0");
        if (TextUtils.isEmpty(mast_custs.get(0).getFDRNAME()))
            cv.put("FDRNAME", "0");
        else cv.put("FDRNAME", mast_custs.get(0).getFDRNAME());
        if (TextUtils.isEmpty(mast_custs.get(0).getTCCODE()))
            cv.put("TCCODE", "0");
        else cv.put("TCCODE", mast_custs.get(0).getTCCODE());
        cv.put("MTR_FLAG", mast_custs.get(0).getMTR_FLAG());
        cv.put("PRES_RDG", mast_custs.get(0).getPRES_RDG());
        cv.put("PRES_STS", "0");
        cv.put("UNITS", String.valueOf(consumption));
        cv.put("FIX", String.valueOf(f));
        cv.put("ENGCHG", String.valueOf(e));
        cv.put("REBATE_AMOUNT", "0");
        if (!TextUtils.isEmpty(t1))
            cv.put("TAX_AMOUNT", t1);
        if (!TextUtils.isEmpty(bmdpenalty1))
            cv.put("BMD_PENALTY", bmdpenalty1);
        if (!TextUtils.isEmpty(pfP10))
            cv.put("PF_PENALTY", pfP10);
        if (!TextUtils.isEmpty(netAmtDue2))
            cv.put("PAYABLE", netAmtDue2);

        cv.put("BILLDATE", "0");
        cv.put("BILLTIME", "0");

        cv.put("TOD_CURRENT1", "0");
        cv.put("TOD_CURRENT3", "0");
        if (!TextUtils.isEmpty(gok3))
            cv.put("GOK_SUBSIDY", gok3);
        cv.put("DEM_REVENUE", "0");
        cv.put("GPS_LAT", "0");
        cv.put("GPS_LONG", "0");
        cv.put("ONLINE_FLAG", "0");
        cv.put("BATTERY_CHARGE", "0");
        cv.put("SIGNAL_STRENGTH", "0");
        cv.put("IMGADD", "NOIMAGE");
        cv.put("PAYABLE_REAL", netAmtDue2);

        database.insert_data(cv);

        String add = String.valueOf((_id));
        completed.setText(add);
    }
}
