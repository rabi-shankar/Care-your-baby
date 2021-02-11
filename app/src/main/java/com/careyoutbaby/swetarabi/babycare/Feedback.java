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
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Feedback extends AppCompatActivity {

    EditText feed_msgl;
    EditText feed_subl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        setupActionBar();
        //check Network Status
        isInternetConnectionEnable();
        feed_subl = (EditText) findViewById(R.id.feedbaceSubject);
        feed_msgl = (EditText) findViewById(R.id.feedback_massage);


    }

    public void sendFeedback(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("FEEDBACK");
        String sub = feed_subl.getText().toString();
        String msg = feed_msgl.getText().toString();

        if (sub.length() == 0) {
            Toast.makeText(this, "Subject is empty!", Toast.LENGTH_LONG).show();
        } else if (msg.length() == 0) {
            Toast.makeText(this, "Message is empty!", Toast.LENGTH_LONG).show();
        } else {
            myRef.child("FEEDBACK_MESSAGE").push().setValue(sub + ":" + msg);

            /*Toast.makeText(this,"Thank You for feedback!",Toast.LENGTH_LONG).show();*/
            allDone();
        }

    }




    private void allDone() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle("Thank you!")
                .setMessage("Your valuable feedback is improve our developments")
                .setIcon(R.drawable.ic_room_black_24dp)
                .setCancelable(false)
                .setPositiveButton("Next",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent x = new Intent(Feedback.this, MainActivity.class);
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
        if (mycoinfo == null){

            Toast.makeText(Feedback.this,"Disconnected! Your are now offline! ",Toast.LENGTH_SHORT).show();
        }

    }

}

