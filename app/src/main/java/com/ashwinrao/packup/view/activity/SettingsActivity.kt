package com.ashwinrao.packup.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.ashwinrao.packup.BuildConfig
import com.ashwinrao.packup.R
import com.ashwinrao.packup.databinding.ActivitySettingsBinding
import kotlinx.android.synthetic.main.activity_settings.*
import com.ashwinrao.packup.util.HideShowNotch


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivitySettingsBinding>(
                this@SettingsActivity,
                R.layout.activity_settings)
        setupToolbar(toolbar)
        listenToHideNotchSetting(hide_notch_checkbox)
        listenToFeedbackSetting(feedback_setting)
        listenToIconAttribution(icons_attribution)
    }

    override fun onResume() {
        super.onResume()
        hide_notch_checkbox.isChecked = HideShowNotch.apply(this, window, R.color.colorPrimaryDark)
    }

    private fun setupToolbar(toolbar: Toolbar) {
        this.setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }
    }

    private fun listenToHideNotchSetting(checkBox: CheckBox) {
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            application
                    .getSharedPreferences(getString(R.string.settings_shared_preference), Context.MODE_PRIVATE)
                    .edit()
                    .putBoolean(getString(R.string.hide_notch_key), isChecked)
                    .apply()

            HideShowNotch.contextSpecific(this, window, isChecked, R.color.colorPrimaryDark)
        }
    }

    private fun listenToFeedbackSetting(view: View) {
        view.setOnClickListener { createMailIntent() }
    }

    private fun listenToIconAttribution(view: View) {
        view.setOnClickListener { createBrowserIntent() }
    }

    private fun createMailIntent() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, BuildConfig.DEVELOPER_CONTACT)
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_mail_subject))
        if (intent.resolveActivity(packageManager) != null) startActivity(intent)
    }

    private fun createBrowserIntent() {
        val webPage = Uri.parse(getString(R.string.flaticon_attribution_gallery))
        val intent = Intent(Intent.ACTION_VIEW, webPage)
        if (intent.resolveActivity(packageManager) != null) startActivity(intent)
    }

    private fun clearDefaults() {}

}