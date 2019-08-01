package uk.colessoft.android.hilllist.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import uk.colessoft.android.hilllist.BHApplication;
import uk.colessoft.android.hilllist.component.DatabaseComponent;

@Module
public class AppModule {

    @Provides
    @Singleton
    Context provideContext(BHApplication application) {
        return application;
    }

}