package com.wht.addekho.Fragments;

import static android.view.View.GONE;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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


public class BagFragment extends Fragment  implements SwipeRefreshLayout.OnRefreshListener {

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

    private LinearLayout favorite, noConnectionLayout;
    private ProgressBar progressView, progressBar_endless;
    private Button btnDiscover_ads;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bag, container, false);

        _act = getActivity();

        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        recycler_mystore = view.findViewById(R.id.recycler_mystorelist);
        favorite = (LinearLayout) view.findViewById(R.id.noRecordLayout);
        noConnectionLayout = (LinearLayout) view.findViewById(R.id.noConnectionLayout);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        // swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(this);
        btnDiscover_ads = view.findViewById(R.id.btnDiscover_ads);
        btnDiscover_ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentstore = new Intent(_act, ActivityAddStore.class);
                intentstore.putExtra("flag", "0");
                startActivity(intentstore);
            }
        });
        progressView = (ProgressBar) view.findViewById(R.id.progress_view);
        progressBar_endless = (ProgressBar) view.findViewById(R.id.progressBar_endless);





        validations = new Validations();
        connectionDetector = ConnectionDetector.getInstance(_act);
        Helper_Method.hideSoftInput(_act);


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

    private void getMyStoreList() {

        Interface api = IUrls.getRetrofit(IUrls.BASE_URL).create(Interface.class);
        Call<ResponseBody> result = api.PostMyStorelist(
                SharedPref.getPrefs(_act, IConstant.USER_ID),
                "",
                "1",
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

                                progressView.setVisibility(GONE);
                                progressBar_endless.setVisibility(GONE);
                                favorite.setVisibility(View.VISIBLE);
                                swipeRefreshLayout.setRefreshing(false);
                                recycler_mystore.setVisibility(View.INVISIBLE);

                            } else {


                                bottomSheetMyStoreListAdapter = new BottomSheetMyStoreListAdapter(_act, bottomsheetMyStoreListObjectArrayList);
                                linearLayoutManager = new LinearLayoutManager(_act);
                                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                                recycler_mystore.setLayoutManager(linearLayoutManager);
                                recycler_mystore.setVisibility(View.VISIBLE);
                                recycler_mystore.setAdapter(bottomSheetMyStoreListAdapter);
                                swipeRefreshLayout.setRefreshing(false);

                                bottomSheetMyStoreListAdapter.notifyDataSetChanged();





                            }

                        } else {


                            progressView.setVisibility(GONE);
                            progressBar_endless.setVisibility(GONE);
                            favorite.setVisibility(View.VISIBLE);
                            recycler_mystore.setVisibility(View.INVISIBLE);
                            swipeRefreshLayout.setRefreshing(false);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                        progressView.setVisibility(GONE);
                        progressBar_endless.setVisibility(GONE);
                        favorite.setVisibility(View.VISIBLE);
                        recycler_mystore.setVisibility(View.INVISIBLE);
                        swipeRefreshLayout.setRefreshing(false);
                    }

                } catch (IOException e) {
                    e.printStackTrace();

                    progressView.setVisibility(GONE);
                    progressBar_endless.setVisibility(GONE);
                    favorite.setVisibility(View.VISIBLE);
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
    public void onRefresh() {
        bottomsheetMyStoreListObjectArrayList.clear();
        page_no = 1;
        getMyStoreList();

    }
}
