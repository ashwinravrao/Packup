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

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class MainActivity extends AppCompatActivity {

    private BoxViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getViewModel(getApplication());

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if(fragment == null) {
            fragment = new ListFragment();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.stay_still, R.anim.stay_still, 0, 0)
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    public BoxViewModel getViewModel() {
        return this.viewModel;
    }

    private void getViewModel(Application application) {
        final BoxViewModelFactory factory = BoxViewModelFactory.getInstance(application);
        this.viewModel = factory.create(BoxViewModel.class);
    }

    public View getFragmentContainerView() {
        return this.getWindow().getDecorView().findViewById(R.id.fragment_container);
    }

    public void customToast(int message, boolean useToastVerticalOffset, boolean useCustomView) {
        Toast toast = Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT);
        if(useCustomView) {
            View toastView = getLayoutInflater().inflate(R.layout.view_toast, (LinearLayout) findViewById(R.id.toast_root));
            TextView tv = toastView.findViewById(R.id.message);
            tv.setText(message);
            toast.setView(toastView);
        }
        toast.setGravity(Gravity.BOTTOM, 0, useToastVerticalOffset ? 400 : 200);
        toast.show();
    }
}
