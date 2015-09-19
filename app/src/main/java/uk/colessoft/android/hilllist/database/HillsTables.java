package uk.colessoft.android.hilllist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import javax.inject.Inject;

import uk.colessoft.android.hilllist.BritishHillsApplication;

public class HillsTables {

    private static Pattern pattern = Pattern.compile(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

    public static long start;

    @Inject
    public String hillsCsv;

    public void onCreate(SQLiteDatabase database, Context context) {

        ((BritishHillsApplication) BritishHillsApplication.getAppContext()).component().inject(this);
        Log.d(HillsTables.class.getName(), "#################" + hillsCsv);
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



            // read and ignore title row
            reader.readLine();

            // read each line of text file

            StringBuilder hillsInsertStringBuilder = new StringBuilder("INSERT INTO " + TableNames.HILLS_TABLE
                    + " VALUES(");
            for (int i = 1; i < ColumnNumbers.marilyn_COLUMN; i++)
                hillsInsertStringBuilder.append("?,");

            String insertHillsSql = hillsInsertStringBuilder.toString();
            insertHillsSql = insertHillsSql.substring(0, insertHillsSql.length() - 1);
            insertHillsSql = insertHillsSql + ")";

            SQLiteStatement insertHillsStatement = database.compileStatement(insertHillsSql);

            StringBuilder insertHillTypesLinkBuilder;
            insertHillTypesLinkBuilder = new StringBuilder();

            insertHillTypesLinkBuilder.append("INSERT INTO "
                    + TableNames.TYPES_LINK_TABLE + " VALUES (null,?,?)");
            SQLiteStatement insertHillTypesStatement = database.compileStatement(insertHillTypesLinkBuilder.toString());

            Log.d(HillsTables.class.getName(),
                    "####Starting insert of hills table after " + (System.nanoTime() - start) / 1000000 + " ms");
            while ((line = reader.readLine()) != null) {
                int col = 1;
                String[] lineArray = pattern.split(line);


                for (String entry : lineArray) {
                    if (col < ColumnNumbers.marilyn_COLUMN) {
                        insertHillsStatement.bindString(col, entry.replace("'", "''"));
                    }else{


                        // get next token check to see if it applies
                        String nextToken = entry;

                        // The csv column has 1 if the classification applies, 0
                        // if
                        // not.
                        if ("1".equals(nextToken)) {
                            // we know that col should reference the correct
                            // value
                            // in the types table
                            insertHillTypesStatement.bindString(1,lineArray[0]);
                            insertHillTypesStatement.bindString(2,String.valueOf(col));
                            insertHillTypesStatement.executeInsert();
                            insertHillTypesStatement.clearBindings();


                        }
                    }
                    col++;

                }
                insertHillsStatement.executeInsert();
                insertHillsStatement.clearBindings();

            }
            database.setTransactionSuccessful();
            database.endTransaction();
            Log.d(HillsTables.class.getName(),
                    "####Finished inserting base hill information after " + (System.nanoTime() - start) / 1000000 + "ms");
        } catch (IOException e) {
            Log.e(HillsTables.class.getName(),
                    "Failed to populate hills database table", e);
        }


    }

    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion, Context context) {
        ((BritishHillsApplication) BritishHillsApplication.getAppContext()).component().inject(this);
        Log.d(HillsTables.class.getName(), "#################" + hillsCsv);
        Log.w(HillsTables.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old hills data");
        database.execSQL("DROP TABLE IF EXISTS " + TableNames.HILLS_TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + TableNames.HILLTYPES_TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + TableNames.TYPES_LINK_TABLE);
        onCreate(database, context);
    }

}
