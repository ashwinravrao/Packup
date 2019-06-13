package com.ashwinrao.boxray.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.util.BackNavCallback;

public class CameraActivity extends AppCompatActivity {

    private BackNavCallback listener = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CameraFragment(), "CameraFragment").commit();
    }

    public void registerBackNavigationListener(@NonNull BackNavCallback listener) {
        this.listener = listener;
    }

    public void unregisterBackNavigationListener() {
        this.listener = null;
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getFragments().get(0).getClass() == CameraFragment.class) {
            if (listener != null) {
                listener.onBackPressed();
            }
        }
        super.onBackPressed();
    }
}
