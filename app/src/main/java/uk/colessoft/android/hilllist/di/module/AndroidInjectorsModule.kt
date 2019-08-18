package uk.colessoft.android.hilllist.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import uk.colessoft.android.hilllist.ui.activity.HillDetailFragmentActivity
import uk.colessoft.android.hilllist.ui.activity.HillListFragmentActivity
import uk.colessoft.android.hilllist.ui.activity.ListHillsMapFragmentActivity

@Module
abstract class AndroidInjectorsModule {

    @ContributesAndroidInjector(modules = [HillListFragmentActivityIntentModule::class])
    abstract fun contributeHillListActivity(): HillListFragmentActivity

    @ContributesAndroidInjector(modules = [HillDetailFragmentActivityIntentModule::class])
    abstract fun contributeHillDetailActivity(): HillDetailFragmentActivity

    @ContributesAndroidInjector(modules = [ListHillsMapFragmentActivityIntentModule::class])
    abstract fun contributeListHillsMapActivity(): ListHillsMapFragmentActivity
}