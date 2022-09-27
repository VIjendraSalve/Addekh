package com.wht.addekh.Activties.Template;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.wht.addekh.BaseActivity;
import com.wht.addekh.Constant.IConstant;
import com.wht.addekh.Constant.IUrls;
import com.wht.addekh.Constant.Interface;
import com.wht.addekh.Helper.ConnectionDetector;
import com.wht.addekh.Helper.Helper_Method;
import com.wht.addekh.Helper.MyLocationService;
import com.wht.addekh.Helper.OnGPS;
import com.wht.addekh.Helper.SharedPref;
import com.wht.addekh.MainActivity;
import com.wht.addekh.Model.TemplateData;
import com.wht.addekh.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTemplateFieldsActivity extends BaseActivity {

    public static Activity _act;
    private ConnectionDetector connectionDetector;

    private LinearLayout ll_offer_name, ll_offer_amount, ll_offer_validity_from, ll_offer_desc, ll_offer_type,
            ll_offer_validity_to, ll_offer_terms, ll_offer_code;
    private EditText edt_offer_name, edt_offer_amount, edt_offer_validity_from, edt_offer_desc, edt_offer_type,
            edt_offer_validity_to, edt_offer_terms, edt_offer_code;
    private ArrayList<TemplateData> templateDataArrayList = new ArrayList<>();
    private int position = 0;

    private String flag_offer_name = "0", flag_offer_amount = "0", flag_offer_validity_from = "0", flag_offer_desc = "0", flag_offer_type = "0",
            flag_offer_validity_to = "0", flag_offer_terms = "0", flag_offer_code = "0";

    private ProgressDialog progressDialog;
    private Button btn_submit;

    private SimpleDateFormat df, ymd;
    private String formattedDate;
    private DatePickerDialog datePickerDialogBirthDate;
    private String strBirthDate = "Select Birth Date", strBirthTime = "Select Birth Time";
    private Calendar currentCal;
    private String BusinessName = "", Email = "", Mobile = "", templateID = "";
    private JSONArray js;
    private String action = "", category_id = "", advt_title = "", advt_desc = "", tags = "", last_date = "", store_id = "";

    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;
    private OnGPS onGPS = new OnGPS();
    private String lat, lng;

    Location gps_loc;
    Location network_loc;
    Location final_loc;
    double longitude;
    double latitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_template_fields);

        _act = AddTemplateFieldsActivity.this;
        templateDataArrayList = getIntent().getParcelableArrayListExtra("List");
        position = getIntent().getIntExtra("position", 0);

        BusinessName = getIntent().getStringExtra("BusinessName");
        Email = getIntent().getStringExtra("Email");
        Mobile = getIntent().getStringExtra("Mobile");
        templateID = getIntent().getStringExtra("TemplateID");

        action = getIntent().getStringExtra("action");
        category_id = getIntent().getStringExtra("category_id");
        advt_title = getIntent().getStringExtra("advt_title");
        advt_desc = getIntent().getStringExtra("advt_desc");
        tags = getIntent().getStringExtra("tags");
        last_date = getIntent().getStringExtra("last_date");
        store_id = getIntent().getStringExtra("store_id");

        connectionDetector = ConnectionDetector.getInstance(_act);
        Helper_Method.hideSoftInput(_act);

        progressDialog = new ProgressDialog(AddTemplateFieldsActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(true);

        Log.d("Size", "templateDataArrayList: " + templateDataArrayList.size());
        Log.d("Size", "position: " + position);
        Log.d("Size", "getTemplate_name: " + templateDataArrayList.get(position).getTemplate_name());
        Log.d("Size", "getField_name: " + templateDataArrayList.get(position).getTemplateFieldsArrayList());

        findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initFunction();

    }

    /********************* Background Location Update**************************/
    private void updateLocation() {
        buildLocationRequest();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, getPendingIntent());
        Log.d("FusedLocation", "updateLocation: " + fusedLocationProviderClient.getLastLocation());

    }

    private PendingIntent getPendingIntent() {
        Log.d("MYTAG", "getPendingIntent: ");
        Intent intent = new Intent(this, MyLocationService.class);
        intent.setAction(MyLocationService.ACTION_PROCESS_UPDATE);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
    }

    private void buildLocationRequest() {

        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(18000);
        locationRequest.setFastestInterval(18000);
        locationRequest.setSmallestDisplacement(10f);

        Log.d("LocationDetails", "buildLocationRequest: " + locationRequest.toString());

    }

    /********************* Background Location Update**************************/

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void initFunction() {

        if (onGPS.IsGpsOn(AddTemplateFieldsActivity.this)) {
            updateLocation();
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsStatus) {
            Toast.makeText(_act, "gps off", Toast.LENGTH_SHORT).show();
            statusCheck();
        }

        if (onGPS.IsGpsOn(AddTemplateFieldsActivity.this)) {
            updateLocation();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        network_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (gps_loc != null) {
            final_loc = gps_loc;
            latitude = final_loc.getLatitude();
            longitude = final_loc.getLongitude();
        } else if (network_loc != null) {
            final_loc = network_loc;
            latitude = final_loc.getLatitude();
            longitude = final_loc.getLongitude();
        } else {
            latitude = 0.0;
            longitude = 0.0;
        }
        Log.d("locationLat", "initFunction: "+latitude);
        Log.d("locationLat", "initFunction: "+longitude);


        ll_offer_name = findViewById(R.id.ll_offer_name);
        ll_offer_amount = findViewById(R.id.ll_offer_amount);
        ll_offer_validity_from = findViewById(R.id.ll_offer_validity_from);
        ll_offer_desc = findViewById(R.id.ll_offer_desc);
        ll_offer_type = findViewById(R.id.ll_offer_type);
        ll_offer_validity_to = findViewById(R.id.ll_offer_validity_to);
        ll_offer_terms = findViewById(R.id.ll_offer_terms);
        ll_offer_code = findViewById(R.id.ll_offer_code);
        btn_submit = findViewById(R.id.btn_submit);

        edt_offer_name = findViewById(R.id.edt_offer_name);
        edt_offer_amount = findViewById(R.id.edt_offer_amount);
        edt_offer_validity_from = findViewById(R.id.edt_offer_validity_from);
        edt_offer_desc = findViewById(R.id.edt_offer_desc);
        edt_offer_type = findViewById(R.id.edt_offer_type);
        edt_offer_validity_to = findViewById(R.id.edt_offer_validity_to);
        edt_offer_terms = findViewById(R.id.edt_offer_terms);
        edt_offer_code = findViewById(R.id.edt_offer_code);

        currentCal = Calendar.getInstance();
        df = new SimpleDateFormat("dd MMM yyyy");
        ymd = new SimpleDateFormat("yyyy-MM-dd");

        edt_offer_validity_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialogBirthDate = new DatePickerDialog(_act,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                Date date = new Date(year - 1900, monthOfYear, dayOfMonth);
                                // SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                                formattedDate = df.format(date);
                                strBirthDate = ymd.format(date);
                                edt_offer_validity_from.setText(formattedDate);
                                //refreshData();

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialogBirthDate.show();
                //datePickerDialogBirthDate.getDatePicker().setMinDate(System.currentTimeMillis());
                //  datePickerDialogBirthDate.getDatePicker().setMaxDate(System.currentTimeMillis());
                //datePickerDialogBirthDate.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                //datePickerDialogBirthDate.getDatePicker().setMinDate(System.currentTimeMillis() - (5 * 24 * 60 * 60 * 1000));
                //datePickerDialogBirthDate.getDatePicker().setMaxDate(System.currentTimeMillis() + (1 * 24 * 60 * 60 * 1000));
                //datePickerDialogBirthDate.getDatePicker().setMaxDate(System.currentTimeMillis() - 568025136000L);

            }
        });

        edt_offer_validity_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialogBirthDate = new DatePickerDialog(_act,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                Date date = new Date(year - 1900, monthOfYear, dayOfMonth);
                                // SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                                formattedDate = df.format(date);
                                strBirthDate = ymd.format(date);
                                edt_offer_validity_to.setText(formattedDate);
                                //refreshData();

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialogBirthDate.show();
                //datePickerDialogBirthDate.getDatePicker().setMinDate(System.currentTimeMillis());
                //  datePickerDialogBirthDate.getDatePicker().setMaxDate(System.currentTimeMillis());
                //datePickerDialogBirthDate.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                //datePickerDialogBirthDate.getDatePicker().setMinDate(System.currentTimeMillis() - (5 * 24 * 60 * 60 * 1000));
                //datePickerDialogBirthDate.getDatePicker().setMaxDate(System.currentTimeMillis() + (1 * 24 * 60 * 60 * 1000));
                //datePickerDialogBirthDate.getDatePicker().setMaxDate(System.currentTimeMillis() - 568025136000L);

            }
        });

        Toast.makeText(_act, "" + templateDataArrayList.get(position).getTemplateFieldsArrayList().size(), Toast.LENGTH_SHORT).show();

        for (int i = 0; i < templateDataArrayList.get(position).getTemplateFieldsArrayList().size(); i++) {
            if (templateDataArrayList.get(position).getTemplateFieldsArrayList().get(i).getField_name().equals("offer_name")) {
                ll_offer_name.setVisibility(View.VISIBLE);
                flag_offer_name = "1";
                break;
            } else {
                ll_offer_name.setVisibility(View.GONE);
            }

        }

        for (int i = 0; i < templateDataArrayList.get(position).getTemplateFieldsArrayList().size(); i++) {
            if (templateDataArrayList.get(position).getTemplateFieldsArrayList().get(i).getField_name().equals("offer_amount")) {
                ll_offer_amount.setVisibility(View.VISIBLE);
                flag_offer_amount = "1";
                break;
            } else {
                ll_offer_amount.setVisibility(View.GONE);
            }

        }

        for (int i = 0; i < templateDataArrayList.get(position).getTemplateFieldsArrayList().size(); i++) {
            if (templateDataArrayList.get(position).getTemplateFieldsArrayList().get(i).getField_name().equals("offer_valid_from")) {
                ll_offer_validity_from.setVisibility(View.VISIBLE);
                flag_offer_validity_from = "1";
                break;
            } else {
                ll_offer_validity_from.setVisibility(View.GONE);
            }

        }

        for (int i = 0; i < templateDataArrayList.get(position).getTemplateFieldsArrayList().size(); i++) {
            if (templateDataArrayList.get(position).getTemplateFieldsArrayList().get(i).getField_name().equals("offer_valid_to")) {
                ll_offer_validity_to.setVisibility(View.VISIBLE);
                flag_offer_validity_to = "1";
                break;
            } else {
                ll_offer_validity_to.setVisibility(View.GONE);
            }

        }

        for (int i = 0; i < templateDataArrayList.get(position).getTemplateFieldsArrayList().size(); i++) {
            if (templateDataArrayList.get(position).getTemplateFieldsArrayList().get(i).getField_name().equals("offer_desc")) {
                ll_offer_desc.setVisibility(View.VISIBLE);
                flag_offer_desc = "1";
                break;
            } else {
                ll_offer_desc.setVisibility(View.GONE);
            }
        }

        for (int i = 0; i < templateDataArrayList.get(position).getTemplateFieldsArrayList().size(); i++) {
            if (templateDataArrayList.get(position).getTemplateFieldsArrayList().get(i).getField_name().equals("offer_code")) {
                ll_offer_code.setVisibility(View.VISIBLE);
                flag_offer_code = "1";
                break;
            } else {
                ll_offer_code.setVisibility(View.GONE);
            }
        }

        for (int i = 0; i < templateDataArrayList.get(position).getTemplateFieldsArrayList().size(); i++) {
            if (templateDataArrayList.get(position).getTemplateFieldsArrayList().get(i).getField_name().equals("offer_terms")) {
                ll_offer_terms.setVisibility(View.VISIBLE);
                flag_offer_terms = "1";
                break;
            } else {
                ll_offer_terms.setVisibility(View.GONE);
            }
        }

        for (int i = 0; i < templateDataArrayList.get(position).getTemplateFieldsArrayList().size(); i++) {
            if (templateDataArrayList.get(position).getTemplateFieldsArrayList().get(i).getField_name().equals("offer_type")) {
                ll_offer_type.setVisibility(View.VISIBLE);
                flag_offer_type = "1";
                break;
            } else {
                ll_offer_type.setVisibility(View.GONE);
            }
        }


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject obj = new JSONObject();
                try {

                    if (flag_offer_name.equals("1")) {
                        obj.put("offer_name", edt_offer_name.getText().toString());
                    }  if (flag_offer_code.equals("1")) {
                        obj.put("offer_code", edt_offer_code.getText().toString());
                    }  if (flag_offer_amount.equals("1")) {
                        obj.put("offer_amount", edt_offer_amount.getText().toString());
                    }  if (flag_offer_validity_from.equals("1")) {
                        obj.put("offer_valid_from", edt_offer_validity_from.getText().toString());
                    }  if (flag_offer_validity_to.equals("1")) {
                        obj.put("offer_valid_to", edt_offer_validity_to.getText().toString());
                    }  if (flag_offer_type.equals("1")) {
                        obj.put("offer_type", edt_offer_type.getText().toString());
                    }  if (flag_offer_terms.equals("1")) {
                        obj.put("offer_terms", edt_offer_terms.getText().toString());
                    }   if (flag_offer_desc.equals("1")) {
                        obj.put("offer_desc", edt_offer_desc.getText().toString());
                    }

                    js = new JSONArray();
                    js.put(obj);
                    Log.d("Input", "onClick1: "+js.toString());

                    webCallSumbitTemplateField(js);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    private void webCallSumbitTemplateField(JSONArray js) {
        Helper_Method.showProgressBar(_act, "Submitting...");
        Interface api = IUrls.getRetrofit(IUrls.BASE_URL).create(Interface.class);


        RequestBody actionValue = RequestBody.create(MediaType.parse("text/plain"), action);
        RequestBody catergotyValue = RequestBody.create(MediaType.parse("text/plain"), category_id);
        RequestBody advtTitleValue = RequestBody.create(MediaType.parse("text/plain"), advt_title);
        RequestBody lat = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(latitude));
        RequestBody lng = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(longitude));
        RequestBody advtdesc = RequestBody.create(MediaType.parse("text/plain"), advt_desc);
        RequestBody tagsValue = RequestBody.create(MediaType.parse("text/plain"), tags);
        RequestBody lastdateValue = RequestBody.create(MediaType.parse("text/plain"), last_date);
        RequestBody store_idValue = RequestBody.create(MediaType.parse("text/plain"), store_id);
        RequestBody temp_fieldsValue = RequestBody.create(MediaType.parse("text/plain"), js.toString());
        RequestBody user_idValue = RequestBody.create(MediaType.parse("text/plain"), SharedPref.getPrefs(_act, IConstant.USER_ID));
        RequestBody template_idValue = RequestBody.create(MediaType.parse("text/plain"), templateID);
        RequestBody businessnameValue = RequestBody.create(MediaType.parse("text/plain"), BusinessName);
        RequestBody emailValue = RequestBody.create(MediaType.parse("text/plain"), Email);
        RequestBody mobileValue = RequestBody.create(MediaType.parse("text/plain"), Mobile);

        Call<ResponseBody> result = api.PostAddTemplateFields(
                actionValue,
                catergotyValue,
                advtTitleValue,
                lat,
                lng,
                advtdesc,
                tagsValue,
                lastdateValue,
                store_idValue,
                temp_fieldsValue,
                user_idValue,
                template_idValue,
                businessnameValue,
                emailValue,
                mobileValue

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


                        if (stringCode.equalsIgnoreCase("true"))
                        {
                            Helper_Method.toaster(_act, stringMsg);
                            Helper_Method.dismissProgessBar();

                            Intent intent = new Intent(_act, MainActivity.class);
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


}