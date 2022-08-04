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

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wht.addekho.Model.AllCategory;
import com.wht.addekho.R;

import java.util.ArrayList;

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.SingleItemRowHolder> {

    public OnItemClickListener listener;
    int selectedPosition = -1;
    private ArrayList<AllCategory> itemsList;
    private Context mContext;
    private AllCategory singleItem;


    public HomeCategoryAdapter(Context context, ArrayList<AllCategory> itemsList, OnItemClickListener listener) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.listener = listener;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_category_search_list_layout, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, @SuppressLint("RecyclerView") int i) {

        singleItem = new AllCategory();
        singleItem = itemsList.get(i);

        holder.tvTitle.setText(singleItem.getName());
        holder.tvTitle.setVisibility(View.VISIBLE);
        if (selectedPosition == i) {

            holder.rlImages.setBackground(mContext.getDrawable(R.drawable.category_selection_border));
            holder.tvTitle.setTextColor(Color.RED);
        } else {
            holder.rlImages.setBackground(null);
            holder.tvTitle.setTextColor(mContext.getResources().getColor(R.color.text_gray));
            //holder.tvTitle.setTextColor(Color.parseColor("#ffbbff"));
        }

        if (singleItem.getName().equals("All")) {
            Glide.with(mContext)
                    .load(R.drawable.ic_down)
                    .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher))
                    .into(holder.itemImage);
            holder.itemImage.setVisibility(View.VISIBLE);
            holder.tvTitle.setVisibility(View.GONE);
            holder.ll_top_layout.setVisibility(View.VISIBLE);
        } else {
            holder.itemImage.setVisibility(View.GONE);
            holder.tvTitle.setVisibility(View.VISIBLE);
            holder.ll_top_layout.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("itemView", "onClick: " + itemsList.get(i).getName());
                Log.d("itemView", "onClick: " + itemsList.get(i).getId());
                selectedPosition = i;
                notifyDataSetChanged();
                if (itemsList.get(i).getName().equals("All")) {
                    listener.onItemClick("-1");
                } else {
                    listener.onItemClick(itemsList.get(i).getId());
                }
                //Toast.makeText(mContext, ""+itemsList.get(i).getName(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public interface OnItemClickListener {
        void onItemClick(String id);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tvTitle;
        protected ImageView itemImage;
        private LinearLayout ll_top_layout;
        private LinearLayout rlImages;

        public SingleItemRowHolder(View view) {
            super(view);

            this.tvTitle = (TextView) view.findViewById(R.id.tvProductName);
            this.itemImage = (ImageView) view.findViewById(R.id.ivProductImg);
            this.ll_top_layout = (LinearLayout) view.findViewById(R.id.ll_top_layout);
            this.rlImages = (LinearLayout) view.findViewById(R.id.rlImages);


        }

    }
}