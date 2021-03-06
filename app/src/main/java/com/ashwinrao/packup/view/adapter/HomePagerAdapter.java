package com.ashwinrao.packup.view.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class HomePagerAdapter extends FragmentStatePagerAdapter {

    private Fragment[] pages = new Fragment[2];
    private String[] pageTitles = new String[]{"Boxes", "Items"};

    @SuppressWarnings("deprecation")
    public HomePagerAdapter(@NonNull FragmentManager fm, @NonNull Fragment boxesPage, @NonNull Fragment itemsPage) {
        super(fm);
        pages[0] = boxesPage;
        pages[1] = itemsPage;
    }

    public void setPages(@NonNull Fragment[] pages) {
        this.pages = pages;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return pages[position];
    }

    @Override
    public int getCount() {
        return pages.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitles[position];
    }
}
