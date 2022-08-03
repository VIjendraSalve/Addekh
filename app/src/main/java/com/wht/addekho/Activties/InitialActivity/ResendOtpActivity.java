package com.wht.addekho.Activties.InitialActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.wht.addekho.BaseActivity;
import com.wht.addekho.Constant.IConstant;
import com.wht.addekho.Constant.IUrls;
import com.wht.addekho.Constant.Interface;
import com.wht.addekho.Helper.ConnectionDetector;
import com.wht.addekho.Helper.Helper_Method;
import com.wht.addekho.Helper.SharedPref;
import com.wht.addekho.Helper.Validations;
import com.wht.addekho.MainActivity;
import com.wht.addekho.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResendOtpActivity extends BaseActivity {
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

    TextView otp;
    EditText otp_box_1,otp_box_2,otp_box_3,otp_box_4,otp_box_5,otp_box_6;

    Button button_verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_resend_otp);

        strMobile = getIntent().getStringExtra("contact_number");
        otpView = findViewById(R.id.otp_view);
        edt_Mobile_number = findViewById(R.id.edt_Mobile_number);
        otpView.requestFocus();

        _act = ResendOtpActivity.this;
        validations = new Validations();
        connectionDetector = ConnectionDetector.getInstance(_act);

        button_verify = findViewById(R.id.verify);
        tvTimer = findViewById(R.id.tvTimer);
//        Timer();

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

    private void verifyOTP() {

        Helper_Method.showProgressBar(_act, "Verifying...");
        Interface api = IUrls.getRetrofit(IUrls.BASE_URL).create(Interface.class);
        Call<ResponseBody> result = api.PostOtpVerify(edt_Mobile_number.getText().toString().trim(), otpNo,stringUserToken,device,strlat,strlang);


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

                            Helper_Method.toaster(_act, stringMsg);
                            Helper_Method.dismissProgessBar();

                            Intent intent = new Intent(_act, LoginActivity.class);
                            intent.putExtra("contact_number",otpNo);
                            startActivityForResult(intent, 100);


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

//    public void Timer() {
//        tvTimer.setVisibility(View.VISIBLE);
//        new CountDownTimer(60000, 1000) { // adjust the milli seconds here
//
//            public void onTick(long millisUntilFinished) {
//                tvTimer.setText("0:" + String.valueOf(millisUntilFinished / 1000));
//            }
//
//            public void onFinish() {
//                tvTimer.setText("0:0");
//                Animation shake = AnimationUtils.loadAnimation(_act, R.anim.shake);
//                tvNote.startAnimation(shake);
//                tvResend.startAnimation(shake);
//                tvNote.setVisibility(View.VISIBLE);
//                tvResend.setVisibility(View.VISIBLE);
//
//            }
//        }.start();
//    }
}