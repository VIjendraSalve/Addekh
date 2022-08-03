package com.wht.addekho.Activties.InitialActivity;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wht.addekho.BaseActivity;
import com.wht.addekho.Constant.IConstant;
import com.wht.addekho.Helper.AppController;
import com.wht.addekho.Helper.ConnectionDetector;
import com.wht.addekho.Helper.Helper_Method;
import com.wht.addekho.Helper.SharedPref;
import com.wht.addekho.MainActivity;
import com.wht.addekho.R;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class SplashActivity extends BaseActivity {

    private Activity _act;
    String TAG = "SplashScreen";
    private ImageView imageView_splash;

    private Dialog dialog;
    private Button buttonUpdate, buttonExit;
    private String versionCode, versionName;
    private TextView tvVersion;
    private ProgressBar progress;
    public static boolean versionFlag;
    private ConnectionDetector connectionDetector;
    public static final int RequestPermissionCode = 1;

    //Notification
    private ArrayList<String> stringArrayListKeys = new ArrayList<>();
    private ArrayList<String> stringArrayListValues = new ArrayList<>();

    private String image = null, title = null, message1 = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (getIntent().getExtras() != null) {
            JSONObject obj = null;
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d("Splash: ", "Key: " + key + " Value: " + value);
                Log.d("Splash: ", "Values :" + getIntent().getExtras().get(key));

                if (key.equalsIgnoreCase("title")) {

                    title = (String) value;
                }
                if (key.equalsIgnoreCase("image")) {

                    image = (String) value;

                }
                if (key.equalsIgnoreCase("body")) {

                    message1 = (String) value;

                } else {

                }

                stringArrayListKeys.add(key);
                stringArrayListValues.add(key);
            }
            Log.d("Data", "Key: Size" + stringArrayListKeys.size());
            Log.d("Data", "Values: Size" + stringArrayListValues.size());

       /*     if (image != null && !image.isEmpty() && !image.equals("null") && !image.equals("")) {
                db.insert_notification(title, message1, image);
            } else {
                db.insert_notification(title, message1, null);
            }*/

        }


        _act = SplashActivity.this;
        connectionDetector = ConnectionDetector.getInstance(this);
        imageView_splash = findViewById(R.id.logo_image);
//        progress = findViewById(R.id.progress);

        DisplayMetrics metricscard = _act.getResources().getDisplayMetrics();
        int cardwidth = metricscard.widthPixels;
        int cardheight = metricscard.heightPixels;
        imageView_splash.getLayoutParams().height = (int) (cardheight / 5.5);
        imageView_splash.getLayoutParams().width = (int) (cardwidth / 2.5);


        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            versionCode = String.valueOf(pInfo.versionCode);
            versionName = String.valueOf(pInfo.versionName);
            Log.d(TAG, "onCreate: App Version Code : " + versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (AppController.isInternet(this)) {
//            if (checkPermission())
//            {

            final Thread timer = new Thread() {
                public void run() {
                    try {

                        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                        imageView_splash.startAnimation(animation2);
                        animation2.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                // Toast.makeText(SplashScreenActivity.this, "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {

                                deleteCache(_act);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
//                                Helper_Method.intentActivity_animate_fade(_act, MainActivity.class, true);
                                    if (SharedPref.getPrefs(_act, IConstant.USER_IS_LOGIN) != null && !SharedPref.getPrefs(_act, IConstant.USER_IS_LOGIN).isEmpty() && !SharedPref.getPrefs(_act, IConstant.USER_IS_LOGIN).equals("null") && !SharedPref.getPrefs(_act, IConstant.USER_IS_LOGIN).equals("")) {
                                        Helper_Method.intentActivity_animate_fade(_act, MainActivity.class, true);
                                    } else {
                                        Helper_Method.intentActivity_animate_fade(_act, LoginActivity.class, true);
                                    }

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                    } finally {
                    }
                }
            };
            timer.start();

            // }
//            else {
//                requestPermission();
//            }
        }

    }



    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

//    private void requestPermission() {
//
//        ActivityCompat.requestPermissions(SplashActivity.this, new String[]
//                {
//                        Manifest.permission.CAMERA,
//                        ACCESS_FINE_LOCATION,
//                        WRITE_EXTERNAL_STORAGE
//
//                }, RequestPermissionCode);
//
//
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case RequestPermissionCode:
//                if (grantResults.length > 0) {
//                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                    boolean ReadFineLoctionPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
//                    boolean WriteExternalStoragePermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
//                    if (CameraPermission && ReadFineLoctionPermission && /*ReadContactsPermission && ReadPhoneStatePermission && ReadAccountPermission && ReadSmsReadPermission &&*/ WriteExternalStoragePermission /*&& CallPhonePermission*/) {
//
//                        Thread timer = new Thread() {
//                            public void run() {
//                                try {
//
//                                    Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
//
//                                    animation2.setAnimationListener(new Animation.AnimationListener() {
//                                        @Override
//                                        public void onAnimationStart(Animation animation) {
//                                            Toast.makeText(SplashActivity.this, "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();
//                                        }
//
//                                        @Override
//                                        public void onAnimationEnd(Animation animation) {
//
//                                            deleteCache(SplashActivity.this);
//                                        }
//
//                                        @Override
//                                        public void onAnimationRepeat(Animation animation) {
//
//                                        }
//                                    });
//
//                                } finally {
//                                    Helper_Method.intentActivity_animate_fade(_act, MainActivity.class, true);
//
//                                }
//
//
//
//                            }
//                        };
//                        timer.start();
//
//                    } else {
//                        Toast.makeText(SplashActivity.this, "Permission has been Denied", Toast.LENGTH_LONG).show();
//                    }
//                }
//                break;
//        }
//    }

    public boolean checkPermission() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);

        int SixthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);


        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SixthPermissionResult == PackageManager.PERMISSION_GRANTED;
    }



}

