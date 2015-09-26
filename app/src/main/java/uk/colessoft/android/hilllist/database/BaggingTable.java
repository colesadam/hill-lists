package uk.colessoft.android.hilllist.database;

import android.database.sqlite.SQLiteDatabase;

public class BaggingTable {
	
	private static final String BAGGING_CREATE = "CREATE TABLE 'Bagging' ('_id','dateClimbed','notes');";



	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(BAGGING_CREATE);
		
	}

}
