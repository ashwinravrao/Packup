package com.ashwinrao.boxray.view;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ashwinrao.boxray.R;

public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CameraFragment()).commit();
    }
}
