package com.wht.addekho.Activties;

import android.Manifest;
import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.wht.addekho.BaseActivity;
import com.wht.addekho.Constant.IConstant;
import com.wht.addekho.Constant.IUrls;
import com.wht.addekho.Constant.Interface;
import com.wht.addekho.Fragments.HomeFragment;
import com.wht.addekho.Helper.ConnectionDetector;
import com.wht.addekho.Helper.Helper_Method;
import com.wht.addekho.Helper.ImagePickerActivity;
import com.wht.addekho.Helper.SharedPref;
import com.wht.addekho.Helper.Validations;
import com.wht.addekho.Model.BottomsheetMyStoreListObject;
import com.wht.addekho.Model.MyStoreCategory;
import com.wht.addekho.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityAddStore extends BaseActivity {
    //Image Selection
    //Image Picker
    public static final int REQUEST_IMAGE = 100;
    public static final int REQUEST_LOGO_IMAGE = 100;
    private static final int IMAGE = 100;
    private static final int GalleryPick = 1;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    private static final int IMAGEPICK_GALLERY_REQUEST = 300;
    private static final int IMAGE_PICKCAMERA_REQUEST = 400;
    private static final String TAG = ActivityAddStore.class.getSimpleName();
    String badge_image, user_image;
    Toolbar toolbar;
    String cameraPermission[];
    String storagePermission[];
    ActivityResultLauncher<Intent> launchSomeActivity = null, launchDropActivity = null;
    String blog_category, blog_title, author, description, blog_id;
    private Activity _act;
    private RecyclerView recycler_blog;
    private Validations validations;
    private SharedPref sharedPref;
    private ConnectionDetector connectionDetector;


    //New Image Section
    private LinearLayoutManager linearLayoutManager;
    private String imagePath = "", receiveImgPath, user_profile_path = null;
    private Uri ilogoUrl;
    private RelativeLayout progress, noInternet;
    private ArrayAdapter<MyStoreCategory> spinnerCategory_Adapter;
    private TextView toolbar_title, txtCount;
    private Button btn_addblog;
    private Spinner spinnerCategory;
    private String strCategoryId, strCategory, action, strblogId = "";
    private ArrayList<MyStoreCategory> categoryBlogListArrayList;
    private ArrayList<BottomsheetMyStoreListObject> blogListArrayList;
    private int pos;
    private RequestBody requestFileProfile = null;
    private File profileFile = null;
    private MultipartBody.Part bodyProfile = null;
    //Image Selection
    private Uri iurl;
    private File file = null;
    private MultipartBody.Part body = null;
    private RequestBody requestFile = null;
    private ImageView ivProfilePic, ivAddProfileImage;
    private BottomsheetMyStoreListObject blogList;
    private String updateFlag = "0", strLoadingTitle = "Adding Profile Details...", strBusinessId = null, strProductId = null;
    private EditText edt_Storename, edt_StoreAddress, edt_StoreContact, edt_StoreDesc, et_description;
    private String lat = "", lang = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_store);

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        initViews();

    }


    private void initViews() {
//        progress = (RelativeLayout) findViewById(R.id.progress);
//        noInternet = (RelativeLayout) findViewById(R.id.noInternet);
        _act = ActivityAddStore.this;
        validations = new Validations();
        connectionDetector = ConnectionDetector.getInstance(_act);
        Helper_Method.hideSoftInput(_act);

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
                            edt_StoreAddress.setText("" + data.getStringExtra("Address"));

                            lat = data.getStringExtra("lat");
                            lang = data.getStringExtra("lagn");
                           /* pickuplat = data.getStringExtra("lat");
                            pickuplong = data.getStringExtra("lagn");
                            pickupAddress = data.getStringExtra("Address");
                            et_Pickuplatlong.setText(pickupAddress + "\nLatitude : " + pickuplat + ", Longitude : " + pickuplong);*/
                        }
                    }
                });


        categoryBlogListArrayList = new ArrayList<>();
        //EDitText
//        et_action = findViewById(R.id.et_action);

        edt_Storename = findViewById(R.id.edt_Storename);
        edt_StoreAddress = findViewById(R.id.edt_StoreAddress);
        edt_StoreContact = findViewById(R.id.edt_StoreContact);
        edt_StoreDesc = findViewById(R.id.edt_StoreDesc);
        btn_addblog = findViewById(R.id.btnSubmit);
        spinnerCategory = (Spinner) findViewById(R.id.sp_Store_category);
        ivProfilePic = findViewById(R.id.ivProfilePic);
        ivAddProfileImage = findViewById(R.id.ivAddProfileImage);


        edt_StoreAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(ActivityAddStore.this)
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
                        .withPermissions(android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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

        if (getIntent().getStringExtra("flag").equals("2")) {

            action = "2";
            blogListArrayList = getIntent().getParcelableArrayListExtra("list");
            pos = getIntent().getIntExtra("pos", 0);
            edt_Storename.setText(blogListArrayList.get(pos).getStore_name());
            edt_StoreAddress.setText(blogListArrayList.get(pos).getStore_address());
            edt_StoreContact.setText(blogListArrayList.get(pos).getStore_contact_number());
            edt_StoreDesc.setText(blogListArrayList.get(pos).getStore_desc());
            strCategoryId = blogListArrayList.get(pos).getCategory_id();
            strblogId = blogListArrayList.get(pos).getId();

            if (SharedPref.getPrefs(_act, IConstant.USER_PHOTO) != null && !SharedPref.getPrefs(_act, IConstant.USER_PHOTO).isEmpty() && !SharedPref.getPrefs(_act, IConstant.USER_PHOTO).equals("null") && !SharedPref.getPrefs(_act, IConstant.USER_PHOTO).equals("")) {
                Glide.with(_act)
                        .load(SharedPref.getPrefs(_act, IConstant.USER_PHOTO))
                        .apply(new RequestOptions().placeholder(R.drawable.default_user).error(R.drawable.default_user))
                        .into(ivProfilePic);
            } else {
                Glide.with(_act)
                        .load(R.drawable.default_user)
                        .apply(new RequestOptions().placeholder(R.drawable.default_user).error(R.drawable.default_user))
                        .into(ivProfilePic);
            }

        } else {
            action = "1";
        }


        getBlogCategories();

    }

    public void openSomeActivityForResult() {
        Intent intent = new Intent(this, ActivityGetLocation.class);
        launchSomeActivity.launch(intent);
    }

    //Image Picker Whole Code
    private void loadProfile(String url) {
        Log.d(TAG, "Image logo cache path: " + url);
        Glide.with(this)
                .load(url)
                .apply(new RequestOptions().placeholder(R.drawable.default_user).error(R.drawable.default_user).diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(ivProfilePic);
    }


    private void showImageLogoPickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
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
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGO_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                    // loading profile image from local cache
                    loadProfile(uri.toString());

                    String strpath = null;
                    strpath = uri.toString();
                    String[] parts = strpath.split("\\//");
                    String part1 = parts[0];
                    imagePath = parts[1];

                    Log.d("mytag", "strcorectPath" + imagePath);
                    Log.d("mytag", "onActivityResult: i" + ilogoUrl);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

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

                            spinnerCategory_Adapter = new ArrayAdapter<MyStoreCategory>(ActivityAddStore.this, android.R.layout.simple_spinner_item,
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

                            if (getIntent().getStringExtra("flag").equals("2")) {
                                for (int j = 0; j < categoryBlogListArrayList.size(); j++) {
                                    if (categoryBlogListArrayList.get(j).getname().equals(blogListArrayList.get(pos).getCategory_name())) {
                                        spinnerCategory.setSelection(j);

                                    }

                                }
                            }


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
    }


    private boolean isValid() {

        if (validations.isBlank(edt_Storename)) {
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            edt_Storename.startAnimation(shake);
            edt_Storename.setError(getResources().getString(R.string.error_field_required));
            return false;
        }
        if (validations.isBlank(edt_StoreAddress)) {
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            edt_StoreAddress.startAnimation(shake);
            edt_StoreAddress.setError(getResources().getString(R.string.error_field_required));
            return false;
        }

        if (validations.isBlank(edt_StoreContact)) {
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            edt_StoreContact.startAnimation(shake);
            edt_StoreContact.setError(getResources().getString(R.string.error_field_required));
            return false;
        }
        return true;

    }


    private void AddBlogAPI() {
        Helper_Method.showProgressBar(_act, strLoadingTitle);
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
            bodyProfile = MultipartBody.Part.createFormData("store_logo", profileFile.getName(), requestFileProfile);
        }
        Log.d("mytag", "profileFile: " + profileFile);
        Log.d("mytag", "file: " + file);
        Log.d("mytag", "body: " + body);
        Log.d("mytag", "action: " + action);
        Log.d("mytag", "userid: " + SharedPref.getPrefs(_act, IConstant.USER_ID));
        Log.d("mytag", "cat: " + strCategoryId);
        Log.d("mytag", "title: " + edt_Storename.getText().toString().trim());
        Log.d("mytag", "author: " + edt_StoreAddress.getText().toString().trim());
        Log.d("mytag", "author: " + edt_StoreContact.getText().toString().trim());
        Log.d("mytag", "desc: " + edt_StoreDesc.getText().toString().trim());
        Log.d("mytag", "desc: " + edt_StoreDesc.getText().toString().trim());
        Log.d("mytag", "desc: " + strblogId);


        RequestBody idMul = RequestBody.create(MediaType.parse("text/plain"), SharedPref.getPrefs(_act, IConstant.USER_ID));
        RequestBody actionn = RequestBody.create(MediaType.parse("text/plain"), action);
        RequestBody strCategory = RequestBody.create(MediaType.parse("text/plain"), strCategoryId);
        RequestBody storename = RequestBody.create(MediaType.parse("text/plain"), edt_Storename.getText().toString().trim());
        RequestBody storeaddr = RequestBody.create(MediaType.parse("text/plain"), edt_StoreAddress.getText().toString().trim());
        RequestBody storecontact = RequestBody.create(MediaType.parse("text/plain"), edt_StoreContact.getText().toString().trim());
        RequestBody storelat = RequestBody.create(MediaType.parse("text/plain"), lat);
        RequestBody storelng = RequestBody.create(MediaType.parse("text/plain"), lang);
        RequestBody storedesc = RequestBody.create(MediaType.parse("text/plain"), edt_StoreDesc.getText().toString().trim());
        RequestBody storeid = RequestBody.create(MediaType.parse("text/plain"), strblogId);

        Call<ResponseBody> result = service.PostAddMystore(
                idMul,
                storename,
                actionn,
                strCategory,
                storeaddr,
                storecontact,
                storelat,
                storelng,
                storedesc,
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
                            /*imagePath = jsonObject.getString("stores_path");

                            JSONObject jsonObject1 = jsonObject.getJSONObject("my_stores");
//                            SharedPref.setPrefs(_act, IConstant.USER_ID, jsonObject.getString("user_id"));
                            SharedPref.setPrefs(_act, IConstant.STORE_TITLE, jsonObject.getString("store_name"));
                            SharedPref.setPrefs(_act, IConstant.STORE_ADDR, jsonObject.getString("store_address"));
                            SharedPref.setPrefs(_act, IConstant.STORE_CONTACT, jsonObject.getString("store_contact_number"));
                            SharedPref.setPrefs(_act, IConstant.STORE_CATEGORY, jsonObject.getString("category_name"));
                            SharedPref.setPrefs(_act, IConstant.STORE_DESCRIPTION, jsonObject.getString("store_desc"));
                            SharedPref.setPrefs(_act, USER_PHOTO, imagePath + jsonObject1.getString("store_logo"));


                            if (SharedPref.getPrefs(_act, IConstant.USER_PHOTO) != null && !SharedPref.getPrefs(_act, IConstant.USER_PHOTO).isEmpty() && !SharedPref.getPrefs(_act, IConstant.USER_PHOTO).equals("null") && !SharedPref.getPrefs(_act, IConstant.USER_PHOTO).equals("")) {
                                Glide.with(_act)
                                        .load(SharedPref.getPrefs(_act, IConstant.USER_PHOTO))
                                        .apply(new RequestOptions().placeholder(R.drawable.default_user).error(R.drawable.default_user))
                                        .into(ivProfilePic);
                            } else {
                                Glide.with(_act)
                                        .load(R.drawable.default_user)
                                        .apply(new RequestOptions().placeholder(R.drawable.default_user).error(R.drawable.default_user))
                                        .into(ivProfilePic);
                            }*/


                            Helper_Method.hideSoftInput(_act);
                            Helper_Method.dismissProgessBar();
                            Helper_Method.toaster(_act, stringMsg);
                            finish();
                            //loadFragment(new HomeFragment());


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

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack("TAG")
                    .commit();
            return true;
        }
        return false;
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

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