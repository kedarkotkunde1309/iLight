package com.example.kedarkotkunde.myapplication.model;

import com.example.kedarkotkunde.myapplication.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kedarkotkunde on 7/24/17.
 */

public class ParseBLEValue {

    private static ParseBLEValue parseBLEValue;
    private static Map<String, ParseModelValues> hashmapParseValues = new HashMap<>();

    private ParseBLEValue() {

    }

    public static ParseBLEValue getInstance() {
        if (parseBLEValue == null) {
            parseBLEValue = new ParseBLEValue();
        }
        return parseBLEValue;
    }

    public void addAddressEntry(byte[] valueFromBLE) {
       updateParseModel(valueFromBLE);
    }


    /*
        sample values:
         byte [0]   1
         byte [1]   25
         byte [2]   50
         byte [3]   50
     */

    private ParseModelValues updateParseModel(byte[] byteArray) {
        ParseModelValues parseModelValues = new ParseModelValues();
        LogUtils.d("parsing start *** ***");
        for (int i = 0; i < byteArray.length; i++) {
            byte b1 = (byte) byteArray[i];
            switch (i) {
                case 0:
                    // provision and on off flag based

                    String s1 = String.format("%8s", Integer.toBinaryString(b1 & 0xFF)).replace(' ', '0');
                    LogUtils.d("byte string  "+s1);
                    char[] chararray = s1.toCharArray();
                    char isvalid = chararray[chararray.length-1];
                    char isProvisioned = chararray[chararray.length - 2];
                    char isOnOff = chararray[chararray.length - 3];
                    parseModelValues.setValidFlag(Integer.parseInt(String.valueOf(isvalid)));
                    parseModelValues.setProvisionFlag(Integer.parseInt(String.valueOf(isProvisioned)));
                    parseModelValues.setOnOffFlag(Integer.parseInt(String.valueOf(isOnOff)));
                    // group address calculation
                    int addressCalculation = 0;
                    for (int m = 0; m < 4; m++) {
                        if (chararray[m] == '1') {
                            switch (m) {
                                case 0:
                                    addressCalculation = addressCalculation + 8;
                                    break;
                                case 1:
                                    addressCalculation = addressCalculation + 4;

                                    break;
                                case 2:
                                    addressCalculation = addressCalculation + 2;
                                    break;
                                case 3:
                                    addressCalculation = addressCalculation + 1;
                                    break;
                            }
                        }
                    }
                    parseModelValues.setUnUsedFlag(addressCalculation);

                    LogUtils.d("parsing isValid  "+isvalid);
                    LogUtils.d("parsing isOnOff  "+isOnOff);
                    LogUtils.d("parsing isProvisioned  "+isProvisioned);
                    LogUtils.d("parsing group address  "+addressCalculation);
                    break;
                case 1:
                    parseModelValues.setAddressValue(String.format("%02x", b1&0xff));
                    parseModelValues.setByteAddress(b1);
                    LogUtils.d("parsing group Light address in byte "+b1);
                    LogUtils.d("parsing group Light address in hex "+parseModelValues.getAddressValue());
                    break;
                case 2:
                    parseModelValues.setChannelOne(String.format("%02x", b1&0xff));
                    parseModelValues.setByteAddressForChannelOne(b1);
                    break;
                case 3:
                    parseModelValues.setChannelTwo(String.format("%02x", b1&0xff));
                    parseModelValues.setByteAddressForChannelTwo(b1);
                    break;

            }
        }
        LogUtils.d("parsing End *** ***");
        if(!hashmapParseValues.containsKey(parseModelValues.getAddressValue()))
        parseModelValues.setDisplayName("L"+hashmapParseValues.size());
        hashmapParseValues.put(parseModelValues.getAddressValue(), parseModelValues);
        return parseModelValues;
    }


    public List<ParseModelValues> getListOfLights(){
        List<ParseModelValues> list = new ArrayList<ParseModelValues>(hashmapParseValues.values());
       return list;
    }


    public ParseModelValues getLightFromAddress(String lightaddress){
        return hashmapParseValues.get(lightaddress);

    }

    public List<ParseModelValues> getLightsAssignedForGroup(String groupAddress){
        List<ParseModelValues> list = new ArrayList<ParseModelValues>();
        for (Map.Entry<String, ParseModelValues> entry : hashmapParseValues.entrySet())
        {
            ParseModelValues parseModelValues = entry.getValue();
            if(parseModelValues!= null && String.valueOf(parseModelValues.getUnUsedFlag()).equalsIgnoreCase(groupAddress)){
                list.add(parseModelValues);
            }

        }
        return  list;
    }



}
