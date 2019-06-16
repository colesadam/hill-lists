package uk.colessoft.android.hilllist.database;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteStatement;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static android.content.ContentValues.TAG;

public class HillsTables {

    private static final String HILLS_CSV = "DoBIH_v15.5.csv";

    public static final String KEY_TITLE = "title";
    public static final String HILLS_TABLE = "hills";
    public static final String HILLTYPES_TABLE = "hilltypes";
    public static final String TYPES_LINK_TABLE = "typeslink";
    public static final String KEY_ID = "tl_id";
    public static final String KEY_HILLNAME = "Name";
    public static final String KEY_SECTION = "Section";
    public static final String KEY_SECTIONNAME = "Section name";
    public static final String KEY_AREA = "Area";
    public static final String KEY_CLASSIFICATION = "Classification";
    public static final String KEY_MAP = "Map 1:50k";
    public static final String KEY_MAP25 = "Map 1:25k";
    public static final String KEY_HEIGHTM = "Metres";
    public static final String KEY_HEIGHTF = "Feet";
    public static final String KEY_GRIDREF = "Grid ref";
    public static final String KEY_GRIDREF10 = "Grid ref 10";
    public static final String KEY_DROP = "Drop";
    public static final String KEY_COLGRIDREF = "Col grid ref";
    public static final String KEY_COLHEIGHT = "Col height";
    public static final String KEY_FEATURE = "Feature";
    public static final String KEY_OBSERVATIONS = "Observations";
    public static final String KEY_SURVEY = "Survey";
    public static final String KEY_CLIMBED = "Climbed";
    public static final String KEY_REVISION = "Revision";
    public static final String KEY_COMMENTS = "Comments";
    public static final String KEY_STREETMAP = "Streetmap/OSiViewer";
    public static final String KEY_GEOGRAPH = "Geograph/MountainViews";
    public static final String KEY_HILLBAGGING = "HillDetail-bagging";
    public static final String KEY_XCOORD = "Xcoord";
    public static final String KEY_YCOORD = "Ycoord";
    public static final String KEY_LATITUDE = "Latitude";
    public static final String KEY_LONGITUDE = "Longitude";
    public static final String KEY_XSECTION = "_Section";
    public static final String KEY_HILL_ID = "hill_id";
    public static final String KEY_TYPES_ID = "type_id";


    public static void onCreate(SupportSQLiteDatabase database, Context context){
        long startTime = System.currentTimeMillis();
        populateHillsTable(database, context, startTime);
        populateHillTypes(database, startTime);
    }

    static void populateHillTypes(SupportSQLiteDatabase database, long startTime) {

        Cursor c = database.query("select * from " + HILLS_TABLE);
        SupportSQLiteStatement insertHillType = database.compileStatement("INSERT or IGNORE into " + HILLTYPES_TABLE + " VALUES(?,?)");
        SupportSQLiteStatement insertHillTypeLink = database.compileStatement("INSERT into " + TYPES_LINK_TABLE + " (" + KEY_HILL_ID + "," + KEY_TYPES_ID + ") values (?,?)");

        // Populate static hill types data
        database.beginTransaction();
        if (c.moveToFirst()) {
            do {
                String[] classifications = c.getString(c.getColumnIndex(KEY_CLASSIFICATION)).replace("\"", "").split(",");
                for (String classification : classifications) {
                    insertHillType.bindString(2, classification);
                    insertHillType.bindLong(1, c.getColumnIndex(classification));
                    insertHillType.executeInsert();

                    insertHillTypeLink.bindLong(1, c.getInt(0));
                    insertHillTypeLink.bindLong(2, c.getColumnIndex(classification));
                    insertHillTypeLink.executeInsert();
                }
            } while (c.moveToNext());
        }
        database.setTransactionSuccessful();
        database.endTransaction();
        c.close();
        Log.d(TAG, "populateHillTypes: Finished inserting hill types information after "
                        + (System.currentTimeMillis() - startTime) / 1000);
    }

    private static void populateHillsTable(SupportSQLiteDatabase database, Context context, long startTime){
        StringBuffer insertHillsBuffer;

        Log.d(TAG, "populateHillsTable: starting");
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(HILLS_CSV)));

            reader.readLine();

            // read main hill data into hills table
            database.beginTransaction();
            String line;
            while ((line = reader.readLine()) != null) {

                insertHillsBuffer = new StringBuffer();
                insertHillsBuffer.append("INSERT INTO " + HILLS_TABLE
                        + " VALUES (");
                String[] lineArray = line
                        .split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

                for (String entry : lineArray) {
                    insertHillsBuffer.append("'").append(entry.replace("'", "''")).append("',");
                }
                insertHillsBuffer.deleteCharAt(insertHillsBuffer.length() - 1);

                insertHillsBuffer.append(")");
                database.execSQL(insertHillsBuffer.toString());
            }

            database.setTransactionSuccessful();
            database.endTransaction();
            Log.d(TAG,
                    "#################Finished inserting base hill information after "
                            + (System.currentTimeMillis() - startTime) / 1000);
        } catch (IOException e) {
            Log.e(TAG,
                    "Failed to populate hills database table", e);
        }
    }

}
