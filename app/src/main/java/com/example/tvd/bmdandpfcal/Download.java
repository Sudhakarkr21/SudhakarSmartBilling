package com.example.tvd.bmdandpfcal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.example.tvd.bmdandpfcal.values.Constant.DIR_DATABASE;
import static com.example.tvd.bmdandpfcal.values.Constant.DOWNLOAD_COMPLETED;
import static com.example.tvd.bmdandpfcal.values.Constant.DOWNLOAD_FAILED;
import static com.example.tvd.bmdandpfcal.values.Constant.FILE_TRM_DATABASE;
import static com.example.tvd.bmdandpfcal.values.Constant.FTP_HOST;
import static com.example.tvd.bmdandpfcal.values.Constant.FTP_PASS;
import static com.example.tvd.bmdandpfcal.values.Constant.FTP_PORT;
import static com.example.tvd.bmdandpfcal.values.Constant.FTP_USER;


public class Download {

    private String mobileFilePathDwnld, serverDwnloadFilePath;
    private String serverUploadFilePath;
    private String fileZipFormat;
    private String file_name_DWN;
    static boolean dwnldCmplt = false;
    Handler handler;


    static boolean Downloadcompleted = false;

    @SuppressLint("StaticFieldLeak")
    private static Context con;

    Database database = new Database(con);


    public void runDownLoadTask(String mobilefilepathDWN, String filenameDWN, String zipformat,
                                String serverDWNfilepath, Handler handler1) {
        mobileFilePathDwnld = mobilefilepathDWN;
        file_name_DWN = filenameDWN;
        fileZipFormat = zipformat;
        serverDwnloadFilePath = serverDWNfilepath;
        handler = handler1;
        new MyTask().execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class MyTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            FTPClient client = new FTPClient();
            FileOutputStream fos = null;
            try {
                client.connect(FTP_HOST, FTP_PORT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                client.login(FTP_USER, FTP_PASS);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                client.setFileType(FTP.BINARY_FILE_TYPE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            client.enterLocalPassiveMode();
            try {
                Log.d("file_name_DWN", file_name_DWN);
                fos = new FileOutputStream(mobileFilePathDwnld + file_name_DWN );
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                dwnldCmplt = client.retrieveFile(serverDwnloadFilePath + file_name_DWN , fos);
//                unpackZip(serverDwnloadFilePath,file_name_DWN);

//                OutputStream outputStream2 = new BufferedOutputStream(new FileOutputStream(mobileFilePathDwnld + file_name_DWN));
//                InputStream inputStream = client.retrieveFileStream(serverDwnloadFilePath + file_name_DWN);
//                byte[] bytesArray = new byte[4096];
//                int bytesRead = -1;
//                while ((bytesRead = inputStream.read(bytesArray)) != -1) {
//                    outputStream2.write(bytesArray, 0, bytesRead);
//                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) {
                    fos.close();
                }
                client.logout();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (dwnldCmplt) {
             copyDwnFolderToInterMem();
            }
            super.onPostExecute(result);
        }
    }

    private void copyDwnFolderToInterMem() {
        Log.d("mobileFilePathDwnld", mobileFilePathDwnld);
        Log.d("file_name_DWN", file_name_DWN);
        Log.d("fileZipFormat", fileZipFormat);
        database.createDatabase(mobileFilePathDwnld ,file_name_DWN , fileZipFormat);
        File mydb = fcall.filestorepath(DIR_DATABASE, FILE_TRM_DATABASE);
        if (mydb.exists()) {
            try {
                database.openDataBase();
                Downloadcompleted = true;
                handler.sendEmptyMessage(DOWNLOAD_COMPLETED);
            }
            catch(SQLException sqle) {
                sqle.printStackTrace();
            }
        } else {
            handler.sendEmptyMessage(DOWNLOAD_FAILED);
        }
    }



}
