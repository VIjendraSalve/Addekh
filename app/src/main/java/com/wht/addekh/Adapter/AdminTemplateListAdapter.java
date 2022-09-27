package com.wht.addekh.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.wht.addekh.Model.TemplateData;
import com.wht.addekh.R;

import java.util.ArrayList;

public class AdminTemplateListAdapter extends RecyclerView.Adapter<AdminTemplateListAdapter.ProductViewHolder> {

    private Context mCtx;
    //we are storing all the products in a list
    private ArrayList<TemplateData> allCategoryArrayList;
    public OnItemClickListener listener;
    private String path="";


    public interface OnItemClickListener {
        void onItemClickForAdminTemplate(int id);
    }


    //getting the context and product list with constructor
    public AdminTemplateListAdapter(Context mCtx, ArrayList<TemplateData> allCategoryArrayList,
                                   OnItemClickListener listener, String path ) {
        this.mCtx = mCtx;
        this.allCategoryArrayList = allCategoryArrayList;
        this.listener = listener;
        this.path = path;

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_view_template, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //AllCategory AllCategory = allCategoryArrayList.get(position);

        Log.d("ImagePath", "onBindViewHolder: "+path+allCategoryArrayList.get(position).getImage());
        Glide.with(mCtx)
                .load(allCategoryArrayList.get(position).getImage())
                .apply(new RequestOptions().placeholder(R.drawable.default_user).error(R.drawable.default_user).diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(holder.circularImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClickForAdminTemplate(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return allCategoryArrayList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView circularImageView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            circularImageView = itemView.findViewById(R.id.iv_template_image);


        }
    }


}

