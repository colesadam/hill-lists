package uk.colessoft.android.hilllist;


import android.app.Application;
import android.content.Context;

import uk.colessoft.android.hilllist.components.DaggerDatabaseComponent;
import uk.colessoft.android.hilllist.components.DaggerHillDetailComponent;
import uk.colessoft.android.hilllist.components.DaggerHillListComponent;
import uk.colessoft.android.hilllist.components.DatabaseComponent;
import uk.colessoft.android.hilllist.components.HillDetailComponent;
import uk.colessoft.android.hilllist.components.HillListComponent;
import uk.colessoft.android.hilllist.modules.AppModule;
import uk.colessoft.android.hilllist.modules.DatabaseModule;

public class BHApplication extends Application {

    DatabaseComponent dbComponent;
    HillDetailComponent hillDetailComponent;
    HillListComponent hillListComponent;

    private static Context context;

    public static Context getAppContext() {
        return BHApplication.context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BHApplication.context = getApplicationContext();
        dbComponent = DaggerDatabaseComponent.builder().appModule(new AppModule(this)).databaseModule(new DatabaseModule()).build();
        hillDetailComponent = DaggerHillDetailComponent.builder().appModule(new AppModule(this)).databaseModule(new DatabaseModule()).build();
        hillListComponent = DaggerHillListComponent.builder().appModule(new AppModule(this)).databaseModule(new DatabaseModule()).build();

    }

    public DatabaseComponent getDbComponent() {
        return dbComponent;
    }
    public HillDetailComponent getHillDetailComponent() {return hillDetailComponent;}
    public HillListComponent getHillListComponent() {return hillListComponent;}

    // Needed to replace the component with a test specific one
    public void setComponent(DatabaseComponent databaseComponent) {
        dbComponent = databaseComponent;
    }
}
