package com.example.tvd.bmdandpfcal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import net.lingala.zip4j.core.ZipFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.content.ContentValues.TAG;
import static com.example.tvd.bmdandpfcal.values.Constant.DIR_DATABASE;
import static com.example.tvd.bmdandpfcal.values.Constant.FILE_TRM_DATABASE;
import static com.example.tvd.bmdandpfcal.values.Constant.ZIP_EXTRACT_PASSWORD;

public class Database  {

    //The Android's default system path of your application database.
    private static String DB_PATH = "TRM1"+File.separator+"database";

    private static String DB_NAME = "mydatabase.db";

    private SQLiteDatabase db;
    private MyHelper mh ;
      private  Context myContext;

      fcall fcall=new fcall();
    private String dwnfilepath;
    private String dwnfilename;
    private String dwnfileformat;
    private String path = fcall.filepath(DIR_DATABASE);
    private String DATABASE_PATH = path + File.separator;

    public Database(Context context) {
        this.myContext = context;
        try {
            // also check the extension of you db file
            File dbfile = new File(fcall.filepath("database") + File.separator + "mydatabase.db");
            if (dbfile.exists())
            {
//                Toast.makeText(context, "database exists", Toast.LENGTH_LONG).show();
            }
            else{
//                Toast.makeText(context, "cant find database", Toast.LENGTH_LONG).show();
            }
        } catch (SQLiteException e) {
            System.out.println("Database doesn't exist");
        }
        mh = new MyHelper(context, DB_NAME, null, 3);
    }
    public void openDataBase () throws SQLException{
        String path = DB_PATH +DB_NAME;
        db = SQLiteDatabase.openDatabase(com.example.tvd.bmdandpfcal.fcall.filepath("database") + File.separator + "mydatabase.db", null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    public void db_close() {
        mh.close();
    }

    public void CopyDataBaseFromAsset() throws IOException {
        InputStream in  = myContext.getAssets().open(DB_NAME);
        Log.e("sample", "Starting copying");
        String outputFileName = DB_PATH+DB_NAME;
        File databaseFile = new File( fcall.filepath("database") + File.separator + "mydatabase.db");
        // check if databases folder exists, if not create one and its subfolders
        if (!databaseFile.exists()){
            databaseFile.mkdir();
        }

        OutputStream out = new FileOutputStream(outputFileName);

        byte[] buffer = new byte[1024];
        int length;


        while ((length = in.read(buffer))>0){
            out.write(buffer,0,length);
        }
        Log.e("sample", "Completed" );
        out.flush();
        out.close();
        in.close();

    }
    public void deleteDb() {
        File file = new File(DB_PATH);
        if(file.exists()) {
            file.delete();
            Log.d(TAG, "Database deleted.");
        }
    }
    public boolean checkDataBase() {
        boolean checkDB = false;
        try {
            File file = new File(DB_PATH);
            checkDB = file.exists();
        } catch(SQLiteException e) {
            Log.d(TAG, e.getMessage());
        }
        return checkDB;
    }

    public void createDatabase(String localfilepath, String localfilename, String localfileformat) {
        dwnfilepath = localfilepath;
        dwnfilename = localfilename;
        dwnfileformat = localfileformat;

        boolean dbExist = checkDataBase();
        if (dbExist) {
            db_delete();
        }

        boolean dbExist1 = checkDataBase();
        if (!dbExist1) {
            try {
                mh.close();
                copyDataBase();
            } catch (IOException e) {
//                throw new Error("Error in copying database");
                e.printStackTrace();
            }

        }
    }
    public void db_delete(){
        delete_file(FILE_TRM_DATABASE);
    }

    private void delete_file(String file) {
        if (!TextUtils.isEmpty(file)) {
            File journal = new File(DATABASE_PATH + file);
            if (journal.exists()) {
                journal.delete();
            }
        }
    }
    private void copyDataBase() throws IOException {

        String outFileName = DATABASE_PATH + FILE_TRM_DATABASE;

        String source = dwnfilepath + dwnfilename;
        String sou =   dwnfilename.replace(".zip","");
        String destination = dwnfilepath;
        String password = ZIP_EXTRACT_PASSWORD;
        try {
            ZipFile zipFile = new ZipFile(source);
            if (zipFile.isEncrypted()) {
                zipFile.setPassword(password);
            }
            zipFile.extractAll(destination);
        }  catch (net.lingala.zip4j.exception.ZipException e) {
            e.printStackTrace();
        }

        File hh = new File(DATABASE_PATH);
        if (!hh.exists()) {
            hh.mkdirs();
        }

//        FunctionCall.move_files(source,dwnfilename,outFileName,FILE_TRM_DATABASE);
        OutputStream myOutput = new FileOutputStream(outFileName);
        InputStream myInput = new FileInputStream(destination + "/" + sou + ".db");
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myInput.close();
        myOutput.flush();
        myOutput.close();


    }


    public void insert_data(ContentValues cv) {
        db.insert("MAST_OUT", null, cv);
    }

    public class MyHelper extends SQLiteOpenHelper {
        MyHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if(newVersion>oldVersion)
                checkDataBase();
        }
    }


    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy){
        return db.query("MAST_CUST", null, null, null, null, null, null);
    }

    public Cursor getTarrifDataBJ( String consumer_id) {
        return db.rawQuery("select * from MAST_CUST where CONSNO = " + "'" + consumer_id +  "'" , null);
    }
    public Cursor getTarrifData( String rrno) {
        return db.rawQuery("select * from MAST_CUST where RRNO = " + "'" + rrno +   "'" , null);
    }

    public Cursor getTarrifDataCus(){
        return db.rawQuery("select * from MAST_CUST " ,null);
    }

    public Cursor getTarrifData1(String Tariff){
        return db.rawQuery("select * from TARIFF_CONFIG_CURRENT where TARIFF_CODE = " + "'" + Tariff + "'" ,null);
    }
    public Cursor getTarrifData2(String Tariff,String CustomerID){
        return db.rawQuery("select * from MAST_CUST where TARIFF = " + "'" + Tariff + "'and CONSNO = " + "'" + CustomerID + "'", null);
    }
    public String getTarrifData11(String Tariff) {
        return fcall.getJSONresult(db.rawQuery("select * from TARIFF_CONFIG_CURRENT where TARIFF_CODE = " + "'" + Tariff +"' and RUFLAG = " + "'" + "0" + "'", null));
    }
//    public Cursor getPresReadDate(){
//        return db.rawQuery("select * from MAST_CUST wh  ")
//    }

    public String getTarrifData12(String customer_id) {
        return fcall.getJSONresult(db.rawQuery("select * from MAST_CUST where CONSNO = " + "'" + customer_id + "'", null));
    }

    public String getTarrifDataID1(int _id) {
        return fcall.getJSONresult(db.rawQuery("select * from MAST_CUST where _id = " + "'" + _id + "'", null));
    }
    public Cursor getTarrifDataID(int _id) {
        return db.rawQuery("select * from MAST_CUST where _id = " + "'" + _id +  "'" , null);
    }

    public Cursor delete(String tariff){
        return db.rawQuery("delete from MAST_OUT where TARIFF = " + "'" + tariff + "'", null);

    }

    public Cursor billed() {
        return db.rawQuery("select * from MAST_OUT", null);
    }
    public Cursor collects1() {
        return db.rawQuery("SELECT 'MR : ' || MRCODE || ' RDG_DATE : ' || SUBSTR(MIN(READDATE),1,2) || ' To ' || MAX(READDATE)COL1,''COL2 FROM MAST_CUST", null);
    }
    public Cursor billstatus() {
        return db.rawQuery("select 'TOTAL'COL1, count(rrno)COL2 from MAST_CUST WHERE PREVSTAT != '' " +
                "UNION ALL " +
                "select 'BILLED'COL1, count(rrno)COL2 from MAST_OUT " +
                "UNION ALL " +
                "select 'NOT BILLED'COL1, ((SELECT count(rrno) from MAST_CUST WHERE PREVSTAT != '')-(SELECT count(rrno) from MAST_OUT))COL2 " +
                "UNION ALL " +
                "select '-----------------'COL1, '------'COL2 " +
                "UNION ALL " +
                "SELECT STATUS_NAME COL1, COUNT(RRNO)COL2 FROM BILLING_STATUS LEFT OUTER JOIN MAST_OUT ON STATUS_CODE =PRES_STS GROUP BY STATUS_CODE " +
                "UNION ALL " +
                "select 'TOTAL'COL1, count(rrno)COL2 from MAST_OUT", null);
    }
    public Cursor status(String status) {
        return db.rawQuery("select * from MAST_OUT where PRES_STS = (select STATUS_CODE from BILLING_STATUS where STATUS_NAME = " + "'" + status + "')", null);
    }
    public Cursor notbilled() {
        return db.rawQuery("SELECT * from MAST_CUST WHERE CONSNO not in (SELECT CONSNO from MAST_OUT) AND PREVSTAT != ''", null);
    }

    public Cursor spinnerData() {
        return db.rawQuery("Select STATUS_NAME from BILLING_STATUS", null);
    }

    public Cursor getBillingRecordData() {
        return db.rawQuery("SELECT * FROM MAST_CUST WHERE _id = (SELECT Billed_Record FROM SUBDIV_DETAILS)", null);
    }
    public String getMastCust_details() {
        return com.example.tvd.bmdandpfcal.fcall.getJSONArray(getBillingRecordData());
    }
    public String getSubdiv_details(){
        return com.example.tvd.bmdandpfcal.fcall.getJSONArray(getSubdiv_details1());
    }
    public Cursor getSubdiv_details1(){
        return db.rawQuery("Select * from SUBDIV_DETAILS",null);
    }
    public Cursor getBilled_details(String value) {
        return db.rawQuery("Select * from MAST_OUT where CONSNO = '" + value + "'", null);
    }
    public void updateDLrecord(double monthdiff) {
        String difference = String.valueOf(monthdiff);
        Cursor data = db.rawQuery("UPDATE MAST_CUST set DLCOUNT = '" + difference + "' WHERE _id = " +
                "(SELECT Billed_Record from SUBDIV_DETAILS)", null);
        data.moveToNext();
        data.close();
    }
    public String getTarrifDataBJ(String rRebate, String Tariff) {
        return com.example.tvd.bmdandpfcal.fcall.getJSONObject(db.rawQuery("select * from TARIFF_CONFIG_CURRENT where TARIFF_CODE = '" +
                Tariff + "'and RUFLAG = '" + rRebate + "'", null));
    }
    public String getTarrifDataBJ2(String rRebate, String Tariff) {
        return com.example.tvd.bmdandpfcal.fcall.getJSONObject(db.rawQuery("select * from TARIFF_CONFIG_CURRENT_OLD where TARIFF_CODE = '" +
                Tariff + "'and RUFLAG = '" + rRebate + "'", null));
    }
    public String getTarrifData(String rRNO, String rRebate) {
        return com.example.tvd.bmdandpfcal.fcall.getJSONObject(db.rawQuery("select * from TARIFF_CONFIG_CURRENT where TARIFF_CODE = " +
                "(select TARIFF from MAST_CUST where CONSNO = " + "'" + rRNO + "') and RUFLAG = (select RREBATE from MAST_CUST " +
                "where RREBATE = '" + rRebate + "')", null));
    }

    public String getTarrifData_old(String rRNO, String rRebate) {
        return com.example.tvd.bmdandpfcal.fcall.getJSONObject(db.rawQuery("select * from TARIFF_CONFIG_CURRENT_OLD where TARIFF_CODE = " +
                "(select TARIFF from MAST_CUST where CONSNO = " + "'" + rRNO + "') and RUFLAG = (select RREBATE from MAST_CUST " +
                "where RREBATE = '" + rRebate + "')", null));
    }

    public String getTotal_Records(){
        String count;
        Cursor cursor = db.rawQuery("select * from MAST_CUST ",null);
        count = String.valueOf(cursor.getCount());
        return count;
    }

    public void update_dlrecord(double record){
        String dl_diff = String.valueOf(record);
        Cursor cursor = db.rawQuery("update MAST_CUST set DLCOUNT = '" + dl_diff + "' where _id = "+
                "(select Billed_Record from SUBDIV_DETAILS)",null);
        cursor.moveToNext();
        cursor.close();
    }

}
