package com.example.kedarkotkunde.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kedarkotkunde.myapplication.model.ParseModelValues;

import java.util.List;

/**
 * Created by kedarkotkunde on 7/19/17.
 */

public class GridViewAdapter extends BaseAdapter{
    private Context mContext;
    private List<ParseModelValues> bulbnames;

    public GridViewAdapter(Context c,List<ParseModelValues> bulbnames ) {
        mContext = c;
        this.bulbnames = bulbnames;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return bulbnames.size();
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
            grid = inflater.inflate(R.layout.grid_row, null);

        } else {
            grid = (View) convertView;
        }
        TextView textView = (TextView) grid.findViewById(R.id.grid_text);
        ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
        TextView textViewGroupName =((TextView) grid.findViewById(R.id.grid_groupName));
        ImageView imageview_right_tick = (ImageView)grid.findViewById(R.id.imageview_right_tick);
        ParseModelValues parseModelValues=bulbnames.get(position);
        textView.setText(""+(position+1));
        parseModelValues.setDisplayName(""+(position+1));
        //grid.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryDark, null));
        if( parseModelValues.getProvisionFlag()==1){
            imageview_right_tick.setVisibility(View.VISIBLE);
            textViewGroupName.setVisibility(View.VISIBLE);

            if(parseModelValues.getOnOffFlag() == 0){
                //imageView.setImageResource(R.drawable.bulb);
            }else{
                imageView.setImageResource(R.drawable.yellow_bulb);
            }

            if(parseModelValues.getUnUsedFlag() == 0){
                textViewGroupName.setText("");
            }else{
                textViewGroupName.setText("G"+parseModelValues.getUnUsedFlag());
            }

        }else{
            imageview_right_tick.setVisibility(View.INVISIBLE);
            textViewGroupName.setVisibility(View.INVISIBLE);
        }
        if(parseModelValues.isLongPressedandSelected()) {
            ((View) grid.findViewById(R.id.view1)).setVisibility(View.VISIBLE);
            ((View) grid.findViewById(R.id.view2)).setVisibility(View.VISIBLE);
            ((View) grid.findViewById(R.id.view3)).setVisibility(View.VISIBLE);
            ((View) grid.findViewById(R.id.view4)).setVisibility(View.VISIBLE);
        }else{
            ((View) grid.findViewById(R.id.view1)).setVisibility(View.INVISIBLE);
            ((View) grid.findViewById(R.id.view2)).setVisibility(View.INVISIBLE);
            ((View) grid.findViewById(R.id.view3)).setVisibility(View.INVISIBLE);
            ((View) grid.findViewById(R.id.view4)).setVisibility(View.INVISIBLE);
        }
        return grid;
    }


    private void enableBorder(View v1,View v2,View v3,View v4){

    }

}