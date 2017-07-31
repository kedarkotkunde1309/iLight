package com.example.kedarkotkunde.myapplication;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kedarkotkunde.myapplication.framework.ConnectToBLE;
import com.example.kedarkotkunde.myapplication.framework.IDeviceState;
import com.example.kedarkotkunde.myapplication.model.ParseBLEValue;
import com.example.kedarkotkunde.myapplication.model.ParseModelValues;

import java.util.ArrayList;
import java.util.List;

import static android.media.CamcorderProfile.get;

public class MainActivity extends Activity implements IDeviceState
{

    private GridView gridView, gridviewGroup;
    private TextView textviewgrouptitle, textViewItemCounts, device_name, device_state, device_uuid;

    String mDeviceAddress="";


    private List<ParseModelValues> bulblist = new ArrayList<ParseModelValues>();
    private List<BlubDetails> grouplist = new ArrayList<BlubDetails>();
    private GridViewAdapter adapter;
    private GridViewGroupAdapter groupAdapter;

    private  ProgressBar progressBar;
    int selectedCountValue = 0;
    boolean isUpdateStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = (GridView)findViewById(R.id.gridview);
        gridviewGroup= (GridView)findViewById(R.id.gridviewGroup);
        textviewgrouptitle=(TextView)findViewById(R.id.textviewGroup) ;
        textViewItemCounts=(TextView)findViewById(R.id.textViewItemCounts) ;
        device_name=(TextView)findViewById(R.id.device_name) ;
        device_state=(TextView)findViewById(R.id.device_state) ;
        device_uuid=(TextView)findViewById(R.id.device_uuid) ;
        progressBar =(ProgressBar) findViewById(R.id.progressBar) ;

        setDeviceInfo();

        // temp items list
        addtempGroup();
        addTempEntry();

        // set the groups on the top first
        setGroupTitle();
        setGroupGridView();

        // set the items belows
        setItemTitle();
        setItemGridView();
        ConnectToBLE.getInstance(this).startBLEService(this,mDeviceAddress );


    }



    private void addTempEntry(){
        ParseModelValues parseModelValues1= new ParseModelValues();
        ParseModelValues parseModelValues2= new ParseModelValues();
        ParseModelValues parseModelValues3= new ParseModelValues();
        ParseModelValues parseModelValues4= new ParseModelValues();
        parseModelValues1.setDisplayName("l1");
        parseModelValues2.setDisplayName("l2");
        parseModelValues3.setDisplayName("l3");
        parseModelValues4.setDisplayName("l4");

        bulblist.add(parseModelValues1);
        bulblist.add(parseModelValues2);
        bulblist.add(parseModelValues3);
        bulblist.add(parseModelValues4);



    }



    @Override
    protected void onResume() {
        super.onResume();
        ConnectToBLE.getInstance(this).registerBLEBroadcast(this);
        if(adapter  != null){
            //LogUtils.d("Notify to reflect the changes....");
            bulblist=ParseBLEValue.getInstance().getListOfLights();
//            for (int i=0 ;i < bulblist.size(); i++){
//                ParseModelValues parseModelValues =bulblist.get(i);
//                LogUtils.d("Notify to reflect the changes...."+i+"  >"+parseModelValues.getOnOffFlag());
//
//            }
            adapter= new GridViewAdapter(MainActivity.this, bulblist);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        ConnectToBLE.getInstance(this).unRegisterBroadcast(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ConnectToBLE.getInstance(this).unRegisterBLEService(this);
    }

    private void setItemTitle(){
        String itemTitle = "Lights ("+bulblist.size()+")";
        Spannable wordtoSpan = new SpannableString(itemTitle);
        wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLUE), 0, itemTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewItemCounts.setText(wordtoSpan);
    }

    private  void  addtempGroup(){
//        grouplist.add("Group 1");
//        grouplist.add("Group 2");
//        grouplist.add("Group 3");
//        grouplist.add("Group 4");

        grouplist.add(new BlubDetails("Group 1", false));
        grouplist.add(new BlubDetails("Group 2", false));
        grouplist.add(new BlubDetails("Group 3", false));
        grouplist.add(new BlubDetails("Group 4", false));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private  void  setDeviceInfo(){
        final Intent intent = getIntent();
        String mDeviceName = intent.getStringExtra(BLEConstants.EXTRAS_DEVICE_NAME);
        mDeviceAddress= intent.getStringExtra(BLEConstants.EXTRAS_DEVICE_ADDRESS);
        device_name.setText("Device Name : " + mDeviceName);
        device_uuid.setText("Device Address : " + mDeviceAddress);

    }

    private void setGroupTitle(){
        String groupTitle = "Groups (4)";
        Spannable wordtoSpan = new SpannableString(groupTitle);
        wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLUE), 0, groupTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textviewgrouptitle.setText(wordtoSpan);
    }

    private  void  setItemGridView(){
        adapter= new GridViewAdapter(MainActivity.this, bulblist);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                LogUtils.d("onitem click ");
                ParseModelValues clickedBulb =bulblist.get(position);
                // Already provisioned no need to do anything
                if(clickedBulb.getProvisionFlag() ==0) {
                    selectedCountValue= selectedCountValue+1;
                    isUpdateStart = true;
                    if (clickedBulb.isActivated()) {
                        clickedBulb.setActivated(false);
                    } else {
                        clickedBulb.setActivated(true);
                    }
                    showConfirmationDialogForProvision(""+(position+1), BLEConstants.TYPE_INDIVISUAL_PROVISION, clickedBulb.getAddressValue());
                    adapter.notifyDataSetChanged();
                    setAnimationForLightItems(view);
                }else{
                    final Intent intent = new Intent(MainActivity.this, OperateLigthtsActivity.class);
                    intent.putExtra(BLEConstants.EXTRAS_LIGHT_NAME, ""+clickedBulb.getDisplayName());
                    intent.putExtra(BLEConstants.EXTRAS_LIGHT_GROUP, ""+clickedBulb.getUnUsedFlag());
                    intent.putExtra(BLEConstants.EXTRAS_LIGHT_ADDRESS, ""+clickedBulb.getAddressValue());
                    intent.putExtra(BLEConstants.EXTRAS_LIGHT_ON_OFF_STATE, ""+clickedBulb.getOnOffFlag());
                    intent.putExtra(BLEConstants.EXTRAS_WARM_COOL, ""+clickedBulb.getByteAddressForChannelTwo());
                    intent.putExtra(BLEConstants.EXTRAS_LIGHT_INTENSITY, ""+clickedBulb.getByteAddressForChannelOne());
                    LogUtils.d("This buld is ON or OFF "+clickedBulb.getOnOffFlag());
                    LogUtils.d("This buld intensity set "+clickedBulb.getChannelOne()+"//"+clickedBulb.getChannelTwo() + "  "+clickedBulb.getByteAddressForChannelOne() + "  "+clickedBulb.getByteAddressForChannelTwo());
                    intent.putExtra(BLEConstants.EXTRAS_GROUP_ADDRESS, "");
                    startActivity(intent);
                    //Toast.makeText(getApplicationContext(), clickedBulb.getDisplayName()+" is Already provisioned." , Toast.LENGTH_SHORT).show();
                }

            }
        });


        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            //this listener should show the context menu for a long click on the gridview.
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                ParseModelValues clickedBulb =bulblist.get(position);
                LogUtils.d("onitem long click "+position);
                if(clickedBulb.getProvisionFlag() == 0) {

                    isUpdateStart = true;
                    if (clickedBulb.isLongPressedandSelected() == false) {
                        updateCount=0;
                        selectedCountValue= selectedCountValue+1;
                        clickedBulb.setLongPressedandSelected(true);
                        clickedBulb.setActivated(true);
                    } else {
                        selectedCountValue= selectedCountValue-1;
                        if(selectedCountValue <0){
                            selectedCountValue=0;
                            isUpdateStart = false;
                        }
                        clickedBulb.setLongPressedandSelected(false);
                        clickedBulb.setActivated(false);
                    }
                    adapter.notifyDataSetChanged();
                }
               return  true;
            }
        });
    }

    private  void  setGroupGridView(){
        groupAdapter= new GridViewGroupAdapter(MainActivity.this, grouplist);
        gridviewGroup.setAdapter(groupAdapter);
        gridviewGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                List<ParseModelValues>list = getListOfLightsNeedProvision();
                BlubDetails groupName =grouplist.get(position);
                LogUtils.d("selected grid position "+position);
                if(list != null && list.size() > 0){
                    showConfirmationDialog(groupName.getBulbName(), position);
                }else{
                    if(groupName.isBulbActivated()){
                        final Intent intent = new Intent(MainActivity.this, OperateLigthtsActivity.class);
                        intent.putExtra(BLEConstants.EXTRAS_LIGHT_NAME, "");
                        intent.putExtra(BLEConstants.EXTRAS_LIGHT_GROUP, groupName.getBulbName());
                        intent.putExtra(BLEConstants.EXTRAS_LIGHT_ADDRESS, "");
                        intent.putExtra(BLEConstants.EXTRAS_GROUP_ADDRESS, ""+position);
                        startActivity(intent);
                    }

                    //Toast.makeText(getApplicationContext(), getResources().getString(R.string.select_one_light), Toast.LENGTH_SHORT).show();
                }
                setAnimationForLightItems(view);
            }
        });

        gridviewGroup.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {


            public boolean onItemLongClick(AdapterView<?> parent, View arg1,
                                           int position, long arg3) {


                Toast.makeText(getApplicationContext(), "long press "+position, 0).show();
                return false;
            }
        });


    }

    private  void  showConfirmationDialog(String title,final int groupId){
        final AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setCancelable(false);
        builder.setTitle(title)
                .setMessage("Are you sure you want to Add in "+title+" ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // yes case provision the device now
                        progressBar.setVisibility(View.VISIBLE);
                        ConnectToBLE.getInstance(MainActivity.this).runCommand(getListOfLightsNeedProvision(),BLEConstants.TYPE_PROVISION ,groupId,"" );

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }


    private  void  showConfirmationDialogForProvision(String title,final int groupId, final String lightAddress){
        final AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setCancelable(false);
        builder.setTitle("Light  "+title)
                .setMessage("Are you sure you want to provision light "+title+"?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // yes case provision the device now
                        progressBar.setVisibility(View.VISIBLE);
                        ConnectToBLE.getInstance(MainActivity.this).runCommand(getListOfLightsNeedProvision(),BLEConstants.TYPE_INDIVISUAL_PROVISION ,groupId,lightAddress );

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }


    private  List<ParseModelValues> getListOfLightsNeedProvision(){
        List<ParseModelValues> parseModelValuesList = new ArrayList<>();
        for (int i=0  ; i <bulblist.size(); i++ ){
            ParseModelValues parseModelValues =bulblist.get(i);
            if(parseModelValues.isActivated()){
                parseModelValuesList.add(parseModelValues);
            }
        }
        return  parseModelValuesList;
    }


    private void setAnimationForLightItems(View myView){
        Animation animBounce = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bounse);
        myView.startAnimation(animBounce);
    }

    @Override
    public void onDeviceConnected() {

        LogUtils.d("Connected");
        device_state.setText("State : Connected");

        Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeviceDisConnected() {

        LogUtils.d("disConnected");
        device_state.setText("State : Disconnected");
    }

    @Override
    public void onServiceDiscovered() {

    }


    int updateCount=0;

    @Override
    public void onDataAvailable() {

        bulblist=ParseBLEValue.getInstance().getListOfLights();

        updateCount =  updateCount+1;
        LogUtils.d("Update the list now======"+bulblist.size()  +"   "+updateCount + " "+selectedCountValue);
        boolean isGroupProvision = false;
        for (int i = 0; i < bulblist.size(); i++){
            LogUtils.d("Update tname ======"+bulblist.get(i).getDisplayName()+ " "+bulblist.get(i).getProvisionFlag() + " "+bulblist.get(i).getUnUsedFlag());
            if(bulblist.get(i).getProvisionFlag() ==1){
                int groupAddress=bulblist.get(i).getUnUsedFlag();
                if(groupAddress  != 0){
                    BlubDetails bulbdetails = grouplist.get(groupAddress-1);
                    bulbdetails.setBulbActivated(true);
                    isGroupProvision= true;
                }
                if(isGroupProvision ){
                    groupAdapter= new GridViewGroupAdapter(MainActivity.this, grouplist);
                    gridviewGroup.setAdapter(groupAdapter);
                    groupAdapter.notifyDataSetChanged();
                }

            }

        }
        if(updateCount== selectedCountValue) {
            progressBar.setVisibility(View.GONE);
            selectedCountValue=0;
            updateCount=0;
        }
       // progressBar.setVisibility(View.GONE);
        if(bulblist != null){
            setItemTitle();
            adapter= new GridViewAdapter(MainActivity.this, bulblist);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }

    }
}
