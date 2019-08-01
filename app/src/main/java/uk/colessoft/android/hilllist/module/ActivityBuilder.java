package uk.colessoft.android.hilllist.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import uk.colessoft.android.hilllist.activity.DisplayHillListFragmentActivity;
import uk.colessoft.android.hilllist.activity.Main;
import uk.colessoft.android.hilllist.activity.SplashScreenActivity;
import uk.colessoft.android.hilllist.fragment.DisplayHillListFragment;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector()
    abstract Main bindMain();

    @ContributesAndroidInjector()
    abstract SplashScreenActivity bindSplashScreenActivity();

    @ContributesAndroidInjector()
    abstract DisplayHillListFragment bindDisplayHillListFragment();


}
