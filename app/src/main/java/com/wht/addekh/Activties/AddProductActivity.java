package com.wht.addekh.Activties;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.wht.addekh.BaseActivity;
import com.wht.addekh.Constant.IConstant;
import com.wht.addekh.Constant.IUrls;
import com.wht.addekh.Constant.Interface;
import com.wht.addekh.Helper.ConnectionDetector;
import com.wht.addekh.Helper.Helper_Method;
import com.wht.addekh.Helper.SharedPref;
import com.wht.addekh.Helper.Validations;
import com.wht.addekh.Model.BottomsheetMyStoreListObject;
import com.wht.addekh.Model.MyStoreCategory;
import com.wht.addekh.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends BaseActivity {

    private Activity _act;
    private RecyclerView recycler_blog;
    private Validations validations;
    private SharedPref sharedPref;
    private ConnectionDetector connectionDetector;
    private LinearLayoutManager linearLayoutManager;
    private String imagePath = "", receiveImgPath,user_profile_path = null;
    private Uri ilogoUrl;

    String badge_image,user_image;
    private RelativeLayout progress, noInternet;
    private ArrayAdapter<MyStoreCategory> spinnerCategory_Adapter;
    Toolbar toolbar;
    private TextView toolbar_title,txtCount;
    private Button btn_addblog;
    private Spinner spinnerCategory,spinnerStore;
    private String strCategoryId, strCategory,action,strblogId="";
    private ArrayList<MyStoreCategory> categoryBlogListArrayList;
    private ArrayList<BottomsheetMyStoreListObject> blogListArrayList;
    private int pos;
    private static final int IMAGE = 100;


    //New Image Section


    private RequestBody requestFileProfile = null;
    private File profileFile = null;
    private MultipartBody.Part bodyProfile = null;

    //Image Selection
    //Image Picker
    public static final int REQUEST_IMAGE = 100;

    //Image Selection
    private Uri iurl;
    private File file = null;
    private MultipartBody.Part body = null;
    private RequestBody requestFile = null;
    private ImageView ivProfilePic, ivAddProfileImage;
    private static final int GalleryPick = 1;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    private static final int IMAGEPICK_GALLERY_REQUEST = 300;
    private static final int IMAGE_PICKCAMERA_REQUEST = 400;
    String cameraPermission[];
    String storagePermission[];
    private BottomsheetMyStoreListObject blogList;
    private String updateFlag = "0", strLoadingTitle = "Adding Profile Details...", strBusinessId = null, strProductId = null;
    private static final String TAG = ActivityAddStore.class.getSimpleName();
    public static final int REQUEST_LOGO_IMAGE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
//        image_close = findViewById(R.id.image_close);
//        image_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        spinnerCategory = (Spinner)findViewById(R.id.sp_Store_category);
        spinnerStore = (Spinner)findViewById(R.id.sp_Store);

        _act = AddProductActivity.this;

        getCategories();

    }

    private void getCategories()
    {
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

                            spinnerCategory_Adapter = new ArrayAdapter<MyStoreCategory>(AddProductActivity.this, android.R.layout.simple_spinner_item,
                                    categoryBlogListArrayList);
                            spinnerCategory_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerCategory.setAdapter(spinnerCategory_Adapter);
                            spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                            {
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                                {
                                    // On selecting a spinner item
                                    String item = adapterView.getItemAtPosition(i).toString();
                                    strCategoryId = categoryBlogListArrayList.get(i).getId();
                                    strCategory = categoryBlogListArrayList.get(i).getname();

                                }

                                public void onNothingSelected(AdapterView<?> adapterView) {
                                    return;
                                }
                            });

                                /*for (int j = 0; j <categoryBlogListArrayList.size() ; j++)
                                {
                                    if (categoryBlogListArrayList.get(j).getname().equals(blogListArrayList.get(pos).getCategory_name()))
                                    {
                                        spinnerCategory.setSelection(j);

                                    }

                                }*/



                        } else {

                            progress.setVisibility(View.INVISIBLE);
                            noInternet.setVisibility(View.VISIBLE);


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