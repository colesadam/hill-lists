package uk.colessoft.android.hilllist.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import uk.colessoft.android.hilllist.ui.activity.HillDetailFragmentActivity;
import uk.colessoft.android.hilllist.ui.activity.Main;
import uk.colessoft.android.hilllist.ui.activity.SplashScreenActivity;
import uk.colessoft.android.hilllist.ui.fragment.DisplayHillListFragment;
import uk.colessoft.android.hilllist.ui.fragment.HillDetailFragment;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector()
    abstract Main bindMain();

    @ContributesAndroidInjector()
    abstract SplashScreenActivity bindSplashScreenActivity();

    @ContributesAndroidInjector()
    abstract DisplayHillListFragment bindDisplayHillListFragment();

    @ContributesAndroidInjector()
    abstract HillDetailFragmentActivity bindHillDetailFragmentActivity();

    @ContributesAndroidInjector()
    abstract HillDetailFragment bindHillDetailFragment();


}
