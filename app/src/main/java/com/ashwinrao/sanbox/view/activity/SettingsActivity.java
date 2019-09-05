package com.ashwinrao.sanbox.view.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.ashwinrao.sanbox.R;
import com.ashwinrao.sanbox.databinding.ActivitySettingsBinding;
import com.ashwinrao.sanbox.view.fragment.PreferenceFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivitySettingsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);

        initializeToolbar(binding.toolbar);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,
                        new PreferenceFragment(),
                        "PreferenceFragment")
                .commit();
    }

    private void initializeToolbar(@NonNull Toolbar toolbar) {
        this.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> this.finish());
    }
}
