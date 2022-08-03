package com.wht.addekho.google_search;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.wht.addekho.Constant.IConstant;
import com.wht.addekho.R;
import com.wht.addekho.BaseActivity;
import com.wht.addekho.Helper.CheckNetwork;
import com.wht.addekho.Helper.GPSTracker;

import java.util.List;

public class SearchResultActivity extends BaseActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    protected GoogleApiClient mGoogleApiClient;
    private static LatLngBounds BOUNDS_INDIA = null;

    private EditText mAutocompleteView;
    private RecyclerView mRecyclerView;
    private ListView simpleListView;
    private LinearLayoutManager mLinearLayoutManager;
    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    ImageView delete;
    String city_name = "";
    int type = 0;
    //AdapterLocalSearchList adapterLocalSearchList;
    private String preFilledText = "";

    String cityName[] = {"Mumbai", "Pune", "Nashik", "Nagpur", "Amravati", "Akola", "Beed", "Jalgaon", "Nagar", "Dhule", "Nandurbar"};
    private List<String> cities;
    private GPSTracker gpsTracker;
    private ProgressBar progress_bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        //overridePendingTransition(R.animator.move_left, R.animator.move_right);

        gpsTracker = new GPSTracker(this);

        BOUNDS_INDIA = new LatLngBounds(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getInt("type");

        }

        //  buildGoogleApiClient();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                           // Log.d("Location error ", "" + connectionResult.getErrorCode());
                        }
                    }).build();
            mGoogleApiClient.connect();
        }

        check_connection();

    }

    private void check_connection() {
        if (CheckNetwork.isInternetAvailable(this))  //if connection available
        {
            init();
        } else {
            Toast.makeText(this, "No Internet Available ...", Toast.LENGTH_SHORT).show();
        }
    }

    private void init() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        simpleListView = (ListView) findViewById(R.id.simpleListView);

        mAutocompleteView = (EditText) findViewById(R.id.autocomplete_places);

        delete = (ImageView) findViewById(R.id.iv_cancel);
        //back=(ImageView)findViewById(R.id.iv_back);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);

        googleSearch();
        //mAutocompleteView.setText(""+city_name);
        if (getIntent().hasExtra(IConstant.HINT_TEXT)) {
            preFilledText = getIntent().getStringExtra(IConstant.HINT_TEXT);
            mAutocompleteView.setHint(preFilledText);
        }
        // tempSearch();

    }


    private void googleSearch() {
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_REGIONS)
                .build();

        mAutoCompleteAdapter = new PlacesAutoCompleteAdapter(this,
                R.layout.adapter_searchview,
                mGoogleApiClient, BOUNDS_INDIA, null, mAutocompleteView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAutoCompleteAdapter);
        delete.setOnClickListener(this);
        //back.setOnClickListener(this);



        /*mAutocompleteView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // optimised way is to start searching for laction after user has typed minimum 3 chars
                if (mAutocompleteView.getText().length() > 0) {

                    mAutoCompleteAdapter.getFilter().filter(s.toString());
                    mRecyclerView.setVisibility(View.VISIBLE);

                    progress_bar.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mAutocompleteView.getText().toString().isEmpty() && mAutoCompleteAdapter.getItemCount()!=0) {
                    progress_bar.setVisibility(View.GONE);
                    mAutoCompleteAdapter.clear();
                    mAutoCompleteAdapter.notifyDataSetChanged();
                }else  delete.setVisibility(View.VISIBLE);
            }
        });*/

        mAutocompleteView.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
                    mAutoCompleteAdapter.getFilter().filter(s.toString());
                    mRecyclerView.setVisibility(View.VISIBLE);
                    //progress_bar.setVisibility(View.VISIBLE);
                    //delete.setVisibility(View.GONE);
                } else if (!mGoogleApiClient.isConnected()) {
                    //    Toast.makeText(getApplicationContext(), Constants.API_NOT_CONNECTED,Toast.LENGTH_SHORT).show();
                    //Log.d(Constants.PlacesTag, Constants.API_NOT_CONNECTED);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (s.toString().equals("") && mAutoCompleteAdapter.getItemCount() != 0) {
                    progress_bar.setVisibility(View.GONE);
                    mAutoCompleteAdapter.clear();
                    mAutoCompleteAdapter.notifyDataSetChanged();
                } else delete.setVisibility(View.VISIBLE);
            }
        });


        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        final PlacesAutoCompleteAdapter.PlaceAutocomplete item = mAutoCompleteAdapter.getItem(position);
                        final String placeId = String.valueOf(item.placeId);
                        Log.i("TAG", "Autocomplete item selected: " + item.description);
                        /*
                             Issue a request to the Places Geo Data API to retrieve a Place object with additional details about the place.
                         */

                        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                                .getPlaceById(mGoogleApiClient, placeId);
                        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(PlaceBuffer places) {
                                if (places.getCount() == 1) {
                                    //Do the things here on Click.....
                                    // Toast.makeText(getApplicationContext(),String.valueOf(places.get(0).getLatLng()),Toast.LENGTH_SHORT).show();
                                    onPlaceSelected(places.get(0), item);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        //mAutocompleteView.setText(item.description);

                        Log.i("TAG", "Clicked: " + item.description);
                        Log.i("TAG", "Called getPlaceById to get Place details for " + item.placeId);
                    }
                })
        );
    }

    private void onPlaceSelected(Place place, PlacesAutoCompleteAdapter.PlaceAutocomplete item) {
        //String city=""+place.getAddress();
        // Log.d("my tag", "onPlaceSelected: "+place.getAddress());
        Intent intent = new Intent();
        intent.putExtra(IConstant.LOCATION_ADDRESS, item.description);
        intent.putExtra(IConstant.LAT, place.getLatLng().latitude);
        intent.putExtra(IConstant.LNG, place.getLatLng().longitude);
        setResult(RESULT_OK, intent);
        finish();
        /*String temp_array []=   city.split(",");
        Bundle bundle=new Bundle();
        bundle.putString(Constants.CITY_NAME,temp_array[0]);
        city_name = ""+temp_array[0];
        if (type==HOTEL) {
            setResult(RESULT_OK,getIntent().putExtras(bundle));
        }
        else if (type==ROOM)
            startActivity(new Intent(SearchResultActivity.this, RoomListActivity.class).putExtras(bundle));*/

    }

    public void onClick(View view) {
        if (view == delete) {
            mAutocompleteView.setText("");
            mRecyclerView.setVisibility(View.GONE);
        }
        /*if(view==back){
            onBackPressed();
        }
*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        googleSearch();
        Log.v("Google API Callback", "Connection Done");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v("Google API Callback", "Connection Suspended");
        Log.v("Code", String.valueOf(i));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v("Google API Callback", "Connection Failed");
        Log.v("Error Code", String.valueOf(connectionResult.getErrorCode()));
        Toast.makeText(this, "API Not Connected", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
            Log.v("Google API", "Connecting");
            mGoogleApiClient.connect();
        }
        mAutocompleteView.setText("");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            Log.v("Google API", "Dis-Connecting");
            mGoogleApiClient.disconnect();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                //overridePendingTransition(R.animator.left_right, R.animator.right_left);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            //overridePendingTransition(R.animator.left_right, R.animator.right_left);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /*@Override
    public void onCitySelect(String city) {
        mAutocompleteView.setText(""+city);
    }*/
}
