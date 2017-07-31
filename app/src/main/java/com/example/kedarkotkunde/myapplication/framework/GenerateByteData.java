package com.example.kedarkotkunde.myapplication.framework;

import android.text.TextUtils;

import com.example.kedarkotkunde.myapplication.LogUtils;
import com.example.kedarkotkunde.myapplication.model.ParseBLEValue;
import com.example.kedarkotkunde.myapplication.model.ParseModelValues;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kedarkotkunde on 7/26/17.
 */

public class GenerateByteData {

    private static  GenerateByteData generateByteData ;

    private GenerateByteData(){

    }

    public static GenerateByteData getInstance(){
        if(generateByteData == null){
            generateByteData = new GenerateByteData();
        }
        return  generateByteData;
    }


    public   byte[]  setProvisioned(List<ParseModelValues> arrayList, int GroupId){
        byte [] data = null;
        if(arrayList != null){
            if(arrayList.size() ==1){
                // its unicast
                LogUtils.d("its Unicast");
                //ParseModelValues parseModelValues =arrayList.get(0);
                data=generateByteStreamForProvision(false,arrayList,GroupId );
            }else{
                // its broadcast multiple lights
                LogUtils.d("its Broadcast");
                data=generateByteStreamForProvision(true,arrayList,GroupId);

            }
        }
        return  data;

    }


    public   byte[]  setIndivisualProvision(List<ParseModelValues> arrayList){
        byte [] data = null;
        if(arrayList != null){
            if(arrayList.size() ==1){
                // its unicast
                LogUtils.d("its Unicast");
                //ParseModelValues parseModelValues =arrayList.get(0);
                data=generateByteStreamForIndivisualProvision(arrayList );
            }else{
                // its broadcast multiple lights
                LogUtils.d("its Broadcast for indivisual");
               // data=generateByteStreamForProvision(true,arrayList,00);

            }
        }
        return  data;

    }


    public byte[] setLightOn( int GroupId, String lightAddress, int warmcool , int intensity){
        byte [] data = null;
        byte channelOne =(byte) getChannel1(warmcool, intensity);
        byte channeltwo =(byte) getChannel2(warmcool, intensity);
        LogUtils.d("setLightOn Channel One value "+channelOne);
        LogUtils.d("setLightOn Channel two value "+channeltwo);
        if(!TextUtils.isEmpty(lightAddress)){
            // unicast
            LogUtils.d("Its Unicast for light on");
            ParseModelValues parseModelValues= ParseBLEValue.getInstance().getLightFromAddress(lightAddress);
            data = new byte[]{01,00,01, parseModelValues.getByteAddress(),channelOne, channeltwo};
            printByteArray(data);
        }else{
            // broadcast
            LogUtils.d("Its broadcast for light on");
            data = new byte[]{01,01,01, getGroupId(GroupId),channelOne, channeltwo};
            printByteArray(data);
        }

            return  data;

    }



    public byte[] setIntensity( int GroupId, String lightAddress, int warmcool , int intensity){
        byte [] data = null;
        byte channelOne =(byte) getChannel1(warmcool, intensity);
        byte channeltwo =(byte) getChannel2(warmcool, intensity);

        if(!TextUtils.isEmpty(lightAddress)){
            // unicast
            LogUtils.d("Its Unicast for light on");
            ParseModelValues parseModelValues= ParseBLEValue.getInstance().getLightFromAddress(lightAddress);
            data = new byte[]{01,00,01, parseModelValues.getByteAddress(),channelOne, channeltwo};
            printByteArray(data);
        }else{
            // broadcast
            LogUtils.d("Its broadcast for light on");
            data = new byte[]{01,01,01, getGroupId(GroupId),channelOne, channeltwo};
            printByteArray(data);
        }

        return  data;

    }


    public byte[] setWarmCool( int GroupId, String lightAddress, int warmcool , int intensity){
        byte [] data = null;
        byte channelOne =(byte) getChannel1(warmcool, intensity);
        byte channeltwo =(byte) getChannel2(warmcool, intensity);

        if(!TextUtils.isEmpty(lightAddress)){
            // unicast
            LogUtils.d("Its Unicast for light on");
            ParseModelValues parseModelValues= ParseBLEValue.getInstance().getLightFromAddress(lightAddress);
            data = new byte[]{01,00,01, parseModelValues.getByteAddress(),channelOne, channeltwo};
            printByteArray(data);
        }else{
            // broadcast
            LogUtils.d("Its broadcast for light on");
            data = new byte[]{01,01,01, getGroupId(GroupId),channelOne, channeltwo};
            printByteArray(data);
        }

        return  data;

    }

    public byte[] setLightOff( int GroupId, String lightAddress, int warmcool , int intensity){
        byte [] data = null;
        byte channelOne =(byte) getChannel1(warmcool, intensity);
        byte channeltwo =(byte) getChannel2(warmcool, intensity);
        LogUtils.d("setLightoff Channel One value "+channelOne);
        LogUtils.d("setLightoff Channel two value "+channeltwo);
        if(!TextUtils.isEmpty(lightAddress)){
            // unicast
            LogUtils.d("Its Unicast  for light off");
            ParseModelValues parseModelValues= ParseBLEValue.getInstance().getLightFromAddress(lightAddress);
            data = new byte[]{01,00,00, parseModelValues.getByteAddress(),channelOne, channeltwo};
            printByteArray(data);
        }else{
            // broadcast
            LogUtils.d("Its broadcast for light off ");
            data = new byte[]{01,01,00, getGroupId(GroupId),channelOne, channeltwo};
            printByteArray(data);
        }

        return  data;

    }


    public byte[] setAutoModeOn( int GroupId, String lightAddress){
        byte [] data = null;

        if(!TextUtils.isEmpty(lightAddress)){
            // unicast
            LogUtils.d("Its Unicast  for Auto mode On");
            ParseModelValues parseModelValues= ParseBLEValue.getInstance().getLightFromAddress(lightAddress);
            data = new byte[]{02,00, parseModelValues.getByteAddress(),01,05};
            printByteArray(data);
        }else{
            // broadcast
            LogUtils.d("Its broadcast for light off ");
            data = new byte[]{02,01, getGroupId(GroupId),01, 05};
            printByteArray(data);
        }

        return  data;

    }

    public byte[] setAutoModeoff( int GroupId, String lightAddress){
        byte [] data = null;

        if(!TextUtils.isEmpty(lightAddress)){
            // unicast
            LogUtils.d("Its Unicast  for Auto mode On");
            ParseModelValues parseModelValues= ParseBLEValue.getInstance().getLightFromAddress(lightAddress);
            data = new byte[]{02,00, parseModelValues.getByteAddress(),00,05};
            printByteArray(data);
        }else{
            // broadcast
            LogUtils.d("Its broadcast for light off ");
            data = new byte[]{02,01, getGroupId(GroupId),00, 05};
            printByteArray(data);
        }

        return  data;

    }

    public byte[] update(){
        return  new  byte[]{03,00,00};
    }

    private int getChannel1(int warmcool , int intensity){
            int warm = 100- warmcool;
           int finalValue = warm * intensity/100;
        return  finalValue;

    }

    private int getChannel2(int warmcool , int intensity){
       int finalvalue = intensity *warmcool/100;
        //return String.format("%02x", finalvalue&0xff);
        return  finalvalue;
    }

    /*
    *  parameter groupid: is the index of group selected by the grid
    */
    private byte getGroupId(int groupid){
        byte groupInfo=00;
        switch (groupid){
            case  0:
                // G1 group
                groupInfo =01;
                break;
            case  1:
                groupInfo =02;
                break;
            case  2:
                groupInfo =03;
                break;
            case  3:
                groupInfo =04;
                break;

        }
        return groupInfo;
    }


    private  byte[] generateByteStreamForProvision(boolean isBroadcast, List<ParseModelValues> parseModelValues, int groupid){
        byte [] byteData = null;
        byte groupInfo =getGroupId(groupid);
        if(isBroadcast){
            ArrayList<Byte> byteArrayList = new ArrayList<>();
            byteArrayList.add((byte)00);
            byteArrayList.add((byte)01);
            byteArrayList.add((byte)01);
            byteArrayList.add((byte)groupInfo);

            // bulb address goes here
            for (int i=0; i <parseModelValues.size(); i++){
                ParseModelValues parseModel = parseModelValues.get(i);
                if(parseModel != null){
                    byteArrayList.add((byte)parseModel.getByteAddress());
                }
            }

            // fill other address with zero
            for (int m=byteArrayList.size() ; m <20; m++){
                byteArrayList.add((byte)00);
            }

            // convert the arraylist to byte array
            byteData= new byte[byteArrayList.size()];
            for (int k=0 ; k < byteArrayList.size(); k++){
                byteData[k]= byteArrayList.get(k);
                LogUtils.d("Broadcast command "+byteData[k]);
            }


        }else{
            /*
            *  00,00,01,groupInfo, byteaddress
            *
            *  00: 0 for provision , 1 for light command
            *  00: 0 for unicase 1 for broadcast
            *  01: 0 for deprovision and 1 for provision
            *  groupinfo: group information
            *  byteaddress: byte address for the light
            *
             */
            byte  byteaddress =parseModelValues.get(0).getByteAddress();
            byteData = new byte[]{00,00,01,groupInfo, byteaddress};
        }
        return  byteData;
    }


    private  byte[] generateByteStreamForIndivisualProvision(List<ParseModelValues> parseModelValues){
        byte [] byteData = null;
        byte  byteaddress =parseModelValues.get(0).getByteAddress();
         byteData = new byte[]{00,00,01,00, byteaddress};
        return  byteData;
    }



    private void printByteArray(byte [] arr){
        if(arr != null){
            for (int i=0; i < arr.length; i++){
                LogUtils.d("printByteArray = "+arr[i]);
            }
        }
    }


}
