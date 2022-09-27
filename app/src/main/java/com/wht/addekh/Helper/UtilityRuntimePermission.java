package com.wht.addekh.Helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.wht.addekh.BaseActivity;
import com.wht.addekh.R;


/**
 * Created by MG on 03-04-2016.
 */

public abstract class UtilityRuntimePermission extends BaseActivity {
    //private SparseIntArray mErrorString;
    private boolean result = false;
    int permissionCheck;
    public final String[] requestedPermissions = new String[]{
            /*Manifest.permission.GET_ACCOUNTS,*/
           /* Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA,*/
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
           /* Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE*/
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // mErrorString = new SparseIntArray();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permission : grantResults) {
            permissionCheck = permissionCheck + permission;
        }
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if ((grantResults.length > 0) && permissionCheck == PackageManager.PERMISSION_GRANTED) {
            result = true;
            //onPermissionsGranted(result);
        } else if (grantResults.length > 0 && currentAPIVersion >= Build.VERSION_CODES.M) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle("Permission necessary");
            alertBuilder.setMessage(R.string.runtime_permissions_txt);
            alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    //intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setData(Uri.parse("package_new:" + getPackageName()));
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    startActivityForResult(intent, 12);
                    dialog.dismiss();

/*ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.ACCESS_FINE_LOCATION}
                                //Manifest.permission.GET_ACCOUNTS,Manifest.permission.READ_EXTERNAL_STORAGE,
                                //Manifest.permission.ACCESS_FINE_LOCATION}
                                , REQUEST_ID_MULTIPLE_PERMISSIONS);*/

                }
            });
            AlertDialog alert = alertBuilder.create();
            alert.show();


/*
Snackbar.make(findViewById(android.R.id.content), mErrorString.get(requestCode),
                    Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.setData(Uri.parse("package_new:" + getPackageName()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            startActivity(intent);
                        }
                    }).show();
*/

        }
    }

    public abstract void processFinish(String result, int img_no);

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 12) {
            permissionCheck = PackageManager.PERMISSION_GRANTED;
            for (String permission : requestedPermissions) {
                permissionCheck = permissionCheck + ContextCompat.checkSelfPermission((Activity) this, permission);
            }
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                //onPermissionsGranted(true);
            }
        }
    }

    public boolean requestAppPermissions(Context context) {


//final int stringId= R.string.runtime_permissions_txt;
        final int requestCode = 123;
//        mErrorString.put(requestCode, stringId);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        boolean shouldShowRequestPermissionRationale = false;
        for (String permission : requestedPermissions) {
            //Log.d("mytag", "requestAppPermissions: "+ ContextCompat.checkSelfPermission((Activity)context, permission));
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission((Activity) context, permission);
            shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale || ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission);
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale) {
                ActivityCompat.requestPermissions(this, requestedPermissions, requestCode);


/*Snackbar.make(findViewById(android.R.id.content), stringId,
                        Snackbar.LENGTH_INDEFINITE).setAction("GRANT",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(UtilityRuntimePermission.this, requestedPermissions, requestCode);
                            }
                        }).show();*/

            } else {
                ActivityCompat.requestPermissions(this, requestedPermissions, requestCode);
            }
        } else {

            result = true;
        }
        //onPermissionsGranted(result);
        return result;
    }

    //public abstract void onPermissionsGranted(boolean result);

    //public abstract void onPermissionsGranted(int requestCode);
}

