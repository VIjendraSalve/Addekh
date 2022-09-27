package com.wht.addekh.Helper;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.LocationManager;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

/**
 * Created by Asus on 10/25/2017.
 */

public class OnGPS {

    public static final int REQUEST_LOCATION = 123;
    private boolean statusOfGPS;
    protected GoogleApiClient mGoogleApiClient;
    private boolean result = false;

    public boolean IsGpsOn(final Activity activity) {
        LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);


        if (!statusOfGPS) {
            if (MyApplication.getGoogleApiHelper().isConnected()) {
                //Get google api client from anywhere
                mGoogleApiClient = MyApplication.getGoogleApiHelper().getGoogleApiClient();

                LocationRequest locationRequest = LocationRequest.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                locationRequest.setInterval(30 * 1000);
                locationRequest.setFastestInterval(5 * 1000);

                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

                // **************************
                builder.setAlwaysShow(true); // this is the key ingredient
                // **************************

                PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
                result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                    @Override
                    public void onResult(LocationSettingsResult result) {
                        final Status status = result.getStatus();
                        final LocationSettingsStates state = result
                                .getLocationSettingsStates();
                        switch (status.getStatusCode()) {
                            case LocationSettingsStatusCodes.SUCCESS:
                                // All location settings are satisfied. The client can
                                // initialize location
                                // requests here.
                                break;
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied. But could be
                                // fixed by showing the user
                                // a dialog.
                                try {
                                    // Show the dialog by calling
                                    // startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    status.startResolutionForResult((Activity) activity, REQUEST_LOCATION);
                                } catch (IntentSender.SendIntentException e) {
                                    // Ignore the error.
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                // Location settings are not satisfied. However, we have
                                // no way to fix the
                                // settings so we won't show the dialog.
                                break;
                        }
                    }
                });
            }
        } else {
            result = true;
        }
        return result;
    }

    /*public void myActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_LOCATION)
            {

            }
                //onSelectFromGalleryResult(data);
        }
    }*/
}
