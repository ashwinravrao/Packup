package com.ashwinrao.locrate.view.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class ListPagerAdapter extends FragmentStatePagerAdapter {

    private Fragment[] pages = new Fragment[3];
    private String[] pageTitles = new String[]{"Moves", "Boxes", "Items"};

    @SuppressWarnings("deprecation")
    public ListPagerAdapter(@NonNull FragmentManager fm, @NonNull Fragment movesPage, @NonNull Fragment boxesPage, @NonNull Fragment itemsPage) {
        super(fm);
        pages[0] = movesPage;
        pages[1] = boxesPage;
        pages[2] = itemsPage;
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
