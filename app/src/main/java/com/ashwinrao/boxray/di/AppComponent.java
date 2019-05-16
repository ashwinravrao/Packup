package com.ashwinrao.boxray.di;

import com.ashwinrao.boxray.view.AddFragment;
import com.ashwinrao.boxray.view.DetailFragment;
import com.ashwinrao.boxray.view.ListFragment;
import com.ashwinrao.boxray.view.pages.ContentsPageTwoFragment;
import com.ashwinrao.boxray.view.pages.DetailsPageOneFragment;
import com.ashwinrao.boxray.view.pages.NumberPageFourFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = DatabaseModule.class)
public interface AppComponent {

    void inject(ListFragment fragment);
    void inject(DetailFragment fragment);
    void inject(AddFragment fragment);
    void inject(DetailsPageOneFragment fragment);
    void inject(ContentsPageTwoFragment fragment);
    void inject(NumberPageFourFragment fragment);

}
