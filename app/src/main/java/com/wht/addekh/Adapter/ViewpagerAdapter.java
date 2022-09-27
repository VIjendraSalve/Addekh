package com.wht.addekh.Adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.wht.addekh.Fragments.HomeFragment;
import com.wht.addekh.Fragments.ProductsFragment;

public class ViewpagerAdapter extends FragmentPagerAdapter {
    private Context mycontext;
    int totaltabs;
    public ViewpagerAdapter(FragmentManager fm) {
        super( fm );
        //mycontext = context;
        this.totaltabs= totaltabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                HomeFragment menu1Fragment = new HomeFragment();
                return menu1Fragment;
            case 1:
                ProductsFragment menu1Fragment2 = new ProductsFragment();
                return menu1Fragment2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totaltabs;
    }
}