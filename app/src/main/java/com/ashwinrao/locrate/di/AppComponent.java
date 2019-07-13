package com.ashwinrao.locrate.di;

import com.ashwinrao.locrate.view.activity.DetailActivity;
import com.ashwinrao.locrate.view.fragment.AddFragment;
import com.ashwinrao.locrate.view.fragment.DetailFragment;
import com.ashwinrao.locrate.view.fragment.HomeFragment;
import com.ashwinrao.locrate.view.pages.BoxesPage;
import com.ashwinrao.locrate.view.pages.ItemsPage;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = DatabaseModule.class)
public interface AppComponent {

    void inject(HomeFragment fragment);
    void inject(DetailFragment fragment);
    void inject(DetailActivity activity);
    void inject(AddFragment fragment);
    void inject(ItemsPage fragment);
    void inject(BoxesPage fragment);
}
