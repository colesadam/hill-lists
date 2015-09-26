package uk.colessoft.android.hilllist.contentprovider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import uk.colessoft.android.hilllist.database.ColumnKeys;
import uk.colessoft.android.hilllist.database.HillsDatabaseHelper;
import uk.colessoft.android.hilllist.database.TableNames;

public class HillsContentProvider extends ContentProvider {

	private HillsDatabaseHelper database;

	private static final int HILLS = 10;
	private static final int HILL_ID = 20;
	private static final int LISTS = 30;
	private static final int LISTS_HILL_ID = 40;

	private static final String AUTHORITY = "uk.colessoft.android.hilllist.contentprovider";

	private static final String HILLS_BASE_PATH = "hills";
	public static final Uri HILLS_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + HILLS_BASE_PATH);

	private static final String LISTS_BASE_PATH = "lists";
	public static final Uri LISTS_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + LISTS_BASE_PATH);

	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/hills";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/hill";

	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, HILLS_BASE_PATH, HILLS);
		sURIMatcher.addURI(AUTHORITY, HILLS_BASE_PATH + "/#", HILL_ID);
		sURIMatcher.addURI(AUTHORITY, LISTS_BASE_PATH, LISTS);
		sURIMatcher.addURI(AUTHORITY, LISTS_BASE_PATH + "/#", LISTS_HILL_ID);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		database = new HillsDatabaseHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		Log.i(HillsContentProvider.class.getName(),"#########query called");
		System.out.println("#########query called");
		// Uisng SQLiteQueryBuilder instead of query() method
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		// Check if the caller has requested a column which does not exists
		// checkColumns(projection);

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case HILLS:
			// Set the table
			queryBuilder.setTables(TableNames.HILLS_TABLE + " join "
							+ TableNames.TYPES_LINK_TABLE + " on "
							+ TableNames.HILLS_TABLE + "." + ColumnKeys.KEY_ID + "="
							+ ColumnKeys.KEY_HILL_ID + " left join "
							+ TableNames.BAGGING_TABLE + " on "
							+ TableNames.HILLS_TABLE + "." + ColumnKeys.KEY_ID + "="
							+ TableNames.BAGGING_TABLE + "." + ColumnKeys.KEY_ID
							+ " join " + TableNames.HILLTYPES_TABLE + " on "
							+ ColumnKeys.KEY_TYPES_ID + "="
							+ TableNames.HILLTYPES_TABLE + "." + ColumnKeys.KEY_ID

			);
			//queryBuilder.setDistinct(true);
			break;
		case HILL_ID:
			// Set the table
			queryBuilder.setTables(TableNames.HILLS_TABLE + " join "
					+ TableNames.TYPES_LINK_TABLE + " on "
					+ TableNames.HILLS_TABLE + "." + ColumnKeys.KEY_ID + "="
					+ ColumnKeys.KEY_HILL_ID + " left join "
					+ TableNames.BAGGING_TABLE + " on "
					+ TableNames.HILLS_TABLE + "." + ColumnKeys.KEY_ID + "="
					+ TableNames.BAGGING_TABLE + "." + ColumnKeys.KEY_ID);
			// Adding the ID to the original query
			queryBuilder.appendWhere(TableNames.HILLS_TABLE + "."
					+ ColumnKeys.KEY_ID + "=" + uri.getLastPathSegment());

			break;
		case LISTS:
			queryBuilder.setTables(TableNames.HILLTYPES_TABLE + "   join "
					+ TableNames.TYPES_LINK_TABLE + " on  "
					+ TableNames.HILLTYPES_TABLE + "._id="
					+ TableNames.TYPES_LINK_TABLE + ".type_id  join "
					+ TableNames.HILLS_TABLE + " on "
					+ TableNames.TYPES_LINK_TABLE + ".hill_id="
					+ TableNames.HILLS_TABLE + "._id");
			queryBuilder.setDistinct(true);

			break;
		case LISTS_HILL_ID:
			queryBuilder.setTables(TableNames.HILLTYPES_TABLE + "   join "
					+ TableNames.TYPES_LINK_TABLE + " on  "
					+ TableNames.HILLTYPES_TABLE + "._id="
					+ TableNames.TYPES_LINK_TABLE + ".type_id ");
			queryBuilder.appendWhere(ColumnKeys.KEY_HILL_ID + "="
					+ uri.getLastPathSegment());

			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		SQLiteDatabase db = database.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, null, null, sortOrder);
		// Make sure that potential listeners are getting notified
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
