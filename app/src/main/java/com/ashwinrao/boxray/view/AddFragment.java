package com.ashwinrao.boxray.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.databinding.FragmentAddBinding;
import com.ashwinrao.boxray.util.Utilities;
import com.ashwinrao.boxray.view.pages.BoxItemsPage;
import com.ashwinrao.boxray.view.pages.BoxNumberPage;
import com.ashwinrao.boxray.view.pages.BoxPhotoPage;
import com.ashwinrao.boxray.view.pages.BoxPropertiesPage;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

public class AddFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentAddBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add, container, false);

        final Fragment[] fragments = {new BoxPropertiesPage(), new BoxItemsPage(), new BoxPhotoPage(), new BoxNumberPage()};

        final FragmentPager adapter = new FragmentPager(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), fragments);

        final ScrollOptionalViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(adapter);
        viewPager.setScrollingBehavior(false);

        ((CommonActivity) getActivity()).getViewModel().getFieldsSatisfied().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean) {
                    // set next button visible
                    binding.nextButton.setVisibility(View.VISIBLE);
                } else {
                    // set next button invisible
                    binding.nextButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        addPageIndicator(binding.indicatorLayout, 0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addPageIndicator(binding.indicatorLayout, position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return binding.getRoot();
    }

    private void addPageIndicator(@NonNull LinearLayout linearLayout, int position) {

        TextView[] indicators = new TextView[4];

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new TextView(getActivity());
            indicators[i].setText(Utilities.fromHtml("&#8226;"));
            indicators[i].setTextSize(35);
            indicators[i].setPadding(0, 0, (int) Utilities.dpToPx(Objects.requireNonNull(getActivity()), 4), 0);
            if (i != position) {
                indicators[i].setTextColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.grayDots));
            } else {
                indicators[i].setTextColor(ContextCompat.getColor(getActivity(), android.R.color.white));
            }
            linearLayout.addView(indicators[i]);
        }
    }

}
