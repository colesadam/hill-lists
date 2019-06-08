package uk.colessoft.android.hilllist


import android.app.Application

import uk.colessoft.android.hilllist.component.DaggerDatabaseComponent
import uk.colessoft.android.hilllist.component.DatabaseComponent
import uk.colessoft.android.hilllist.module.AppModule
import uk.colessoft.android.hilllist.module.DatabaseModule

class BHApplication : Application() {

    lateinit var dbComponent: DatabaseComponent
        internal set

    override fun onCreate() {
        super.onCreate()

        dbComponent = DaggerDatabaseComponent.builder().appModule(AppModule(this)).databaseModule(DatabaseModule()).build()
    }

    // Needed to replace the component with a test specific one
    fun setComponent(databaseComponent: DatabaseComponent) {
        dbComponent = databaseComponent
    }
}
