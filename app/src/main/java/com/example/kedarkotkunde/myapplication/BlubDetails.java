package com.example.kedarkotkunde.myapplication;


import java.util.List;

/**
 * Created by kedarkotkunde on 7/19/17.
 */

public class BlubDetails {
    public BlubDetails(String bulbName, boolean isBulbActivated) {
        this.bulbName = bulbName;
        this.isBulbActivatedForThisGroup = isBulbActivated;
    }

    private  String bulbName;
    private  boolean isBulbActivatedForThisGroup;



    public String getBulbName() {
        return bulbName;
    }

    public void setBulbName(String bulbName) {
        this.bulbName = bulbName;
    }

    public boolean isBulbActivated() {
        return isBulbActivatedForThisGroup;
    }

    public void setBulbActivated(boolean bulbActivated) {
        isBulbActivatedForThisGroup = bulbActivated;
    }
}