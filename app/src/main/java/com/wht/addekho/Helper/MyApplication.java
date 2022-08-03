package com.wht.addekho.Helper;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.wht.addekho.map_custom_search.utils.VolleySingleton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



public class MyApplication extends Application {
    public static final String TAG = MyApplication.class.getSimpleName();
    public static VolleySingleton volleyQueueInstance;
    private static MyApplication mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private GoogleApiHelper googleApiHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        //FacebookSdk.sdkInitialize(getApplicationContext());
        //AppEventsLogger.activateApp(getApplicationContext());
        printHashKey();
        // AppEventsLogger.activateApp(this);
        instantiateVolleyQueue();
        googleApiHelper = new GoogleApiHelper(mInstance);


    }



    public void printHashKey() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.wht.addekho", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public static Context getAppContext() {
        return mInstance.getApplicationContext();
    }

    public GoogleApiHelper getGoogleApiHelperInstance() {
        return this.googleApiHelper;
    }

    public static GoogleApiHelper getGoogleApiHelper() {
        return getInstance().getGoogleApiHelperInstance();
    }


    /*App.getGoogleApiHelper().setConnectionListener(new GoogleApiHelper.ConnectionListener() {
        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        }

        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onConnected(Bundle bundle, GoogleApiClient googleApiClient) {
            //this function will call whenever google api connected or already connected when setting listener
            //You are connected do what ever you want
            //Like i get last known location
            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        }
    });
    //Or you can also get it like this

            if(App.getGoogleApiHelper().isConnected())
    {
        //Get google api client from anywhere
        GoogleApiClient client = App.getGoogleApiHelper().getGoogleApiClient();
    }*/

    public void instantiateVolleyQueue() {
        volleyQueueInstance = VolleySingleton.getInstance(getApplicationContext());
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {

            mRequestQueue = Volley.newRequestQueue(getApplicationContext(),
                    new HurlStack());

        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        req.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public void showGenericToast(String error) {
        try {
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
