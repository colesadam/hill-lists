package uk.colessoft.android.hilllist.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import uk.colessoft.android.hilllist.ui.activity.HillDetailFragmentActivity;
import uk.colessoft.android.hilllist.ui.activity.Main;
import uk.colessoft.android.hilllist.ui.activity.SplashScreenActivity;
import uk.colessoft.android.hilllist.ui.fragment.HillListFragment;
import uk.colessoft.android.hilllist.ui.fragment.HillDetailFragment;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector()
    abstract Main bindMain();

    @ContributesAndroidInjector()
    abstract SplashScreenActivity bindSplashScreenActivity();

    @ContributesAndroidInjector()
    abstract HillListFragment bindDisplayHillListFragment();

    @ContributesAndroidInjector()
    abstract HillDetailFragment bindHillDetailFragment();


}
