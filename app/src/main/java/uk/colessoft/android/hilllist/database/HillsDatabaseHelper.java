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

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;


import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import uk.colessoft.android.hilllist.fragments.NearbyHillsFragment;
import uk.colessoft.android.hilllist.model.Hill;
import uk.colessoft.android.hilllist.model.TinyHill;
import uk.colessoft.android.hilllist.utility.DistanceCalculator;

import static android.content.ContentValues.TAG;
import static rx.schedulers.Schedulers.io;
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
    protected Context context;
    public static final String DATABASE_NAME = "hill-list.db";
    private static final int DATABASE_VERSION = 1;
    private String hillTypes = HillsTables.HILLTYPES_TABLE;
    private String typesLinkKeyHillId = typesLink + "." + HillsTables.KEY_HILL_ID;
    private String hillTypesKeyId = hillTypes + "." + KEY_ID;
    private String hillTypesTitle = hillTypes + "." + KEY_TITLE;

    private Handler handler;
    protected static HillsDatabaseHelper sInstance;

    public HillsDatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    protected HillsDatabaseHelper(Context context, String name, CursorFactory factory,
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

    private static <T> Observable<T> makeObservable(final Callable<T> func) {
        return Observable.create(
                new Observable.OnSubscribe<T>() {
                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        try {
                            subscriber.onNext(func.call());
                        } catch (Exception ex) {
                            Log.e(TAG, "Error reading from the database", ex);
                        }
                    }
                });
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        HillsTables.onCreate(db, context, handler, 9999999);
        BaggingTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        HillsTables.onUpgrade(db, oldVersion, newVersion, context, handler, 99999999);
    }

    @Override
    public Observable<Long> markHillClimbed(int hillNumber, LocalDate dateClimbed, String notes) {

        return makeObservable(hillClimbed(hillNumber, dateClimbed, notes)).subscribeOn(Schedulers.computation());

    }

    private Callable<Long> hillClimbed(int hillNumber, LocalDate dateClimbed, String notes) {
        return () -> {
            SQLiteDatabase db = getWritableDatabase();

            ContentValues climbedValues = new ContentValues();
            climbedValues.put("dateClimbed", dateClimbed.toString("yyyy-MM-dd"));
            climbedValues.put("_id", String.valueOf(hillNumber));
            climbedValues.put("notes", notes);
            Cursor existing = db.query(true, BAGGING_TABLE,
                    new String[]{KEY_ID},
                    KEY_ID + "='" + String.valueOf(hillNumber) + "'", null, null,
                    null, null, null);

            db.beginTransaction();
            long result;
            if (existing.getCount() == 0) {
                result = db.insert(BAGGING_TABLE, null, climbedValues);
            } else {
                result = db.update(BAGGING_TABLE, climbedValues, "_id='"
                        + String.valueOf(hillNumber) + "'", null);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            existing.close();
            return result;
        };
    }

    @Override
    public Observable<Integer> markHillNotClimbed(int hillNumber) {
        return makeObservable(hillNotClimbed(hillNumber)).subscribeOn(Schedulers.computation());
    }

    private Callable<Integer> hillNotClimbed(int hillNumber) {
        return () -> {
            SQLiteDatabase db = getWritableDatabase();
            return db.delete(BAGGING_TABLE, "_id='" + hillNumber + "'", null);
        };

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
    public Observable<List<TinyHill>> getNearbyHills(double lat1, double lon1, double nearRadius){
        return makeObservable(getNearbyHillsCallable(lat1,lon1,nearRadius)).subscribeOn(Schedulers.computation());
    }

    private Callable<List<TinyHill>> getNearbyHillsCallable(double lat1, double lon1, double nearRadius) {

        return () -> {

            List<TinyHill> hills = new ArrayList<>();
            Cursor hillsCursor = getHillsForNearby();

            if (hillsCursor.moveToFirst()) {
                do {
                    Double lat = hillsCursor.getDouble(hillsCursor
                            .getColumnIndex(HillsTables.KEY_LATITUDE));
                    Double lng = hillsCursor.getDouble(hillsCursor
                            .getColumnIndex(HillsTables.KEY_LONGITUDE));

                    double distanceKm = DistanceCalculator
                            .calculationByDistance(lat1, lat, lon1,
                                    lng);
                    int row_id = hillsCursor.getInt(hillsCursor
                            .getColumnIndex(HillsTables.KEY_HILL_ID));
                    if (distanceKm < nearRadius) {
                        TinyHill hill = new TinyHill();
                        hill.setHillname(hillsCursor.getString(hillsCursor
                                .getColumnIndex(HillsTables.KEY_HILLNAME)));
                        hill.setHeightM(hillsCursor.getFloat(hillsCursor
                                .getColumnIndex(HillsTables.KEY_HEIGHTM)));
                        hill.setHeightF(hillsCursor.getFloat(hillsCursor
                                .getColumnIndex(HillsTables.KEY_HEIGHTF)));

                        hill._id = hillsCursor.getInt(hillsCursor.getColumnIndex(HillsTables.KEY_HILL_ID));
                        hill.setDistance(distanceKm);
                        hills.add(hill);
                    }

                } while (hillsCursor.moveToNext());

            }
            return hills;

        };
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

        if ("T100".equals(groupId)) return getT100(moreFilters, orderBy, filter);
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

    @Override
    public Cursor getT100(String moreFilters, String orderBy, int filter) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        String where = "T100='1'";

        if (filter == 1)
            where = KEY_DATECLIMBED + " NOT NULL";
        else if (filter == 2)
            where = KEY_DATECLIMBED + " IS NULL";


        queryBuilder.setTables(hills + " left join "
                + baggingTable + " on " + hillsKeyId + "=" + baggingKeyId);

        where = addToWhere(moreFilters, where);

        SQLiteDatabase db = getReadableDatabase();

        return queryBuilder.query(db, new String[]{HILLS_TABLE + "." + KEY_ID + " as hill_id"
                        , HILLS_TABLE + "." + KEY_ID, KEY_LATITUDE, KEY_LONGITUDE, KEY_HEIGHTM
                        , KEY_HEIGHTF, KEY_HILLNAME,
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
    public Observable<List<TinyHill>> getHills(String groupId, String countryClause, String moreWhere
            , String orderBy, int filter) {
        return makeObservable(getHillsCallable(groupId, countryClause, moreWhere, orderBy, filter))
                .subscribeOn(Schedulers.computation());
    }

    private Callable<List<TinyHill>> getHillsCallable(String groupId, String countryClause
            , String moreWhere, String orderBy, int filter) {

        return () -> {
            Cursor cursor = getHillGroup(groupId, countryClause, moreWhere, orderBy, filter);

            List<TinyHill> hills = new ArrayList<>();
            cursor.moveToFirst();

            do {
                TinyHill hill = new TinyHill();
                try {
                    hill.setDateClimbed(LocalDate.parse(cursor.getString
                                    (cursor.getColumnIndex(KEY_DATECLIMBED))
                            , ISODateTimeFormat.date()));
                } catch (NullPointerException npe) {
                    Log.v(TAG, "getHill: no date climbed");
                }
                hill._id = cursor.getInt(0);
                hill.setHeightF(cursor.getFloat(cursor
                        .getColumnIndex(KEY_HEIGHTF)));
                hill.setHeightM(cursor.getFloat(cursor
                        .getColumnIndex(KEY_HEIGHTM)));
                hill.setHillname(cursor.getString(cursor
                        .getColumnIndex(KEY_HILLNAME)));
                hill.setLatitude(cursor.getDouble(cursor
                        .getColumnIndex(KEY_LATITUDE)));
                hill.setLongitude(cursor.getDouble(cursor
                        .getColumnIndex(KEY_LONGITUDE)));
                if (hill.getDateClimbed() != null) {
                    hill.setClimbed(true);
                }
                hill.setNotes(cursor.getString(cursor
                        .getColumnIndex(KEY_NOTES)));

                hills.add(hill);

            } while (cursor.moveToNext());

            return hills;
        };

    }


    @Override
    public Observable<Hill> getHill(long rowIndex) {
        return makeObservable(getHillFromDatabase(rowIndex))
                .subscribeOn(Schedulers.computation());
    }


    private Callable<Hill> getHillFromDatabase(long _rowIndex) {
        return () -> {
            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
            queryBuilder.setTables(hills + " left join "
                    + baggingTable + " on " + hillsKeyId + "=" + baggingKeyId);

            queryBuilder.appendWhere(hillsKeyId + "=" + _rowIndex);
            SQLiteDatabase db = getWritableDatabase();

            Cursor cursor = queryBuilder.query(db, null, null,
                    null, null, null, null);

            if (cursor.moveToFirst()) {
                Hill hill = getHill(cursor);
                Log.d(TAG, hill.getHillname());
                cursor.close();
                return hill;
            } else {
                Log.d(TAG, "Not found");
                cursor.close();
                return null;
            }
        };
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
        String revision = cursor.getString(cursor
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
        LocalDate dateClimbed = null;

        try {
            dateClimbed = LocalDate.parse(cursor.getString(cursor.getColumnIndex(KEY_DATECLIMBED))
                    , ISODateTimeFormat.date());
        } catch (NullPointerException npe) {
            Log.d(TAG, "getHill: no date climbed");
        }

        String notes = cursor.getString(cursor
                .getColumnIndex(KEY_NOTES));

        Hill result = new Hill(_id, _section, hillname, section, region, area,
                heightm, heightf, map, map25, gridref, colgridref, colheight,
                drop, gridref10, feature, observations, survey, climbed,
                classification, revision, comments, xcoord, ycoord,
                latitude, longitude, streetmap, getamap, hillBagging,
                dateClimbed, notes);
        System.out.println(result.getClassification());
        return result;

    }

    @Override
    public void touch(Handler handler) {
        this.handler = handler;
        getReadableDatabase();
    }

}
