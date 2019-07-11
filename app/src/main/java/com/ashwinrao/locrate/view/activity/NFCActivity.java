package com.ashwinrao.locrate.view.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ashwinrao.locrate.R;
import com.ashwinrao.locrate.util.callback.BackNavCallback;
import com.ashwinrao.locrate.view.fragment.NFCReadFragment;
import com.ashwinrao.locrate.view.fragment.NFCWriteFragment;

import java.util.Objects;


public class NFCActivity extends AppCompatActivity {

    private BackNavCallback listener = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (getIntent().getExtras() != null && fragment == null) {
            final String extra = getIntent().getExtras().getString("fragment");
            if(extra != null) {
                if(extra.toLowerCase().startsWith("r")) {
                    fragment = new NFCReadFragment();
                } else {
                    fragment = new NFCWriteFragment();
                }
            }
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,
                        Objects.requireNonNull(fragment),
                        fragment.getTag())
                .commit();
    }

    public void registerBackNavigationListener(@NonNull BackNavCallback listener) {
        this.listener = listener;
    }

    public void unregisterBackNavigationListener() {
        this.listener = null;
    }

    @Override
    public void onBackPressed() {
        if(listener != null) {
            listener.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

}
