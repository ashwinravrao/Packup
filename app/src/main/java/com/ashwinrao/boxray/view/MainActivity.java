package com.ashwinrao.boxray.view;

import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.view.pages.ContentsPageTwoFragment;
import com.ashwinrao.boxray.view.pages.DetailsPageOneFragment;
import com.ashwinrao.boxray.view.pages.NumberPageFourFragment;
import com.ashwinrao.boxray.view.pages.PhotoPageThreeFragment;
import com.ashwinrao.boxray.viewmodel.BoxViewModel;
import com.ashwinrao.boxray.viewmodel.BoxViewModelFactory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class MainActivity extends AppCompatActivity {

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

    public View getFragmentContainerView() {
        return this.getWindow().getDecorView().findViewById(R.id.fragment_container);
    }
}
