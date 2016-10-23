package uk.colessoft.android.hilllist;


import android.app.Application;

import uk.colessoft.android.hilllist.components.DaggerDatabaseComponent;
import uk.colessoft.android.hilllist.components.DatabaseComponent;
import uk.colessoft.android.hilllist.modules.AppModule;
import uk.colessoft.android.hilllist.modules.DatabaseModule;

public class BHApplication extends Application {

    DatabaseComponent dbComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        dbComponent = DaggerDatabaseComponent.builder().appModule(new AppModule(this)).databaseModule(new DatabaseModule()).build();
    }

    public DatabaseComponent getDbComponent() {
        return dbComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(DatabaseComponent databaseComponent) {
        dbComponent = databaseComponent;
    }
}
