package uk.colessoft.android.hilllist.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import uk.colessoft.android.hilllist.activity.DisplayHillListFragmentActivity

@Module
abstract class AndroidInjectorsModule {

    @ContributesAndroidInjector(modules = [DisplayHillListFragmentActivityIntentModule::class])
    abstract fun contributeDisplayHillListActivity(): DisplayHillListFragmentActivity
}