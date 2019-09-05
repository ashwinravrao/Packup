package com.ashwinrao.sanbox.di;


import com.ashwinrao.sanbox.view.activity.AddActivity;
import com.ashwinrao.sanbox.view.activity.DetailActivity;
import com.ashwinrao.sanbox.view.activity.EditActivity;
import com.ashwinrao.sanbox.view.fragment.CategoryFilterDialog;
import com.ashwinrao.sanbox.view.fragment.HomeFragment;
import com.ashwinrao.sanbox.view.fragment.pages.BoxesPage;
import com.ashwinrao.sanbox.view.fragment.pages.ItemsPage;

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
