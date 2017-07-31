package com.example.kedarkotkunde.myapplication.model;

/**
 * Created by kedarkotkunde on 7/24/17.
 */

public class ParseModelValues {

    private  int validFlag ;
    private  int provisionFlag;
    private int OnOffFlag;
    private  int unUsedFlag;
    private String addressValue;
    private String channelOne;
    private String channelTwo;
    private String displayName;
    private  byte byteAddressForChannelOne;
    private  byte byteAddressForChannelTwo;

    private boolean isLongPressedandSelected= false;

    public boolean isLongPressedandSelected() {
        return isLongPressedandSelected;
    }

    public void setLongPressedandSelected(boolean longPressedandSelected) {
        isLongPressedandSelected = longPressedandSelected;
    }

    public byte getByteAddressForChannelOne() {
        return byteAddressForChannelOne;
    }

    public void setByteAddressForChannelOne(byte byteAddressForChannelOne) {
        this.byteAddressForChannelOne = byteAddressForChannelOne;
    }

    public byte getByteAddressForChannelTwo() {
        return byteAddressForChannelTwo;
    }

    public void setByteAddressForChannelTwo(byte byteAddressForChannelTwo) {
        this.byteAddressForChannelTwo = byteAddressForChannelTwo;
    }

    public byte getByteAddress() {
        return byteAddress;
    }

    public void setByteAddress(byte byteAddress) {
        this.byteAddress = byteAddress;
    }

    private  byte byteAddress;

    // Fields for the UI purpose
    private boolean isActivated;


    public ParseModelValues(){

    }

    public ParseModelValues(int validFlag, int provisionFlag, int onOffFlag, int unUsedFlag, String addressValue, String channelOne, String channelTwo) {
        this.validFlag = validFlag;
        this.provisionFlag = provisionFlag;
        OnOffFlag = onOffFlag;
        this.unUsedFlag = unUsedFlag;
        this.addressValue = addressValue;
        this.channelOne = channelOne;
        this.channelTwo = channelTwo;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getValidFlag() {
        return validFlag;
    }

    public void setValidFlag(int validFlag) {
        this.validFlag = validFlag;
    }

    public int getProvisionFlag() {
        return provisionFlag;
    }

    public void setProvisionFlag(int provisionFlag) {
        this.provisionFlag = provisionFlag;
    }

    public int getOnOffFlag() {
        return OnOffFlag;
    }

    public void setOnOffFlag(int onOffFlag) {
        OnOffFlag = onOffFlag;
    }

    public int getUnUsedFlag() {
        return unUsedFlag;
    }

    public void setUnUsedFlag(int unUsedFlag) {
        this.unUsedFlag = unUsedFlag;
    }

    public String getAddressValue() {
        return addressValue;
    }

    public void setAddressValue(String addressValue) {
        this.addressValue = addressValue;
    }

    public String getChannelOne() {
        return channelOne;
    }

    public void setChannelOne(String channelOne) {
        this.channelOne = channelOne;
    }

    public String getChannelTwo() {
        return channelTwo;
    }

    public void setChannelTwo(String channelTwo) {
        this.channelTwo = channelTwo;
    }
    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }
}
