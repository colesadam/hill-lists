package uk.colessoft.android.hilllist.component


import javax.inject.Singleton

import dagger.Component
import uk.colessoft.android.hilllist.activity.BaggingExportActivity
import uk.colessoft.android.hilllist.activity.BusinessSearchMapActivity
import uk.colessoft.android.hilllist.activity.CheckHillListActivity
import uk.colessoft.android.hilllist.activity.DetailGMapActivity
import uk.colessoft.android.hilllist.activity.DisplayHillListFragmentActivity
import uk.colessoft.android.hilllist.activity.HillImagesActivity
import uk.colessoft.android.hilllist.activity.Main
import uk.colessoft.android.hilllist.activity.SplashScreenActivity
import uk.colessoft.android.hilllist.fragment.DisplayHillListFragment
import uk.colessoft.android.hilllist.fragment.HillDetailFragment
import uk.colessoft.android.hilllist.fragment.ListHillsMapFragment
import uk.colessoft.android.hilllist.fragment.NearbyHillsFragment
import uk.colessoft.android.hilllist.fragment.NearbyHillsMapFragment
import uk.colessoft.android.hilllist.module.AppModule
import uk.colessoft.android.hilllist.module.DatabaseModule
import uk.colessoft.android.hilllist.module.HelperModule
import uk.colessoft.android.hilllist.viewmodel.HillDetailViewModel

@Singleton
@Component(modules = [AppModule::class, DatabaseModule::class, HelperModule::class])
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
    val viewModel: HillDetailViewModel
}
