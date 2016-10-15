package uk.colessoft.android.hilllist.modules;


import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import uk.colessoft.android.hilllist.database.DbHelper;
import uk.colessoft.android.hilllist.database.HillsDatabaseHelper;

@Module
public class DatabaseModule {

    @Provides
    @Singleton
    HillsDatabaseHelper provideDbHelper(Application application) {
        return HillsDatabaseHelper.getInstance(application);

    }
}
