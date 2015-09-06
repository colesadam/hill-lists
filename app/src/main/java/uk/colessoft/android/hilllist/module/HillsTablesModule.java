package uk.colessoft.android.hilllist.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import uk.colessoft.android.hilllist.database.HillsTables;

@Module
public class HillsTablesModule {

    @Provides @Singleton
    HillsTables provideHillsTables(){
        return new HillsTables("DoBIH_v13_1.csv");
    }

}
