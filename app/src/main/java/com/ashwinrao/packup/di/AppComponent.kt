package com.ashwinrao.packup.di

import com.ashwinrao.packup.view.activity.AddActivity
import com.ashwinrao.packup.view.activity.DetailActivity
import com.ashwinrao.packup.view.activity.EditActivity
import com.ashwinrao.packup.view.fragment.CategoryFilterDialog
import com.ashwinrao.packup.view.fragment.HomeFragment
import com.ashwinrao.packup.view.fragment.pages.BoxesPage
import com.ashwinrao.packup.view.fragment.pages.ItemsPage
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class])
interface AppComponent {

    fun inject(fragment: ItemsPage)
    fun inject(fragment: BoxesPage)
    fun inject(activity: AddActivity)
    fun inject(activity: EditActivity)
    fun inject(fragment: HomeFragment)
    fun inject(activity: DetailActivity)
    fun inject(fragment: CategoryFilterDialog)

}
