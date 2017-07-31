package com.example.kedarkotkunde.myapplication.framework;


/**
 * Created by kedarkotkunde on 7/24/17.
 */

public interface IDeviceState  {


    public void onDeviceConnected();
    public void onDeviceDisConnected();
    public void onServiceDiscovered();
    public void onDataAvailable();

}
