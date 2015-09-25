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

import uk.colessoft.android.hilllist.BHApp;

public class HillsTables {

    public static final String CLASSIFICATION_APPLIES = "1";
    private static Pattern pattern = Pattern.compile(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

    @Inject
    public String hillsCsv;

    public void onCreate(SQLiteDatabase database, Context context) {

        ((BHApp) BHApp.getAppContext()).component().inject(this);

        createTables(database);

        populateHillTypes(database);

        populateHillsAndLinks(database, context);

    }

    private void populateHillsAndLinks(SQLiteDatabase database, Context context) {
        InputStream is;
        try {
            is = context.getAssets().open(hillsCsv);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is));

            database.beginTransaction();

            // read and ignore title row
            reader.readLine();

            StringBuilder hillsInsertStringBuilder = new StringBuilder("INSERT INTO " + TableNames.HILLS_TABLE
                    + " VALUES(");
            for (int i = 1; i < ColumnNumbers.marilyn_COLUMN; i++)
                hillsInsertStringBuilder.append("?,");

            String insertHillsSql = hillsInsertStringBuilder.toString();
            insertHillsSql = insertHillsSql.substring(0, insertHillsSql.length() - 1);
            insertHillsSql = insertHillsSql + ")";

            SQLiteStatement insertHillsStatement = database.compileStatement(insertHillsSql);

            StringBuilder insertHillTypesLinkBuilder = new StringBuilder();

            insertHillTypesLinkBuilder.append("INSERT INTO "
                    + TableNames.TYPES_LINK_TABLE + " VALUES (null,?,?)");
            SQLiteStatement insertHillTypesStatement = database.compileStatement(insertHillTypesLinkBuilder.toString());

            String line = null;
            while ((line = reader.readLine()) != null) {
                int currentColumnNo = 1;
                String[] lineArray = pattern.split(line);

                for (String columnValue : lineArray) {
                    if (currentColumnNo < ColumnNumbers.marilyn_COLUMN) {
                        insertHillsStatement.bindString(currentColumnNo, columnValue);
                    } else {

                        if (CLASSIFICATION_APPLIES.equals(columnValue)) {

                            insertHillTypesStatement.bindString(1, lineArray[0]);
                            insertHillTypesStatement.bindString(2, String.valueOf(currentColumnNo));
                            insertHillTypesStatement.executeInsert();
                            insertHillTypesStatement.clearBindings();
                        }
                    }
                    currentColumnNo++;
                }
                insertHillsStatement.executeInsert();
                insertHillsStatement.clearBindings();
            }
            database.setTransactionSuccessful();
            database.endTransaction();

        } catch (IOException e) {
            Log.e(HillsTables.class.getName(),
                    "Failed to populate hills database table", e);

        }
    }

    private void populateHillTypes(SQLiteDatabase database) {
        database.beginTransaction();
        for (int i = 0; i < HillTypes.HILL_TYPES_ARRAY.size(); i++) {
            database.execSQL("INSERT INTO " + TableNames.HILLTYPES_TABLE + " VALUES ("
                    + HillTypes.HILL_TYPES_ARRAY.keyAt(i) + ",'"
                    + HillTypes.HILL_TYPES_ARRAY.valueAt(i).name + "'," + HillTypes.HILL_TYPES_ARRAY.valueAt(i).importance + ",'" + HillTypes.HILL_TYPES_ARRAY.valueAt(i).description + "')");
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    private void createTables(SQLiteDatabase database) {
        database.execSQL("PRAGMA foreign_keys=ON;");
        database.execSQL(TableCreationStatements.HILLS_CREATE);
        database.execSQL(TableCreationStatements.HILLTYPES_CREATE);
        database.execSQL(TableCreationStatements.TYPESLINK_CREATE);
    }

    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion, Context context) {
        ((BHApp) BHApp.getAppContext()).component().inject(this);
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
