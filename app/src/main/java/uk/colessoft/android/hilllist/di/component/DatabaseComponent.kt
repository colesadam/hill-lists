package uk.colessoft.android.hilllist.di.component


import javax.inject.Singleton

import dagger.Subcomponent
import uk.colessoft.android.hilllist.ui.activity.BaggingExportActivity
import uk.colessoft.android.hilllist.ui.activity.BusinessSearchMapActivity
import uk.colessoft.android.hilllist.ui.activity.CheckHillListActivity
import uk.colessoft.android.hilllist.ui.activity.DetailGMapActivity
import uk.colessoft.android.hilllist.ui.activity.HillImagesActivity
import uk.colessoft.android.hilllist.ui.activity.Main
import uk.colessoft.android.hilllist.ui.activity.SplashScreenActivity
import uk.colessoft.android.hilllist.ui.fragment.DisplayHillListFragment
import uk.colessoft.android.hilllist.ui.fragment.HillDetailFragment
import uk.colessoft.android.hilllist.ui.fragment.ListHillsMapFragment
import uk.colessoft.android.hilllist.ui.fragment.NearbyHillsFragment
import uk.colessoft.android.hilllist.ui.fragment.NearbyHillsMapFragment
import uk.colessoft.android.hilllist.di.module.DatabaseModule
import uk.colessoft.android.hilllist.di.module.HelperModule

@Singleton
@Subcomponent(modules = [DatabaseModule::class, HelperModule::class])
interface DatabaseComponent {

    fun inject(activity: SplashScreenActivity)
    fun inject(activity: BaggingExportActivity)
    fun inject(activity: BusinessSearchMapActivity)
    fun inject(activity: CheckHillListActivity)
    fun inject(activity: DetailGMapActivity)
    fun inject(main: Main)
    fun inject(hillImagesActivity: HillImagesActivity)
    fun inject(displayHillListFragment: DisplayHillListFragment)
    fun inject(hillDetailFragment: HillDetailFragment)
    fun inject(listHillsMapFragment: ListHillsMapFragment)
    fun inject(nearbyHillsFragment: NearbyHillsFragment)
    fun inject(nearbyHillsMapFragment: NearbyHillsMapFragment)
}
