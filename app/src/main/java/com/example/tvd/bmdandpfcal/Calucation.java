package com.example.tvd.bmdandpfcal;

import android.util.Log;

import java.util.List;

public class Calucation {
    double bmdpenalty = 0.0;
    String fc10, e1;
    double totalCharge;
    List<tariff_conf> tarrif_configuration;
    List<Mast_Cust> mast_custs;

    Calucation(List<tariff_conf> tarrif_configuration,List<Mast_Cust> mast_custs) {
        this.tarrif_configuration = tarrif_configuration;
        this.mast_custs = mast_custs;
    }

    public double bmdPenalty(double rmdvalue1, double constant1, double load1) {
        Log.d("debug2", String.valueOf(rmdvalue1));
        if ((rmdvalue1 * constant1) > load1)
            bmdpenalty = (((rmdvalue1 * constant1) - load1) * 2 * 60);
        return bmdpenalty;
    }

    public double pfPenalty(double pfvalue1, double consumption1) {
        if (pfvalue1 >= 0.7 && pfvalue1 < 0.85) {
                double pfPenalty = (0.85 - pfvalue1) * 2 * consumption1;
            return pfPenalty;
        } else {
            pfvalue1 = 0.7;
            double pfPenalty = (0.85 - pfvalue1) * 2 * consumption1;
            return pfPenalty;
        }
    }

    public double fixedRate1(double load) {

        double fixedrate1 = 0.0;
        StringBuffer buffer = new StringBuffer();
        int noOfSlab=0;
        double frate5=0.0,frate6=0.0;
        try {
             noOfSlab = Integer.parseInt(tarrif_configuration.get(0).getNOF_FSLABS());
             frate5 = Double.parseDouble(tarrif_configuration.get(0).getFRATE1());
             frate6 = Double.parseDouble(tarrif_configuration.get(0).getFRATE2());
        }catch (Exception e){
            e.printStackTrace();
        }
        double remload = 0;

        for (int i = 1; i <= noOfSlab; i++) {
            double fixedrate = 0.0;
            if (i == 1) {
                if (noOfSlab == 1) {
                    fixedrate = fixedrate + frate5 * load;
                    String frate1 = String.valueOf(frate5);
                    String load4 = String.valueOf(load);
                    String fc1 = String.format("%.2f", fixedrate);
                    buffer.append(load4 + " * " + frate1 + " :  " + fc1 + " \n");
                } else {
                    if (load <= 1) {
                        fixedrate = fixedrate + frate5 * load;
                        String frate1 = String.valueOf(frate5);
                        String load4 = String.valueOf(load);
                        String fc1 = String.format("%.2f", fixedrate);
                        buffer.append(load4 + " * " + frate1 + " :  " + fc1 + " \n");
                    } else {
                        fixedrate = fixedrate + frate5 * 1;
                        String frate1 = String.valueOf(frate5);
                        String load4 = String.valueOf(load);
                        String fc1 = String.format("%.2f", fixedrate);
                        buffer.append("1 " + " * " + frate1 + " :  " + fc1 + " \n");
                    }
                }
            } else if (load > 1) {
                remload = load - 1;
                fixedrate = fixedrate + frate6 * remload;
                String frate1 = String.valueOf(frate6);
                String load4 = String.valueOf(remload);
                String fc1 = String.format("%.2f", fixedrate);
                buffer.append(load4 + " * " + frate1 + " :   " + fc1 + " \n");
            }

            fixedrate1 = fixedrate1 + fixedrate;
        }
        fc10 = buffer.toString();
//        fixedcharge.setText(".......Fixedcharge......." + "\n" + buffer);
        return fixedrate1;


    }

    public double EC(double consumption1) {
        StringBuffer result = new StringBuffer();
        double con1 = 0;
//        result.append("consumption   :" + consumption1 + "\n");
        totalCharge = 0;
        double consumption3 = 0;
        double noOfslab = 0, ecSlab1 = 0, ecrate1 = 0, ecSlab2 = 0, ecrate2 = 0, ecSlab3 = 0, ecrate3 = 0, ecSlab4 = 0, ecrate4 = 0;
        try {
            noOfslab = Double.parseDouble(tarrif_configuration.get(0).getNOF_ESLABS());
            ecSlab1 = Double.parseDouble(tarrif_configuration.get(0).getESLAB1());
            ecrate1 = Double.parseDouble(tarrif_configuration.get(0).getERATE1());
            ecSlab2 = Double.parseDouble(tarrif_configuration.get(0).getESLAB2());
            ecrate2 = Double.parseDouble(tarrif_configuration.get(0).getERATE2());
            ecSlab3 = Double.parseDouble(tarrif_configuration.get(0).getESLAB3());
            ecrate3 = Double.parseDouble(tarrif_configuration.get(0).getERATE3());
            ecrate4 = Double.parseDouble(tarrif_configuration.get(0).getERATE4());
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 1; i <= noOfslab; i++) {
            double total = 0;

            if (i == 1) {
                if (consumption1 > ecSlab1) {
                    consumption3 = consumption1 - ecSlab1;
                    total = ecSlab1 * ecrate1;
                    String ecslab11 = String.valueOf(ecSlab1);
                    String ecrate11 = String.valueOf(ecrate1);
                    totalCharge = totalCharge + total;
                    String to1 = String.valueOf(total);
                    result.append(ecslab11 + " * " + ecrate11 + "  :" + to1 + " \n");
//                    energyCharge.setText("30" + " * " + "3.35  :"+ to1);

                } else {
                    total = consumption1 * ecrate1;
                    totalCharge = totalCharge + total;
                    String ecslab11 = String.valueOf(ecSlab1);
                    String ecrate11 = String.valueOf(ecrate1);
                    String con = String.valueOf(consumption1);
                    String ec2 = String.format("%.2f", total);
                    result.append(con + " * " + ecrate11 + "  :" + ec2 + " \n");
//                    energyCharge.setText(con + " * " + "3.35  :"+ ec1);
                }
            } else if (i == 2 && consumption1 > ecSlab1) {
                if (consumption3 > ecSlab2) {
                    consumption3 = consumption3 - ecSlab2;
                    total = ecSlab2 * ecrate2;
                    String ecslab11 = String.valueOf(ecSlab2);
                    String ecrate11 = String.valueOf(ecrate2);
                    totalCharge = totalCharge + total;
                    String to1 = String.valueOf(total);
                    result.append(ecslab11 + " * " + ecrate11 + "  :").append(to1).append(" \n");
//                    energyCharge1.setText("70" + " * " + "4.65  :"+ to1);
                } else {
                    total = consumption3 * ecrate2;
                    totalCharge = totalCharge + total;
                    String ecslab11 = String.valueOf(ecSlab2);
                    String ecrate11 = String.valueOf(ecrate2);
                    String con = String.valueOf(consumption3);
                    String ec2 = String.format("%.2f", total);
                    result.append(con + " * " + ecrate11 + "  :" + ec2 + " \n");
//                    energyCharge1.setText(con + " * " + "4.65  :"+ ec1);
                }
            } else if (i == 3 && consumption1 > (ecSlab1 + ecSlab2)) {
                if (consumption3 > ecSlab3) {
                    consumption3 = consumption3 - ecSlab3;
                    total = ecSlab3 * ecrate3;
                    String ecslab11 = String.valueOf(ecSlab3);
                    String ecrate11 = String.valueOf(ecrate3);
                    totalCharge = totalCharge + total;
                    String to1 = String.valueOf(total);
                    result.append(ecslab11 + " * " + ecrate11 + " :" + to1 + " \n");
//                    energyCharge2.setText("100" + " * " + "6.2  :"+ to1);
                } else {
                    total = consumption3 * ecrate3;
                    totalCharge = totalCharge + total;
                    String ecslab11 = String.valueOf(ecSlab3);
                    String ecrate11 = String.valueOf(ecrate3);
                    String con = String.valueOf(consumption3);
                    String ec2 = String.format("%.2f", total);
                    result.append(con + " * " + ecrate11 + "  :" + ec2 + " \n");
//                    energyCharge2.setText(con + " * " + "6.20  :"+ ec1);
                }

            } else if (i == 4 && consumption1 > (ecSlab1 + ecSlab2 + ecSlab3)) {
                total = consumption3 * ecrate4;
                totalCharge = totalCharge + total;
                String con = String.valueOf(consumption3);
                String ec2 = String.format("%.2f", total);
                result.append(con + " * " + "7.55  :" + ec2 + " \n");
//                energyCharge3.setText(con + " * " + "7.05  :"+ ec1);
            }
        }
        e1 = result.toString();
//        energyCharge.setText("..........EnergyCharge......." + "\n" + result);
        return totalCharge;

    }

    public double tax(double totalCharge) {
        double tax1 = (totalCharge * 0.09);
        return tax1;
    }

    public double prepaidRent(int noOfdays1) {
        double a = 375.00 / 30.00;
        double prepaidRent1 = a * noOfdays1;
        return prepaidRent1;
    }

    public double GST(double prepaidRent1) {
        double gst1 = prepaidRent1 * 0.18;
        return gst1;
    }

    public double GOK(double fc, double consumption, int tariff1, double e, double e23,String rebate,double tax,double bmd,double pf) {
        double gok = 0;
        if (tariff1 == 23 ) {
            if (consumption < 200) {
                gok = fc + e;
            } else {
                gok = (e - e23) + fc;
            }
        }else if (rebate.equals("5")||rebate.equals("10")||rebate.equals("12")){
            gok = e - (1.25 * consumption);
        }else if(tariff1 == 31){
            gok = fc + e + tax + bmd + pf;
        }
        return gok;
    }

    public double rebates_bill(double consumption, double fc, double ec, String rebate) {
        double rebate1 = 0.0;
        if (rebate.equals("1") || rebate.equals("6") || rebate.equals("11") || rebate.equals("12") || rebate.equals("14")) { //solar Rebate
            if (consumption <= 100) {//Solar
                rebate1 = consumption * 0.5;
            } else rebate1 = 50.00;
        } else if (rebate.equals("2")) {  //HandiCap
            rebate1 = (fc * 0.2) + (ec * 0.2);
        } else if (rebate.equals("7")) { //charity
            rebate1 = consumption * 0.25;
        }
        return rebate1;
    }
    public double todCalucalation(String constant,String currOnPeak,String currNormalPeak,String curroffPeak){
        double todMinRate= Double.parseDouble(mast_custs.get(0).getTOD_PREVIOUS1());
        double todNormalRate= 0.0;
        double todMaxRate= 0.0;

        double onPeak = (Double.parseDouble(currOnPeak)-todMinRate)*(Double.parseDouble(constant))*1;
        double normalPeak = (Double.parseDouble(currNormalPeak)-todNormalRate)*(Double.parseDouble(constant))*0;
        double offPeak = (Double.parseDouble(curroffPeak)-todMaxRate)*(Double.parseDouble(constant))*(-1) ;

        double totalTod = onPeak + normalPeak + offPeak;

        return totalTod;

    }

//    public double fixedRate(double load) {
//        int tariff1=0;
//        double fixedrate1 = 0.0;
//        StringBuffer buffer = new StringBuffer();
//        if (tariff1 == 20 || tariff1 == 23) {
//            double rem1load = 0, remload2 = 1;
//            if (load > 1) {
//                rem1load = load - 1;
//            } else {
//                remload2 = load;
//            }
//            int noOfSlab;
//
//            for (int i = 1; i <= 2; i++) {
//                double fixedrate = 0.0;
//                if ((remload2 <= 1) && i == 1) {
//                    fixedrate = fixedrate + 50 * remload2;
//                    String load4 = String.valueOf(remload2);
//                    String fc1 = String.format("%.2f", fixedrate);
//                    buffer.append(load4 + " * " + "50" + " : " + fc1 + " \n");
////                fixedcharge.setText(load4 + " * "+ "50" + " :   "+ fc1);
//                } else if (rem1load > 0) {
//                    fixedrate = fixedrate + 60 * rem1load;
//                    String load4 = String.valueOf(rem1load);
//                    String fc1 = String.format("%.2f", fixedrate);
//                    buffer.append(load4 + " * " + "60" + " : " + fc1 + " \n");
////                fixedcharge1.setText(load4 + " * "+ "60" + " :   "+ fc1);
//                }
//
//                fixedrate1 = fixedrate1 + fixedrate;
//            }
//
//
//        } else if (tariff1 == 70) {
//            double a = 200.0 / 7;
////            String days = String.format("%.2f",a);
////            double days1 = Double.parseDouble(days);
//            fixedrate1 = (load * noOfdays1 * a);
//            String fc = String.format("%.2f", fixedrate1);
//            buffer.append(load2 + " * " + noOfdays2 + " * " + "(200/7)   :" + fc);
//        }
//        fc10 = buffer.toString();
//        calValues.setFixedCharge(fc10);
//        fixedcharge.setText(".......Fixedcharge......." + "\n" + buffer);
//        return fixedrate1;
//    }

//    public double energyCharge(double consumption1) {
//
//        double consumption3 = 0;
//        StringBuffer result = new StringBuffer();
//        double con1 = 0;
////        result.append("consumption   :" + consumption1 + "\n");
//        totalCharge = 0;
//        switch (tariff1) {
//            case (20):
//                for (int i = 1; i <= 4; i++) {
//                    double total = 0;
//                    if (i == 1) {
//                        if (consumption1 > 30) {
//                            consumption3 = consumption1 - 30;
//                            total = 30 * 3.45;
//                            totalCharge = totalCharge + total;
//                            String to1 = String.valueOf(total);
//                            result.append("30" + " * " + "3.45  :" + to1 + " \n");
////                    energyCharge.setText("30" + " * " + "3.35  :"+ to1);
//
//                        } else {
//                            total = consumption1 * 3.45;
//                            totalCharge = totalCharge + total;
//                            String con = String.valueOf(consumption1);
//                            String ec2 = String.format("%.2f", total);
//                            result.append(con + " * " + "3.45  :" + ec2 + " \n");
////                    energyCharge.setText(con + " * " + "3.35  :"+ ec1);
//                        }
//                    } else if (i == 2 && consumption1 > 30) {
//                        if (consumption3 > 70) {
//                            consumption3 = consumption3 - 70;
//                            total = 70 * 4.95;
//                            totalCharge = totalCharge + total;
//                            String to1 = String.valueOf(total);
//                            result.append("70" + " * " + "4.95  :").append(to1).append(" \n");
////                    energyCharge1.setText("70" + " * " + "4.65  :"+ to1);
//                        } else {
//                            total = consumption3 * 4.95;
//                            totalCharge = totalCharge + total;
//                            String con = String.valueOf(consumption3);
//                            String ec2 = String.format("%.2f", total);
//                            result.append(con + " * " + "4.95  :" + ec2 + " \n");
////                    energyCharge1.setText(con + " * " + "4.65  :"+ ec1);
//                        }
//                    } else if (i == 3 && consumption1 > 100) {
//                        if (consumption3 > 100) {
//                            consumption3 = consumption3 - 100;
//                            total = 100 * 6.5;
//                            totalCharge = totalCharge + total;
//                            String to1 = String.valueOf(total);
//                            result.append("100" + " * " + "6.5  :" + to1 + " \n");
////                    energyCharge2.setText("100" + " * " + "6.2  :"+ to1);
//                        } else {
//                            total = consumption3 * 6.5;
//                            totalCharge = totalCharge + total;
//                            String con = String.valueOf(consumption3);
//                            String ec2 = String.format("%.2f", total);
//                            result.append(con + " * " + "6.5  :" + ec2 + " \n");
////                    energyCharge2.setText(con + " * " + "6.20  :"+ ec1);
//                        }
//
//                    } else if (consumption1 > 200) {
//                        total = consumption3 * 7.55;
//                        totalCharge = totalCharge + total;
//                        String con = String.valueOf(consumption3);
//                        String ec2 = String.format("%.2f", total);
//                        result.append(con + " * " + "7.55  :" + ec2 + " \n");
////                energyCharge3.setText(con + " * " + "7.05  :"+ ec1);
//                    }
//                }
//                break;
//            case 70:
//                double ecrate = consumption1 * 10.30;
//                totalCharge = totalCharge + ecrate;
//                String ec1 = String.format("%.2f", ecrate);
//                result.append(presentReading2 + " * " + " 10.30   :" + ec1);
//                break;
//            case 23:
//                for (int i = 1; i <= 4; i++) {
//                    double total = 0;
//
//                    if (i == 1) {
//                        if (consumption1 > 30) {
//                            consumption3 = consumption1 - 30;
//                            total = 30 * 3.45;
//                            totalCharge = totalCharge + total;
//                            String to1 = String.valueOf(total);
//                            result.append("30" + " * " + "3.45  :" + to1 + " \n");
////                    energyCharge.setText("30" + " * " + "3.35  :"+ to1);
//
//                        } else {
//                            total = consumption1 * 3.45;
//                            totalCharge = totalCharge + total;
//                            String con = String.valueOf(consumption1);
//                            String ec2 = String.format("%.2f", total);
//                            result.append(con + " * " + "3.45  :" + ec2 + " \n");
////                    energyCharge.setText(con + " * " + "3.35  :"+ ec1);
//                        }
//                    } else if (i == 2 && consumption1 > 30) {
//                        if (consumption3 > 70) {
//                            consumption3 = consumption3 - 70;
//                            total = 70 * 4.95;
//                            totalCharge = totalCharge + total;
//                            String to1 = String.valueOf(total);
//                            result.append("70" + " * " + "4.95  :").append(to1).append(" \n");
////                    energyCharge1.setText("70" + " * " + "4.65  :"+ to1);
//                        } else {
//                            total = consumption3 * 4.95;
//                            totalCharge = totalCharge + total;
//                            String con = String.valueOf(consumption3);
//                            String ec2 = String.format("%.2f", total);
//                            result.append(con + " * " + "4.95  :" + ec2 + " \n");
////                    energyCharge1.setText(con + " * " + "4.65  :"+ ec1);
//                        }
//                    } else if (i == 3 && consumption1 > 100) {
//                        if (consumption3 > 100) {
//                            consumption3 = consumption3 - 100;
//                            total = 100 * 6.5;
//                            totalCharge = totalCharge + total;
//                            String to1 = String.valueOf(total);
//                            result.append("100" + " * " + "6.5  :" + to1 + " \n");
////                    energyCharge2.setText("100" + " * " + "6.2  :"+ to1);
//                        } else {
//                            total = consumption3 * 6.5;
//                            totalCharge = totalCharge + total;
//                            String con = String.valueOf(consumption3);
//                            String ec2 = String.format("%.2f", total);
//                            result.append(con + " * " + "6.5  :" + ec2 + " \n");
////                    energyCharge2.setText(con + " * " + "6.20  :"+ ec1);
//                        }
//
//                    } else if (consumption1 > 200) {
//                        total = consumption3 * 7.55;
//                        totalCharge = totalCharge + total;
//                        String con = String.valueOf(consumption3);
//                        String ec2 = String.format("%.2f", total);
//                        result.append(con + " * " + "7.55  :" + ec2 + " \n");
////                energyCharge3.setText(con + " * " + "7.05  :"+ ec1);
//                    }
//                }
//                break;
//
//        }
//        e1 = result.toString();
//        energyCharge.setText("..........EnergyCharge......." + "\n" + result);
//        return totalCharge;
//    }
}
