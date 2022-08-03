package com.wht.addekho.Fragments;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wht.addekho.Activties.ActivityAddStore;
import com.wht.addekho.Adapter.BottomSheetMyStoreListAdapter;
import com.wht.addekho.Constant.IConstant;
import com.wht.addekho.Constant.IUrls;
import com.wht.addekho.Constant.Interface;
import com.wht.addekho.Helper.ConnectionDetector;
import com.wht.addekho.Helper.Helper_Method;
import com.wht.addekho.Helper.SharedPref;
import com.wht.addekho.Helper.Validations;
import com.wht.addekho.Model.BottomsheetMyStoreListObject;
import com.wht.addekho.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener , OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    public static final String TAG = "EmailOrdersDialogFrag";
    public static Context context;
    public int remaining = 0;
    FloatingActionButton floatingActionButton;
    String badge_image, blog_image;
    SwipeRefreshLayout swipeRefreshLayout;
    Toolbar toolbar;
    String blog_category, blog_title, author, description, blog_id, strcatName;
    private Button okBt;
    private int pos;
    private Activity _act;
    private RecyclerView recycler_mystore;
    private Validations validations;
    private SharedPref sharedPref;
    private ConnectionDetector connectionDetector;
    private BottomSheetMyStoreListAdapter bottomSheetMyStoreListAdapter;
    private ArrayList<BottomsheetMyStoreListObject> bottomsheetMyStoreListObjectArrayList;
    private LinearLayoutManager linearLayoutManager;
    private RelativeLayout progress, noInternet;
    private int page_no = 1;
    private String image_path;
    private TextView toolbar_title, txtCount, tvCategoryName;

    GoogleMap Map;
    MarkerOptions markerOptions;
    LatLng latLng;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);
        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        recycler_mystore = view.findViewById(R.id.recycler_mystorelist);
        progress = view.findViewById(R.id.progress);
        noInternet = view.findViewById(R.id.noInternet);
//        tvCategoryName =view.findViewById(R.id.tvCategoryName);
        _act = getActivity();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);

        validations = new Validations();
        connectionDetector = ConnectionDetector.getInstance(_act);
        Helper_Method.hideSoftInput(_act);
        swipeRefreshLayout.setOnRefreshListener(this);

        SupportMapFragment supportMapFragment=(SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        buildGoogleApiClient();
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Map = googleMap;
                mGoogleApiClient.connect();
            }
        });

        // Async map
      /*  supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                // When map is loaded
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        // When clicked on map
                        // Initialize marker options
                        MarkerOptions markerOptions=new MarkerOptions();
                        // Set position of marker
                        markerOptions.position(latLng);
                        // Set title of marker
                        markerOptions.title(latLng.latitude+" : "+latLng.longitude);
                        // Remove all marker
                        googleMap.clear();
                        // Animating to zoom the marker
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                        // Add marker on map
                        googleMap.addMarker(markerOptions);
                    }
                });
            }
        });*/


        bottomsheetMyStoreListObjectArrayList = new ArrayList<>();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityAddStore.class);
                intent.putExtra("flag", "1");
                //intent.putExtra("pos", pos);
                // intent.putParcelableArrayListExtra("list", bottomsheetMyStoreListObjectArrayList);
                startActivity(intent);
            }
        });

        recycler_mystore.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getAdapter().getItemCount() != 0) {
                    int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                    if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition >= recyclerView.getAdapter().getItemCount() - 1 && bottomsheetMyStoreListObjectArrayList.size() > 9) {
                        Log.d("test", "remaining: " + remaining);
                        Log.d("test", "page_no: " + page_no);
                        if (remaining > 0) {
                            page_no++;
                            getMyStoreList();
                        }
                    }
                }
            }

        });
        getMyStoreList();

        return view;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        System.out.println("ABC buildGoogleApiClient map was invoked: ");
    }


    private void getMyStoreList() {
        swipeRefreshLayout.setRefreshing(true);
        progress.setVisibility(View.VISIBLE);
        noInternet.setVisibility(View.INVISIBLE);
        recycler_mystore.setVisibility(View.INVISIBLE);
        Interface api = IUrls.getRetrofit(IUrls.BASE_URL).create(Interface.class);
        Call<ResponseBody> result = api.PostMyStorelist(
                SharedPref.getPrefs(_act, IConstant.USER_ID),
                "",
                "0",
                page_no);
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
                        image_path = i.getString("stores_path");
                        remaining = i.getInt("remaining");

                        if (stringCode.equalsIgnoreCase("true")) {
                            JSONArray jsonArray = i.getJSONArray("my_stores");
                            for (int index = 0; index < jsonArray.length(); index++) {
                                try {


                                    // page_no = 1;
                                    bottomsheetMyStoreListObjectArrayList.add(new BottomsheetMyStoreListObject(jsonArray.getJSONObject(index), image_path));


                                } catch (JSONException e) {
                                    e.printStackTrace();

                                    progress.setVisibility(View.INVISIBLE);
                                    noInternet.setVisibility(View.VISIBLE);
                                    recycler_mystore.setVisibility(View.INVISIBLE);
                                    swipeRefreshLayout.setRefreshing(false);

                                }
                            }




                            if (bottomsheetMyStoreListObjectArrayList.size() == 0) {

                                progress.setVisibility(View.INVISIBLE);
                                noInternet.setVisibility(View.VISIBLE);
                                swipeRefreshLayout.setRefreshing(false);
                                recycler_mystore.setVisibility(View.INVISIBLE);

                            } else {


                                bottomSheetMyStoreListAdapter = new BottomSheetMyStoreListAdapter(_act, bottomsheetMyStoreListObjectArrayList);
                                linearLayoutManager = new LinearLayoutManager(_act);
                                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                                recycler_mystore.setLayoutManager(linearLayoutManager);
                                progress.setVisibility(View.INVISIBLE);
                                noInternet.setVisibility(View.INVISIBLE);
                                recycler_mystore.setVisibility(View.VISIBLE);
                                recycler_mystore.setAdapter(bottomSheetMyStoreListAdapter);
                                swipeRefreshLayout.setRefreshing(false);

                                bottomSheetMyStoreListAdapter.notifyDataSetChanged();

                                LatLngBounds.Builder b = new LatLngBounds.Builder();
                                for(int j = 0 ; j < bottomsheetMyStoreListObjectArrayList.size() ; j++) {


                                    if(bottomsheetMyStoreListObjectArrayList.get(j).getStore_lat() != null &&
                                            bottomsheetMyStoreListObjectArrayList.get(j).getStore_lat() != null) {

                                        Log.d(TAG, "onResponse: "+bottomsheetMyStoreListObjectArrayList.get(j).getStore_lat());
                                        Log.d(TAG, "onResponse: "+bottomsheetMyStoreListObjectArrayList.get(j).getStore_lng());
                                        b.include(createMarker(
                                                Double.parseDouble(bottomsheetMyStoreListObjectArrayList.get(j).getStore_lat()),
                                                Double.parseDouble(bottomsheetMyStoreListObjectArrayList.get(j).getStore_lng()),
                                                bottomsheetMyStoreListObjectArrayList.get(j).getStore_name(),
                                                bottomsheetMyStoreListObjectArrayList.get(j).getStore_address()).getPosition());
                                    }
                                }


                                int width, height, padding;
                                LatLngBounds bounds = b.build();
                                width = getResources().getDisplayMetrics().widthPixels;
                                height = getResources().getDisplayMetrics().heightPixels;
                                padding = (int) (width * 0.35); // offset from edges of the map - 20% of screen
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                                // CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 25,25,2);
                                Map.animateCamera(cameraUpdate);


                            }

                        } else {


                            progress.setVisibility(View.INVISIBLE);
                            noInternet.setVisibility(View.VISIBLE);
                            recycler_mystore.setVisibility(View.INVISIBLE);
                            swipeRefreshLayout.setRefreshing(false);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                        progress.setVisibility(View.INVISIBLE);
                        noInternet.setVisibility(View.VISIBLE);
                        recycler_mystore.setVisibility(View.INVISIBLE);
                        swipeRefreshLayout.setRefreshing(false);
                    }

                } catch (IOException e) {
                    e.printStackTrace();

                    progress.setVisibility(View.INVISIBLE);
                    noInternet.setVisibility(View.VISIBLE);
                    recycler_mystore.setVisibility(View.INVISIBLE);
                    swipeRefreshLayout.setRefreshing(false);

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

    protected Marker createMarker(double latitude, double longitude, String title, String snippet) {

        return Map.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.defaultMarker()));
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.Map = googleMap;
        //getMyStoreList();

    }
    @Override
    public void onRefresh() {
        bottomsheetMyStoreListObjectArrayList.clear();
        page_no = 1;
        getMyStoreList();
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
}