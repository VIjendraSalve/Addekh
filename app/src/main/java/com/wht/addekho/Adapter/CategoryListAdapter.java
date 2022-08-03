package com.wht.addekho.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.wht.addekho.Model.AllCategory;
import com.wht.addekho.R;

import java.util.ArrayList;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.SingleItemRowHolder> {

    private ArrayList<AllCategory> itemsList;
    private Context mContext;
    private AllCategory singleItem;
    public OnItemClickListener listener;



    public interface OnItemClickListener {
        void onItemClick(int id);
    }


    public CategoryListAdapter(Context context, ArrayList<AllCategory> itemsList, OnItemClickListener listener) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.listener = listener;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_list_layout, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, @SuppressLint("RecyclerView") int i) {

        singleItem = new AllCategory();
        singleItem = itemsList.get(i);



        holder.tv_Producttitle.setText(singleItem.getName());

        Glide.with(mContext)
                .load(itemsList.get(i).getImage())
                .apply(new RequestOptions().placeholder(R.drawable.default_user).error(R.drawable.default_user).diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(holder.imageView_product);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("itemView", "onClick: "+itemsList.get(i).getName());
                Log.d("itemView", "onClick: "+itemsList.get(i).getId());


                if(itemsList.get(i).getName().equals("All")) {
                    listener.onItemClick(-1);
                }else {
                    listener.onItemClick(i);
                }
                //Toast.makeText(mContext, ""+itemsList.get(i).getName(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tv_Producttitle;
        protected ImageView imageView_product;

        public SingleItemRowHolder(View view) {
            super(view);

            this.tv_Producttitle = (TextView) view.findViewById(R.id.tv_Producttitle);
            this.imageView_product = (ImageView) view.findViewById(R.id.imageView_product);


        }

    }
}