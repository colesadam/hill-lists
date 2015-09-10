package uk.colessoft.android.hilllist.database;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import uk.colessoft.android.hilllist.BritishHillsApplication;
import uk.colessoft.android.hilllist.BuildConfig;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import uk.colessoft.android.hilllist.BritishHillsApplication.BritishHillsApplicationComponent;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk=21,manifest = "src/main/AndroidManifest.xml")
public class DatabaseTest {

    static HillsDatabaseHelper helper;
    static SQLiteDatabase db;

    @Before
    public  void setUp() throws Exception {
        BritishHillsApplication.BritishHillsApplicationComponent appComponent =DaggerDatabaseTest_TestAppComponent.create();
        ((BritishHillsApplication) RuntimeEnvironment.application).setTestComponent(appComponent);
        ShadowLog.stream = System.out;
        helper = new HillsDatabaseHelper(RuntimeEnvironment.application);

        db = helper.getReadableDatabase();    }

    @Component(modules = DatabaseModule.class)
    interface TestAppComponent extends BritishHillsApplicationComponent {}

    @Module
    static class DatabaseModule {

        @Provides
        String provideCsvName() {
            return "test_data.csv";
        }
    }

    @Test
    public void databaseIsCreated() throws Exception {

        long start=System.nanoTime();


        Log.d(DatabaseTest.class.getName(),"Took " + (System.nanoTime() - start)/1000000000 + " seconds");
        System.out.println("#########Robo test ran");
        assertNotNull(db);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(TableNames.HILLS_TABLE);

        String[] projection = { ColumnKeys.KEY_HILLNAME,
                ColumnKeys.KEY_HEIGHTM, ColumnKeys.KEY_HEIGHTF,
                TableNames.HILLS_TABLE + "." + ColumnKeys.KEY_ID,
                ColumnKeys.KEY_LATITUDE, ColumnKeys.KEY_LONGITUDE };

        Cursor cursor = queryBuilder.query(db, projection, null,
                null, null, null, ColumnKeys.KEY_HEIGHTF + " desc");

        assertTrue(cursor.getCount() == 9);
        cursor.moveToFirst();
        String hill1=cursor.getString(0);
        cursor.moveToNext();
        String hill2=cursor.getString(0);

        assertNotSame(hill1, hill2);

        queryBuilder.setTables(TableNames.HILLTYPES_TABLE + "   join "
                + TableNames.TYPES_LINK_TABLE + " on  "
                + TableNames.HILLTYPES_TABLE + "._id="
                + TableNames.TYPES_LINK_TABLE + ".type_id  join "
                + TableNames.HILLS_TABLE + " on "
                + TableNames.TYPES_LINK_TABLE + ".hill_id="
                + TableNames.HILLS_TABLE + "._id");
        queryBuilder.setDistinct(true);

        String[] projection2 = { ColumnKeys.KEY_HILLNAME,
                ColumnKeys.KEY_HEIGHTM, ColumnKeys.KEY_HEIGHTF,
                TableNames.HILLS_TABLE + "." + ColumnKeys.KEY_ID,
                ColumnKeys.KEY_LATITUDE, ColumnKeys.KEY_LONGITUDE};

        cursor = queryBuilder.query(db,projection2,TableNames.HILLTYPES_TABLE+"._id=?",new String[]{"32"},null,null,null);
        cursor.moveToFirst();
        Log.d(DatabaseTest.class.getName(), cursor.getString(0));
        cursor.moveToNext();
        Log.d(DatabaseTest.class.getName(), cursor.getString(0));
        assertTrue(cursor.getCount() == 9);

    }

}
