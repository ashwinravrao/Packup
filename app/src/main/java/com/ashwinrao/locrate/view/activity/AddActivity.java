package com.ashwinrao.locrate.view.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ashwinrao.locrate.R;
import com.ashwinrao.locrate.util.callback.BackNavCallback;
import com.ashwinrao.locrate.view.fragment.AddFragment;
import com.ashwinrao.locrate.view.fragment.ListFragment;

import java.util.Objects;

public class AddActivity extends AppCompatActivity {

    private BackNavCallback listener = null;
    private static final String TAG = "AddActivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new AddFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, "AddFragment").commit();
    }

    public void registerBackNavigationListener(@NonNull BackNavCallback listener) {
        this.listener = listener;
    }

    public void unregisterBackNavigationListener() {
        this.listener = null;
    }

    @Override
    public void onBackPressed() {
        final Fragment addFragment = getSupportFragmentManager().findFragmentByTag("AddFragment");
        if(addFragment != null && addFragment.isVisible()) {
            if(listener != null) listener.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }
}
