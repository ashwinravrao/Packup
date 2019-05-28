package com.ashwinrao.boxray.di;

import com.ashwinrao.boxray.view.AddFragment;
import com.ashwinrao.boxray.view.CameraFragment;
import com.ashwinrao.boxray.view.DetailFragment;
import com.ashwinrao.boxray.view.ListFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = DatabaseModule.class)
public interface AppComponent {

    void inject(ListFragment fragment);
    void inject(DetailFragment fragment);
    void inject(AddFragment fragment);
    void inject(CameraFragment fragment);

}
