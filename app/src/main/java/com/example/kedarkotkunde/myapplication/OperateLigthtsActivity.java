package com.example.kedarkotkunde.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.kedarkotkunde.myapplication.framework.ConnectToBLE;


public class OperateLigthtsActivity extends Activity {


    private android.support.v7.widget.CardView cardviewAuto, cardViewLight, cardviewIntensity, cardviewwarm_cool;
    private SeekBar seekbar_warm_cool, seekbar_intensity;
    private Switch switch_light,switch_auto;
    private  TextView textview_Group_name, light_name, text_intensity_level,text_warm_cool_level;
    private  String lightAddress="", groupAddress="";
    private  boolean isUnicast= false;

    private ImageView imageView_backkey;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operate_light);
        cardviewAuto =(android.support.v7.widget.CardView)findViewById(R.id.card_view_auto);
        cardViewLight =(android.support.v7.widget.CardView)findViewById(R.id.card_view_light);
        cardviewIntensity =(android.support.v7.widget.CardView)findViewById(R.id.card_view_intensity);
        cardviewwarm_cool =(android.support.v7.widget.CardView)findViewById(R.id.card_view_warm_cool);
        seekbar_warm_cool=(SeekBar)findViewById(R.id.seekbar_warm_cool);
        seekbar_intensity=(SeekBar)findViewById(R.id.seekbar_intensity);
        imageView_backkey = (ImageView) findViewById(R.id.backkey);
        imageView_backkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        switch_light=(Switch)findViewById(R.id.switch_light);
        switch_auto=(Switch)findViewById(R.id.switch_auto);
        textview_Group_name =(TextView) findViewById(R.id.textview_Group_name);
        light_name =(TextView) findViewById(R.id.light_name);
        text_intensity_level =(TextView) findViewById(R.id.text_intensity_level);
        text_warm_cool_level =(TextView) findViewById(R.id.text_warm_cool_level);
        seekbar_warm_cool.setProgress(50);
        seekbar_intensity.setProgress(100);
        text_intensity_level.setText(""+100);
        text_warm_cool_level.setText(""+50);
        setAddressDetails();
        handleAutoSelect();
        handleLightOnOff();
        handleSeekbarIntensity();
        handleSeekbarWarmCool();

    }


    private void handleSeekbarIntensity(){
        seekbar_intensity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                LogUtils.d("Seekbar value "+progress);
                text_intensity_level.setText(""+progress);

                if(switch_light.isChecked()){
                    ConnectToBLE.getInstance().ValuesFromSlider(seekbar_warm_cool.getProgress(), seekbar_intensity.getProgress());
                    if(isUnicast== true){
                        // no group for this
                        ConnectToBLE.getInstance().runCommand(null,BLEConstants.TYPE_INTENSITY_COOL, -1, lightAddress);
                    }else{
                        // broadcast
                        ConnectToBLE.getInstance().runCommand(null,BLEConstants.TYPE_INTENSITY_COOL, Integer.parseInt(groupAddress),"");
                    }
                }
            }
        });
    }


    private void handleLightOnOff(){
        switch_light.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ConnectToBLE.getInstance().ValuesFromSlider(seekbar_warm_cool.getProgress(), seekbar_intensity.getProgress());
              if(isChecked){
                  // turn on the light
                  if(isUnicast== true){
                      // no group for this
                      ConnectToBLE.getInstance().runCommand(null,BLEConstants.TYPE_LIGHT_ON, -1, lightAddress);
                  }else{
                      // broadcast
                      ConnectToBLE.getInstance().runCommand(null,BLEConstants.TYPE_LIGHT_ON, Integer.parseInt(groupAddress),"");
                  }


              }else{
                  // turn off the light
                  if(isUnicast== true){
                      // no group for this
                      ConnectToBLE.getInstance().runCommand(null,BLEConstants.TYPE_LIGHT_OFF, -1, lightAddress);
                  }else{
                      // broadcast
                      ConnectToBLE.getInstance().runCommand(null,BLEConstants.TYPE_LIGHT_OFF, Integer.parseInt(groupAddress), "");
                  }

              }

            }
        });


    }


    private void handleSeekbarWarmCool(){
        seekbar_warm_cool.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                LogUtils.d("Seekbar value "+progress);
                text_warm_cool_level.setText(""+progress);
                if(switch_light.isChecked()){
                    ConnectToBLE.getInstance().ValuesFromSlider(seekbar_warm_cool.getProgress(), seekbar_intensity.getProgress());
                    if(isUnicast== true){
                        // no group for this
                        ConnectToBLE.getInstance().runCommand(null,BLEConstants.TYPE_WARM_COOL, -1, lightAddress);
                    }else{
                        // broadcast
                        ConnectToBLE.getInstance().runCommand(null,BLEConstants.TYPE_WARM_COOL, Integer.parseInt(groupAddress),"");
                    }
                }

            }
        });
    }


    private void handleAutoSelect(){
        switch_auto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) {
                    cardViewLight.setBackgroundColor(getResources().getColor(R.color.lightgrey, null));
                    cardviewIntensity.setBackgroundColor(getResources().getColor(R.color.lightgrey, null));
                    cardviewwarm_cool.setBackgroundColor(getResources().getColor(R.color.lightgrey, null));
                    switch_light.setEnabled(false);
                    seekbar_warm_cool.setEnabled(false);
                    seekbar_intensity.setEnabled(false);
                    seekbar_intensity.setProgress(100);
                    seekbar_warm_cool.setProgress(50);
                    ConnectToBLE.getInstance().ValuesFromSlider(seekbar_warm_cool.getProgress(), seekbar_intensity.getProgress());
                    if(isUnicast== true){
                        // no group for this
                        ConnectToBLE.getInstance().runCommand(null,BLEConstants.TYPE_AUTO_ON, -1, lightAddress);
                    }else{
                        // broadcast
                        ConnectToBLE.getInstance().runCommand(null,BLEConstants.TYPE_AUTO_ON, Integer.parseInt(groupAddress),"");
                    }

                }else{
                    cardViewLight.setBackgroundColor(getResources().getColor(R.color.white, null));
                    cardviewIntensity.setBackgroundColor(getResources().getColor(R.color.white, null));
                    cardviewwarm_cool.setBackgroundColor(getResources().getColor(R.color.white, null));
                    switch_light.setEnabled(true);
                    seekbar_warm_cool.setEnabled(true);
                    seekbar_intensity.setEnabled(true);
                    ConnectToBLE.getInstance().ValuesFromSlider(seekbar_warm_cool.getProgress(), seekbar_intensity.getProgress());
                    if(isUnicast== true){
                        // no group for this
                        ConnectToBLE.getInstance().runCommand(null,BLEConstants.TYPE_AUTO_OFF, -1, lightAddress);
                    }else{
                        // broadcast
                        ConnectToBLE.getInstance().runCommand(null,BLEConstants.TYPE_AUTO_OFF, Integer.parseInt(groupAddress), "");
                    }
                }
            }
        });
    }

    String  channelOne, channeltwo;

    private  void setAddressDetails(){
        final Intent intent = getIntent();
        String lightname = intent.getStringExtra(BLEConstants.EXTRAS_LIGHT_NAME);
        String groupInfo= intent.getStringExtra(BLEConstants.EXTRAS_LIGHT_GROUP);
        lightAddress= intent.getStringExtra(BLEConstants.EXTRAS_LIGHT_ADDRESS);
        groupAddress= intent.getStringExtra(BLEConstants.EXTRAS_GROUP_ADDRESS);

        channelOne= intent.getStringExtra(BLEConstants.EXTRAS_WARM_COOL);
        channeltwo= intent.getStringExtra(BLEConstants.EXTRAS_LIGHT_INTENSITY);

        LogUtils.d("channel One and channel two "+channelOne + "   "+channeltwo);

        if(TextUtils.isEmpty(groupAddress)){
            isUnicast= true;
        }else{
            // its broadcast condition
            isUnicast= false;
        }

        if(TextUtils.isEmpty(lightAddress)){
            light_name.setVisibility(View.GONE);
        }else{
            light_name.setText("Light Name : " + lightname);
        }

        if(isUnicast) {
            String on_off = intent.getStringExtra(BLEConstants.EXTRAS_LIGHT_ON_OFF_STATE);
            LogUtils.d("operateightActivity "+on_off);
            try {
                int on_off_state = Integer.parseInt(on_off);
                LogUtils.d("operateightActivity on_off_state "+on_off_state );
                if (on_off_state == 0) {
                    switch_light.setChecked(false);
                } else {
                    switch_light.setChecked(true);
                }
            } catch (Exception e) {

            }

            int intensity = getIntensityFromChannelOne();
            int warm_cool =getWarmCoolFromChanneltwo();

            seekbar_warm_cool.setProgress(warm_cool);
            seekbar_intensity.setProgress(intensity);
            text_warm_cool_level.setText(""+warm_cool);
            text_intensity_level.setText(""+intensity);
        }

        if(groupInfo != null && groupInfo.equalsIgnoreCase("0")){
            textview_Group_name.setText("Individual");
        }else{
            textview_Group_name.setText("Group  : " + groupInfo);
        }


    }


    private int  getIntensityFromChannelOne(){

       int ch1 =  Integer.parseInt(channelOne);
        int ch2 =  Integer.parseInt(channeltwo);
        if(ch1 == 0 && ch2 ==0){
            return 100;
        }else{
            return ch1+ch2;
        }

    }

    private int  getWarmCoolFromChanneltwo(){
        int ch1 =  Integer.parseInt(channelOne);
        int ch2 =  Integer.parseInt(channeltwo);
        if(ch1 == 0 && ch2 ==0){
            return 50;
        }else{
            return (100*ch2)/getIntensityFromChannelOne();
        }

    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return true;
    }



}