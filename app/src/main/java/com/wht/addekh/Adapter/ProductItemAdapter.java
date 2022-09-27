package com.wht.addekh.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.wht.addekh.Fragments.ProductsFragment;

public class ProductItemAdapter extends FragmentPagerAdapter {

    int tabCounts;

    public ProductItemAdapter(@NonNull FragmentManager fm, int tabCounts) {
        super(fm);
        this.tabCounts = tabCounts;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ProductsFragment();
            case 1:
                return new ProductsFragment();
            case 2:
                return new ProductsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCounts;
    }
}

