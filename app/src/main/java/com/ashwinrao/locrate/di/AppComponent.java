package com.ashwinrao.locrate.di;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ashwinrao.locrate.view.activity.AddActivity;
import com.ashwinrao.locrate.view.activity.DetailActivity;
import com.ashwinrao.locrate.view.fragment.HomeFragment;
import com.ashwinrao.locrate.view.fragment.nfc.NfcReadFragment;
import com.ashwinrao.locrate.view.fragment.nfc.NfcWriteFragment;
import com.ashwinrao.locrate.view.fragment.pages.BoxesPage;
import com.ashwinrao.locrate.view.fragment.pages.ItemsPage;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = DatabaseModule.class)
public interface AppComponent {

    void inject(HomeFragment fragment);
    void inject(AddActivity activity);
    void inject(DetailActivity activity);
    void inject(ItemsPage fragment);
    void inject(BoxesPage fragment);
}
