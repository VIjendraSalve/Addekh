package com.wht.addekho.Helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import com.wht.addekho.Activties.InitialActivity.SplashActivity;


public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();
    //todo, progressbar
    public static volatile Context mMainContext;
    public static volatile Handler mMainHandler;
    private static AppController mInstance;


    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public static boolean isInternet(final Activity context)
    {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("OOP'S Connection Error");
            builder.setCancelable(false);
            builder.setMessage(" Please check your internet connection!");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    context.startActivity(new Intent(context, SplashActivity.class));
                    context.finish();
                }
            });
            builder.show();
            return false;
        }

        return true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

       /* OneSignal.startInit(this)
                .autoPromptLocation(false) // default call promptLocation later
                .setNotificationOpenedHandler(new MyNotificationOpenedHandler())
                .setNotificationReceivedHandler(new MyNotificationReceivedHandler())
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .init();*/
    }

    public static Context getContext() {
        return mMainContext;
    }
}
