package uk.colessoft.android.hilllist.components;


import javax.inject.Singleton;

import dagger.Component;
import uk.colessoft.android.hilllist.activities.SplashScreenActivity;
import uk.colessoft.android.hilllist.modules.AppModule;
import uk.colessoft.android.hilllist.modules.DatabaseModule;

@Singleton
@Component(modules = {AppModule.class,
        DatabaseModule.class})
public interface DatabaseComponent {

    void inject(SplashScreenActivity activity);
}
