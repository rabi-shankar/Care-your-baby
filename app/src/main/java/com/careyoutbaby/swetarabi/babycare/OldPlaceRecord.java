package com.careyoutbaby.swetarabi.babycare;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;

import static android.os.Build.VERSION_CODES.M;
import static com.careyoutbaby.swetarabi.babycare.Map_launcher.MY_PERMISSIONS_REQUEST_LOCATION;
import static com.careyoutbaby.swetarabi.babycare.PickPlaces2Activity.KEY_EXTRA_CONTACT_ID;
import static com.careyoutbaby.swetarabi.babycare.R.id.map;


public class OldPlaceRecord extends AppCompatActivity implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener {


    public int Boundary_ID;
    public DBHelper dbHelper;
    private ListView listView;

    private GoogleMap mMap;
    public GoogleApiClient mGoogleApiClient;
    public Marker mMarker =null;

    public LinearLayout save_new_lay;
    public LinearLayout save_new;
    public LinearLayout oldList;
    public EditText boundary_nane;
    public ScrollView saved_location;
    public LinearLayout currentBoundary;

    public Button save_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_place_record);

        setupActionBar();
        //check Network Status
        isInternetConnectionEnable();

        Boundary_ID = getIntent().getIntExtra(KEY_EXTRA_CONTACT_ID, 0);

        boundary_nane = (EditText) findViewById(R.id.boundary_nane);
        save_new_lay = (LinearLayout) findViewById(R.id.save_new_lay);
        save_new = (LinearLayout) findViewById(R.id.save_new);
        oldList = (LinearLayout) findViewById(R.id.oldlist);
        saved_location = (ScrollView) findViewById(R.id.saved_location);
        currentBoundary = (LinearLayout) findViewById(R.id.currentBoundary);
        save_btn = (Button) findViewById(R.id.save_btn);
        save_new_lay.setVisibility(View.INVISIBLE);
        saved_location.setVisibility(View.VISIBLE);



        dbHelper = new DBHelper(this);

        final Cursor cursor = dbHelper.getAllLocation();
        String [] columns = new String[] {
                DBHelper.BOUNDARY_COLUMN_NAME,
                DBHelper.BOUNDARY_COLUMN_LATLNG
        };
        int [] widgets = new int[] {
                R.id.textView1z1,
                R.id.textView2z1
        };

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.placepicker1,
                cursor, columns, widgets, 0);
        listView = (ListView)findViewById(R.id.listView1);
        listView.setAdapter(cursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                Cursor itemCursor = (Cursor) OldPlaceRecord.this.listView.getItemAtPosition(position);
                String LatLng_String  = itemCursor.getString(itemCursor.getColumnIndex(DBHelper.BOUNDARY_COLUMN_LATLNG));
                String name  = itemCursor.getString(itemCursor.getColumnIndex(DBHelper.BOUNDARY_COLUMN_NAME));

                //split the String in two part
                final String[] parts = LatLng_String.split(",",2);

                String xs = parts[0];
                String ys = parts[1];

                //convert String to Double value
                Double x = Double.parseDouble(xs);
                Double y = Double.parseDouble(ys);

                //Create new latLng Object
                LatLng m = new LatLng(x,y);

                //Remove Last marker to the location
                /*if (mMarker != null) {
                    mMarker.remove();
                }*/


                //Add marker to the location\
                mMarker = mMap.addMarker(new MarkerOptions()
                        //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ilog))
                        .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                        .position(m)
                        //.flat(true)
                        .title(name));


                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(m)           // Sets the center of the map to Mountain View
                        .zoom(18)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(60)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        });




        save_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_new_lay.setVisibility(View.VISIBLE);
                saved_location.setVisibility(View.INVISIBLE);

            }
        });



        oldList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_new_lay.setVisibility(View.INVISIBLE);
                saved_location.setVisibility(View.VISIBLE);
            }
        });




        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = boundary_nane.getText().toString();
                if(name.isEmpty()){
                    Toast.makeText(OldPlaceRecord.this,"Invalid Name",Toast.LENGTH_SHORT).show();
                }

            }
        });

        /*---------------------------------------------------------------------------------------
      * Date : 20/03/2017  3:34 PM
      * Description: check Google Service is Available or not
      * ---------------------------------------------------------------------------------------*/

        if (android.os.Build.VERSION.SDK_INT >= M) {
            checkLocationPermission();
        }

        if (googleServicesAvailable()) {
            //Toast.makeText(this, "Every thing is good!", Toast.LENGTH_LONG).show();
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(map);
            mapFragment.getMapAsync(this);
        } else {
            //no google maps layout
            //setContentView(R.layout.activity_google_services_not_available);
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
            // onBackPressed();
        }
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

            Toast.makeText(OldPlaceRecord.this,"Disconnected! Your are now offline! ",Toast.LENGTH_SHORT).show();
        }

    }



     /*---------------------------------------------------------------------------------------
      * Date : 20/03/2017  3:34 PM
      * Description: Menu Method
      * ---------------------------------------------------------------------------------------*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_type_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mapTypeNone:
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
            case R.id.mapTypeNormal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.mapTypeSatellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.mapTypeTerrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.mapTypeHybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case android.R.id.home: // back button in Actionbar
                onBackPressed();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }












     /*---------------------------------------------------------------------------------------
      * Date : 20/03/2017  3:34 PM
      * Description: Method check Google Service Available or not
      * ---------------------------------------------------------------------------------------*/

    private boolean googleServicesAvailable() {

        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Can't connect to play Services !", Toast.LENGTH_LONG).show();
        }

        return false;
    }







    /*---------------------------------------------------------------------------------------
      * Date : 20/03/2017  3:34 PM
      * Description: Method check Location Permission
      * ---------------------------------------------------------------------------------------*/

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(OldPlaceRecord.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }

    }



    /*---------------------------------------------------------------------------------------
      * Date : 20/03/2017  3:34 PM
      * Description: On Ready Method
      * ---------------------------------------------------------------------------------------*/

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);




        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }


        /*LatLng m = new LatLng(19.198020643419174, 84.7491118311882);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(m)           // Sets the center of the map to Mountain View
                .zoom(18)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(60)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/


       Boundary();
    }





    /*---------------------------------------------------------------------------------------
      * Date : 20/03/2017  3:34 PM
      * Description: On create Boundary
      * ---------------------------------------------------------------------------------------*/


    private void Boundary(){

        ArrayList<String> location_list;
        LatLng [] loc = new LatLng[6];
        try{

            location_list = dbHelper.getAllLocationLatLng();
            for(int i=0; i<location_list.size();i++){

                String loc_str = location_list.get(i);

                //split the String in two part
                final String[] parts = loc_str.split(",",2);
                String xs = parts[0];
                String ys = parts[1];

                //convert String to Double value
                Double x = Double.parseDouble(xs);
                Double y = Double.parseDouble(ys);

                //Create new latLng Object
                loc[i] = new LatLng(x,y);


            }



            Polygon polygon = mMap.addPolygon(new PolygonOptions()
                    .add(
                            loc[0],
                            loc[1],
                            loc[2],
                            loc[3],
                            loc[4],
                            loc[5]
                    )
                    .strokeColor(Color.argb(100,0,0,0))
                    .fillColor(Color.argb(60,255,140,179)));

        }catch (NullPointerException e){
            Toast.makeText(OldPlaceRecord.this,e.toString(),Toast.LENGTH_SHORT).show();
        }


    }







     /*---------------------------------------------------------------------------------------
      * Date : 20/03/2017  3:34 PM
      * Description: Method ,Initial build Google API client
      * ---------------------------------------------------------------------------------------*/


    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();

    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
