package com.wht.addekh.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wht.addekh.Activties.ProductDetailActivity;
import com.wht.addekh.Model.ProductItemModel;
import com.wht.addekh.R;

import java.util.List;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    List<ProductItemModel> list;
    Context context;

    public ProductAdapter(List<ProductItemModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder,  int position) {
        holder.product_image.setImageResource(list.get(position).getProduct_image());
        holder.product_name.setText(list.get(position).getProduct_name());
//        holder.product_price.setText(list.get(position).getProduct_price());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("product_name", list.get(position).getProduct_name());
//                intent.putExtra("product_price", list.get(position).getProduct_price());
                intent.putExtra("product_image", list.get(position).getProduct_image());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView product_image;
        TextView product_name, product_price;
        View view;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            product_image = itemView.findViewById(R.id.product_image);
            product_name = itemView.findViewById(R.id.product_name);
//            product_price = itemView.findViewById(R.id.product_price);


        }
    }
}
