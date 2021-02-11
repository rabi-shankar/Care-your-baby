package com.careyoutbaby.swetarabi.babycare;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static com.careyoutbaby.swetarabi.babycare.MainActivity.TIME_DELAY;
import static com.careyoutbaby.swetarabi.babycare.MainActivity.back_pressed;

public class NoInternetActivity extends AppCompatActivity {


    WifiManager wifi;
    ConnectivityManager myco;
    NetworkInfo mycoinfo;
    boolean wififlag = false;
    boolean flg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);

        Button Tryagain = (Button) findViewById(R.id.tryagain);


        if(isInternetConnectionEnable()){
            flg= true;
        }



        Tryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myco = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                mycoinfo = myco.getActiveNetworkInfo();

                wifi= (WifiManager) getSystemService(Context.WIFI_SERVICE);

                    if(!wifi.isWifiEnabled() || mycoinfo == null){

                        wifi.setWifiEnabled(true);
                        Toast.makeText(NoInternetActivity.this,"Connect with your Wifi!",Toast.LENGTH_SHORT).show();
                        wififlag = true;
                        flg= true;

                        Intent y = new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK);
                        startActivity(y);
                    }

            }
        });

    }


    @Override
    protected void onRestart() {
        super.onRestart();

        if(flg){

            if(wififlag){
                Toast.makeText(NoInternetActivity.this,"Wifi is successfully Connected!",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(NoInternetActivity.this,"Your Internet DataPack is Enable!",Toast.LENGTH_SHORT).show();
            }
            Intent x = new Intent(NoInternetActivity.this, StartActivity.class);
            startActivity(x);
        }
    }

    @Override
    public void onBackPressed() {

            if(back_pressed+TIME_DELAY > System.currentTimeMillis()){
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            }else {
                Toast.makeText(this,"Tab again to exit",Toast.LENGTH_SHORT).show();
            }
            back_pressed =System.currentTimeMillis();
    }






    /*---------------------------------------------------------------------------------------
   * Date : 23/03/2017  1:36 AM
   * Description: isInternetConnectionEnable method check Internet connectivity and Toast message
   * "Disconnected! Your are now offline!"
   * ---------------------------------------------------------------------------------------*/

    public boolean isInternetConnectionEnable(){
        ConnectivityManager myco = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mycoinfo = myco.getActiveNetworkInfo();
        if (mycoinfo == null){

            return true;
        }

        return  false;
    }


     /*---------------------------------------------------------------------------------------
    * Date : 20/03/2017  3:30 PM
    * Description: Logout DialogBox is a method.job is show a dial box and conform the action
    * and toast action report to user
    * ---------------------------------------------------------------------------------------*/

    public void wifiEnabletDialogBox(View v) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle("WIFI Settings")
                .setMessage("WIFI is disabled in your device. Would you like to enable it?")
                .setIcon(R.drawable.ic_signal_wifi_off_black_24dp)
                .setCancelable(false)
                .setPositiveButton("Enable",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                wifi.setWifiEnabled(true);
                                Toast.makeText(NoInternetActivity.this,"Connect with your Wifi!",Toast.LENGTH_SHORT).show();
                                wififlag = true;
                                Intent y = new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK);
                                startActivity(y);

                                /*if (mycoinfo != null && mycoinfo.isConnected()){
                                    Toast.makeText(Intro2Activity.this,"Wifi is successfully Enable!",Toast.LENGTH_SHORT).show();
                                    Intent x = new Intent(Intro2Activity.this, MainActivity.class);
                                    startActivity(x);
                                }*/

                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }




}



