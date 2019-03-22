package com.ashwinrao.boxray.view;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class FragmentPager extends FragmentStatePagerAdapter {

    private Fragment[] mFragments;

    public FragmentPager(@NonNull FragmentManager fm, @NonNull Fragment[] fragments) {
        super(fm);
        mFragments = fragments;
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        // preloads the next page for performance
        return mFragments[position];
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.length;
    }
}
