package com.ashwinrao.boxray.view;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.util.BackNavigationListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


public class MainActivity extends AppCompatActivity {

    private BackNavigationListener listener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if(fragment == null) {
            fragment = new ListFragment();
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    public void registerBackNavigationListener(@NonNull BackNavigationListener listener) {
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
        if(getSupportFragmentManager().getFragments().get(0).getClass() == ListFragment.class) {
            if(listener != null) {
                listener.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }
}
