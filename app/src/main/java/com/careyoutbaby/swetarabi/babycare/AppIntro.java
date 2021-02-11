package com.careyoutbaby.swetarabi.babycare;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

public class AppIntro extends AppCompatActivity {

    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_intro);

        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);

       /* long[] pattern = {100};
        Vibrator vx  = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if(vx.hasVibrator()){
            vx.vibrate(pattern,1);
        }*/



        if(isInternetConnectionEnable()){
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {

                    Intent x = new Intent(AppIntro.this,StartActivity.class);
                    startActivity(x);
                }
            }, 2000);
        }else{
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent x = new Intent(AppIntro.this,NoInternetActivity.class);
                    startActivity(x);
                }
            }, 2000);

        }

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
            return  false;
            //Toast.makeText(NoInternetActivity.this,"Disconnected! Your are now offline! ", Toast.LENGTH_SHORT).show();
        }
        return  true;

    }


}
