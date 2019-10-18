package com.ashwinrao.packup.view.activity;

import android.os.Bundle;
import android.transition.Fade;
import android.view.View;

import com.ashwinrao.packup.R;
import com.ashwinrao.packup.util.HideShowNotch;
import com.ashwinrao.packup.util.callback.BackNavCallback;
import com.ashwinrao.packup.view.fragment.HomeFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;


public class MainActivity extends AppCompatActivity {

    private BackNavCallback listener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Exclude certain window elements from participating in activity fade transition
        final Fade fade = new Fade();
        final View decor = getWindow().getDecorView();
        fade.excludeTarget(decor.findViewById(R.id.action_bar_container), true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        getWindow().setEnterTransition(fade);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if(fragment == null) {
            fragment = new HomeFragment();
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, "HomeFragment")
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        HideShowNotch.apply(this, getWindow(), R.color.colorPrimary, true);
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
        if(topFragment == HomeFragment.class) {
            if (listener != null) {
                if(!listener.onBackPressed()) {
                    super.onBackPressed();
                }
            }
        } else {
            super.onBackPressed();
        }
    }

    public void startActionMode(ActionMode.Callback callback) {
        this.startSupportActionMode(callback);
    }
}
