package com.wht.addekh.Helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationResult;
import com.wht.addekh.Constant.IConstant;

import okhttp3.ResponseBody;
import retrofit2.Call;


public class MyLocationService extends BroadcastReceiver {
    public static final String ACTION_PROCESS_UPDATE = "com.wht.addekho.backgroundlocationupdate.UPDATE_LOCATION";

    private Call<ResponseBody> result;
    //private UpdateLocationAPI api;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if(ACTION_PROCESS_UPDATE.equals(action)){

                LocationResult result = LocationResult.extractResult(intent);
                if (result != null){
                    Location location = result.getLastLocation();
                    String LocationString = "Latitude: "+location.getLatitude()+"\nLongitude: "+location.getLongitude();
                    try {

                        SharedPref.setPrefs(context, IConstant.MyCurrentLatitude,String.valueOf(location.getLatitude()));
                        SharedPref.setPrefs(context, IConstant.MyCurrentLongitude,String.valueOf(location.getLongitude()));
                        //MainActivity.getInstance().updateTextVIew(LocationString);
                        //Toast.makeText(context, "App Background\n"+LocationString, Toast.LENGTH_SHORT).show();
                        Log.d("locationDetails", "onReceive: "+LocationString);


                    }catch (Exception ex){
                        //Toast.makeText(context, "App Killed\n"+LocationString, Toast.LENGTH_SHORT).show();
                        Log.d("LocationUpdate: Killed", "onReceive: "+LocationString);
                    }
                }
            }
        }
    }

    /*private void updateVehicleLocation(final Context context, Location location) {


        Map<String,String> param = new HashMap<>();
        param.put("user_name", "" +Shared_Preferences.getPrefs(context, Constants.FULL_NAME));
        param.put("lat",String.valueOf(location.getLatitude()));
        param.put("lng",String.valueOf(location.getLongitude()));
        param.put("user_id",Shared_Preferences.getPrefs(context, Constants.UserID));
        param.put("awb_number","");
        param.put("is_delivered","");

        Log.d("Parameters: ", "updateVehicleLocation: "+param);
        if (api == null)
            api = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL_WIND_HANS).create(UpdateLocationAPI.class);


            result = api.updateLocation(param);



        Callback<ResponseBody> callback = new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";

                try {
                    output = response.body().string();
                    Log.d("my_tag", "onResponse:Service " + output);
                    JSONObject jsonObject = new JSONObject(output);



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("my_tag", "Retrofit Error " + t.getMessage());
            }
        };
        result.clone().enqueue(callback);
    }*/

    /*private interface UpdateLocationAPI {
        @FormUrlEncoded
        @POST(MyConfig.JSON_SUB_URL_WIND_HANS + "/app_add_location")
        public Call<ResponseBody> updateLocation(
                @FieldMap Map<String, String> params);
    }*/


}
