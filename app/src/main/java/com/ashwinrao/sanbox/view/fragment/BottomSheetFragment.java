package com.ashwinrao.sanbox.view.fragment;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ashwinrao.sanbox.R;
import com.ashwinrao.sanbox.databinding.FragmentBottomSheetBinding;
import com.ashwinrao.sanbox.view.activity.SettingsActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.Date;
import java.util.Objects;

public class BottomSheetFragment extends BottomSheetDialogFragment implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "BottomSheetFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentBottomSheetBinding binding = FragmentBottomSheetBinding.inflate(inflater);
        binding.navigationView.setNavigationItemSelectedListener(this);
        binding.greeting.setText(getTimeDependentGreeting());
        return binding.getRoot();
    }

    private String getTimeDependentGreeting() {
        final SimpleDateFormat formatter = new SimpleDateFormat("HH");
        final Date date = new Date();
        final int hour = Integer.valueOf(formatter.format(date));
        if(hour >= 1 && hour < 12) {
            return "Good morning!";
        } else if(hour >= 12 && hour <= 17) {
            return "Good afternoon!";
        } else {
            return "Good evening!";
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                final Fragment list = Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentByTag("HomeFragment");
                if(list != null && !list.isVisible()) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                }
                this.dismiss();
                return true;
            case R.id.settings:
                final Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                this.dismiss();
                return true;
        }
        return false;
    }
}
