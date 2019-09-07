package com.ashwinrao.packup.di;


import com.ashwinrao.packup.view.activity.AddActivity;
import com.ashwinrao.packup.view.activity.DetailActivity;
import com.ashwinrao.packup.view.activity.EditActivity;
import com.ashwinrao.packup.view.fragment.CategoryFilterDialog;
import com.ashwinrao.packup.view.fragment.HomeFragment;
import com.ashwinrao.packup.view.fragment.pages.BoxesPage;
import com.ashwinrao.packup.view.fragment.pages.ItemsPage;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = DatabaseModule.class)
public interface AppComponent {

    void inject(HomeFragment fragment);
    void inject(AddActivity activity);
    void inject(EditActivity activity);
    void inject(DetailActivity activity);
    void inject(ItemsPage fragment);
    void inject(BoxesPage fragment);
    void inject(CategoryFilterDialog fragment);
}
