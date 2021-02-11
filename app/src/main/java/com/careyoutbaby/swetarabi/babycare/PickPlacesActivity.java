package com.careyoutbaby.swetarabi.babycare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PickPlacesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_places);
    }

    public void next(View view) {
        Intent x = new Intent(this,PickPlaces2Activity.class);
        startActivity(x);
    }
}
