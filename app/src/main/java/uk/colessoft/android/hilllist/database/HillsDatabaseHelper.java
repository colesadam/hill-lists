package uk.colessoft.android.hilllist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.colessoft.android.hilllist.model.Hill;

import static android.content.ContentValues.TAG;
import static uk.colessoft.android.hilllist.database.BaggingTable.BAGGING_TABLE;
import static uk.colessoft.android.hilllist.database.BaggingTable.KEY_DATECLIMBED;
import static uk.colessoft.android.hilllist.database.BaggingTable.KEY_NOTES;
import static uk.colessoft.android.hilllist.database.HillsTables.HILLS_TABLE;
import static uk.colessoft.android.hilllist.database.HillsTables.KEY_AREA;
import static uk.colessoft.android.hilllist.database.HillsTables.KEY_CLASSIFICATION;
import static uk.colessoft.android.hilllist.database.HillsTables.KEY_CLIMBED;
import static uk.colessoft.android.hilllist.database.HillsTables.KEY_COLGRIDREF;
import static uk.colessoft.android.hilllist.database.HillsTables.KEY_COLHEIGHT;
import static uk.colessoft.android.hilllist.database.HillsTables.KEY_COMMENTS;
import static uk.colessoft.android.hilllist.database.HillsTables.KEY_DROP;
import static uk.colessoft.android.hilllist.database.HillsTables.KEY_FEATURE;
import static uk.colessoft.android.hilllist.database.HillsTables.KEY_GRIDREF;
import static uk.colessoft.android.hilllist.database.HillsTables.KEY_GRIDREF10;
import static uk.colessoft.android.hilllist.database.HillsTables.KEY_HEIGHTF;
import static uk.colessoft.android.hilllist.database.HillsTables.KEY_HEIGHTM;
import static uk.colessoft.android.hilllist.database.HillsTables.KEY_HILLBAGGING;
import static uk.colessoft.android.hilllist.database.HillsTables.KEY_HILLNAME;
import static uk.colessoft.android.hilllist.database.HillsTables.KEY_ID;
import static uk.colessoft.android.hilllist.database.HillsTables.KEY_LATITUDE;
import static uk.colessoft.android.hilllist.database.HillsTables.KEY_LONGITUDE;
import static uk.colessoft.android.hilllist.database.HillsTables.KEY_MAP;
import static uk.colessoft.android.hilllist.database.HillsTables.KEY_MAP25;
import static uk.colessoft.android.hilllist.database.HillsTables.KEY_OBSERVATIONS;
import static uk.colessoft.android.hilllist.database.HillsTables.KEY_REVISION;
import static uk.colessoft.android.hilllist.database.HillsTables.KEY_SECTION;
import static uk.colessoft.android.hilllist.database.HillsTables.KEY_SECTIONNAME;
import static uk.colessoft.android.hilllist.database.HillsTables.KEY_STREETMAP;
import static uk.colessoft.android.hilllist.database.HillsTables.KEY_SURVEY;
import static uk.colessoft.android.hilllist.database.HillsTables.KEY_TITLE;
import static uk.colessoft.android.hilllist.database.HillsTables.KEY_XCOORD;
import static uk.colessoft.android.hilllist.database.HillsTables.KEY_XSECTION;
import static uk.colessoft.android.hilllist.database.HillsTables.KEY_YCOORD;

public class HillsDatabaseHelper extends SQLiteOpenHelper implements DbHelper {

    private final String typesLink = HillsTables.TYPES_LINK_TABLE;
    private final String typesLinkKeyId = typesLink + "." + HillsTables.KEY_TYPES_ID;
    private final String hills = HILLS_TABLE;
    private final String hillsKeyId = hills + "." + KEY_ID;


    private final String baggingTable = BAGGING_TABLE;
    private final String baggingKeyId = baggingTable + "." + KEY_ID;
    private Context context;
    public static final String DATABASE_NAME = "hill-list.db";
    private static final int DATABASE_VERSION = 1;
    private String hillTypes = HillsTables.HILLTYPES_TABLE;
    private String typesLinkKeyHillId = typesLink + "." + HillsTables.KEY_HILL_ID;
    private String hillTypesKeyId = hillTypes + "." + KEY_ID;
    private String hillTypesTitle = hillTypes + "." + KEY_TITLE;

    private Handler handler;
    private static HillsDatabaseHelper sInstance;

    public HillsDatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    private HillsDatabaseHelper(Context context, String name, CursorFactory factory,
                                int version) {
        super(context, name, factory, version);
        this.context = context;

    }

    public static synchronized HillsDatabaseHelper getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new HillsDatabaseHelper(context.getApplicationContext(), DATABASE_NAME
                    , (sqLiteDatabase, sqLiteCursorDriver, s, sqLiteQuery) -> {

                Log.d("SQL", sqLiteQuery.toString());

                return new SQLiteCursor(sqLiteDatabase, sqLiteCursorDriver, s, sqLiteQuery);
            }, DATABASE_VERSION);
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        HillsTables.onCreate(db, context, handler);
        BaggingTable.onCreate(db, context);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        HillsTables.onUpgrade(db, oldVersion, newVersion, context, handler);
    }

    @Override
    public void markHillClimbed(int hillNumber, Date dateClimbed, String notes) {
        SQLiteDatabase db = getWritableDatabase();
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
            db.insert(BAGGING_TABLE, null, climbedValues);
        } else {
            db.update(BAGGING_TABLE, climbedValues, "_id='"
                    + String.valueOf(hillNumber) + "'", null);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        existing.close();

    }

    @Override
    public void markHillNotClimbed(int hillNumber) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(BAGGING_TABLE, "_id='" + hillNumber + "'", null);
    }

    @Override
    public Cursor getAllHillsCursor() {
        Log.d(TAG, "getAllHillsCursor: ######## getting all hills");
        SQLiteDatabase db = getReadableDatabase();
        return db.query(HILLS_TABLE + " LEFT OUTER JOIN " + BAGGING_TABLE
                        + " ON (" + HILLS_TABLE + "._id" + "=" + BAGGING_TABLE
                        + "._id)", new String[]{HILLS_TABLE + "." + KEY_ID + " as hill_id", KEY_LATITUDE, KEY_LONGITUDE, KEY_HEIGHTM, KEY_HEIGHTF, KEY_HILLNAME,
        KEY_NOTES, KEY_DATECLIMBED},
                null, null, null, null, null);
    }

    @Override
    public Cursor getHillsForNearby() {
        Log.d(TAG, "getHillsForNearby: ######## getting all hills");
        SQLiteDatabase db = getReadableDatabase();
        return db.query(HILLS_TABLE, new String[]{HILLS_TABLE + "." + KEY_ID + " as hill_id", KEY_LATITUDE, KEY_LONGITUDE, KEY_HEIGHTM, KEY_HEIGHTF, KEY_HILLNAME},
                null, null, null, null, null);

    }

    @Override
    public Cursor getBaggedHillList() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(HILLS_TABLE + " INNER JOIN " + BAGGING_TABLE + " ON ("
                        + HILLS_TABLE + "._id" + "=" + BAGGING_TABLE + "._id)",
                new String[]{HILLS_TABLE + "." + KEY_ID, KEY_HILLNAME,
                        KEY_DATECLIMBED, KEY_NOTES}, null, null, null, null,
                null);
    }

    @Override
    public Cursor getHillGroup(String groupId, String countryClause, String moreFilters, String orderBy, int filter) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        String where = "";
        String includeTypes = " join "
                + typesLink + " on " + hillsKeyId + "=" + typesLinkKeyHillId + " join "
                + hillTypes + " on " + typesLinkKeyId + "=" + hillTypesKeyId;

        if (filter == 1)
            where = KEY_DATECLIMBED + " NOT NULL";
        else if (filter == 2)
            where = KEY_DATECLIMBED + " IS NULL";

        if (groupId != null) {
            String[] groups = groupId.split(",");
            String groupSelector = "";
            for (String group : groups) {
                groupSelector += hillTypesTitle + " = '" + group + "' OR ";
            }
            groupSelector = groupSelector.trim().substring(0, groupSelector.length() - 3);
            where = addToWhere("(" + groupSelector + ")", where);
        } else {
            includeTypes = "";
        }

        queryBuilder.setTables(hills + includeTypes + " left join "
                + baggingTable + " on " + hillsKeyId + "=" + baggingKeyId);

        //exclude ROI for now
        if (countryClause == null) {
            countryClause = "cast(_Section as float) <45";
        }

        where = addToWhere(countryClause, where);
        where = addToWhere(moreFilters, where);

        SQLiteDatabase db = getReadableDatabase();

        return queryBuilder.query(db, new String[]{HILLS_TABLE + "." + KEY_ID + " as hill_id", HILLS_TABLE + "." + KEY_ID, KEY_LATITUDE, KEY_LONGITUDE, KEY_HEIGHTM, KEY_HEIGHTF, KEY_HILLNAME,
                        KEY_NOTES, KEY_DATECLIMBED}, where,
                new String[]{}, null, null, orderBy);
    }

    @NonNull
    private String addToWhere(String filter, String where) {
        if (!"".equals(where) && filter != null && !"".equals(filter))
            where = where + " AND ";
        where = where + nonNull(filter);
        return where;
    }

    private String nonNull(String filter) {
        return filter == null ? "" : filter;
    }

    @Override
    public Hill getHill(long _rowIndex){

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(hills + " left join "
                + baggingTable + " on " + hillsKeyId + "=" + baggingKeyId);

        queryBuilder.appendWhere(hillsKeyId + "=" + _rowIndex);

        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = queryBuilder.query(db, null, null,
                null, null, null, null);

        if (cursor.moveToFirst()) {
            Hill hill = getHill(cursor);
            cursor.close();
            return hill;
        } else {
            cursor.close();
            return null;
        }

    }

    @Override
    public void importBagging(String filePath) {

        SQLiteDatabase db = getWritableDatabase();

        FileInputStream is = null;
        try {
            File baggingFile = new File(filePath);
            db.delete(BAGGING_TABLE, null, null);

            is = new FileInputStream(baggingFile);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is));

            String line;
            ContentValues values;
            while ((line = reader.readLine()) != null) {

                String[] lineSplit = line.split("',");

                values = new ContentValues();
                values.put("_id", lineSplit[0].substring(1, lineSplit[0].length()));
                values.put("dateClimbed", lineSplit[1].substring(1, lineSplit[1].length()));
                values.put("notes", lineSplit[2].substring(1, lineSplit[2].length() - 1));

                db.insert(BAGGING_TABLE, null, values);
            }
        } catch (IOException e) {
            Log.e(this.toString(), "error: " + e.toString());
        } catch (SQLiteException se) {
            se.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.e(this.toString(), "error: " + e.toString());
            }
        }
    }

    private Hill getHill(Cursor cursor) throws SQLException {

        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd");
        int _id = cursor.getInt(0);
        String _section = cursor.getString(cursor
                .getColumnIndex(KEY_XSECTION));
        String hillname = cursor.getString(cursor
                .getColumnIndex(KEY_HILLNAME));
        String section = cursor.getString(cursor
                .getColumnIndex(KEY_SECTION));
        String region = cursor.getString(cursor
                .getColumnIndex(KEY_SECTIONNAME));
        String area = cursor.getString(cursor
                .getColumnIndex(KEY_AREA));
        float heightm = cursor.getFloat(cursor
                .getColumnIndex(KEY_HEIGHTM));
        float heightf = cursor.getFloat(cursor
                .getColumnIndex(KEY_HEIGHTF));
        String map = cursor.getString(cursor
                .getColumnIndex(KEY_MAP));
        String map25 = cursor.getString(cursor
                .getColumnIndex(KEY_MAP25));
        String gridref = cursor.getString(cursor
                .getColumnIndex(KEY_GRIDREF));
        String colgridref = cursor.getString(cursor
                .getColumnIndex(KEY_COLGRIDREF));
        float colheight = cursor.getFloat(cursor
                .getColumnIndex(KEY_COLHEIGHT));
        float drop = cursor.getFloat(cursor
                .getColumnIndex(KEY_DROP));
        String gridref10 = cursor.getString(cursor
                .getColumnIndex(KEY_GRIDREF10));
        String feature = cursor.getString(cursor
                .getColumnIndex(KEY_FEATURE));
        String observations = cursor.getString(cursor
                .getColumnIndex(KEY_OBSERVATIONS));
        String survey = cursor.getString(cursor
                .getColumnIndex(KEY_SURVEY));
        String climbed = cursor.getString(cursor
                .getColumnIndex(KEY_CLIMBED));
        String classification = cursor.getString(cursor
                .getColumnIndex(KEY_CLASSIFICATION));
        long revision = cursor.getLong(cursor
                .getColumnIndex(KEY_REVISION));
        String comments = cursor.getString(cursor
                .getColumnIndex(KEY_COMMENTS));
        int xcoord = cursor.getInt(cursor
                .getColumnIndex(KEY_XCOORD));
        int ycoord = cursor.getInt(cursor
                .getColumnIndex(KEY_YCOORD));
        double latitude = cursor.getDouble(cursor
                .getColumnIndex(KEY_LATITUDE));
        double longitude = cursor.getDouble(cursor
                .getColumnIndex(KEY_LONGITUDE));
        String streetmap = cursor.getString(cursor
                .getColumnIndex(KEY_STREETMAP));
        String getamap = cursor.getString(cursor
                .getColumnIndex(HillsTables.KEY_GEOGRAPH));
        String hillBagging = cursor.getString(cursor
                .getColumnIndex(KEY_HILLBAGGING));
        Date dateClimbed = null;

        try {
            dateClimbed = iso8601Format.parse(cursor.getString(cursor
                    .getColumnIndex(KEY_DATECLIMBED)));
        } catch (java.text.ParseException e) {
            Log.e(TAG, "getHill: parse exception", e);
        } catch (NullPointerException npe) {
            Log.d(TAG, "getHill: no date climbed");
        }

        String notes = cursor.getString(cursor
                .getColumnIndex(KEY_NOTES));

        Hill result = new Hill(_id, _section, hillname, section, region, area,
                heightm, heightf, map, map25, gridref, colgridref, colheight,
                drop, gridref10, feature, observations, survey, climbed,
                classification, new Date(revision), comments, xcoord, ycoord,
                latitude, longitude, streetmap, getamap, hillBagging,
                dateClimbed, notes);
        System.out.println(result.getClassification());
        return result;

    }

    public void touch(Handler handler) {
        this.handler = handler;
        getReadableDatabase();
    }

}
