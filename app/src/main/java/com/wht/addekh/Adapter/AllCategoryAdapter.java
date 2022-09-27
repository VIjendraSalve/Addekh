package com.wht.addekh.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.wht.addekh.Model.AllCategory;
import com.wht.addekh.R;

import java.util.ArrayList;

public class AllCategoryAdapter extends RecyclerView.Adapter<AllCategoryAdapter.ProductViewHolder> {

    private Context mCtx;
    //we are storing all the products in a list
    private ArrayList<AllCategory> allCategoryArrayList;


    //getting the context and product list with constructor
    public AllCategoryAdapter(Context mCtx, ArrayList<AllCategory> allCategoryArrayList) {
        this.mCtx = mCtx;
        this.allCategoryArrayList = allCategoryArrayList;

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.category_list_layout, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        //AllCategory AllCategory = allCategoryArrayList.get(position);
        holder.tv_Producttitle.setText(allCategoryArrayList.get(position).getName());
        Log.d("productti", "onBindViewHolder: "+allCategoryArrayList.get(position).getName());
//        holder.tv_Producttitle.setText(String.valueOf(AllCategory.getProduct_title()));
//        holder.tv_firstName.setText(String.valueOf(allCategoryArrayList.get(position).getUser_first_name() +" "+ allCategoryArrayList.get(position).getUser_last_name()));
//        holder.tv_email.setText(String.valueOf(allCategoryArrayList.get(position).getRole_email()));

        Log.d("imagetest", "onBindViewHolder: " + allCategoryArrayList.get(position).getImage());

        Glide.with(mCtx)
                .load(allCategoryArrayList.get(position).getImage())
                .apply(new RequestOptions().placeholder(R.drawable.default_user).error(R.drawable.default_user).diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(holder.circularImageView);


    }

    @Override
    public int getItemCount() {
        return allCategoryArrayList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_Producttitle, tv_firstName, tv_email;
        CircularImageView circularImageView;
        ImageView imageView;

        public ProductViewHolder(View itemView) {
            super(itemView);
            tv_Producttitle = itemView.findViewById(R.id.tv_Producttitle);
//            tv_firstName = itemView.findViewById(R.id.tv_firstName);
//            tv_email = itemView.findViewById(R.id.tv_email);

            circularImageView = itemView.findViewById(R.id.imageView_product);
//        imageView = itemView.findViewById(R.id.imageView_email);


        }
    }


}

