package uk.colessoft.android.hilllist.database;

import android.database.sqlite.SQLiteDatabase;

public class BaggingTable {
	
	private static final String BAGGING_CREATE = "CREATE TABLE 'Bagging' ('_id','dateClimbed','notes');";
	public static final String KEY_DATECLIMBED = "dateClimbed";
	public static final String KEY_NOTES = "notes";
	public static final String BAGGING_TABLE = "Bagging";

	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(BAGGING_CREATE);
		
	}

}
