package uk.colessoft.android.hilllist.modules;


import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import uk.colessoft.android.hilllist.database.DbHelper;
import uk.colessoft.android.hilllist.database.HillsDatabaseHelper;
import uk.colessoft.android.hilllist.database.TestHelper;

@Module
public class TestDatabaseModule {

    @Provides
    @Singleton
    DbHelper provideDbHelper(Application application) {
        return TestHelper.getInstance(application);

    }
}
