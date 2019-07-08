package uk.colessoft.android.hilllist.component;


import javax.inject.Singleton;

import dagger.Component;
import uk.colessoft.android.hilllist.activity.BaggingExportActivity;
import uk.colessoft.android.hilllist.activity.BusinessSearchMapActivity;
import uk.colessoft.android.hilllist.activity.CheckHillListActivity;
import uk.colessoft.android.hilllist.activity.DetailGMapActivity;
import uk.colessoft.android.hilllist.activity.DisplayHillListFragmentActivity;
import uk.colessoft.android.hilllist.activity.HillImagesActivity;
import uk.colessoft.android.hilllist.activity.Main;
import uk.colessoft.android.hilllist.activity.SplashScreenActivity;
import uk.colessoft.android.hilllist.fragment.DisplayHillListFragment;
import uk.colessoft.android.hilllist.fragment.HillDetailFragment;
import uk.colessoft.android.hilllist.fragment.ListHillsMapFragment;
import uk.colessoft.android.hilllist.fragment.NearbyHillsFragment;
import uk.colessoft.android.hilllist.fragment.NearbyHillsMapFragment;
import uk.colessoft.android.hilllist.module.AppModule;
import uk.colessoft.android.hilllist.module.DatabaseModule;
import uk.colessoft.android.hilllist.module.HelperModule;

@Singleton
@Component(modules = {AppModule.class,
        DatabaseModule.class, HelperModule.class})
public interface DatabaseComponent {

    void inject(SplashScreenActivity activity);
    void inject(BaggingExportActivity activity);
    void inject(BusinessSearchMapActivity activity);
    void inject(CheckHillListActivity activity);
    void inject(DetailGMapActivity activity);
    void inject(Main main);
    void inject(HillImagesActivity hillImagesActivity);
    void inject(DisplayHillListFragment displayHillListFragment);
    void inject(HillDetailFragment hillDetailFragment);
    void inject(ListHillsMapFragment listHillsMapFragment);
    void inject(NearbyHillsFragment nearbyHillsFragment);
    void inject(NearbyHillsMapFragment nearbyHillsMapFragment);
    //void inject(DisplayHillListFragmentActivity displayHillListFragmentActivity);
}
