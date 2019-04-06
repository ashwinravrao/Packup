package com.ashwinrao.boxray.view;

import android.os.Bundle;
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
import com.ashwinrao.boxray.view.pages.PhotoPageThreeFragment;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class AddFragment extends Fragment {

    private int currentPage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentAddBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add, container, false);

        // Configure viewpager "pages" in order of appearance
        final Fragment[] fragments = { new DetailsPageOneFragment(), new ContentsPageTwoFragment(), new PhotoPageThreeFragment()};

//         Configure toolbar
//        Toolbar toolbar = binding.toolbar;
//        toolbar.inflateMenu(R.menu.menu_toolbar_add);
//        toolbar.setOnMenuItemClickListener(this);

        // Configure viewpager + adapter
        final FragmentPager adapter = new FragmentPager(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), fragments);
        final ScrollOptionalViewPager viewPager = binding.viewpager;
//        ((MainActivity) getActivity()).getViewModel().setWasSwiped(viewPager.getWasSwiped()); todo maybe re-enable?
        viewPager.setAdapter(adapter);
        viewPager.setScrollingBehavior(true);
//        ((MainActivity) getActivity()).getViewModel().setCanViewPagerAdvance(false);

        // Configure viewpager dot indicators
        addDotsIndicator(binding.indicators, 0, fragments.length);
        viewPager.addOnPageChangeListener(pageChangeListener(binding.indicators, fragments.length));

//        ((MainActivity) getActivity()).getViewModel().getCanViewPagerAdvance().observe(this, new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean aBoolean) {
//                viewPager.setScrollingBehavior(aBoolean);
//            }
//        });

        configureSoftInputBackgroundViewBehavior(binding);

        return binding.getRoot();
    }

    private void configureSoftInputBackgroundViewBehavior(@NonNull final FragmentAddBinding binding) {
        binding.getRoot().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                hideBackgroundViewOnSoftInputVisible(binding.rootViewGroup, binding.indicators);
            }
        });
    }

    private void hideBackgroundViewOnSoftInputVisible(@NonNull ViewGroup root, @NonNull View viewToHide) {
        // Hides the specified view when the soft keyboard is visible, so as not to resize along with other views
        if(Utilities.keyboardIsShowing(root)) { viewToHide.setVisibility(View.INVISIBLE); }
        else { viewToHide.setVisibility(View.VISIBLE); }
    }

    private void addDotsIndicator(@NonNull ViewGroup dotsContainer, int position, int numFragments) {
        TextView[] dots = new TextView[numFragments];
        dotsContainer.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            TextView dot = new TextView(this.getContext());
            dot.setText(Utilities.fromHtml("&#8226"));
            dot.setTextSize(35);
            dot.setTextColor(getResources().getColor(R.color.toastBackground, Objects.requireNonNull(getActivity()).getTheme()));
            dots[i] = dot;
            dotsContainer.addView(dots[i]);
        }

        if(dots.length > 0) { dots[position].setTextColor(getResources().getColor(R.color.iconAndTextColor, getActivity().getTheme())); }
    }

    private ViewPager.OnPageChangeListener pageChangeListener(@NonNull final ViewGroup container, final int numFragments) {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                addDotsIndicator(container, position, numFragments);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
    }


//    @Override
//    public boolean onMenuItemClick(MenuItem item) {
//        switch(item.getItemId()) {
//            case R.id.toolbar_help:
//                ((MainActivity) Objects.requireNonNull(getActivity())).customToast(R.string.help_placeholder, false, true);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}
