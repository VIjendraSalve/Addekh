package com.wht.addekh.Notification;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wht.addekh.R;
import com.wht.addekh.Helper.Helper_Method;

import java.util.List;

/**
 * Created by YoYoNituSingh on 7/14/2016.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<Notification> notificationList;
    private DatabaseSqliteHandler db;
    private NoData noData;


    public NotificationAdapter(Context context, List<Notification> notificationList, NoData noData) {
        this.context = context;
        this.notificationList = notificationList;
        this.noData = noData;
    }

    public interface NoData {
        void on_Remove(List<Notification> notificationList);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_notification_row, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        db = DatabaseSqliteHandler.getInstance(context);
        final Notification m = notificationList.get(position);


        // load the animation
        //Animation fadeInAnimation  = AnimationUtils.loadAnimation(convertView.getContext(), R.anim.fade_in);

        // title
        holder.tv_notification_title.setText(Helper_Method.toTitleCase(m.getNotification_title()));
        holder.tv_notification_message.setText(m.getNotification_message());
        holder.notification_date_time.setText(m.getNotification_date_time());

        Linkify.addLinks(holder.tv_notification_message, Linkify.ALL);

  /*      Glide.with(context).load(m.getNotification_image())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        holder.iv_notification_image.setImageResource(R.drawable.default_square);

                        return true;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.iv_notification_image.setImageDrawable(resource);

                        return true;
                    }
                })
                .into(holder.iv_notification_image);*/

        Log.d("Notification", "Adapter: " + m.getNotification_image());

        if (m.getNotification_image() != null && !m.getNotification_image().isEmpty() && !m.getNotification_image().equals("null") && !m.getNotification_image().equals("")) {

            holder.iv_notification_image.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(m.getNotification_image())
                    .apply(new RequestOptions().placeholder(R.drawable.no_data_found).error(R.drawable.no_data_found))
                    .into(holder.iv_notification_image);
        } else {
          /*  Glide.with(context)
                    .load(R.drawable.defaultimagebanner)
                    .apply(new RequestOptions().placeholder(R.drawable.defaultimagebanner).error(R.drawable.defaultimagebanner))
                    .into(holder.iv_notification_image);*/
            holder.iv_notification_image.setVisibility(View.GONE);
        }

       /* holder.iv_notification_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (m.getNotification_image() != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("image_url", m.getNotification_image());
                    Intent intent = new Intent(context, ViewImage.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            }
        });*/

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CustomAlertDialog(context, CustomAlertDialog.WARNING_TYPE)
                        //.setThemeColor(R.color.red_light)
                        .setCancelText(context.getResources().getString(R.string.no))
                        .setConfirmText(context.getResources().getString(R.string.yes))
                        .setContentText(context.getResources().getString(R.string.delete_message))
                        .setTitleText(context.getResources().getString(R.string.confirm_delete))
                        .setConfirmClickListener(new CustomAlertDialog.OnCMEDialogClickListner() {
                            @Override
                            public void onClick(CustomAlertDialog customDialog) {
                                db.delete_notification(m.getNotification_id());
                                notificationList.remove(position);
                                notifyDataSetChanged();
                                Toast.makeText(context, R.string.noti_delete, Toast.LENGTH_SHORT).show();
                                //if (notificationList.size() == 0)
                                noData.on_Remove(notificationList);
                                customDialog.dismiss();
                            }
                        })
                        .setCancelClickListener(new CustomAlertDialog.OnCMEDialogClickListner() {
                            @Override
                            public void onClick(CustomAlertDialog customDialog) {

                                customDialog.dismiss();
                            }
                        }).show();
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                new CustomAlertDialog(context, CustomAlertDialog.WARNING_TYPE)
                        //.setThemeColor(R.color.red_light)
                        .setCancelText(context.getResources().getString(R.string.no))
                        .setConfirmText(context.getResources().getString(R.string.yes))
                        .setContentText(context.getResources().getString(R.string.delete_message))
                        .setTitleText(context.getResources().getString(R.string.confirm_delete))
                        .setConfirmClickListener(new CustomAlertDialog.OnCMEDialogClickListner() {
                            @Override
                            public void onClick(CustomAlertDialog customDialog) {
                                db.delete_notification(m.getNotification_id());
                                notificationList.remove(position);
                                notifyDataSetChanged();
                                Toast.makeText(context, R.string.noti_delete, Toast.LENGTH_SHORT).show();
                                //if (notificationList.size() == 0)
                                noData.on_Remove(notificationList);
                                customDialog.dismiss();
                            }
                        })
                        .setCancelClickListener(new CustomAlertDialog.OnCMEDialogClickListner() {
                            @Override
                            public void onClick(CustomAlertDialog customDialog) {

                                customDialog.dismiss();
                            }
                        }).show();

                return false;
            }
        });

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_notification_title, tv_notification_message, notification_date_time;
        ImageView iv_notification_image;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_notification_title = (TextView) itemView.findViewById(R.id.tv_notification_title);
            tv_notification_message = (TextView) itemView.findViewById(R.id.tv_notification_message);
            notification_date_time = (TextView) itemView.findViewById(R.id.notification_date_time);
            iv_notification_image = (ImageView) itemView.findViewById(R.id.iv_notification_image);
        }
    }
}
