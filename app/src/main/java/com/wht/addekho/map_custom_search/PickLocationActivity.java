package com.wht.addekho.map_custom_search;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.wht.addekho.Constant.IConstant;
import com.wht.addekho.Helper.MyApplication;
import com.wht.addekho.Helper.SharedPref;
import com.wht.addekho.R;
import com.wht.addekho.BaseActivity;
import com.wht.addekho.map_custom_search.adapter.AutoCompleteAdapter;
import com.wht.addekho.map_custom_search.model.PlaceAutoComplete;
import com.wht.addekho.map_custom_search.model.PlacePredictions;
import com.wht.addekho.map_custom_search.utils.VolleyJSONRequest;
import com.wht.addekho.map_custom_search.utils.VolleySingleton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PickLocationActivity extends BaseActivity implements Response.Listener<String>, Response.ErrorListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "VijendraPlaces";
    double latitude;
    double longitude;
    private ListView mAutoCompleteList;
    private EditText edtAddress;
    private String GETPLACESHIT = "places_hit";
    private PlacePredictions predictions=new PlacePredictions();
    private Location mLastLocation;
    private AutoCompleteAdapter mAutoCompleteAdapter;
    private static final int MY_PERMISSIONS_REQUEST_LOC = 30;
    private ImageView iv_cancel;
    private String preFilledText = "";
    private Handler handler;
    private VolleyJSONRequest request;
    private GoogleApiClient mGoogleApiClient;
    private LatLng queriedLocation;
    private ProgressBar progress_bar;
    private static final String TAG_RESULT = "predictions";
    private CardView card_place_list;

    private Handler mHandler;
    private String query = "", search = "";
    PlacesClient placesClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_location);

        // Initialize Places. //New library
        Places.initialize(getApplicationContext(), getResources().getString(R.string.google_maps_key));

        // Create a new Places client instance.
        placesClient = Places.createClient(this);

        edtAddress = (EditText) findViewById(R.id.adressText);
        card_place_list = (CardView) findViewById(R.id.card_place_list);
        mAutoCompleteList = (ListView) findViewById(R.id.searchResultLV);
        //searchBtn = (ImageView) findViewById(R.id.search);
        iv_cancel = (ImageView) findViewById(R.id.iv_cancel);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);

        if (getIntent().hasExtra(IConstant.HINT_TEXT)) {
            preFilledText = getIntent().getStringExtra(IConstant.HINT_TEXT);
            edtAddress.setHint(preFilledText);
        }


        //get permission for Android M
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            fetchLocation();
        } else {

            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOC);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            } else {
                fetchLocation();
            }
        }


        //Add a text change listener to implement autocomplete functionality
        edtAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // optimised way is to start searching for laction after user has typed minimum 3 chars
                if (edtAddress.getText().length() > 0) {


                    progress_bar.setVisibility(View.VISIBLE);
                    iv_cancel.setVisibility(View.GONE);

                    Runnable run = new Runnable() {


                        @Override
                        public void run() {

                            // cancel all the previous requests in the queue to optimise your network calls during autocomplete search
                            MyApplication.volleyQueueInstance.cancelRequestInQueue(GETPLACESHIT);


                            //build Get url of Place Autocomplete and hit the url to fetch result.
                            request = new VolleyJSONRequest(Request.Method.GET, getPlaceAutoCompleteUrl(edtAddress.getText().toString()), null, null, PickLocationActivity.this, PickLocationActivity.this);

                            //Give a tag to your request so that you can use this tag to cancle request later.
                            request.setTag(GETPLACESHIT);
                           // VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
                            MyApplication.volleyQueueInstance.addToRequestQueue(request);
                        }

                    };

                    // only canceling the network calls will not help, you need to remove all callbacks as well
                    // otherwise the pending callbacks and messages will again invoke the handler and will send the request
                    if (handler != null) {
                        handler.removeCallbacksAndMessages(null);
                    } else {
                        handler = new Handler();
                    }
                    handler.postDelayed(run, 100);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edtAddress.getText().toString().isEmpty() && predictions != null) {
                    progress_bar.setVisibility(View.GONE);
                    mAutoCompleteAdapter.clear();
                    mAutoCompleteAdapter.notifyDataSetChanged();
                    card_place_list.setVisibility(View.GONE);
                }
            }
        });

        //edtAddress.setText(preFilledText);
        //edtAddress.setSelection(edtAddress.getText().length());



        mAutoCompleteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                Log.d("Vijendra", "onItemClick: " + position);
                Log.d("Vijendra", "onItemClick: " + predictions.getPlaces().get(position).getPlaceDesc());
                Log.d("Vijendra", "onItemClick: " + predictions.getPlaces().get(position).getPlaceID());
                Log.d("Vijendra", "onItemClick: " + predictions.getPlaces().get(position).getLat());
                Log.d("Vijendra", "onItemClick: " + predictions.getPlaces().get(position).getLng());

                final String placeId = predictions.getPlaces().get(position).getPlaceID();

                if (!predictions.getPlaces().get(position).getPlaceID().isEmpty()) {

                LatLng latLng =  getLocationFromAddress(PickLocationActivity.this, predictions.getPlaces().get(position).getPlaceDesc());
                    //Log.d(TAG, "onItemClick: "+latLng.latitude);
                    SharedPref.setPrefs(PickLocationActivity.this, IConstant.MyCurrentLatitude, String.valueOf(latLng.latitude));
                    SharedPref.setPrefs(PickLocationActivity.this, IConstant.MyCurrentLongitude, String.valueOf(latLng.longitude));

                    Log.d("Vijendra", "placenotempty " +
                            predictions.getPlaces().get(position).getPlaceID());

                    final List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

                    // Specify the fields to return.
                    // List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

                    // Construct a request object, passing the place ID and fields array.
                    final FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);


                    placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                        @Override
                        public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                            Place place = fetchPlaceResponse.getPlace();

                            if(place.getLatLng() != null)
                                Log.d(TAG, "onSuccess: "+place.getLatLng().latitude);

                            Intent intent = new Intent();
                            intent.putExtra(IConstant.LOCATION_ADDRESS, predictions.getPlaces().get(position).getPlaceDesc());
                            intent.putExtra(IConstant.LAT, latLng.latitude);
                            intent.putExtra(IConstant.LNG, latLng.longitude);
                            intent.putExtra(IConstant.LOCATION_ADDRESS_FLAG, "0");
                            intent.putExtra(IConstant.LOCATION_ADDRESS_REAL, predictions.getPlaces().get(position).getPlaceDesc());
                            SharedPref.setPrefs(PickLocationActivity.this, IConstant.LocationToSetOnDashboard, place.getName());
                            setResult(RESULT_OK, intent);
                            finish();
                            Log.i(TAG, "Placefound: getName" + place.getName());
                            Log.i(TAG, "Placefound: getLat" + predictions.getPlaces().get(position).getLat().toString());
                            Log.i(TAG, "Placefound: getLat" + predictions.getPlaces().get(position).getLat().toString());
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Toast.makeText(PickLocationActivity.this, "Exception occurs", Toast.LENGTH_SHORT).show();
                                    if (exception instanceof ApiException) {
                                        ApiException apiException = (ApiException) exception;
                                        int statusCode = apiException.getStatusCode();
                                        // Handle error with given status code.
                                        Log.e("Vijendra", "Place not found: " + exception.getMessage());
                                    }
                                }
                            });

                } else {
                    Toast.makeText(PickLocationActivity.this, "Empty place id", Toast.LENGTH_SHORT).show();
                }

                //Log.v("ltnlng is", "" + queriedLocation.longitude);
                // pass the result to the calling activity

            }
        });

        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtAddress.setText("");
            }
        });

    }


    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address = null;
        LatLng p1 = null;

        // May throw an IOException
        try {
            address = coder.getFromLocationName(strAddress, 5);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Address location = address.get(0);
        p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        return p1;
    }


    /*
     * Create a get url to fetch results from google place autocomplete api.
     * Append the input received from autocomplete edittext
     * Append your current location
     * Append radius you want to search results within
     * Choose a language you want to fetch data in
     * Append your google API Browser key
     */


    public String getPlaceAutoCompleteUrl(String input) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/place/autocomplete/json");
        urlString.append("?input=");
        try {
            urlString.append(URLEncoder.encode(input, "utf8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        urlString.append("&location=");
        urlString.append(latitude + "," + longitude); // append lat long of current location to show nearby results.
        urlString.append("&radius=5000&language=en");
        urlString.append("&key=" + getResources().getString(R.string.google_maps_key));
        //urlString.append("&components=country:in");
        //urlString.append("&components=administrative_area_level_3:Nashik");

        //sb.append("&components=administrative_area_level_3:bengaluru");
        //sb.append("&components=locality:redmond");

        Log.d("FINAL URL:::   ", urlString.toString());
        return urlString.toString();
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        //searchBtn.setVisibility(View.VISIBLE);

    }

    @Override
    public void onResponse(String response) {

        progress_bar.setVisibility(View.GONE);
        iv_cancel.setVisibility(View.VISIBLE);

        Log.d("PLACES RESULT:::", response);
       /* Gson gson = new Gson();
        predictions = gson.fromJson(response, PlacePredictions.class);*/

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray =jsonObject.getJSONArray(TAG_RESULT);
            ArrayList<PlaceAutoComplete> prediction=new ArrayList<>();

            if (jsonArray.length()>0)
                card_place_list.setVisibility(View.VISIBLE);

            for (int i = 0; i < (jsonArray.length()); i++) {
                PlaceAutoComplete placeAutoComplete = new PlaceAutoComplete();
                JSONObject c = jsonArray.getJSONObject(i);
                placeAutoComplete.setPlaceDesc(c.getString("description"));
                placeAutoComplete.setPlaceID(c.getString("place_id"));

                placeAutoComplete.setId("");
                placeAutoComplete.setLocation_name("");
                placeAutoComplete.setAddress("");
                placeAutoComplete.setType("");
                placeAutoComplete.setLat("");
                placeAutoComplete.setLng("");

                prediction.add(placeAutoComplete);
            }
            predictions.setPlaces(prediction);

            if (mAutoCompleteAdapter == null) {
                mAutoCompleteAdapter = new AutoCompleteAdapter(this, predictions.getPlaces(), PickLocationActivity.this);
                mAutoCompleteList.setAdapter(mAutoCompleteAdapter);
            } else {
                mAutoCompleteAdapter.clear();
                mAutoCompleteAdapter.addAll(predictions.getPlaces());
                mAutoCompleteAdapter.notifyDataSetChanged();
                mAutoCompleteList.invalidate();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
               // .addApi(Places.GEO_DATA_API)
              //  .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLastLocation != null) {
                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();
            }

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void fetchLocation() {
        //Build google API client to use fused location
        buildGoogleApiClient();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOC: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission granted!
                    fetchLocation();

                } else {
                    // permission denied!

                    Toast.makeText(this, "Please grant permission for using this app!", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(Build.VERSION.SDK) > 5 && keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            //overridePendingTransition(R.animator.left_right, R.animator.right_left);
            //Toast.makeText(this, "Please Pick Location", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

