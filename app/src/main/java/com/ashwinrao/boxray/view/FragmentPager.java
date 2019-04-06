package com.ashwinrao.boxray.view;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.LiveData;

public class FragmentPager extends FragmentStatePagerAdapter {

    private Fragment[] fragments;
    private LiveData<Boolean> wasSwiped;

    public FragmentPager(@NonNull FragmentManager fm, @NonNull Fragment[] fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public void passWasSwipedObservable(LiveData<Boolean> wasSwiped) {
        this.wasSwiped = wasSwiped;
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
        return fragments[position];
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.length;
    }
}
