package uk.colessoft.android.hilllist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

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

    public void onCreate(SQLiteDatabase database, Context context) {

        start = System.nanoTime();
        database.execSQL("PRAGMA foreign_keys=ON;");
        database.execSQL(TableCreationStatements.HILLS_CREATE);
        database.execSQL(TableCreationStatements.HILLTYPES_CREATE);
        database.execSQL(TableCreationStatements.TYPESLINK_CREATE);

        // Populate static hill types data
        database.beginTransaction();
        for (int i = 0; i < HillTypes.HILL_TYPES_ARRAY.size(); i++) {
            database.execSQL("INSERT INTO " + TableNames.HILLTYPES_TABLE + " VALUES ("
                    + HillTypes.HILL_TYPES_ARRAY.keyAt(i) + ",'"
                    + HillTypes.HILL_TYPES_ARRAY.valueAt(i).name + "'," + HillTypes.HILL_TYPES_ARRAY.valueAt(i).importance + ",'" + HillTypes.HILL_TYPES_ARRAY.valueAt(i).description + "')");
        }
        database.setTransactionSuccessful();
        database.endTransaction();
        Log.d(HillsTables.class.getName(),
                "#####Finished inserting hill type information after " + (System.nanoTime() - start) / 1000000 + " ms");


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



    private static void profilingLog(String message) {
      //  Log.d(HillsTables.class.getName(),
        //        "####" + message + " at " + (System.nanoTime() - start) + " nanoseconds");
    }

}
