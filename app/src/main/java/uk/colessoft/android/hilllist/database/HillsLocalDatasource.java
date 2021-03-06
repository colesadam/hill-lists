package uk.colessoft.android.hilllist.database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.AsyncTask;
import android.os.Handler;
import androidx.annotation.NonNull;
import android.util.Log;
import android.util.Pair;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import uk.colessoft.android.hilllist.dao.BaggingDao;
import uk.colessoft.android.hilllist.dao.CountryClause;
import uk.colessoft.android.hilllist.dao.HillDetailDao;
import uk.colessoft.android.hilllist.domain.entity.Bagging;
import uk.colessoft.android.hilllist.domain.entity.Hill;
import uk.colessoft.android.hilllist.domain.HillDetail;
import uk.colessoft.android.hilllist.utility.DistanceCalculator;

import static android.content.ContentValues.TAG;

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
import static uk.colessoft.android.hilllist.database.HillsTables.KEY_STREETMAP;
import static uk.colessoft.android.hilllist.database.HillsTables.KEY_SURVEY;
import static uk.colessoft.android.hilllist.database.HillsTables.KEY_TITLE;
import static uk.colessoft.android.hilllist.database.HillsTables.KEY_XCOORD;
import static uk.colessoft.android.hilllist.database.HillsTables.KEY_XSECTION;
import static uk.colessoft.android.hilllist.database.HillsTables.KEY_YCOORD;
import static uk.colessoft.android.hilllist.domain.entity.Bagging.BAGGING_TABLE;
import static uk.colessoft.android.hilllist.domain.entity.Bagging.KEY_DATECLIMBED;
import static uk.colessoft.android.hilllist.domain.entity.Bagging.KEY_NOTES;


public class HillsLocalDatasource implements BritishHillsDatasource {

    private final String typesLink = HillsTables.TYPES_LINK_TABLE;
    private final String typesLinkKeyId = typesLink + "." + HillsTables.KEY_TYPES_ID;
    private final String hills = HILLS_TABLE;
    private final String hillsKeyId = hills + "." + KEY_ID;


    private final String baggingTable = BAGGING_TABLE;
    private final String baggingKeyId = baggingTable + "." + "b_id";
    protected Context context;

    private String hillTypes = HillsTables.HILLTYPES_TABLE;
    private String typesLinkKeyHillId = typesLink + "." + "tl_id";
    private String hillTypesKeyId = hillTypes + "." + "ht_id";
    private String hillTypesTitle = hillTypes + "." + KEY_TITLE;


    private SupportSQLiteOpenHelper dbHelper;

    @Inject
    public HillsLocalDatasource(HillsDatabase database){
        this.dbHelper = database.getOpenHelper();
    }

    @Inject
    public HillDetailDao hillDetailDao;

    @Inject
    public BaggingDao baggingDao;

    protected static HillsLocalDatasource sInstance;

    private SupportSQLiteDatabase getWritableDatabase() {
        return dbHelper.getWritableDatabase();
    }

    private SupportSQLiteDatabase getReadableDatabase() {
        return dbHelper.getReadableDatabase();
    }


    @Override
    public void markHillClimbed(long hillNumber, Date dateClimbed, String notes){
        new markHillClimbedAsyncTask(baggingDao).execute(new Bagging(hillNumber, dateClimbed, notes));
    }

    private static class markHillClimbedAsyncTask extends AsyncTask<Bagging, Void, Void> {

        private BaggingDao baggingDao;

        markHillClimbedAsyncTask(BaggingDao dao) {
            baggingDao = dao;
        }

        @Override
        protected Void doInBackground(final Bagging... params) {
            baggingDao.insertBagging(params[0]);
            return null;
        }
    }

    @Override
    public void markHillNotClimbed(long hillNumber){
        new markHillNotClimbedAsyncTask(baggingDao).execute(hillNumber);
    }

    private static class markHillNotClimbedAsyncTask extends AsyncTask<Long, Void, Void> {

        private BaggingDao baggingDao;

        markHillNotClimbedAsyncTask(BaggingDao dao) {
            baggingDao = dao;
        }

        @Override
        protected Void doInBackground(final Long... params) {
            baggingDao.deleteByHillId(params[0]);
            return null;
        }
    }


    @Override
    public Cursor getAllHillsCursor() {
        Log.d(TAG, "getAllHillsCursor: ######## getting all hills");
        SupportSQLiteDatabase db = getReadableDatabase();
        return db.query("select " + HILLS_TABLE + "." + KEY_ID + " as hill_id ,"+ KEY_LATITUDE +"," + KEY_LONGITUDE+","+ KEY_HEIGHTM+","+ KEY_HEIGHTF+","+ KEY_HILLNAME+","+
                KEY_NOTES+","+ KEY_DATECLIMBED +" from "+ HILLS_TABLE + " LEFT OUTER JOIN " + BAGGING_TABLE
                        + " ON (" + HILLS_TABLE + ".h_id" + "=" + BAGGING_TABLE
                        + ".b_id)");
    }

    @Override
    public Cursor getHillsForNearby() {
        Log.d(TAG, "getHillsForNearby: ######## getting all hills");
        SupportSQLiteDatabase db = getReadableDatabase();
        return db.query("select " + HILLS_TABLE+ "." + KEY_ID + " as hill_id,"+ KEY_LATITUDE+","+ KEY_LONGITUDE+","+ KEY_HEIGHTM+","+ KEY_HEIGHTF+","+ KEY_HILLNAME+" from "+ HILLS_TABLE);

    }

    @Override
    public Cursor getBaggedHillList() {
        SupportSQLiteDatabase db = getReadableDatabase();
        return db.query("select " + HILLS_TABLE + "." + KEY_ID+","+ KEY_HILLNAME+","+
                KEY_DATECLIMBED+","+ KEY_NOTES+" from "+HILLS_TABLE + " INNER JOIN " + BAGGING_TABLE + " ON ("
                        + HILLS_TABLE + ".h_id" + "=" + BAGGING_TABLE + ".b_id)");
    }

    @Override
    public HillDetail getHill(long _rowIndex) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(hills + " left join "
                + baggingTable + " on " + hillsKeyId + "=" + baggingKeyId);

        queryBuilder.appendWhere(hillsKeyId + "=" + _rowIndex);

        SupportSQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.query("select  * from "+hills + " left join "
                        + baggingTable + " on " + hillsKeyId + "=" + baggingKeyId + " where " + hillsKeyId + "=" + _rowIndex, null);

        if (cursor.moveToFirst()) {
            HillDetail hillDetail = getHill(cursor);
            cursor.close();
            return hillDetail;
        } else {
            cursor.close();
            return null;
        }

    }

    @Override
    public LiveData<HillDetail> getHillReactive(long hillId) {
        return hillDetailDao.getHillDetail(hillId);
    }

    @Override
    public LiveData<List<HillDetail>> getHills(String groupId, CountryClause country, String moreFilters) {
        return hillDetailDao.getHills(groupId, country, moreFilters);
    }

    @Override
    public LiveData<List<Pair<Double,HillDetail>>> getHills(Double latitude, Double longitude, Float range) {
        return Transformations.map(
                hillDetailDao.getHills(),hills ->
                        hills.stream().map(hillDetail -> new Pair<>(getDistanceKm(hillDetail,latitude,longitude),hillDetail))
                                .filter(hillPair -> hillPair.first <= range).collect(Collectors.toList()));
    }

    private double getDistanceKm(HillDetail hillDetail, Double lat1, Double lon1) {
        Double lat = hillDetail.getHill().getLatitude();
        Double lng = hillDetail.getHill().getLongitude();

        return DistanceCalculator.calculationByDistance(
                lat1, lat, lon1, lng );
    }

    @Override
    public void importBagging(Reader fileReader) {

        SupportSQLiteDatabase db = getWritableDatabase();

        try {
            db.delete(BAGGING_TABLE, null, null);
            CSVReader csvReader = new CSVReader(fileReader, ',', '\'');
            List<String[]> allOfit = csvReader.readAll();

            ContentValues values;
            for (String[] row : allOfit) {
                int size = row.length;
                String notes = row[2];
                if (size > 3) {
                    for (int i = 3; i < size; i++) {
                        notes = notes + "," + row[i];
                    }
                }
                values = new ContentValues();
                values.put("tl_id", row[0]);
                values.put("dateClimbed", row[1]);
                values.put("notes", notes);
                db.insert(BAGGING_TABLE, 0, values);

            }
        } catch (IOException e) {
            Log.e(this.toString(), "error: " + e.toString());
        } catch (SQLiteException se) {
            se.printStackTrace();
        }
    }

    private HillDetail getHill(Cursor cursor) throws SQLException {

        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd");
        int _id = cursor.getInt(0);
        String _section = cursor.getString(cursor
                .getColumnIndex(KEY_XSECTION));
        String hillname = cursor.getString(cursor
                .getColumnIndex(KEY_HILLNAME));
        String section = cursor.getString(cursor
                .getColumnIndex(KEY_SECTION));
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

        HillDetail result = new HillDetail(new Hill(
                _id,
                hillname,"","",
                section,"",
                area,"","","",
                classification,
                map,
                map25,
                heightm,
                heightf,
                gridref,
                gridref10,
                drop,
                colgridref,
                colheight,
                feature,
                observations,
                survey,
                climbed,"","",
                revision,
                comments,
                streetmap,"",
                hillBagging,
                xcoord,
                ycoord,
                latitude,
                longitude,
                _section,"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""));
        System.out.println(result.getHill().getClassification());
        Date finalDateClimbed = dateClimbed;
        if(finalDateClimbed != null) {
            result.setBagging(new ArrayList<Bagging>() {{
                add(new Bagging(_id, finalDateClimbed, notes));
            }});
        }
        return result;

    }

}
