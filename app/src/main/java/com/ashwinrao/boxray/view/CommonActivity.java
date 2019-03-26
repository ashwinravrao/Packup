package com.ashwinrao.boxray.view;

import android.os.Bundle;
import android.view.MenuItem;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.viewmodel.BoxViewModel;
import com.ashwinrao.boxray.viewmodel.BoxViewModelFactory;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * Single fragment host for adding/viewing boxes and adjusting settings (AddFragment, DetailFragment, SettingsFragment)
 */

public class CommonActivity extends AppCompatActivity {

    private BoxViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        configureToolbar();

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container_common);
        if(fragment == null) { fragment = new AddFragment(); }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_common, fragment, "AddFragment").commit();

        final BoxViewModelFactory factory = BoxViewModelFactory.getInstance(getApplication());
        viewModel = factory.create(BoxViewModel.class);
    }

    protected void configureToolbar() {
        ActionBar bar = getSupportActionBar();
        if(bar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     *
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            case android.R.id.home:
                this.finish();
        }
        return true;
    }

    public BoxViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_to_right);
    }
}
