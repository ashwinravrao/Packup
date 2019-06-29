package com.ashwinrao.locrate.view.activity;

import android.os.Bundle;

import com.ashwinrao.locrate.R;
import com.ashwinrao.locrate.util.callback.BackNavCallback;
import com.ashwinrao.locrate.view.fragment.AddFragment;
import com.ashwinrao.locrate.view.fragment.ListFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


public class MainActivity extends AppCompatActivity {

    private BackNavCallback listener = null;

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
                .replace(R.id.fragment_container, fragment, "ListFragment")
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
        final Class topFragment = getSupportFragmentManager().getFragments().get(0).getClass();
        if(topFragment == ListFragment.class || topFragment == AddFragment.class) {
            if (listener != null) {
                listener.onBackPressed();
            }
        }
        super.onBackPressed();
    }
}
