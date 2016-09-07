package uk.colessoft.android.hilllist.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.core.Is.is;

import uk.colessoft.android.hilllist.model.Hill;

import static android.support.test.InstrumentationRegistry.getTargetContext;

@RunWith(AndroidJUnit4.class)
public class HillsDatabaseHelperTest {

    private static HillsDatabaseHelper helper;

    @BeforeClass
    public static void setUp() throws Exception {
        getTargetContext().deleteDatabase(HillsDatabaseHelper.DATABASE_NAME);
        helper = HillsDatabaseHelper.getInstance(getTargetContext());
    }

    @Test
    public void getHill(){
        Hill hill=helper.getHill(1);
        assertThat(hill.getHillname(),is("Ben Chonzie"));
    }

    @Test
    public void getMunros(){
        Cursor cursor = helper.getHillGroup("M","cast(_Section as float) between 1 and 28.9","","cast(Metres as float) desc",0);
        cursor.moveToFirst();
        assertTrue(cursor.getString(cursor.getColumnIndex("Name")).equals("Ben Nevis"));
        cursor.close();
    }

    @After
    public void tearDown() throws Exception {
        helper.close();
    }
}
