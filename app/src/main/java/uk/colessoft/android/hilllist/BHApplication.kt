package uk.colessoft.android.hilllist


import android.app.Application

import uk.colessoft.android.hilllist.components.DaggerDatabaseComponent
import uk.colessoft.android.hilllist.components.DatabaseComponent
import uk.colessoft.android.hilllist.modules.AppModule
import uk.colessoft.android.hilllist.modules.DatabaseModule

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
