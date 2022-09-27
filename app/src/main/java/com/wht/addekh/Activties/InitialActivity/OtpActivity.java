package com.wht.addekh.Activties.InitialActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.wht.addekh.BaseActivity;
import com.wht.addekh.Constant.IConstant;
import com.wht.addekh.Constant.IUrls;
import com.wht.addekh.Constant.Interface;
import com.wht.addekh.Helper.ConnectionDetector;
import com.wht.addekh.Helper.Helper_Method;
import com.wht.addekh.Helper.SharedPref;
import com.wht.addekh.Helper.Validations;
import com.wht.addekh.MainActivity;
import com.wht.addekh.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OtpActivity extends BaseActivity {
    private Activity _act;
    private TextView tvTimer;
    private Validations validations;
    private SharedPref sharedPref;
    private ConnectionDetector connectionDetector;
    private OtpView otpView;
    EditText edt_Mobile_number;
    private TextView btnSubmit, tvNote, tvResend;
    private String otpNo,strlat = null,strlang = null,device;
    private boolean flagOtpComplete = false;
    private String strMobile, stringUserToken = null, user_profile_path = null;

    TextView otp,tv_Resend_otp;
    EditText otp_box_1,otp_box_2,otp_box_3,otp_box_4,otp_box_5,otp_box_6;

    Button button_verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        strMobile = getIntent().getStringExtra("contact_number");
        otpView = findViewById(R.id.otp_view);
        tv_Resend_otp = findViewById(R.id.tv_Resend_otp);
        edt_Mobile_number = findViewById(R.id.edt_Mobile_number);
        edt_Mobile_number.setText(""+strMobile);
        otpView.requestFocus();

        _act = OtpActivity.this;
        validations = new Validations();
        connectionDetector = ConnectionDetector.getInstance(_act);

        button_verify = findViewById(R.id.verify);
        tvTimer = findViewById(R.id.tvTimer);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            //Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        stringUserToken = task.getResult().getToken();
                        Log.d("Notification_Token1", "onComplete: " + stringUserToken);

                    }
                });


        tv_Resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webCallResendOTP();
            }
        });
        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                Log.d("onOtpCompleted", otp);
                otpNo = otp;
                flagOtpComplete = true;
            }
        });
        button_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
                if (connectionDetector.checkConnection(_act)) {

                    if (flagOtpComplete) {
                        verifyOTP();
                    } else {
                        Helper_Method.toaster(_act, "Enter OTP");
                    }

                } else {
                    Helper_Method.toaster_long(_act, getResources().getString(R.string.string_internet_connection_warning));

                }
            }
        });



    }

    private void webCallResendOTP() {

        Helper_Method.showProgressBar(_act, "Resending OTP...");
        Interface api = IUrls.getRetrofit(IUrls.BASE_URL).create(Interface.class);
        Call<ResponseBody> result = api.PostResendOtp(
                edt_Mobile_number.getText().toString().trim()
        );


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
                        if (stringCode.equalsIgnoreCase("true")) {

                            Timer();
                            Helper_Method.toaster(_act, stringMsg);
                            Helper_Method.dismissProgessBar();

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

    private void verifyOTP() {

        Helper_Method.showProgressBar(_act, "Verifying...");
        Interface api = IUrls.getRetrofit(IUrls.BASE_URL).create(Interface.class);
        Call<ResponseBody> result = api.PostOtpVerify(
                edt_Mobile_number.getText().toString().trim(),
                otpNo,
                stringUserToken,device,strlat,strlang);


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
                        if (stringCode.equalsIgnoreCase("true")) {


                            Helper_Method.hideSoftInput(_act);
                            Helper_Method.dismissProgessBar();
                            Helper_Method.toaster(_act, stringMsg);

                            user_profile_path = i.getString("user_profile_path");

                            JSONArray jsonArray = i.getJSONArray("user_data");

                            JSONObject jsonObjectData = jsonArray.getJSONObject(0);
                            String id = jsonObjectData.getString("id");
                            String full_name = jsonObjectData.getString("first_name");
                            String last_name = jsonObjectData.getString("last_name");
                            String email = jsonObjectData.getString("email");
                            String mobile_no = jsonObjectData.getString("mobile_no");
                            String image = jsonObjectData.getString("image");

                            SharedPref.setPrefs(_act, IConstant.USER_ID, id);
                            SharedPref.setPrefs(_act, IConstant.USER_FIRST_NAME, full_name);
                            SharedPref.setPrefs(_act, IConstant.USER_LAST_NAME, last_name);
                            SharedPref.setPrefs(_act, IConstant.USER_EMAIL, email);
                            SharedPref.setPrefs(_act, IConstant.USER_MOBILE, mobile_no);
                            SharedPref.setPrefs(_act, IConstant.USER_PHOTO, user_profile_path + image);

                            SharedPref.setPrefs(_act, IConstant.USER_IS_LOGIN, "true");
                                Helper_Method.intentActivity(_act, MainActivity.class, true);
                                finish();
//                            Intent returnIntent = new Intent();
//                            setResult(Activity.RESULT_OK, returnIntent);
//                            finish();


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

    public void Timer() {
        tvTimer.setVisibility(View.VISIBLE);
        tv_Resend_otp.setVisibility(View.GONE);
        new CountDownTimer(3000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                tvTimer.setText("0:" + String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                tvTimer.setText("0:0");
                //Animation shake = AnimationUtils.loadAnimation(_act, R.anim.shake);
                //tvNote.startAnimation(shake);
                //tvResend.startAnimation(shake);
                //tvNote.setVisibility(View.VISIBLE);
                tv_Resend_otp.setVisibility(View.VISIBLE);

            }
        }.start();
    }
}