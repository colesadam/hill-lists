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

import uk.colessoft.android.hilllist.BuildConfig;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk=21,manifest = "src/main/AndroidManifest.xml")
public class DatabaseTest {

    static HillsDatabaseHelper helper;
    static SQLiteDatabase db;

    @Before
    public  void setUp() throws Exception {
        ShadowLog.stream = System.out;
        helper = new HillsDatabaseHelper(RuntimeEnvironment.application);

        db = helper.getReadableDatabase();    }

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

        assertTrue(cursor.getCount() > 0);
        cursor.moveToFirst();
        String hill1=cursor.getString(0);
        cursor.moveToNext();
        String hill2=cursor.getString(0);

        assertNotSame(hill1, hill2);

    }

}
