package com.careyoutbaby.swetarabi.babycare;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class SettingsMenu extends AppCompatActivity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_menu);
        setupActionBar();
        //check Network Status
        isInternetConnectionEnable();




        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

         //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, EmergencyAlertFragment.newInstance());
        transaction.commit();
    }



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:

                    SettingsMenu.this.setTitle(R.string.emergency_nalert);

                    FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                    transaction1.replace(R.id.content, EmergencyAlertFragment.newInstance());
                    transaction1.commit();

                    return true;
                case R.id.navigation_dashboard:

                    SettingsMenu.this.setTitle(R.string.places_pick);


                    FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                    transaction2.replace(R.id.content, PlacesPickFragment.newInstance());
                    transaction2.commit();

                    return true;
                case R.id.navigation_notifications:

                    SettingsMenu.this.setTitle(R.string.notificationcap);

                    FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction();
                    transaction3.replace(R.id.content, NotificationFragment.newInstance());
                    transaction3.commit();

                    return true;
            }

            return false;
        }

    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.location:
                Intent locationSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(locationSettingIntent);
                break;
            case R.id.wifi:
                Intent wifiSettingIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(wifiSettingIntent);
                break;
            case R.id.notification:
                Intent notificationSettingIntent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivity(notificationSettingIntent);
                break;
            case R.id.ringandvolume:
                Intent ringandvolumeSettingIntent = new Intent(Settings.ACTION_SOUND_SETTINGS);
                startActivity(ringandvolumeSettingIntent);
                break;
            case R.id.datausage:
                Intent datausageSettingIntent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                startActivity(datausageSettingIntent);
                break;
            case R.id.aboutphone:
                Intent aboutphoneSettingIntent = new Intent(Settings.ACTION_DEVICE_INFO_SETTINGS);
                startActivity(aboutphoneSettingIntent);
                break;
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void next(View view) {
        Intent x =new Intent(this,PickPlaces2Activity.class);
        startActivity(x);
    }


    public void next_emergency_alert(View view) {
        Intent x =new Intent(this,EmergencyAlertActivity.class);
        startActivity(x);

    }



    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            // onBackPressed();
        }
    }

    public void vibration(View view) {

        /*long[] pattern = {100};
        Vibrator vx  = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if(vx.hasVibrator()){
            vx.vibrate(pattern,1);
        }*/
    }


    /*---------------------------------------------------------------------------------------
   * Date : 23/03/2017  1:36 AM
   * Description: isInternetConnectionEnable method check Internet connectivity and Toast message
   * "Disconnected! Your are now offline!"
   * ---------------------------------------------------------------------------------------*/

    public void isInternetConnectionEnable(){
        ConnectivityManager myco = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mycoinfo = myco.getActiveNetworkInfo();
        if (mycoinfo == null){

            Toast.makeText(SettingsMenu.this,"Disconnected! Your are now offline! ",Toast.LENGTH_SHORT).show();
        }

    }
}
