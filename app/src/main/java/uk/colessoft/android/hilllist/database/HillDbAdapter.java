package uk.colessoft.android.hilllist.database;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.colessoft.android.hilllist.model.Hill;


public class HillDbAdapter {
    private static final String DATABASE_NAME = "hill-list.db";
    private static final String HILLS_TABLE = "hills";
    private static final String BAGGING_TABLE = "Bagging";
    private static final int DATABASE_VERSION = 4;

    public static final String KEY_ID = "_id";
    public static final String KEY_XSECTION = "_Section";
    public static final String KEY_HILLNAME = "Hillname";
    public static final String KEY_SECTION = "Section";
    public static final String KEY_REGION = "Region";
    public static final String KEY_AREA = "Area";
    public static final String KEY_HEIGHTM = "Metres";
    public static final String KEY_HEIGHTF = "Feet";
    public static final String KEY_MAP = "Map";
    public static final String KEY_MAP25 = "Map25";
    public static final String KEY_GRIDREF = "Gridref";
    public static final String KEY_COLGRIDREF = "Colgridref";
    public static final String KEY_COLHEIGHT = "Colheight";
    public static final String KEY_DROP = "sDrop";
    public static final String KEY_GRIDREF10 = "Gridref10";
    public static final String KEY_FEATURE = "Feature";
    public static final String KEY_OBSERVATIONS = "Observations";
    public static final String KEY_SURVEY = "Survey";
    public static final String KEY_CLIMBED = "Climbed";
    public static final String KEY_CLASSIFICATION = "Classification";
    public static final String KEY_REVISION = "Revision";
    public static final String KEY_COMMENTS = "Comments";
    public static final String KEY_XCOORD = "xcoord";
    public static final String KEY_YCOORD = "ycoord";
    public static final String KEY_LATITUDE = "Latitude";
    public static final String KEY_LONGITUDE = "Longitude";
    public static final String KEY_STREETMAP = "Streetmap";
    public static final String KEY_GETAMAP = "Getamap";
    public static final String KEY_HILLBAGGING = "HillBagging";// "\"Hill-bagging\"";
    public static final String KEY_DATECLIMBED = "dateClimbed";
    public static final String KEY_NOTES = "notes";

    public static final int NAME_COLUMN = 1;

    private SQLiteDatabase db;
    private final Context context;
    private HillDataBaseHelper dbHelper;


    public HillDbAdapter(Context _context) {
        this.context = _context;
        dbHelper = new HillDataBaseHelper(context, DATABASE_NAME, null,
                DATABASE_VERSION);
    }

    public void close() {
        db.close();
    }

    public boolean isOpen() {
        if (db != null) {
            return db.isOpen();
        } else return false;
    }

    public void open() throws SQLiteException {
        dbHelper.openDataBase();
        db = dbHelper.getReadableDatabase();


    }

    public void markHillClimbed(int hillNumber, Date dateClimbed, String notes) {
        // set the format to sql date time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        ContentValues climbedValues = new ContentValues();
        climbedValues.put("dateClimbed", dateFormat.format(dateClimbed));
        climbedValues.put("_id", String.valueOf(hillNumber));
        climbedValues.put("notes", notes);
        Cursor existing = db.query(true, BAGGING_TABLE,
                new String[]{KEY_ID},
                KEY_ID + "='" + String.valueOf(hillNumber) + "'", null, null,
                null, null, null);
        db.beginTransaction();
        if (existing.getCount() == 0) {
            long r = db.insert(BAGGING_TABLE, null, climbedValues);
        } else {
            long rowId = db.update(BAGGING_TABLE, climbedValues, "_id='"
                    + String.valueOf(hillNumber) + "'", null);
        }
        db.setTransactionSuccessful();
        db.endTransaction();

    }

    public void markHillNotClimbed(int hillNumber) {
        db.delete("Bagging", "_id='" + hillNumber + "'", null);

    }

    public Cursor getAllHillsCursor() {
        return db.query(HILLS_TABLE + " LEFT OUTER JOIN " + BAGGING_TABLE
                        + " ON (" + HILLS_TABLE + "._id" + "=" + BAGGING_TABLE
                        + "._id)", new String[]{HILLS_TABLE + "." + KEY_ID,
                        KEY_XSECTION, KEY_HILLNAME, KEY_SECTION, KEY_REGION, KEY_AREA,
                        KEY_HEIGHTM, KEY_HEIGHTF, KEY_MAP, KEY_MAP25, KEY_GRIDREF,
                        KEY_COLGRIDREF, KEY_COLHEIGHT, KEY_DROP, KEY_GRIDREF10,
                        KEY_FEATURE, KEY_OBSERVATIONS, KEY_SURVEY, KEY_CLIMBED,
                        KEY_CLASSIFICATION, KEY_REVISION, KEY_COMMENTS, KEY_XCOORD,
                        KEY_YCOORD, KEY_LATITUDE, KEY_LONGITUDE, KEY_STREETMAP,
                        KEY_GETAMAP, KEY_HILLBAGGING, KEY_DATECLIMBED, KEY_NOTES},
                null, null, null, null, null);
    }

    public Cursor getBaggedHillList() {
        return db.query(HILLS_TABLE + " INNER JOIN " + BAGGING_TABLE + " ON ("
                        + HILLS_TABLE + "._id" + "=" + BAGGING_TABLE + "._id)",
                new String[]{HILLS_TABLE + "." + KEY_ID, KEY_HILLNAME,
                        KEY_DATECLIMBED, KEY_NOTES}, null, null, null, null,
                null);
    }

    public void importBagging(final String filePath) {
        final String baggingCreation = "CREATE TABLE 'Bagging' ('_id','dateClimbed','notes');";
        final ProgressDialog pbarDialog;
        final Context myContext = null;
        pbarDialog = ProgressDialog.show(context, null,
                "Importing Bagging Table", true, false);
        pbarDialog.setCancelable(false);
        pbarDialog.setCanceledOnTouchOutside(false);
        Thread thread = new Thread() {
            @Override
            public void run() {
                FileInputStream is = null;
                try {
                    File baggingFile = new File(filePath);
                    db.delete("Bagging", null, null);

                    is = new FileInputStream(baggingFile);
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(is));

                    // db.beginTransaction();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // line=line.replace(",", "','");
                        // line = "'" + line + "'";
                        String[] lineSplit = line.split(",");
                        String newLine = "";

                        newLine = newLine + "\"" + lineSplit[0].substring(1, lineSplit[0].length() - 1) + "\"";
                        newLine = newLine + ",";
                        newLine = newLine + "\"" + lineSplit[1].substring(1, lineSplit[1].length() - 1) + "\"";
                        newLine = newLine + ",";
                        newLine = newLine + "\"" + lineSplit[2].substring(1, lineSplit[2].length() - 1) + "\"";


                        db.execSQL("INSERT INTO Bagging VALUES (" + newLine + ");");
                    }

                    // db.setTransactionSuccessful();
                    // db.endTransaction();

                } catch (IOException e) {

                    Log.e(this.toString(), "error: " + e.toString());

                } catch (SQLiteException se) {
                    se.printStackTrace();
                } finally {
                    pbarDialog.cancel();
                    try {
                        is.close();
                        // database.close();
                    } catch (IOException e) {
                        Log.e(this.toString(), "error: " + e.toString());
                    }
                }

            }

        };
        thread.start();

    }

    public Cursor getHillsbyPartialName(String where, String orderBy) {

        return db.query(HILLS_TABLE + " LEFT OUTER JOIN " + BAGGING_TABLE
                        + " ON (" + HILLS_TABLE + "._id" + "=" + BAGGING_TABLE
                        + "._id)", new String[]{HILLS_TABLE + "." + KEY_ID,
                        KEY_XSECTION, KEY_HILLNAME, KEY_SECTION, KEY_REGION, KEY_AREA,
                        KEY_HEIGHTM, KEY_HEIGHTF, KEY_MAP, KEY_MAP25, KEY_GRIDREF,
                        KEY_COLGRIDREF, KEY_COLHEIGHT, KEY_DROP, KEY_GRIDREF10,
                        KEY_FEATURE, KEY_OBSERVATIONS, KEY_SURVEY, KEY_CLIMBED,
                        KEY_CLASSIFICATION, KEY_REVISION, KEY_COMMENTS, KEY_XCOORD,
                        KEY_YCOORD, KEY_LATITUDE, KEY_LONGITUDE, KEY_STREETMAP,
                        KEY_GETAMAP, KEY_HILLBAGGING, KEY_DATECLIMBED, KEY_NOTES},
                where, null, null, null, orderBy);

    }

    public Cursor getHillGroup(String groupId, String countryClause,
                               String moreWhere, String orderBy, int filter) {
        /*
		 * if (countryClause != null) { countryClause = "AND " + countryClause +
		 * " "; } else countryClause = ""; if ((moreWhere !=
		 * null)&&(!"".equals(countryClause))) { moreWhere = "AND " + moreWhere;
		 * } else if(moreWhere==null){ moreWhere=""; } String where; if
		 * (groupId!=null) { where = groupId + "='1'" + countryClause +
		 * moreWhere; } else where=countryClause+moreWhere;
		 */
        String where;

        if (filter == 1)
            where = KEY_DATECLIMBED + " NOT NULL";
        else if (filter == 2)
            where = KEY_DATECLIMBED + " IS NULL";
        else
            where = "";
        if (groupId != null) {
            if (!"".equals(where))
                where = where + " AND ";
            where = where + "(" + groupId + "='1')";
        }
        if (countryClause != null && !"".equals(where)) {
            if (!"".equals(where))
                where = where + " AND ";
            if (countryClause != null) where = where + countryClause;
        } else if (countryClause != null) {
            if (!"".equals(where))
                where = where + " AND ";
            where = where + countryClause;
        }
        if (moreWhere != null && !"".equals(moreWhere) && !"".equals(where)) {
            where = where + " AND " + moreWhere;
        } else if (moreWhere != null && !"".equals(moreWhere)) {
            where = where + moreWhere;
        }


        return db.query(HILLS_TABLE + " LEFT OUTER JOIN " + BAGGING_TABLE
                        + " ON (" + HILLS_TABLE + "._id" + "=" + BAGGING_TABLE
                        + "._id)", new String[]{HILLS_TABLE + "." + KEY_ID,
                        KEY_XSECTION, KEY_HILLNAME, KEY_SECTION, KEY_REGION, KEY_AREA,
                        KEY_HEIGHTM, KEY_HEIGHTF, KEY_MAP, KEY_MAP25, KEY_GRIDREF,
                        KEY_COLGRIDREF, KEY_COLHEIGHT, KEY_DROP, KEY_GRIDREF10,
                        KEY_FEATURE, KEY_OBSERVATIONS, KEY_SURVEY, KEY_CLIMBED,
                        KEY_CLASSIFICATION, KEY_REVISION, KEY_COMMENTS, KEY_XCOORD,
                        KEY_YCOORD, KEY_LATITUDE, KEY_LONGITUDE, KEY_STREETMAP,
                        KEY_GETAMAP, KEY_HILLBAGGING, KEY_marilyn, KEY_subMarilyn,
                        KEY_deletedMarilyn, KEY_marilynTwinTop, KEY_deletedSubMarilyn,
                        KEY_munro, KEY_munroTop, KEY_deletedMunroTop, KEY_corbett, KEY_corbettTop,
                        KEY_corbettTopOfMunro, KEY_corbettTopOfCorbett,
                        KEY_deletedCorbett, KEY_deletedCorbettTop, KEY_graham, KEY_grahamTop,
                        KEY_grahamTopOfMunro, KEY_grahamTopOfCorbett,
                        KEY_grahamTopOfGraham, KEY_subGraham, KEY_doubleSubGraham, KEY_donald,
                        KEY_donaldTop, KEY_deletedDonaldTop, KEY_murdo, KEY_subMurdo, KEY_doubleSubMurdo,
                        KEY_hewitt, KEY_subHewitt, KEY_doubleSubHewitt, KEY_nuttall, KEY_deletedNuttall,
                        KEY_wainwright, KEY_wainwrightOutlyingFell, KEY_buxtonAndLewis,
                        KEY_dewey, KEY_birkett, KEY_hump,
                        KEY_deletedHump, KEY_humpTwinTop, KEY_bridge, KEY_trail100,
                        KEY_countyTop, KEY_deletedCountyTop, KEY_currentCountyTop,
                        KEY_londonBoroughTop, KEY_DATECLIMBED, KEY_NOTES}, where,
                null, null, null, orderBy);
    }


    public Cursor setCursorHill(long _rowIndex) throws SQLException {
        Cursor result = db.query(true, HILLS_TABLE, new String[]{KEY_ID,
                        KEY_HILLNAME}, KEY_ID + "=" + _rowIndex, null, null, null,
                null, null);
        if ((result.getCount() == 0) || !result.moveToFirst()) {
            throw new SQLException("No hills found for row: " + _rowIndex);
        }
        return result;
    }

    public Hill getHill(long _rowIndex) throws SQLException {
        Cursor cursor = db.query(true, HILLS_TABLE + " LEFT OUTER JOIN "
                + BAGGING_TABLE + " ON (" + HILLS_TABLE + "._id" + "="
                + BAGGING_TABLE + "._id)", new String[]{
                HILLS_TABLE + "." + KEY_ID, KEY_XSECTION, KEY_HILLNAME,
                KEY_SECTION, KEY_REGION, KEY_AREA, KEY_HEIGHTM, KEY_HEIGHTF,
                KEY_MAP, KEY_MAP25, KEY_GRIDREF, KEY_COLGRIDREF, KEY_COLHEIGHT,
                KEY_DROP, KEY_GRIDREF10, KEY_FEATURE, KEY_OBSERVATIONS,
                KEY_SURVEY, KEY_CLIMBED, KEY_CLASSIFICATION, KEY_REVISION,
                KEY_COMMENTS, KEY_XCOORD, KEY_YCOORD, KEY_LATITUDE,
                KEY_LONGITUDE, KEY_STREETMAP, KEY_GETAMAP, KEY_HILLBAGGING,
                KEY_DATECLIMBED, KEY_NOTES}, HILLS_TABLE + "." + KEY_ID + "='"
                + _rowIndex + "'", null, null, null, null, null);
        if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
            throw new SQLException("No hill found for row: " + _rowIndex);
        }
        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd");
        int _id = cursor.getInt(0);
        int test = cursor.getColumnIndex(KEY_XSECTION);
        String _section = cursor.getString(cursor.getColumnIndex(KEY_XSECTION));
        String hillname = cursor.getString(cursor.getColumnIndex(KEY_HILLNAME));
        String section = cursor.getString(cursor.getColumnIndex(KEY_SECTION));
        String region = cursor.getString(cursor.getColumnIndex(KEY_REGION));
        String area = cursor.getString(cursor.getColumnIndex(KEY_AREA));
        float heightm = cursor.getFloat(cursor.getColumnIndex(KEY_HEIGHTM));
        float heightf = cursor.getFloat(cursor.getColumnIndex(KEY_HEIGHTF));
        String map = cursor.getString(cursor.getColumnIndex(KEY_MAP));
        String map25 = cursor.getString(cursor.getColumnIndex(KEY_MAP25));
        String gridref = cursor.getString(cursor.getColumnIndex(KEY_GRIDREF));
        String colgridref = cursor.getString(cursor
                .getColumnIndex(KEY_COLGRIDREF));
        float colheight = cursor.getFloat(cursor.getColumnIndex(KEY_COLHEIGHT));
        float drop = cursor.getFloat(cursor.getColumnIndex(KEY_DROP));
        String gridref10 = cursor.getString(cursor
                .getColumnIndex(KEY_GRIDREF10));
        String feature = cursor.getString(cursor.getColumnIndex(KEY_FEATURE));
        String observations = cursor.getString(cursor
                .getColumnIndex(KEY_OBSERVATIONS));
        String survey = cursor.getString(cursor.getColumnIndex(KEY_SURVEY));
        String climbed = cursor.getString(cursor.getColumnIndex(KEY_CLIMBED));
        String classification = cursor.getString(cursor
                .getColumnIndex(KEY_CLASSIFICATION));
        long revision = cursor.getLong(cursor.getColumnIndex(KEY_REVISION));
        String comments = cursor.getString(cursor.getColumnIndex(KEY_COMMENTS));
        int xcoord = cursor.getInt(cursor.getColumnIndex(KEY_XCOORD));
        int ycoord = cursor.getInt(cursor.getColumnIndex(KEY_YCOORD));
        double latitude = cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE));
        double longitude = cursor.getDouble(cursor
                .getColumnIndex(KEY_LONGITUDE));
        String streetmap = cursor.getString(cursor
                .getColumnIndex(KEY_STREETMAP));
        String getamap = cursor.getString(cursor.getColumnIndex(KEY_GETAMAP));
        String hillBagging = cursor.getString(cursor
                .getColumnIndex(KEY_HILLBAGGING));
        Date dateClimbed = null;

        try {
            dateClimbed = iso8601Format.parse(cursor.getString(cursor
                    .getColumnIndex(KEY_DATECLIMBED)));
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            Log.e(this.toString(), e.toString());
        } catch (NullPointerException npe) {
            // ignore
        }

        String notes = cursor.getString(cursor.getColumnIndex(KEY_NOTES));

        Hill result = new Hill(_id, _section, hillname, section, region, area,
                heightm, heightf, map, map25, gridref, colgridref, colheight,
                drop, gridref10, feature, observations, survey, climbed,
                classification, new Date(revision), comments, xcoord, ycoord,
                latitude, longitude, streetmap, getamap, hillBagging,
                dateClimbed, notes);
        return result;
    }

    public boolean createDatabase() throws IOException {
        return dbHelper.createDataBase();
    }

    private static class HillDataBaseHelper {
        // The Android's default system path of your application database.
        private static String DB_PATH = Environment
                .getExternalStorageDirectory().getPath()
                + "/data/uk.colessoft.android.hilllist/databases/";
        private static boolean mExternalStorageAvailable = false;
        private static String hillCreation = "CREATE TABLE 'Hills' ('_id','_Section','Hillname','Section',"
                + "'Region','Area','Metres','Feet','Map','Map25','Gridref','Colgridref','Colheight','sDrop',"
                + "'Gridref10','Feature','Observations','Survey','Climbed','Classification','Countyname',"
                + "'Revision','Comments','xcoord','ycoord','Latitude','Longitude','Streetmap','Getamap',"
                + "'HillBagging','Ma','sMa','xMa','twinMa','xsMa','M','MT','xMT','C','CT','CTM','CTC','xC','xCT',"
                + "'G','GT','GTM','GTC','GTG','sG','dsG','D','DT','xDT','Mur','sMur','dsMur','Hew','sHew','dsHew','N','xN','W','WO',"
                + "'BL','Dewey','B','Hu','xHu','twinHu','Bg','T100','COH','COA','COU','CoL');";
        private static String baggingCreation = "CREATE TABLE 'Bagging' ('_id','dateClimbed','notes');";
        private static String DB_NAME = DATABASE_NAME;
        private static ProgressDialog pbarDialog;
        private static InputStream is;
        private static BufferedReader reader;
        private File extPath = Environment.getExternalStorageDirectory();

        private File dbFile = new File(DB_PATH + DB_NAME);
        private SQLiteDatabase database;

        private final Context myContext;
        private SQLiteDatabase.CursorFactory factory;

        /**
         * Constructor Takes and keeps a reference of the passed context in
         * order to access to the application assets and resources.
         *
         * @param context
         */
        public HillDataBaseHelper(Context context, String databaseName,
                                  SQLiteDatabase.CursorFactory factory, int databaseVersion) {

            this.myContext = context;
            this.factory = factory;
        }

        /**
         * Creates a empty database on the system and rewrites it with your own
         * database.
         */
        public boolean createDataBase() throws IOException {

            boolean dbExist = checkDataBase();

            if (dbExist) {
                // check bagging table exists
                Log.i(this.toString(), "Missing Bagging Table");
            } else if (mExternalStorageAvailable) {

                // By calling this method and empty database will be created
                // into the default system path
                // of your application so we are gonna be able to overwrite that
                // database with our database.
                this.getReadableDatabase();

                try {
                    dbExist = true;
                    copyDataBase();
                    this.close();

                } catch (IOException e) {
                    dbExist = false;
                    throw new Error("Error copying database", e);

                }
            } else {

                dbExist = false;
            }
            return dbExist;

        }

        /**
         * Check if the database already exist to avoid re-copying the file each
         * time you open the application.
         *
         * @return true if it exists, false if it doesn't
         */
        private boolean checkDataBase() {

            SQLiteDatabase checkDB = null;

            boolean mExternalStorageWriteable = false;
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                // We can read and write the media
                mExternalStorageAvailable = mExternalStorageWriteable = true;
            } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                // We can only read the media
                mExternalStorageAvailable = true;
                mExternalStorageWriteable = false;
            } else {
                // Something else is wrong. It may be one of many other states,
                // but all we need
                // to know is we can neither read nor write
                mExternalStorageAvailable = mExternalStorageWriteable = false;
            }

            if (mExternalStorageAvailable) {

                try {
                    String myPath = DB_PATH + DB_NAME;
                    checkDB = SQLiteDatabase.openDatabase(myPath, null,
                            SQLiteDatabase.OPEN_READONLY);
                    Log.i(this.toString(),
                            "Current DB Version:"
                                    + String.valueOf(checkDB.getVersion()));

                } catch (SQLiteException e) {

                    Log.i(this.toString(), "No current Database");

                }
                boolean baggingExists = true;
                if (checkDB != null) {
                    // check for existence of Bagging table
                    Cursor result = checkDB.query(true, "SQLITE_MASTER",
                            new String[]{"NAME"}, "name='" + BAGGING_TABLE
                                    + "'", null, null, null, null, null);
                    if ((result.getCount() == 0) || !result.moveToFirst()) {
                        baggingExists = false;
                    }
                }
                if (checkDB != null && checkDB.getVersion() == DATABASE_VERSION
                        && baggingExists) {
                    checkDB.close();
                } else if ((checkDB != null && checkDB.getVersion() < DATABASE_VERSION)
                        || !baggingExists) {
                    checkDB.close();
                    try {
                        upgradeDatabase();
                    } catch (IOException e) {
                        Log.e(this.toString(), "Upgrade Failed");
                    }
                }
            }

            return checkDB != null ? true : false;
        }

        private void upgradeDatabase() throws IOException {
            String myPath = DB_PATH + DB_NAME;
            SQLiteDatabase db = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READWRITE);
            db.execSQL("DROP TABLE HILLS");
            db.close();
            copyDataBase();

        }

        /**
         * Copies your database from your local assets-folder to the just
         * created empty database in the system folder, from where it can be
         * accessed and handled. This is done by transfering bytestream.
         */
        private void copyDataBase() throws IOException {

            File dbDir = new File(DB_PATH);
            dbDir.mkdirs();

            // Open the empty db as the output stream
            // OutputStream myOutput = new FileOutputStream(outFileName);

            pbarDialog = ProgressDialog.show(myContext, null,
                    "Initializing Database", true, false);
            pbarDialog.setCancelable(false);
            pbarDialog.setCanceledOnTouchOutside(false);
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        String myPath = DB_PATH + DB_NAME;
                        database = SQLiteDatabase.openDatabase(myPath, null,
                                SQLiteDatabase.CREATE_IF_NECESSARY);
                        // Log.i(this.toString(),"Current DB Version:"+String.valueOf(database.getVersion()));
                        database.setVersion(DATABASE_VERSION);


                        database.execSQL(hillCreation);

                        is = myContext.getAssets().open("hills1.csv");
                        reader = new BufferedReader(new InputStreamReader(is));

                        doTransaction(pbarDialog, reader);

                        is = myContext.getAssets().open("hills2.csv");
                        reader = new BufferedReader(new InputStreamReader(is));

                        doTransaction(pbarDialog, reader);
                        is = myContext.getAssets().open("hills3.csv");
                        reader = new BufferedReader(new InputStreamReader(is));

                        doTransaction(pbarDialog, reader);
                        is = myContext.getAssets().open("hills4.csv");
                        reader = new BufferedReader(new InputStreamReader(is));
                        doTransaction(pbarDialog, reader);
                        database.execSQL(baggingCreation);
                    } catch (IOException e) {

                        Log.e(this.toString(), "error: " + e.toString());
                    } catch (SQLiteException se) {
                        Log.e(this.toString(), "error: " + se.toString());
                    } finally {
                        pbarDialog.cancel();
                        try {
                            is.close();
                            database.close();
                        } catch (IOException e) {
                            Log.e(this.toString(), "error: " + e.toString());
                        }
                    }

                }

            };
            thread.start();

            // newDatabase.close();

        }

        private void doTransaction(ProgressDialog pbarDialog,
                                   BufferedReader reader) {
            String line = null;
            try {
                database.beginTransaction();
                while ((line = reader.readLine()) != null) {

                    line = line.replace("|", "','");
                    line = "'" + line + "'";

                    database.execSQL("INSERT INTO Hills VALUES (" + line
                            + ");");

                }
                database.setTransactionSuccessful();
            } catch (SQLException e) {
                Log.e(this.toString(), "error: " + e.toString());
            } catch (IOException e) {
                Log.e(this.toString(), "error: " + e.toString());
            } finally {
                database.endTransaction();
                pbarDialog.incrementProgressBy(1);
            }
        }

        public void openDataBase() throws SQLException {

            // Open the database
            String myPath = DB_PATH + DB_NAME;
            database = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READWRITE);

        }

        private boolean open() {
            boolean opened = false;
            if (dbFile.exists()) {
                try {
                    database = SQLiteDatabase.openDatabase(
                            dbFile.getAbsolutePath(), factory,
                            SQLiteDatabase.OPEN_READWRITE);
                    opened = true;

                } catch (SQLiteException e) {
                    // TODO Auto-generated catch block
                    opened = false;
                }
            }
            return opened;
        }

        public synchronized void close() {
            if (database != null) {
                database.close();
                database = null;
            }
        }

        // Add your public helper methods to access and get content from the
        // database.
        // You could return cursors by doing "return myDataBase.query(....)" so
        // it'd be easy
        // to you to create adapters for your views.

        public synchronized SQLiteDatabase getReadableDatabase() {
            return getRDatabase();
        }

        private SQLiteDatabase getRDatabase() {
            boolean opened = true;
            if (database == null) {
                opened = open();
            }
            if (opened == false) {
                if (database != null) {
                    database.close();
                }
                dbFile.delete();
                return null;
            }
            return database;
        }

    }

    public static final int marilyn_COLUMN = 31;
    public static final int subMarilyn_COLUMN = 32;
    public static final int deletedMarilyn_COLUMN = 33;
    public static final int marilynTwinTop_COLUMN = 34;
    public static final int deletedSubMarilyn_COLUMN = 35;
    public static final int munro_COLUMN = 36;
    public static final int munroTop_COLUMN = 37;
    public static final int deletedMunroTop_COLUMN = 38;
    public static final int corbett_COLUMN = 39;
    public static final int corbettTopOfMunro_COLUMN = 40;
    public static final int corbettTopOfCorbett_COLUMN = 41;
    public static final int deletedCorbett_COLUMN = 42;
    public static final int deletedCorbettTop_COLUMN = 43;
    public static final int graham_COLUMN = 44;
    public static final int grahamTopOfMunro_COLUMN = 45;
    public static final int grahamTopOfCorbett_COLUMN = 46;
    public static final int grahamTopOfGraham_COLUMN = 47;
    public static final int subGraham_COLUMN = 48;
    public static final int donald_COLUMN = 49;
    public static final int donaldTop_COLUMN = 50;
    public static final int deletedDonaldTop_COLUMN = 51;
    public static final int murdo_COLUMN = 52;
    public static final int subMurdo_COLUMN = 53;
    public static final int hewitt_COLUMN = 54;
    public static final int subHewitt_COLUMN = 55;
    public static final int nuttall_COLUMN = 56;
    public static final int deletedNuttall_COLUMN = 57;
    public static final int wainwright_COLUMN = 58;
    public static final int wainwrightOutlyingFell_COLUMN = 59;
    public static final int buxtonAndLewis_COLUMN = 60;
    public static final int dewey_COLUMN = 61;
    public static final int deletedDewey_COLUMN = 62;
    public static final int buxton_COLUMN = 63;
    public static final int hump_COLUMN = 64;
    public static final int deletedHump_COLUMN = 65;
    public static final int humpTwinTop_COLUMN = 66;
    public static final int bridge_COLUMN = 67;
    public static final int trail100_COLUMN = 68;
    public static final int countyTop_COLUMN = 69;
    public static final int deletedCountyTop_COLUMN = 70;
    public static final int currentCountyTop_COLUMN = 71;
    public static final int londonBoroughTop_COLUMN = 72;

    public static final String KEY_marilyn = "ma";
    public static final String KEY_subMarilyn = "sma";
    public static final String KEY_deletedMarilyn = "xma";
    public static final String KEY_marilynTwinTop = "twinma";
    public static final String KEY_deletedSubMarilyn = "xsma";
    public static final String KEY_munro = "m";
    public static final String KEY_munroTop = "mt";
    public static final String KEY_deletedMunroTop = "xmt";
    public static final String KEY_corbett = "c";
    public static final String KEY_corbettTop = "ct";
    public static final String KEY_corbettTopOfMunro = "ctm";
    public static final String KEY_corbettTopOfCorbett = "ctc";
    public static final String KEY_deletedCorbett = "xc";
    public static final String KEY_deletedCorbettTop = "xct";
    public static final String KEY_graham = "g";
    public static final String KEY_grahamTop = "gt";
    public static final String KEY_grahamTopOfMunro = "gtm";
    public static final String KEY_grahamTopOfCorbett = "gtc";
    public static final String KEY_grahamTopOfGraham = "gtg";
    public static final String KEY_subGraham = "sg";
    public static final String KEY_doubleSubGraham = "dsg";
    public static final String KEY_donald = "d";
    public static final String KEY_donaldTop = "dt";
    public static final String KEY_deletedDonaldTop = "xdt";
    public static final String KEY_murdo = "mur";
    public static final String KEY_subMurdo = "smur";
    public static final String KEY_doubleSubMurdo = "dsmur";
    public static final String KEY_hewitt = "hew";
    public static final String KEY_subHewitt = "shew";
    public static final String KEY_doubleSubHewitt = "dshew";
    public static final String KEY_nuttall = "n";
    public static final String KEY_deletedNuttall = "xn";
    public static final String KEY_wainwright = "w";
    public static final String KEY_wainwrightOutlyingFell = "wo";
    public static final String KEY_buxtonAndLewis = "bl";
    public static final String KEY_dewey = "dewey";
    public static final String KEY_birkett = "b";
    public static final String KEY_hump = "hu";
    public static final String KEY_deletedHump = "xhu";
    public static final String KEY_humpTwinTop = "twinhu";
    public static final String KEY_bridge = "bg";
    public static final String KEY_trail100 = "t100";
    public static final String KEY_countyTop = "coh";
    public static final String KEY_deletedCountyTop = "coa";
    public static final String KEY_currentCountyTop = "cou";
    public static final String KEY_londonBoroughTop = "col";


    public int getClimbedCount(String hilltype, String countryClause,
                               String moreWhere) {
        String where;

        where = KEY_DATECLIMBED + " NOT NULL";

        if (hilltype != null) {
            if (!"".equals(where))
                where = where + " AND ";
            where = where + "(" + hilltype + "='1')";
        }
        if (countryClause != null && !"".equals(where)) {
            if (!"".equals(where))
                where = where + " AND ";
            if (countryClause != null) where = where + countryClause;
        } else if (countryClause != null) {
            if (!"".equals(where))
                where = where + " AND ";
            where = where + countryClause;
        }
        if (moreWhere != null && !"".equals(moreWhere) && !"".equals(where)) {
            where = where + " AND " + moreWhere;
        } else if (moreWhere != null && !"".equals(moreWhere)) {
            where = where + moreWhere;
        }


        Cursor result = db.query(HILLS_TABLE + " LEFT OUTER JOIN " + BAGGING_TABLE
                        + " ON (" + HILLS_TABLE + "._id" + "=" + BAGGING_TABLE
                        + "._id)", new String[]{HILLS_TABLE + "." + KEY_ID}, where,
                null, null, null, null);

        return result.getCount();
    }

}