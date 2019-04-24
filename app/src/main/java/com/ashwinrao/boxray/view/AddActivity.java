package com.ashwinrao.boxray.view;

import android.app.Application;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.viewmodel.BoxViewModel;
import com.ashwinrao.boxray.viewmodel.BoxViewModelFactory;

public class AddActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

//        getViewModel(getApplication());

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new AddFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

//    public BoxViewModel getViewModel() {
//        return this.viewModel;
//    }
//
//    private void getViewModel(Application application) {
//        final BoxViewModelFactory factory = BoxViewModelFactory.getInstance(application);
//        this.viewModel = factory.create(BoxViewModel.class);
//    }

    public View getFragmentContainerView() {
        return this.getWindow().getDecorView().findViewById(R.id.fragment_container);
    }
}
