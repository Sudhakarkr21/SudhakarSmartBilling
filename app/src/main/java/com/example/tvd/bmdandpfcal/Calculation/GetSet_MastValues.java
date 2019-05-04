package com.example.tvd.bmdandpfcal.Calculation;

import android.database.Cursor;
import android.text.TextUtils;

import com.example.tvd.bmdandpfcal.Database;
import com.example.tvd.bmdandpfcal.GetSetValues;
import com.example.tvd.bmdandpfcal.Mast_Cust;
import com.example.tvd.bmdandpfcal.Subdiv_Details;
import com.example.tvd.bmdandpfcal.Tariff_config;
import com.example.tvd.bmdandpfcal.values.FunctionCall;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.example.tvd.bmdandpfcal.Calculation.DB_Table_Columns.DLCOUNT;
import static com.example.tvd.bmdandpfcal.values.Constant.ZERO;

public class GetSet_MastValues {
    private FunctionCall functionsCall = new FunctionCall();
    private Extra_Calculation extraCalculation = new Extra_Calculation();
   private Database database;
   private GetSetValues getSetValues;
   private GetSet_Old_Mast  getSet_old_mast;

    public GetSet_MastValues(Database database,GetSetValues getSetValues){
        this.database = database;
        this.getSetValues = getSetValues;
    }

    public void setValues(List<Mast_Cust> mast_custs, List<Subdiv_Details> subdiv_details, boolean bulk_billing) {
        int Present_STS = functionsCall.convert_int(mast_custs.get(0).getPRESSTS());
        int intTodFlag = functionsCall.convert_int(mast_custs.get(0).getTOD_FLAG());

        if (intTodFlag != 1) {
            if (Present_STS == 3 || Present_STS == 4 || Present_STS == 6 || Present_STS == 8 || Present_STS == 9 || Present_STS == 10
                    || Present_STS == 11 || Present_STS == 12 || Present_STS == 13 || Present_STS == 14 || Present_STS == 16
                    || Present_STS == 17 || Present_STS == 18 || Present_STS == 19 || Present_STS == 20 || Present_STS == 21
                    || Present_STS == 22 || Present_STS == 23 || Present_STS == 24 || Present_STS == 25 || Present_STS == 26
                    || Present_STS == 27 || Present_STS == 28 || Present_STS == 29 || Present_STS == 30 || Present_STS == 31) {
                double units = functionsCall.convert_decimal(mast_custs.get(0).getMF())*
                        (functionsCall.convert_decimal(mast_custs.get(0).getPRES_RDG()) - functionsCall.convert_decimal(mast_custs.get(0).getPRVRED()));
                double diff = 0.0;
                if(mast_custs.get(0).getMTRCHGOLDUNITS().equals(""))
                    diff = 0.0;
                else
                 diff = functionsCall.convert_decimal(mast_custs.get(0).getMTRCHGOLDUNITS());
                double consume = diff + units ;
                mast_custs.get(0).setUNITS(""+consume);

            }  else {
                if(Present_STS == 5){
                    double units = functionsCall.convert_decimal(mast_custs.get(0).getMF())*
                        functionsCall.convert_decimal(dialover(mast_custs.get(0).getPRVRED(),
                                mast_custs.get(0).getPRES_RDG()));
                    mast_custs.get(0).setUNITS(""+units);
                }else {
                    String dl_flag = subdiv_details.get(0).getDLFLAG();
                    if(!TextUtils.isEmpty(dl_flag)){
                        if(StringUtils.startsWithIgnoreCase(dl_flag,"Y")||StringUtils.startsWithIgnoreCase(dl_flag,"y")){
                            mast_custs.get(0).setUNITS(""+(functionsCall.convert_decimal(mast_custs.get(0).getMF()) *
                                    functionsCall.convert_decimal(mast_custs.get(0).getAVGCON())));
                        }else mast_custs.get(0).setUNITS("0");
                    }else  mast_custs.get(0).setUNITS(""+(functionsCall.convert_decimal(mast_custs.get(0).getMF()) *
                            functionsCall.convert_decimal(mast_custs.get(0).getAVGCON())));
                }
            }

        }else {
            double units = functionsCall.convert_decimal(mast_custs.get(0).getPRES_RDG())
                    - functionsCall.convert_decimal(mast_custs.get(0).getPRVRED());
            double totalunits = functionsCall.convert_decimal(mast_custs.get(0).getMF()) * units;
            double diff = functionsCall.convert_decimal(mast_custs.get(0).getMTRCHGOLDUNITS());
            double totalconsume = totalunits + diff;
            mast_custs.get(0).setUNITS("" + totalconsume);
        }

        if (Present_STS == 1 || Present_STS == 2 || Present_STS == 7 || Present_STS == 15) {
            database.updateDLrecord(0);
            Cursor data = database.getBillingRecordData();
            data.moveToNext();
            mast_custs.get(0).setDLCOUNT(functionsCall.getCursorValue(data, DLCOUNT));
            mast_custs.get(0).setDEPOSIT(ZERO);
            data.close();
        }

        if (mast_custs.get(0).getEXTRA2().equals("FAC"))
            mast_custs.get(0).setDATA2(functionsCall.splitString(extraCalculation.check_fac_status(mast_custs, subdiv_details, getSetValues)));
        startCalculation(mast_custs,subdiv_details,bulk_billing);
    }


    private void startCalculation(List<Mast_Cust> mastList, List<Subdiv_Details> subdivDetails, boolean bulk_billing) {
        ArrayList<Tariff_config> tariffConfigs = new ArrayList<>();
        int Present_STS = functionsCall.convert_int(mastList.get(0).getPRESSTS());
        new Calculation_Tariff().tariff_calculation(mastList, database, tariffConfigs);
        if(Present_STS == 2 || Present_STS == 1 || Present_STS == 7 || Present_STS == 15){

        }

    }

    private String dialover(String last_reading, String pre_reading) {
        int s5 = (int) Math.round(functionsCall.convert_decimal(pre_reading));
        int s6 = (int) Math.round(functionsCall.convert_decimal(last_reading));
        int s7 = String.valueOf(s6).length();
        String repeat = new String(new char[s7]).replace("\0", "9");
        int s8 = (int) Math.round(functionsCall.convert_decimal(repeat));

        int s9 = s8 - s6;
        int s10 = s9 + s5 + 1;
        return String.valueOf(s10);
    }

}

