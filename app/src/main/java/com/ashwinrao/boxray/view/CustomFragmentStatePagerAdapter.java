package com.ashwinrao.boxray.view;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class CustomFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    private Fragment[] fragments;

    public CustomFragmentStatePagerAdapter(@NonNull FragmentManager fm, @NonNull Fragment[] fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) { return fragments[position]; }

    @Override
    public int getCount() { return fragments == null ? 0 : fragments.length; }
}
