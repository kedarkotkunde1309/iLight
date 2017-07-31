package com.example.kedarkotkunde.myapplication;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;

import com.example.kedarkotkunde.myapplication.framework.BluetoothLeService;
import com.example.kedarkotkunde.myapplication.framework.SampleGattAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BLEDeviceInfoActivity extends Activity {


    private TextView textviewDeviceName, textviewDeviceAddress, textviewstate, textviewservices, textviewretriveData;

    private String mDeviceName, mDeviceAddress;
    private BluetoothLeService mBluetoothLeService;
    private boolean mConnected = false;
    public final static UUID TX_UID_UPDATE =
            UUID.fromString(SampleGattAttributes.READ_TX);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ble_device_info);
        textviewDeviceName = (TextView) findViewById(R.id.textviewDeviceName);
        textviewDeviceAddress = (TextView) findViewById(R.id.textviewDeviceAddress);
        textviewstate = (TextView) findViewById(R.id.textviewstate);
        textviewservices = (TextView) findViewById(R.id.textviewservices);
        textviewretriveData = (TextView) findViewById(R.id.textviewservices);
        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(BLEConstants.EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(BLEConstants.EXTRAS_DEVICE_ADDRESS);
        textviewDeviceName.setText("Device Name : " + mDeviceName);
        textviewDeviceAddress.setText("Device Address : " + mDeviceAddress);

        // connect to the BLE device
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            LogUtils.d("Connect request result=" + result);
        }
    }


    public void readValue(View v){
        readAllTheservices();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = null;//((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                LogUtils.d("Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;

                LogUtils.d("Conncted to the device =====");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        textviewstate.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                        textviewstate.setText("Device Connected");
                    }
                });

                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                textviewstate.setTextColor(getResources().getColor(R.color.darkred, null));
                textviewstate.setText("Device Disconnected");
                textviewretriveData.setText("");

                invalidateOptionsMenu();

            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                //displayAllServices(mBluetoothLeService.getSupportedGattServices());

                LogUtils.d("Service discoved now ==");
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
                readAllTheservices();

            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                //displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
                //textviewretriveData.setText(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    private BluetoothGattCharacteristic mNotifyCharacteristic;



    private void readAllTheservices() {
        if (mGattCharacteristics != null) {

            for (int i = 0; i < mGattCharacteristics.size(); i++) {
                ArrayList<BluetoothGattCharacteristic> arrayList = mGattCharacteristics.get(i);
                for (int m = 0; m < arrayList.size(); m++) {
                    BluetoothGattCharacteristic characteristic = arrayList.get(m);
                    final int charaProp = characteristic.getProperties();
                    LogUtils.d("read the  UUID " + characteristic.getUuid().toString());
                    if (characteristic.getUuid().toString().equals("6e400003-b5a3-f393-e0a9-e50e24dcca9e")) {
                        LogUtils.d("read the  UUID check for the property " );
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
//                            if (mNotifyCharacteristic != null) {
//                                mBluetoothLeService.setCharacteristicNotification(
//                                        mNotifyCharacteristic, false);
//                                mNotifyCharacteristic = null;
//                                LogUtils.d("read the  not null part" );
//                            }
                            mBluetoothLeService.readCharacteristic(characteristic);
                            LogUtils.d("read the  UUID check for tPROPERTY_READ " );
                        }
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                            mNotifyCharacteristic = characteristic;
                            mBluetoothLeService.setCharacteristicNotification(
                                    characteristic, true);
                            mBluetoothLeService.readCharacteristic(characteristic);
                            LogUtils.d("read the  UUID check for PROPERTY_NOTIFY " );
                            //mBluetoothLeService.readCharacteristic(characteristic);

                        }
                    }
                }
            }


//            final BluetoothGattCharacteristic characteristic =
//                    mGattCharacteristics.get(groupPosition).get(childPosition);
//            final int charaProp = characteristic.getProperties();
//            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
//                // If there is an active notification on a characteristic, clear
//                // it first so it doesn't update the data field on the user interface.
//                if (mNotifyCharacteristic != null) {
//                    mBluetoothLeService.setCharacteristicNotification(
//                            mNotifyCharacteristic, false);
//                    mNotifyCharacteristic = null;
//                }
//                mBluetoothLeService.readCharacteristic(characteristic);
//            }
//            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
//                mNotifyCharacteristic = characteristic;
//                mBluetoothLeService.setCharacteristicNotification(
//                        characteristic, true);
//            }
//            return true;
        }
    }


    private void displayAllServices(List<BluetoothGattService> list) {
        if (list == null) {
            LogUtils.d("service list is empty");
            return;
        }
        final StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            BluetoothGattService bluetoothGattService = list.get(i);
            buffer.append("UUID: " + bluetoothGattService.getUuid() + "\n\n");
            LogUtils.d("UUID for the ==" + bluetoothGattService.getUuid());

            List<android.bluetooth.BluetoothGattCharacteristic> characteristics = bluetoothGattService.getCharacteristics();
            for (int m = 0; m < characteristics.size(); m++) {
                BluetoothGattCharacteristic bluetoothGattCharacteristic = characteristics.get(m);
                LogUtils.d("===" + bluetoothGattCharacteristic.getUuid());
                buffer.append("UUID =" + bluetoothGattCharacteristic.getUuid() + " \n");

                if (bluetoothGattCharacteristic.getUuid().toString().equals(SampleGattAttributes.READ_TX)) {
                    LogUtils.d("===set to notification true");
                    List<android.bluetooth.BluetoothGattDescriptor> list1 = bluetoothGattCharacteristic.getDescriptors();
                    for (int a = 0; a < list1.size(); a++) {
                        BluetoothGattDescriptor bluetoothGattDescriptor = list1.get(a);
                        LogUtils.d("BLE DESCRIBER===" + bluetoothGattDescriptor.getUuid());
                    }
                    setToReadNotify(bluetoothGattCharacteristic);
                }

            }
            buffer.append("\n");

        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                textviewservices.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                textviewservices.setText(" " + buffer.toString());
            }
        });
    }

    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void displayGattServices(List<BluetoothGattService> gattServices) {
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
                gattCharacteristicGroupData.add(currentCharaData);
            }
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }


    }


    public void setToReadNotify(BluetoothGattCharacteristic characteristic) {

        final int charaProp = characteristic.getProperties();
        LogUtils.d("read the property =" + charaProp);
        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {

            mBluetoothLeService.setCharacteristicNotification(
                    characteristic, true);

        }
    }
}