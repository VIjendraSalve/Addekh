package com.wht.addekho.Activties;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wht.addekho.Adapter.HomeCategoryAdapter;
import com.wht.addekho.Adapter.ProductListAdapter;
import com.wht.addekho.BaseActivity;
import com.wht.addekho.Constant.IConstant;
import com.wht.addekho.Constant.IUrls;
import com.wht.addekho.Constant.Interface;
import com.wht.addekho.Helper.ConnectionDetector;
import com.wht.addekho.Helper.Helper_Method;
import com.wht.addekho.Helper.RecyclerItemClickListener;
import com.wht.addekho.Helper.SharedPref;
import com.wht.addekho.Model.CategorySearchObject;
import com.wht.addekho.Model.ProductListObject;
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

public class ProductListActivity extends BaseActivity {

    public static Activity _act;
    private ConnectionDetector connectionDetector;
    private String setLang;
    private TextView toolbar_title;

    //Product List
    private ArrayList<ProductListObject> productListObjectArrayList;
    private RelativeLayout progress, noInternet;
    private RecyclerView rvProduct;
    private ProductListAdapter productListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private String items_image_path, strCategoryId = null, strCategoryName = null, strSubCategoryId = null, strSubCategory = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
//
//        strCategoryId = getIntent().getStringExtra("id");
//        strCategoryName = getIntent().getStringExtra("name");


        _act = ProductListActivity.this;
        connectionDetector = ConnectionDetector.getInstance(_act);
        Helper_Method.hideSoftInput(_act);


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("");
//        setSupportActionBar(toolbar);
//        toolbar.setTitleTextColor(getApplicationContext().getResources().getColor(R.color.white));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
//        toolbar_title.setTextColor(getResources().getColor(R.color.white));
//        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//        toolbar_title.setText(strCategoryName + " / " + strSubCategory);
//
//        productListObjectArrayList = new ArrayList<>();
//        productListAdapter = new ProductListAdapter(_act, productListObjectArrayList);
//        progress = (RelativeLayout) findViewById(R.id.progress);
//        noInternet = (RelativeLayout) findViewById(R.id.noInternet);
////        rvProduct = (RecyclerView) findViewById(R.id.rvProduct);
//        progress.setVisibility(View.GONE);

//        webCallProductList();
    }

//    private void webCallProductList() {
//        progress.setVisibility(View.VISIBLE);
//        noInternet.setVisibility(View.INVISIBLE);
//        rvProduct.setVisibility(View.INVISIBLE);
//        Interface api = IUrls.getRetrofit(IUrls.BASE_URL).create(Interface.class);
//        Call<ResponseBody> result = api.POSTAllProductList(String.valueOf(SharedPref.getPrefs(_act, IConstant.USER_ID)), "", strSubCategoryId, SharedPref.getPrefs(_act, IConstant.CITY_ID), "", "1", setLang);
//        result.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                String output = "";
//                try {
//                    output = response.body().string();
//                    productListObjectArrayList.clear();
//                    Log.d("my_tag", "onResponseSachin: " + output);
//                    try {
//
//                        JSONObject jsonObject = new JSONObject(output);
//                        String stringCode = jsonObject.getString(IConstant.RESPONSE_CODE);
//                        String stringMsg = jsonObject.getString(IConstant.RESPONSE_MESSAGE);
//
//
//                        if (stringCode.equalsIgnoreCase(IConstant.RESPONSE_SUCCESS)) {
//
//
//                            items_image_path = jsonObject.getString("items_image_path");
//
//                            JSONArray jsonArrayCategory = jsonObject.getJSONArray("items_list");
//                            for (int index = 0; index < jsonArrayCategory.length(); index++) {
//                                try {
//                                    // productListObjectArrayList.add(parseObjectCategory(jsonArrayCategory.getJSONObject(index), items_image_path));
//                                    productListObjectArrayList.add(new ProductListObject(jsonArrayCategory.getJSONObject(index), items_image_path));
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//
//                                    progress.setVisibility(View.INVISIBLE);
//                                    noInternet.setVisibility(View.VISIBLE);
//                                    rvProduct.setVisibility(View.INVISIBLE);
//                                }
//                            }
//
//                            if (productListObjectArrayList.size() == 0) {
//
//                                progress.setVisibility(View.INVISIBLE);
//                                noInternet.setVisibility(View.VISIBLE);
//                                rvProduct.setVisibility(View.INVISIBLE);
//
//                            } else {
//
//                                linearLayoutManager = new LinearLayoutManager(_act);
//                                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
//                                rvProduct.setLayoutManager(linearLayoutManager);
//                                rvProduct.setItemAnimator(new DefaultItemAnimator());
//                                rvProduct.setAdapter(productListAdapter);
//                                productListAdapter.notifyDataSetChanged();
//                                progress.setVisibility(View.GONE);
//                                noInternet.setVisibility(View.GONE);
//                                rvProduct.setVisibility(View.VISIBLE);
//                            }
//
//                        } else {
//                            progress.setVisibility(View.INVISIBLE);
//                            noInternet.setVisibility(View.VISIBLE);
//                            rvProduct.setVisibility(View.INVISIBLE);
//
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        progress.setVisibility(View.INVISIBLE);
//                        noInternet.setVisibility(View.VISIBLE);
//                        rvProduct.setVisibility(View.INVISIBLE);
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    progress.setVisibility(View.INVISIBLE);
//                    noInternet.setVisibility(View.VISIBLE);
//                    rvProduct.setVisibility(View.INVISIBLE);
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Helper_Method.toaster(_act, t.getMessage());
//                Log.d("Error", "onFailure: " + t.getMessage());
//                if (t instanceof NetworkErrorException)
//                    Helper_Method.warnUser(_act, "Network Error", getString(R.string.error_network), true);
//                else if (t instanceof IOException)
//                    Helper_Method.warnUser(_act, "Connection Error", getString(R.string.error_network), true);
//                    //else if (t instanceof ServerError)
//                    //   Helper_Method.warnUser(_act, "Server Error", getString(R.string.error_server), true);
//                else if (t instanceof ConnectException)
//                    Helper_Method.warnUser(_act, "No Connection Error", getString(R.string.error_connection), true);
//                    //else if (t instanceof ConnectException)
//                    //Helper_Method.warnUser(_act, "No Connection Error", getString(R.string.error_connection), true);
//                else if (t instanceof TimeoutException)
//                    Helper_Method.warnUser(_act, "Timeout Error", getString(R.string.error_timeout), true);
//                else if (t instanceof SocketTimeoutException)
//                    Helper_Method.warnUser(_act, "Timeout Error", getString(R.string.error_timeout), true);
//                else
//                    Helper_Method.warnUser(_act, "Unknown Error", getString(R.string.error_something_wrong), true);
//
//            }
//        });
//    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}