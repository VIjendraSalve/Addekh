package com.wht.addekho.Fragments;

import static android.app.Activity.RESULT_OK;
import static com.wht.addekho.Constant.IConstant.USER_PHOTO;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

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
import com.wht.addekho.Activties.InitialActivity.LoginActivity;
import com.wht.addekho.BaseActivity;
import com.wht.addekho.Constant.IConstant;
import com.wht.addekho.Constant.IUrls;
import com.wht.addekho.Constant.Interface;
import com.wht.addekho.Helper.Camera;
import com.wht.addekho.Helper.ConnectionDetector;
import com.wht.addekho.Helper.Helper_Method;
import com.wht.addekho.Helper.ImagePickerActivity;
import com.wht.addekho.Helper.SharedPref;
import com.wht.addekho.Helper.Validations;
import com.wht.addekho.MainActivity;
import com.wht.addekho.Model.GenderObject;
import com.wht.addekho.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends BaseActivity implements Camera.AsyncResponse {

    private Button btn_logout;
    Button btnSubmit;
    EditText edt_fname, edt_lname, edt_email, edt_mobileNo;
    CircleImageView profile;
    Activity _act;

    private ImageView iv_proof, iv_edit_profile_pic;

    private Camera camera;
    private ProgressDialog progressDialog;
    private String profile_image_name = "", profile_image_path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        _act = ProfileActivity.this;
        camera = new Camera(ProfileActivity.this);
        progressDialog = new ProgressDialog(_act);
        progressDialog.setMessage("" + getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        initVariable();

    }

    private void initVariable() {
        iv_proof = findViewById(R.id.iv_proof);
        iv_edit_profile_pic = findViewById(R.id.iv_edit_profile_pic);
        edt_fname = findViewById(R.id.edt_fname);
        edt_lname = findViewById(R.id.edt_lname);
        edt_email = findViewById(R.id.edt_email);
        edt_mobileNo = findViewById(R.id.edt_mobileNo);
        btn_logout = findViewById(R.id.btn_logout);
        btnSubmit = findViewById(R.id.btnSubmit);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPref.clearPref(ProfileActivity.this);
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        Log.d("valueToaset", "initVariable: "+SharedPref.getPrefs(_act, IConstant.USER_FIRST_NAME));

        if(!SharedPref.getPrefs(_act, IConstant.USER_FIRST_NAME).equals("null")) {
            edt_fname.setText(SharedPref.getPrefs(_act, IConstant.USER_FIRST_NAME));
        }else {
            edt_fname.setText("");
        }

        if(!SharedPref.getPrefs(_act, IConstant.USER_LAST_NAME).equals("null")) {
            edt_lname.setText(SharedPref.getPrefs(_act, IConstant.USER_LAST_NAME));
        }else {
            edt_lname.setText("");
        }

        if(!SharedPref.getPrefs(_act, IConstant.USER_EMAIL).equals("null")) {
            edt_email.setText(SharedPref.getPrefs(_act, IConstant.USER_EMAIL));
        }else{
            edt_email.setText("");
        }

        if(!SharedPref.getPrefs(_act, USER_PHOTO).equals("null")) {
            Glide.with(_act)
                    .load(SharedPref.getPrefs(_act, USER_PHOTO))
                    .apply(new RequestOptions().placeholder(R.drawable.default_user).error(R.drawable.default_user))
                    .into(iv_proof);
        }else{

            Glide.with(_act)
                    .load(R.drawable.default_user)
                    .apply(new RequestOptions().placeholder(R.drawable.default_user).error(R.drawable.default_user))
                    .into(iv_proof);

        }

        edt_mobileNo.setText(SharedPref.getPrefs(_act, IConstant.USER_MOBILE));

        iv_edit_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(ProfileActivity.this)
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

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile(profile_image_path, profile_image_name);
            }
        });
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
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
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

    private void uploadFile(String fileUri, String filename) {

        progressDialog.show();
        Interface api = IUrls.getRetrofit(IUrls.BASE_URL).create(Interface.class);

        MultipartBody.Part body = null;
        if (!profile_image_path.equalsIgnoreCase("")) {
            File file = new File(fileUri);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            body = MultipartBody.Part.createFormData("profile_image", filename, requestFile);
        } else {
            //Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }

        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"),
                SharedPref.getPrefs(_act, IConstant.USER_ID));
        RequestBody firstname = RequestBody.create(MediaType.parse("text/plain"), edt_fname.getText().toString());
        RequestBody lastname = RequestBody.create(MediaType.parse("text/plain"), edt_lname.getText().toString());
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), edt_email.getText().toString());
        RequestBody gender = RequestBody.create(MediaType.parse("text/plain"), "1");

        Call<ResponseBody> call;
        RequestBody file_name = RequestBody.create(MediaType.parse("text/plain"), "" + filename);
        call = api.PostUpdateProfile(
                user_id,
                firstname,
                lastname,
                email,
                gender,
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
                        Toast.makeText(_act, "" + reason, Toast.LENGTH_SHORT).show();

                        JSONArray jsonArray = i.getJSONArray("user_data");
                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                        SharedPref.setPrefs(ProfileActivity.this, IConstant.USER_FIRST_NAME, jsonObject.getString("first_name"));
                        SharedPref.setPrefs(ProfileActivity.this, IConstant.USER_LAST_NAME, jsonObject.getString("last_name"));
                        SharedPref.setPrefs(ProfileActivity.this, IConstant.USER_EMAIL, jsonObject.getString("email"));
                        SharedPref.setPrefs(ProfileActivity.this, IConstant.USER_GENDER, jsonObject.getString("gender"));
                        SharedPref.setPrefs(ProfileActivity.this, IConstant.USER_PHOTO, i.getString("user_profile_path")+jsonObject.getString("image"));

                        Log.d("valueToaset", "initVariable after response: "+SharedPref.getPrefs(_act, IConstant.USER_FIRST_NAME));
                       /* Intent intent = new Intent(AddStatusActivity.this, IncidenceListActivity.class);
                        startActivity(intent);
                        finish();*/
                    } else
                        Toast.makeText(_act, "" + reason, Toast.LENGTH_SHORT).show();
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

}