package com.ashwinrao.packup.view.fragment;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.ashwinrao.packup.R;

public class PreferenceFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
