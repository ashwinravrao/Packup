package com.ashwinrao.locrate.view.fragment;

import android.content.Context;
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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class BottomSheetFragment extends BottomSheetDialogFragment implements NavigationView.OnNavigationItemSelectedListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentBottomSheetBinding binding = FragmentBottomSheetBinding.inflate(inflater);
        binding.navigationView.setNavigationItemSelectedListener(this);
        binding.setUsername(getUserDetails()[0]);
        binding.setUserEmail(getUserDetails()[1]);
        return binding.getRoot();
    }

    private String[] getUserDetails() {
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("userdata", Context.MODE_PRIVATE);
        return new String[]{pref.getString("name", "Guest"), pref.getString("email", "Tap to sign in")};
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment current = Objects.requireNonNull(getActivity()).getSupportFragmentManager().getFragments().get(0);
        switch (item.getItemId()) {
            case R.id.home:
                if(current.getClass() != ListFragment.class) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ListFragment()).commit();
                }
                this.dismiss();
                return true;
        }
        return false;
    }
}
