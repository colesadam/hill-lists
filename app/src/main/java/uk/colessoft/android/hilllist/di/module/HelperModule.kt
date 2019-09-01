package uk.colessoft.android.hilllist.di.module

import android.content.Context
import com.google.android.gms.location.LocationServices

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import uk.colessoft.android.hilllist.database.BritishHillsDatasource
import uk.colessoft.android.hilllist.database.HillsLocalDatasource
import uk.colessoft.android.hilllist.utility.LocationRepository

@Module
class HelperModule {

    @Provides
    @Singleton
    fun bindDatasource(datasource: HillsLocalDatasource): BritishHillsDatasource {
        return datasource
    }

    @Provides
    @Singleton
    fun bindLocationRepository(appContext: Context): LocationRepository {
        return LocationRepository(LocationServices.getFusedLocationProviderClient(appContext))
    }
}
