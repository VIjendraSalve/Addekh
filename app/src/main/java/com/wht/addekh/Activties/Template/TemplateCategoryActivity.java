package com.wht.addekh.Activties.Template;

import android.accounts.NetworkErrorException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wht.addekh.Adapter.AdminTemplateListAdapter;
import com.wht.addekh.Adapter.TemplateCategoryListAdapter;
import com.wht.addekh.BaseActivity;
import com.wht.addekh.Constant.IConstant;
import com.wht.addekh.Constant.IUrls;
import com.wht.addekh.Constant.Interface;
import com.wht.addekh.Helper.ConnectionDetector;
import com.wht.addekh.Helper.Helper_Method;
import com.wht.addekh.Helper.SharedPref;
import com.wht.addekh.Model.CategorySearchObject;
import com.wht.addekh.Model.TemplateCategory;
import com.wht.addekh.Model.TemplateData;
import com.wht.addekh.R;

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

public class TemplateCategoryActivity extends BaseActivity implements
        TemplateCategoryListAdapter.OnItemClickListener, AdminTemplateListAdapter.OnItemClickListener {

    public static Activity _act;
    long delay = 1000; // 1 seconds after user stops typing
    long last_text_edit = 0;
    Handler handler = new Handler();
    private ConnectionDetector connectionDetector;
    private String strUserId = null, strName = null, strEmail = null,
            strMobile = null, strAddress = null, UserType = null, strImage = null;
    private String categoryId = null;
    private EditText etWord;
    private String setLang;
    private ArrayList<TemplateData> templateDataArrayList = new ArrayList<>();
    private AdminTemplateListAdapter adminTemplateListAdapter;
    private RecyclerView rvTemplate;
    private String image_path = "";
    private String keyWord = "All";

    //Category List
    private ArrayList<TemplateCategory> CategorySearchObjectArrayList;
    private RelativeLayout progress, noInternet;
    private RecyclerView rvCategory;
    private TemplateCategoryListAdapter categoryListAdapter;
    private GridLayoutManager gridLayoutManager;
    private String category_path = null;
    private LinearLayoutManager linearLayoutManager;

    private ProgressDialog progressDialog;
    private String BusinessName = "", Email = "", Mobile = "";
    private Runnable input_finish_checker = new Runnable() {
        public void run() {
            if (System.currentTimeMillis() > (last_text_edit + delay - 600)) {
                // TODO: do what you need here
                // ............
                // ............
                webServiceCallTemplateCategoryList(keyWord);
                //Helper_Method.toaster(_act, "Webservice Call" + keyWord);
                //removePhoneKeypad();
            }
        }
    };
    private String action = "", category_id = "", advt_title = "", advt_desc = "", tags = "", last_date = "", store_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_category);

        BusinessName = getIntent().getStringExtra("BusinessName");
        Email = getIntent().getStringExtra("Email");
        Mobile = getIntent().getStringExtra("Mobile");

        action = getIntent().getStringExtra("action");
        category_id = getIntent().getStringExtra("category_id");
        advt_title = getIntent().getStringExtra("advt_title");
        advt_desc = getIntent().getStringExtra("advt_desc");
        tags = getIntent().getStringExtra("tags");
        last_date = getIntent().getStringExtra("last_date");
        store_id = getIntent().getStringExtra("store_id");

        _act = TemplateCategoryActivity.this;
       /* Window window = _act.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);*/

        connectionDetector = ConnectionDetector.getInstance(_act);
        Helper_Method.hideSoftInput(_act);

        progressDialog = new ProgressDialog(TemplateCategoryActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(true);

        CategorySearchObjectArrayList = new ArrayList<>();
        progress = (RelativeLayout) findViewById(R.id.progress);
        noInternet = (RelativeLayout) findViewById(R.id.noInternet);
        rvCategory = (RecyclerView) findViewById(R.id.rvCategory);
        rvTemplate = (RecyclerView) findViewById(R.id.recycler_template);
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
            webServiceCallTemplateCategoryList("");
        } else {
            Helper_Method.toaster_long(_act, getResources().getString(R.string.internet_connection_required));
        }

        getTemplate("");
    }

    private void getTemplate(String categoryID) {
        progress.setVisibility(View.VISIBLE);
        noInternet.setVisibility(View.GONE);
        rvCategory.setVisibility(View.GONE);

        templateDataArrayList.clear();
        Interface api = IUrls.getRetrofit(IUrls.BASE_URL).create(Interface.class);
        Call<ResponseBody> result = api.PostAdminTemplate(
                SharedPref.getPrefs(TemplateCategoryActivity.this, IConstant.USER_ID),
                categoryID
        );
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {
                    output = response.body().string();
                    Log.d("my_tag3333", "onResponse11: " + output);
                    JSONObject jsonObject = new JSONObject(output);
                    boolean res = jsonObject.getBoolean("result");

                    // Log.d(TAG, "onResponse: " + object.getString("Message"));
                    if (res) {
                       /* remainingCount = Integer.parseInt(jsonObject.getString("remaining"));
                        Log.d(TAG, "onResponse: " + remainingCount);*/
                        image_path = jsonObject.getString("template_path");

                        JSONArray jsonArray = jsonObject.getJSONArray("offer_tamplates");
                        // Parsing json
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                templateDataArrayList.add(new TemplateData(obj, image_path));
                                Log.d("Vijendra", "onResponse: " + obj.getJSONArray("offer_fields").getJSONObject(0).getString("field_name"));
                            } catch (JSONException e) {
                                progress.setVisibility(View.GONE);
                                noInternet.setVisibility(View.VISIBLE);
                                //Log.d("Progress Dialog","Progress Dialog");
                                e.printStackTrace();
                            }
                        }

                        progressDialog.dismiss();

                    } else {
                        progress.setVisibility(View.GONE);
                        noInternet.setVisibility(View.GONE);
                        progressDialog.dismiss();

                    }
                    progressDialog.dismiss();

                    if (templateDataArrayList.size() == 0) {

                       /* progress.setVisibility(View.GONE);
                       //noInternet.setVisibility(View.VISIBLE);
                        rvCategory.setVisibility(View.GONE);*/
                    } else {

                        adminTemplateListAdapter = new AdminTemplateListAdapter(
                                _act,
                                templateDataArrayList,
                                TemplateCategoryActivity.this,
                                image_path);

                        linearLayoutManager = new LinearLayoutManager(_act);
                        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                        rvTemplate.setLayoutManager(new GridLayoutManager(
                                TemplateCategoryActivity.this, 1));
                        rvTemplate.setAdapter(adminTemplateListAdapter);
                        adminTemplateListAdapter.notifyDataSetChanged();

                        progress.setVisibility(View.GONE);
                        noInternet.setVisibility(View.GONE);
                        rvCategory.setVisibility(View.VISIBLE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progress.setVisibility(View.GONE);
               // noInternet.setVisibility(View.VISIBLE);
                rvCategory.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }

    private void webServiceCallTemplateCategoryList(String keyWord) {

        Interface api = IUrls.getRetrofit(IUrls.BASE_URL).create(Interface.class);
        Call<ResponseBody> result = api.PostTemplateCategory(
                SharedPref.getPrefs(_act, IConstant.USER_ID)
        );
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

                            JSONArray jsonArrayCategory = jsonObject.getJSONArray("template_categories");
                            for (int index = 0; index < jsonArrayCategory.length(); index++) {
                                try {
                                    //CategorySearchObjectArrayList.add(AllCategory(jsonArrayCategory.getJSONObject(index), category_path));
                                    CategorySearchObjectArrayList.add(new TemplateCategory(jsonArrayCategory.getJSONObject(index), category_path));

                                } catch (JSONException e) {
                                    e.printStackTrace();


                                    rvCategory.setVisibility(View.GONE);
                                }
                            }

                            if (CategorySearchObjectArrayList.size() == 0) {

                                progress.setVisibility(View.GONE);
                                //noInternet.setVisibility(View.VISIBLE);
                                rvCategory.setVisibility(View.GONE);
                            } else {

                                /*categoryListAdapter = new CategoryListAdapter(_act, CategorySearchObjectArrayList, new HomeCategoryAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(String id) {

                                    }
                                });*/
                                Log.d("CategorySearchActivity", "onResponse: 1");
                                categoryListAdapter = new TemplateCategoryListAdapter(
                                        _act,
                                        CategorySearchObjectArrayList,
                                        TemplateCategoryActivity.this);

                                linearLayoutManager = new LinearLayoutManager(_act);
                                linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
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
                            //noInternet.setVisibility(View.VISIBLE);
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

/*    public static class Utility {
        public static int calculateNoOfColumns(Context context, float columnWidthDp) { // For example columnWidthdp=180
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
            int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
            return noOfColumns;
        }
    }*/

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

    @Override
    public void onItemClick(int id) {
        Bundle bundle = new Bundle();
        //int position = Integer.parseInt(id);
        bundle.putString("category_id", CategorySearchObjectArrayList.get(id).getId());
        bundle.putString("category_name", CategorySearchObjectArrayList.get(id).getName());

        getTemplate(CategorySearchObjectArrayList.get(id).getId());

        /*Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(900, intent);
        finish();*/
    }


    @Override
    public void onItemClickForAdminTemplate(int id) {

        Intent intent = new Intent(TemplateCategoryActivity.this, AddTemplateFieldsActivity.class);
        intent.putExtra("List", templateDataArrayList);
        intent.putExtra("position", id);
        intent.putExtra("BusinessName", BusinessName);
        intent.putExtra("Email", Email);
        intent.putExtra("Mobile", Mobile);
        intent.putExtra("TemplateID", templateDataArrayList.get(id).getId());

        intent.putExtra("action", action);
        intent.putExtra("category_id", category_id);
        intent.putExtra("advt_title", advt_title);
        intent.putExtra("advt_desc", advt_desc);
        intent.putExtra("tags", tags);
        intent.putExtra("last_date", last_date);
        intent.putExtra("store_id", store_id);


        startActivity(intent);


    }
}