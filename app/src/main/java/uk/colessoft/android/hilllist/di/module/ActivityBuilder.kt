package uk.colessoft.android.hilllist.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import uk.colessoft.android.hilllist.ui.activity.*
import uk.colessoft.android.hilllist.ui.fragment.*

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector
    internal abstract fun bindMain(): Main

    @ContributesAndroidInjector
    internal abstract fun bindSplashScreenActivity(): SplashScreenActivity

    @ContributesAndroidInjector
    internal abstract fun bindDisplayHillListFragment(): HillListFragment

    @ContributesAndroidInjector
    internal abstract fun bindHillDetailFragment(): HillDetailFragment

    @ContributesAndroidInjector
    internal abstract fun bindBaggingExportActivity(): BaggingExportActivity

    @ContributesAndroidInjector
    internal abstract fun bindBusinessSearchMapActivity(): BusinessSearchMapActivity

    @ContributesAndroidInjector
    internal abstract fun bindDetailGMapActivity(): DetailGMapActivity

    @ContributesAndroidInjector
    internal abstract fun bindHillImagesActivity(): HillImagesActivity

    @ContributesAndroidInjector
    internal abstract fun bindListHillsMapFragment(): ListHillsMapFragment

    @ContributesAndroidInjector
    internal abstract fun bindNearbyHillsFragment(): NearbyHillsFragment

    @ContributesAndroidInjector
    internal abstract fun bindNearbyHillsMapFragment(): NearbyHillsMapFragment

    @ContributesAndroidInjector
    internal abstract fun bindNearbyHillsFragmentActivity(): NearbyHillsFragmentActivity

    @ContributesAndroidInjector
    internal abstract fun bindNearbyHillsMapFragmentActivity(): NearbyHillsMapFragmentActivity

}
