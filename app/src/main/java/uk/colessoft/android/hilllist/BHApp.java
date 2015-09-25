package uk.colessoft.android.hilllist;


import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import uk.colessoft.android.hilllist.database.HillsTables;


public class BHApp extends Application{

    private BHAppComponent mAppComponent;
    private static Context context;

    @Override
    public void onCreate() {

        super.onCreate();
        BHApp.context=getApplicationContext();

    }

    public BHApp(){
        super();
        mAppComponent = DaggerBHApp_BHAppComponent.create();
    }

    public static Context getAppContext() {
        return BHApp.context;
    }

    public BHAppComponent component() {
        return mAppComponent;
    }


    @Singleton
    @Component(modules = DatabaseModule.class)
    public interface BHAppComponent {

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
    public void setTestComponent(BHAppComponent appComponent) {
        mAppComponent = appComponent;
    }
}
