package com.example.kedarkotkunde.myapplication.framework;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;

import com.example.kedarkotkunde.myapplication.BLEConstants;
import com.example.kedarkotkunde.myapplication.LogUtils;
import com.example.kedarkotkunde.myapplication.R;
import com.example.kedarkotkunde.myapplication.model.ParseBLEValue;
import com.example.kedarkotkunde.myapplication.model.ParseModelValues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kedarkotkunde on 7/24/17.
 */

public class ConnectToBLE {

    private static ConnectToBLE connectToBLE ;
    private BluetoothLeService mBluetoothLeService;
    private  String mDeviceAddress;
    private static IDeviceState iDeviceState;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

    private int warmcool_value;
    private int intensity_value;

    private  ConnectToBLE(){

    }

    public static  ConnectToBLE getInstance(IDeviceState iDeviceState_){
        if(connectToBLE == null){
            connectToBLE = new ConnectToBLE();
        }
       iDeviceState =iDeviceState_;
        return  connectToBLE;
    }
    public static  ConnectToBLE getInstance(){
        if(connectToBLE == null){
            connectToBLE = new ConnectToBLE();
        }

        return  connectToBLE;
    }
    public void startBLEService(Context context, String mDeviceAddress){

        LogUtils.d("Start the BLE service ");
        this.mDeviceAddress = mDeviceAddress;
        Intent gattServiceIntent = new Intent(context, BluetoothLeService.class);
        context.bindService(gattServiceIntent, mServiceConnection, context.BIND_AUTO_CREATE);
    }

    public void registerBLEBroadcast(Context context){
        context.registerReceiver(mGattUpdateReceiver, Utility.makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            LogUtils.d("Connect request result=" + result);
        }
    }

    public void unRegisterBLEService(Context context){
        context.unbindService(mServiceConnection);
        mBluetoothLeService.disconnect();
        mBluetoothLeService = null;
    }

    public void  unRegisterBroadcast(Context context){
        context.unregisterReceiver(mGattUpdateReceiver);
    }


    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                if(iDeviceState!= null)
                iDeviceState.onDeviceConnected();

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                if(iDeviceState!= null)
                iDeviceState.onDeviceDisConnected();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                updateAvailableGattServices(mBluetoothLeService.getSupportedGattServices());
                if(iDeviceState!= null) {
                    iDeviceState.onServiceDiscovered();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable(){
                        @Override
                        public void run(){
                            LogUtils.d("Run update command");
                            ConnectToBLE.getInstance().runCommand(null,BLEConstants.TYPE_UPDATE, -1, "");
                        }
                    }, 200);

                }

            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                // sample Data :  01 10 32 32
                String valueFromBLE =intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                LogUtils.d("Framework : Value from BLE  "+valueFromBLE);
                if(iDeviceState!= null)
                iDeviceState.onDataAvailable();
            }
        }
    };

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                LogUtils.d("Unable to initialize Bluetooth");

            }
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };


    private void updateAvailableGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = "Unknown";
        String unknownCharaString = "Unknown charar";
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData
                = new ArrayList<ArrayList<HashMap<String, String>>>();
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(
                    LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData =
                    new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas =
                    new ArrayList<BluetoothGattCharacteristic>();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();
                currentCharaData.put(
                        LIST_NAME, SampleGattAttributes.lookup(uuid, unknownCharaString));
                currentCharaData.put(LIST_UUID, uuid);
                LogUtils.d("UUIds ="+uuid);
                gattCharacteristicGroupData.add(currentCharaData);
                EnableNotifyForCharacter(gattCharacteristic);
            }
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }

    }

    private  void  EnableNotifyForCharacter(BluetoothGattCharacteristic characteristic) {
        if (characteristic.getUuid().toString().equals(BLEConstants.CONNECT_TO_ADDRESS)) {
            final int charaProp = characteristic.getProperties();
            LogUtils.d("read the  UUID check for the property ");
            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                mBluetoothLeService.readCharacteristic(characteristic);
                LogUtils.d("read the  UUID check for tPROPERTY_READ ");
            }
            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                mBluetoothLeService.setCharacteristicNotification(
                        characteristic, true);
                mBluetoothLeService.readCharacteristic(characteristic);
                LogUtils.d("read the  UUID check for PROPERTY_NOTIFY ");
            }
        }
    }

    public void runCommand(List<ParseModelValues> arrayList, int command_type, int GroupId, String lightAddress){
        switch (command_type){
            case  BLEConstants.TYPE_PROVISION:
                writeCharac(GenerateByteData.getInstance().setProvisioned(arrayList,GroupId));
                break;

            case  BLEConstants.TYPE_INDIVISUAL_PROVISION:
                writeCharac(GenerateByteData.getInstance().setIndivisualProvision(arrayList));
                break;


            case  BLEConstants.TYPE_LIGHT_ON:
                writeCharac(GenerateByteData.getInstance().setLightOn(GroupId,lightAddress,warmcool_value, intensity_value));
                break;

            case  BLEConstants.TYPE_LIGHT_OFF:
                writeCharac(GenerateByteData.getInstance().setLightOff(GroupId,lightAddress,warmcool_value, intensity_value));
                break;

            case  BLEConstants.TYPE_WARM_COOL:
                writeCharac(GenerateByteData.getInstance().setWarmCool(GroupId,lightAddress,warmcool_value, intensity_value));
                break;

            case  BLEConstants.TYPE_INTENSITY_COOL:
                writeCharac(GenerateByteData.getInstance().setIntensity(GroupId,lightAddress,warmcool_value, intensity_value));
                break;

            case  BLEConstants.TYPE_AUTO_ON:
                writeCharac(GenerateByteData.getInstance().setAutoModeOn(GroupId,lightAddress));
                break;

            case  BLEConstants.TYPE_AUTO_OFF:
                writeCharac(GenerateByteData.getInstance().setAutoModeoff(GroupId,lightAddress));
                break;

            case  BLEConstants.TYPE_UPDATE:
                writeCharac(GenerateByteData.getInstance().update());
                break;

        }
    }

    public void ValuesFromSlider(int warmcool, int intensity){
        this.intensity_value = intensity;
        this.warmcool_value = warmcool;

    }

    private void  writeCharac(byte [] data){
        if (mGattCharacteristics != null) {
            for (int i = 0; i < mGattCharacteristics.size(); i++) {
                ArrayList<BluetoothGattCharacteristic> arrayList = mGattCharacteristics.get(i);
                for (int m = 0; m < arrayList.size(); m++) {
                    BluetoothGattCharacteristic characteristic = arrayList.get(m);
                    final int charaProp = characteristic.getProperties();
                    if (characteristic.getUuid().toString().equals(BLEConstants.WRITE_TO_ADDRESS)) {
                        LogUtils.d("write start  ");
                        mBluetoothLeService.writeToChar(data,characteristic);
                        break;
                    }
                }
            }
        }
    }

}
