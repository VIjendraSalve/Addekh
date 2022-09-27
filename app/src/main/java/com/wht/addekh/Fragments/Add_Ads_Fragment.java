package com.wht.addekh.Fragments;

import android.Manifest;
import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.wht.addekh.Activties.ActivityAddStore;
import com.wht.addekh.Activties.ActivityGetLocation;
import com.wht.addekh.Activties.Template.TemplateCategoryActivity;
import com.wht.addekh.Constant.IConstant;
import com.wht.addekh.Constant.IUrls;
import com.wht.addekh.Constant.Interface;
import com.wht.addekh.Helper.ConnectionDetector;
import com.wht.addekh.Helper.EasyLocationProvider;
import com.wht.addekh.Helper.Helper_Method;
import com.wht.addekh.Helper.ImagePickerActivity;
import com.wht.addekh.Helper.MyValidator;
import com.wht.addekh.Helper.SharedPref;
import com.wht.addekh.Model.AllCategory;
import com.wht.addekh.Model.BottomsheetMyStoreListObject;
import com.wht.addekh.Model.CategorySearchObject;
import com.wht.addekh.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeoutException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Add_Ads_Fragment extends Fragment {

    public static final int REQUEST_LOGO_IMAGE = 100;
    private static final String TAG = ActivityAddStore.class.getSimpleName();
    ActivityResultLauncher<Intent> launchSomeActivity = null, launchDropActivity = null;
    EasyLocationProvider easyLocationProvider; //Declare Global Variable
    private Spinner spnr_store, spinnerCategory;
    private ImageView image_add_banner;
    private EditText edt_add_title, edt_add_end_date, et_add_details,edt_tags;
    private Activity _act;
    private TextView btnSubmit, tv_lat_long;
    private String imagePath = "", receiveImgPath, user_profile_path = null;
    private Uri ilogoUrl;
    // private TagEditText tagedt_tags;
    private ConnectionDetector connectionDetector;
    private RequestBody requestFileProfile = null;
    private File profileFile = null;
    private MultipartBody.Part bodyProfile = null;
    //Image Selection
    private Uri iurl;
    private File file = null;
    private MultipartBody.Part body = null;
    private RequestBody requestFile = null;
    //Category List
    private ArrayList<AllCategory> categoryArrayList = new ArrayList<>();
    private String category_path = null;
    private ArrayList<BottomsheetMyStoreListObject> myStoreListObjects = new ArrayList<>();
    //variable : action:1 // 1:add, 2:update  // last_date:2022-07-01
    private String user_id = "";
    private String action = "1";
    private String categoryId = "";
    private String advt_title = "";
    private double lat;
    private double lang;
    private String advt_desc = "";
    private String tags = "";
    private String last_date = "";
    private String store_id = "";
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private ImageView cancelBtn;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add__ads_, container, false);

        tv_lat_long = view.findViewById(R.id.tv_lat_long);
//        Dexter.withActivity(getContext())
//                .withPermissions(
//                        Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.ACCESS_COARSE_LOCATION)
//                .withListener(new MultiplePermissionsListener() {
//                    @Override
//                    public void onPermissionsChecked(MultiplePermissionsReport report) {
//                        // check if all permissions are granted
//                        if (report.areAllPermissionsGranted()) {
//                            //true
//                            getLocation();
//                        }
//                        // check for permanent denial of any permission
//                        if (report.isAnyPermissionPermanentlyDenied()) {
//                            // show alert dialog navigating to Settings
//                            showSettingsDialog();
//                        }
//                    }
//
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                        token.continuePermissionRequest();
//                    }
//                }).
//                withErrorListener(new PermissionRequestErrorListener() {
//                    @Override
//                    public void onError(DexterError error) {
//                        Toast.makeText(AddAdsActivity.this, "Error occurred! ", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .onSameThread()
//                .check();


        initViews();
        return view;
    }




    private void getMyStoreList() {
        Interface api = IUrls.getRetrofit(IUrls.BASE_URL).create(Interface.class);
        Call<ResponseBody> result = api.PostMyStorelist(
                SharedPref.getPrefs(_act, IConstant.USER_ID),
                "",
                "1",
                -1);
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

                        if (stringCode.equalsIgnoreCase("true")) {

                            myStoreListObjects.add(new BottomsheetMyStoreListObject("-2", "Select Store"));
                            myStoreListObjects.add(new BottomsheetMyStoreListObject("-1", "Create new store"));

                            JSONArray jsonArray = i.getJSONArray("my_stores");
                            for (int index = 0; index < jsonArray.length(); index++) {
                                try {
                                    myStoreListObjects.add(new BottomsheetMyStoreListObject(jsonArray.getJSONObject(index), ""));

                                } catch (JSONException e) {
                                    e.printStackTrace();

                                }
                            }

                            if (myStoreListObjects.size() != 0) {
                                ArrayAdapter<BottomsheetMyStoreListObject> arrayAdapter1 = new ArrayAdapter<BottomsheetMyStoreListObject>(
                                        getContext(),
                                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                                        myStoreListObjects);
                                spnr_store.setAdapter(arrayAdapter1);
                                //progressDialog.dismiss();
                                spnr_store.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                                        ((TextView) parent.getChildAt(0)).setGravity(Gravity.CENTER);
                                        store_id = myStoreListObjects.get(position).getId();
                                        if (store_id.equals("-1")) {
                                            Intent intent = new Intent(getContext(), ActivityAddStore.class);
                                            intent.putExtra("flag", "1");
                                            startActivity(intent);
                                        }
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });

                            } else {

                            }

                        } else {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
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

    private void webServiceCallCategory() {
        Interface api = IUrls.getRetrofit(IUrls.BASE_URL).create(Interface.class);
        Call<ResponseBody> result = api.PostCategoryCategorySearch("");
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {
                    output = response.body().string();
                    categoryArrayList.clear();
                    Log.d("my_tag", "onResponse: " + output);
                    try {

                        JSONObject jsonObject = new JSONObject(output);
                        String stringCode = jsonObject.getString(IConstant.RESPONSE_CODE);
                        String stringMsg = jsonObject.getString(IConstant.RESPONSE_MESSAGE);

                        if (stringCode.equalsIgnoreCase(IConstant.RESPONSE_SUCCESS)) {

                            category_path = jsonObject.getString("category_path");

                            JSONArray jsonArrayCategory = jsonObject.getJSONArray("category_list");
                            for (int i = 0; i < jsonArrayCategory.length(); i++) {
                                try {
                                    JSONObject obj = jsonArrayCategory.getJSONObject(i);
                                    categoryArrayList.add(new AllCategory(obj, category_path));
                                } catch (JSONException e) {
                                    //Log.d("Progress Dialog","Progress Dialog");
                                    e.printStackTrace();
                                }
                            }

                            if (categoryArrayList.size() != 0) {

                                //   Toast.makeText(_act, "rewrwe", Toast.LENGTH_SHORT).show();
                                ArrayAdapter<AllCategory> arrayAdapter1 = new ArrayAdapter<AllCategory>(
                                        getContext(),
                                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                                        categoryArrayList);
                                spinnerCategory.setAdapter(arrayAdapter1);
                                //progressDialog.dismiss();
                                spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        ((TextView) parent.getChildAt(0)).setGravity(Gravity.CENTER);
                                        categoryId = categoryArrayList.get(position).getId();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                                getMyStoreList();


                            } else {
                                Toast.makeText(_act, "rewrwe11111", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Helper_Method.toaster(_act, stringMsg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                } catch (IOException e) {
                    e.printStackTrace();

                } /*finally {
                    webServiceCartCount();
                }*/
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

    private void initViews() {

        View view = null;
        cancelBtn = view.findViewById(R.id.cancelBtn);
        spnr_store = view.findViewById(R.id.spnr_store);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        image_add_banner = view.findViewById(R.id.image_add_banner);
        edt_add_title = view.findViewById(R.id.edt_add_title);
        edt_add_end_date = view.findViewById(R.id.edt_add_end_date);
        et_add_details = view.findViewById(R.id.et_add_details);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        edt_tags = view.findViewById(R.id.edt_tags);



        launchSomeActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 123) {
                            Intent data = result.getData();
                            Log.d("valueVIJ", "onActivityResult: " + data.getStringExtra("lat"));
                            Log.d("valueVIJ", "onActivityResult: " + data.getStringExtra("lagn"));
                            Log.d("valueVIJ", "onActivityResult: " + data.getStringExtra("Address"));

                        }
                    }
                });


        image_add_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(AddAdsActivity.this, "Test", Toast.LENGTH_SHORT).show();
                Dexter.withActivity(_act)
                        .withPermissions(
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {
                                    //showImageLogoPickerOptions();

                                    Intent intent = new Intent(getContext(), TemplateCategoryActivity.class);
                                    startActivity(intent);
                                }

                                if (report.isAnyPermissionPermanentlyDenied()) {
                                  //  showSettingsDialog();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        edt_add_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker arg0, int year, int month, int day_of_month) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, (month));
                        calendar.set(Calendar.DAY_OF_MONTH, day_of_month);
                        String myFormat = "yyyy/MM/dd"; // vijChange
                        //String myFormat = "MM/dd/yyyy"; // vijChange
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

                        edt_add_end_date.setText(sdf.format(calendar.getTime()));


                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                long now = System.currentTimeMillis() - 1000;
                dialog.getDatePicker().setMinDate(now);// TODO: used to hide previous date,month and year
                // dialog.getDatePicker().setMinDate(oldMillis);// TODO: used to hide previous date,month and year
                calendar.add(Calendar.YEAR, 0);
                dialog.show();

                /*long now = System.currentTimeMillis() - 1000;
                dp_time.setMinDate(now);
                dp_time.setMaxDate(now+(1000*60*60*24*7)); //After 7 Days from Now*/
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    if (imagePath.length() > 0) {
                        AddStoreAdvertise();
                    } else {
                        Toast.makeText(getContext(), "Please select image..!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        webServiceCallCategory();
    }

    private boolean validateFields() {
        boolean result = true;
        if (!MyValidator.isValidField(edt_add_title)) {
            result = false;
        }
        if (!MyValidator.isValidField(et_add_details)) {
            result = false;
        }
        if (!MyValidator.isValidField(edt_add_end_date)) {
            result = false;
        }
        return result;
    }

    public void openSomeActivityForResult() {
        Intent intent = new Intent(getContext(), ActivityGetLocation.class);
        launchSomeActivity.launch(intent);
    }

    //Image Picker Whole Code
    private void loadProfile(String url) {
        Log.d(TAG, "Image logo cache path: " + url);
        Glide.with(this)
                .load(url)
                .apply(new RequestOptions().placeholder(R.drawable.no_image_available).error(R.drawable.no_image_available).diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(image_add_banner);
    }

    private void showImageLogoPickerOptions() {
        ImagePickerActivity.showImagePickerOptions(getContext(), new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchLogoCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchLogoGalleryIntent();
            }
        });
    }

    private void launchLogoCameraIntent() {
        Intent intent = new Intent(_act, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 0); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_LOGO_IMAGE);
    }

    private void launchLogoGalleryIntent() {
        Intent intent = new Intent(_act, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_LOGO_IMAGE);
    }

    private void showSettingsDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(_act);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();

        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings



    private void AddStoreAdvertise() {
        Helper_Method.showProgressBar(_act, "Please wait..");
        Interface service = IUrls.getApiService();
        profileFile = null;
        bodyProfile = null;
        requestFileProfile = null;
        int compressionRatioLogoImg = 50; //1 == originalImage, 2 = 50% compression, 4=25% compress
        if (!imagePath.equalsIgnoreCase("")) {

            profileFile = new File(String.valueOf(imagePath));
            try {
                Bitmap bitmap = BitmapFactory.decodeFile(profileFile.getPath());
                bitmap.compress(Bitmap.CompressFormat.JPEG, compressionRatioLogoImg, new FileOutputStream(profileFile));
            } catch (Throwable t) {
                Log.e("ERROR", "Error compressing file." + t.toString());
                t.printStackTrace();
            }
            requestFileProfile = RequestBody.create(MediaType.parse("image/*"), profileFile);
            bodyProfile = MultipartBody.Part.createFormData("banner_image", profileFile.getName(), requestFileProfile);
        }
        Log.d("mytag", "profileFile: " + profileFile);
        Log.d("mytag", "file: " + file);
        Log.d("mytag", "body: " + body);
        Log.d("mytag", "action: " + action);
        Log.d("mytag", "userid: " + SharedPref.getPrefs(_act, IConstant.USER_ID));
        Log.d("mytag", "action: " + action);
        Log.d("mytag", "title: " + edt_add_title.getText().toString());
        Log.d("mytag", "lat: " + lat);
        Log.d("mytag", "lng: " + lang);
        Log.d("mytag", "desc: " + et_add_details.getText().toString().trim());
        Log.d("mytag", "tags: " + edt_tags.getText().toString().trim());
        Log.d("mytag", "last_Date: " + edt_add_end_date.getText().toString());
        Log.d("mytag", "storeid: " + store_id);


        RequestBody userID = RequestBody.create(MediaType.parse("text/plain"), SharedPref.getPrefs(_act, IConstant.USER_ID));
        RequestBody action1 = RequestBody.create(MediaType.parse("text/plain"), action);
        RequestBody categoryId1 = RequestBody.create(MediaType.parse("text/plain"), categoryId);
        RequestBody title = RequestBody.create(MediaType.parse("text/plain"), edt_add_title.getText().toString().trim());
        RequestBody lat1 = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(lat));
        RequestBody lang1 = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(lang));
        RequestBody desc = RequestBody.create(MediaType.parse("text/plain"), et_add_details.getText().toString().trim());
        RequestBody tags = RequestBody.create(MediaType.parse("text/plain"), edt_tags.getText().toString().trim());
        RequestBody last_date = RequestBody.create(MediaType.parse("text/plain"), edt_add_end_date.getText().toString().trim());
        RequestBody storeid = RequestBody.create(MediaType.parse("text/plain"), store_id);

        Call<ResponseBody> result = service.PostAddMyAdvertisment(
                userID,
                action1,
                categoryId1,
                title,
                lat1,
                lang1,
                desc,
                tags,
                last_date,
                storeid,
                bodyProfile);

        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";

                try {
                    output = response.body().string();
                    Log.d("my_tag", "onResponse: " + output);
                    try {
                        JSONObject jsonObject = new JSONObject(output);
                        String stringCode = jsonObject.getString(IConstant.RESPONSE_CODE);
                        String stringMsg = jsonObject.getString(IConstant.RESPONSE_MESSAGE);

                        if (stringCode.equalsIgnoreCase(IConstant.RESPONSE_SUCCESS)) {
                            Helper_Method.dismissProgessBar();

                            //loadFragment(new HomeFragment());
                            _act.finish();

                        } else {
                            Helper_Method.toaster(_act, stringMsg);
                            Helper_Method.dismissProgessBar();


                        }
                    } catch (JSONException e) {
                        Helper_Method.dismissProgessBar();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Helper_Method.dismissProgessBar();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(_act, "Failure", Toast.LENGTH_SHORT).show();
                Helper_Method.dismissProgessBar();

            }
        });
    }


    }

