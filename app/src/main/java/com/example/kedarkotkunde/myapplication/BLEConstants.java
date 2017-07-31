package com.example.kedarkotkunde.myapplication;

/**
 * Created by kedarkotkunde on 7/24/17.
 */

public class BLEConstants {

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static final String EXTRAS_LIGHT_NAME = "LIGHT_NAME";
    public static final String EXTRAS_LIGHT_GROUP= "GROUP_NAME";
    public static final String EXTRAS_LIGHT_ADDRESS= "LIGHT_ADDRESS";
    public static final String EXTRAS_GROUP_ADDRESS= "GROUP_ADDRESS";
    public static final String EXTRAS_LIGHT_ON_OFF_STATE= "LIGHT_ON_OFF_STATE";
    public static final String EXTRAS_WARM_COOL= "WARM_COOL";
    public static final String EXTRAS_LIGHT_INTENSITY= "LIGHT_INTENSITY";

    public static final String CONNECT_TO_ADDRESS ="6e400003-b5a3-f393-e0a9-e50e24dcca9e";

    public static final String WRITE_TO_ADDRESS ="6e400002-b5a3-f393-e0a9-e50e24dcca9e";

    public static final int  TYPE_PROVISION= 1;
    public static final int  TYPE_INDIVISUAL_PROVISION= 2;
    public static final int  TYPE_LIGHT_ON= 3;
    public static final int  TYPE_LIGHT_OFF= 4;
    public static final int  TYPE_AUTO_ON= 5;
    public static final int  TYPE_AUTO_OFF= 6;
    public static final int  TYPE_UPDATE= 7;
    public static final int  TYPE_WARM_COOL= 8;
    public static final int  TYPE_INTENSITY_COOL= 9;
}
