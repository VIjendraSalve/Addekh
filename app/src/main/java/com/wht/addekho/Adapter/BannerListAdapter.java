package com.wht.addekho.Adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Picture;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.wht.addekho.Model.Banner;
import com.wht.addekho.Model.Template;
import com.wht.addekho.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BannerListAdapter extends RecyclerView.Adapter<BannerListAdapter.SingleItemRowHolder> {

    private ArrayList<Banner> itemsList;
    private Context mContext;
    private Banner singleItem;
    private String strBusinessId;
    private String vendorType="", vendorId="";
    private Dialog dialog;
    private int quantity = 0;

    public BannerListAdapter(Context context, ArrayList<Banner> itemsList, String vendorType, String vendorId) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.vendorType = vendorType;
        this.vendorId = vendorId;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_banner, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, @SuppressLint("RecyclerView") int i) {

        singleItem = new Banner();
        singleItem = itemsList.get(i);


        holder.mWebView.loadUrl(itemsList.get(i).gettemplate_link());



        holder.ivShare.setOnClickListener(new View.OnClickListener() {
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
                                    shareResultAsImage(holder.mWebView, "", "");
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

    private void shareResultAsImage(WebView webView, String examTitle, String examDesc) {
        Bitmap bitmap = getBitmapOfWebView(webView);
        String pathofBmp = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), bitmap, "data", null);
        Log.d("BannerImagePath", "shareResultAsImage1: "+pathofBmp.toString());
        Uri bmpUri = Uri.parse(pathofBmp);
        Log.d("BannerImagePath", "shareResultAsImage: "+bmpUri.getPath());
        Log.d("BannerImagePath", "shareResultAsImage: "+bmpUri.getLastPathSegment());
        String bannerPath = getRealPathFromURI(mContext, bmpUri);
        Log.d("BannerImagePath", "bannerPath: "+bannerPath);

        final Intent emailIntent1 = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent1.setType("image/png");
        emailIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent1.putExtra(Intent.EXTRA_STREAM, bmpUri);
        emailIntent1.putExtra(Intent.EXTRA_SUBJECT, "Design Created by : Add Dekho ");
        emailIntent1.putExtra(Intent.EXTRA_SUBJECT, mContext.getResources().getString(R.string.app_name));
        String sAux = Html.fromHtml(examTitle) + "\n\n" + Html.fromHtml(examTitle) /*mContext.getResources().getString(R.string.share_text) +*//* "\n\n" + "Call On:" + mContext.getResources().getString(R.string.mobile_no) + "\n\n"*/;
        sAux = sAux + mContext.getResources().getString(R.string.app_url) + "\n\n";
        emailIntent1.putExtra(Intent.EXTRA_TEXT, sAux);
        emailIntent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        mContext.startActivity(Intent.createChooser(emailIntent1, "Share Post"));

        //mCtx.startActivity(emailIntent1);
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private Bitmap getBitmapOfWebView(final WebView webView) {
        Picture picture = webView.capturePicture();
        Bitmap bitmap = Bitmap.createBitmap(picture.getWidth(), picture.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        picture.draw(canvas);
        return bitmap;
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        private ImageView ivShare;
        public WebView mWebView;


        public SingleItemRowHolder(View view) {
            super(view);


            this.ivShare = view.findViewById(R.id.ivShare);
            this.mWebView = view.findViewById(R.id.webview);

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
}




