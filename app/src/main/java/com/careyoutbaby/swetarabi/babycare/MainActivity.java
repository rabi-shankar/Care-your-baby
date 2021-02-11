package com.careyoutbaby.swetarabi.babycare;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RelativeLayout mapLunch;
    ImageView mapIcon;
    LinearLayout share;
    Button btn;
    LatLng current_child_location;
    public static final int TIME_DELAY = 2000;
    public static long back_pressed = 0;


    private static final String TAG = "GoogleSignInActivity";
    private static final int RC_SIGN_IN = 9001;
    public FirebaseAuth mAuth;
    public FirebaseAuth.AuthStateListener mAuthListener;
    public  GoogleApiClient mGoogleApiClient;
    public DBLogin dblogin;

    public TextView name;
    public TextView email;

    public boolean flg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //check Network Status
        isInternetConnectionEnable();



        mapLunch = (RelativeLayout) findViewById(R.id.map_launcher);
        mapIcon = (ImageView) findViewById(R.id.imageView3);
        share = (LinearLayout) findViewById(R.id.linearLayout1);
        btn = (Button) findViewById(R.id.button4);
        name = (TextView) findViewById(R.id.textView);
        email = (TextView) findViewById(R.id.textView1);


        /*name.setText(R.string.username);
        email.setText(R.string.email);*/

        /*dblogin = new DBLogin(this);
        final Cursor cursor = dblogin.getAllUser();*/

       /* String UserName_String  = cursor.getString(cursor.getColumnIndex(DBLogin.USER_NAME));
        String UserEmail_String  = cursor.getString(cursor.getColumnIndex(DBLogin.USER_EMAIL));
*/
       /* String [] columns = new String[] {
                DBLogin.USER_NAME,
                DBLogin.USER_EMAIL
        };
        int [] widgets = new int[] {
                R.id.textView,
                R.id.textView1
        };*/

       /* String username = DBLogin.USER_NAME;
        String useremail = DBLogin.USER_EMAIL;*/

        /*try {
            if(UserName_String.length()>0 || UserEmail_String.length()>0){
                name.setText(UserName_String);
                email.setText(UserEmail_String);
            }
        }catch (NullPointerException e){
            Toast.makeText(MainActivity.this,e.toString(),Toast.LENGTH_LONG).show();
        }*/






       /* // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();*/




        /*mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } *//* OnConnectionFailedListener *//*)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();*/




        /*mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                //signInButton.setVisibility(View.GONE);
                //signOutButton.setVisibility(View.VISIBLE);
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    if(user.getDisplayName() != null)
                        name.setText(user.getDisplayName().toString());
                        email.setText(user.getEmail().toString());

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };*/






        /*---------------------------------------------------------------------------------------
        * Date : 16/03/2017  12:02 PM
        * Description: MapLunch On Click Listener switch form MainActivity
        * to Map_launcher Activity
        * ---------------------------------------------------------------------------------------*/

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    Intent y = new Intent(MainActivity.this, Map_launcher.class);
                    startActivity(y);
                    Toast.makeText(MainActivity.this, "GPS is Enabled in your device", Toast.LENGTH_SHORT).show();
                }else{
                    showGPSDisabledAlertToUser();

                    /*Intent y = new Intent(MainActivity.this, Map_launcher.class);
                    startActivity(y);*/
                }
            }
        });





        /*---------------------------------------------------------------------------------------
        * Date : 16/03/2017  12:06 AM
        * Description: MapLunch Touch Listener which show click effect
        * Touch Listener
        * ---------------------------------------------------------------------------------------*/

        mapIcon.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        mapIcon.setImageResource(R.drawable.ic_pin_drop_black_24dp);
                        //mapIcon.setBackgroundResource(R.color.colorAccent);

                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        mapIcon.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        //mapIcon.setBackgroundResource(R.color.black);
                        break;
                    }
                }
                return false;
            }
        });








        /*---------------------------------------------------------------------------------------
         * Date : 16/03/2017  12:18 PM
         * Description: On Click Listener in share (Object of LinearLayer). The job of the Method
         *  to share child location with other apps
         * ---------------------------------------------------------------------------------------*/

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                double lat =19.199047790037735;
                double lng = 84.74110007286072;
                String location = null;//latLngToString(lat,lng);
                Intent fx =new Intent(Intent.ACTION_SEND);
                fx.setType("text/plain");
                fx.putExtra(Intent.EXTRA_TEXT,"Child current loaction, "+/*location+*/" Latitude: "+lat+" Longitude: "+lng+"\nSend from babyCare App");
                if(fx.resolveActivity(getPackageManager()) != null){
                    startActivity(fx);
                }
            }
        });








        /*---------------------------------------------------------------------------------------
        * Date : 16/03/2017  12:06 AM
        * Description: Touch Listener which show click effect
        * Touch Listener
        * ---------------------------------------------------------------------------------------*/

        share.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        share.setBackgroundColor(Color.parseColor("#FFFF00"));
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        share.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        break;
                    }
                }
                return false;
            }
        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }










    /*---------------------------------------------------------------------------------------
      * Date : 22/03/2017  11:58 PM
      * Description: Method for enable GPS  Settings
      * ---------------------------------------------------------------------------------------*/

    private void showGPSDisabledAlertToUser() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setTitle("GPS Settings")
                .setIcon(R.drawable.ic_place_black_24dp)
                .setCancelable(false)
                .setPositiveButton("Enable",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
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






    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {


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





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout){
            logoutDialogBox();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Registration) {
            Intent x = new Intent(this, GoogleSignInActivity.class);
            startActivity(x);
             //signIn();
        } else if (id == R.id.nav_settings) {
            Intent z = new Intent(this, SettingsMenu.class);//SettingActivity
            startActivity(z);
        } /*else if (id == R.id.nav_help) {
           //

        } */else if (id == R.id.nav_about) {
            Intent a = new Intent(this, AboutActivity.class);
            startActivity(a);

        } else if (id == R.id.nav_feedback) {
            Intent y = new Intent(this, Feedback.class);
            startActivity(y);

        }else if (id == R.id.nav_share) {


        }/*else if(id ==R.id.nav_exit){

            //showExitAlertToUser();

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*private void signIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            } else {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                // ...
            }
        }
    }*/

    /*private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }*/



     /*---------------------------------------------------------------------------------------
      * Date : 17/03/2017  12:30 PM
      * Description: LatLngTOString this method Converted LatLng Value to corresponding String Address
      * base on Google Map api.
      * input double lat, lng value and output is address as String
      * ---------------------------------------------------------------------------------------*/


    public String latLngToString(double lat, double lng) {

        Geocoder mGeoCoder = null;
        List<Address> mAddresses = null;

        mGeoCoder = new Geocoder(getBaseContext(), Locale.ENGLISH);
        StringBuilder  strReturnedAddress = new StringBuilder("Address:\n");
        try {
            mAddresses = mGeoCoder.getFromLocation(lat, lng, 1);

            if (mAddresses.size() > 0) {
                android.location.Address returnedAddress = mAddresses.get(0);

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }

            } else {
                return null;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return String.valueOf(strReturnedAddress);
    }







    /*---------------------------------------------------------------------------------------
    * Date : 20/03/2017  3:30 PM
    * Description: Logout DialogBox is a method.job is show a dial box and conform the action
    * and toast action report to user
    * ---------------------------------------------------------------------------------------*/

    public void logoutDialogBox() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setIcon(R.drawable.ic_close_black_24dp)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){

                                /*FirebaseAuth.getInstance().signOut();
                                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                        new ResultCallback<Status>() {
                                            @Override
                                            public void onResult(@NonNull Status status) {
                                                //signInButton.setVisibility(View.VISIBLE);
                                                //signOutButton.setVisibility(View.GONE);
                                                email.setText(" ");
                                                name.setText(" ");
                                            }
                                        });*/
                                Toast.makeText(MainActivity.this,"You are successfully logout!",Toast.LENGTH_LONG).show();

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





    /*---------------------------------------------------------------------------------------
   * Date : 23/03/2017  1:36 AM
   * Description: isInternetConnectionEnable method check Internet connectivity and Toast message
   * "Disconnected! Your are now offline!"
   * ---------------------------------------------------------------------------------------*/

    public void isInternetConnectionEnable(){
        ConnectivityManager myco = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mycoinfo = myco.getActiveNetworkInfo();
        if (mycoinfo == null){

            Toast.makeText(MainActivity.this,"Disconnected! Your are now offline! ",Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        /*try {
            mAuth.addAuthStateListener(mAuthListener);
        }catch (NullPointerException e){
            //
        }*/
    }

    @Override
    public void onStop() {
        super.onStop();
        /*if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }*/
    }



    /*---------------------------------------------------------------------------------------
      * Date : 22/03/2017  12:38 AM
      * Description: Method for exit
      * ---------------------------------------------------------------------------------------*/

    private void showExitAlertToUser() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to Exit?")
                .setTitle("Exit")
                .setIcon(R.drawable.ic_close_black_24dp)
                .setCancelable(false)
                .setPositiveButton("Exit",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){

                                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                                homeIntent.addCategory( Intent.CATEGORY_HOME );
                                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(homeIntent);
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


    @Override
    protected void onRestart() {
        super.onRestart();

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Intent y = new Intent(MainActivity.this, Map_launcher.class);
            startActivity(y);
            Toast.makeText(MainActivity.this, "GPS is Enabled in your device", Toast.LENGTH_SHORT).show();
        }

    }
}

