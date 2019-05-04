package com.example.tvd.bmdandpfcal;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Tariff_config implements Serializable {
    @SerializedName("TARIFF_CODE")
    @Expose
    String TARIFF_CODE;
    @SerializedName("RUFLAG")
    @Expose
    String RUFLAG;
    @SerializedName("TARIFF")
    @Expose
    String TARIFF;
    @SerializedName("NOF_FSLABS")
    @Expose
    String NOF_FSLABS;
    @SerializedName("FSLAB1")
    @Expose
    String FSLAB1;
    @SerializedName("FRATE1")
    @Expose
    String FRATE1;
    @SerializedName("FSLAB2")
    @Expose
    String FSLAB2;
    @SerializedName("FRATE2")
    @Expose
    String FRATE2;
    @SerializedName("FSLAB3")
    @Expose
    String FSLAB3;
    @SerializedName("FRATE3")
    @Expose
    String FRATE3;
    @SerializedName("FSLAB4")
    @Expose
    String FSLAB4;
    @SerializedName("FRATE4")
    @Expose
    String FRATE4;
    @SerializedName("FSLAB5")
    @Expose
    String FSLAB5;
    @SerializedName("FRATE5")
    @Expose
    String FRATE5;
    @SerializedName("FCMIN")
    @Expose
    String FCMIN;
    @SerializedName("NOF_ESLABS")
    @Expose
    String NOF_ESLABS;
    @SerializedName("ESLAB1")
    @Expose
    String ESLAB1;
    @SerializedName("ERATE1")
    @Expose
    String ERATE1;
    @SerializedName("ESLAB2")
    @Expose
    String ESLAB2;
    @SerializedName("ERATE2")
    @Expose
    String ERATE2;
    @SerializedName("ESLAB3")
    @Expose
    String ESLAB3;
    @SerializedName("ERATE3")
    @Expose
    String ERATE3;
    @SerializedName("ESLAB4")
    @Expose
    String ESLAB4;
    @SerializedName("ERATE4")
    @Expose
    String ERATE4;
    @SerializedName("ESLAB5")
    @Expose
    String ESLAB5;
    @SerializedName("ERATE5")
    @Expose
    String ERATE5;
    @SerializedName("ESLAB6")
    @Expose
    String ESLAB6;
    @SerializedName("ERATE6")
    @Expose
    String ERATE6;
    @SerializedName("ECMIN")
    @Expose
    String ECMIN;
    @SerializedName("SOLAR_RATE")
    @Expose
    String SOLAR_RATE;
    @SerializedName("SOLAR_MAX_VAL")
    @Expose
    String SOLAR_MAX_VAL;
    @SerializedName("HREBATE_PER")
    @Expose
    String HREBATE_PER;
    @SerializedName("TAX_PER")
    @Expose
    String TAX_PER;
    @SerializedName("INTR_PER")
    @Expose
    String INTR_PER;
    @SerializedName("PF_PEN_CHARGE")
    @Expose
    String PF_PEN_CHARGE;
    @SerializedName("PL_CONSUMER")
    @Expose
    String PL_CONSUMER;
    @SerializedName("TOD_MIN")
    @Expose
    String TOD_MIN;
    @SerializedName("TOD_NORMAL")
    @Expose
    String TOD_NORMAL;
    @SerializedName("TOD_MAX")
    @Expose
    String TOD_MAX;
    @SerializedName("CAP_RATE")
    @Expose
    String CAP_RATE;
    @SerializedName("CHARITY_RATE")
    @Expose
    String CHARITY_RATE;
    @SerializedName("TAX_PER_OLD")
    @Expose
    String TAX_PER_OLD;


    public String getTARIFF_CODE() {
        return TARIFF_CODE;
    }

    public void setTARIFF_CODE(String TARIFF_CODE) {
        this.TARIFF_CODE = TARIFF_CODE;
    }

    public String getRUFLAG() {
        return RUFLAG;
    }

    public void setRUFLAG(String RUFLAG) {
        this.RUFLAG = RUFLAG;
    }

    public String getTARIFF() {
        return TARIFF;
    }

    public void setTARIFF(String TARIFF) {
        this.TARIFF = TARIFF;
    }

    public String getNOF_FSLABS() {
        return NOF_FSLABS;
    }

    public void setNOF_FSLABS(String NOF_FSLABS) {
        this.NOF_FSLABS = NOF_FSLABS;
    }

    public String getFSLAB1() {
        return FSLAB1;
    }

    public void setFSLAB1(String FSLAB1) {
        this.FSLAB1 = FSLAB1;
    }

    public String getFRATE1() {
        return FRATE1;
    }

    public void setFRATE1(String FRATE1) {
        this.FRATE1 = FRATE1;
    }

    public String getFSLAB2() {
        return FSLAB2;
    }

    public void setFSLAB2(String FSLAB2) {
        this.FSLAB2 = FSLAB2;
    }

    public String getFRATE2() {
        return FRATE2;
    }

    public void setFRATE2(String FRATE2) {
        this.FRATE2 = FRATE2;
    }

    public String getFSLAB3() {
        return FSLAB3;
    }

    public void setFSLAB3(String FSLAB3) {
        this.FSLAB3 = FSLAB3;
    }

    public String getFRATE3() {
        return FRATE3;
    }

    public void setFRATE3(String FRATE3) {
        this.FRATE3 = FRATE3;
    }

    public String getFSLAB4() {
        return FSLAB4;
    }

    public void setFSLAB4(String FSLAB4) {
        this.FSLAB4 = FSLAB4;
    }

    public String getFRATE4() {
        return FRATE4;
    }

    public void setFRATE4(String FRATE4) {
        this.FRATE4 = FRATE4;
    }

    public String getFSLAB5() {
        return FSLAB5;
    }

    public void setFSLAB5(String FSLAB5) {
        this.FSLAB5 = FSLAB5;
    }

    public String getFRATE5() {
        return FRATE5;
    }

    public void setFRATE5(String FRATE5) {
        this.FRATE5 = FRATE5;
    }

    public String getFCMIN() {
        return FCMIN;
    }

    public void setFCMIN(String FCMIN) {
        this.FCMIN = FCMIN;
    }

    public String getNOF_ESLABS() {
        return NOF_ESLABS;
    }

    public void setNOF_ESLABS(String NOF_ESLABS) {
        this.NOF_ESLABS = NOF_ESLABS;
    }

    public String getESLAB1() {
        return ESLAB1;
    }

    public void setESLAB1(String ESLAB1) {
        this.ESLAB1 = ESLAB1;
    }

    public String getERATE1() {
        return ERATE1;
    }

    public void setERATE1(String ERATE1) {
        this.ERATE1 = ERATE1;
    }

    public String getESLAB2() {
        return ESLAB2;
    }

    public void setESLAB2(String ESLAB2) {
        this.ESLAB2 = ESLAB2;
    }

    public String getERATE2() {
        return ERATE2;
    }

    public void setERATE2(String ERATE2) {
        this.ERATE2 = ERATE2;
    }

    public String getESLAB3() {
        return ESLAB3;
    }

    public void setESLAB3(String ESLAB3) {
        this.ESLAB3 = ESLAB3;
    }

    public String getERATE3() {
        return ERATE3;
    }

    public void setERATE3(String ERATE3) {
        this.ERATE3 = ERATE3;
    }

    public String getESLAB4() {
        return ESLAB4;
    }

    public void setESLAB4(String ESLAB4) {
        this.ESLAB4 = ESLAB4;
    }

    public String getERATE4() {
        return ERATE4;
    }

    public void setERATE4(String ERATE4) {
        this.ERATE4 = ERATE4;
    }

    public String getESLAB5() {
        return ESLAB5;
    }

    public void setESLAB5(String ESLAB5) {
        this.ESLAB5 = ESLAB5;
    }

    public String getERATE5() {
        return ERATE5;
    }

    public void setERATE5(String ERATE5) {
        this.ERATE5 = ERATE5;
    }

    public String getESLAB6() {
        return ESLAB6;
    }

    public void setESLAB6(String ESLAB6) {
        this.ESLAB6 = ESLAB6;
    }

    public String getERATE6() {
        return ERATE6;
    }

    public void setERATE6(String ERATE6) {
        this.ERATE6 = ERATE6;
    }

    public String getECMIN() {
        return ECMIN;
    }

    public void setECMIN(String ECMIN) {
        this.ECMIN = ECMIN;
    }

    public String getSOLAR_RATE() {
        return SOLAR_RATE;
    }

    public void setSOLAR_RATE(String SOLAR_RATE) {
        this.SOLAR_RATE = SOLAR_RATE;
    }

    public String getSOLAR_MAX_VAL() {
        return SOLAR_MAX_VAL;
    }

    public void setSOLAR_MAX_VAL(String SOLAR_MAX_VAL) {
        this.SOLAR_MAX_VAL = SOLAR_MAX_VAL;
    }

    public String getHREBATE_PER() {
        return HREBATE_PER;
    }

    public void setHREBATE_PER(String HREBATE_PER) {
        this.HREBATE_PER = HREBATE_PER;
    }

    public String getTAX_PER() {
        return TAX_PER;
    }

    public void setTAX_PER(String TAX_PER) {
        this.TAX_PER = TAX_PER;
    }

    public String getINTR_PER() {
        return INTR_PER;
    }

    public void setINTR_PER(String INTR_PER) {
        this.INTR_PER = INTR_PER;
    }

    public String getPF_PEN_CHARGE() {
        return PF_PEN_CHARGE;
    }

    public void setPF_PEN_CHARGE(String PF_PEN_CHARGE) {
        this.PF_PEN_CHARGE = PF_PEN_CHARGE;
    }

    public String getPL_CONSUMER() {
        return PL_CONSUMER;
    }

    public void setPL_CONSUMER(String PL_CONSUMER) {
        this.PL_CONSUMER = PL_CONSUMER;
    }

    public String getTOD_MIN() {
        return TOD_MIN;
    }

    public void setTOD_MIN(String TOD_MIN) {
        this.TOD_MIN = TOD_MIN;
    }

    public String getTOD_NORMAL() {
        return TOD_NORMAL;
    }

    public void setTOD_NORMAL(String TOD_NORMAL) {
        this.TOD_NORMAL = TOD_NORMAL;
    }

    public String getTOD_MAX() {
        return TOD_MAX;
    }

    public void setTOD_MAX(String TOD_MAX) {
        this.TOD_MAX = TOD_MAX;
    }

    public String getCAP_RATE() {
        return CAP_RATE;
    }

    public void setCAP_RATE(String CAP_RATE) {
        this.CAP_RATE = CAP_RATE;
    }

    public String getCHARITY_RATE() {
        return CHARITY_RATE;
    }

    public void setCHARITY_RATE(String CHARITY_RATE) {
        this.CHARITY_RATE = CHARITY_RATE;
    }

    public String getTAX_PER_OLD() {
        return TAX_PER_OLD;
    }

    public void setTAX_PER_OLD(String TAX_PER_OLD) {
        this.TAX_PER_OLD = TAX_PER_OLD;
    }
}

