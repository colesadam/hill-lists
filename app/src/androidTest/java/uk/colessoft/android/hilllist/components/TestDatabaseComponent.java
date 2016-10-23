package uk.colessoft.android.hilllist.components;


import javax.inject.Singleton;

import dagger.Component;
import uk.colessoft.android.hilllist.activities.BaggingExportActivity;
import uk.colessoft.android.hilllist.activities.BusinessSearchMapActivity;
import uk.colessoft.android.hilllist.activities.CheckHillListActivity;
import uk.colessoft.android.hilllist.activities.DetailGMapActivity;
import uk.colessoft.android.hilllist.activities.HillImagesActivity;
import uk.colessoft.android.hilllist.activities.Main;
import uk.colessoft.android.hilllist.activities.SplashScreenActivity;
import uk.colessoft.android.hilllist.database.DbHelper;
import uk.colessoft.android.hilllist.fragments.DisplayHillListFragment;
import uk.colessoft.android.hilllist.fragments.HillDetailFragment;
import uk.colessoft.android.hilllist.fragments.ListHillsMapFragment;
import uk.colessoft.android.hilllist.fragments.NearbyHillsFragment;
import uk.colessoft.android.hilllist.fragments.NearbyHillsMapFragment;
import uk.colessoft.android.hilllist.modules.AppModule;
import uk.colessoft.android.hilllist.modules.TestDatabaseModule;

@Singleton
@Component(modules = {AppModule.class,
        TestDatabaseModule.class})
public interface TestDatabaseComponent extends DatabaseComponent {

    DbHelper dbHelper();
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

}
