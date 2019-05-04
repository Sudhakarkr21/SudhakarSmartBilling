package com.example.tvd.bmdandpfcal.Calculation;


import com.example.tvd.bmdandpfcal.Database;
import com.example.tvd.bmdandpfcal.Mast_Cust;
import com.example.tvd.bmdandpfcal.Tariff_config;
import com.example.tvd.bmdandpfcal.fcall;
import com.example.tvd.bmdandpfcal.values.FunctionCall;

import java.util.ArrayList;
import java.util.List;

import static com.example.tvd.bmdandpfcal.values.Constant.LT1_MIN;


public class Calculation_Tariff {
    private FunctionCall functionsCall = new FunctionCall();
    fcall fcall = new fcall();


    public void tariff_calculation(List<Mast_Cust> mastArrayList, Database database, ArrayList<Tariff_config> tariffConfigList) {
        String urban="0", rural="1", dbt_urban="3", dbt_rural="4";
        Tariff_config tariffConfig;
        Tariff_config tariffConfig_old;
        tariffConfigList.clear();
        switch (mastArrayList.get(0).getTARIFF()) {
            case "10":
                double consUnits = functionsCall.convert_decimal(mastArrayList.get(0).getUNITS());
                if (consUnits > LT1_MIN * (functionsCall.convert_decimal(mastArrayList.get(0).getDLCOUNT()) + 1)) {
                    if (mastArrayList.get(0).getRREBATE().equals(urban)) {
                        tariffConfig = fcall.getTariffConfig2(database.getTarrifDataBJ(urban, "20"));
                        tariffConfig_old = fcall.getTariffConfig2(database.getTarrifDataBJ2(urban, "20"));
                    } else {
                        tariffConfig = fcall.getTariffConfig2(database.getTarrifDataBJ(rural, "20"));
                        tariffConfig_old = fcall.getTariffConfig2(database.getTarrifDataBJ2(rural, "20"));
                    }
                } else {
                    tariffConfig = fcall.getTariffConfig2(database.getTarrifDataBJ(rural, mastArrayList.get(0).getTARIFF()));
                    tariffConfig_old = fcall.getTariffConfig2(database.getTarrifDataBJ2(rural, mastArrayList.get(0).getTARIFF()));
                }
                break;

            case "23":
                if (mastArrayList.get(0).getRREBATE().equals(urban)) {
                    tariffConfig = fcall.getTariffConfig2(database.getTarrifDataBJ(urban, "20"));
                    tariffConfig_old = fcall.getTariffConfig2(database.getTarrifDataBJ2(urban, "20"));
                } else {
                    tariffConfig = fcall.getTariffConfig2(database.getTarrifDataBJ(rural, "20"));
                    tariffConfig_old = fcall.getTariffConfig2(database.getTarrifDataBJ2(rural, "20"));
                }
                break;

            case "30":
                if (mastArrayList.get(0).getREBATE_FLAG().equals("4") || mastArrayList.get(0).getREBATE_FLAG().equals("9") ||
                        mastArrayList.get(0).getREBATE_FLAG().equals("10") || mastArrayList.get(0).getREBATE_FLAG().equals("14")
                    /* && functionsCall.convertKWtoHP(mastArrayList.get(0).getSANCHP(), mastArrayList.get(0).getSANCKW()) > 5*/) {
                    if (mastArrayList.get(0).getRREBATE().equals(urban)) {
                        tariffConfig = fcall.getTariffConfig2(database.getTarrifDataBJ(dbt_urban, mastArrayList.get(0).getTARIFF()));
                        tariffConfig_old = fcall.getTariffConfig2(database.getTarrifDataBJ2(dbt_urban, mastArrayList.get(0).getTARIFF()));
                    } else {
                        tariffConfig = fcall.getTariffConfig2(database.getTarrifDataBJ(dbt_rural, mastArrayList.get(0).getTARIFF()));
                        tariffConfig_old = fcall.getTariffConfig2(database.getTarrifDataBJ2(dbt_rural, mastArrayList.get(0).getTARIFF()));
                    }
                } else {
                    tariffConfig = fcall.getTariffConfig2(database.getTarrifData(mastArrayList.get(0).getCONSNO(),
                            mastArrayList.get(0).getRREBATE()));
                    tariffConfig_old = fcall.getTariffConfig2(database.getTarrifData_old(mastArrayList.get(0).getCONSNO(),
                            mastArrayList.get(0).getRREBATE()));
                }
                break;

            case "50":
                if (mastArrayList.get(0).getREBATE_FLAG().equals("4") || mastArrayList.get(0).getREBATE_FLAG().equals("9") ||
                        mastArrayList.get(0).getREBATE_FLAG().equals("10") || mastArrayList.get(0).getREBATE_FLAG().equals("14")
                    /* && functionsCall.convertKWtoHP(mastArrayList.get(0).getSANCHP(), mastArrayList.get(0).getSANCKW()) > 5*/) {
                    if (mastArrayList.get(0).getRREBATE().equals(urban)) {
                        tariffConfig = fcall.getTariffConfig2(database.getTarrifDataBJ(dbt_urban, mastArrayList.get(0).getTARIFF()));
                        tariffConfig_old = fcall.getTariffConfig2(database.getTarrifDataBJ2(dbt_urban, mastArrayList.get(0).getTARIFF()));
                    } else {
                        tariffConfig = fcall.getTariffConfig2(database.getTarrifDataBJ(dbt_rural, mastArrayList.get(0).getTARIFF()));
                        tariffConfig_old = fcall.getTariffConfig2(database.getTarrifDataBJ2(dbt_rural, mastArrayList.get(0).getTARIFF()));
                    }
                } else {
                    tariffConfig = fcall.getTariffConfig2(database.getTarrifDataBJ(urban, mastArrayList.get(0).getTARIFF()));
                    tariffConfig_old = fcall.getTariffConfig2(database.getTarrifDataBJ2(urban, mastArrayList.get(0).getTARIFF()));
                }
                break;

            case "51":
                if (mastArrayList.get(0).getREBATE_FLAG().equals("4") || mastArrayList.get(0).getREBATE_FLAG().equals("9") ||
                        mastArrayList.get(0).getREBATE_FLAG().equals("10") || mastArrayList.get(0).getREBATE_FLAG().equals("14")
                    /* && functionsCall.convertKWtoHP(mastArrayList.get(0).getSANCHP(), mastArrayList.get(0).getSANCKW()) > 5*/) {
                    if (mastArrayList.get(0).getRREBATE().equals(urban)) {
                        tariffConfig = fcall.getTariffConfig2(database.getTarrifDataBJ(dbt_urban, mastArrayList.get(0).getTARIFF()));
                        tariffConfig_old = fcall.getTariffConfig2(database.getTarrifDataBJ2(dbt_urban, mastArrayList.get(0).getTARIFF()));
                    } else {
                        tariffConfig = fcall.getTariffConfig2(database.getTarrifDataBJ(dbt_rural, mastArrayList.get(0).getTARIFF()));
                        tariffConfig_old = fcall.getTariffConfig2(database.getTarrifDataBJ2(dbt_rural, mastArrayList.get(0).getTARIFF()));
                    }
                } else {
                    tariffConfig = fcall.getTariffConfig2(database.getTarrifDataBJ(urban, mastArrayList.get(0).getTARIFF()));
                    tariffConfig_old = fcall.getTariffConfig2(database.getTarrifDataBJ2(urban, mastArrayList.get(0).getTARIFF()));
                }
                break;

            case "52":
                if (mastArrayList.get(0).getREBATE_FLAG().equals("4") || mastArrayList.get(0).getREBATE_FLAG().equals("9") ||
                        mastArrayList.get(0).getREBATE_FLAG().equals("10") || mastArrayList.get(0).getREBATE_FLAG().equals("14")
                    /* && functionsCall.convertKWtoHP(mastArrayList.get(0).getSANCHP(), mastArrayList.get(0).getSANCKW()) > 5*/) {
                    if (mastArrayList.get(0).getRREBATE().equals(urban)) {
                        tariffConfig = fcall.getTariffConfig2(database.getTarrifDataBJ(dbt_urban, mastArrayList.get(0).getTARIFF()));
                        tariffConfig_old = fcall.getTariffConfig2(database.getTarrifDataBJ2(dbt_urban, mastArrayList.get(0).getTARIFF()));
                    } else {
                        tariffConfig = fcall.getTariffConfig2(database.getTarrifDataBJ(dbt_rural, mastArrayList.get(0).getTARIFF()));
                        tariffConfig_old = fcall.getTariffConfig2(database.getTarrifDataBJ2(dbt_rural, mastArrayList.get(0).getTARIFF()));
                    }
                } else {
                    tariffConfig = fcall.getTariffConfig2(database.getTarrifDataBJ(urban, mastArrayList.get(0).getTARIFF()));
                    tariffConfig_old = fcall.getTariffConfig2(database.getTarrifDataBJ2(urban, mastArrayList.get(0).getTARIFF()));
                }
                break;

            case "53":
                if (mastArrayList.get(0).getREBATE_FLAG().equals("4") || mastArrayList.get(0).getREBATE_FLAG().equals("9") ||
                        mastArrayList.get(0).getREBATE_FLAG().equals("10") || mastArrayList.get(0).getREBATE_FLAG().equals("14")
                    /* && functionsCall.convertKWtoHP(mastArrayList.get(0).getSANCHP(), mastArrayList.get(0).getSANCKW()) > 5*/) {
                    if (mastArrayList.get(0).getRREBATE().equals(urban)) {
                        tariffConfig = fcall.getTariffConfig2(database.getTarrifDataBJ(dbt_urban, mastArrayList.get(0).getTARIFF()));
                        tariffConfig_old = fcall.getTariffConfig2(database.getTarrifDataBJ2(dbt_urban, mastArrayList.get(0).getTARIFF()));
                    } else {
                        tariffConfig = fcall.getTariffConfig2(database.getTarrifDataBJ(dbt_rural, mastArrayList.get(0).getTARIFF()));
                        tariffConfig_old = fcall.getTariffConfig2(database.getTarrifDataBJ2(dbt_rural, mastArrayList.get(0).getTARIFF()));
                    }
                } else {
                    tariffConfig = fcall.getTariffConfig2(database.getTarrifDataBJ(urban, mastArrayList.get(0).getTARIFF()));
                    tariffConfig_old = fcall.getTariffConfig2(database.getTarrifDataBJ2(urban, mastArrayList.get(0).getTARIFF()));
                }
                break;

            case "60":
                tariffConfig = fcall.getTariffConfig2(database.getTarrifDataBJ(urban, mastArrayList.get(0).getTARIFF()));
                tariffConfig_old = fcall.getTariffConfig2(database.getTarrifDataBJ2(urban, mastArrayList.get(0).getTARIFF()));
                break;

            case "61":
                tariffConfig = fcall.getTariffConfig2(database.getTarrifDataBJ(urban, mastArrayList.get(0).getTARIFF()));
                tariffConfig_old = fcall.getTariffConfig2(database.getTarrifDataBJ2(urban, mastArrayList.get(0).getTARIFF()));
                break;

            case "70":
                tariffConfig = fcall.getTariffConfig2(database.getTarrifDataBJ(urban, mastArrayList.get(0).getTARIFF()));
                tariffConfig_old = fcall.getTariffConfig2(database.getTarrifDataBJ2(urban, mastArrayList.get(0).getTARIFF()));
                break;

            case "40":
                tariffConfig = fcall.getTariffConfig2(database.getTarrifDataBJ(urban, mastArrayList.get(0).getTARIFF()));
                tariffConfig_old = fcall.getTariffConfig2(database.getTarrifDataBJ2(urban, mastArrayList.get(0).getTARIFF()));
                break;

            default:
                tariffConfig = fcall.getTariffConfig2(database.getTarrifData(mastArrayList.get(0).getCONSNO(),
                        mastArrayList.get(0).getRREBATE()));
                tariffConfig_old = fcall.getTariffConfig2(database.getTarrifData_old(mastArrayList.get(0).getCONSNO(),
                        mastArrayList.get(0).getRREBATE()));
                break;
        }
        tariffConfigList.add(tariffConfig);
        tariffConfigList.add(tariffConfig_old);
    }
}

