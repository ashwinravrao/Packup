package com.ashwinrao.locrate.di;

import com.ashwinrao.locrate.view.fragment.AddFragment;
import com.ashwinrao.locrate.view.fragment.DetailFragment;
import com.ashwinrao.locrate.view.fragment.HomeFragment;
import com.ashwinrao.locrate.view.fragment.PhotoFragment;
import com.ashwinrao.locrate.view.pages.BoxesPage;
import com.ashwinrao.locrate.view.pages.ItemsPage;
import com.ashwinrao.locrate.view.pages.PhotoPage;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = DatabaseModule.class)
public interface AppComponent {

    void inject(HomeFragment fragment);
    void inject(DetailFragment fragment);
    void inject(AddFragment fragment);
    void inject(PhotoPage fragment);
    void inject(PhotoFragment fragment);
    void inject(ItemsPage fragment);
    void inject(BoxesPage fragment);
}
