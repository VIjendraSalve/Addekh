package com.wht.addekho.Activties.InitialActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.wht.addekho.BaseActivity;
import com.wht.addekho.Constant.IUrls;
import com.wht.addekho.Constant.Interface;
import com.wht.addekho.Helper.ConnectionDetector;
import com.wht.addekho.Helper.Helper_Method;
import com.wht.addekho.Helper.Validations;
import com.wht.addekho.MainActivity;
import com.wht.addekho.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    TextView otp;
    Button btn_Generate_otp;
    EditText edt_Mobile_number;
    private Activity _act;
    private Validations validations;
    private ConnectionDetector connectionDetector;
    private boolean doubleBackToExitPressedOnce = false;
    Button btn_Login;
    private String stringUserToken = "None", user_profile_path;
    private TextView tvNewUser;
    private String deviceInformation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        _act = LoginActivity.this;
        validations = new Validations();
        connectionDetector = ConnectionDetector.getInstance(_act);

        otp = findViewById(R.id.otp);
        btn_Generate_otp = findViewById(R.id.btn_Generate_otp);
        edt_Mobile_number = findViewById(R.id.edt_Mobile_number);
        otp.setText(Html.fromHtml(getResources().getString(R.string.otp)));
        btn_Generate_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(edt_Mobile_number.getText().toString().equals(""))
//                    Toast.makeText(getApplicationContext(),"Please enter the mobile no.",Toast.LENGTH_SHORT).show();
//                else if(edt_Mobile_number.getText().length()<10)
//                    Toast.makeText(getApplicationContext(),"Please enter correct mobile no.",Toast.LENGTH_SHORT).show();
//                else{
//                    Intent intent = new Intent(getApplicationContext(),OtpActivity.class);
//                    startActivity(intent);
//                }

                if (connectionDetector.checkConnection(_act)) {
                    if (isValid()) {
                        Helper_Method.hideSoftInput(_act);
                        userLogin();
                    }

                }else {
                    Helper_Method.toaster_long(_act, getResources().getString(R.string.string_internet_connection_warning));
                }

            }
        });

    }

    private void userLogin() {
        Helper_Method.showProgressBar(_act, "LOGIN...");
        Interface api = IUrls.getRetrofit(IUrls.BASE_URL).create(Interface.class);
        Call<ResponseBody> result = api.PostLogin(edt_Mobile_number.getText().toString().trim());

        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {
                    output = response.body().string();
                    Log.d("my_tag", "onResponse: " + output);
                    try {
                        JSONObject i = new JSONObject(output);
                        String stringCode = i.getString("result");
                        String stringMsg = i.getString("reason");


                        if (stringCode.equalsIgnoreCase("true"))
                        {
                            Helper_Method.toaster(_act, stringMsg);
                            Helper_Method.dismissProgessBar();

                            Intent intent = new Intent(_act, OtpActivity.class);
                            intent.putExtra("contact_number",edt_Mobile_number.getText().toString().trim());
                            startActivity(intent);
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
                // Toast.makeText(_act, "", Toast.LENGTH_SHORT).show();
                Log.d("Issue", "Technical Error");
                Helper_Method.dismissProgessBar();

            }
        });
    }

    private boolean isValid() {
        if (!validations.isValidPhone(edt_Mobile_number)) {
            if(edt_Mobile_number.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(),"Please enter the mobile no.",Toast.LENGTH_SHORT).show();
                else if(edt_Mobile_number.getText().length()<10)
//                    Toast.makeText(getApplicationContext(),"Please enter correct mobile no.",Toast.LENGTH_SHORT).show();etError(getResources().getString(R.string.error_invalid_mobile));
            return false;
        }
        if (validations.isBlank(edt_Mobile_number)) {

            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            edt_Mobile_number.startAnimation(shake);
            edt_Mobile_number.setError(getResources().getString(R.string.error_field_required));
            return false;
        }

        return true;

    }
}