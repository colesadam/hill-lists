package uk.colessoft.android.hilllist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.util.SparseArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.inject.Inject;

public class HillsTables {

    private static Pattern pattern = Pattern.compile(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

    public static long start;

    @Inject
    public String hillsCsv;

    private static final SparseArray<TempHillClassification> hillTypesArray;

    static {
        hillTypesArray = new SparseArray<TempHillClassification>();

        hillTypesArray.append(ColumnNumbers.munro_COLUMN, new HillsTables.TempHillClassification("Munro", 20, "Scottish hills at least 3000 feet in height regarded by the SMC as distinct and separate mountains."));
        hillTypesArray.append(ColumnNumbers.marilyn_COLUMN, new TempHillClassification("Marilyn", 19, "British and Irish hills of any height with a drop of at least 150 metres on all sides."));
        hillTypesArray.append(ColumnNumbers.marilynTwinTop_COLUMN, new TempHillClassification("Marilyn Twin Top", 0, ""));
        hillTypesArray.append(ColumnNumbers.hump_COLUMN, new TempHillClassification("HuMP", 2, "British and Irish Hills of any height with a drop of at least 100 metres or more on all sides."));
        hillTypesArray.append(ColumnNumbers.humpTwinTop_COLUMN, new TempHillClassification("HuMP Twin Top", 0, ""));

        hillTypesArray.append(ColumnNumbers.munroTop_COLUMN, new TempHillClassification("Munro Top", 0, ""));
        hillTypesArray.append(ColumnNumbers.murdo_COLUMN, new TempHillClassification("Murdo", 16, "Scottish hills at least 3000 feet in height with a drop of at least 30 metres on all sides."));
        hillTypesArray.append(ColumnNumbers.furth_COLUMN, new TempHillClassification("Furth", 15, "Summits equivalent to the Munros and Tops in England, Wales and Ireland."));
        hillTypesArray.append(ColumnNumbers.corbett_COLUMN, new TempHillClassification("Corbett", 18, "Scottish hills between 2500 and 2999 feet high with a drop of at least 500 feet (152.4m) on all sides."));
        hillTypesArray.append(ColumnNumbers.corbettTop_COLUMN, new TempHillClassification("Corbett Top", 0, ""));
        hillTypesArray.append(ColumnNumbers.corbettTopOfMunro_COLUMN, new TempHillClassification("Corbett Top of Munro", 0, ""));
        hillTypesArray.append(ColumnNumbers.corbettTopOfCorbett_COLUMN, new TempHillClassification(
                "Corbett Top of Corbett", 0, ""));
        hillTypesArray.append(ColumnNumbers.graham_COLUMN, new TempHillClassification("Graham", 17, "Scottish hills between 2000 and 2499 feet high with a drop of at least 150 metres on all sides."));
        hillTypesArray.append(ColumnNumbers.grahamTop_COLUMN, new TempHillClassification("Graham Top", 0, ""));
        hillTypesArray.append(ColumnNumbers.grahamTopOfMunro_COLUMN, new TempHillClassification("Graham Top of Munro", 0, ""));
        hillTypesArray.append(ColumnNumbers.grahamTopOfCorbett_COLUMN, new TempHillClassification(
                "Graham Top of Corbett", 0, ""));
        hillTypesArray.append(ColumnNumbers.grahamTopOfGraham_COLUMN, new TempHillClassification("Graham Top of Graham", 0, ""));
        hillTypesArray.append(ColumnNumbers.grahamTopOfHump_COLUMN, new TempHillClassification("Graham Top of HuMP", 0, ""));
        hillTypesArray.append(ColumnNumbers.donald_COLUMN, new TempHillClassification("Donald", 14, "Hills in the Scottish Lowlands at least 2000 feet high."));
        hillTypesArray.append(ColumnNumbers.donaldTop_COLUMN, new TempHillClassification("Donald Top", 0, ""));
        hillTypesArray.append(ColumnNumbers.hewitt_COLUMN, new TempHillClassification("Hewitt", 13, "Hills in England, Wales and Ireland at least 2000 feet high with a drop of at least 30 metres on all sides."));
        hillTypesArray.append(ColumnNumbers.nuttall_COLUMN, new TempHillClassification("Nuttall", 12, "Hills in England and Wales at least 2000 feet high with a drop of at least 15 metres on all sides."));
        hillTypesArray.append(ColumnNumbers.dillon_COLUMN, new TempHillClassification("Dillon", 11, "Hills in Ireland at least 2000 feet high published in The Mountains of Ireland."));
        hillTypesArray.append(ColumnNumbers.arderin_COLUMN, new TempHillClassification("Arderin", 10, "Hills in Ireland at least 500 metres high with a drop of at least 30m on all sides."));
        hillTypesArray.append(ColumnNumbers.vandeleurLynam_COLUMN, new TempHillClassification("Vandeleur-Lynam", 9, "Hills in Ireland at least 600 metres high with a drop of at least 15 metres on all sides."));
        hillTypesArray.append(ColumnNumbers.sim_COLUMN, new TempHillClassification("Sim", 8, "Hills in Britain and Ireland at least 600 metres high with a drop of at least 30 metres on all sides."));
        hillTypesArray.append(ColumnNumbers.dewey_COLUMN, new TempHillClassification("Dewey", 7, "Hills in England,Wales and IOM > 500m high and < 609.6m with a drop of at least 30m on all sides."));
        hillTypesArray.append(ColumnNumbers.donaldDewey_COLUMN, new TempHillClassification("Donald Dewey", 0, ""));
        hillTypesArray.append(ColumnNumbers.highland5_COLUMN, new TempHillClassification("Highland Five", 0, ""));
        hillTypesArray.append(ColumnNumbers.myrddynDeweyCOLUMN, new TempHillClassification("Myrddyn Dewey", 0, ""));
        hillTypesArray.append(ColumnNumbers.h490_COLUMN, new TempHillClassification("490-499m with 30m drop", 0, ""));
        hillTypesArray.append(ColumnNumbers.wainwright_COLUMN, new TempHillClassification("Wainwright", 6, "The 214 hills listed in volumes 1-7 of Wainwright''s A Pictorial Guide to the Lakeland Fells."));
        hillTypesArray.append(ColumnNumbers.wainwrightOutlyingFell_COLUMN, new TempHillClassification(
                "Wainwright Outlying Fells", 7, "Hills listed in The Outlying Fells of Lakeland."));
        hillTypesArray.append(ColumnNumbers.birkett_COLUMN, new TempHillClassification("Birkett", 5, "Lake District hills over 1,000ft listed in Bill Birkett''s Complete Lakeland Fells."));
        hillTypesArray
                .append(ColumnNumbers.countyTopHistoric_COLUMN, new TempHillClassification("County Top - Historic", 0, ""));
        hillTypesArray.append(ColumnNumbers.countyTopCurrent_COLUMN, new TempHillClassification(
                "County Top � Current County and Unitary Authority", 0, ""));
        hillTypesArray.append(ColumnNumbers.countyTopCurrentTwin_COLUMN, new TempHillClassification("Current County Twin Top", 0, ""));
        hillTypesArray.append(ColumnNumbers.countyTopAdministrative_COLUMN, new TempHillClassification(
                "County Top � Administrative", 0, ""));
        hillTypesArray.append(ColumnNumbers.countyTopLondonBorough_COLUMN, new TempHillClassification(
                "County Top � London Borough", 0, ""));
        hillTypesArray.append(ColumnNumbers.countyTopLondonBoroughTwin_COLUMN, new TempHillClassification("London Borough Twin Top", 0, ""));
        hillTypesArray.append(ColumnNumbers.subMarilyn_COLUMN, new TempHillClassification("Sub Marilyn", 0, ""));
        hillTypesArray.append(ColumnNumbers.subHump_COLUMN, new TempHillClassification("Sub HuMP", 0, ""));
        hillTypesArray.append(ColumnNumbers.subMurdo_COLUMN, new TempHillClassification("Sub Murdo", 0, ""));
        hillTypesArray.append(ColumnNumbers.subCorbett_COLUMN, new TempHillClassification("Sub Corbett", 0, ""));
        hillTypesArray.append(ColumnNumbers.subGrahamTop_COLUMN, new TempHillClassification("Sub Graham Top", 0, ""));
        hillTypesArray.append(ColumnNumbers.subHewitt_COLUMN, new TempHillClassification("Sub Hewitt", 0, ""));
        hillTypesArray.append(ColumnNumbers.subDewey_COLUMN, new TempHillClassification("Sub Dewey", 0, ""));
        hillTypesArray.append(ColumnNumbers.subDonaldDewey_COLUMN, new TempHillClassification("Sub Donald Dewey", 0, ""));
        hillTypesArray.append(ColumnNumbers.subHighland5_COLUMN, new TempHillClassification("Sub Highland Five", 0, ""));
        hillTypesArray.append(ColumnNumbers.subMyrddynDewey_COLUMN, new TempHillClassification("Sub Myrddyn Dewey", 0, ""));
        hillTypesArray.append(ColumnNumbers.subh490_COLUMN, new TempHillClassification("Sub Hills 490-499m", 0, ""));
        hillTypesArray.append(ColumnNumbers.deletedMunroTop_COLUMN, new TempHillClassification("Deleted Munro Top", 0, ""));
        hillTypesArray.append(ColumnNumbers.deletedCorbett_COLUMN, new TempHillClassification("Deleted Corbett", 0, ""));
        hillTypesArray.append(ColumnNumbers.deletedNuttall_COLUMN, new TempHillClassification("Deleted Nuttall", 0, ""));
        hillTypesArray.append(ColumnNumbers.deletedDonaldTop_COLUMN, new TempHillClassification("Deleted Donald Top", 0, ""));
        hillTypesArray.append(ColumnNumbers.buxtonAndLewis_COLUMN, new TempHillClassification("Buxton and Lewis", 0, ""));
        hillTypesArray.append(ColumnNumbers.bridge_COLUMN, new TempHillClassification("Bridge", 0, ""));
        hillTypesArray.append(ColumnNumbers.trail100_COLUMN, new TempHillClassification("Trail 100", 0, ""));
        hillTypesArray.append(ColumnNumbers.carns_COLUMN, new TempHillClassification("Carns", 0, ""));
        hillTypesArray.append(ColumnNumbers.binnions_COLUMN, new TempHillClassification("Binnions", 0, ""));
        hillTypesArray.append(ColumnNumbers.otherList_COLUMN, new TempHillClassification("Other", 0, ""));
        hillTypesArray.append(ColumnNumbers.un_COLUMN, new TempHillClassification("Unclassified", 0, ""));

    }

    // Database creation SQL statement
    private static final String HILLS_CREATE = "CREATE TABLE '" + TableNames.HILLS_TABLE
            + "' ('" + ColumnKeys.KEY_ID + "' integer primary key,'" + ColumnKeys.KEY_HILLNAME
            + "','" + ColumnKeys.KEY_SECTION + "','" + ColumnKeys.KEY_REGION + "','" + ColumnKeys.KEY_AREA
            + "','" + ColumnKeys.KEY_CLASSIFICATION + "','" + ColumnKeys.KEY_MAP + "','" + ColumnKeys.KEY_MAP25
            + "','" + ColumnKeys.KEY_HEIGHTM + "','" + ColumnKeys.KEY_HEIGHTF + "','" + ColumnKeys.KEY_GRIDREF
            + "','" + ColumnKeys.KEY_GRIDREF10 + "','" + ColumnKeys.KEY_DROP + "','" + ColumnKeys.KEY_COLGRIDREF
            + "','" + ColumnKeys.KEY_COLHEIGHT + "','" + ColumnKeys.KEY_FEATURE + "','"
            + ColumnKeys.KEY_OBSERVATIONS + "','" + ColumnKeys.KEY_SURVEY + "','" + ColumnKeys.KEY_CLIMBED
            + "','" + ColumnKeys.KEY_COUNTRY + "','" + ColumnKeys.KEY_COUNTY + "','" + ColumnKeys.KEY_REVISION
            + "','" + ColumnKeys.KEY_COMMENTS + "','" + ColumnKeys.KEY_STREETMAP + "','"
            + ColumnKeys.KEY_GEOGRAPH + "','" + ColumnKeys.KEY_HILLBAGGING + "','" + ColumnKeys.KEY_XCOORD
            + "','" + ColumnKeys.KEY_YCOORD + "','" + ColumnKeys.KEY_LATITUDE + "','" + ColumnKeys.KEY_LONGITUDE
            + "','" + ColumnKeys.KEY_XSECTION + "');";


    private static final String HILLTYPES_CREATE = "CREATE TABLE "
            + TableNames.HILLTYPES_TABLE + "(" + ColumnKeys.KEY_ID + " integer primary key," + ColumnKeys.KEY_TITLE + "," + ColumnKeys.KEY_IMPORTANCE + " integer," + ColumnKeys.KEY_DESC + ")";

    private static final String TYPESLINK_CREATE = "CREATE TABLE "
            + TableNames.TYPES_LINK_TABLE + "(" + ColumnKeys.KEY_ID
            + " integer primary key autoincrement," + ColumnKeys.KEY_HILL_ID
            + " references " + TableNames.HILLS_TABLE + "(" + ColumnKeys.KEY_ID + ")," + ColumnKeys.KEY_TYPES_ID
            + " references " + TableNames.HILLTYPES_TABLE + "(" + ColumnKeys.KEY_ID + "))";

    public void onCreate(SQLiteDatabase database, Context context) {
        // create tables

        start = System.nanoTime();
        database.execSQL("PRAGMA foreign_keys=ON;");
        database.execSQL(HILLS_CREATE);
        database.execSQL(HILLTYPES_CREATE);
        database.execSQL(TYPESLINK_CREATE);

        // populate static hills data

        InputStream is;
        try {
            is = context.getAssets().open(hillsCsv);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is));

            // read main hill data into hills table
            database.beginTransaction();
            String line = null;

            int col = 1;

            // read and ignore title row
            reader.readLine();

            // read each line of text file

            StringBuilder sqlBuilder = new StringBuilder("INSERT INTO " + TableNames.HILLS_TABLE
                    + " VALUES(");
            for (int i = 1; i < ColumnNumbers.marilyn_COLUMN; i++)
                sqlBuilder.append("?,");

            String sql = sqlBuilder.toString();
            sql = sql.substring(0, sql.length() - 1);
            sql = sql + ")";

            SQLiteStatement stmt = database.compileStatement(sql);
            Log.d(HillsTables.class.getName(),
                    "####Starting insert of hills table after " + (System.nanoTime() - start) / 1000000 + " ms");
            while ((line = reader.readLine()) != null) {
                profilingLog("Read next line");
                String[] lineArray = pattern.split(line);
                profilingLog("Split line");
                // read lines up to marilyn column - the first type column

                for (String entry : lineArray) {
                    if (col < ColumnNumbers.marilyn_COLUMN) {
                        stmt.bindString(col, entry.replace("'", "''"));
                    }
                    col++;

                }
                profilingLog("Modified Columns");

                col = 1;

                stmt.executeInsert();
                stmt.clearBindings();
                profilingLog("Executed statement");


            }
            database.setTransactionSuccessful();
            database.endTransaction();
            Log.d(HillsTables.class.getName(),
                    "####Finished inserting base hill information after " + (System.nanoTime() - start) / 1000000 + "ms");
        } catch (IOException e) {
            Log.e(HillsTables.class.getName(),
                    "Failed to populate hills database table", e);
        }

        // Populate static hill types data
        database.beginTransaction();
        for (int i = 0; i < hillTypesArray.size(); i++) {
            database.execSQL("INSERT INTO " + TableNames.HILLTYPES_TABLE + " VALUES ("
                    + hillTypesArray.keyAt(i) + ",'"
                    + hillTypesArray.valueAt(i).name + "'," + hillTypesArray.valueAt(i).importance + ",'" + hillTypesArray.valueAt(i).description + "')");
        }
        database.setTransactionSuccessful();
        database.endTransaction();
        Log.d(HillsTables.class.getName(),
                "#####Finished inserting hill type information after " + (System.nanoTime() - start) / 1000000 + " ms");

        // Populate link table for hill types

        try {
            is = context.getAssets().open(hillsCsv);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is));

            // read main hill data into hills table
            database.beginTransaction();
            String line = null;

            int col = 32;

            // read and ignore title row
            reader.readLine();
            // read each line of text file
            String hillId = "";
            StringTokenizer st;
            while ((line = reader.readLine()) != null) {

                String[] lineArray = pattern.split(line);
                // read lines up to marilyn column - the first type column
                StringBuilder insertHillTypesLinkBuilder;
                hillId = lineArray[0];
                for (int i = col - 1; i < lineArray.length; i++) {
                    insertHillTypesLinkBuilder = new StringBuilder();
                    insertHillTypesLinkBuilder.append("INSERT INTO "
                            + TableNames.TYPES_LINK_TABLE + " VALUES (null,");
                    // get next token check to see if it applies
                    String nextToken = lineArray[i];

                    // The csv column has 1 if the classification applies, 0
                    // if
                    // not.
                    if ("1".equals(nextToken)) {
                        // we know that col should reference the correct
                        // value
                        // in the types table
                        insertHillTypesLinkBuilder
                                .append(hillId + "," + (i + 1) + ")");
                        database.execSQL(insertHillTypesLinkBuilder.toString());

                    }
                }
            }
            database.setTransactionSuccessful();
            database.endTransaction();
            Log.d(HillsTables.class.getName(),
                    "####Finished inserting hill links information after " + (System.nanoTime() - start) / 1000000 + " ms");
        } catch (IOException e) {
            Log.e(HillsTables.class.getName(),
                    "Failed to populate link database table", e);
        }

    }

    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion, Context context) {
        Log.w(HillsTables.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old hills data");
        database.execSQL("DROP TABLE IF EXISTS " + TableNames.HILLS_TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + TableNames.HILLTYPES_TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + TableNames.TYPES_LINK_TABLE);
        onCreate(database, context);
    }

    private static class TempHillClassification {

        String name;
        int importance;
        String description;

        public TempHillClassification(String name,
                                      int importance, String description) {


            this.name = name;
            this.importance = importance;
            this.description = description;
        }


    }

    private static void profilingLog(String message) {
      //  Log.d(HillsTables.class.getName(),
        //        "####" + message + " at " + (System.nanoTime() - start) + " nanoseconds");
    }

}
