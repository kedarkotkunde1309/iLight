package com.example.kedarkotkunde.myapplication.framework;

import android.content.IntentFilter;

import com.example.kedarkotkunde.myapplication.framework.BluetoothLeService;

/**
 * Created by kedarkotkunde on 7/24/17.
 */

public class Utility {

    public static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

}
