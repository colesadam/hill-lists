package uk.colessoft.android.hilllist.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import uk.colessoft.android.hilllist.model.Hill;

public class HillsDatabaseHelper  extends SQLiteOpenHelper implements DbHelper {
	
	private Context context;
	public static final String DATABASE_NAME = "hill-list.db";
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

	@Override
	public void markHillClimbed(int hillNumber, Date dateClimbed, String notes) {

	}

	@Override
	public void markHillNotClimbed(int hillNumber) {

	}

	@Override
	public Cursor getAllHillsCursor() {
		return null;
	}

	@Override
	public Cursor getBaggedHillList() {
		return null;
	}

	@Override
	public Cursor getHillsbyPartialName(String where, String orderBy) {
		return null;
	}

	@Override
	public Cursor getHillGroup(String groupId, String countryClause, String moreWhere, String orderBy, int filter) {
		return null;
	}

	@Override
	public Cursor setCursorHill(long _rowIndex) throws SQLException {
		return null;
	}

	@Override
	public Hill getHill(long _rowIndex) throws SQLException {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(HillsTables.HILLS_TABLE + " join "
				+ HillsTables.TYPES_LINK_TABLE + " on "
				+ HillsTables.HILLS_TABLE + "." + HillsTables.KEY_ID + "="
				+ HillsTables.KEY_HILL_ID + " left join "
				+ BaggingTable.BAGGING_TABLE + " on "
				+ HillsTables.HILLS_TABLE + "." + HillsTables.KEY_ID + "="
				+ BaggingTable.BAGGING_TABLE + "." + HillsTables.KEY_ID);
		// Adding the ID to the original query
		queryBuilder.appendWhere(HillsTables.HILLS_TABLE + "."
				+ HillsTables.KEY_ID + "=" + _rowIndex);

		SQLiteDatabase db = getWritableDatabase();
		String selection = null;
		String[] selectionArgs = null;
		String sortOrder = null;

		Cursor cursor = queryBuilder.query(db, null, selection,
				selectionArgs, null, null, sortOrder);

		if(cursor.moveToFirst()){
			return getHill(cursor);
		}
		return null;
	}

	@Override
	public int getClimbedCount(String hilltype, String countryClause, String moreWhere) {
		return 0;
	}

	private Hill getHill(Cursor cursor) throws SQLException {

		SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd");
		int _id = cursor.getInt(0);
		String _section = cursor.getString(cursor
				.getColumnIndex(HillsTables.KEY_XSECTION));
		String hillname = cursor.getString(cursor
				.getColumnIndex(HillsTables.KEY_HILLNAME));
		String section = cursor.getString(cursor
				.getColumnIndex(HillsTables.KEY_SECTION));
		String region = cursor.getString(cursor
				.getColumnIndex(HillsTables.KEY_REGION));
		String area = cursor.getString(cursor
				.getColumnIndex(HillsTables.KEY_AREA));
		float heightm = cursor.getFloat(cursor
				.getColumnIndex(HillsTables.KEY_HEIGHTM));
		float heightf = cursor.getFloat(cursor
				.getColumnIndex(HillsTables.KEY_HEIGHTF));
		String map = cursor.getString(cursor
				.getColumnIndex(HillsTables.KEY_MAP));
		String map25 = cursor.getString(cursor
				.getColumnIndex(HillsTables.KEY_MAP25));
		String gridref = cursor.getString(cursor
				.getColumnIndex(HillsTables.KEY_GRIDREF));
		String colgridref = cursor.getString(cursor
				.getColumnIndex(HillsTables.KEY_COLGRIDREF));
		float colheight = cursor.getFloat(cursor
				.getColumnIndex(HillsTables.KEY_COLHEIGHT));
		float drop = cursor.getFloat(cursor
				.getColumnIndex(HillsTables.KEY_DROP));
		String gridref10 = cursor.getString(cursor
				.getColumnIndex(HillsTables.KEY_GRIDREF10));
		String feature = cursor.getString(cursor
				.getColumnIndex(HillsTables.KEY_FEATURE));
		String observations = cursor.getString(cursor
				.getColumnIndex(HillsTables.KEY_OBSERVATIONS));
		String survey = cursor.getString(cursor
				.getColumnIndex(HillsTables.KEY_SURVEY));
		String climbed = cursor.getString(cursor
				.getColumnIndex(HillsTables.KEY_CLIMBED));
		String classification = cursor.getString(cursor
				.getColumnIndex(HillsTables.KEY_CLASSIFICATION));
		long revision = cursor.getLong(cursor
				.getColumnIndex(HillsTables.KEY_REVISION));
		String comments = cursor.getString(cursor
				.getColumnIndex(HillsTables.KEY_COMMENTS));
		int xcoord = cursor.getInt(cursor
				.getColumnIndex(HillsTables.KEY_XCOORD));
		int ycoord = cursor.getInt(cursor
				.getColumnIndex(HillsTables.KEY_YCOORD));
		double latitude = cursor.getDouble(cursor
				.getColumnIndex(HillsTables.KEY_LATITUDE));
		double longitude = cursor.getDouble(cursor
				.getColumnIndex(HillsTables.KEY_LONGITUDE));
		String streetmap = cursor.getString(cursor
				.getColumnIndex(HillsTables.KEY_STREETMAP));
		String getamap = cursor.getString(cursor
				.getColumnIndex(HillsTables.KEY_GEOGRAPH));
		String hillBagging = cursor.getString(cursor
				.getColumnIndex(HillsTables.KEY_HILLBAGGING));
		Date dateClimbed = null;

		try {
			dateClimbed = iso8601Format.parse(cursor.getString(cursor
					.getColumnIndex(BaggingTable.KEY_DATECLIMBED)));
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			Log.e(this.toString(), e.toString());
		} catch (NullPointerException npe) {
			// ignore
		}

		String notes = cursor.getString(cursor
				.getColumnIndex(BaggingTable.KEY_NOTES));

		Hill result = new Hill(_id, _section, hillname, section, region, area,
				heightm, heightf, map, map25, gridref, colgridref, colheight,
				drop, gridref10, feature, observations, survey, climbed,
				classification, new Date(revision), comments, xcoord, ycoord,
				latitude, longitude, streetmap, getamap, hillBagging,
				dateClimbed, notes);
		return result;

	}
}
