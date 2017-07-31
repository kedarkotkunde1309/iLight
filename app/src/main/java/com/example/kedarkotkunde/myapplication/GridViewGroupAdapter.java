package com.example.kedarkotkunde.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

/**
 * Created by kedarkotkunde on 7/19/17.
 */

public class GridViewGroupAdapter extends BaseAdapter{
    private Context mContext;
    private List<BlubDetails> groupInformation;

    public GridViewGroupAdapter(Context c, List<BlubDetails> groupInformation ) {
        mContext = c;
        this.groupInformation = groupInformation;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return groupInformation.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_row_groups, null);
            TextView textView = (TextView) grid.findViewById(R.id.grid_text);
            View circularColor = (View) grid.findViewById(R.id.view_colorcode);
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            //grid.setBackgroundColor(getTheColorsForTheGroups(position));
           // grid.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryDark, null));
            //setRandomColor(circularColor);
            BlubDetails bulbdetails= groupInformation.get(position);
            if(bulbdetails != null ){
                if(bulbdetails.isBulbActivated()){
                    circularColor.setVisibility(View.VISIBLE);
                }else{
                    circularColor.setVisibility(View.INVISIBLE);
                }
            }

            textView.setText(bulbdetails.getBulbName());
        } else {
            grid = (View) convertView;
        }

        return grid;
    }


    private  int  getTheColorsForTheGroups(int position){
        int color= 0;
        switch (position){
            case 0:

                 color = Color.argb(255, 240, 192, 182);
                break;
            case 1:

                 color = Color.argb(255, 208, 240, 182);
                break;
            case 2:

                color = Color.argb(255, 182, 239, 240);
                break;
            case 3:

                color = Color.argb(255, 182, 192, 240);
                break;
            default:
                getRandomColor();
                break;

        }
        return  color;
    }

    private int  getRandomColor(){
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
       return color;
    }
}