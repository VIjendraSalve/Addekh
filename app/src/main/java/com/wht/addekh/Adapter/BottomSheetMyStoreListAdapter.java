package com.wht.addekh.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.wht.addekh.Activties.ActivityAddStore;
import com.wht.addekh.Activties.ShowAdvertiesmentActivity;
import com.wht.addekh.Constant.IConstant;
import com.wht.addekh.Model.BottomsheetMyStoreListObject;
import com.wht.addekh.R;

import java.util.ArrayList;

public class BottomSheetMyStoreListAdapter extends RecyclerView.Adapter<BottomSheetMyStoreListAdapter.ProductViewHolder> {

    private Context mCtx;
    //we are storing all the products in a list
    private ArrayList<BottomsheetMyStoreListObject> bottomsheetMyStoreListObjects;

    String blog_category ,blog_title,author,description;


    //getting the context and product list with constructor
    public BottomSheetMyStoreListAdapter(Context mCtx, ArrayList<BottomsheetMyStoreListObject> bottomsheetMyStoreListObjects)
    {
        this.mCtx = mCtx;
        this.bottomsheetMyStoreListObjects = bottomsheetMyStoreListObjects;

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.my_store_list_layout, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //getting the product of the specified position
        BottomsheetMyStoreListObject product = bottomsheetMyStoreListObjects.get(position);
        //binding the data with the viewholder views
        holder.tv_StoreName.setText(""+bottomsheetMyStoreListObjects.get(position).getStore_name());

        //holder.tv_StoreDesc.setText(""+bottomsheetMyStoreListObjects.get(position).getStore_desc());
      //  holder.tv_Mobilenumber.setText(""+bottomsheetMyStoreListObjects.get(position).getStore_contact_number());
        holder.tv_SAddr.setText(""+bottomsheetMyStoreListObjects.get(position).getStore_address());

        Glide.with(mCtx)
                .load(bottomsheetMyStoreListObjects.get(position).getStore_logo())
                //.apply(new RequestOptions().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).diskCacheStrategy(DiskCacheStrategy.ALL))
                .apply(new RequestOptions().placeholder(R.drawable.no_image_available).error(R.drawable.no_image_available).diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(holder.img_Storelogo);

        holder.tv_StoreAdds.setText(""+bottomsheetMyStoreListObjects.get(position).getStore_advertisements()+" Ads ");



        holder.tv_StoreAdds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mCtx, ShowAdvertiesmentActivity.class);
                i.putExtra(IConstant.STORE_ID, bottomsheetMyStoreListObjects.get(position).getId());
                i.putExtra(IConstant.STORE_TITLE, bottomsheetMyStoreListObjects.get(position).getStore_name());
                mCtx.startActivity(i);

            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mCtx, ActivityAddStore.class);
                i.putParcelableArrayListExtra("list", bottomsheetMyStoreListObjects);
                i.putExtra("pos", position);
                i.putExtra("flag", "2");
                mCtx.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount()
    {
        return bottomsheetMyStoreListObjects.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_StoreName,tv_StoreDesc,tv_Mobilenumber,tv_SAddr,tv_StoreAdds;
        ImageView img_Storelogo,imgBrowseCategoryHeart;
      //  private Button btn_create_banner;




        public ProductViewHolder(View itemView)
        {
            super(itemView);
            tv_StoreName = itemView.findViewById(R.id.tv_StoreName);
          //  tv_StoreDesc = itemView.findViewById(R.id.tv_StoreDesc);
        //    tv_Mobilenumber = itemView.findViewById(R.id.tv_Mobilenumber);
            tv_SAddr = itemView.findViewById(R.id.tv_SAddr);
            img_Storelogo = itemView.findViewById(R.id.img_Storelogo);
            tv_StoreAdds =  itemView.findViewById(R.id.tv_StoreAdds);
        //    imgBrowseCategoryHeart = itemView.findViewById(R.id.imgBrowseCategoryHeart);
        //    btn_create_banner = itemView.findViewById(R.id.btn_create_banner);



        }
    }



}
