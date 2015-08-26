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

    @Before
    public void setUp() throws Exception {
        ShadowLog.stream = System.out;
        //you other setup here
    }

    @Test
    public void databaseIsCreated() throws Exception {

        long start=System.nanoTime();
        HillsDatabaseHelper helper = new HillsDatabaseHelper(RuntimeEnvironment.application);

        SQLiteDatabase db = helper.getReadableDatabase();

        Log.d(DatabaseTest.class.getName(),"Took " + (System.nanoTime() - start)/1000000000 + " seconds");
        assertNotNull(db);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(HillsTables.HILLS_TABLE);

        String[] projection = { HillsTables.KEY_HILLNAME,
                HillsTables.KEY_HEIGHTM, HillsTables.KEY_HEIGHTF,
                HillsTables.HILLS_TABLE + "." + HillsTables.KEY_ID,
                HillsTables.KEY_LATITUDE, HillsTables.KEY_LONGITUDE };

        Cursor cursor = queryBuilder.query(db, projection, null,
                null, null, null, HillsTables.KEY_HEIGHTF + " desc");

        assertTrue(cursor.getCount() > 0);
        cursor.moveToFirst();
        String hill1=cursor.getString(0);
        cursor.moveToNext();
        String hill2=cursor.getString(0);

        assertNotSame(hill1, hill2);

    }


}
