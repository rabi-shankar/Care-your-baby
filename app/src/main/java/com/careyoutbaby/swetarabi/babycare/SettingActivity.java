package com.careyoutbaby.swetarabi.babycare;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

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
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /*
    * Emergency Alert Activity calling*/
    public void emergency_Alert(View view) {
        Intent x = new Intent(this, EmergencyAlertActivity.class);
        startActivity(x);
    }


    /*
    * Place piker Activity calling*/
    public void placepiker(View view) {
        Intent x =new Intent(this,PickPlacesActivity.class);
        startActivity(x);

    }
}
