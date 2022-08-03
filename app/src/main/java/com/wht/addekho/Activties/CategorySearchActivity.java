package com.wht.addekho.Activties;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.accounts.NetworkErrorException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.wht.addekho.Adapter.AdvertismentListAdapter;
import com.wht.addekho.Adapter.CategoryListAdapter;
import com.wht.addekho.Adapter.HomeCategoryAdapter;
import com.wht.addekho.BaseActivity;
import com.wht.addekho.Constant.IConstant;
import com.wht.addekho.Constant.IUrls;
import com.wht.addekho.Constant.Interface;
import com.wht.addekho.Helper.ConnectionDetector;
import com.wht.addekho.Helper.Helper_Method;
import com.wht.addekho.Helper.RecyclerItemClickListener;
import com.wht.addekho.MainActivity;
import com.wht.addekho.Model.AllCategory;
import com.wht.addekho.Model.CategorySearchObject;
import com.wht.addekho.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeoutException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategorySearchActivity extends BaseActivity implements CategoryListAdapter.OnItemClickListener {

    public static Activity _act;
    private ConnectionDetector connectionDetector;
    private String strUserId = null, strName = null, strEmail = null,
            strMobile = null, strAddress = null, UserType = null, strImage = null;
    private String categoryId = null;
    private EditText etWord;
    private String setLang;


    long delay = 1000; // 1 seconds after user stops typing
    long last_text_edit = 0;
    Handler handler = new Handler();
    private String keyWord = "All";

    //Category List
    private ArrayList<AllCategory> CategorySearchObjectArrayList;
    private RelativeLayout progress, noInternet;
    private RecyclerView rvCategory;
    private CategoryListAdapter categoryListAdapter;
    private GridLayoutManager gridLayoutManager;
    private String category_path = null;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_search);

        _act = CategorySearchActivity.this;
        Window window = _act.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        connectionDetector = ConnectionDetector.getInstance(_act);
        Helper_Method.hideSoftInput(_act);

        etWord = findViewById(R.id.etWord);
        CategorySearchObjectArrayList = new ArrayList<>();
        progress = (RelativeLayout) findViewById(R.id.progress);
        noInternet = (RelativeLayout) findViewById(R.id.noInternet);
        rvCategory = (RecyclerView) findViewById(R.id.rvCategory);
        rvCategory.setHasFixedSize(true);
        rvCategory.setNestedScrollingEnabled(false);
        //categoryListAdapter = new HomeCategoryAdapter(_act, CategorySearchObjectArrayList);
        linearLayoutManager = new LinearLayoutManager(_act);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvCategory.setLayoutManager(linearLayoutManager);


        findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        etWord.addTextChangedListener(new TextWatcher() {
                                          @Override
                                          public void beforeTextChanged(CharSequence s, int start, int count,
                                                                        int after) {
                                          }

                                          @Override
                                          public void onTextChanged(final CharSequence s, int start, int before,
                                                                    int count) {
                                              //You need to remove this to run only once
                                              handler.removeCallbacks(input_finish_checker);

                                          }

                                          @Override
                                          public void afterTextChanged(final Editable s) {
                                              //avoid triggering event when text is empty
                                              if (s.length() > 0) {
                                                  keyWord = String.valueOf(s);
                                                  last_text_edit = System.currentTimeMillis();
                                                  handler.postDelayed(input_finish_checker, delay);
                                              } else {
                                                  keyWord = "";
                                                  webServiceCallProductFirstCall(keyWord);
                                              }
                                          }
                                      }

        );

       /* rvCategory.addOnItemTouchListener(
                new RecyclerItemClickListener(_act, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Bundle bundle = new Bundle();
                        bundle.putString("category_id",CategorySearchObjectArrayList.get(position).getId());


                    *//*Intent intent = new Intent();
                    intent.putExtra("lat", latitude);
                    intent.putExtra("lagn", longitude);
                    setResult(123, intent);
                    finish();*//*

                        // AddAddressDailog(String.valueOf(latitude), String.valueOf(longitude), addresss, city, state);
                        //bundle.putDouble("distance", distance);
                        Intent intent = new Intent();
                        intent.putExtras(bundle);
                        setResult(900, intent);
                        finish();


                    }
                })
        );*/


        //Default Dress Code Webcall
        if (connectionDetector.isConnectionAvailable()) {
            webServiceCallProductFirstCall("");
        } else {
            Helper_Method.toaster_long(_act, getResources().getString(R.string.internet_connection_required));
        }
    }

    private void webServiceCallProductFirstCall(String keyWord) {
        progress.setVisibility(View.VISIBLE);
        noInternet.setVisibility(View.GONE);
        rvCategory.setVisibility(View.GONE);
        Interface api = IUrls.getRetrofit(IUrls.BASE_URL).create(Interface.class);
        Call<ResponseBody> result = api.PostCategoryCategorySearch(keyWord);
        result.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {
                    output = response.body().string();
                    CategorySearchObjectArrayList.clear();
                    Log.d("my_tag", "onResponse: " + output);
                    try {

                        JSONObject jsonObject = new JSONObject(output);
                        String stringCode = jsonObject.getString(IConstant.RESPONSE_CODE);
                        String stringMsg = jsonObject.getString(IConstant.RESPONSE_MESSAGE);

                        if (stringCode.equalsIgnoreCase(IConstant.RESPONSE_SUCCESS)) {

                            category_path = jsonObject.getString("category_path");

                            JSONArray jsonArrayCategory = jsonObject.getJSONArray("category_list");
                            for (int index = 0; index < jsonArrayCategory.length(); index++) {
                                try {
                                    //CategorySearchObjectArrayList.add(AllCategory(jsonArrayCategory.getJSONObject(index), category_path));
                                    CategorySearchObjectArrayList.add(new AllCategory(jsonArrayCategory.getJSONObject(index), category_path));


                                } catch (JSONException e) {
                                    e.printStackTrace();

                                    progress.setVisibility(View.GONE);
                                    noInternet.setVisibility(View.VISIBLE);
                                    rvCategory.setVisibility(View.GONE);
                                }
                            }

                            if (CategorySearchObjectArrayList.size() == 0) {

                                progress.setVisibility(View.GONE);
                                noInternet.setVisibility(View.VISIBLE);
                                rvCategory.setVisibility(View.GONE);
                            } else {

                                /*categoryListAdapter = new CategoryListAdapter(_act, CategorySearchObjectArrayList, new HomeCategoryAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(String id) {

                                    }
                                });*/
                                Log.d("CategorySearchActivity", "onResponse: 1");
                                categoryListAdapter = new CategoryListAdapter(
                                        _act,
                                        CategorySearchObjectArrayList,
                                        CategorySearchActivity.this);

                                linearLayoutManager = new LinearLayoutManager(_act);
                                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                                rvCategory.setLayoutManager(linearLayoutManager);
                                rvCategory.setAdapter(categoryListAdapter);
                                categoryListAdapter.notifyDataSetChanged();
                                Log.d("CategorySearchActivity", "onResponse: 2");
                                progress.setVisibility(View.INVISIBLE);
                                noInternet.setVisibility(View.INVISIBLE);
                                rvCategory.setVisibility(View.VISIBLE);
                            }

                        } else {
                            Helper_Method.toaster(_act, stringMsg);
                            progress.setVisibility(View.GONE);
                            noInternet.setVisibility(View.VISIBLE);
                            rvCategory.setVisibility(View.GONE);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progress.setVisibility(View.GONE);
                        noInternet.setVisibility(View.VISIBLE);
                        rvCategory.setVisibility(View.GONE);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    progress.setVisibility(View.GONE);
                    noInternet.setVisibility(View.VISIBLE);
                    rvCategory.setVisibility(View.GONE);

                } /*finally {
                    webServiceCartCount();
                }*/
                Log.d("CategorySearchActivity", "onResponse: 3");
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

    public CategorySearchObject parseObjectCategory(JSONObject object, String category_path) {
        CategorySearchObject CategorySearchObject = new CategorySearchObject();
        try {
            CategorySearchObject.id = object.getString("id");
//            CategorySearchObject.category_id = object.getString("category_id");
            CategorySearchObject.name = object.getString("name");
            CategorySearchObject.image = category_path + object.getString("image");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return CategorySearchObject;
    }


    private ArrayList<CategorySearchObject> sortBannerFunction(ArrayList<CategorySearchObject> homeBannerObjArrayList) {

        Collections.sort(homeBannerObjArrayList, new Comparator<CategorySearchObject>() {
            @Override
            public int compare(CategorySearchObject o1, CategorySearchObject o2) {
                return o1.id.compareTo(o2.id);
            }
        });
        return homeBannerObjArrayList;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        _act.overridePendingTransition(R.anim.activity_stay, R.anim.activity_slide_to_bottom);
        //finish();
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

/*    public static class Utility {
        public static int calculateNoOfColumns(Context context, float columnWidthDp) { // For example columnWidthdp=180
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
            int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
            return noOfColumns;
        }
    }*/

    public int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }

    public void removePhoneKeypad() {
        if (_act.getCurrentFocus() != null && _act.getCurrentFocus().getWindowToken() != null) {
            System.out.println("getCurrentFocus() in frag");
            InputMethodManager inputManager = (InputMethodManager) _act.getSystemService(Context.INPUT_METHOD_SERVICE);

            IBinder binder = _act.getCurrentFocus().getWindowToken();
            inputManager.hideSoftInputFromWindow(binder,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
        _act.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    private Runnable input_finish_checker = new Runnable() {
        public void run() {
            if (System.currentTimeMillis() > (last_text_edit + delay - 600)) {
                // TODO: do what you need here
                // ............
                // ............
                webServiceCallProductFirstCall(keyWord);
                //Helper_Method.toaster(_act, "Webservice Call" + keyWord);
                //removePhoneKeypad();
            }
        }
    };

    @Override
    public void onItemClick(int id) {
        Bundle bundle = new Bundle();
        //int position = Integer.parseInt(id);
        bundle.putString("category_id",CategorySearchObjectArrayList.get(id).getId());
        bundle.putString("category_name",CategorySearchObjectArrayList.get(id).getName());

        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(900, intent);
        finish();
    }
}