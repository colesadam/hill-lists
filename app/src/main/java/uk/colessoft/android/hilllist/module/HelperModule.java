package uk.colessoft.android.hilllist.module;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import uk.colessoft.android.hilllist.database.BritishHillsDatasource;
import uk.colessoft.android.hilllist.database.HillsLocalDatasource;

@Module
public class HelperModule {

    @Provides
    @Singleton
    public  BritishHillsDatasource bindDatasource(HillsLocalDatasource datasource){
        return datasource;
    }
}
