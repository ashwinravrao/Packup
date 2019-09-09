package com.ashwinrao.packup.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

import com.ashwinrao.packup.BuildConfig;
import com.ashwinrao.packup.R;
import com.ashwinrao.packup.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    private String developerContact = BuildConfig.DEVELOPER_CONTACT;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivitySettingsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        configureToolbar(binding.toolbar);
        configureFeedbackSetting(binding.feedbackSetting);
        configureIconsAttribution(binding.iconsAttribution);
    }

    private void configureToolbar(@NonNull Toolbar toolbar) {
        this.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> this.finish());
    }

    private void configureFeedbackSetting(@NonNull ConstraintLayout feedbackSetting) {
        feedbackSetting.setOnClickListener(view -> buildMailIntent(new String[]{developerContact}, getString(R.string.feedback_setting_mail_subject)));
    }

    private void buildMailIntent(@NonNull String[] addresses, @NonNull String subject) {
        final Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void configureIconsAttribution(@NonNull ConstraintLayout iconsAttributionPane) {
        iconsAttributionPane.setOnClickListener(view -> buildBrowserIntent(getString(R.string.icons_attribution_url)));
    }

    private void buildBrowserIntent(@NonNull String url) {
        final Uri webpage = Uri.parse(url);
        final Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
