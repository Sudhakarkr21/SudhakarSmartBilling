package com.example.tvd.bmdandpfcal.values;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.tvd.bmdandpfcal.Database;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static com.example.tvd.bmdandpfcal.values.Constant.ZERO;


public class FunctionCall {
    public void logstatus(String value) {
        Log.d("debug", value+" length : "+value.length());
    }

    public String aligncenter(String msg, int len) {
        int count = msg.length();
        int value = len - count;
        int append = (value / 2);
        return space(" ", append) + msg + space(" ", append);
    }
    public String space(String s, int len) {
        int temp;
        StringBuilder spaces = new StringBuilder();
        temp = len - s.length();
        for (int i = 0; i < temp; i++) {
            spaces.append(" ");
        }
        return (s + spaces);
    }
    public String alignright(String msg, int len) {
        for (int i = 0; i < len - msg.length(); i++) {
            msg = " " + msg;
        }
        msg = String.format("%" + len + "s", msg);
        return msg;
    }
    public String alignright3(String msg, int len) {
        int i;
        StringBuilder stringBuilder = new StringBuilder(msg);
        for ( i = 0; i < len - msg.length(); i++) {
            //msg = String.format("%s"+msg," ");
        }
        stringBuilder.insert(0,String.format("%" + i + "s", " "));
        msg = stringBuilder.toString();
        return msg;
    }

    public String currentDateandTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);
        return sdf.format(new Date());
    }


    public String leftAppend(String str, int maxlen){
        StringBuilder retStr= new StringBuilder();
        if(str.length() < maxlen){
            for(int i=0;i<maxlen-str.length();i++){
                retStr.append(" ");
            }
            retStr.insert(0, str);
        }
        return retStr.toString();

    }


    public String rightAppend(String str, int maxlen){
        StringBuilder retStr= new StringBuilder();
        if(str.length() < maxlen){
            for(int i=0;i<maxlen-str.length();i++){
                retStr.append(" ");
            }
            retStr.append(str);
        }
        return retStr.toString();

    }

    public String line(int length) {
        StringBuilder sb5 = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb5.append("-");
        }
        return (sb5.toString());
    }

    public String empty(int length) {
        StringBuilder sb5 = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb5.append(" ");
        }
        return (sb5.toString());
    }

    public static void move_files(String source_path, String source_file, String destination_path, String destination_file) {
        File fromfile = new File(source_path + File.separator + source_file);
        File tofile = new File(destination_path + File.separator + destination_file);
        if (fromfile.exists())
            //noinspection ResultOfMethodCallIgnored
            fromfile.renameTo(tofile);
    }
    public String getCursorValue(Cursor data, String column_name) {
        if (check_column(data, column_name)) {
            if (!TextUtils.isEmpty(data.getString(data.getColumnIndexOrThrow(column_name))))
                return data.getString(data.getColumnIndexOrThrow(column_name));
            else return "0";
        } else return "0";
    }
    public String splitString(double value1) {
        if (value1 != 0) {
            String value = ""+value1;
            if (value.contains(".")) {
                DecimalFormat num = new DecimalFormat("##0.00");
                String[] result = value.split("\\.");
                if (result[1].length() > 2)
                    return num.format(convert_decimal(value));
                else return value;
            } else return value;
        } else return ZERO;
    }
    private boolean check_column(Cursor data, String column) {
        long result = data.getColumnIndex(column);
        return result != -1;
    }
    public String dateSet() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }
    public String decimalroundoff(String value) {
        BigDecimal a = new BigDecimal(convert_decimal(value));
        BigDecimal roundOff = a.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        return "" + roundOff;
    }

    public String decimalroundoff(double value) {
        BigDecimal a = new BigDecimal(value);
        BigDecimal roundOff = a.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        return "" + roundOff;
    }

    public Double convert_decimal(String value) {
        return Double.parseDouble(value);
    }
    public int convert_int(String bill_billed_status) {
        return  Integer.parseInt(bill_billed_status);
    }
    public double getBigdecimal(double value, int decimal) {
        return new BigDecimal(value).setScale(decimal, RoundingMode.HALF_EVEN).doubleValue();
    }

    public void showtoast(Context context, String Message) {
        Toast.makeText(context, Message, Toast.LENGTH_SHORT).show();
    }

    public boolean isDeviceSupportCamera(Context context) {
        // this device has a camera
        // no camera on this device
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }
    public Bitmap getImage(String path, Context con) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int srcWidth = options.outWidth;
        int srcHeight = options.outHeight;
        int[] newWH = new int[2];
        newWH[0] = srcWidth / 2;
        newWH[1] = (newWH[0] * srcHeight) / srcWidth;

        int inSampleSize = 1;
        while (srcWidth / 2 >= newWH[0]) {
            srcWidth /= 2;
            srcHeight /= 2;
            inSampleSize *= 2;
        }

        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inSampleSize = inSampleSize;
        options.inScaled = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap sampledSrcBitmap = BitmapFactory.decodeFile(path, options);
        ExifInterface exif = new ExifInterface(path);
        String s = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        System.out.println("Orientation>>>>>>>>>>>>>>>>>>>>" + s);
        Matrix matrix = new Matrix();
        float rotation = rotationForImage(con, Uri.fromFile(new File(path)));
        if (rotation != 0f) {
            matrix.preRotate(rotation);
        }
        return Bitmap.createBitmap(
                sampledSrcBitmap, 0, 0, sampledSrcBitmap.getWidth(), sampledSrcBitmap.getHeight(), matrix, true);
    }


    private float rotationForImage(Context context, Uri uri) {
        if (Objects.equals(uri.getScheme(), "content")) {
            String[] projection = {MediaStore.Images.ImageColumns.ORIENTATION};
            Cursor c = context.getContentResolver().query(
                    uri, projection, null, null, null);
            if (c != null && c.moveToFirst()) {
                return c.getInt(0);
            }
            if (c != null) {
                c.close();
            }
        } else if (Objects.requireNonNull(uri.getScheme()).equals("file")) try {
            ExifInterface exif = new ExifInterface(uri.getPath());
            return (int) exifOrientationToDegrees(
                    exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0f;
    }

    private static float exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }
    public File filestorepath(String value, String file) {
        File dir = new File(android.os.Environment.getExternalStorageDirectory(), "TRM1"
                + File.separator + value);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, File.separator + file);
    }
    public String filepath(String value) {
        File dir = new File(android.os.Environment.getExternalStorageDirectory(), "TRM1"
                + File.separator + value);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.toString();
    }

    public void logStatus(String msg) {
        Log.d("debug", msg);
    }

    public String get_month_date_decreased(String date, int month, int days) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date month_date = null;
        try {
            month_date = formatter.parse(changedateformat(date, "-"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(month_date);
        calendar.add(Calendar.MONTH,  month);
        calendar.add(Calendar.DATE,  days);
        return formatter.format(calendar.getTime());
    }
    public String changedateformat(String datevalue, String changedivider) {
        Date date = null;
        if ((datevalue.substring(datevalue.length() - 5, datevalue.length() - 4)).equals("/")) {
            try {
                date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(datevalue);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            if ((datevalue.substring(datevalue.length() - 5, datevalue.length() - 4)).equals("-")) {
                try {
                    date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(datevalue);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                if (datevalue.length() == 8) {
                    try {
                        date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(datevalue.substring(0, 2) + "-"
                                + datevalue.substring(2, 4) + "-" + datevalue.substring(4));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String format = sdf.format(c.getTime());
        return format.substring(0, 2) + changedivider + format.substring(3, 5) + changedivider + format.substring(6, 10);
    }

    public String calculateDays(String Prev_date, String Pres_date) {
        Prev_date = changedateformat(Prev_date, "/");
        Pres_date = changedateformat(Pres_date, "/");
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date Date1 = null;
        Date Date2 = null;
        try {
            Date1 = format.parse(Prev_date);
            Date2 = format.parse(Pres_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long result = 0;
        if (Date2 != null) {
            result = (Date2.getTime() - Date1.getTime()) / (24 * 60 * 60 * 1000);
        }
        return "" + result;
    }

    public Database check_billing_database(Context context){
        Database database = new Database(context);
        try {
            if (database.checkDataBase()) {
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
        return database;
    }
}

