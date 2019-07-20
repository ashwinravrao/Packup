package com.ashwinrao.locrate.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ashwinrao.locrate.R;
import com.ashwinrao.locrate.databinding.FragmentBottomSheetBinding;
import com.ashwinrao.locrate.view.activity.SettingsActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class BottomSheetFragment extends BottomSheetDialogFragment implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "BottomSheetFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentBottomSheetBinding binding = FragmentBottomSheetBinding.inflate(inflater);
        binding.navigationView.setNavigationItemSelectedListener(this);
        binding.setAppUserName(getUserDetails()[0]);
        binding.setAppUserEmail(getUserDetails()[1]);
        return binding.getRoot();
    }

    private String[] getUserDetails() {
        SharedPreferences pref = Objects.requireNonNull(getActivity()).getApplicationContext().getSharedPreferences("userdata", Context.MODE_PRIVATE);
        return new String[]{pref.getString("name", "Guest"), pref.getString("email", "Tap to sign in")};
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
