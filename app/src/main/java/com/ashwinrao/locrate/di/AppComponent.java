package com.ashwinrao.locrate.di;

import com.ashwinrao.locrate.view.fragment.AddFragment;
import com.ashwinrao.locrate.view.fragment.DetailFragment;
import com.ashwinrao.locrate.view.fragment.ListFragment;
import com.ashwinrao.locrate.view.fragment.PhotoReviewFragment;
import com.ashwinrao.locrate.view.pages.ListBoxesPageFragment;
import com.ashwinrao.locrate.view.pages.ListItemsPageFragment;
import com.ashwinrao.locrate.view.pages.ReviewPageFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = DatabaseModule.class)
public interface AppComponent {

    void inject(ListFragment fragment);
    void inject(DetailFragment fragment);
    void inject(AddFragment fragment);
    void inject(ReviewPageFragment fragment);
    void inject(PhotoReviewFragment fragment);
    void inject(ListItemsPageFragment fragment);
    void inject(ListBoxesPageFragment fragment);
}