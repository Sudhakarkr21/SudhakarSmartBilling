package com.example.tvd.bmdandpfcal;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class fcall {
    double totalCharge;

  static public String filepath(String value) {
        File dir = new File(android.os.Environment.getExternalStorageDirectory(), "TRM1"
                + File.separator + value);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.toString();
    }

    static public File filestorepath(String value, String file) {
        File dir = new File(android.os.Environment.getExternalStorageDirectory(), "TRM1"
                + File.separator + value);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, File.separator + file);
    }

  static public int curBillAmt(double ec,double fc,double bmd,double pf,double tax,double rebate)
  {
      double curBillAmt = ec + fc + bmd +pf + tax-rebate;
      return (int)curBillAmt;
  }

  static public int netAmtDue(double curBillAmt,double arrears,double gok){
      double netAmt = curBillAmt + arrears - gok ;
      return (int)netAmt;
  }

   static public double GOK2(double consumption,double tariff1) {
        double consumption1 = consumption - 200.00 ;
        double consumption3 = 0;

       double totalCharge = 0;
        if(tariff1 == 23) {

                for (int i = 1; i <= 3; i++) {
                    double total = 0;

                    if (i == 1) {
                        if (consumption1 > 80) {
                            consumption3 = consumption1 - 80;
                            total = 80 * 0.1;
                            totalCharge = totalCharge + total;

                        } else {
                            total = consumption1 * 0.1;
                            totalCharge = totalCharge + total;

                        }
                    } else if (i == 2 && consumption1 > 80) {
                        if (consumption3 > 120) {
                            consumption3 = consumption3 - 120;
                            total = 120 * 1.82;
                            totalCharge = totalCharge + total;

                        } else {
                            total = consumption3 * 1.82;
                            totalCharge = totalCharge + total;

                        }
                    }  else {
                            total = consumption3 * 7.55;
                            totalCharge = totalCharge + total;

                        }


                }


        }
        return totalCharge;
    }

    public static String getJSONresult(Cursor cursor) {
//        if (cursor.getCount() > 1)
            return getJSONArray(cursor);
//        else return getJSONObject(cursor);
    }

    public static String getJSONArray(Cursor cursor) {
        JSONArray resultSet = new JSONArray();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();
            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        if (cursor.getString(i) != null)
                            rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                        else rowObject.put(cursor.getColumnName(i), "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }
//        logStatus(resultSet.toString());
        Log.d("werty1",resultSet.toString());
        return resultSet.toString();
    }

         static String getJSONObject(Cursor cursor) {
        int totalColumn = cursor.getColumnCount();
        JSONObject rowObject = new JSONObject();
        cursor.moveToFirst();
        for (int i = 0; i < totalColumn; i++) {
            if (cursor.getColumnName(i) != null) {
                try {
                    if (cursor.getString(i) != null)
                        rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                    else rowObject.put(cursor.getColumnName(i), "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
//        logStatus(rowObject.toString());
        Log.d("ertyg",rowObject.toString());
        return rowObject.toString();
    }

    /*public tariff_conf getTariffConfig(String cursor) {
        return new Gson().fromJson(cursor, tariff_conf.class);
    }*/

//    List<MastOut> getTariffConfig() {
//        return Arrays.asList(new Gson().fromJson(getMastOut(), MastOut[].class));
//    }

   public static List<Tariff_config> getTariffConfig(String cursor) {
        return Arrays.asList(new Gson().fromJson(cursor, Tariff_config[].class));
    }
    public Tariff_config getTariffConfig2(String cursor) {
        return new Gson().fromJson(cursor, Tariff_config.class);
    }

    public static List<Mast_Cust> getTariffConfig1(String cursor) {
        return Arrays.asList(new Gson().fromJson(cursor, Mast_Cust[].class));
    }
  public static List<Subdiv_Details> getSubdiv(String cursor){
        return Arrays.asList(new Gson().fromJson(cursor,Subdiv_Details[].class));
    }

    public static void Calculation_textfile(String value, String cunsumer_ID) {
        String path = textfilepath("Calculation");
        String filename = cunsumer_ID + "_" + "Calculation_Report"  + ".txt";
        File log = new File(path + File.separator + filename);
        try {
            log.createNewFile();
            PrintWriter out = new PrintWriter(new FileWriter(log, true));
            out.append(value);
            out.append("\r\n");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String textfilepath(String value) {
        File dir = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath(), "TRM1" + File.separator + value);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.toString();
    }

    public static String dateSet() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int mnth2 = month + 1;
        String present_date1 = day + "/" + mnth2 + "/" + "" + year;
        Date date = null;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(present_date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        c.setTime(date);
        return sdf.format(c.getTime());
    }
    public static String getCursorValue(Cursor data, String column_name) {
        if (!TextUtils.isEmpty(data.getString(data.getColumnIndexOrThrow(column_name))))
            return data.getString(data.getColumnIndexOrThrow(column_name));
        else return "0";
    }

    public static void showprogress(String Message, ProgressDialog dialog, String Title) {
        dialog.setTitle(Title);
        dialog.setMessage(Message);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(false);
        dialog.show();
    }

    public static double convert_decimal(String value) {
        if (TextUtils.isEmpty(value))
            return 0;
        else return Double.parseDouble(value);
    }

    public static String changedateformat(String datevalue, String changedivider) {
        Date date = null;
        if ((datevalue.substring(datevalue.length() - 5, datevalue.length() - 4)).equals("/")) {
            try {
                date = new SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(datevalue);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            if ((datevalue.substring(datevalue.length() - 5, datevalue.length() - 4)).equals("-")) {
                try {
                    date = new SimpleDateFormat("dd-MM-yyyy", Locale.US).parse(datevalue);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                if (datevalue.length() == 8) {
                    try {
                        date = new SimpleDateFormat("dd-MM-yyyy", Locale.US).parse(datevalue.substring(0, 2) + "-"
                                + datevalue.substring(2, 4) + "-" + datevalue.substring(4));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String format = sdf.format(c.getTime());
        return format.substring(0, 2) + changedivider + format.substring(3, 5) + changedivider + format.substring(6, 10);
    }

    public static void logStatus(String message) {
        Log.d("debug", message);
    }

}
