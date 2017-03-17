package uk.colessoft.android.hilllist.components;


import javax.inject.Singleton;

import dagger.Component;
import uk.colessoft.android.hilllist.modules.AppModule;
import uk.colessoft.android.hilllist.modules.DatabaseModule;
import uk.colessoft.android.hilllist.presenter.HillListPresenter;
import uk.colessoft.android.hilllist.presenter.NearbyHillsPresenterFactory;

@Singleton
@Component(modules = {AppModule.class,
        DatabaseModule.class})
public interface NearbyHillsComponent {

    NearbyHillsPresenterFactory presenterFactory();
}
