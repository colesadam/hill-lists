package uk.colessoft.android.hilllist.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Date;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.activities.ListHillsMapFragmentActivity;
import uk.colessoft.android.hilllist.activities.Main;
import uk.colessoft.android.hilllist.activities.PreferencesActivity;
import uk.colessoft.android.hilllist.database.HillDbAdapter;

public class DisplayHillListFragment extends Fragment implements
		LoaderManager.LoaderCallbacks<Cursor> {
	
	private int climbedCount=0;


	private class HillsViewBinder implements ViewBinder {

		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			

			TextView tv = (TextView) view;

			String vtext = cursor.getString(columnIndex);
			switch (tv.getId()) {
			case R.id.name_entry: {
				tv.setTextColor(Color.WHITE);
				if (cursor.getString(cursor
						.getColumnIndex(HillDbAdapter.KEY_DATECLIMBED)) != null) {

					tv.setTextColor(Color.GREEN);

				}
				tv.setText(vtext);
				

				return true;
			}

			case R.id.number_entry: {
				if (useMetricHeights) {
					vtext = convText(tv, vtext) + "m";
				} else
					vtext = convText(tv, vtext) + "ft";
				tv.setText(vtext);
				dbAdapter.close();
				return true;
			}
			case R.id.check_hill_climbed: {
				CheckBox ctv = (CheckBox) tv;
				final int id = cursor.getInt(cursor
						.getColumnIndex(HillDbAdapter.KEY_ID));
				if (cursor.getString(cursor
						.getColumnIndex(HillDbAdapter.KEY_DATECLIMBED)) != null) {

					ctv.setChecked(true);

				} else
					ctv.setChecked(false);
				ctv.setOnClickListener(null);
				ctv.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						dbAdapter.open();
						CheckBox xcv = (CheckBox) v;
						if (xcv.isChecked()) {
							((TextView) ((ViewGroup) ((ViewGroup) xcv
									.getParent()).getChildAt(0)).getChildAt(0))
									.setTextColor(Color.GREEN);
							dbAdapter.markHillClimbed(id, new Date(), "");
						} else {
							((TextView) ((ViewGroup) ((ViewGroup) xcv
									.getParent()).getChildAt(0)).getChildAt(0))
									.setTextColor(Color.WHITE);
							dbAdapter.markHillNotClimbed(id);
						}
						dbAdapter.close();
						HillDetailFragment fragment = (HillDetailFragment) getActivity()
								.getSupportFragmentManager().findFragmentById(
										R.id.hill_detail_fragment);

						if (fragment != null && fragment.isInLayout()) {
							hillSelectedListener.onHillSelected(id);
						}
					}

				});

				return true;
			}
			}

			return false;
		}

	}

	public interface OnHillSelectedListener {
		public void onHillSelected(int rowid);
	}

	private static class UpdateHillsTaskLoader extends AsyncTaskLoader<Cursor> {
		private Cursor data;
		private String where = null;
		private String orderBy;
		private String hilltype;
		private String countryClause;
		private int filterHills;
		private HillDbAdapter dbAdapter;

		public UpdateHillsTaskLoader(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		public UpdateHillsTaskLoader(Context context, String hilltype,
				String countryClause, String where, String orderBy,
				int filterHills, HillDbAdapter dbAdapter) {
			super(context);
			this.where = where;
			this.orderBy = orderBy;
			this.hilltype = hilltype;
			this.countryClause = countryClause;
			this.filterHills = filterHills;
			this.dbAdapter = dbAdapter;

		}

		@Override
		public Cursor loadInBackground() {
			dbAdapter.open();

			Cursor result = dbAdapter.getHillGroup(hilltype, countryClause,
					where, orderBy, filterHills);
			// dbAdapter.close();
			data=result;
			return result;
		}

		@Override
		protected void onStartLoading() {
			if (data != null) {
				deliverResult(data);
			}

			if (takeContentChanged() || data == null) {
				forceLoad();
			}
		}

		@Override
		protected void onStopLoading() {
			// TODO Auto-generated method stub
			super.onStopLoading();
		}

	}

	private static class HillsClimbedTaskLoader extends AsyncTaskLoader<Cursor> {
		private Cursor data;
		private String where = null;
		private String orderBy;
		private String hilltype;
		private String countryClause;
		private int filterHills;
		private HillDbAdapter dbAdapter;
		private int allMarked;
		private Handler handler;

		public HillsClimbedTaskLoader(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		public HillsClimbedTaskLoader(Context context, String hilltype,
				String countryClause, String where, String orderBy,
				int filterHills, HillDbAdapter dbAdapter, int allMarked,
				Handler handler) {
			super(context);
			this.where = where;
			this.orderBy = orderBy;
			this.hilltype = hilltype;
			this.countryClause = countryClause;
			this.filterHills = filterHills;
			this.dbAdapter = dbAdapter;
			this.allMarked = allMarked;
			this.handler = handler;
		}

		@Override
		public Cursor loadInBackground() {
			dbAdapter.open();

			Cursor result = dbAdapter.getHillGroup(hilltype, countryClause,
					where, orderBy, filterHills);
			int numberOfHills = result.getCount();
			Message m1;
			m1 = handler.obtainMessage();
			m1.arg1 = numberOfHills;
			m1.arg2 = -1;
			handler.sendMessage(m1);
			int i = 0;
			Message msg;
			result.moveToFirst();
			do {
				int row_id = result.getInt(result
						.getColumnIndex(HillDbAdapter.KEY_ID));
				if (allMarked == 1) {

					if (result.getString(result
							.getColumnIndex(HillDbAdapter.KEY_DATECLIMBED)) == null) {
						Log.d(this.toString(), "debug: marking hill " + row_id);
						dbAdapter.markHillClimbed(row_id, new Date(), "");
					}

				} else if (allMarked == 0) {
					dbAdapter.markHillNotClimbed(row_id);
				}
				i++;
				msg = handler.obtainMessage();
				msg.arg1 = i;
				handler.sendMessage(msg);

			} while (result.moveToNext());
			return null;
		}

		@Override
		protected void onStartLoading() {
			if (data != null) {
				deliverResult(data);
			}

			if (takeContentChanged() || data == null) {
				forceLoad();
			}
		}

		@Override
		protected void onStopLoading() {
			// TODO Auto-generated method stub
			super.onStopLoading();
		}

	}

	private HillDbAdapter dbAdapter;

	private String where = null;
	private String orderBy;

	private ListView myListView;
	private int firstRow;
	boolean useMetricHeights;
	private String hilltype;
	private String hilllistType;
	private int country;
	private String countryClause;
	private final DecimalFormat df3 = new DecimalFormat();
	private int filterHills;
	private final int ALL_HILLS = 0;
	private final int CLIMBED_HILLS = 1;
	private final int UNCLIMBED_HILLS = 2;
	private OnHillSelectedListener hillSelectedListener;
	private View viewer;

	private int currentRowId;

	private ProgressDialog dialog;

	private SimpleCursorAdapter cursorAdapter;

	// Define the Handler that receives messages from the thread and update the
	// progress
	final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int total = msg.arg1;
			pdialog.setProgress(total);
			if (total >= pdialog.getMax()) {
				pdialog.dismiss();
				updateList();
			}
		}
	};

	final Handler handlerHc = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int total = msg.arg1;
			if (msg.arg2 == -1) {
				dialog.setMax(msg.arg1);
			} else {
				dialog.setProgress(total);

			}
		}
	};

	private ProgressDialog pdialog;

	private String convText(TextView v, String text) {

		double dblAmt;
		dblAmt = Double.valueOf(text);
		return df3.format(dblAmt);

	}

	private void markAllHills(int allMarked) {
		Bundle b = new Bundle();
		b.putInt("allMarked", allMarked);
		getLoaderManager().restartLoader(1, b, this);
	
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		String[] qstrings;
		if (useMetricHeights) {
			qstrings = new String[] { "Hillname", "Metres", "dateClimbed",
					"_id" };
		}

		else {
			qstrings = new String[] { "Hillname", "Feet", "dateClimbed", "_id" };
		}
		cursorAdapter = new SimpleCursorAdapter(getActivity(),
				R.layout.simple_hill_item, null, qstrings, new int[] {
						R.id.name_entry, R.id.number_entry,
						R.id.check_hill_climbed, R.id.rowid },
				0);
		cursorAdapter.setViewBinder(new HillsViewBinder());
		
		myListView.setAdapter(cursorAdapter);
		myListView.setOnItemClickListener(new OnItemClickListener() {

			// @Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {

				// Extract the row id.
				int rowId = Integer.parseInt(((TextView) (((ViewGroup) view)
						.findViewById(R.id.rowid))).getText().toString());

				currentRowId = rowId;

				hillSelectedListener.onHillSelected(rowId);

			}
		});
		HillDetailFragment fragment = (HillDetailFragment) getActivity()
				.getSupportFragmentManager().findFragmentById(
						R.id.hill_detail_fragment);

		if (fragment != null && fragment.isInLayout()) {

		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		dbAdapter = new HillDbAdapter(getActivity());
		try {
			hillSelectedListener = (OnHillSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHillSelectedListener");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.hill_lists_menu, menu);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		updateFromPreferences();

		viewer = inflater.inflate(R.layout.list_hills, container, false);
		myListView = (ListView) (ViewGroup) viewer
				.findViewById(R.id.myListView);
		

		df3.isParseIntegerOnly();
		super.onCreate(savedInstanceState);

		registerForContextMenu(myListView);

		hilltype = getActivity().getIntent().getExtras().getString("hilltype");
		hilllistType = getActivity().getIntent().getExtras()
				.getString("hilllistType");
		if (!"".equals(hilllistType)) {
			getActivity().setTitle(hilllistType);
		} else
			getActivity().setTitle("Results");
		country = getActivity().getIntent().getExtras().getInt("country");
		where = getActivity().getIntent().getExtras().getString("search");

		switch (country) {
		case Main.SCOTLAND: {
			countryClause = "cast(_Section as float) between 1 and 28.9";
			break;

		}
		case Main.WALES: {
			countryClause = "cast(_Section as float) between 30 and 32.9";
			break;

		}
		case Main.ENGLAND: {
			countryClause = "cast(_Section as float) between 33 and 42.9";
			break;

		}
		case Main.OTHER_GB: {
			countryClause = "_Section='29' OR cast(_Section as float)>42.9";
			break;

		}

		}

		orderBy = "cast(" + HillDbAdapter.KEY_HEIGHTM + " as float)" + " desc";

		return viewer;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		super.onOptionsItemSelected(item);

		int index = myListView.getSelectedItemPosition();

		switch (item.getItemId()) {
		case android.R.id.home:{
            // app icon in Action Bar clicked; go home
            Intent intent = new Intent(getActivity(), Main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;

		}
		case (R.id.menu_list_alpha): {

			orderBy = HillDbAdapter.KEY_HILLNAME;
			updateList();
			return true;

		}
		case (R.id.menu_list_height): {

			orderBy = "cast(" + HillDbAdapter.KEY_HEIGHTM + " as float)"
					+ " desc";
			updateList();
			return true;

		}

		case (R.id.menu_show_climbed): {

			filterHills = CLIMBED_HILLS;
			updateList();
			return true;
		}

		case (R.id.menu_show_not_climbed): {
			filterHills = UNCLIMBED_HILLS;
			updateList();
			return true;
		}

		case (R.id.menu_show_all): {
			filterHills = ALL_HILLS;
			updateList();
			return true;
		}
		case (R.id.menu_show_map): {
			Intent intent = new Intent(getActivity(),
					ListHillsMapFragmentActivity.class);
			intent.putExtra("groupId", hilltype);
			intent.putExtra("orderBy", orderBy);
			intent.putExtra("moreWhere", where);
			intent.putExtra("countryClause", countryClause);
			intent.putExtra("title", "Map of " + hilllistType);
			intent.putExtra("selectedHill", currentRowId);
			startActivity(intent);
			return true;

		}

		case (R.id.menu_search): {
			AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

			alert.setTitle("Search");

			// Set an EditText view to get user input
			final EditText searchText = new EditText(getActivity());

			alert.setView(searchText);

			alert.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							String value = searchText.getText().toString();

							if (!"".equals(value))
								where = "hillname like "
										+ DatabaseUtils.sqlEscapeString("%"
												+ value + "%");
							else
								where = null;

							updateList();

						}
					});

			alert.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

						}
					});

			AlertDialog search = alert.create();
			search.show();
			return true;
		}
		case (R.id.menu_all_climbed): {
			markAllHills(1);
			return true;
		}

		case (R.id.menu_none_climbed): {
			markAllHills(0);
			return true;
		}
		}
		return false;

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		updateList();
	}

	private void refresh(final Cursor result) {

		cursorAdapter.swapCursor(result);
		// populate detail view with first row

		if (result.getCount() != 0) {
			
			result.moveToPosition(0);
			firstRow = result.getInt(result.getColumnIndexOrThrow("_id"));

			HillDetailFragment fragment = (HillDetailFragment) getActivity()
					.getSupportFragmentManager().findFragmentById(
							R.id.hill_detail_fragment);

			if (fragment != null && fragment.isInLayout()) {

				// populate detail view with first row
				currentRowId = firstRow;
				hillSelectedListener.onHillSelected(firstRow);
			}
			// hillSelectedListener.onHillSelected(firstRow);

		}
		String updateTitle=hilllistType + " - " + String.valueOf(result.getCount())
		+ " hills found";
		getActivity().setTitle(updateTitle);
		//reset climbed count
		

	}

	private void updateFromPreferences() {
		Context context = getActivity().getApplicationContext();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		useMetricHeights = prefs.getBoolean(
				PreferencesActivity.PREF_METRIC_HEIGHTS, false);
	}

	private void updateList() {
		getLoaderManager().restartLoader(0, null, this);

	}

	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		dialog = new ProgressDialog(getActivity());
		if (id == 1) {
			int allMarked = args.getInt("allMarked");
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			this.dialog.setMessage("Marking Hills Climbed");
			this.dialog.show();
			return new HillsClimbedTaskLoader(getActivity(), hilltype,
					countryClause, where, orderBy, filterHills, dbAdapter,
					allMarked, handlerHc);
		} else {
			this.dialog.setMessage("Getting Hills...");
			this.dialog.show();
			return new UpdateHillsTaskLoader(getActivity(), hilltype,
					countryClause, where, orderBy, filterHills, dbAdapter);
		}

	}

	public void onLoaderReset(Loader<Cursor> loader) {

		cursorAdapter.swapCursor(null);

	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (this.dialog.isShowing()) {
			this.dialog.dismiss();
		}
		if (loader.getId() == 0)
			refresh(data);
		else {
			getLoaderManager().getLoader(0).reset();
			updateList();
		}

	}

}
