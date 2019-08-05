package uk.colessoft.android.hilllist.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import uk.colessoft.android.hilllist.ui.activity.DisplayHillListFragmentActivity

@Module
abstract class AndroidInjectorsModule {

    @ContributesAndroidInjector(modules = [DisplayHillListFragmentActivityIntentModule::class])
    abstract fun contributeDisplayHillListActivity(): DisplayHillListFragmentActivity
}