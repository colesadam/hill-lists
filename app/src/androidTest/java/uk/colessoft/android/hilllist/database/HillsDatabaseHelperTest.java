package uk.colessoft.android.hilllist.database;

import android.database.Cursor;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import junit.framework.Assert;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import uk.colessoft.android.hilllist.model.Hill;

import static android.content.ContentValues.TAG;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static uk.colessoft.android.hilllist.database.BaggingTable.KEY_NOTES;

@RunWith(AndroidJUnit4.class)
public class HillsDatabaseHelperTest {

    private static HillsDatabaseHelper helper;

    @BeforeClass
    public static void setUp() throws Exception {
        getTargetContext().deleteDatabase(HillsDatabaseHelper.DATABASE_NAME);
        helper = HillsDatabaseHelper.getInstance(getTargetContext());
    }

    @Test
    public void markHillClimbed() throws Exception {

        helper.markHillClimbed(1, new Date(), "some notes");
        assertEquals("some notes", helper.getHill(1).getNotes());
    }

    @Test
    public void markHillNotClimbed() throws Exception {
        helper.markHillClimbed(1, new Date(), "some notes");
        helper.markHillNotClimbed(1);
        assertNull(helper.getHill(1).getNotes());
    }

    @Test
    public void getAllHillsCursor() throws Exception {
        Cursor c = helper.getAllHillsCursor();
        assertTrue(c.moveToFirst());
        c.close();
    }

    @Test
    public void getBaggedHillList() throws Exception {
        helper.markHillClimbed(1, new Date(), "some notes");
        Cursor c = helper.getBaggedHillList();
        assertTrue(c.moveToFirst());
        assertEquals(c.getInt(c.getColumnIndex("_id")), 1);
        assertFalse(c.moveToNext());
        c.close();
    }

    @Test
    public void importBagging() throws Exception {
        String fileName = "bagging_file.csv";
        File testDirectory = getTargetContext().getDir("tmp1", 0);
        File storageFile = new File(testDirectory.getAbsolutePath()
                , fileName);
        if (storageFile.exists()) {
            storageFile.delete();
        }
        String notes = "some 'notes88***\",";

        helper.markHillClimbed(1, new Date(), notes);
        Cursor c = helper.getBaggedHillList();

        writeBaggingFile(testDirectory.getAbsolutePath(), c, fileName);

        helper.markHillNotClimbed(1);

        helper.importBagging(storageFile.getAbsolutePath());
        c = helper.getBaggedHillList();
        assertTrue(c.moveToFirst());
        Log.d(TAG, "importBagging: "+ c.getString(c.getColumnIndex(KEY_NOTES)));
        assertEquals(c.getString(c.getColumnIndex(KEY_NOTES)),notes);
    }

    @Test
    public void getHill() throws Exception {
        Hill hill = helper.getHill(1);
        assertThat(hill.getHillname(), is("Ben Chonzie"));
    }

    @Test
    public void getMunros() throws Exception {
        Cursor cursor = helper.getHillGroup("M", "cast(_Section as float) between 1 and 28.9", "", "cast(Metres as float) desc", 0);
        cursor.moveToFirst();
        assertTrue(cursor.getString(cursor.getColumnIndex("Name")).equals("Ben Nevis"));
        cursor.close();
    }

    @Test
    public void getT100() throws Exception {
        Cursor cursor = helper.getT100("","",0);
        cursor.moveToFirst();
        assertTrue(cursor.getCount()==100);
        cursor.close();
    }

    @After
    public void tearDown() throws Exception {
        helper.close();
    }

    private void writeBaggingFile(String folderPath, Cursor baggedCursor, String filename) throws IOException {

        File gpxfile = new File(folderPath, filename);

        FileWriter writer = new FileWriter(gpxfile);

        if (baggedCursor.moveToFirst()) {

            do {
                writer.append("'");
                writer.append(baggedCursor.getString(0));
                writer.append("'");
                writer.append(',');
                writer.append("'");
                writer.append(baggedCursor.getString(2));
                writer.append("'");
                writer.append(',');
                writer.append("'");
                writer.append(baggedCursor.getString(3));
                writer.append("'");
                writer.append('\n');

            } while (baggedCursor.moveToNext());
        }

        writer.flush();
        writer.close();

    }
}
