package com.example.tvd.bmdandpfcal.Calculation;

import com.example.tvd.bmdandpfcal.GetSetValues;
import com.example.tvd.bmdandpfcal.Mast_Cust;
import com.example.tvd.bmdandpfcal.Subdiv_Details;
import com.example.tvd.bmdandpfcal.values.FunctionCall;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.tvd.bmdandpfcal.values.Constant.FAC_DEC_18;
import static com.example.tvd.bmdandpfcal.values.Constant.FAC_END;
import static com.example.tvd.bmdandpfcal.values.Constant.FAC_START;
import static com.example.tvd.bmdandpfcal.values.Constant.NEW_FAC;

public class Extra_Calculation {
    private FunctionCall functionsCall = new FunctionCall();

    public double check_fac_status(List<Mast_Cust> getSetMast, List<Subdiv_Details> subdivDetails, GetSetValues getSetValues) {
        String fac_start, fac_end, fac_dec_18, result = null;
        int diff = functionsCall.convert_int(getSetMast.get(0).getREADDATE().substring(0, 2));
        int present_status = functionsCall.convert_int(getSetMast.get(0).getPRESSTS());
        fac_start = functionsCall.get_month_date_decreased(subdivDetails.get(0).getFACSTART(), -1, diff - 1);
        fac_end = functionsCall.get_month_date_decreased(subdivDetails.get(0).getFACEND(), -1, diff + 1);
        fac_dec_18 = functionsCall.get_month_date_decreased(subdivDetails.get(0).getFACDEC18(), -1, diff);
        Date /*present_date = null,*/ previous_date = null, fac_start_date = null, fac_end_date = null, fac_dec_18_date = null ;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        try {
            previous_date = formatter.parse(functionsCall.changedateformat(getSetMast.get(0).getPREV_READ_DATE(), "-"));
            fac_start_date = formatter.parse(fac_start);
            fac_end_date = formatter.parse(fac_end);
            fac_dec_18_date = formatter.parse(fac_dec_18);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        double total_days = functionsCall.convert_decimal(functionsCall.calculateDays(getSetMast.get(0).getPREV_READ_DATE(),
                getSetMast.get(0).getREADDATE()));
        double fac_days_diff = functionsCall.convert_decimal(functionsCall.calculateDays(fac_start, fac_end));
        double data2_1=0, data2_2=0, data2_3=0;
        assert previous_date != null;
        if (previous_date.after(fac_dec_18_date) || previous_date.equals(fac_dec_18_date))
            result = NEW_FAC;
        if (previous_date.before(fac_dec_18_date))
            result = FAC_DEC_18;
        if (previous_date.before(fac_end_date))
            result = FAC_END;
        if (previous_date.before(fac_start_date))
            result = FAC_START;
        if (present_status == 1 || present_status == 2 || present_status == 7 || present_status == 15) {
            getSetValues.setFAC_days(functionsCall.getBigdecimal(functionsCall.convert_decimal(getSetMast.get(0).getUNITS()), 1));
            getSetValues.setFAC_18zero_days(0);
            data2_3 = functionsCall.convert_decimal(subdivDetails.get(0).getFEC()) * getSetValues.getFAC_days();
            getSetValues.setFAC_days_value("" + data2_3);
        } else {
            double fac_old_days, fac_old_units, fac_zero_units, fac_dec_18_days, fac_dec_18_units, fac_days, fac_units;
            assert result != null;
            switch (result) {
                case FAC_START:
                    /*------------------------------ FAC before April 2018 for 0.13 rate ------------------------------*/
                    fac_old_days = functionsCall.convert_decimal(functionsCall.calculateDays(getSetMast.get(0).getPREV_READ_DATE(), fac_start));
                    fac_old_units = (fac_old_days / total_days) * functionsCall.convert_decimal(getSetMast.get(0).getUNITS());
                    getSetValues.setFAC_18old_days(functionsCall.getBigdecimal(fac_old_units, 1));
                    data2_1 = functionsCall.convert_decimal(subdivDetails.get(0).getFACOLD()) * getSetValues.getFAC_18old_days();
                    getSetValues.setFAC_18old_value("" + data2_1);
                    /*------------------------------ FAC for Zero rates ------------------------------*/
                    fac_zero_units = (fac_days_diff / total_days) * functionsCall.convert_decimal(getSetMast.get(0).getUNITS());
                    getSetValues.setFAC_18zero_days(functionsCall.getBigdecimal(fac_zero_units, 1));
                    /*------------------------------ FAC after Zero rates & before December 2018 for 0.08 rate ------------------------------ */
                    fac_dec_18_days = functionsCall.convert_decimal(functionsCall.calculateDays(fac_end, fac_dec_18));
                    fac_dec_18_units = (fac_dec_18_days / total_days) * functionsCall.convert_decimal(getSetMast.get(0).getUNITS());
                    getSetValues.setFAC_18dec_days(functionsCall.getBigdecimal(fac_dec_18_units, 1));
                    data2_2 = functionsCall.convert_decimal(subdivDetails.get(0).getFACDEC18RATE()) * getSetValues.getFAC_18dec_days();
                    getSetValues.setFAC_18dec_value("" + data2_2);
                    /*------------------------------ FAC after December 2018 for 0.04 rate ------------------------------*/
                    fac_days = functionsCall.convert_decimal(functionsCall.calculateDays(fac_dec_18, getSetMast.get(0).getREADDATE()));
                    fac_units = (fac_days / total_days) * functionsCall.convert_decimal(getSetMast.get(0).getUNITS());
                    getSetValues.setFAC_days(functionsCall.getBigdecimal(fac_units, 1));
                    data2_3 = functionsCall.convert_decimal(subdivDetails.get(0).getFEC()) * getSetValues.getFAC_days();
                    getSetValues.setFAC_days_value("" + data2_3);
                    break;

                case FAC_END:
                    /*------------------------------ FAC for Zero rates ------------------------------*/
                    fac_zero_units = (fac_days_diff / total_days) * functionsCall.convert_decimal(getSetMast.get(0).getUNITS());
                    getSetValues.setFAC_18zero_days(functionsCall.getBigdecimal(fac_zero_units, 1));
                    /*------------------------------ FAC after Zero rates & before December 2018 for 0.08 rate ------------------------------ */
                    fac_dec_18_days = functionsCall.convert_decimal(functionsCall.calculateDays(fac_end, fac_dec_18));
                    fac_dec_18_units = functionsCall.getBigdecimal((fac_dec_18_days / total_days) *
                            functionsCall.convert_decimal(getSetMast.get(0).getUNITS()), 1);
                    getSetValues.setFAC_18dec_days(functionsCall.getBigdecimal(fac_dec_18_units, 1));
                    data2_2 = functionsCall.convert_decimal(subdivDetails.get(0).getFACDEC18RATE()) * getSetValues.getFAC_18dec_days();
                    getSetValues.setFAC_18dec_value("" + data2_2);
                    /*------------------------------ FAC after December 2018 for 0.04 rate ------------------------------*/
                    fac_days = functionsCall.convert_decimal(functionsCall.calculateDays(fac_dec_18, getSetMast.get(0).getREADDATE()));
                    fac_units = functionsCall.getBigdecimal((fac_days / total_days) *
                            functionsCall.convert_decimal(getSetMast.get(0).getUNITS()), 1);
                    getSetValues.setFAC_days(fac_units);
                    data2_3 = functionsCall.convert_decimal(subdivDetails.get(0).getFEC()) * getSetValues.getFAC_days();
                    getSetValues.setFAC_days_value("" + data2_3);
                    break;

                case FAC_DEC_18:
                    /*------------------------------ FAC after Zero rates & before December 2018 for 0.08 rate ------------------------------ */
                    fac_dec_18_days = functionsCall.convert_decimal(functionsCall.calculateDays(getSetMast.get(0).getPREV_READ_DATE(),
                            fac_dec_18));
                    fac_dec_18_units = functionsCall.getBigdecimal((fac_dec_18_days / total_days) *
                            functionsCall.convert_decimal(getSetMast.get(0).getUNITS()), 1);
                    getSetValues.setFAC_18dec_days(functionsCall.getBigdecimal(fac_dec_18_units, 1));
                    data2_2 = functionsCall.convert_decimal(subdivDetails.get(0).getFACDEC18RATE()) * getSetValues.getFAC_18dec_days();
                    getSetValues.setFAC_18dec_value("" + data2_2);
                    /*------------------------------ FAC after December 2018 for 0.04 rate ------------------------------*/
                    fac_days = functionsCall.convert_decimal(functionsCall.calculateDays(fac_dec_18, getSetMast.get(0).getREADDATE()));
                    fac_units = functionsCall.getBigdecimal((fac_days / total_days) *
                            functionsCall.convert_decimal(getSetMast.get(0).getUNITS()), 1);
                    getSetValues.setFAC_days(functionsCall.getBigdecimal(fac_units, 1));
                    getSetValues.setFAC_18zero_days(0);
                    data2_3 = functionsCall.convert_decimal(subdivDetails.get(0).getFEC()) * getSetValues.getFAC_days();
                    getSetValues.setFAC_days_value("" + data2_3);
                    break;

                case NEW_FAC:
                    /*------------------------------ FAC after December 2018 for 0.04 rate ------------------------------*/
                    getSetValues.setFAC_days(functionsCall.convert_decimal(getSetMast.get(0).getUNITS()));
                    getSetValues.setFAC_18zero_days(0);
                    data2_3 = functionsCall.convert_decimal(subdivDetails.get(0).getFEC()) * getSetValues.getFAC_days();
                    getSetValues.setFAC_days_value("" + data2_3);
                    break;
            }

        }
        return data2_1 + data2_2 + data2_3;
    }

    void get_fc_tarrif_rate_status(List<Mast_Cust> getSetMast, List<Subdiv_Details> subdivDetails, GetSet_Old_Mast getSetOldMast) {
        String tarrif_date_chg = subdivDetails.get(0).getNWTRFDATE();
        String tarrif_date = functionsCall.get_month_date_decreased(tarrif_date_chg, 0, -1);
        double days_diff;
        int total_days = functionsCall.convert_int(functionsCall.calculateDays(getSetMast.get(0).getPREV_READ_DATE(), getSetMast.get(0).getREADDATE()));
        int diff_days1 = functionsCall.convert_int(functionsCall.calculateDays(
                functionsCall.get_month_date_decreased(getSetMast.get(0).getPREV_READ_DATE(), 0, 0), tarrif_date));
        getSetOldMast.setBILLDAYS(String.valueOf(diff_days1));
        int diff_days2 = total_days - diff_days1;
        getSetOldMast.setBILLDAYSNEW(String.valueOf(diff_days2));
        days_diff = (double)diff_days1 / 30;
        getSetOldMast.setDLCOUNT(String.valueOf(days_diff));
        days_diff = (double)diff_days2 / 30;
        getSetOldMast.setDLCOUNTNEW(String.valueOf(days_diff));
    }
}
