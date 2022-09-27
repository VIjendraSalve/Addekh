package com.wht.addekh;

import static com.wht.addekh.Constant.IConstant.MyCurrentLatitude;
import static com.wht.addekh.Constant.IConstant.MyCurrentLongitude;
import static com.wht.addekh.Constant.IConstant.USER_PHOTO;

import android.Manifest;
import android.accounts.NetworkErrorException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.wht.addekh.Activties.ActivityAddStore;
import com.wht.addekh.Activties.ActivityGetLocation;
import com.wht.addekh.Activties.AddAdsActivity;
import com.wht.addekh.Activties.BottomDialogFragment;
import com.wht.addekh.Activties.InitialActivity.ProfileActivity;
import com.wht.addekh.Activties.Template.TemplateCategoryActivity;
import com.wht.addekh.Constant.IConstant;
import com.wht.addekh.Constant.IUrls;
import com.wht.addekh.Constant.Interface;
import com.wht.addekh.Fragments.AdFragment;
import com.wht.addekh.Fragments.BagFragment;
import com.wht.addekh.Fragments.BottomsheetDialogMyStoreListFragment;
import com.wht.addekh.Fragments.BottomsheetPublishAdFragment;
import com.wht.addekh.Fragments.HomeFragment;
import com.wht.addekh.Fragments.StoreFragment;
import com.wht.addekh.Helper.ConnectionDetector;
import com.wht.addekh.Helper.GPSTracker;
import com.wht.addekh.Helper.Helper_Method;
import com.wht.addekh.Helper.ImagePickerActivity;
import com.wht.addekh.Helper.MyLocationService;
import com.wht.addekh.Helper.MyValidator;
import com.wht.addekh.Helper.OnGPS;
import com.wht.addekh.Helper.SharedPref;
import com.wht.addekh.Helper.TagEditText;
import com.wht.addekh.Helper.Validations;
import com.wht.addekh.Model.AllCategory;
import com.wht.addekh.Model.BottomsheetMyStoreListObject;
import com.wht.addekh.Model.MyStoreCategory;
import com.wht.addekh.map_custom_search.PickLocationActivity;

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

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks {


    //AddStoreDialog
    public static final int REQUEST_IMAGE = 100;
    public static final int REQUEST_LOGO_IMAGE = 100;
    private final static int REQUEST_LOCATION = 199;
    private static final int IMAGE = 100;
    private static final int GalleryPick = 1;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    private static final int IMAGEPICK_GALLERY_REQUEST = 300;
    private static final int IMAGE_PICKCAMERA_REQUEST = 400;
    private static final String TAG = ActivityAddStore.class.getSimpleName();
    protected GoogleApiClient mGoogleApiClient;
    FloatingActionButton floatingActionButton;
    LinearLayout linearLayout;
    LinearLayout bottom_sheet_image;
    BottomSheetBehavior bottomSheetBehavior;
    ImageView imageView;
    Location gps_loc;
    Location network_loc;
    Location final_loc;
    double longitude;
    double latitude;
    String userCountry, userAddress;
    ImageView img_profile;
    ActivityResultLauncher<Intent> launchSomeActivity = null;
    LocationRequest locationRequest;

    //TextView btnSubmit;
    FusedLocationProviderClient fusedLocationProviderClient;
    String badge_image, user_image;
    Toolbar toolbar;
    String cameraPermission[];
    String storagePermission[];
    // ActivityResultLauncher<Intent> launchSomeActivity = null, launchDropActivity = null;
    String blog_category, blog_title, author, description, blog_id;
    private GPSTracker gpsTracker;
    private boolean statusOfGPS;
    private Geocoder geocoder;
    private TextView tv_location;
    private Handler handler = new Handler();
    private Activity _act;
    private String flagToOpenAddStoreForm = "0"; // 0: store not added 1: store added
    private OnGPS onGPS = new OnGPS();
    //Dialog
    private Spinner spnr_store, spinnerCategory;
    private ImageView image_add_banner;
    private EditText edt_add_title, edt_add_end_date, et_add_details;
    private TextView btnSubmit, tv_lat_long;
    private String imagePath = "", receiveImgPath, user_profile_path = null;
    private Uri ilogoUrl;
    private TagEditText edt_tags;
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
    private String lat = "";
    private String lang = "";
    private String advt_desc = "";
    private String tags = "";
    private String last_date = "";
    private String store_id = "", businessname = "", email = "", mobile = "";
    private FusedLocationProviderClient mFusedLocationClient;
    //private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private ImageView cancelBtn, cancelBtnAddStore;
    // private Activity _act;
    private RecyclerView recycler_blog;
    private Validations validations;
    private SharedPref sharedPref;
    // private ConnectionDetector connectionDetector;


    //New Image Section
    private LinearLayoutManager linearLayoutManager;
    //  private String imagePath = "", receiveImgPath, user_profile_path = null;
    //  private Uri ilogoUrl;
    private RelativeLayout progress, noInternet;
    private ArrayAdapter<MyStoreCategory> spinnerCategory_Adapter;
    private TextView toolbar_title, txtCount;
    private Button btn_addblog;
    // private Spinner spinnerCategory;
    private String strCategoryId, strCategory, strblogId = "";
    private ArrayList<MyStoreCategory> categoryBlogListArrayList;
    private ArrayList<BottomsheetMyStoreListObject> blogListArrayList;
    private int pos;
    //  private RequestBody requestFileProfile = null;
    //  private File profileFile = null;
    // private MultipartBody.Part bodyProfile = null;
    //Image Selection
    // private Uri iurl;
    //  private File file = null;
    // private MultipartBody.Part body = null;
    //  private RequestBody requestFile = null;
    private ImageView ivAddProfileImage;
    private BottomsheetMyStoreListObject blogList;
    private String updateFlag = "0", strLoadingTitle = "Adding Profile Details...", strBusinessId = null, strProductId = null;
    private EditText edt_Storename, edt_StoreAddress, edt_StoreContact, edt_StoreDesc, et_description;
    // private String lat = "", lang = "";
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFragment(new HomeFragment());
        _act = MainActivity.this;
        //loading the default fragment


        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        img_profile = (ImageView) findViewById(R.id.img_profile);
        if (!SharedPref.getPrefs(_act, USER_PHOTO).equals("null")) {
            Glide.with(_act)
                    .load(SharedPref.getPrefs(_act, USER_PHOTO))
                    .apply(new RequestOptions().placeholder(R.drawable.default_user).error(R.drawable.default_user))
                    .into(img_profile);
        } else {

            Glide.with(_act)
                    .load(R.drawable.default_user)
                    .apply(new RequestOptions().placeholder(R.drawable.default_user).error(R.drawable.default_user))
                    .into(img_profile);

        }

        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //                bottomSheetBehavior.setHideable(true);
//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                if (flagToOpenAddStoreForm.equals("1")) {
                    showDialog();
                } else {
                    Intent intentstore = new Intent(getApplicationContext(), ActivityAddStore.class);
                    intentstore.putExtra("flag", "0");
                    startActivity(intentstore);
                }
            }
        });
//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                bottomSheetBehavior.setHideable(true);
////                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                if (flagToOpenAddStoreForm.equals("1")) {
//                    Intent intentstore = new Intent(getApplicationContext(), AddAdsActivity.class);
//                    startActivity(intentstore);
//                } else {
//                    Intent intentstore = new Intent(getApplicationContext(), ActivityAddStore.class);
//                    intentstore.putExtra("flag", "0");
//                    startActivity(intentstore);
//                }
////                openBottomSheetPublishAdDialog();
//
//            }
//        });

        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        tv_location = findViewById(R.id.tv_location);
        boolean gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsStatus) {
            Toast.makeText(_act, "gps off", Toast.LENGTH_SHORT).show();
            statusCheck();
        }

        if (onGPS.IsGpsOn(MainActivity.this)) {
            updateLocation();
        }

        Dexter.withActivity(MainActivity.this)
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
                            // Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                            try {
                                if (ActivityCompat.checkSelfPermission(_act, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                        ActivityCompat.checkSelfPermission(_act, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                network_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (gps_loc != null) {
                                final_loc = gps_loc;
                                latitude = final_loc.getLatitude();
                                longitude = final_loc.getLongitude();
                            } else if (network_loc != null) {
                                final_loc = network_loc;
                                latitude = final_loc.getLatitude();
                                longitude = final_loc.getLongitude();
                            } else {
                                latitude = 0.0;
                                longitude = 0.0;
                            }

                            if (SharedPref.getPrefs(_act, MyCurrentLatitude) != null) {
                                latitude = Double.parseDouble(SharedPref.getPrefs(_act, MyCurrentLatitude));
                                longitude = Double.parseDouble(SharedPref.getPrefs(_act, MyCurrentLongitude));
                            }

                            try {
                                Log.d("locationDetails", "latitude1: " + latitude);
                                Log.d("locationDetails", "longitude: " + longitude);


                                SharedPref.setPrefs(MainActivity.this, MyCurrentLatitude, String.valueOf(latitude));
                                SharedPref.setPrefs(MainActivity.this, MyCurrentLongitude, String.valueOf(longitude));

                               // Toast.makeText(_act, "" + SharedPref.getPrefs(_act, MyCurrentLongitude), Toast.LENGTH_SHORT).show();

                                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                if (addresses != null && addresses.size() > 0) {
                                    userCountry = addresses.get(0).getCountryName();
                                    userAddress = addresses.get(0).getAddressLine(0);

                                    Log.d("locationDetails", "getCountryName: " + addresses.get(0).getCountryName());
                                    Log.d("locationDetails", "getAddressLine 0 : " + addresses.get(0).getAddressLine(0));
                                    Log.d("locationDetails", "getAddressLine 1 : " + addresses.get(0).getAddressLine(1));
                                    Log.d("locationDetails", "getLocality : " + addresses.get(0).getLocality());
                                    Log.d("locationDetails", "getAdminArea : " + addresses.get(0).getAdminArea());
                                    Log.d("locationDetails", "getSubAdminArea : " + addresses.get(0).getSubAdminArea());
                                    Log.d("locationDetails", "getSubLocality : " + addresses.get(0).getSubLocality());

                                    SharedPref.setPrefs(MainActivity.this, IConstant.LocationToSetOnDashboard, addresses.get(0).getSubLocality());

                                    Log.d("locationDetails", "getSubLocality : " + addresses.get(0).getSubLocality());
                                    if(!SharedPref.getPrefs(MainActivity.this, IConstant.LocationToSetOnDashboard).equals("null")){
                                        SharedPref.setPrefs(MainActivity.this, IConstant.LocationToSetOnDashboard, addresses.get(0).getSubLocality());
                                        tv_location.setText(SharedPref.getPrefs(MainActivity.this, IConstant.LocationToSetOnDashboard));
                                        Log.d("locationDetails", "getSubLocality1 : " + addresses.get(0).getSubLocality());
                                    }else{
                                        Log.d("locationDetails", "getSubLocality2 : " + addresses.get(0).getSubLocality());
                                        SharedPref.setPrefs(MainActivity.this, IConstant.LocationToSetOnDashboard, addresses.get(0).getLocality());
                                        tv_location.setText(SharedPref.getPrefs(MainActivity.this, IConstant.LocationToSetOnDashboard));
                                        Log.d(TAG, "onPermissionsCheckedLocation: "+SharedPref.getPrefs(MainActivity.this, IConstant.LocationToSetOnDashboard));
                                    }

                                } else {
                                    Log.d("locationDetails", "getSubLocality3 : " + addresses.get(0).getSubLocality());
                                    userCountry = "Unknown";
                                    tv_location.setText(userCountry);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


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


        tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PickLocationActivity.class);
                launchSomeActivity.launch(intent);
            }
        });

        launchSomeActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                       /* if (result.getResultCode() == 123) {
                            Intent data = result.getData();
                        }*/

                        tv_location.setText(SharedPref.getPrefs(MainActivity.this, IConstant.LocationToSetOnDashboard));
                    }
                });

        getMyStoreList();
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void setLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Dexter.withActivity(MainActivity.this)
                .withPermissions(
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                )
                .withListener(new MultiplePermissionsListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                            try {
                                gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                network_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (gps_loc != null) {
                                final_loc = gps_loc;
                                latitude = final_loc.getLatitude();
                                longitude = final_loc.getLongitude();
                            } else if (network_loc != null) {
                                final_loc = network_loc;
                                latitude = final_loc.getLatitude();
                                longitude = final_loc.getLongitude();
                            } else {
                                latitude = 0.0;
                                longitude = 0.0;
                            }

                            try {
                                Log.d("locationDetails", "latitude: " + latitude);
                                Log.d("locationDetails", "longitude: " + longitude);
                                SharedPref.setPrefs(MainActivity.this, IConstant.MyCurrentLatitude, String.valueOf(latitude));
                                SharedPref.setPrefs(MainActivity.this, IConstant.MyCurrentLongitude, String.valueOf(longitude));

                                //  Toast.makeText(MainActivity.this, ""+SharedPref.getPrefs(MainActivity.this, IConstant.MyCurrentLongitude), Toast.LENGTH_SHORT).show();

                                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                if (addresses != null && addresses.size() > 0) {
                                    userCountry = addresses.get(0).getCountryName();
                                    userAddress = addresses.get(0).getAddressLine(0);

                                    Log.d("locationDetails", "getCountryName: " + addresses.get(0).getCountryName());
                                    Log.d("locationDetails", "getAddressLine 0 : " + addresses.get(0).getAddressLine(0));
                                    Log.d("locationDetails", "getAddressLine 1 : " + addresses.get(0).getAddressLine(1));
                                    Log.d("locationDetails", "getLocality : " + addresses.get(0).getLocality());
                                    Log.d("locationDetails", "getAdminArea : " + addresses.get(0).getAdminArea());
                                    Log.d("locationDetails", "getSubAdminArea : " + addresses.get(0).getSubAdminArea());
                                    Log.d("locationDetails", "getSubLocality : " + addresses.get(0).getSubLocality());

                                    if(!SharedPref.getPrefs(MainActivity.this, IConstant.LocationToSetOnDashboard).equals("null")){
                                        SharedPref.setPrefs(MainActivity.this, IConstant.LocationToSetOnDashboard, addresses.get(0).getSubLocality());
                                        tv_location.setText(SharedPref.getPrefs(MainActivity.this, IConstant.LocationToSetOnDashboard));
                                        Log.d("locationDetails", "getSubLocality1 : " + addresses.get(0).getSubLocality());
                                    }else{
                                        Log.d("locationDetails", "getSubLocality2 : " + addresses.get(0).getSubLocality());
                                        SharedPref.setPrefs(MainActivity.this, IConstant.LocationToSetOnDashboard, addresses.get(0).getLocality());
                                        tv_location.setText(SharedPref.getPrefs(MainActivity.this, IConstant.LocationToSetOnDashboard));
                                        Log.d(TAG, "onPermissionsCheckedLocation: "+SharedPref.getPrefs(MainActivity.this, IConstant.LocationToSetOnDashboard));
                                    }
                                } else {
                                    userCountry = "Unknown";
                                    tv_location.setText(userCountry);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


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
                            JSONArray jsonArray = i.getJSONArray("my_stores");
                            if (jsonArray.length() > 0) {
                                flagToOpenAddStoreForm = "1";
                            } else {
                                flagToOpenAddStoreForm = "0";
                            }

                        } else {
                            flagToOpenAddStoreForm = "0";
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

    private void enableLoc() {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(2 * 1000);
        locationRequest.setFastestInterval(5 * 1000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:

                        geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                        Log.d("longitude", "onCreate: " + gpsTracker.getLongitude());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (addresses != null) {
                            if (addresses.size() > 0) {
                                String cityName = addresses.get(0).getLocality();
                                String stateName = addresses.get(0).getAddressLine(1);
                                String countryName = addresses.get(0).getAddressLine(2);
                                Log.d("CityName", "onCreate: " + cityName);

                            }
                        }

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {

                            // Ignore the error.
                        }
                        break;
                }
            }
        });
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void openBottomSheetPublishAdDialog() {
        BottomsheetPublishAdFragment bottomSheet = new BottomsheetPublishAdFragment();
        bottomSheet.show(getSupportFragmentManager(),
                "ModalBottomSheet");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();
                break;

            case R.id.navigation_store:
                fragment = new StoreFragment();
//                Intent intentstore =new Intent(getApplicationContext(), BottomsheetActivity.class);
//                startActivity(intentstore);
//                openBottomSheetDialog();
//                openBottomSheetStoreDialog();

                break;

            case R.id.placeholder:
                Intent intentstore = new Intent(getApplicationContext(), AddAdsActivity.class);
                startActivity(intentstore);
                break;

            case R.id.navigation_ads:
                fragment = new AdFragment();

//                openBottomSheetStoreDialog();
                break;

            case R.id.navigation_profile:
                //Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                //startActivity(intent);

                fragment = new BagFragment();
                break;
        }

        return loadFragment(fragment);
    }

    private void openBottomSheetStoreDialog() {
        BottomsheetDialogMyStoreListFragment addBottomDialogFragment =
                BottomsheetDialogMyStoreListFragment.newInstance(this);
        addBottomDialogFragment.show(getSupportFragmentManager(),
                BottomsheetDialogMyStoreListFragment.TAG);
    }

    private void openBottomSheetDialog() {
        BottomDialogFragment addBottomDialogFragment =
                BottomDialogFragment.newInstance(this);
        addBottomDialogFragment.show(getSupportFragmentManager(),
                BottomDialogFragment.TAG);
    }

    public void showDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_add__ads_);

        cancelBtn = dialog.findViewById(R.id.cancelBtn);
        //cancelBtnAddStore = dialog.findViewById(R.id.cancelBtnAddStore);
        spnr_store = dialog.findViewById(R.id.spnr_store);
        spinnerCategory = dialog.findViewById(R.id.spinnerCategory);
        image_add_banner = dialog.findViewById(R.id.image_add_banner);
        edt_add_title = dialog.findViewById(R.id.edt_add_title);
        edt_add_end_date = dialog.findViewById(R.id.edt_add_end_date);
        et_add_details = dialog.findViewById(R.id.et_add_details);
        btnSubmit = dialog.findViewById(R.id.btnSubmit);
        edt_tags = dialog.findViewById(R.id.edt_tags);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

       /* launchSomeActivity = registerForActivityResult(
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
                });*/


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

                                    Intent intent = new Intent(_act, TemplateCategoryActivity.class);
                                    startActivity(intent);
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

        edt_add_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(_act, new DatePickerDialog.OnDateSetListener() {
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
                    //AddStoreAdvertise();

                    Intent intent = new Intent(_act, TemplateCategoryActivity.class);
                    intent.putExtra("action", "1");
                    intent.putExtra("category_id", categoryId);
                    intent.putExtra("advt_title", "" + edt_add_title.getText().toString());
                    intent.putExtra("advt_desc", "" + et_add_details.getText().toString());
                    intent.putExtra("tags", "" + edt_tags.getText().toString());
                    intent.putExtra("last_date", "" + edt_add_end_date.getText().toString());
                    intent.putExtra("store_id", "" + store_id);
                    intent.putExtra("BusinessName", businessname);
                    intent.putExtra("Email", email);
                    intent.putExtra("Mobile", mobile);


                    startActivity(intent);
                }


            }
        });
        webServiceCallCategory();

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialoAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }


    //+++++++++++++++++++END++++++++++++++++++++
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
        Log.d("mytag", "file: " + requestFileProfile);
        Log.d("mytag", "body: " + bodyProfile);
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
                            finish();

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

    private void getMyStoreListForDialogue() {

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

                    myStoreListObjects.clear();
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
                                        _act,
                                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                                        myStoreListObjects);
                                spnr_store.setAdapter(arrayAdapter1);
                                //progressDialog.dismiss();
                                spnr_store.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        store_id = myStoreListObjects.get(position).getId();
                                        businessname = myStoreListObjects.get(position).getStore_name();
                                        email = "";
                                        mobile = myStoreListObjects.get(position).getStore_contact_number();
                                        if (store_id.equals("-1")) {
                                            Intent intent = new Intent(_act, ActivityAddStore.class);
                                            intent.putExtra("flag", "1");
                                            startActivity(intent);
                                            //AddStoreShowDialog();
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
                        categoryArrayList.clear();
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
                                        _act,
                                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                                        categoryArrayList);
                                spinnerCategory.setAdapter(arrayAdapter1);
                                //progressDialog.dismiss();
                                spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        categoryId = categoryArrayList.get(position).getId();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                                getMyStoreListForDialogue();


                            } else {
                                // Toast.makeText(_act, "rewrwe11111", Toast.LENGTH_SHORT).show();
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


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        MainActivity.super.onBackPressed();
                        finishAffinity();
                    }
                }).create().show();
    }

    /********************* Background Location Update**************************/
    private void updateLocation() {
        buildLocationRequest();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, getPendingIntent());
    }

    private PendingIntent getPendingIntent() {
        Log.d("MYTAG", "getPendingIntent: ");
        Intent intent = new Intent(this, MyLocationService.class);
        intent.setAction(MyLocationService.ACTION_PROCESS_UPDATE);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
    }

    private void buildLocationRequest() {

        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(18000);
        locationRequest.setFastestInterval(18000);
        locationRequest.setSmallestDisplacement(10f);

    }

    //Add Store DialogBox

    public void AddStoreShowDialog() {

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_add_store);

        _act = MainActivity.this;
        validations = new Validations();
        connectionDetector = ConnectionDetector.getInstance(_act);
        Helper_Method.hideSoftInput(_act);

        /*launchSomeActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 123) {
                            Intent data = result.getData();         Log.d("valueVIJ", "onActivityResult: " + data.getStringExtra("lat"));
                            Log.d("valueVIJ", "onActivityResult: " + data.getStringExtra("lagn"));
                            Log.d("valueVIJ", "onActivityResult: " + data.getStringExtra("Address"));
                            edt_StoreAddress.setText("" + data.getStringExtra("Address"));
                         }
                    }
                });*/


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
                Dexter.withActivity(MainActivity.this)
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
                        AddBlogAPI(dialog);

                        Log.d("mytagstrcorectPath", "strcorectPath" + imagePath);
                        Log.d("mytagilogoUrl", "onActivityResult: i" + ilogoUrl);
                    }

                } else {
                    Helper_Method.toaster_long(_act, getResources().getString(R.string.string_internet_connection_warning));
                }
            }
        });

        String flag = "2"; // 1 : update 2 : add
        if (flag.equals("1")) {

            action = "1";
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
                        .into(ivAddProfileImage);
            } else {
                Glide.with(_act)
                        .load(R.drawable.default_user)
                        .apply(new RequestOptions().placeholder(R.drawable.default_user).error(R.drawable.default_user))
                        .into(ivAddProfileImage);
            }

        } else {
            action = "1";
        }


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialoAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    public void openSomeActivityForResult() {
        Intent intent = new Intent(this, ActivityGetLocation.class);
        launchSomeActivity.launch(intent);
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

    private void AddBlogAPI(Dialog dialog) {
        Helper_Method.showProgressBar(_act, strLoadingTitle);
        Interface service = IUrls.getApiService();
        profileFile = null;
        bodyProfile = null;
        requestFileProfile = null;

        String[] parts = imagePath.split("/");
        String imagename = parts[parts.length - 1];
        // Log.d("Image_path", result + " " + img_no);
        String filename = imagename;

        int compressionRatioLogoImg = 50; //1 == originalImage, 2 = 50% compression, 4=25% compress
        if (!imagePath.equalsIgnoreCase("")) {

            File file = new File(imagePath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            body = MultipartBody.Part.createFormData("profile_image", filename, requestFile);
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
        RequestBody storelat = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(latitude));
        RequestBody storelng = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(longitude));
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
                                        .into(ivAddProfileImage);
                            } else {
                                Glide.with(_act)
                                        .load(R.drawable.default_user)
                                        .apply(new RequestOptions().placeholder(R.drawable.default_user).error(R.drawable.default_user))
                                        .into(ivAddProfileImage);
                            }*/


                            Helper_Method.hideSoftInput(_act);
                            Helper_Method.dismissProgessBar();
                            Helper_Method.toaster(_act, stringMsg);
                            dialog.dismiss();
                            showDialog();


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

//    private void openSettings() {
//        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        Uri uri = Uri.fromParts("package", getPackageName(), null);
//        intent.setData(uri);
//        startActivityForResult(intent, 101);
//    }

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

    private void loadProfile(String url) {
        Log.d(TAG, "Image logo cache path: " + url);
        Glide.with(this)
                .load(url)
                .apply(new RequestOptions().placeholder(R.drawable.default_user).error(R.drawable.default_user).diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(ivAddProfileImage);
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

                           /* if (getIntent().getStringExtra("flag").equals("2")) {
                                for (int j = 0; j < categoryBlogListArrayList.size(); j++) {
                                    if (categoryBlogListArrayList.get(j).getname().equals(blogListArrayList.get(pos).getCategory_name())) {
                                        spinnerCategory.setSelection(j);

                                    }

                                }
                            }*/


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

    /********************* Background Location Update END**************************/


}