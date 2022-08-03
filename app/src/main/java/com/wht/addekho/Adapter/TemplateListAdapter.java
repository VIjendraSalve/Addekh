package com.wht.addekho.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wht.addekho.Activties.BannerListActivity;
import com.wht.addekho.Activties.CreateBannerActivity;
import com.wht.addekho.Model.Template;
import com.wht.addekho.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class TemplateListAdapter extends RecyclerView.Adapter<TemplateListAdapter.SingleItemRowHolder> {

    private ArrayList<Template> itemsList;
    private Context mContext;
    private Template singleItem;
    private String strBusinessId;
    private String vendorType="", vendorId="";
    private Dialog dialog;
    private int quantity = 0;

    public TemplateListAdapter(Context context, ArrayList<Template> itemsList, String vendorType, String vendorId) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.vendorType = vendorType;
        this.vendorId = vendorId;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_template_layout, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, @SuppressLint("RecyclerView") int i) {

        singleItem = new Template();
        singleItem = itemsList.get(i);
        holder.tv_template_name.setText("Template " + (i+1));

        Glide.with(mContext)
                .load(itemsList.get(i).getImage())
                .apply(new RequestOptions().placeholder(R.drawable.no_image_available).error(R.drawable.no_image_available))
                .into(holder.iv_template_image);



        holder.btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CreateBannerActivity.class);
                intent.putExtra("TemplateID",itemsList.get(i).getId());
                mContext.startActivity(intent);
            }
        });

        holder.btn_view_template.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, BannerListActivity.class);
                mContext.startActivity(intent);
            }
        });

      /*  holder.tv_shop_name.setText("" + singleItem.getShop_name());
        holder.tv_vendor_type_discount.setText("" + singleItem.getVendor_type() + "  " + singleItem.getDiscount_on_order() + "%");
        holder.tv_vendor_address.setText("" + singleItem.getAddress() + " " + singleItem.getAddress2());
        holder.tv_vendor_name.setText("" + singleItem.getVendor_name());
        holder.tv_shop_mobile.setText("" + singleItem.getMobile_number());
        Glide.with(mContext)
                .load(singleItem.getShop_image())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher))
                .into(holder.civ_shop_image);

        holder.tv_shop_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity((Activity) mContext)
                        .withPermissions(Manifest.permission.CALL_PHONE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                // check if all permissions are granted
                                if (report.areAllPermissionsGranted()) {
                                    // Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                                    String number = singleItem.getMobile_number();
                                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                    //callIntent.setData(Uri.parse("tel:" + number));
                                    callIntent.setData(Uri.parse("tel:" + number));
                                    mContext.startActivity(callIntent);
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

        holder.ll_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumberWithCountryCode = "+91" + itemsList.get(i).getMobile_number();
                String message = "Hey ";

                mContext.startActivity(
                        new Intent(Intent.ACTION_VIEW,
                                Uri.parse(
                                        String.format("https://api.whatsapp.com/send?phone=%s&text=%s", phoneNumberWithCountryCode, message)
                                )
                        )
                );
            }
        });*/


    }



    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tv_template_name;
        private ImageView iv_template_image;
        private Button btn_select, btn_view_template;


        public SingleItemRowHolder(View view) {
            super(view);

            this.tv_template_name = (TextView) view.findViewById(R.id.tv_template_name);
            this.iv_template_image = view.findViewById(R.id.iv_template_image);
            this.btn_select = view.findViewById(R.id.btn_select);
            this.btn_view_template = view.findViewById(R.id.btn_view_template);

        }

    }
}




