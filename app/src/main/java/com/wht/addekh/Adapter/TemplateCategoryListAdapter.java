package com.wht.addekh.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wht.addekh.Model.TemplateCategory;
import com.wht.addekh.R;

import java.util.ArrayList;

public class TemplateCategoryListAdapter extends RecyclerView.Adapter<TemplateCategoryListAdapter.SingleItemRowHolder> {

    private ArrayList<TemplateCategory> itemsList;
    private Context mContext;
    private TemplateCategory singleItem;
    public OnItemClickListener listener;




    public interface OnItemClickListener {
        void onItemClick(int id);
    }


    public TemplateCategoryListAdapter(Context context, ArrayList<TemplateCategory> itemsList,
                                       OnItemClickListener listener) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.listener = listener;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.template_category_list_layout, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, @SuppressLint("RecyclerView") int i) {

        singleItem = new TemplateCategory();
        singleItem = itemsList.get(i);



        holder.tv_Producttitle.setText(singleItem.getName());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("itemView", "onClick: "+itemsList.get(i).getName());
                Log.d("itemView", "onClick: "+itemsList.get(i).getId());


                listener.onItemClick(i);
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

        public SingleItemRowHolder(View view) {
            super(view);

            this.tv_Producttitle = (TextView) view.findViewById(R.id.tv_Producttitle);


        }

    }
}