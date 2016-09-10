package uk.colessoft.android.hilllist.database;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static android.content.ContentValues.TAG;

public class HillsTables {


    public static final String KEY_TITLE = "title";

    private static final String HILLS_CSV = "DoBIH_v15.3.csv";

    public static final String HILLS_TABLE = "hills";
    public static final String HILLTYPES_TABLE = "hilltypes";
    public static final String TYPES_LINK_TABLE = "typeslink";
    public static final String KEY_ID = "_id";
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
    public static final String KEY_HILLBAGGING = "Hill-bagging";
    public static final String KEY_XCOORD = "Xcoord";
    public static final String KEY_YCOORD = "Ycoord";
    public static final String KEY_LATITUDE = "Latitude";
    public static final String KEY_LONGITUDE = "Longitude";
    public static final String KEY_XSECTION = "_Section";


    public static final String KEY_HILL_ID = "hill_id";
    public static final String KEY_TYPES_ID = "type_id";


    private static final String HILLTYPES_CREATE = "CREATE TABLE "
            + HILLTYPES_TABLE + "(" + KEY_ID + " integer primary key," + KEY_TITLE + " unique)";

    private static final String TYPESLINK_CREATE = "CREATE TABLE "
            + TYPES_LINK_TABLE + "(" + KEY_ID
            + " integer primary key autoincrement," + KEY_HILL_ID
            + " references " + HILLS_TABLE + "(" + KEY_ID + ")," + KEY_TYPES_ID
            + " references " + HILLTYPES_TABLE + "(" + KEY_ID + "))";

    private static ProgressDialog progressDialog;

    public static void onCreate(SQLiteDatabase database, Context context) {


        createSimpleTables(database);

        long startTime = System.currentTimeMillis();

        int orientation = ((Activity) (context)).getRequestedOrientation();
        ((Activity) (context)).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        ((Activity) context).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                progressDialog = new ProgressDialog(context);
                progressDialog.setTitle("Updating Database");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setMax(20600);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }
        });

        populateHillsTable(database, context, startTime);
        progressDialog.cancel();
        populateHillTypes(database, startTime);

        ((Activity) (context)).setRequestedOrientation(orientation);

    }

    private static void populateHillTypes(SQLiteDatabase database, long startTime) {
        // Populate static hill types data
        database.beginTransaction();
        Cursor c = database.query(HILLS_TABLE, null, null, null, null, null, null, null);
        SQLiteStatement insertHillType = database.compileStatement("INSERT or IGNORE into " + HILLTYPES_TABLE + " VALUES(?,?)");
        SQLiteStatement insertHillTypeLink = database.compileStatement("INSERT into " + TYPES_LINK_TABLE + " (" + KEY_HILL_ID + "," + KEY_TYPES_ID + ") values (?,?)");

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
        Log.d(HillsTables.class.getName(),
                "#################Finished inserting hill types information after "
                        + (System.currentTimeMillis() - startTime) / 1000);
    }

    private static void populateHillsTable(SQLiteDatabase database, Context context, long startTime) {
        InputStream is;
        Log.d(TAG, "populateHillsTable: starting");
        try {

            is = context.getAssets().open(HILLS_CSV);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is));

            // read main hill data into hills table
            database.beginTransaction();
            String line = null;

            String headerRow = reader.readLine();
            String hillsTableStarter = "CREATE TABLE '" + HILLS_TABLE
                    + "' ('" + KEY_ID + "' integer primary key,";
            String[] headerArray = headerRow.split(",");
            StringBuilder createHillsTableBuilder = new StringBuilder();
            for (String header : headerArray) {
                if (!"Number".equals(header)) {
                    createHillsTableBuilder.append("'").append(header).append("',");
                }
            }
            String c = createHillsTableBuilder.toString();
            String createHillsTable = hillsTableStarter + c.substring(0, c.length() - 1) + ")";

            database.execSQL(createHillsTable);

            StringBuffer insertHillsBuffer;
            // read each line of text file
            while ((line = reader.readLine()) != null) {
                ((Activity) context).runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        progressDialog.incrementProgressBy(1);
                    }
                });

                insertHillsBuffer = new StringBuffer();
                insertHillsBuffer.append("INSERT INTO " + HILLS_TABLE
                        + " VALUES (");
                String[] lineArray = line
                        .split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                // StringTokenizer st = new StringTokenizer(line, ",");
                // read lines up to marilyn column - the first type column
                for (String entry : lineArray) {
                    insertHillsBuffer.append("'").append(entry.replace("'", "''")).append("',");

                }
                insertHillsBuffer.deleteCharAt(insertHillsBuffer.length() - 1);

                insertHillsBuffer.append(")");
                String statement = insertHillsBuffer.toString();
                database.execSQL(statement);

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

    private static void createSimpleTables(SQLiteDatabase database) {
        database.execSQL("PRAGMA foreign_keys=ON;");
        database.execSQL(HILLTYPES_CREATE);
        database.execSQL(TYPESLINK_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion, Context context) {
        Log.w(HillsTables.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old hills data");
        database.execSQL("DROP TABLE IF EXISTS " + HILLS_TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + HILLTYPES_TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + TYPES_LINK_TABLE);
        onCreate(database, context);
    }


}
