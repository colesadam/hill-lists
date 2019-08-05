package uk.colessoft.android.hilllist.di.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import uk.colessoft.android.hilllist.BHApplication;

@Module
public class AppModule {

    @Provides
    @Singleton
    Context provideContext(BHApplication application) {
        return application;
    }

}