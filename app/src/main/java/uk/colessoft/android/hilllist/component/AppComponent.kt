package uk.colessoft.android.hilllist.component

import android.app.Application
import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import uk.colessoft.android.hilllist.BHApplication
import uk.colessoft.android.hilllist.module.*
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        AndroidSupportInjectionModule::class,
        AppModule::class,
        HelperModule::class,
        DatabaseModule::class,
        ActivityBuilder::class,
        AndroidInjectorsModule::class

        ))
interface AppComponent : AndroidInjector<BHApplication> {

    override fun inject(application: BHApplication)

    @Component.Builder
    interface Builder {

        fun build(): AppComponent

        @BindsInstance
        fun applicationBind(application: BHApplication): Builder

    }
}