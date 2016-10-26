package uk.colessoft.android.hilllist.database;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;
import java.io.File;

public class OldHillDbAdapter {

    private static final String DATABASE_NAME = "hill-list.db";
    private static final String HILLS_TABLE = "hills";
    private static final String BAGGING_TABLE = "Bagging";
    private static final String KEY_ID = "_id";
    public static final String KEY_HILLNAME = "Hillname";
    private static final String KEY_DATECLIMBED = "dateClimbed";
    private static final String KEY_NOTES = "notes";

    private SQLiteDatabase db;
    private final HillDataBaseHelper dbHelper;

    OldHillDbAdapter() {
        dbHelper = new HillDataBaseHelper();
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

    Cursor getBaggedHillList() {
        return db.query(HILLS_TABLE + " INNER JOIN " + BAGGING_TABLE + " ON ("
                        + HILLS_TABLE + "._id" + "=" + BAGGING_TABLE + "._id)",
                new String[]{HILLS_TABLE + "." + KEY_ID, KEY_HILLNAME,
                        KEY_DATECLIMBED, KEY_NOTES}, null, null, null, null,
                null);
    }


    private static class HillDataBaseHelper {

        private static final String DB_PATH = Environment
                .getExternalStorageDirectory().getPath()
                + "/data/uk.colessoft.android.hilllist/databases/";

        private static final String DB_NAME = DATABASE_NAME;

        private final File dbFile = new File(DB_PATH + DB_NAME);
        private SQLiteDatabase database;

        void openDataBase() throws SQLException {

            String myPath = DB_PATH + DB_NAME;
            database = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READWRITE);

        }

        private boolean open() {
            boolean opened = false;
            if (dbFile.exists()) {
                try {
                    database = SQLiteDatabase.openDatabase(
                            dbFile.getAbsolutePath(), null,
                            SQLiteDatabase.OPEN_READWRITE);
                    opened = true;

                } catch (SQLiteException e) {
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

        synchronized SQLiteDatabase getReadableDatabase() {
            return getRDatabase();
        }

        private SQLiteDatabase getRDatabase() {
            boolean opened = true;
            if (database == null) {
                opened = open();
            }
            if (!opened) {
                if (database != null) {
                    database.close();
                }
                dbFile.delete();
                return null;
            }
            return database;
        }
    }

}
