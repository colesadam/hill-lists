package uk.colessoft.android.hilllist.modules;


import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import uk.colessoft.android.hilllist.database.BritishHillsDatasource;
import uk.colessoft.android.hilllist.database.TestHelper;

@Module
public class TestDatabaseModule {

    @Provides
    @Singleton
    BritishHillsDatasource provideDbHelper(Application application) {
        return TestHelper.getInstance(application);

    }
}
