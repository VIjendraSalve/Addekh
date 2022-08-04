package com.wht.addekho;

import static com.wht.addekho.Constant.IConstant.USER_PHOTO;

import android.Manifest;
import android.accounts.NetworkErrorException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.wht.addekho.Activties.ActivityAddStore;
import com.wht.addekho.Activties.AddAdsActivity;
import com.wht.addekho.Activties.BottomDialogFragment;
import com.wht.addekho.Constant.IConstant;
import com.wht.addekho.Constant.IUrls;
import com.wht.addekho.Constant.Interface;
import com.wht.addekho.Fragments.AdFragment;
import com.wht.addekho.Fragments.BagFragment;
import com.wht.addekho.Fragments.BottomsheetDialogMyStoreListFragment;
import com.wht.addekho.Fragments.BottomsheetPublishAdFragment;
import com.wht.addekho.Fragments.HomeFragment;
import com.wht.addekho.Activties.InitialActivity.ProfileActivity;
import com.wht.addekho.Fragments.StoreFragment;
import com.wht.addekho.Helper.GPSTracker;
import com.wht.addekho.Helper.Helper_Method;
import com.wht.addekho.Helper.MyLocationService;
import com.wht.addekho.Helper.OnGPS;
import com.wht.addekho.Helper.SharedPref;
import com.wht.addekho.map_custom_search.PickLocationActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeoutException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks {


    private final static int REQUEST_LOCATION = 199;
    protected GoogleApiClient mGoogleApiClient;
    FloatingActionButton floatingActionButton;
    LinearLayout linearLayout;
    LinearLayout bottom_sheet_image;
    BottomSheetBehavior bottomSheetBehavior;
    ImageView imageView;
    Location gps_loc;
    Location network_loc;
    Location final_loc;
    double longitude;
    double latitude;
    String userCountry, userAddress;
    ImageView img_profile;
    ActivityResultLauncher<Intent> launchSomeActivity = null;
    private GPSTracker gpsTracker;
    private boolean statusOfGPS;
    private Geocoder geocoder;
    private TextView tv_location;
    private Handler handler = new Handler();
    private Activity _act;
    private String flagToOpenAddStoreForm = "0"; // 0: store not added 1: store added

    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;
    private OnGPS onGPS = new OnGPS();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _act = MainActivity.this;
        //loading the default fragment
        loadFragment(new HomeFragment());

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        img_profile = (ImageView) findViewById(R.id.img_profile);
        if (!SharedPref.getPrefs(_act, USER_PHOTO).equals("null")) {
            Glide.with(_act)
                    .load(SharedPref.getPrefs(_act, USER_PHOTO))
                    .apply(new RequestOptions().placeholder(R.drawable.default_user).error(R.drawable.default_user))
                    .into(img_profile);
        } else {

            Glide.with(_act)
                    .load(R.drawable.default_user)
                    .apply(new RequestOptions().placeholder(R.drawable.default_user).error(R.drawable.default_user))
                    .into(img_profile);

        }

        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                bottomSheetBehavior.setHideable(true);
//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                if (flagToOpenAddStoreForm.equals("1")) {
                    Intent intentstore = new Intent(getApplicationContext(), AddAdsActivity.class);
                    startActivity(intentstore);
                } else {
                    Intent intentstore = new Intent(getApplicationContext(), ActivityAddStore.class);
                    intentstore.putExtra("flag", "0");
                    startActivity(intentstore);
                }
//                openBottomSheetPublishAdDialog();

            }
        });

        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        tv_location = findViewById(R.id.tv_location);
        boolean gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsStatus) {
            Toast.makeText(_act, "gps off", Toast.LENGTH_SHORT).show();
            statusCheck();
        }

        if (onGPS.IsGpsOn(MainActivity.this)) {
            updateLocation();
            Toast.makeText(_act, "Function called", Toast.LENGTH_SHORT).show();
        }

        Dexter.withActivity(MainActivity.this)
                .withPermissions(
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                            try {
                                if (ActivityCompat.checkSelfPermission(_act, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                        ActivityCompat.checkSelfPermission(_act, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                network_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (gps_loc != null) {
                                final_loc = gps_loc;
                                latitude = final_loc.getLatitude();
                                longitude = final_loc.getLongitude();
                            } else if (network_loc != null) {
                                final_loc = network_loc;
                                latitude = final_loc.getLatitude();
                                longitude = final_loc.getLongitude();
                            } else {
                                latitude = 0.0;
                                longitude = 0.0;
                            }

                            latitude = Double.parseDouble(SharedPref.getPrefs(_act, IConstant.MyCurrentLatitude));
                            longitude = Double.parseDouble(SharedPref.getPrefs(_act, IConstant.MyCurrentLongitude));

                            try {
                                Log.d("locationDetails", "latitude: " + latitude);
                                Log.d("locationDetails", "longitude: " + longitude);
                                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                if (addresses != null && addresses.size() > 0) {
                                    userCountry = addresses.get(0).getCountryName();
                                    userAddress = addresses.get(0).getAddressLine(0);

                                    Log.d("locationDetails", "getCountryName: " + addresses.get(0).getCountryName());
                                    Log.d("locationDetails", "getAddressLine 0 : " + addresses.get(0).getAddressLine(0));
                                    Log.d("locationDetails", "getAddressLine 1 : " + addresses.get(0).getAddressLine(1));
                                    Log.d("locationDetails", "getLocality : " + addresses.get(0).getLocality());
                                    Log.d("locationDetails", "getAdminArea : " + addresses.get(0).getAdminArea());
                                    Log.d("locationDetails", "getSubAdminArea : " + addresses.get(0).getSubAdminArea());
                                    Log.d("locationDetails", "getSubLocality : " + addresses.get(0).getSubLocality());

                                    SharedPref.setPrefs(MainActivity.this, IConstant.LocationToSetOnDashboard, addresses.get(0).getSubLocality());
                                    tv_location.setText(SharedPref.getPrefs(MainActivity.this, IConstant.LocationToSetOnDashboard));
                                } else {
                                    userCountry = "Unknown";
                                    tv_location.setText(userCountry);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();


        tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PickLocationActivity.class);
                launchSomeActivity.launch(intent);
            }
        });

        launchSomeActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                       /* if (result.getResultCode() == 123) {
                            Intent data = result.getData();
                        }*/
                        tv_location.setText(SharedPref.getPrefs(MainActivity.this, IConstant.LocationToSetOnDashboard));
                    }
                });

        getMyStoreList();
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void setLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Dexter.withActivity(MainActivity.this)
                .withPermissions(
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                )
                .withListener(new MultiplePermissionsListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                            try {
                                gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                network_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (gps_loc != null) {
                                final_loc = gps_loc;
                                latitude = final_loc.getLatitude();
                                longitude = final_loc.getLongitude();
                            } else if (network_loc != null) {
                                final_loc = network_loc;
                                latitude = final_loc.getLatitude();
                                longitude = final_loc.getLongitude();
                            } else {
                                latitude = 0.0;
                                longitude = 0.0;
                            }

                            try {
                                Log.d("locationDetails", "latitude: " + latitude);
                                Log.d("locationDetails", "longitude: " + longitude);
                                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                if (addresses != null && addresses.size() > 0) {
                                    userCountry = addresses.get(0).getCountryName();
                                    userAddress = addresses.get(0).getAddressLine(0);

                                    Log.d("locationDetails", "getCountryName: " + addresses.get(0).getCountryName());
                                    Log.d("locationDetails", "getAddressLine 0 : " + addresses.get(0).getAddressLine(0));
                                    Log.d("locationDetails", "getAddressLine 1 : " + addresses.get(0).getAddressLine(1));
                                    Log.d("locationDetails", "getLocality : " + addresses.get(0).getLocality());
                                    Log.d("locationDetails", "getAdminArea : " + addresses.get(0).getAdminArea());
                                    Log.d("locationDetails", "getSubAdminArea : " + addresses.get(0).getSubAdminArea());
                                    Log.d("locationDetails", "getSubLocality : " + addresses.get(0).getSubLocality());

                                    SharedPref.setPrefs(MainActivity.this, IConstant.LocationToSetOnDashboard, addresses.get(0).getSubLocality());
                                    tv_location.setText(SharedPref.getPrefs(MainActivity.this, IConstant.LocationToSetOnDashboard));
                                } else {
                                    userCountry = "Unknown";
                                    tv_location.setText(userCountry);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();

    }

    private void getMyStoreList() {
        Interface api = IUrls.getRetrofit(IUrls.BASE_URL).create(Interface.class);
        Call<ResponseBody> result = api.PostMyStorelist(
                SharedPref.getPrefs(_act, IConstant.USER_ID),
                "",
                "1",
                -1);
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {


                    output = response.body().string();
                    Log.d("my_tag43", "Response: " + output);
                    try {
                        JSONObject i = new JSONObject(output);
                        String stringCode = i.getString("result");

                        if (stringCode.equalsIgnoreCase("true")) {
                            JSONArray jsonArray = i.getJSONArray("my_stores");
                            if (jsonArray.length() > 0) {
                                flagToOpenAddStoreForm = "1";
                            } else {
                                flagToOpenAddStoreForm = "0";
                            }

                        } else {
                            flagToOpenAddStoreForm = "0";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Helper_Method.toaster(_act, t.getMessage());
                Log.d("Error", "onFailure: " + t.getMessage());

                if (t instanceof NetworkErrorException)
                    Helper_Method.warnUser(_act, "Network Error", getString(R.string.error_network), true);
                else if (t instanceof IOException)
                    Helper_Method.warnUser(_act, "Connection Error", getString(R.string.error_network), true);
                    //else if (t instanceof ServerError)
                    //   Helper_Method.warnUser(_act, "Server Error", getString(R.string.error_server), true);
                else if (t instanceof ConnectException)
                    Helper_Method.warnUser(_act, "No Connection Error", getString(R.string.error_connection), true);
                    //else if (t instanceof ConnectException)
                    //Helper_Method.warnUser(_act, "No Connection Error", getString(R.string.error_connection), true);
                else if (t instanceof TimeoutException)
                    Helper_Method.warnUser(_act, "Timeout Error", getString(R.string.error_timeout), true);
                else if (t instanceof SocketTimeoutException)
                    Helper_Method.warnUser(_act, "Timeout Error", getString(R.string.error_timeout), true);
                else
                    Helper_Method.warnUser(_act, "Unknown Error", getString(R.string.error_something_wrong), true);

            }
        });
    }

    private void enableLoc() {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(2 * 1000);
        locationRequest.setFastestInterval(5 * 1000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:

                        geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                        Log.d("longitude", "onCreate: " + gpsTracker.getLongitude());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (addresses != null) {
                            if (addresses.size() > 0) {
                                String cityName = addresses.get(0).getLocality();
                                String stateName = addresses.get(0).getAddressLine(1);
                                String countryName = addresses.get(0).getAddressLine(2);
                                Log.d("CityName", "onCreate: " + cityName);

                            }
                        }

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {

                            // Ignore the error.
                        }
                        break;
                }
            }
        });
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void openBottomSheetPublishAdDialog() {
        BottomsheetPublishAdFragment bottomSheet = new BottomsheetPublishAdFragment();
        bottomSheet.show(getSupportFragmentManager(),
                "ModalBottomSheet");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();
                break;

            case R.id.navigation_store:
                fragment = new StoreFragment();
//                Intent intentstore =new Intent(getApplicationContext(), BottomsheetActivity.class);
//                startActivity(intentstore);
//                openBottomSheetDialog();
//                openBottomSheetStoreDialog();

                break;

            case R.id.placeholder:
                Intent intentstore = new Intent(getApplicationContext(), AddAdsActivity.class);
                startActivity(intentstore);
                break;

            case R.id.navigation_ads:
                fragment = new AdFragment();

//                openBottomSheetStoreDialog();
                break;

            case R.id.navigation_profile:
                //Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                //startActivity(intent);

                fragment = new BagFragment();
                break;
        }

        return loadFragment(fragment);
    }

    private void openBottomSheetStoreDialog() {
        BottomsheetDialogMyStoreListFragment addBottomDialogFragment =
                BottomsheetDialogMyStoreListFragment.newInstance(this);
        addBottomDialogFragment.show(getSupportFragmentManager(),
                BottomsheetDialogMyStoreListFragment.TAG);
    }

    private void openBottomSheetDialog() {
        BottomDialogFragment addBottomDialogFragment =
                BottomDialogFragment.newInstance(this);
        addBottomDialogFragment.show(getSupportFragmentManager(),
                BottomDialogFragment.TAG);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack("TAG")
                    .commit();
            return true;
        }
        return false;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        MainActivity.super.onBackPressed();
                        finishAffinity();
                    }
                }).create().show();
    }

    /********************* Background Location Update**************************/
    private void updateLocation() {
        buildLocationRequest();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
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
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, getPendingIntent());
    }

    private PendingIntent getPendingIntent() {
        Log.d("MYTAG", "getPendingIntent: ");
        Intent intent = new Intent(this, MyLocationService.class);
        intent.setAction(MyLocationService.ACTION_PROCESS_UPDATE);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private void buildLocationRequest() {

        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(18000);
        locationRequest.setFastestInterval(18000);
        locationRequest.setSmallestDisplacement(10f);

    }

    /********************* Background Location Update END**************************/


}