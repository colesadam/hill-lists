package uk.colessoft.android.hilllist.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import uk.colessoft.android.hilllist.ui.activity.HillDetailFragmentActivity
import uk.colessoft.android.hilllist.ui.activity.HillListFragmentActivity

@Module
abstract class AndroidInjectorsModule {

    @ContributesAndroidInjector(modules = [HillListFragmentActivityIntentModule::class])
    abstract fun contributeHillListActivity(): HillListFragmentActivity

    @ContributesAndroidInjector(modules = [HillDetailFragmentActivityIntentModule::class])
    abstract fun contributeHillDetailActivity(): HillDetailFragmentActivity
}