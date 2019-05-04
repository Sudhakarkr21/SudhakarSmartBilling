package com.example.tvd.bmdandpfcal;

import android.support.annotation.NonNull;

public class GetSetValues implements Comparable<GetSetValues> {
    private String role="";
    private String account_id="";

    private double FAC_days=0;
    private double FAC_19mar_days = 0;
    private double FAC_18dec_days=0;
    private double FAC_18zero_days=0;
    private double FAC_18old_days=0;

    private String FAC_days_value="";
    private String FAC_19mar_value="";
    private String FAC_18dec_value="";
    private String FAC_18old_value="";

    private String PDPENALTY;
    private String PREPAID_RENT="0";
    private String PREPAID_RENT_GST="0";
    private String TOD_ONPEAK;
    private String TOD_NORMALPEAK;
    private String TOD_ONPEAK_1;
    private String TOD_OFFPEAK;
    private String CURRENT_BILL;
    private String DL_DAYS_COUNT;

    private String MRCODE;
    private String READDATE;

    private double[] arrFrate = new double[10];
    private double[] arrFslab = new double[10];
    private double[] arrErate = new double[10];
    private double[] arrEslab = new double[10];
    private double[] arrEc = new double[10];
    private double[] arrFc = new double[10];
    private double[] arrFrate_old = new double[10];
    private double[] arrFslab_old = new double[10];
    private double[] arrErate_old = new double[10];
    private double[] arrEslab_old = new double[10];
    private double[] arrEc_old = new double[10];
    private double[] arrFc_old = new double[10];
    private double[] arrdlFslab = new double[10];
    private double[] arrdlFslab_old = new double[10];

    public GetSetValues() {
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    @Override
    public int compareTo(@NonNull GetSetValues o) {
        return this.account_id.compareTo(o.account_id);
    }

    public double getFAC_days() {
        return FAC_days;
    }

    public void setFAC_days(double FAC_days) {
        this.FAC_days = FAC_days;
    }
    public double getFAC_19mar_days() {
        return FAC_19mar_days;
    }

    public void setFAC_19mar_days(double FAC_19mar_days) {
        this.FAC_19mar_days = FAC_19mar_days;
    }

    public double getFAC_18dec_days() {
        return FAC_18dec_days;
    }

    public void setFAC_18dec_days(double FAC_18dec_days) {
        this.FAC_18dec_days = FAC_18dec_days;
    }

    public double getFAC_18zero_days() {
        return FAC_18zero_days;
    }

    public void setFAC_18zero_days(double FAC_18zero_days) {
        this.FAC_18zero_days = FAC_18zero_days;
    }

    public double getFAC_18old_days() {
        return FAC_18old_days;
    }

    public void setFAC_18old_days(double FAC_18old_days) {
        this.FAC_18old_days = FAC_18old_days;
    }

    public String getFAC_days_value() {
        return FAC_days_value;
    }

    public void setFAC_days_value(String FAC_days_value) {
        this.FAC_days_value = FAC_days_value;
    }

    public String getFAC_19mar_value() {
        return FAC_19mar_value;
    }

    public void setFAC_19mar_value(String FAC_19mar_value) {
        this.FAC_19mar_value = FAC_19mar_value;
    }

    public String getFAC_18dec_value() {
        return FAC_18dec_value;
    }

    public void setFAC_18dec_value(String FAC_18dec_value) {
        this.FAC_18dec_value = FAC_18dec_value;
    }

    public String getFAC_18old_value() {
        return FAC_18old_value;
    }

    public void setFAC_18old_value(String FAC_18old_value) {
        this.FAC_18old_value = FAC_18old_value;
    }

    public String getPDPENALTY() {
        return PDPENALTY;
    }

    public void setPDPENALTY(String PDPENALTY) {
        this.PDPENALTY = PDPENALTY;
    }

    public String getPREPAID_RENT() {
        return PREPAID_RENT;
    }

    public void setPREPAID_RENT(String PREPAID_RENT) {
        this.PREPAID_RENT = PREPAID_RENT;
    }

    public String getPREPAID_RENT_GST() {
        return PREPAID_RENT_GST;
    }

    public void setPREPAID_RENT_GST(String PREPAID_RENT_GST) {
        this.PREPAID_RENT_GST = PREPAID_RENT_GST;
    }

    public double[] getArrFrate() {
        return arrFrate;
    }

    public void setArrFrate(double[] arrFrate) {
        this.arrFrate = arrFrate;
    }

    public double[] getArrFslab() {
        return arrFslab;
    }

    public void setArrFslab(double[] arrFslab) {
        this.arrFslab = arrFslab;
    }

    public double[] getArrErate() {
        return arrErate;
    }

    public void setArrErate(double[] arrErate) {
        this.arrErate = arrErate;
    }

    public double[] getArrEslab() {
        return arrEslab;
    }

    public void setArrEslab(double[] arrEslab) {
        this.arrEslab = arrEslab;
    }

    public double[] getArrEc() {
        return arrEc;
    }

    public void setArrEc(double[] arrEc) {
        this.arrEc = arrEc;
    }

    public double[] getArrFc() {
        return arrFc;
    }

    public void setArrFc(double[] arrFc) {
        this.arrFc = arrFc;
    }

    public double[] getArrFrate_old() {
        return arrFrate_old;
    }

    public void setArrFrate_old(double[] arrFrate_old) {
        this.arrFrate_old = arrFrate_old;
    }

    public double[] getArrFslab_old() {
        return arrFslab_old;
    }

    public void setArrFslab_old(double[] arrFslab_old) {
        this.arrFslab_old = arrFslab_old;
    }

    public double[] getArrErate_old() {
        return arrErate_old;
    }

    public void setArrErate_old(double[] arrErate_old) {
        this.arrErate_old = arrErate_old;
    }

    public double[] getArrEslab_old() {
        return arrEslab_old;
    }

    public void setArrEslab_old(double[] arrEslab_old) {
        this.arrEslab_old = arrEslab_old;
    }

    public double[] getArrEc_old() {
        return arrEc_old;
    }

    public void setArrEc_old(double[] arrEc_old) {
        this.arrEc_old = arrEc_old;
    }

    public double[] getArrFc_old() {
        return arrFc_old;
    }

    public void setArrFc_old(double[] arrFc_old) {
        this.arrFc_old = arrFc_old;
    }

    public double[] getArrdlFslab() {
        return arrdlFslab;
    }

    public void setArrdlFslab(double[] arrdlFslab) {
        this.arrdlFslab = arrdlFslab;
    }

    public double[] getArrdlFslab_old() {
        return arrdlFslab_old;
    }

    public void setArrdlFslab_old(double[] arrdlFslab_old) {
        this.arrdlFslab_old = arrdlFslab_old;
    }

    public String getTOD_ONPEAK() {
        return TOD_ONPEAK;
    }

    public void setTOD_ONPEAK(String TOD_ONPEAK) {
        this.TOD_ONPEAK = TOD_ONPEAK;
    }

    public String getTOD_NORMALPEAK() {
        return TOD_NORMALPEAK;
    }

    public void setTOD_NORMALPEAK(String TOD_NORMALPEAK) {
        this.TOD_NORMALPEAK = TOD_NORMALPEAK;
    }

    public String getTOD_ONPEAK_1() {
        return TOD_ONPEAK_1;
    }

    public void setTOD_ONPEAK_1(String TOD_ONPEAK_1) {
        this.TOD_ONPEAK_1 = TOD_ONPEAK_1;
    }

    public String getTOD_OFFPEAK() {
        return TOD_OFFPEAK;
    }

    public void setTOD_OFFPEAK(String TOD_OFFPEAK) {
        this.TOD_OFFPEAK = TOD_OFFPEAK;
    }

    public String getCURRENT_BILL() {
        return CURRENT_BILL;
    }

    public void setCURRENT_BILL(String CURRENT_BILL) {
        this.CURRENT_BILL = CURRENT_BILL;
    }

    public String getDL_DAYS_COUNT() {
        return DL_DAYS_COUNT;
    }

    public void setDL_DAYS_COUNT(String DL_DAYS_COUNT) {
        this.DL_DAYS_COUNT = DL_DAYS_COUNT;
    }

    public String getMRCODE() {
        return MRCODE;
    }

    public void setMRCODE(String MRCODE) {
        this.MRCODE = MRCODE;
    }

    public String getREADDATE() {
        return READDATE;
    }

    public void setREADDATE(String READDATE) {
        this.READDATE = READDATE;
    }
}

