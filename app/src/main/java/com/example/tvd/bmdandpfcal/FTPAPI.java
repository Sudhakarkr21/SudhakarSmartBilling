package com.example.tvd.bmdandpfcal;

import android.content.Context;
import android.os.Handler;

import com.example.tvd.bmdandpfcal.values.Constant;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.tvd.bmdandpfcal.values.Constant.DIR_FTP_DOWNLOAD;
import static com.example.tvd.bmdandpfcal.values.Constant.DOWNLOAD_FILE_FOUND;
import static com.example.tvd.bmdandpfcal.values.Constant.DOWNLOAD_FILE_NOT_FOUND;

class FTPAPI {
    private String FTP_USER, FTP_PASS, FTP_HOST ;
    private int FTP_PORT;
    FTPAPI(Context context) {
        server_links();
    }

    private void server_links() {
        FTP_USER = Constant.FTP_USER;
        FTP_PASS = Constant.FTP_PASS;
        FTP_HOST = Constant.FTP_HOST;
        FTP_PORT = Constant.FTP_PORT;

    }

    public class Check_download_available_file implements Runnable {
        String serverpath, mrcode, file_date, log_name;
        Handler handler;
        Mast_Cust mast_cust;
        boolean checkfile = false, filefound = false;

        Check_download_available_file(String mrcode, String file_date, Handler handler, Mast_Cust mast_cust) {
            this.mrcode = mrcode;
            this.file_date = file_date;
            this.handler = handler;
            this.mast_cust = mast_cust;
        }

        @Override
        public void run() {
            log_name = "Download_check :";
            serverpath = DIR_FTP_DOWNLOAD + mrcode.substring(0, 6) + "/" + fcall.changedateformat(file_date, "") + "/";
            fcall.logStatus("Server Path: "+serverpath);

            fcall.logStatus(log_name+" 1");
            FTPClient ftp_1 = new FTPClient();
            fcall.logStatus(log_name+" 2");
            try {
                fcall.logStatus(log_name+" 3");
                ftp_1.connect(FTP_HOST, FTP_PORT);
                fcall.logStatus(log_name+" 4");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fcall.logStatus(log_name+" 5");
                ftp_1.login(FTP_USER, FTP_PASS);
                fcall.logStatus("FTP Login Status: "+ftp_1.login(FTP_USER, FTP_PASS));
                checkfile = ftp_1.login(FTP_USER, FTP_PASS);
                fcall.logStatus(log_name+" 6");
            } catch (FTPConnectionClosedException e) {
                e.printStackTrace();
                try {
                    checkfile = false;
                    ftp_1.disconnect();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (checkfile) {
                fcall.logStatus("Download check billing_file true....");
                try {
                    fcall.logStatus(log_name+" 7");
                    ftp_1.setFileType(FTP.BINARY_FILE_TYPE);
                    ftp_1.enterLocalPassiveMode();
                    fcall.logStatus(log_name+" 8");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fcall.logStatus(log_name+" 9");
                    ftp_1.changeWorkingDirectory(serverpath);
                    fcall.logStatus(log_name+" 10");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fcall.logStatus(log_name+" 11");
                    fcall.logStatus(log_name+"serverDwnloadFilePath: "+serverpath);
                    FTPFile[] ftpFiles = ftp_1.listFiles(serverpath);
                    fcall.logStatus(log_name+" 12");
                    int length = ftpFiles.length;
                    fcall.logStatus(log_name+" 13");
                    fcall.logStatus(log_name+"Download_length"+"length = " + length);
                    for (int i = 0; i < length; i++) {
                        String namefile = ftpFiles[i].getName();
                        fcall.logStatus(log_name+"["+(i+1)+"]"+" : "+namefile);
                        boolean isFile = ftpFiles[i].isFile();
                        if (isFile) {
                            fcall.logStatus(log_name+"_file "+"Download_file: " + find_regex(namefile));
                            if (find_regex(namefile).equals(mrcode)) {
                                filefound = true;
                                mast_cust.setDownloadFileName(namefile);
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            checkfile = false;
            if (filefound) {
                try {
                    ftp_1.logout();
                    handler.sendEmptyMessage(DOWNLOAD_FILE_FOUND);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    ftp_1.logout();
                    handler.sendEmptyMessage(DOWNLOAD_FILE_NOT_FOUND);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private String find_regex(String code) {
        Pattern pattern = Pattern.compile("(\\d{"+ 8 +"})");
        Matcher matcher = pattern.matcher(code);
        String val = "";
        if (matcher.find()) {
            val = matcher.group(1);
        }
        return val;
    }
}
