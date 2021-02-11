package com.careyoutbaby.swetarabi.babycare;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class PickPlaces2Activity extends AppCompatActivity {


    public final static String KEY_EXTRA_CONTACT_ID = "KEY_EXTRA_CONTACT_ID";

    public DBHelper dbHelper ;
    public int Boundary_ID;
    public int PLACE_PICKER_REQUEST = 1;

    public String name=null;
    public String add=null;
    public LatLng boundaryLatLng=null;



    public String[] allName;
    public String[] allAdd;
    public String[] allBoundaryLatLng;

    // picker flages
    public  boolean isflag1 = false;
    public  boolean isflag2 = false;
    public  boolean isflag3 = false;
    public  boolean isflag4 = false;
    public  boolean isflag5 = false;
    public  boolean isflag6 = false;



    public  boolean isflagset1 = false;
    public  boolean isflagset2 = false;
    public  boolean isflagset3 = false;
    public  boolean isflagset4 = false;
    public  boolean isflagset5 = false;
    public  boolean isflagset6 = false;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("BOUNDARY_AREA");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_places2);
        setupActionBar();
        //check Network Status
        isInternetConnectionEnable();

        //database intent
        dbHelper = new DBHelper(this);

        LinearLayout x1 = (LinearLayout) findViewById(R.id.pick1);
        LinearLayout x2 = (LinearLayout) findViewById(R.id.pick2);
        LinearLayout x3 = (LinearLayout) findViewById(R.id.pick3);
        LinearLayout x4 = (LinearLayout) findViewById(R.id.pick4);
        LinearLayout x5 = (LinearLayout) findViewById(R.id.pick5);
        LinearLayout x6 = (LinearLayout) findViewById(R.id.pick6);

        TextView seeMore = (TextView) findViewById(R.id.seemore);


        allName = new String[6];
        allAdd = new  String[6];
        allBoundaryLatLng = new String[6];




        x1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    isflag1 = true;
                    Placepicker();

                } catch (GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                }


            }
        });

        x2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    isflag2 = true;
                    Placepicker();

                } catch (GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                }
            }
        });

        x3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {

                    isflag3 =true;
                    Placepicker();

                } catch (GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                }
            }
        });

        x4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    isflag4 = true;
                    Placepicker();
                } catch (GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                }
            }
        });

        x5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    isflag5 = true;
                    Placepicker();
                } catch (GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                }
            }
        });

        x6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    isflag6 =true;
                    Placepicker();
                } catch (GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                }
            }
        });






        seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent x =new Intent(PickPlaces2Activity.this,OldPlaceRecord.class);
                x.putExtra(KEY_EXTRA_CONTACT_ID, 0);
                startActivity(x);
            }
        });


    }










    /*---------------------------------------------------------------------------------------
  * Date : 25/03/2017  6:34 AM
  * Description:   Place picker methods
  *  ---------------------------------------------------------------------------------------*/

    public void Placepicker() throws GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        startActivityForResult(builder.build(this),PLACE_PICKER_REQUEST);
        //Toast.makeText(PickPlaces2Activity.this,"inside picker",Toast.LENGTH_SHORT).show();
    }







    /*---------------------------------------------------------------------------------------
  * Date : 25/03/2017  6:40 AM
  * Description:  onActivityResult method is call when PlacePicker  method is called
  *
  * ---------------------------------------------------------------------------------------*/



    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode == PLACE_PICKER_REQUEST){
            if(resultCode==RESULT_OK){
                Place place =PlacePicker.getPlace(this,data);
                name = (String) place.getName();
                add = (String) place.getAddress();
                boundaryLatLng = place.getLatLng();


                //for 1st place picker
                if(isflag1){
                    //Toast.makeText(PickPlaces2Activity.this,"inside  picker result",Toast.LENGTH_SHORT).show();
                    TextView tx = (TextView) findViewById(R.id.textView1z1);
                    TextView ty = (TextView) findViewById(R.id.textView2z1);
                    tx.setText(name);
                    ty.setText(add);
                    allName[0]=name;
                    allAdd[0] = add;
                    allBoundaryLatLng[0] = latLngToString(boundaryLatLng);
                    /*myRef.child("NAME1").setValue(name);
                    myRef.child("ADDRESS1").setValue(add);
                    myRef.child("LATLNG1").setValue(boundaryLatLng);
                    addNewLocation(name,add,latLngToString(boundaryLatLng));*/
                    isflag1 =false;
                    isflagset1 =true;

                    //for 2nd place picker
                }else if(isflag2){
                    TextView tx = (TextView) findViewById(R.id.textView1z2);
                    TextView ty = (TextView) findViewById(R.id.textView2z2);
                    tx.setText(name);
                    ty.setText(add);

                    allName[1]=name;
                    allAdd[1] = add;
                    allBoundaryLatLng[1] = latLngToString(boundaryLatLng);
                    /*myRef.child("NAME2").setValue(name);
                    myRef.child("ADDRESS2").setValue(add);
                    myRef.child("LATLNG2").setValue(boundaryLatLng);
                    addNewLocation(name,add,latLngToString(boundaryLatLng));*/
                    isflag2 =false;
                    isflagset2 =true;

                    //for 3rd place picker
                }else if(isflag3){
                    TextView tx = (TextView) findViewById(R.id.textView1z3);
                    TextView ty = (TextView) findViewById(R.id.textView2z3);
                    tx.setText(name);
                    ty.setText(add);
                    allName[2]=name;
                    allAdd[2] = add;
                    allBoundaryLatLng[2] = latLngToString(boundaryLatLng);
                    /*myRef.child("NAME3").setValue(name);
                    myRef.child("ADDRESS3").setValue(add);
                    myRef.child("LATLNG3").setValue(boundaryLatLng);
                    addNewLocation(name,add,latLngToString(boundaryLatLng));*/
                    isflag3 = false;
                    isflagset3 = true;

                    //for 4th place picker
                }else  if(isflag4){
                    TextView tx = (TextView) findViewById(R.id.textView1z4);
                    TextView ty = (TextView) findViewById(R.id.textView2z4);
                    tx.setText(name);
                    ty.setText(add);
                    allName[3]=name;
                    allAdd[3] = add;
                    allBoundaryLatLng[3] = latLngToString(boundaryLatLng);
                    /*myRef.child("NAME4").setValue(name);
                    myRef.child("ADDRESS4").setValue(add);
                    myRef.child("LATLNG4").setValue(boundaryLatLng);
                    addNewLocation(name,add,latLngToString(boundaryLatLng));*/
                    isflag4 =false;
                    isflagset4 =true;



                    //for 5th place picker
                }else if(isflag5){
                    TextView tx = (TextView) findViewById(R.id.textView1z5);
                    TextView ty = (TextView) findViewById(R.id.textView2z5);
                    tx.setText(name);
                    ty.setText(add);
                    allName[4]=name;
                    allAdd[4] = add;
                    allBoundaryLatLng[4] = latLngToString(boundaryLatLng);

                    /*myRef.child("NAME5").setValue(name);
                    myRef.child("ADDRESS5").setValue(add);
                    myRef.child("LATLNG5").setValue(boundaryLatLng);
                    addNewLocation(name,add,latLngToString(boundaryLatLng));*/
                    isflag5 = false;
                    isflagset5 =true;


                    //for 6th place picker
                }else if (isflag6){
                    TextView tx = (TextView) findViewById(R.id.textView1z6);
                    TextView ty = (TextView) findViewById(R.id.textView2z6);
                    tx.setText(name);
                    ty.setText(add);
                    allName[5]=name;
                    allAdd[5] = add;
                    allBoundaryLatLng[5] = latLngToString(boundaryLatLng);
                    /*myRef.child("NAME6").setValue(name);
                    myRef.child("ADDRESS6").setValue(add);
                    myRef.child("LATLNG6").setValue(boundaryLatLng);
                    addNewLocation(name,add,latLngToString(boundaryLatLng));*/
                    isflag6 =false;
                    isflagset6 =true;
                }


            }
        }
    }


    /*---------------------------------------------------------------------------------------
  * Date : 25/03/2017  6:34 AM
  * Description:  Alert Dialog Box Methods
  * ---------------------------------------------------------------------------------------*/

    private void allDone() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle("You’re all set")
                .setMessage("You can edit your designated  places and manage the close boundary ")
                .setIcon(R.drawable.ic_room_black_24dp)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                dataupdate();
                                Intent x = new Intent(PickPlaces2Activity.this, MainActivity.class);
                                startActivity(x);
                            }
                        });
        /*alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });*/

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }










    /*---------------------------------------------------------------------------------------
      * Date : 25/03/2017  6:34 AM
      * Description: dataUpdate method delete old table and create new table with new data
      * update is done in both database, firebase and SQLite
      * ---------------------------------------------------------------------------------------*/

    private void dataupdate() {

        dbHelper.allDelete();
        String locationStringArray = "";
        int i;
        for(i=0;i<6;i++){
            myRef.child("NAME"+(i+1)).setValue(allName[i]);
            myRef.child("ADDRESS"+(i+1)).setValue(allAdd[i]);
            myRef.child("LATLNG"+(i+1)).setValue(allBoundaryLatLng[i]);
            addNewLocation(allName[i],allAdd[i],allBoundaryLatLng[i]);
            locationStringArray = locationStringArray+" | "+allBoundaryLatLng[i];
            //Toast.makeText(PickPlaces2Activity.this,allBoundaryLatLng[i],Toast.LENGTH_SHORT).show();
        }
        myRef.child("LOCATION_STRING_ARRAY").setValue(locationStringArray);
    }







    /*---------------------------------------------------------------------------------------
      * Date : 20/03/2017  3:34 PM
      * Description: Next all done Methods
      * ---------------------------------------------------------------------------------------*/


    public void next(View view) {
        if(isflagset1 && isflagset2 && isflagset3 && isflagset4 && isflagset5 && isflagset6){

            allDone();
        }else {
            Toast.makeText(this,"Please pick up all 6 Places",Toast.LENGTH_SHORT).show();
        }

    }









   /*---------------------------------------------------------------------------------------
      * Date : 19/03/2017  12:34 PM
      * Description: Enable the Back button at Top of the ActionBar
      * Set up the {@link android.app.ActionBar}, if the API is available.
      * ---------------------------------------------------------------------------------------*/

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
    }













    /*---------------------------------------------------------------------------------------
      * Date : 19/03/2017  12:32 PM
      * Description: Back button switch form current Activity to Parent Activity
      * ---------------------------------------------------------------------------------------*/

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
















     /*---------------------------------------------------------------------------------------
   * Date : 23/03/2017  1:36 AM
   * Description: isInternetConnectionEnable method check Internet connectivity and Toast message
   * "Disconnected! Your are now offline!"
   * ---------------------------------------------------------------------------------------*/

    public void isInternetConnectionEnable(){
        ConnectivityManager myco = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mycoinfo = myco.getActiveNetworkInfo();
        if (mycoinfo == null ){

            Toast.makeText(PickPlaces2Activity.this,"Disconnected! Your are now offline! ",Toast.LENGTH_SHORT).show();
        }

    }




















     /*---------------------------------------------------------------------------------------
   * Date : 23/03/2017  1:36 AM
   * Description: isInternetConnectionEnable method check Internet connectivity and Toast message
   * "Disconnected! Your are now offline!"
   * ---------------------------------------------------------------------------------------*/
    public String latLngToString(LatLng x){

        double lat= x.latitude;
        double lng= x.longitude;
        return String.format("%s,%s", lat, lng);
    }







     /*---------------------------------------------------------------------------------------
   * Date : 29/03/2017  11:56 AM
   * Description: addNewLocation method add new location to SQLite Database
   * Input: Three String Location Name,Address,LatLng Value
   * Output: void
   * Method Toast Message when Operations is Failed
   * ---------------------------------------------------------------------------------------*/

    public void addNewLocation(String t_name,String t_address,String t_latlng) {
        if(Boundary_ID > 0) {
            if(dbHelper.updateNewLocation(Boundary_ID, t_name,t_address,t_latlng)) {
                //Toast.makeText(getApplicationContext(), "Next Location", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Location Update Failed", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            if(dbHelper.insertNewLocation(t_name,t_address,t_latlng)) {
                //Toast.makeText(getApplicationContext(), "Location Inserted", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Could not Insert Location", Toast.LENGTH_SHORT).show();
            }
            /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);*/
        }
    }
}
