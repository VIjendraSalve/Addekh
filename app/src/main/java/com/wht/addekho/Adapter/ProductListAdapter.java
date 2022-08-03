package com.wht.addekho.Adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.wht.addekho.Model.ProductListObject;
import com.wht.addekho.R;

import java.util.ArrayList;
import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<ProductListObject> imageModelArrayList, filterList;
    private Context mContext;

    private Dialog dialog;
    private TextView tvBusinessName, tvName, tvEmail, tvCall, tvCancel, tvSubmit;
    private String strUserId, UserType, strImage, strName, strEmail, strMobile, strAddress;
    int pos = 0;


    public ProductListAdapter(Context mContext, ArrayList<ProductListObject> imageModelArrayList) {

        inflater = LayoutInflater.from(mContext);
        this.imageModelArrayList = imageModelArrayList;
        //this.filterList = new ArrayList<ProductListObject>();
        // this.filterList.addAll(this.imageModelArrayList);
        this.mContext = mContext;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.product_list_item_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        pos = position;
        //ProductListObject listItem = filterList.get(position);
        holder.tvBusinessName.setText(imageModelArrayList.get(position).name);
//        holder.tvAddress.setText(imageModelArrayList.get(position).address);
        // holder.tvTotalRatings.setText(imageModelArrayList.get(position).Rating + " Rating");

      /*
        holder.tvTotalRatings.setText(imageModelArrayList.get(position).Rating + " Rating");


        holder.ratingBar.setRating(Float.parseFloat(imageModelArrayList.get(position).AverageRating));
        holder.tvRatings.setText(imageModelArrayList.get(position).AverageRating);
        holder.tvAvailable.setText(imageModelArrayList.get(position).AvailableStatus);*/


       /* if (imageModelArrayList.get(position).AvailableStatus.equalsIgnoreCase("yes")) {
            holder.tvAvailable.setTextColor(mContext.getResources().getColor(R.color.md_green_600));
            holder.tvAvailable.setText("Available");
        } else {
            holder.tvAvailable.setTextColor(mContext.getResources().getColor(R.color.md_red_600));
            holder.tvAvailable.setText("Unavailable");
        }*/

        DisplayMetrics metricscard = mContext.getResources().getDisplayMetrics();
        int cardwidth = metricscard.widthPixels;
        int cardheight = metricscard.heightPixels;
        holder.ivProfile.getLayoutParams().height = (int) (cardheight / 5.5);
        holder.ivProfile.getLayoutParams().width = (int) (cardwidth / 2.5);

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mContext, BusinessDetailsActivity.class);
//                intent.putExtra("businessId", imageModelArrayList.get(position).items_id);
//                ((Activity) mContext).startActivityForResult(intent, 1);
//
//            }
//        });


//        Glide.with(mContext)
//                .load(imageModelArrayList.get(position).image)
//                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).diskCacheStrategy(DiskCacheStrategy.ALL))
//                .into(holder.ivProfile);


//        holder.tvCall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                Dexter.withActivity((Activity) mContext)
//                        .withPermissions(Manifest.permission.CALL_PHONE,
//                                Manifest.permission.READ_EXTERNAL_STORAGE,
//                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        .withListener(new MultiplePermissionsListener() {
//                            @Override
//                            public void onPermissionsChecked(MultiplePermissionsReport report) {
//                                // check if all permissions are granted
//                                if (report.areAllPermissionsGranted()) {
//                                    // Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
//                                    String number = imageModelArrayList.get(position).mobile;
//                                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
//                                    //callIntent.setData(Uri.parse("tel:" + number));
//                                    callIntent.setData(Uri.parse("tel:" + number));
//                                    mContext.startActivity(callIntent);
//                                }
//                                // check for permanent denial of any permission
//                                if (report.isAnyPermissionPermanentlyDenied()) {
//                                    // show alert dialog navigating to Settings
//                                    showSettingsDialog();
//                                }
//                            }
//
//
//                            @Override
//                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                                token.continuePermissionRequest();
//                            }
//                        }).
//                        withErrorListener(new PermissionRequestErrorListener() {
//                            @Override
//                            public void onError(DexterError error) {
//                                Toast.makeText(mContext, "Error occurred! ", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .onSameThread()
//                        .check();
//            }
//        });
//
//        holder.ivShare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            /*    Dexter.withActivity((Activity) mContext)
//                        .withPermissions(Manifest.permission.CALL_PHONE,
//                                Manifest.permission.READ_EXTERNAL_STORAGE,
//                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        .withListener(new MultiplePermissionsListener() {
//                            @Override
//                            public void onPermissionsChecked(MultiplePermissionsReport report) {
//                                // check if all permissions are granted
//                                if (report.areAllPermissionsGranted()) {
//                                    Uri imageUri = Uri.parse("android.resource://" + mContext.getPackageName()
//                                            + "/drawable/" + "ic_launcher");
//                                    Drawable mDrawable = holder.ivProfile.getDrawable().getCurrent();
//                                    Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();
//                                    // Bitmap mBitmap = ((BitmapDrawable)holder.ivProfile.getDrawable().getCurrent()).getBitmap();
//                                    //Bitmap mBitmap = ((BitmapDrawable) holder.ivProfile.getDrawable()).getBitmap();
//
//                                    String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), mBitmap, "Image Description", null);
//                                    Uri uri = Uri.parse(path);
//
//                                    Intent intent = new Intent(Intent.ACTION_SEND);
//                                    intent.setType("image/png");
//                                    intent.putExtra(Intent.EXTRA_STREAM, uri);
//                                    intent.putExtra(Intent.EXTRA_SUBJECT, mContext.getResources().getString(R.string.app_name));
//                                    String sAux = Html.fromHtml(imageModelArrayList.get(position).name) + "\n\n" + Html.escapeHtml(imageModelArrayList.get(position).address) + Html.escapeHtml(imageModelArrayList.get(position).mobile) + "\n\n" *//*+ "Call On:" + mContext.getResources().getString(R.string.mobile_no) + "\n\n"*//*;
//                                    sAux = sAux + mContext.getResources().getString(R.string.app_url) + "\n\n";
//                                    intent.putExtra(Intent.EXTRA_TEXT, sAux);
//                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                                    mContext.startActivity(Intent.createChooser(intent, "Share Post"));
//                                }
//                                // check for permanent denial of any permission
//                                if (report.isAnyPermissionPermanentlyDenied()) {
//                                    // show alert dialog navigating to Settings
//                                    showSettingsDialog();
//                                }
//                            }
//
//
//                            @Override
//                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                                token.continuePermissionRequest();
//                            }
//                        }).
//                        withErrorListener(new PermissionRequestErrorListener() {
//                            @Override
//                            public void onError(DexterError error) {
//                                Toast.makeText(mContext, "Error occurred! ", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .onSameThread()
//                        .check();*/
//
//                Uri imageUri = Uri.parse("android.resource://" + mContext.getPackageName()
//                        + "/drawable/" + "ic_launcher");
//                //Drawable mDrawable = holder.ivProfile.getDrawable().getCurrent();
//                Drawable mDrawable = holder.ivProfile.getDrawable().getCurrent();
//                Bitmap mBitmap = getBitmapFromDrawable(mDrawable);
//                // Bitmap mBitmap = ((BitmapDrawable)holder.itemImage.getDrawable().getCurrent()).getBitmap();
//
//                String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), mBitmap, "Image Description", null);
//                Uri uri = Uri.parse(path);
//
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("image/jpeg");
//                intent.putExtra(Intent.EXTRA_STREAM, uri);
//                intent.putExtra(Intent.EXTRA_SUBJECT, mContext.getResources().getString(R.string.app_name));
//                String sAux = Html.fromHtml(imageModelArrayList.get(position).name) + "\n\n" + Html.escapeHtml(imageModelArrayList.get(position).address) + Html.escapeHtml(imageModelArrayList.get(position).mobile) + "\n\n";
//                sAux = sAux + mContext.getResources().getString(R.string.app_url) + "\n\n";
//                intent.putExtra(Intent.EXTRA_TEXT, sAux);
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                mContext.startActivity(Intent.createChooser(intent, "Share Post"));
//
//            }
//        });


        // holder.tvLikeCount.setText(String.valueOf(Integer.parseInt(imageModelArrayList.get(position).FavouriteCount)) + " Like's");
        //like unlike
/*        if (imageModelArrayList.get(position).MyFavouriteStatus.equals("like")) {
            holder.ivLike.setVisibility(View.VISIBLE);
            holder.ivUnlike.setVisibility(View.INVISIBLE);


        } else {

            holder.ivLike.setVisibility(View.INVISIBLE);
            holder.ivUnlike.setVisibility(View.VISIBLE);
        }*/

/*        holder.ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int likeCount = 0;
                likeCount = Integer.parseInt(imageModelArrayList.get(position).FavouriteCount) - 1;
                imageModelArrayList.get(position).MyFavouriteStatus = "unlike";
                imageModelArrayList.get(position).FavouriteCount = String.valueOf(likeCount);
                holder.tvLikeCount.setText(String.valueOf(likeCount) + " Like's");
                notifyItemChanged(position);
                notifyDataSetChanged();
                webServiceCallProfileStatus(imageModelArrayList.get(position).BusinessID, strUserId, "unlike");

            }
        });

        holder.ivUnlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int likeCount = 0;
                likeCount = Integer.parseInt(imageModelArrayList.get(position).FavouriteCount) + 1;
                imageModelArrayList.get(position).MyFavouriteStatus = "like";
                imageModelArrayList.get(position).FavouriteCount = String.valueOf(likeCount);
                holder.tvLikeCount.setText(String.valueOf(likeCount) + " Like's");
                notifyItemChanged(position);
                notifyDataSetChanged();
                webServiceCallProfileStatus(imageModelArrayList.get(position).BusinessID, strUserId, "like");
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return imageModelArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvBusinessName, tvAddress, tvRatings, tvTotalRatings, tvExperience, tvLikeCount;
        RatingBar ratingBar;
        private ImageView ivProfile, ivShare, ivLike, ivUnlike;
        private TextView tvCall;

        public MyViewHolder(View itemView) {
            super(itemView);



        }

    }

    // Do Search...
    public void filter(final String text) {

        // Searching could be complex..so we will dispatch it to a different thread...
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Clear the filter list
                filterList.clear();

                // If there is no search value, then add all original list items to filter list
                if (TextUtils.isEmpty(text)) {

                    filterList.addAll(imageModelArrayList);

                } else {
                    // Iterate in the original List and add it to filter list...
                    for (ProductListObject item : imageModelArrayList) {
                        if (item.name.toLowerCase().contains(text.toLowerCase()) /*||
                                item.address.toLowerCase().contains(text.toLowerCase())*/) {
                            // Adding Matched items
                            filterList.add(item);
                        }
                    }
                }

                // Set on UI Thread
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Notify the List that the DataSet has changed...
                        notifyDataSetChanged();
                    }
                });

            }
        }).start();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
        intent.setData(uri);
        ((Activity) mContext).startActivityForResult(intent, 101);
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
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
    @NonNull
    private Bitmap getBitmapFromDrawable(@NonNull Drawable drawable) {
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }


}
