package com.ashwinrao.boxray.view;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.databinding.FragmentAddBinding;
import com.ashwinrao.boxray.util.Utilities;
import com.ashwinrao.boxray.view.pages.ContentsPageTwoFragment;
import com.ashwinrao.boxray.view.pages.DetailsPageOneFragment;
import com.ashwinrao.boxray.view.pages.NumberPageFourFragment;
import com.ashwinrao.boxray.view.pages.PhotoPageThreeFragment;
import com.ashwinrao.boxray.viewmodel.BoxViewModel;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

public class AddFragment extends Fragment {

    private static final String TAG = "AddBox";
    private BoxViewModel viewModel;
    private FragmentManager fragmentManager;
    private Resources.Theme appTheme;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        appTheme = getActivity().getTheme();
        viewModel = ((MainActivity) Objects.requireNonNull(getActivity())).getViewModel();
        viewModel.recreateBox();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentAddBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add, container, false);

        final Fragment[] fragments = {new DetailsPageOneFragment(), new ContentsPageTwoFragment(), new PhotoPageThreeFragment(), new NumberPageFourFragment()};

        final FragmentPager adapter = new FragmentPager(fragmentManager, fragments);
        final ScrollOptionalViewPager viewPager = binding.viewpager;
        viewPager.setAdapter(adapter);
        viewPager.setScrollingBehavior(true);
        viewPager.setOffscreenPageLimit(4); // to retain page fields in memory

        addViewPagerIndicator(binding.indicators, 0, fragments.length);
        viewPager.addOnPageChangeListener(pageChangeListener(binding.indicators, fragments.length));

        configureIndicatorBehaviorOnSoftKeyboardOpen(binding);

        viewModel.getShouldGoToInitialPage().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.d(TAG, "shouldGoToInitialPage set to: " + aBoolean);
                if (aBoolean) {
                    viewPager.setCurrentItem(0);
                }
            }
        });

        viewModel.getIsAddComplete().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    fragmentManager.popBackStack();
                }
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.setShouldGoToInitialPage(false);
        viewModel.setIsAddComplete(false);
    }

    private void configureIndicatorBehaviorOnSoftKeyboardOpen(@NonNull final FragmentAddBinding binding) {
        binding.getRoot().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                hideIndicatorOnSoftInputVisible(binding.rootViewGroup, binding.indicators);
            }
        });
    }

    private void hideIndicatorOnSoftInputVisible(@NonNull ViewGroup root, @NonNull View viewToHide) {
        if (Utilities.keyboardIsShowing(root)) {
            viewToHide.setVisibility(View.INVISIBLE);
        } else {
            viewToHide.setVisibility(View.VISIBLE);
        }
    }

    private void addViewPagerIndicator(@NonNull ViewGroup dotsContainer, int position, int numFragments) {
        TextView[] dots = new TextView[numFragments];
        dotsContainer.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            TextView dot = new TextView(this.getContext());
            dot.setText(Utilities.fromHtml("&#8226"));
            dot.setTextSize(35);
            dot.setTextColor(getResources().getColor(R.color.toastBackground, appTheme));
            dots[i] = dot;
            dotsContainer.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.iconAndTextColor, appTheme));
        }
    }

    private ViewPager.OnPageChangeListener pageChangeListener(@NonNull final ViewGroup container, final int numFragments) {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addViewPagerIndicator(container, position, numFragments);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
    }
}
