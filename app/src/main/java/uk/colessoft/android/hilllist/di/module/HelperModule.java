package uk.colessoft.android.hilllist.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import uk.colessoft.android.hilllist.database.BritishHillsDatasource;
import uk.colessoft.android.hilllist.database.HillsLocalDatasource;
import uk.colessoft.android.hilllist.utility.LocationRepository;

@Module
public class HelperModule {

    @Provides
    @Singleton
    public  BritishHillsDatasource bindDatasource(HillsLocalDatasource datasource){
        return datasource;
    }

    @Provides
    @Singleton
    public LocationRepository bindLocationRepository(LocationRepository repository){
        return repository;
    }
}
