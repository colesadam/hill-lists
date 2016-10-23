package uk.colessoft.android.hilllist.database;


import android.content.Context;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TestHelper extends HillsDatabaseHelper {

    private static final int DATABASE_VERSION = 1;
    private static TestHelper sInstance;

    public TestHelper(Context context) {
        super(context);
    }

    private TestHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                                int version) {
        super(context, name, factory, version);
        this.context = context;

    }

    public static synchronized TestHelper getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new TestHelper(context.getApplicationContext(), DATABASE_NAME
                    , (sqLiteDatabase, sqLiteCursorDriver, s, sqLiteQuery) -> {

                Log.d("SQL", sqLiteQuery.toString());

                return new SQLiteCursor(sqLiteDatabase, sqLiteCursorDriver, s, sqLiteQuery);
            }, DATABASE_VERSION);
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        HillsTables.onCreate(db, context, null, 100);
        BaggingTable.onCreate(db, context);
    }
}
