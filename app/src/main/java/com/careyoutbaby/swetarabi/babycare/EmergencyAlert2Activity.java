package com.careyoutbaby.swetarabi.babycare;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.os.Build.VERSION_CODES.M;

public class EmergencyAlert2Activity extends AppCompatActivity {

    public String msg;
    public String user_number = null;
    public String user_name = null;
    public EditText pName;
    public EditText pNumber;
    //public android.support.design.widget.TextInputLayout pick;
    public Button contactPicker;
    public boolean flg = false;

    private static final int RESULT_PICK_CONTACT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_alert2);

        setupActionBar();

        //check Network Status
        isInternetConnectionEnable();
        pName = (EditText) findViewById(R.id.editText2);
        pNumber = (EditText) findViewById(R.id.editText3);
        //pick = (TextInputLayout) findViewById(R.id.textInputLayout1);
        contactPicker = (Button) findViewById(R.id.button1);




        contactPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickContact(v);
            }
        });

    }







    public void next(View view) throws InterruptedException {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("EMERGENCY_ALERT");

        msg = getIntent().getStringExtra(EmergencyAlertActivity.EXTRA_MY_STR);
        //user_number = pNumber.getText().toString();

        // if flg is false the we get Text View value
        boolean validphone = false;

        if(flg){

            myRef.child("PHONE_NUMBER").setValue(user_number);
            myRef.child("CONTACT_NAME").setValue(user_name);
            myRef.child("EMERGENCY_MESSAGE").setValue(msg);
            allDone();

        }else{

            user_name = pName.getText().toString();
            user_number = pNumber.getText().toString();

            if(!user_name.isEmpty() && !user_number.isEmpty()){

                validphone = android.util.Patterns.PHONE.matcher(user_number).matches();
                Toast.makeText(this,toString().valueOf(validphone),Toast.LENGTH_LONG).show();
                if(validphone && user_number.length()==10){

                    myRef.child("PHONE_NUMBER").setValue(user_number);
                    myRef.child("CONTACT_NAME").setValue(user_name);
                    myRef.child("EMERGENCY_MESSAGE").setValue(msg);
                    allDone();

                }else{
                    Toast.makeText(this,"Invalid Phone Number!",Toast.LENGTH_LONG).show();
                }

            }

            //Toast.makeText(this,"Please pick Contact!",Toast.LENGTH_LONG).show();
        }
    }







     /*---------------------------------------------------------------------------------------
      * Date : 19/03/2017  12:34 PM
      * Description:
      * ---------------------------------------------------------------------------------------*/
    private void allDone() {

        flg =false;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle("You’re all set")
                .setMessage("You can further test or adjust Alert emergency operations in settings")
                .setIcon(R.drawable.ic_room_black_24dp)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent x = new Intent(EmergencyAlert2Activity.this, MainActivity.class);
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
        if (mycoinfo == null ){

            Toast.makeText(EmergencyAlert2Activity.this,"Disconnected! Your are now offline! ",Toast.LENGTH_SHORT).show();
        }

    }

    
     /*---------------------------------------------------------------------------------------
   * Date : 08/04/2017  12:37 AM
   * Description:
   * ---------------------------------------------------------------------------------------*/

    public void pickContact(View v)
    {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
    }




    /*---------------------------------------------------------------------------------------
  * Date : 08/04/2017  12:39 AM
  * Description:
  * ---------------------------------------------------------------------------------------*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check whether the result is ok
        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        } else {
            Log.e("EmergencyAlert2Activity", "Failed to pick contact");
        }

    }


     /*---------------------------------------------------------------------------------------
   * Date : 23/03/2017  1:36 AM
   * Description: Query the Uri and read contact details. Handle the picked contact data.
   * @param data
   * ---------------------------------------------------------------------------------------*/
    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String phoneNo = null ;
            String name = null;
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();

            //Query the content uri
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();

            // column index of the phone number
            int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

            // column index of the contact name
            int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);

            // Set the value to the contact variable
            user_number = phoneNo.toString();
            user_name =  name.toString();
            pName.setText(name);
            pNumber.setText(phoneNo);

            flg = true;
            contactPicker.setText("Change Contact");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
