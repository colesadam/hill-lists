package uk.colessoft.android.hilllist.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class BaggingTable {
	
	private static final String BAGGING_CREATE = "CREATE TABLE 'Bagging' ('_id','dateClimbed','notes');";
	public static final String KEY_DATECLIMBED = "dateClimbed";
	public static final String KEY_NOTES = "notes";
	public static final String BAGGING_TABLE = "Bagging";

	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(BAGGING_CREATE);

		OldHillDbAdapter oldHillDbAdapter = new OldHillDbAdapter();
		try {
			oldHillDbAdapter.open();
			Cursor oldBagging = oldHillDbAdapter.getBaggedHillList();

			if(oldBagging.moveToFirst()){
                Log.d(TAG, "onCreate: importing old bagging");
                SQLiteStatement insertB = db.compileStatement("INSERT into " + BAGGING_TABLE + " VALUES(?,?,?)");
                db.beginTransaction();
                do {
                    insertB.bindString(1,oldBagging.getString(0));
                    insertB.bindString(2,oldBagging.getString(2));
                    insertB.bindString(3,oldBagging.getString(3));

                    insertB.executeInsert();
                }while(oldBagging.moveToNext());

				db.setTransactionSuccessful();
                db.endTransaction();
            }
			oldHillDbAdapter.close();
		} catch (SQLException e) {
			Log.d(TAG,"No old bagging table imported");
		}

	}

}
