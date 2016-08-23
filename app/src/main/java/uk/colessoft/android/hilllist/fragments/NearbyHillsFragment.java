package uk.colessoft.android.hilllist.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.activities.Main;
import uk.colessoft.android.hilllist.activities.NearbyHillsMapFragmentActivity;
import uk.colessoft.android.hilllist.activities.PreferencesActivity;
import uk.colessoft.android.hilllist.database.HillDbAdapter;
import uk.colessoft.android.hilllist.utility.DistanceCalculator;

public class NearbyHillsFragment extends Fragment implements
		LoaderManager.LoaderCallbacks<ArrayList<Map<String,?>>> {

	public interface OnHillSelectedListener {
		public void onHillSelected(int rowid);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		dbAdapter = new HillDbAdapter(getActivity());
	}

	private int firstRow;
	private HillDbAdapter dbAdapter;
	private View viewer;
	private ListView hillListView;
	private LocationManager lm;
	private MyLocationListener locationListener;
	private ArrayList<Map<String,?>> nearbyHills;
	double lat1;
	double lon1;
	private double nearRadius = 16;
	private final DecimalFormat df2 = new DecimalFormat("#,###,###,##0.0");
	private final DecimalFormat df1 = new DecimalFormat("#,###,###,##0");
	private final DecimalFormat df3 = new DecimalFormat();
	MenuItem dist;
	private boolean useMetricHeights;
	private boolean useMetricDistances;
	private String orderBy = "distance";
	private ProgressDialog dialog;
	private final int ALL_HILLS = 0;
	private final int CLIMBED_HILLS = 1;
	private final int UNCLIMBED_HILLS = 2;
	private int filterHills = ALL_HILLS;
	private boolean searched = false;
	private boolean locationSet = false;
	private ArrayList<Map<String,?>> backedUpHills;
	public OnHillSelectedListener hillSelectedListener;
	private OnLocationFoundListener locationFoundListener;
	private SimpleAdapter nearbyHillsAdapter;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			hillSelectedListener = (OnHillSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHillSelectedListener");
		}
		try {
			locationFoundListener = (OnLocationFoundListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement LocationFoundListener");
		}

	}

	@Override
	public void onPause() {
		lm.removeUpdates(locationListener);

		super.onPause();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		inflater.inflate(R.menu.hill_lists_menu, menu);
		dist = menu.findItem(R.id.menu_by_distance);
		// dist=(MenuItem)findViewById(R.id.menu_by_distance);
		dist.setVisible(true);
		menu.findItem(R.id.menu_set_range).setVisible(true);

	}

	@Override
	public void onDestroy() {

		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		super.onOptionsItemSelected(item);

		int index = hillListView.getSelectedItemPosition();

		switch (item.getItemId()) {
		case android.R.id.home: {
			// app icon in Action Bar clicked; go home
			Intent intent = new Intent(getActivity(), Main.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;

		}
		case (R.id.menu_range_10miles): {
			nearRadius = 16;
			if (useMetricDistances) {
				getActivity().setTitle(
						"Hills within " + df1.format(nearRadius) + "km");
			} else
				getActivity().setTitle(
						"Hills within " + df1.format(nearRadius / 1.601)
								+ " miles");
			getNearHills();
			updateList();
			return true;
		}

		case (R.id.menu_range_25miles): {
			nearRadius = 40;
			if (useMetricDistances) {
				getActivity().setTitle(
						"Hills within " + df1.format(nearRadius) + "km");
			} else
				getActivity().setTitle(
						"Hills within " + df1.format(nearRadius / 1.601)
								+ " miles");
			getNearHills();
			updateList();
			return true;

		}

		case (R.id.menu_range_50miles): {
			nearRadius = 80;
			if (useMetricDistances) {
				getActivity().setTitle(
						"Hills within " + df1.format(nearRadius) + "km");
			} else
				getActivity().setTitle(
						"Hills within " + df1.format(nearRadius / 1.601)
								+ " miles");
			getNearHills();

			updateList();
			return true;

		}

		case (R.id.menu_show_climbed): {

			filterHills = CLIMBED_HILLS;
			return true;
		}

		case (R.id.menu_show_not_climbed): {
			filterHills = UNCLIMBED_HILLS;
			return true;
		}

		case (R.id.menu_show_all): {
			filterHills = ALL_HILLS;
			return true;
		}

		case (R.id.menu_list_alpha): {

			orderBy = dbAdapter.KEY_HILLNAME;
			if (nearbyHills != null && !nearbyHills.isEmpty()) {
				Collections.sort(nearbyHills, new HillHashMapAlphaComparator());
				updateList();
			}
			return true;

		}
		case (R.id.menu_list_height): {

			orderBy = dbAdapter.KEY_HEIGHTM;
			if (nearbyHills != null && !nearbyHills.isEmpty()) {
				Collections
						.sort(nearbyHills, new HillHashMapHeightComparator());
				updateList();
			}
			return true;

		}

		case (R.id.menu_by_distance): {

			orderBy = "distance";
			if (nearbyHills != null && !nearbyHills.isEmpty()) {
				Collections.sort(nearbyHills,
						new HillHashMapDistanceComparator());
				updateList();
			}
			return true;

		}
		case (R.id.menu_show_map): {
			Intent intent = new Intent(getActivity(),
					NearbyHillsMapFragmentActivity.class);
			String[] rowIds;
			int sindex = 0;
			if (nearbyHills!=null) {
				rowIds = new String[nearbyHills.size()];
				for (Map hs : nearbyHills) {
					String srow = ((Integer) (hs.get("rowid"))).toString();
					rowIds[sindex] = srow;
					sindex++;
				}
			}else{
				rowIds = new String[0];
			}
			



			intent.putExtra("rowids", rowIds);
			intent.putExtra("title",
					"Hills within " + df2.format(nearRadius / 1.601)
							+ " miles (" + nearRadius + "km)");

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
							String value = searchText.getText().toString()
									.toLowerCase();
							// go back to original list if we've already
							// whittled down through search.
							if (searched) {
								nearbyHills = backedUpHills;
								searched = false;
							} else {
								backedUpHills = (ArrayList<Map<String,?>>) nearbyHills
										.clone();
								searched = true;
							}
							ArrayList<Map<String,?>> newList = new ArrayList();
							for (Map hs : nearbyHills) {
								String hillname = (String) (hs.get("hillname"));
								if (hillname.toLowerCase().contains(value)) {
									newList.add(hs);
								}
							}
							nearbyHills = newList;
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
		}
		return false;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		viewer = inflater.inflate(R.layout.list_hills, container, false);
		hillListView = (ListView) (ViewGroup) viewer
				.findViewById(R.id.myListView);

		return viewer;
	}

	private void getNearHills() {
		if(!dbAdapter.isOpen())
			dbAdapter.open();
		Cursor hillsCursor = dbAdapter.getAllHillsCursor();

		nearbyHills = new ArrayList();
		Drawable marker = getResources().getDrawable(R.drawable.yellow_hill);

		marker.setBounds(0, 0, marker.getIntrinsicWidth(),
				marker.getIntrinsicHeight());

		// iterate over cursor and get hill positions
		// Make sure there is at least one row.
		if (hillsCursor.moveToFirst()) {
			// Iterate over each cursor.
			do {
				Double lat = hillsCursor.getDouble(hillsCursor
						.getColumnIndex(HillDbAdapter.KEY_LATITUDE)) * 1E6;
				Double lng = hillsCursor.getDouble(hillsCursor
						.getColumnIndex(HillDbAdapter.KEY_LONGITUDE)) * 1E6;

				double distanceKm = DistanceCalculator.CalculationByDistance(
						lat1, lat / 1E6, lon1, lng / 1E6);
				int row_id = hillsCursor.getInt(hillsCursor
						.getColumnIndex(HillDbAdapter.KEY_ID));
				if (distanceKm < nearRadius) {

					String hillname = hillsCursor.getString(hillsCursor
							.getColumnIndex(HillDbAdapter.KEY_HILLNAME));
					float height;
					if (useMetricHeights) {
						height = hillsCursor.getFloat(hillsCursor
								.getColumnIndex(HillDbAdapter.KEY_HEIGHTM));
					} else {
						height = hillsCursor.getFloat(hillsCursor
								.getColumnIndex(HillDbAdapter.KEY_HEIGHTF));

					}
					HashMap<String,Object> hillExtract = new HashMap();
					hillExtract.put("hillname", hillname);
					hillExtract.put("height", height);
					hillExtract.put("rowid", row_id);

					if (useMetricDistances) {
						hillExtract.put("distance",
								new Double(df2.format(distanceKm))
										.doubleValue());
					} else {
						hillExtract.put("distance",
								new Double(df2.format(distanceKm / 1.601))
										.doubleValue());
					}
					nearbyHills.add(hillExtract);

				}

			} while (hillsCursor.moveToNext());
			if (orderBy.equals(HillDbAdapter.KEY_HEIGHTM)) {
				Collections
						.sort(nearbyHills, new HillHashMapHeightComparator());
			} else if (orderBy.equals(HillDbAdapter.KEY_HILLNAME)) {
				Collections.sort(nearbyHills, new HillHashMapAlphaComparator());
			} else if (orderBy.equals("distance")) {
				Collections.sort(nearbyHills,
						new HillHashMapDistanceComparator());
			}
		}
//		dbAdapter.close();

	}

	final Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			locationProgress.setVisibility(View.GONE);

		}
	};
	private View locationProgress;

	@Override
	public void onResume() {

		super.onResume();
		//dbAdapter = new HillDbAdapter(getActivity());
		 if(!dbAdapter.isOpen())
			 dbAdapter.open();

		lm = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);
		locationListener = new MyLocationListener();
		updateFromPreferences();
		locationProgress = viewer.findViewById(R.id.location_progress);
		locationProgress.setVisibility(View.VISIBLE);
		locationSet = false;
		LocationProvider lp1 = lm.getProvider(LocationManager.GPS_PROVIDER);
		LocationProvider lp2 = lm.getProvider(LocationManager.NETWORK_PROVIDER);

		if (lp1 != null) {
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 120000, // ms
					// since
					// last
					// call
					100, // m movement
					locationListener);
		}
		if (lp2 != null) {
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 120000, // ms
					// since
					// last
					// call
					100, // m movement
					locationListener);
		}

		Runnable showWaitDialog = new Runnable() {

			public void run() {

				while (!locationSet) {
					// Wait for first GPS Fix (do nothing until loc != null)
				}

				Message m1;
				m1 = handler.obtainMessage();
				m1.arg1 = 0;

				handler.sendMessage(m1);

			}

		};
		// locationFoundListener.showDialog();

		Thread t = new Thread(showWaitDialog);
		t.start();

		Location lastLocation = lm
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (lastLocation != null) {
			lat1 = lastLocation.getLatitude();
			lon1 = lastLocation.getLongitude();
			// locationSet = true;

			startLoader();
		}

	}

	private void updateList() {
		nearbyHillsAdapter = new SimpleAdapter(getActivity(),
				(List<? extends Map<String, ?>>) nearbyHills,
				R.layout.nearby_hill_item, new String[] { "hillname",
						"distance", "height", }, new int[] {
						R.id.hillname_entry, R.id.distance_entry,
						R.id.height_entry }) {

			@Override
			public void setViewText(TextView v, String text) {
				switch (v.getId()) {
				case R.id.distance_entry: {
					if (useMetricDistances) {
						text = convText(v, text, df2) + " km";
					} else {
						text = convText(v, text, df2) + " miles";
					}
					break;
				}
				case R.id.height_entry: {
					if (useMetricHeights) {
						text = convText(v, text, df3) + "m";
					} else
						text = convText(v, text, df3) + "ft";
					break;
				}
				}
				super.setViewText(v, text);
			}

		};
		hillListView.setAdapter(nearbyHillsAdapter);
		hillListView.setOnItemClickListener(new OnItemClickListener() {

			// @Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				HashMap entry = (HashMap) nearbyHills.get(pos);
				int rowId = (Integer) entry.get("rowid");

				hillSelectedListener.onHillSelected(rowId);

				// finish();
			}
		});

		Fragment fragment2 = getActivity().getSupportFragmentManager()
				.findFragmentById(R.id.hill_detail_fragment);

		HillDetailFragment fragment = (HillDetailFragment) getActivity()
				.getSupportFragmentManager().findFragmentById(
						R.id.hill_detail_fragment);

		if (fragment != null && fragment.isInLayout()) {
			if (nearbyHills != null && nearbyHills.size() > 0) {
				hillSelectedListener.onHillSelected((Integer) (nearbyHills
						.get(0)).get("rowid"));
			}
		}
	}

	public interface OnLocationFoundListener {
		public void locationFound();

		public void showDialog();
	}

	public void startLoader() {
		getLoaderManager().restartLoader(0, null, this);
	}

	public class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location location) {
			lat1 = location.getLatitude();
			lon1 = location.getLongitude();
			locationSet = true;

			startLoader();

		}

		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

	}

	private String convText(TextView v, String text, DecimalFormat df) {

		double dblAmt;
		dblAmt = Double.valueOf(text);
		return df.format(dblAmt);

	}

	private static class HillHashMapHeightComparator implements Comparator {

		public int compare(Object hill1, Object hill2) {

			Float height1 = (Float) ((HashMap) hill1).get("height");
			Float height2 = (Float) ((HashMap) hill2).get("height");
			return (height2.compareTo(height1));
		}

	}

	private static class HillHashMapDistanceComparator implements Comparator {

		public int compare(Object hill1, Object hill2) {

			Double distance1 = (Double) ((HashMap) hill1).get("distance");
			Double distance2 = (Double) ((HashMap) hill2).get("distance");
			return (distance1.compareTo(distance2));
		}

	}

	private static class HillHashMapAlphaComparator implements Comparator {

		public int compare(Object hill1, Object hill2) {
			String hillname1 = (String) ((HashMap) hill1).get("hillname");
			String hillname2 = (String) ((HashMap) hill2).get("hillname");
			return (hillname1.compareTo(hillname2));

		}

	}

	private void updateFromPreferences() {
		Context context = getActivity().getApplicationContext();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		useMetricHeights = prefs.getBoolean(
				PreferencesActivity.PREF_METRIC_HEIGHTS, false);
		useMetricDistances = prefs.getBoolean(
				PreferencesActivity.PREF_METRIC_DISTANCES, false);
	}

	private static class UpdateHillsTaskLoader extends
			AsyncTaskLoader<ArrayList<Map<String,?>>> {
		private ArrayList<Map<String,?>> nearbyHills;
		private String where = null;
		private String orderBy;
		private String hilltype;
		private String countryClause;
		private int filterHills;
		private HillDbAdapter dbAdapter;
		private double lat1;
		private double lon1;
		private double nearRadius;
		private boolean useMetricHeights;
		private boolean useMetricDistances;
		private DecimalFormat df2;

		public UpdateHillsTaskLoader(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		public UpdateHillsTaskLoader(Context context, String orderBy,
				int filterHills, HillDbAdapter dbAdapter,
				boolean useMetricDistances, boolean useMetricHeights,
				double lat1, double lon1, double nearRadius, DecimalFormat df2) {
			super(context);

			this.orderBy = orderBy;
			this.filterHills = filterHills;
			this.dbAdapter = dbAdapter;
			this.lat1 = lat1;
			this.lon1 = lon1;
			this.nearRadius = nearRadius;
			this.useMetricHeights = useMetricHeights;
			this.useMetricDistances = useMetricDistances;
			this.df2 = df2;

		}

		@Override
		public ArrayList<Map<String,?>> loadInBackground() {
			if(!dbAdapter.isOpen())
				dbAdapter.open();
			Cursor hillsCursor = dbAdapter.getAllHillsCursor();

			nearbyHills = new ArrayList();
			/*
			 * Drawable marker =
			 * getResources().getDrawable(R.drawable.yellow_hill);
			 * 
			 * marker.setBounds(0, 0, marker.getIntrinsicWidth(),
			 * marker.getIntrinsicHeight());
			 */

			// iterate over cursor and get hill positions
			// Make sure there is at least one row.
			if (hillsCursor.moveToFirst()) {
				// Iterate over each cursor.
				do {
					Double lat = hillsCursor.getDouble(hillsCursor
							.getColumnIndex(HillDbAdapter.KEY_LATITUDE)) * 1E6;
					Double lng = hillsCursor.getDouble(hillsCursor
							.getColumnIndex(HillDbAdapter.KEY_LONGITUDE)) * 1E6;

					double distanceKm = DistanceCalculator
							.CalculationByDistance(lat1, lat / 1E6, lon1,
									lng / 1E6);
					int row_id = hillsCursor.getInt(hillsCursor
							.getColumnIndex(HillDbAdapter.KEY_ID));
					if (distanceKm < nearRadius) {

						String hillname = hillsCursor.getString(hillsCursor
								.getColumnIndex(HillDbAdapter.KEY_HILLNAME));
						float height;
						if (useMetricHeights) {
							height = hillsCursor.getFloat(hillsCursor
									.getColumnIndex(HillDbAdapter.KEY_HEIGHTM));
						} else {
							height = hillsCursor.getFloat(hillsCursor
									.getColumnIndex(HillDbAdapter.KEY_HEIGHTF));

						}
						HashMap hillExtract = new HashMap();
						hillExtract.put("hillname", hillname);
						hillExtract.put("height", height);
						hillExtract.put("rowid", row_id);

						if (useMetricDistances) {
							hillExtract.put("distance",
									new Double(df2.format(distanceKm))
											.doubleValue());
						} else {
							hillExtract.put("distance",
									new Double(df2.format(distanceKm / 1.601))
											.doubleValue());
						}
						nearbyHills.add(hillExtract);

					}

				} while (hillsCursor.moveToNext());
				if (orderBy.equals(HillDbAdapter.KEY_HEIGHTM)) {
					Collections.sort(nearbyHills,
							new HillHashMapHeightComparator());
				} else if (orderBy.equals(HillDbAdapter.KEY_HILLNAME)) {
					Collections.sort(nearbyHills,
							new HillHashMapAlphaComparator());
				} else if (orderBy.equals("distance")) {
					Collections.sort(nearbyHills,
							new HillHashMapDistanceComparator());
				}
			}
//			dbAdapter.close();
			return nearbyHills;
		}

		@Override
		protected void onStartLoading() {
			if (nearbyHills != null) {
				deliverResult(nearbyHills);
			}

			if (takeContentChanged() || nearbyHills == null) {
				forceLoad();
			}
		}

		@Override
		protected void onStopLoading() {
			// TODO Auto-generated method stub
			super.onStopLoading();
		}

	}

	public Loader<ArrayList<Map<String,?>>> onCreateLoader(int id, Bundle args) {
		/*
		 * dialog = new ProgressDialog(getActivity());
		 * 
		 * this.dialog.setMessage("Getting Hills..."); this.dialog.show();
		 */
		return new UpdateHillsTaskLoader(getActivity(), orderBy, filterHills,
				dbAdapter, useMetricDistances, useMetricHeights, lat1, lon1,
				nearRadius, df2);

	}

	public void onLoaderReset(Loader<ArrayList<Map<String,?>>> loader) {

		// updateList();

	}

	public void onLoadFinished(Loader<ArrayList<Map<String,?>>> loader,
			ArrayList<Map<String,?>> data) {
		refresh(data);

	}

	private void refresh(ArrayList<Map<String,?>> data) {
		nearbyHills = data;
		updateList();

	}

}
