package uk.colessoft.android.hilllist.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import uk.colessoft.android.hilllist.ui.activity.HillDetailActivity
import uk.colessoft.android.hilllist.ui.activity.HillListActivity
import uk.colessoft.android.hilllist.ui.activity.HillListMapActivity
import uk.colessoft.android.hilllist.ui.activity.NearbyHillsActivity

@Module
abstract class AndroidInjectorsModule {

    @ContributesAndroidInjector(modules = [HillListActivityIntentModule::class])
    abstract fun contributeHillListActivity(): HillListActivity

    @ContributesAndroidInjector(modules = [HillDetailActivityIntentModule::class])
    abstract fun contributeHillDetailActivity(): HillDetailActivity

    @ContributesAndroidInjector(modules = [HillListMapActivityIntentModule::class])
    abstract fun contributeHillListMapActivity(): HillListMapActivity

}