package com.wht.addekho.Activties;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class MyStoreActivity extends AppCompatActivity implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener{

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
   // private RelativeLayout progress, noInternet;
    private int page_no = 1;
    private String image_path;
    private TextView toolbar_title, txtCount, tvCategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_store);
        //floatingActionButton = findViewById(R.id.floatingActionButton);
        recycler_mystore = findViewById(R.id.recyclerView_myStore);
      // progress = findViewById(R.id.progress);
       // noInternet = findViewById(R.id.noInternet);
//        tvCategoryName =view.findViewById(R.id.tvCategoryName);
        _act = MyStoreActivity.this;
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);

        validations = new Validations();
        connectionDetector = ConnectionDetector.getInstance(_act);
        Helper_Method.hideSoftInput(_act);
        swipeRefreshLayout.setOnRefreshListener(this);

        bottomsheetMyStoreListObjectArrayList = new ArrayList<>();


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
                            getMyStore();
                        }
                    }
                }
            }

        });
        getMyStore();


    }
    private void getMyStore() {
        swipeRefreshLayout.setRefreshing(true);
      //  progress.setVisibility(View.VISIBLE);
       // noInternet.setVisibility(View.INVISIBLE);
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

                                 //   progress.setVisibility(View.INVISIBLE);
                                 //   noInternet.setVisibility(View.VISIBLE);
                                    recycler_mystore.setVisibility(View.INVISIBLE);
                                    swipeRefreshLayout.setRefreshing(false);

                                }
                            }




                            if (bottomsheetMyStoreListObjectArrayList.size() == 0) {

                               /// progress.setVisibility(View.INVISIBLE);
                               // noInternet.setVisibility(View.VISIBLE);
                                swipeRefreshLayout.setRefreshing(false);
                                recycler_mystore.setVisibility(View.INVISIBLE);

                            } else {


                                bottomSheetMyStoreListAdapter = new BottomSheetMyStoreListAdapter(_act, bottomsheetMyStoreListObjectArrayList);
                                linearLayoutManager = new LinearLayoutManager(_act);
                                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                                recycler_mystore.setLayoutManager(linearLayoutManager);
                              //  progress.setVisibility(View.INVISIBLE);
                              //  noInternet.setVisibility(View.INVISIBLE);
                                recycler_mystore.setVisibility(View.VISIBLE);
                                recycler_mystore.setAdapter(bottomSheetMyStoreListAdapter);
                                swipeRefreshLayout.setRefreshing(false);

                                bottomSheetMyStoreListAdapter.notifyDataSetChanged();






                            }

                        } else {


                            //progress.setVisibility(View.INVISIBLE);
                          //  noInternet.setVisibility(View.VISIBLE);
                            recycler_mystore.setVisibility(View.INVISIBLE);
                            swipeRefreshLayout.setRefreshing(false);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                       // progress.setVisibility(View.INVISIBLE);
                       // noInternet.setVisibility(View.VISIBLE);
                        recycler_mystore.setVisibility(View.INVISIBLE);
                        swipeRefreshLayout.setRefreshing(false);
                    }

                } catch (IOException e) {
                    e.printStackTrace();

                   // progress.setVisibility(View.INVISIBLE);
                   // noInternet.setVisibility(View.VISIBLE);
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


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefresh() {
        bottomsheetMyStoreListObjectArrayList.clear();
        page_no = 1;
        getMyStore();

    }


}