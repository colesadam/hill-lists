package uk.colessoft.android.hilllist.component;

import javax.inject.Singleton;

import dagger.Component;
import uk.colessoft.android.hilllist.database.HillsTables;
import uk.colessoft.android.hilllist.module.HillsTablesModule;

@Singleton
@Component(modules = {HillsTablesModule.class})
public interface HillsTablesComponent {

    HillsTables provideHillsTables();
}
