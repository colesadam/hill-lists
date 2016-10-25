package uk.colessoft.android.hilllist.components;


import javax.inject.Singleton;

import dagger.Component;
import uk.colessoft.android.hilllist.modules.AppModule;
import uk.colessoft.android.hilllist.modules.DatabaseModule;
import uk.colessoft.android.hilllist.presenter.HillDetailPresenter;

@Singleton
@Component(modules = {AppModule.class,
        DatabaseModule.class})
public interface HillDetailComponent {

    HillDetailPresenter presenter();
}
