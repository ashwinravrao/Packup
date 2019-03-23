package com.ashwinrao.boxray.view;

import android.os.Bundle;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.viewmodel.BoxViewModel;
import com.ashwinrao.boxray.viewmodel.BoxViewModelFactory;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * Single fragment host for adding, viewing boxes, and adjusting settings
 */

public class CommonActivity extends AppCompatActivity {

    private BoxViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_common);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container_common);
        if(fragment == null) { fragment = new AddFragment(); }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_common, fragment, "AddFragment").commit();

        // Obtain ViewModel reference
        final BoxViewModelFactory factory = BoxViewModelFactory.getInstance(getApplication());
        mViewModel = factory.create(BoxViewModel.class);

    }

    public BoxViewModel getViewModel() {
        return mViewModel;
    }
}
