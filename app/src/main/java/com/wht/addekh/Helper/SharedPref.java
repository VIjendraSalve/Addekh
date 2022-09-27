package com.wht.addekh.Helper;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPref {


    static String prefName = "MyJobPreferences";


    static SharedPreferences preferences;
    static SharedPreferences.Editor editor;

    public static void setPrefs(Context context, String prefKey, String prefValue) {
        preferences = context.getSharedPreferences(prefName, context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString(prefKey, prefValue);
        editor.commit();
    }

    public static String getPrefs(Context context, String prefKey) {
        preferences = context.getSharedPreferences(prefName, context.MODE_PRIVATE);
        return preferences.getString(prefKey, null);
    }

    public static void clearPref(Context context) {
        preferences = context.getSharedPreferences(prefName, context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.clear().commit();
    }


    public static void clearPref1(Context context, String prefKey) {
        preferences = context.getSharedPreferences(prefName, context.MODE_PRIVATE);
        preferences.edit().remove(prefKey).commit();
        //editor = preferences.edit();
        //editor.clear().commit();
    }

    /*public static void saveSharedPrefs(Context context, DriverPOJO userPojo) {
        preferences = context.getSharedPreferences(prefName, context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(SP_REG_ID, userPojo.getRegId());
        editor.putString(SP_OWNER_ID, userPojo.getProfOwnerId());
        editor.putString(EMP_ID, userPojo.getProfEmpId());
        editor.putString(SP_FIRST_NAME, userPojo.getProfFirstName());
        editor.putString(SP_LAST_NAME, userPojo.getProfLastName());
        editor.putString(SP_MOBILE_NO, userPojo.getRegMobile());
        editor.putString(SP_EMAIL, userPojo.getRegEmail());
        editor.putString(SP_GENDER, userPojo.getProfGender());
        editor.putString(SP_DOB, userPojo.getProfDob());
        editor.putString(SP_PROFILE_IMAGE, userPojo.getProfPic());
        editor.putString(SP_OP_FNAME, userPojo.getOpFirstName());
        editor.putString(SP_OP_LNAME, userPojo.getOpLastName());
        editor.putString(SP_OP_PROFILE, userPojo.getOpProfileImage());
        editor.putString(SP_PRESENT_ADDRESS, userPojo.getProfPresentAddress());
        editor.putString(SP_PRESENT_ADDRESS_IMG, userPojo.getProfAddressproofCurImage());
        editor.putString(SP_PERMANENT_ADDRESS, userPojo.getProfPermanentAddress());
        editor.putString(SP_PERMANENT_ADDRESS_IMG, userPojo.getProfAddressproofPerImage());
        editor.putString(SP_LIC_NO, userPojo.getProfDrivingTrLicenceNo());
        editor.putString(SP_LIC_IMG, userPojo.getProfTrLicenceImage());
        editor.putString(SP_LIC_EXP_DATE, userPojo.getProfDrivingLicenceExpiryDate());
        editor.putString(SP_BADGE_NO, userPojo.getProfBadgeNumber());
        editor.putString(SP_PINCODE, "" + userPojo.getProf_pincode());


        editor.apply();
    }*/
}
