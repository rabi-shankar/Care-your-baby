package com.careyoutbaby.swetarabi.babycare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import static com.careyoutbaby.swetarabi.babycare.MainActivity.TIME_DELAY;
import static com.careyoutbaby.swetarabi.babycare.MainActivity.back_pressed;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }

    public void next(View view) {

        Intent x = new Intent(this, Intro2Activity.class);
        startActivity(x);

        /*ConnectivityManager myco = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mycoinfo = myco.getActiveNetworkInfo();
        if (mycoinfo != null && mycoinfo.isConnected()){
            Intent x = new Intent(this, Intro2Activity.class);
            startActivity(x);
        }
        else {
            Toast.makeText(IntroActivity.this,"Internet is not connected!",Toast.LENGTH_SHORT).show();
        }*/

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
}
