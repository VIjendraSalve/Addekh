package com.wht.addekho.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.wht.addekho.Adapter.ViewPagerAdapter_MyAds;
import com.wht.addekho.R;

public class AdFragment extends Fragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ImageView ivHome, ivChat, ivSell, ivHeart, ivAccount;


    public AdFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ad, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.viewPage);
        tabLayout = view.findViewById(R.id.TbTabLayout);

        setViewpgaerAdapter();

    }



    private void setViewpgaerAdapter() {
        ViewPagerAdapter_MyAds viewPagerAdapter_myAds = new ViewPagerAdapter_MyAds(getChildFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter_myAds);
        tabLayout.setupWithViewPager(viewPager);


    }



}
//Sanku