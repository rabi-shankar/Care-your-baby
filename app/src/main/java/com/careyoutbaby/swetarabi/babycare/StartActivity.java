package com.careyoutbaby.swetarabi.babycare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import static com.careyoutbaby.swetarabi.babycare.MainActivity.TIME_DELAY;
import static com.careyoutbaby.swetarabi.babycare.MainActivity.back_pressed;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
/*
        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            Toast.makeText(this,"InterruptedException is throw",Toast.LENGTH_SHORT).show();
        }

        Intent x = new Intent(this, IntroActivity.class);
        startActivity(x);*/
    }

    public void next(View view) {
        Intent x = new Intent(this, IntroActivity.class);//IntroActivity.class
        startActivity(x);
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
