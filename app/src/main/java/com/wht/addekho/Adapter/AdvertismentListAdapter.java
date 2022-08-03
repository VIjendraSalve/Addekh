package com.wht.addekho.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.wht.addekho.Constant.IConstant;
import com.wht.addekho.Constant.IUrls;
import com.wht.addekho.Constant.Interface;
import com.wht.addekho.Helper.SharedPref;
import com.wht.addekho.Model.Advertisment;
import com.wht.addekho.R;

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

        holder.iv_banner_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(itemsList, i);

            }
        });


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

        //  ImageView iv_banner_image =  dialog.findViewById(R.id.iv_banner_image);
        //  iv_banner_image.

        TextView tv_date_time = dialog.findViewById(R.id.tv_date_time);
        tv_date_time.setText(advertismentArrayList.get(position).getCreated_on());

        TextView tv_location = dialog.findViewById(R.id.tv_location);
        tv_location.setText(advertismentArrayList.get(position).getStore_address());

        ImageView iv_banner_image = dialog.findViewById(R.id.iv_banner_image);
        Glide.with(mContext)
                .load(advertismentArrayList.get(position).getBanner_image())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(iv_banner_image);

        ImageView iv_shop_image = dialog.findViewById(R.id.iv_shop_image);
        Glide.with(mContext)
                .load(advertismentArrayList.get(position).getStore_logo())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(iv_shop_image);

        ImageView iv_like_adv = dialog.findViewById(R.id.iv_like_adv);
        if (advertismentArrayList.get(position).getIs_like().equals("1")) {
            iv_like_adv.setImageResource(R.drawable.ic_like);
        } else {
            iv_like_adv.setImageResource(R.drawable.ic_unlike);
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

        // TextView adv_contact = dialog.findViewById(R.id.adv_contact);
        //  adv_contact.setText(advertismentArrayList.get(position).get);

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
                Toast.makeText(mContext, "Reported Successfully", Toast.LENGTH_SHORT).show();
            }
        });


        Button btn_share = dialog.findViewById(R.id.btn_share);
        // btn_share.setText(advertismentArrayList.get(position).getAdvt_lat());
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String StoreName = "";
                String StroreAddress = "";
                ;
                Intent intentShare = new Intent(Intent.ACTION_SEND);
                intentShare.setType("text/plain");
                intentShare.putExtra(Intent.EXTRA_TEXT, "My Text of the message goes here ... write anything what you want" + "name\n" + "address\n" + StoreName + "\n");
                mContext.startActivity(Intent.createChooser(intentShare, "Shared the text ..."));
            }
        });


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
                            iv_like.setImageResource(R.drawable.ic_unlike);
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

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tv_add_title, tv_date_time, tv_location;
        private ImageView iv_banner_image, iv_shop_image, adv_contact;


        public SingleItemRowHolder(View view) {
            super(view);

            this.tv_add_title = (TextView) view.findViewById(R.id.tv_add_title);
            this.tv_date_time = (TextView) view.findViewById(R.id.tv_date_time);
            this.tv_location = (TextView) view.findViewById(R.id.tv_location);
            this.iv_banner_image = (ImageView) view.findViewById(R.id.iv_banner_image);
            this.iv_shop_image = (ImageView) view.findViewById(R.id.iv_shop_image);
            //this.adv_contact = view.findViewById(R.id.adv_contact);

        }

    }

}




