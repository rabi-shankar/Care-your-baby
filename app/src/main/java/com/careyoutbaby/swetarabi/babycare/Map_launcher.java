package com.careyoutbaby.swetarabi.babycare;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Modules.DirectionFinder;
import Modules.DirectionFinderListener;
import Modules.Route;

import static com.careyoutbaby.swetarabi.babycare.R.id.map;

public class Map_launcher extends AppCompatActivity implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,DirectionFinderListener{

    private static final String TAG = "com.careyoutbaby.swetarabi.babycare";
    public DBHelper dbHelper;
    private GoogleMap mMap;
    public GoogleApiClient mGoogleApiClient;
    public Geocoder mGeoCoder = null;
    public List<android.location.Address> mAddresses = null;

    public List<Marker> originMarkers = new ArrayList<>();
    public List<Marker> destinationMarkers = new ArrayList<>();
    public List<Polyline> polylinePaths = new ArrayList<>();
    public Polygon polygon;
    public ProgressDialog progressDialog;
    public LocationRequest mLocationRequest;

    public Location mLastLocation;
    //public LatLng mCurrentLocation ;
    public Marker mCurrLocationMarker;
    public Marker ChildLocationMarker;



    public double latDevice;
    public double lngDevice;
    public double latChild;
    public double lngChild;

    int i=0;
    View v;
    public String flg_str="inside";



    public FirebaseDatabase database;
    public FirebaseDatabase database1;
    public  DatabaseReference myRef;
    public  DatabaseReference noti;

    public boolean boundaryflg =true;


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_launcher);
        setupActionBar();

        final ImageView dis = (ImageView) findViewById(R.id.imageView1);
        final ImageView cl = (ImageView) findViewById(R.id.imageView2);
        final ImageView reg = (ImageView) findViewById(R.id.imageView3);
        final ImageView refr = (ImageView) findViewById(R.id.imageView4);
        final LinearLayout reg_menu = (LinearLayout) findViewById(R.id.linearLayout2);
        final ImageView editloc = (ImageView) findViewById(R.id.imageView1x);
        final ImageView clrloc = (ImageView) findViewById(R.id.imageView2x);

        reg_menu.setVisibility(View.GONE);

        // check Network Status
        isInternetConnectionEnable();

        //mGoogleApiClient =  new mGoogleApiClient.Builder(this,this,this).addA



        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("CHILD_LOCATION");


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String childLocationSt = dataSnapshot.getValue(String.class);
                if (childLocationSt.length()>0) {

                    //split the String in two part
                    final String[] parts = childLocationSt.split(",", 2);

                    String stlatChild = parts[0];
                    String stlngChild = parts[1];
                    latChild = Double.parseDouble(stlatChild);
                    lngChild = Double.parseDouble(stlngChild);


                    if(ChildLocationMarker!=null) {
                        ChildLocationMarker.remove();
                    }

                    LatLng cl = new LatLng(latChild,lngChild);

                    ChildLocationMarker = mMap.addMarker(new MarkerOptions()
                            //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_crop_free_black_24dp))
                            .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                            .position(cl)
                            //.flat(true)
                            .title(cl.toString()));

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(cl)           // Sets the center of the map to Mountain View
                            .zoom(18)                   // Sets the zoom
                            .bearing(90)                // Sets the orientation of the camera to east
                            .tilt(60)                   // Sets the tilt of the camera to 30 degrees
                            .build();                   // Creates a CameraPosition from the builder

                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });




        database1 = FirebaseDatabase.getInstance();
        noti = database1.getReference("NOTIFIACATION");
        noti.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                flg_str = dataSnapshot.getValue(String.class);
                switch(flg_str){
                    case  "inside":
                        //notification(v,"inside");
                        break;
                    case  "outside":
                        notification(v,"outside");
                        break;

                    case "boundary":
                        notification(v,"boundary");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




     /*---------------------------------------------------------------------------------------
      * Date : 20/03/2017  3:34 PM
      * Description: Trigger button find shortest path between child location and App  current
      * location
      * ---------------------------------------------------------------------------------------*/

        dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pathRequest();
            }
        });




        /*---------------------------------------------------------------------------------------
      * Date : 20/03/2017  3:34 PM
      * Description: On Touch button switching between two different  color when button is touch
      * ---------------------------------------------------------------------------------------*/

        dis.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        dis.setBackgroundColor(Color.parseColor("#9dfca800"));
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        dis.setBackgroundColor(Color.parseColor("#b8ffffff"));
                        break;
                    }
                }
                return false;
            }
        });







        /*---------------------------------------------------------------------------------------
      * Date : 20/03/2017  3:34 PM
      * Description: Trigger button
      * ---------------------------------------------------------------------------------------*/


        cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Snackbar.make(view, "Child location ("+flg_str+")" , Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });


        cl.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        cl.setBackgroundColor(Color.parseColor("#9dfca800"));
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        cl.setBackgroundColor(Color.parseColor("#b8ffffff"));
                        break;
                    }
                }
                return false;
            }
        });










        /*---------------------------------------------------------------------------------------
      * Date : 20/03/2017  3:34 PM
      * Description: Trigger button
      * ---------------------------------------------------------------------------------------*/

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Boundary();
                reg_menu.setVisibility(View.VISIBLE);
                clrloc.setImageResource(R.drawable.ic_visibility_off_black_24dp);

                /*new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        reg_menu.setVisibility(View.GONE);
                    }
                }, 10000);*/



            }
        });

        reg.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        reg.setBackgroundColor(Color.parseColor("#9dfca800"));
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        reg.setBackgroundColor(Color.parseColor("#b8ffffff"));
                        break;
                    }
                }
                return false;
            }
        });









        /*---------------------------------------------------------------------------------------
      * Date : 20/03/2017  3:34 PM
      * Description: Trigger button
      * ---------------------------------------------------------------------------------------*/

        refr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LatLng m = new LatLng(19.198020643419174, 84.7491118311882);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(m)           // Sets the center of the map to Mountain View
                        .zoom(18)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(60)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                PolMarks();

            }
        });


        refr.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        refr.setBackgroundColor(Color.parseColor("#123456"));
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        refr.setBackgroundColor(Color.parseColor("#b8ffffff"));//#b8ffffff
                        break;
                    }
                }
                return false;
            }
        });





        /*---------------------------------------------------------------------------------------
      * Date : 20/03/2017  3:34 PM
      * Description: Trigger button
      * ---------------------------------------------------------------------------------------*/

        editloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reg_menu.setVisibility(View.VISIBLE);
                Intent x = new Intent(Map_launcher.this,PickPlaces2Activity.class);
                startActivity(x);




            }
        });





           /*---------------------------------------------------------------------------------------
      * Date : 20/03/2017  3:34 PM
      * Description: Trigger button
      * ---------------------------------------------------------------------------------------*/

        clrloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(boundaryflg){
                    //Remove Last Boundary Location
                    if(polygon!=null){
                        polygon.remove();
                    }
                    clrloc.setImageResource(R.drawable.ic_visibility_black_24dpblack);
                    boundaryflg = false;
                }else {
                    Boundary();
                    clrloc.setImageResource(R.drawable.ic_visibility_off_black_24dp);
                    boundaryflg = true;
                }



            }
        });




     /*---------------------------------------------------------------------------------------
      * Date : 20/03/2017  3:34 PM
      * Description: check Google Service is Available or not
      * ---------------------------------------------------------------------------------------*/

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
      * Date : 20/03/2017  3:34 PM
      * Description: Method check Location Permission
      * ---------------------------------------------------------------------------------------*/

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(Map_launcher.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
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
      * Description: On Ready Method
      * ---------------------------------------------------------------------------------------*/

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);




        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
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

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(500);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (LocationListener) this);
        }



    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }




    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        /*mCurrentLocation = new LatLng(location.getLatitude(), location.getLongitude());*/
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        /*MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
*/
        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (LocationListener) this);
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
                //onBackPressed();
                Intent x=new Intent(this,MainActivity.class);
                startActivity(x);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }






     /*---------------------------------------------------------------------------------------
      * Date : 20/03/2017  3:34 PM
      * Description: Method  to draw Polygon in Google Map
      * ---------------------------------------------------------------------------------------*/

    private void PolMarks( ){
        Polygon polygon = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(19.199047790037735,84.74110007286072),
                        new LatLng(19.198490522610015,84.74220514297485),
                        new LatLng(19.199402413781797,84.74431872367859),
                        new LatLng(19.20025350764912,84.74458694458008),
                        new LatLng(19.200213823755195,84.74714040756226),
                        new LatLng(19.198384979349782,84.74823474884033),
                        new LatLng(19.198020643419174,84.7491118311882),
                        new LatLng(19.196931008151818,84.7487336397171),
                        new LatLng(19.19727043811756,84.74785923957825),
                        new LatLng(19.196348402769786,84.74478542804718),
                        new LatLng(19.196616908156813,84.74463522434235),
                        new LatLng(19.198207666476886,84.7405743598938)
                )
                .strokeColor(Color.argb(100,0,0,0))
                .fillColor(Color.argb(60,255,140,179)));
    }













    /*---------------------------------------------------------------------------------------
      * Date : 19/03/2017  12:34 PM
      * Description: GPS Service
      * ---------------------------------------------------------------------------------------*/

    public void gpsService(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null)
        {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

    }






     /*---------------------------------------------------------------------------------------
      * Date : 20/03/2017  3:34 PM
      * Description: Method Find The Path between Android Device Current Location
      * to The Child Module
      * ---------------------------------------------------------------------------------------*/

    public void pathRequest() {


        String mOrigin = String.valueOf(mLastLocation.getLatitude()) + "," + String.valueOf(mLastLocation.getLongitude());
        String mDestination = String.valueOf(latChild) + "," + String.valueOf(lngChild);


        if (mOrigin.length()<0) {
            Toast.makeText(this, "Starting Location is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mDestination.length()<0) {
            Toast.makeText(this, "Ending Location is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            new DirectionFinder(this, mOrigin, mDestination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }















 /*---------------------------------------------------------------------------------------
      * Date : 20/03/2017  3:34 PM
      * Description: Method  latLngToString name suggested that it Convert   LatLng Object
      * in to respective Address String
      *
      * Input : Two Double Value
      * Output: String
      * ---------------------------------------------------------------------------------------*/

    public String latLngToString(double lat, double lng) {

        mGeoCoder = new Geocoder(getBaseContext(), Locale.ENGLISH);
        StringBuilder strReturnedAddress = new StringBuilder("Address:\n");
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
      * Date : 20/03/2017  3:34 PM
      * Description: Method FonDirectionFinderStart
      *
      * Input : void
      *
      * Output: void
      * ---------------------------------------------------------------------------------------*/


    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait...",
                "Finding Direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }

    }







     /*---------------------------------------------------------------------------------------
      * Date : 20/03/2017  3:34 PM
      * Description: Method onDirectionFinderSuccess
      *
      * Input : List routes type
      *
      * Output: void
      * ---------------------------------------------------------------------------------------*/


    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {

        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        
        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            /*((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);
            */
            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.pointc142))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            /*destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));*/

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.RED).
                    width(8);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
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

            Toast.makeText(Map_launcher.this,"Disconnected! Your are now offline! ",Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onStart() {
        super.onStart();


    }





    /*---------------------------------------------------------------------------------------
      * Date : 20/03/2017  3:34 PM
      * Description: On create Boundary
      * ---------------------------------------------------------------------------------------*/

    private void Boundary(){

        ArrayList<String> location_list;
        LatLng [] loc = new LatLng[6];
        dbHelper = new DBHelper(this);
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
                //Toast.makeText(Map_launcher.this,x+" and "+y,Toast.LENGTH_SHORT).show();


            }

            //Remove Last marker to the location
            if(polygon!=null){
                polygon.remove();
            }

            polygon = mMap.addPolygon(new PolygonOptions()
                    .add(
                            loc[0],
                            loc[1],
                            loc[2],
                            loc[3],
                            loc[4],
                            loc[5]
                    )
                    .strokeColor(Color.RED)
                    .fillColor(Color.argb(60,255,140,179)));


            LatLng c;
            c = getCenterOfPolygon(loc);


            // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(c)           // Sets the center of the map to Mountain View
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(60)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }catch (NullPointerException e){
            Toast.makeText(Map_launcher.this,e.toString(),Toast.LENGTH_SHORT).show();
        }


    }


  /*---------------------------------------------------------------------------------------
      * Date : 28/03/2017  1:53 PM
      * Description: Find the center of Polygon
      * Input : LatLng Array Object
      * Output: LatLng Object
      * ---------------------------------------------------------------------------------------*/



    private static LatLng getCenterOfPolygon(LatLng[] latLngList) {

        double[] centroid = {0.0, 0.0};

        for (int i = 0; i < latLngList.length; i++) {
            centroid[0] += latLngList[i].latitude;
            centroid[1] += latLngList[i].longitude;
        }
        int totalPoints = latLngList.length;
        return new LatLng(centroid[0] / totalPoints, centroid[1] / totalPoints);
    }





    // notification message....
    public void notification(View v, String str){
        Uri u = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationCompat.Builder bul = new NotificationCompat.Builder(this);
        bul.setSmallIcon(R.drawable.ic_audiotrack);
        bul.setNumber(i);
        i=i+1;
        bul.setPriority(1);
        long[] pattern = {0,100,200,300};
        bul.setVibrate(pattern);
        //bul.setLights(Color.RED, 3000, 3000);
        bul.setContentTitle("Baby Care Alert!");
        bul.setContentText("Your Baby is "+str+" the boundary!");
        bul.setSound(u);
        bul.setColor(Color.RED);

        /*Notification sound ...
        Uri u = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        bul.setSound(u);*/

        Intent notx = new Intent(this,Map_launcher.class);
        PendingIntent cont =PendingIntent.getActivity(this,0,notx,PendingIntent.FLAG_UPDATE_CURRENT);

        bul.setContentIntent(cont);
        NotificationManager manger = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        manger.notify(1,bul.build());
        // Write a message to the database
        /*FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");*/


    }


}
