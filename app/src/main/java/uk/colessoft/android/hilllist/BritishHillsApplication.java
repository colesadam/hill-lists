package uk.colessoft.android.hilllist;


import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import uk.colessoft.android.hilllist.database.HillsTables;


public class BritishHillsApplication extends Application{

    private BritishHillsApplicationComponent mAppComponent;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        BritishHillsApplication.context=getApplicationContext();
        mAppComponent = DaggerBritishHillsApplication_BritishHillsApplicationComponent.create();
    }

    public static Context getAppContext() {
        return BritishHillsApplication.context;
    }

    public BritishHillsApplicationComponent component() {
        return mAppComponent;
    }

    @Singleton
    @Component(modules = DatabaseModule.class)
    public interface BritishHillsApplicationComponent {

        void inject(HillsTables hillsTables);
    }

    @Module
    public static class DatabaseModule {

        @Provides
        String provideCsvName() {
            return "DoBIH_v13_1.csv";
        }
    }

    /**
     * Visible only for testing purposes.
     */
// @VisibleForTesting
    public void setTestComponent(BritishHillsApplicationComponent appComponent) {
        mAppComponent = appComponent;
    }
}
