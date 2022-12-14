package com.wht.addekh.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.wht.addekh.Fragments.FragmentADS;
import com.wht.addekh.Fragments.FragmentFavouritesAds;

public class ViewPagerAdapter_MyAds extends FragmentStatePagerAdapter {
    public ViewPagerAdapter_MyAds(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return FragmentADS.newInstanse();
            case 1:
                return FragmentFavouritesAds.newInstanse();
        }
        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String tabName = "";
        switch (position) {
            case 0:
                tabName = "ADS";
                break;
            case 1:
                tabName = "FAVOURITES";
                break;
        }
        return tabName;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
