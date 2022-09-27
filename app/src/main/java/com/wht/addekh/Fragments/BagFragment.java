package com.wht.addekh.Fragments;

import static android.view.View.GONE;

import android.Manifest;
import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.wht.addekh.Activties.ActivityAddStore;
import com.wht.addekh.Activties.ActivityGetLocation;
import com.wht.addekh.Adapter.BottomSheetMyStoreListAdapter;
import com.wht.addekh.Constant.IConstant;
import com.wht.addekh.Constant.IUrls;
import com.wht.addekh.Constant.Interface;
import com.wht.addekh.Helper.ConnectionDetector;
import com.wht.addekh.Helper.Helper_Method;
import com.wht.addekh.Helper.SharedPref;
import com.wht.addekh.Helper.Validations;
import com.wht.addekh.MainActivity;
import com.wht.addekh.Model.BottomsheetMyStoreListObject;
import com.wht.addekh.Model.MyStoreCategory;
import com.wht.addekh.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BagFragment extends Fragment  implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "EmailOrdersDialogFrag";
    public static Context context;
    public int remaining = 0;
   // FloatingActionButton floatingActionButton;
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

    //new ADD
    private ArrayList<MyStoreCategory> categoryBlogListArrayList;
    private EditText edt_Storename, edt_StoreAddress, edt_StoreContact, edt_StoreDesc, et_description;
    private Button btn_addblog;
    private Spinner spinnerCategory;
    private ImageView ivAddProfileImage;
    private String action = "1";
    private ArrayList<BottomsheetMyStoreListObject> blogListArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bag, container, false);

        _act = getActivity();

       // floatingActionButton = view.findViewById(R.id.floatingActionButton);
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
       /* floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityAddStore.class);
                intent.putExtra("flag", "1");
                //intent.putExtra("pos", pos);
                // intent.putParcelableArrayListExtra("list", bottomsheetMyStoreListObjectArrayList);
                startActivity(intent);
            }
        });*/

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

  /*  public void AddStoreShowDialog(){

        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_add_store);

        _act = getActivity();
        validations = new Validations();
        connectionDetector = ConnectionDetector.getInstance(_act);
        Helper_Method.hideSoftInput(_act);

        categoryBlogListArrayList = new ArrayList<>();
        //EDitText
//        et_action = findViewById(R.id.et_action);

        edt_Storename = dialog.findViewById(R.id.edt_Storename);
        edt_StoreAddress = dialog.findViewById(R.id.edt_StoreAddress);
        edt_StoreContact = dialog.findViewById(R.id.edt_StoreContact);
        edt_StoreDesc = dialog.findViewById(R.id.edt_StoreDesc);
        btn_addblog = dialog.findViewById(R.id.btnSubmit);
        spinnerCategory = (Spinner) dialog.findViewById(R.id.sp_Store_category);
        //ivProfilePic = findViewById(R.id.ivProfilePic);
        ivAddProfileImage = dialog.findViewById(R.id.ivAddProfileImage);

        getBlogCategories();


        edt_StoreAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(getActivity())
                        .withPermissions(
                                Manifest.permission.ACCESS_NETWORK_STATE,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                // check if all permissions are granted
                                if (report.areAllPermissionsGranted()) {
                                    //camera.selectImage(iv_image, 0);
                                    openSomeActivityForResult();
                                }
                                // check for permanent denial of any permission
                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    // show alert dialog navigating to Settings
                                    showSettingsDialog();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).
                        withErrorListener(new PermissionRequestErrorListener() {
                            @Override
                            public void onError(DexterError error) {
                                Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .onSameThread()
                        .check();


            }
        });

        ivAddProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(_act)
                        .withPermissions(android.Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {
                                    showImageLogoPickerOptions();
                                }

                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    showSettingsDialog();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        btn_addblog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionDetector.checkConnection(_act)) {

                    if (isValid()) {

                        Helper_Method.hideSoftInput(_act);
                        AddBlogAPI();

                        Log.d("mytagstrcorectPath", "strcorectPath" + imagePath);
                        Log.d("mytagilogoUrl", "onActivityResult: i" + ilogoUrl);
                    }

                } else {
                    Helper_Method.toaster_long(_act, getResources().getString(R.string.string_internet_connection_warning));
                }
            }
        });
        action = "1";


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialoAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    public void openSomeActivityForResult() {
        Intent intent = new Intent(this, get);
        launchSomeActivity.launch(intent);
    }

    private void getBlogCategories() {
//        progress.setVisibility(View.VISIBLE);
//        noInternet.setVisibility(View.INVISIBLE);
        Interface api = IUrls.getRetrofit(IUrls.BASE_URL).create(Interface.class);
        Call<ResponseBody> result = api.POSTCategoryList(SharedPref.getPrefs(_act, IConstant.USER_ID));
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {
                    categoryBlogListArrayList = new ArrayList<>();

                    categoryBlogListArrayList.clear();
                    output = response.body().string();
                    Log.d("my_tag", "Response: " + output);
                    try {

                        JSONObject i = new JSONObject(output);
                        String stringCode = i.getString("result");
                        String stringMsg = i.getString("reason");

                        if (stringCode.equalsIgnoreCase("true")) {
                            JSONArray jsonArray = i.getJSONArray("categories");
                            for (int index = 0; index < jsonArray.length(); index++) {
                                try {

                                    categoryBlogListArrayList.add(new MyStoreCategory(jsonArray.getJSONObject(index)));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    progress.setVisibility(View.INVISIBLE);
                                    noInternet.setVisibility(View.VISIBLE);

                                }
                            }

                            spinnerCategory_Adapter = new ArrayAdapter<MyStoreCategory>(MainActivity.this, android.R.layout.simple_spinner_item,
                                    categoryBlogListArrayList);
                            spinnerCategory_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerCategory.setAdapter(spinnerCategory_Adapter);
                            spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    // On selecting a spinner item
                                    String item = adapterView.getItemAtPosition(i).toString();
                                    strCategoryId = categoryBlogListArrayList.get(i).getId();
                                    strCategory = categoryBlogListArrayList.get(i).getname();

                                }

                                public void onNothingSelected(AdapterView<?> adapterView) {
                                    return;
                                }
                            });

                           *//* if (getIntent().getStringExtra("flag").equals("2")) {
                                for (int j = 0; j < categoryBlogListArrayList.size(); j++) {
                                    if (categoryBlogListArrayList.get(j).getname().equals(blogListArrayList.get(pos).getCategory_name())) {
                                        spinnerCategory.setSelection(j);

                                    }

                                }
                            }*//*


                        } else {

                            //  progress.setVisibility(View.INVISIBLE);
                            // noInternet.setVisibility(View.VISIBLE);


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progress.setVisibility(View.INVISIBLE);
                        noInternet.setVisibility(View.VISIBLE);

                    }

                } catch (IOException e) {
                    e.printStackTrace();
//                    progress.setVisibility(View.INVISIBLE);
//                    noInternet.setVisibility(View.VISIBLE);


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
    }*/

    @Override
    public void onRefresh() {
        bottomsheetMyStoreListObjectArrayList.clear();
        page_no = 1;
        getMyStoreList();

    }
}
