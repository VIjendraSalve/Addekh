package com.wht.addekho.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wht.addekho.Adapter.ProductAdapter;
import com.wht.addekho.Model.ProductItemModel;
import com.wht.addekho.R;

import java.util.ArrayList;
import java.util.List;

public class ProductsFragment extends Fragment {

    RecyclerView recyclerView;
    ProductAdapter productAdapter;
    List<ProductItemModel> list;

    public ProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        list = new ArrayList<>();
        list.add(new ProductItemModel(R.drawable.ban2, "Product name 1", "Product price"));
        list.add(new ProductItemModel(R.drawable.ban1, "Product name 2", " Product price"));
        list.add(new ProductItemModel(R.drawable.mobile1, "Product name 3", "Product price"));
        list.add(new ProductItemModel(R.drawable.ban, "Product name 4", "Product price"));
        list.add(new ProductItemModel(R.drawable.mobile1, "Product name 5", "Product price"));
        list.add(new ProductItemModel(R.drawable.ban2, "Product name 6", "Product price"));
        list.add(new ProductItemModel(R.drawable.mobile1, "Product name 7", "Product price"));
        list.add(new ProductItemModel(R.drawable.ban, "Product name 8", "Product price"));
        list.add(new ProductItemModel(R.drawable.mobile1, "Product name 9", "Product price"));
        list.add(new ProductItemModel(R.drawable.ban1, "Product name 10", "Product price"));

        recyclerView = view.findViewById(R.id.product_recycler);
        productAdapter = new ProductAdapter(list, getActivity().getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(productAdapter);
        return view;


    }
}