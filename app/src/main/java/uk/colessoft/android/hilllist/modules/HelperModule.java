package uk.colessoft.android.hilllist.modules;

import dagger.Binds;
import dagger.Module;
import uk.colessoft.android.hilllist.database.BritishHillsDatasource;
import uk.colessoft.android.hilllist.database.HillsLocalDatasource;

@Module
public abstract class HelperModule {

    @Binds
    public abstract BritishHillsDatasource bindDatasource(HillsLocalDatasource datasource);
}
