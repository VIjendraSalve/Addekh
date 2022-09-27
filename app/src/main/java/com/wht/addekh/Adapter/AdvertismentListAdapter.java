package com.wht.addekh.Adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Picture;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

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
import com.wht.addekh.Constant.IConstant;
import com.wht.addekh.Constant.IUrls;
import com.wht.addekh.Constant.Interface;
import com.wht.addekh.Helper.SharedPref;
import com.wht.addekh.Model.Advertisment;
import com.wht.addekh.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdvertismentListAdapter extends RecyclerView.Adapter<AdvertismentListAdapter.SingleItemRowHolder> {

    boolean flagisFav;
    private ArrayList<Advertisment> itemsList;
    private Context mContext;
    private Advertisment singleItem;
    private String strBusinessId;
    private Spinner spnr_reason;
    private String spnr_reason_id, spnr_reason_name;
    private ArrayList<Advertisment> reasonArrayList = new ArrayList<>();
    private Dialog dialog;
    private Object advt_lng;
    private Object advt_lat;



    public AdvertismentListAdapter(Context context,
                                   ArrayList<Advertisment> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_view_add_display, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, @SuppressLint("RecyclerView") int i) {

        singleItem = new Advertisment();
        singleItem = itemsList.get(i);


        if (singleItem.getTitle() != null &&
                !singleItem.getTitle().equals("null") &&
                !singleItem.getTitle().equals("")) {
            holder.tv_add_title.setText("" + singleItem.getTitle());
        } else {
            holder.tv_add_title.setText("--");
        }

        Log.d("WebView", "onBindViewHolder: "+singleItem.getTemplate_link());
        holder.mWebView.loadUrl(singleItem.getTemplate_link());

        if (singleItem.getLast_date() != null && !singleItem.getLast_date().equals("null") && !singleItem.getLast_date().equals("")) {
            holder.tv_date_time.setText("" + singleItem.getLast_date());
        } else {
            holder.tv_date_time.setText("--");
        }

        String [] items = singleItem.getStore_address().split("\\s*,\\s*");
        List<String> container = Arrays.asList(items);
        Log.d("StoreAddres", "container size: "+container.size());


        if (singleItem.getStore_address() != null && !singleItem.getStore_address().equals("null") && !singleItem.getStore_address().equals("")) {
            holder.tv_location.setText("" +container.get(container.size()-4) + ", "+container.get(container.size()-3));
        } else {
            holder.tv_location.setText("--");
        }


        Log.d("UserImage", "onBindViewHolder: " + singleItem.getStore_logo());
        Log.d("UserImage", "onBindViewHolder: " + singleItem.getBanner_image());
        Glide.with(mContext)
                .load(singleItem.getBanner_image())
                .apply(new RequestOptions().placeholder(R.drawable.no_image_available).error(R.drawable.no_image_available))
                .into(holder.iv_banner_image);

        Glide.with(mContext)
                .load(singleItem.getStore_logo())
                .apply(new RequestOptions().placeholder(R.drawable.no_image_available).error(R.drawable.no_image_available))
                .into(holder.iv_shop_image);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
                showDialog(itemsList, i);
            }
        });

        holder.rl_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
                showDialog(itemsList, i);
            }
        });

        /*holder.iv_banner_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(itemsList, i);

            }
        });*/


        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date past = format.parse(itemsList.get(i).getCreated_on());
            Date now = new Date();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
//
//          System.out.println(TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime()) + " milliseconds ago");
//          System.out.println(TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) + " minutes ago");
//          System.out.println(TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) + " hours ago");
//          System.out.println(TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) + " days ago");

            if (seconds < 60) {
                holder.tv_date_time.setText("" + seconds + " seconds ago");
                // System.out.println(seconds+" seconds ago");
            } else if (minutes < 60) {
                holder.tv_date_time.setText("" + minutes + " minutes ago");
                //System.out.println(minutes+" minutes ago");
            } else if (hours < 24) {
                holder.tv_date_time.setText("" + hours + " hours ago");
                // System.out.println(hours+" hours ago");
            } else {
                holder.tv_date_time.setText("" + days + " days ago");
                //System.out.println(days+" days ago");
            }
        } catch (Exception j) {
            j.printStackTrace();
        }


    }

    private void showDialog(ArrayList<Advertisment> advertismentArrayList, int position) {

        Fragment fragment = null;
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_adv_details);

        TextView tv_add_title = dialog.findViewById(R.id.tv_add_title);
        tv_add_title.setText(advertismentArrayList.get(position).getTitle());
        WebView webview = dialog.findViewById(R.id.webview);

        webview.setInitialScale(1);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webview.setScrollbarFadingEnabled(false);
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        Log.d("getTemplate_link", "showDialog: "+advertismentArrayList.get(position).getTemplate_link());
        webview.loadUrl(advertismentArrayList.get(position).getTemplate_link());

        //  ImageView iv_banner_image =  dialog.findViewById(R.id.iv_banner_image);
        //  iv_banner_image.

        TextView tv_date_time = dialog.findViewById(R.id.tv_date_time);
        tv_date_time.setText(advertismentArrayList.get(position).getLast_date());

        TextView tv_location = dialog.findViewById(R.id.tv_location);
        String [] items = singleItem.getStore_address().split("\\s*,\\s*");
        List<String> container = Arrays.asList(items);

        if (singleItem.getStore_address() != null && !singleItem.getStore_address().equals("null") && !singleItem.getStore_address().equals("")) {
            tv_location.setText("" +container.get(container.size()-4) + ", "+container.get(container.size()-3));
        } else {
            tv_location.setText("--");
        }
       // tv_location.setText(advertismentArrayList.get(position).getStore_address());



        ImageView iv_banner_image = dialog.findViewById(R.id.iv_banner_image);
        Glide.with(mContext)
                .load(advertismentArrayList.get(position).getBanner_image())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(iv_banner_image);

        ImageView iv_shop_image = dialog.findViewById(R.id.iv_shop_image);
        Glide.with(mContext)
                .load(advertismentArrayList.get(position).getStore_logo())
                .apply(new RequestOptions().placeholder(R.drawable.no_image_available).error(R.drawable.no_image_available).diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(iv_shop_image);

        ImageView iv_like_adv = dialog.findViewById(R.id.iv_like_adv);
        if (advertismentArrayList.get(position).getIs_like().equals("1")) {
            iv_like_adv.setImageResource(R.drawable.ic_like);
        } else {
            iv_like_adv.setImageResource(R.drawable.ic_unlike_img);
        }

        iv_like_adv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavouriteAdvertisment(advertismentArrayList.get(position).getId(), iv_like_adv);
            }
        });

//        Glide.with(mContext)
//                .load(advertismentArrayList.get(position).getIs_active())
//                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).diskCacheStrategy(DiskCacheStrategy.ALL))
//                .into(adv_contact);


        TextView discription = dialog.findViewById(R.id.discription);
        discription.setText(advertismentArrayList.get(position).getDescription());

        TextView StoreName = dialog.findViewById(R.id.StoreName);
        StoreName.setText(advertismentArrayList.get(position).getStore_name());

        TextView adv_Address = dialog.findViewById(R.id.adv_Address);
        adv_Address.setText(advertismentArrayList.get(position).getStore_address());

        TextView adv_contact = dialog.findViewById(R.id.mobile_number);
          adv_contact.setText(advertismentArrayList.get(position).getStore_contact_number());

        TextView adv_ADID = dialog.findViewById(R.id.adv_ADID);
        adv_ADID.setText(advertismentArrayList.get(position).getStore_id());

        Button btn_direction = dialog.findViewById(R.id.btn_direction);
        btn_direction.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", advt_lat, advt_lng);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                mContext.startActivity(intent);
            }
        });


        TextView reportAD = dialog.findViewById(R.id.reportAD);
        reportAD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webCallReportAdvertisment(advertismentArrayList.get(position).getId());
            }
        });


        Button btn_share = dialog.findViewById(R.id.btn_share);
        // btn_share.setText(advertismentArrayList.get(position).getAdvt_lat());
        /*btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String StoreName =advertismentArrayList.get(position).getTitle()
                        +", \n"+advertismentArrayList.get(position).getDescription()
                        +", \n"+ advertismentArrayList.get(position).getStore_name()
                        +", \n"+ advertismentArrayList.get(position).getStore_address();

                Intent intentShare = new Intent(Intent.ACTION_SEND);
                intentShare.setType("text/plain");
                intentShare.putExtra(Intent.EXTRA_TEXT, ""+ StoreName);
                mContext.startActivity(Intent.createChooser(intentShare, "Shared the text ..."));
            }
        });*/

        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dexter.withActivity((Activity) mContext)
                        .withPermissions(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                // check if all permissions are granted
                                if (report.areAllPermissionsGranted()) {
                                    shareResultAsImage(webview,
                                            advertismentArrayList.get(position).getTitle(),
                                            advertismentArrayList.get(position).getDescription(),
                                            advertismentArrayList.get(position).getStore_name(),
                                            advertismentArrayList.get(position).getStore_address(),
                                            advertismentArrayList.get(position).getStore_contact_number()
                                    );
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
                                Toast.makeText(mContext, "Error occurred! ", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .onSameThread()
                        .check();

            }
        });

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date past = format.parse(itemsList.get(position).getCreated_on());
            Date now = new Date();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
//
//          System.out.println(TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime()) + " milliseconds ago");
//          System.out.println(TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) + " minutes ago");
//          System.out.println(TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) + " hours ago");
//          System.out.println(TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) + " days ago");

            if (seconds < 60) {
                tv_date_time.setText("" + seconds + " seconds ago");
                // System.out.println(seconds+" seconds ago");
            } else if (minutes < 60) {
                tv_date_time.setText("" + minutes + " minutes ago");
                //System.out.println(minutes+" minutes ago");
            } else if (hours < 24) {
                tv_date_time.setText("" + hours + " hours ago");
                // System.out.println(hours+" hours ago");
            } else {
                tv_date_time.setText("" + days + " days ago");
                //System.out.println(days+" days ago");
            }
        } catch (Exception j) {
            j.printStackTrace();
        }
        // Intent i = new Intent()

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialoAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }


    private void FavouriteAdvertisment(String advertismentId, ImageView iv_like) {

        Interface api = IUrls.getRetrofit(IUrls.BASE_URL).create(Interface.class);
        Call<ResponseBody> result = api.PostAddvertismentAddToFavourite(
                SharedPref.getPrefs(mContext, IConstant.USER_ID),
                advertismentId
        );
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {
                    output = response.body().string();
                    Log.d("flagisFav", "onResponse11: " + output);
                    JSONObject jsonObject = new JSONObject(output);
                    boolean res = jsonObject.getBoolean("result");

                    // Log.d(TAG, "onResponse: " + object.getString("Message"));
                    if (res) {
                        flagisFav = jsonObject.getBoolean("is_like");

                        if (flagisFav) {
                            iv_like.setImageResource(R.drawable.ic_like);
                        } else {
                            iv_like.setImageResource(R.drawable.ic_unlike_img);
                        }
                        //Toast.makeText(mContext, ""+flagisFav, Toast.LENGTH_SHORT).show();
                    } else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }

        });

    }

    private void webCallReportAdvertisment(String advertismentId) {

        Interface api = IUrls.getRetrofit(IUrls.BASE_URL).create(Interface.class);
        Call<ResponseBody> result = api.PostReportAdvertisment(
                SharedPref.getPrefs(mContext, IConstant.USER_ID),
                advertismentId
        );
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {
                    output = response.body().string();
                    Log.d("flagisFav", "onResponse11: " + output);
                    JSONObject jsonObject = new JSONObject(output);
                    boolean res = jsonObject.getBoolean("result");
                    String reason = jsonObject.getString("reason");

                    // Log.d(TAG, "onResponse: " + object.getString("Message"));
                    if (res) {

                        Toast.makeText(mContext, ""+reason, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, ""+reason, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }

        });

    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tv_add_title, tv_date_time, tv_location;
        private ImageView iv_banner_image, iv_shop_image, adv_contact;
        public WebView mWebView;
        private RelativeLayout rl_main;


        public SingleItemRowHolder(View view) {
            super(view);

            this.rl_main = (RelativeLayout) view.findViewById(R.id.rl_main);
            this.tv_add_title = (TextView) view.findViewById(R.id.tv_add_title);
            this.tv_date_time = (TextView) view.findViewById(R.id.tv_date_time);
            this.tv_location = (TextView) view.findViewById(R.id.tv_location);
            this.iv_banner_image = (ImageView) view.findViewById(R.id.iv_banner_image);
            this.iv_shop_image = (ImageView) view.findViewById(R.id.iv_shop_image);
            //this.adv_contact = view.findViewById(R.id.adv_contact);
            mWebView = view.findViewById(R.id.webview);
            mWebView.setInitialScale(1);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setLoadWithOverviewMode(true);
            mWebView.getSettings().setUseWideViewPort(true);
            mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            mWebView.setScrollbarFadingEnabled(false);
            mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        }

    }

    private void shareResultAsImage(WebView webView, String Title, String Desc, String StroeName, String StoreAddress, String Mobile) {
        Bitmap bitmap = getBitmapOfWebView(webView);
        String pathofBmp = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), bitmap, "data", null);
        Uri bmpUri = Uri.parse(pathofBmp);
        final Intent emailIntent1 = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent1.setType("image/png");
        emailIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent1.putExtra(Intent.EXTRA_STREAM, bmpUri);
        emailIntent1.putExtra(Intent.EXTRA_SUBJECT, "ADD Dekh");
        emailIntent1.putExtra(Intent.EXTRA_SUBJECT, mContext.getResources().getString(R.string.app_name));
        String sAux = Html.fromHtml(Title) + "\n\n" + Html.fromHtml(Desc)+ "\n\n" + Html.fromHtml(StroeName)
                + "\n\n" + Html.fromHtml(StoreAddress)+ "Call On:" + Mobile + "\n\n";
        sAux = sAux+ "\n\n App Link For More Info \n\n" + mContext.getResources().getString(R.string.app_url) + "\n\n";
        emailIntent1.putExtra(Intent.EXTRA_TEXT, sAux);
        emailIntent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        mContext.startActivity(Intent.createChooser(emailIntent1, "Share Post"));

        //mCtx.startActivity(emailIntent1);
    }

    private Bitmap getBitmapOfWebView(final WebView webView) {
        Picture picture = webView.capturePicture();
        Bitmap bitmap = Bitmap.createBitmap(picture.getWidth(), picture.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        picture.draw(canvas);
        return bitmap;
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
        intent.setData(uri);
        ((Activity) mContext).startActivityForResult(intent, 101);
    }

    private void showSettingsDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
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

}




