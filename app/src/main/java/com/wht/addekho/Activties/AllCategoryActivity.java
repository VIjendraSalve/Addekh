package com.wht.addekho.Activties;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wht.addekho.Adapter.AllCategoryAdapter;
import com.wht.addekho.BaseActivity;
import com.wht.addekho.Constant.IConstant;
import com.wht.addekho.Constant.IUrls;
import com.wht.addekho.Constant.Interface;
import com.wht.addekho.Helper.ConnectionDetector;
import com.wht.addekho.Helper.Helper_Method;
import com.wht.addekho.Helper.SharedPref;
import com.wht.addekho.Helper.Validations;
import com.wht.addekho.Model.AllCategory;
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

public class AllCategoryActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener
{
    private Activity _act;
    private RecyclerView recyclerView;
    private Validations validations;
    private SharedPref sharedPref;
    private ConnectionDetector connectionDetector;
    private AllCategoryAdapter allCategoryAdapter;
    private ArrayList<AllCategory> allCategoryArrayList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    SwipeRefreshLayout swipeRefreshLayout;
    String badge_image,product_image;
    private int page_no = 1;
    public int remainingCount = 0;
    Toolbar toolbar;
    private TextView toolbar_title,txtCount;
    ImageView img_back;

    private RelativeLayout progress, noInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_caegory);

        img_back =findViewById(R.id.img_back);

        _act = AllCategoryActivity.this;
        validations = new Validations();
        connectionDetector = ConnectionDetector.getInstance(_act);
        Helper_Method.hideSoftInput(_act);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(this);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        initviews();
    }

    private void initviews()
    {
        recyclerView = findViewById(R.id.recycler_enquiry);
        progress = (RelativeLayout) findViewById(R.id.progress);
        noInternet = (RelativeLayout) findViewById(R.id.noInternet);



//
//        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (recyclerView.getAdapter().getItemCount() != 0) {
//                    int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
//                    if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition >= recyclerView.getAdapter().getItemCount() - 1 && allCategoryArrayList.size() > 9) {
//                        Log.d("test", "remaining: "+remainingCount);
//                        Log.d("test", "page_no: "+page_no);
//                        if (remainingCount >= 0) {
//                            page_no++;
//                            PostEnquiryAPI();
//                        }
//                    }
//                }
//            }
//
//        });
        PostEnquiryAPI();
    }
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void PostEnquiryAPI() {
        swipeRefreshLayout.setRefreshing(true);
        progress.setVisibility(View.VISIBLE);
        noInternet.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        Interface api = IUrls.getRetrofit(IUrls.BASE_URL).create(Interface.class);
        Call<ResponseBody> result = api.POSTCategoryList(SharedPref.getPrefs(_act, IConstant.USER_ID));
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {


                    output = response.body().string();
                    Log.d("my_tag", "Response: " + output);
                    try {

                        JSONObject i = new JSONObject(output);
                        String stringCode = i.getString("result");

                        product_image = i.getString("category_path");

                        if (stringCode.equalsIgnoreCase("true")) {
                            JSONArray jsonArray = i.getJSONArray("categories");
                            for (int index = 0; index < jsonArray.length(); index++) {
                                try {
                                    //  page_no = 1;
                                    allCategoryArrayList.add(new AllCategory(jsonArray.getJSONObject(index), product_image));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    progress.setVisibility(View.INVISIBLE);
                                    noInternet.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.INVISIBLE);
                                    swipeRefreshLayout.setRefreshing(false);

                                }
                            }

                            if (allCategoryArrayList.size() == 0) {

                                progress.setVisibility(View.INVISIBLE);
                                noInternet.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.INVISIBLE);
                                swipeRefreshLayout.setRefreshing(false);


                            } else {


                                allCategoryAdapter = new AllCategoryAdapter(_act,allCategoryArrayList);
                                linearLayoutManager = new LinearLayoutManager(_act);
                                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                                recyclerView.setLayoutManager(linearLayoutManager);
                                progress.setVisibility(View.INVISIBLE);
                                noInternet.setVisibility(View.INVISIBLE);
                                recyclerView.setVisibility(View.VISIBLE);
                                recyclerView.setAdapter(allCategoryAdapter);
                                swipeRefreshLayout.setRefreshing(false);

                                allCategoryAdapter.notifyDataSetChanged();


                            }

                        } else {


                            progress.setVisibility(View.INVISIBLE);
                            noInternet.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                            swipeRefreshLayout.setRefreshing(false);


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                        progress.setVisibility(View.INVISIBLE);
                        noInternet.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                        swipeRefreshLayout.setRefreshing(false);

                    }

                } catch (IOException e) {
                    e.printStackTrace();

                    progress.setVisibility(View.INVISIBLE);
                    noInternet.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
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
    public void onRefresh()
    {
        allCategoryArrayList.clear();

        PostEnquiryAPI();

    }
}