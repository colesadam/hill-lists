package uk.colessoft.android.hilllist.database;

import android.content.Context;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.util.Log;

public class HillsDatabaseHelper extends SQLiteOpenHelper {


    protected Context context;
    public static final String DATABASE_NAME = "hill-list.db";
    private static final int DATABASE_VERSION = 2;

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

    @Override
    public void onCreate(SQLiteDatabase db) {
        HillsTables.onCreate(db, context, handler, 9999999);
        BaggingTable.onCreate(db, context);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        HillsTables.onUpgrade(db, oldVersion, newVersion, context, handler, 99999999);
    }

}
