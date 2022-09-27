package com.wht.addekh.Activties;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.wht.addekh.BaseActivity;
import com.wht.addekh.Helper.ConnectionDetector;
import com.wht.addekh.Helper.SharedPref;
import com.wht.addekh.Helper.Validations;
import com.wht.addekh.R;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ActivityGetLocation extends BaseActivity implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleMap.OnCameraIdleListener,
        GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraMoveCanceledListener {

    private static final String LOG_TAG = "PlaceSelectionListener";
    private static final int REQUEST_SELECT_PLACE = 1000;
    private static LatLngBounds BOUNDS_MOUNTAIN_VIEW;
    Geocoder geocoder;
    List<Address> addresses;
    FusedLocationProviderClient mFusedLocationClient;
    int REQUEST_CHECK_SETTINGS = 100;
    private com.google.android.gms.location.LocationRequest locationRequest;
    private GoogleMap mMap;
    private String addresss = "";
    private String city = "";
    private String state = "";
    private final String country = "";
    private final String postalCode = "";
    private final String knownName = "";
    private final String marker = "";
    private final Double lat = null;
    private final Double lon = null;
    private Double latitude = 0.0;
    private Double longitude = 0.0;
    private Location myLocation;
    private ProgressDialog progressDialog;
    private FloatingActionButton fab;
    private boolean flag = true, flag1 = true;
    //private ADDPOST addpost;
    //private ArrayList<ADDPOST> addposts = new ArrayList<ADDPOST>();
    private boolean statusOfGPS;
    private Button btn_get_location;
    private ImageView iv_marker, iv_marker_ring, iv_marker_shadow;
    private Boolean userTouch = false;
    private Location mLastLocation;
    //private Bundle bundle;
    private MenuItem miActionProgressItem;
    private final double current_lat = 0.0;
    private final double current_lon = 0.0;
    private Activity _act;
    private Validations validations;
    private SharedPref sharedPrefMgr;
    private ConnectionDetector connectionDetector;
    private Boolean myLocationClick = false;
    private EditText tvAddress;
    private Dialog dialog;
    private TextView textLat, textLng;
    private EditText etAddress;
    private Button buttonSave, buttonExit;
    private final String user_id = null;
    private final String strAddressId = null;
    private final String isDefault = "0";
    private CheckBox cb_defaultAddress;
    private final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            CurrentLocation();
            Log.d("MyTag2", "onComplete: " + mLastLocation.getLatitude() + " " + mLastLocation.getLongitude());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);
        myLocationClick = true;

        _act = ActivityGetLocation.this;
        connectionDetector = ConnectionDetector.getInstance(_act);
        tvAddress = findViewById(R.id.tvAddress);
        connectionDetector = ConnectionDetector.getInstance(_act);

        validations = new Validations();
        removePhoneKeypad();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Initialize Places.
        Places.initialize(getApplicationContext(), "AIzaSyDeY_aQDqJnP1t9znDD1CRQ1xzg032CzO0");
        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        geocoder = new Geocoder(ActivityGetLocation.this, Locale.getDefault());
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        iv_marker = (ImageView) findViewById(R.id.iv_marker);
        iv_marker_ring = (ImageView) findViewById(R.id.iv_marker_ring);
        iv_marker_shadow = (ImageView) findViewById(R.id.iv_marker_shadow);
        check_connection();

    }

    private void check_connection() {
        //Default Dress Code Webcall
        if (ConnectionDetector.checkConnection(_act)) {
            if (statusOfGPS) {
                init();
            } else {
                OnGPS();
            }

        } else {
            Snackbar.make(this.findViewById(android.R.id.content), "Internet Not Available",
                    Snackbar.LENGTH_INDEFINITE).setAction("RETRY",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ActivityGetLocation.this.check_connection();
                        }
                    }).show();

        }
    }

    private void init() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(new LatLng(current_lat, current_lon), new LatLng(current_lat, current_lon));
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading please wait...");
        progressDialog.setCancelable(true);
        // progressDialog.show();
        //progress_view=new ProgressBar(this);

        //bundle=getIntent().getExtras();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Method #2
                // List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

                // Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.OVERLAY, Arrays.asList(Place.Field.NAME, Place.Field.ID, Place.Field.ADDRESS, Place.Field.LAT_LNG))
                        .setTypeFilter(TypeFilter.ADDRESS)
                        .setCountry("IN")
                        .setLocationBias(RectangularBounds.newInstance(new LatLng(19.994200, 73.797045), new LatLng(19.994200, 73.797045)))
                        .build(ActivityGetLocation.this);
                startActivityForResult(intent, REQUEST_SELECT_PLACE);
            }
        });
        if (mMap == null) {
            SupportMapFragment frag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment1);
            frag.getMapAsync(this);
        }

        btn_get_location = (Button) findViewById(R.id.btn_get_location);
        btn_get_location.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                double distance = distance(19.994200, 73.797045, latitude, longitude);

                if (latitude != null) {


                    /* */
                    // Toast.makeText(ActivityGetLocation.this, "" + latitude, Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    bundle.putString("lat", String.valueOf(latitude));
                    bundle.putString("lagn", String.valueOf(longitude));
                    bundle.putString("Address", tvAddress.getText().toString());
                    //bundle.putString("Address", addresss);
                    bundle.putString("city", city);
                    bundle.putString("state", state);
                    bundle.putBoolean("location", true);

                    /*Intent intent = new Intent();
                    intent.putExtra("lat", latitude);
                    intent.putExtra("lagn", longitude);
                    setResult(123, intent);
                    finish();*/

                    // AddAddressDailog(String.valueOf(latitude), String.valueOf(longitude), addresss, city, state);
                    //bundle.putDouble("distance", distance);
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    setResult(123, intent);
                    finish();


                } else {
                    Toast.makeText(ActivityGetLocation.this, "Location is not getting.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //setUpMap();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        } else flag1 = true;
        //myLocation = mMap.getMyLocation();
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                flag = true;
                myLocationClick = true;
                // CurrentLocation();
                return false;
            }
        });

        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraMoveCanceledListener(this);
        getLocation();

    }

    private void getAddress(double lat, double lon) {

        Log.d("Distance: ", "onClick: " + distance(19.994200, 73.797045, lat, lon));

        try {
            addresses = geocoder.getFromLocation(lat, lon, 5); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if (addresses != null && addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                city = addresses.get(0).getLocality();
                state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                Log.d("my tag", "onMapClick: " + address + " " + city + " " + state + " " + country + " " + postalCode + " " + knownName);
                latitude = lat;
                longitude = lon;
                // addresss = address + ", " + city + ", " + state + ", " + country + ", " + postalCode;
                addresss = address;

                // Log.d("my tag", "onClick: " + distance(20.014043427246794, 73.7562814, latitude, longitude));
                //getSupportActionBar().setTitle(addresss);
                tvAddress.setText(addresss);
                //progress_view.setVisibility(View.GONE);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void CurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));

    }

    private void OnGPS() {

        /*LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);*/
        buildLocationRequest();

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // Toast.makeText(MainActivity.this, "addOnSuccessListener", Toast.LENGTH_SHORT).show();
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(MainActivity.this, "addOnFailureListener", Toast.LENGTH_SHORT).show();
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(ActivityGetLocation.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        mFusedLocationClient.getLastLocation().addOnCompleteListener(
                new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            // buildLocationRequest();

                            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(ActivityGetLocation.this);
//
//                            mFusedLocationClient.requestLocationUpdates(
//                                    locationRequest, mLocationCallback,
//                                    Looper.myLooper()
//                            );
                        } else {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            CurrentLocation();
                            Log.d("MyTag1", "onComplete: " + location.getLatitude() + " " + location.getLongitude());
                            //latTextView.setText(location.getLatitude()+"");
                            //lonTextView.setText(location.getLongitude()+"");
                        }
                    }
                }
        );
    }

//    private void buildLocationRequest() {
//        locationRequest = new LocationRequest();
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(5000);
//        locationRequest.setFastestInterval(3000);
//        locationRequest.setSmallestDisplacement(10f);
//    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_SELECT_PLACE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("MyTag", "Place: " + place.getName() + ", " + place.getAddress());
                tvAddress.setText(place.getAddress());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 16.0f));
                //getAddress(place.getLatLng().latitude,place.getLatLng().latitude);
                addresss = place.getAddress();
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("MyTag", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                init();
                //getLocation();
                Toast.makeText(getApplicationContext(), "GPS enabled", Toast.LENGTH_LONG).show();
            } else {
                OnGPS();
                Toast.makeText(getApplicationContext(), "Please On GPS...", Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onCameraMoveStarted(int reason) {

        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            if (!userTouch) {
                markerUp();
                userTouch = true;
            }
            // Toast.makeText(this, "The user gestured on the map.", Toast.LENGTH_SHORT).show();
        } else if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_API_ANIMATION) {
            Log.d("mapaction", "The user tapped something on the map.");
            // Toast.makeText(this, "The user tapped something on the map.", Toast.LENGTH_SHORT).show();
        } else if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION) {
            Log.d("mapaction", "The app moved the camera.");
            //  Toast.makeText(this, "The app moved the camera.",  Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCameraMove() {
        //Log.d("mapaction","The camera is moving.");
        // Toast.makeText(this, "The camera is moving.",  Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCameraMoveCanceled() {
        Log.d("mapaction", "Camera movement canceled.");
        // Toast.makeText(this, "Camera movement canceled.",               Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCameraIdle() {

        if (userTouch) {
            markerDown();
            userTouch = false;
        }
        if (myLocationClick) {
            LatLng cl = mMap.getCameraPosition().target;
            getAddress(cl.latitude, cl.longitude);
            myLocationClick = false;
        }
    }

    public void markerUp() {
        //progress_view.setVisibility(View.VISIBLE);
        // getSupportActionBar().setTitle("Getting Address...");
        tvAddress.setText("Getting Address...");
        iv_marker.clearAnimation();
        iv_marker_shadow.clearAnimation();
        iv_marker.animate().translationY(-50f).setInterpolator(new AccelerateInterpolator()).setDuration(300);
        //progress_view.animate().translationY(-50f).setInterpolator(new AccelerateInterpolator()).setDuration(300);
        //iv_marker_shadow.animate().translationY(10f).translationX(20f).setInterpolator(new AccelerateInterpolator());
        iv_marker_shadow.animate().translationY(-50f).translationX(30f).setInterpolator(new AccelerateInterpolator()).setDuration(300);
        iv_marker_ring.setVisibility(View.VISIBLE);
        Animation zoom_in = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        iv_marker_ring.startAnimation(zoom_in);

    }

    public void markerDown() {
        iv_marker.clearAnimation();
        iv_marker_shadow.clearAnimation();

        iv_marker.animate().translationY(0f).setInterpolator(new DecelerateInterpolator()).setDuration(300);
        //progress_view.animate().translationY(0f).setInterpolator(new DecelerateInterpolator()).setDuration(300);
        Animation zoom_out = AnimationUtils.loadAnimation(this, R.anim.zoom_out);
        iv_marker_ring.startAnimation(zoom_out);
        iv_marker_ring.setVisibility(View.GONE);
        LatLng cl = mMap.getCameraPosition().target;
        getAddress(cl.latitude, cl.longitude);
        iv_marker_shadow.animate().translationY(0f).translationX(0f).setInterpolator(new DecelerateInterpolator());
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        //overridePendingTransition(R.animator.left_right, R.animator.right_left);
        return super.onOptionsItemSelected(item);
    }



   /* private void webcallAddAddress() {

        Interface api = IUrls.getRetrofit(IUrls.BASE_URL).create(Interface.class);
        Call<ResponseBody> result = api.POSTAddAddress(user_id,
                etAddress.getText().toString().trim(), "", "", "", textLat.getText().toString().trim(),
                textLng.getText().toString().trim(), isDefault, "1");

        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                //EraseLocalData();
                try {
                    output = response.body().string();
                    Log.d("my_tag", "onResponseSachin: " + output);
                    try {
                        JSONObject i = new JSONObject(output);
                        String stringCode = i.getString(IConstant.RESPONSE_CODE);
                        String stringMsg = i.getString(IConstant.RESPONSE_MESSAGE);
                        Log.d("my_tag2", "onResponseSachin: " + output);
                        if (stringCode.equalsIgnoreCase(IConstant.RESPONSE_SUCCESS)) {
                            Log.d("my_tag3", "onResponseSachin: " + output);

                            scheduleDismiss();
                            showToast(stringMsg);
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("isDefault", isDefault);
                            returnIntent.putExtra("lat", textLat.getText().toString().trim());
                            returnIntent.putExtra("lagn", textLng.getText().toString().trim());
                            returnIntent.putExtra("Address", etAddress.getText().toString().trim());
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();

                        } else {
                            showToast(stringMsg);
                            scheduleDismiss();

                        }


                    } catch (JSONException e) {
                        scheduleDismiss();
                    }

                } catch (
                        IOException e) {
                    e.printStackTrace();
                    scheduleDismiss();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(_act, "Technical Error", Toast.LENGTH_SHORT).show();
                scheduleDismiss();

            }
        });
    }*/



    private void showToast(String msg) {
        Toast.makeText(_act, msg, Toast.LENGTH_SHORT).show();
    }

    public void removePhoneKeypad() {
        if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
            System.out.println("getCurrentFocus() in frag");
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            IBinder binder = getCurrentFocus().getWindowToken();
            inputManager.hideSoftInputFromWindow(binder,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}

