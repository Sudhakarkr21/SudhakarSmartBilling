package com.example.tvd.bmdandpfcal;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CalValues implements Serializable {
    @SerializedName("fixedCharge")
    @Expose
    String fixedCharge;

    @SerializedName("energyCharge")
    @Expose
    String energyCharge;

    @SerializedName("bmd")
    @Expose
    String bmd;

    @SerializedName("pf")
    @Expose
    String pf;

    @SerializedName("tax")
    @Expose
    String tax;

    public String getFixedCharge() {
        return fixedCharge;
    }

    public void setFixedCharge(String fixedCharge) {
        this.fixedCharge = fixedCharge;
    }

    public String getEnergyCharge() {
        return energyCharge;
    }

    public void setEnergyCharge(String energyCharge) {
        this.energyCharge = energyCharge;
    }

    public String getBmd() {
        return bmd;
    }

    public void setBmd(String bmd) {
        this.bmd = bmd;
    }

    public String getPf() {
        return pf;
    }

    public void setPf(String pf) {
        this.pf = pf;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }
}
