package uk.colessoft.android.hilllist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class HillsDatabaseHelper extends SQLiteOpenHelper {
	
	private Context context;
	private static final String DATABASE_NAME = "hill-list.db";
	private static final int DATABASE_VERSION = 8;

	public HillsDatabaseHelper(Context context) {
		
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    this.context=context;
	  }

	public HillsDatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		this.context=context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		HillsTables.onCreate(db,context);
		BaggingTable.onCreate(db);
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		HillsTables.onUpgrade(db, oldVersion, newVersion, context);
		
	}

}
