package com.careyoutbaby.swetarabi.babycare;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.os.Build.VERSION_CODES.M;

public class EmergencyAlertActivity extends AppCompatActivity {

    EditText message;

    public final static String EXTRA_MY_STR ="com.careyoutbaby.swetarabi.babycare.MASSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        //check NetWork Status
        isInternetConnectionEnable();
        setContentView(R.layout.activity_emergency_alert);
        message = (EditText) findViewById(R.id.message);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("EMERGENCY_ALERT");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.child("EMERGENCY_MESSAGE").getValue(String.class);
                message.setText(value);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

               //not required
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //not required

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                //not required

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //not required

            }
        });
    }



    public void next(View view) {

        String msg = message.getText().toString();


        if(msg.length()>0){


            Intent x = new Intent(this, EmergencyAlert2Activity.class);
            x.putExtra(EXTRA_MY_STR,msg);
            startActivity(x);
        }else {

            Toast.makeText(this,"Messsage is Empty!",Toast.LENGTH_SHORT).show();
            /*Snackbar.make(view,"I am Alive.....",Snackbar.LENGTH_SHORT).setAction("Action",null).show();*/
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

    @RequiresApi(api = M)
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
        if (mycoinfo == null){

            Toast.makeText(EmergencyAlertActivity.this,"Disconnected! Your are now offline! ",Toast.LENGTH_SHORT).show();
        }

    }
}
