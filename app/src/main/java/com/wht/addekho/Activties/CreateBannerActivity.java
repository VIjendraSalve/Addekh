package com.wht.addekho.Activties;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

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
import com.wht.addekho.Helper.Camera;
import com.wht.addekho.Helper.Helper_Method;
import com.wht.addekho.Helper.MyValidator;
import com.wht.addekho.Helper.SharedPref;
import com.wht.addekho.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.wht.addekho.Constant.IConstant;
import com.wht.addekho.Constant.IUrls;
import com.wht.addekho.Constant.Interface;

import com.wht.addekho.MainActivity;

import com.wht.addekho.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateBannerActivity extends BaseActivity implements Camera.AsyncResponse {


    private EditText edt_business_name, edt_email, edt_mobile, edt_amount, edt_offer,
            edt_offer_desc, edt_offer_date;
    private Button btn_upload_image, btn_add_vendor;
    private CircleImageView iv_proof;
    private Camera camera;
    private ProgressDialog progressDialog;
    private String profile_image_name = "", profile_image_path = "";
    private String discountType="";
    private Activity _act;
    private Button btn_pick_location;
    private String templateID = "";
    private DatePickerDialog datePickerDialog;

    private ArrayList<String> discountList = new ArrayList<>();
    private Spinner spnr_discount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_banner);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        TextView toolbar_title = toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("Add Banner Fields");
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back_24);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(upArrow);
        upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        _act = CreateBannerActivity.this;
        templateID = getIntent().getStringExtra("TemplateID");
        initViews();
    }

    private void initViews() {


        edt_business_name = findViewById(R.id.edt_business_name);
        edt_email = findViewById(R.id.edt_email);
        edt_mobile = findViewById(R.id.edt_mobile);
        edt_amount = findViewById(R.id.edt_amount);
        edt_offer = findViewById(R.id.edt_offer);
        edt_offer_desc = findViewById(R.id.edt_offer_desc);
        edt_offer_date = findViewById(R.id.edt_offer_date);
        spnr_discount = findViewById(R.id.spnr_discount);

        btn_add_vendor = findViewById(R.id.btn_add_vendor);
        btn_upload_image = findViewById(R.id.btn_upload_image);
        iv_proof = findViewById(R.id.iv_proof);

        progressDialog = new ProgressDialog(CreateBannerActivity.this);
        progressDialog.setMessage("" + getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        camera = new Camera(CreateBannerActivity.this);
        btn_upload_image = findViewById(R.id.btn_upload_image);

        edt_offer_date.setFocusable(false);

        edt_offer_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(CreateBannerActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                view.setMinDate(System.currentTimeMillis() - 1000);
                                edt_offer_date.setText( year+ "/"+ (monthOfYear + 1) + "/" + dayOfMonth);
                            }

                        }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        });


        btn_add_vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {

                        uploadFile(profile_image_path, profile_image_name);

                } else {
                    Toast.makeText(CreateBannerActivity.this, "Please update image", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(CreateBannerActivity.this)
                        .withPermissions(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA
                        )
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                // check if all permissions are granted
                                if (report.areAllPermissionsGranted()) {
                                    camera.selectImage(iv_proof, 0);
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



        discountList.add("Flat");
        discountList.add("Percentage");

        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(
                CreateBannerActivity.this,
                com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                discountList);
        spnr_discount.setAdapter(arrayAdapter2);
        //progressDialog.dismiss();
        spnr_discount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                discountType = discountList.get(position);
                if(discountType.equals("Flat")){
                    discountType = "1";
                }else {
                    discountType = "2";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private boolean validateFields() {
        boolean result = true;
        if (!MyValidator.isValidField(edt_business_name)) {
            result = false;
        }
        if (!MyValidator.isValidField(edt_email)) {
            result = false;
        }
        if (!MyValidator.isValidField(edt_mobile)) {
            result = false;
        }
        if (!MyValidator.isValidField(edt_amount)) {
            result = false;
        }
        if (!MyValidator.isValidField(edt_offer)) {
            result = false;
        }
        if (!MyValidator.isValidField(edt_offer_desc)) {
            result = false;
        }
        if (!MyValidator.isValidField(edt_offer_date)) {
            result = false;
        }



        return result;
    }

    @Override
    public void processFinish(String result, int img_no) {
        String[] parts = result.split("/");
        String imagename = parts[parts.length - 1];
        // Log.d("Image_path", result + " " + img_no);
        profile_image_name = imagename;
        profile_image_path = result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        camera.myActivityResult(requestCode, resultCode, data);

    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateBannerActivity.this);
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

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }


    private void uploadFile(String fileUri, String filename) {

        progressDialog.show();
        Interface api = IUrls.getRetrofit(IUrls.BASE_URL).create(Interface.class);
        MultipartBody.Part body = null;
        if (!profile_image_path.equalsIgnoreCase("")) {
            File file = new File(fileUri);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            body = MultipartBody.Part.createFormData("image", filename, requestFile);
        } else {
            //Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }

        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), SharedPref.getPrefs(CreateBannerActivity.this, IConstant.USER_ID));
        RequestBody businessName = RequestBody.create(MediaType.parse("text/plain"), edt_business_name.getText().toString());
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), edt_email.getText().toString());
        RequestBody mobile = RequestBody.create(MediaType.parse("text/plain"), edt_mobile.getText().toString());
        RequestBody discountType1 = RequestBody.create(MediaType.parse("text/plain"), discountType);
        RequestBody discountAmount = RequestBody.create(MediaType.parse("text/plain"), edt_amount.getText().toString());
        RequestBody offerName = RequestBody.create(MediaType.parse("text/plain"), edt_offer.getText().toString());
        RequestBody offerDesc = RequestBody.create(MediaType.parse("text/plain"), edt_offer_desc.getText().toString());
        RequestBody validupto = RequestBody.create(MediaType.parse("text/plain"), edt_offer_date.getText().toString());
        RequestBody templateID1 = RequestBody.create(MediaType.parse("text/plain"), templateID);


        Call<ResponseBody> call;
        RequestBody file_name = RequestBody.create(MediaType.parse("text/plain"), "" + filename);
        call = (Call<ResponseBody>) api.POSTCreateBanner(
                user_id,
                templateID1,
                businessName,
                email,
                mobile,
                discountType1,
                discountAmount,
                offerName,
                offerDesc,
                validupto,
                body
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v("Upload", "success");
                String output = "";
                try {
                    output = response.body().string();

                    Log.d("Response", "response=> " + output);
                    JSONObject i = new JSONObject(output);
                    boolean res = Boolean.parseBoolean(i.getString("result"));
                    String reason = i.getString("reason");

                    if (res) {
                        progressDialog.dismiss();
                        Toast.makeText(CreateBannerActivity.this, "" + reason, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CreateBannerActivity.this, BannerListActivity.class);
                        startActivity(intent);

                    } else
                        Toast.makeText(CreateBannerActivity.this, "" + reason, Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();
                    //  Log.v("Upload", "" + response.body().string());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Upload error:", "");
                progressDialog.dismiss();
            }

        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                //overridePendingTransition(R.animator.left_right, R.animator.right_left);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            //overridePendingTransition(R.animator.left_right, R.animator.right_left);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}