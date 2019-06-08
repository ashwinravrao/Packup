package com.ashwinrao.boxray.view;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.util.BackNavigationCallback;

public class AddActivity extends AppCompatActivity {

    private BackNavigationCallback listener = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new AddFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    public void registerBackNavigationListener(@NonNull BackNavigationCallback listener) {
        this.listener = listener;
    }

    public void unregisterBackNavigationListener() {
        this.listener = null;
    }

    public View getFragmentContainerView() {
        return this.getWindow().getDecorView().findViewById(R.id.fragment_container);
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getFragments().get(0).getClass() == AddFragment.class) {
            if (listener != null) {
                listener.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }
}
