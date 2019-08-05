package uk.colessoft.android.hilllist.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cursoradapter.widget.SimpleCursorAdapter.ViewBinder;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.ui.activity.ListHillsMapFragmentActivity;
import uk.colessoft.android.hilllist.ui.activity.Main;
import uk.colessoft.android.hilllist.ui.activity.PreferencesActivity;
import uk.colessoft.android.hilllist.ui.adapter.HillDetailListAdapter;
import uk.colessoft.android.hilllist.dao.HillsOrder;
import uk.colessoft.android.hilllist.database.BritishHillsDatasource;
import uk.colessoft.android.hilllist.domain.entity.Bagging;
import uk.colessoft.android.hilllist.domain.HillDetail;
import uk.colessoft.android.hilllist.ui.viewmodel.HillDetailViewModel;

import static uk.colessoft.android.hilllist.database.HillsTables.KEY_HILLNAME;

public class DisplayHillListFragment extends DaggerFragment implements
		LoaderManager.LoaderCallbacks<Cursor> {
	
	private int climbedCount=0;

	private List<HillDetail> hills;

	@Inject
	BritishHillsDatasource dbAdapter;

	private String where = null;
	private String orderBy;

	private RecyclerView myListView;
	private boolean useMetricHeights;
	private String hilltype;
	private String hilllistType;
	private String countryClause;
	private final DecimalFormat df3 = new DecimalFormat();
	private int filterHills;
	private OnHillSelectedListener hillSelectedListener;
	private HillDetailViewModel viewModel;

	private int currentRowId;

	private ProgressDialog dialog;

	private RecyclerView.Adapter<HillDetailListAdapter.HillDetailViewHolder> cursorAdapter;

	private RecyclerView.LayoutManager layoutManager;


	private ProgressDialog pdialog;


	private class HillsViewBinder implements ViewBinder {

		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			

			TextView tv = (TextView) view;

			String vtext = cursor.getString(columnIndex);
			switch (tv.getId()) {

			case R.id.check_hill_climbed: {
				CheckBox ctv = (CheckBox) tv;
				final int id = cursor.getInt(cursor
						.getColumnIndex("h_id"));
				if (cursor.getString(cursor
						.getColumnIndex(Bagging.KEY_DATECLIMBED)) != null) {

					ctv.setChecked(true);

				} else
					ctv.setChecked(false);
				ctv.setOnClickListener(null);
				ctv.setOnClickListener(v -> {
                   // dbAdapter.open();
                    CheckBox xcv = (CheckBox) v;
                    if (xcv.isChecked()) {
                        ((RelativeLayout)xcv.getParent()).setBackgroundColor(getResources().getColor(R.color.paler_light_green));
                        dbAdapter.markHillClimbed(id, new Date(), "");
                    } else {
						((RelativeLayout)xcv.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        dbAdapter.markHillNotClimbed(id);
                    }
                    //dbAdapter.close();
                    HillDetailFragment fragment = (HillDetailFragment) getActivity()
                            .getSupportFragmentManager().findFragmentById(
                                    R.id.hill_detail_fragment);

//                    if (fragment != null && fragment.isInLayout()) {
//                        hillSelectedListener.onHillSelected(id);
//                    }
                });

				return true;
			}
			}

			return false;
		}

	}

	public interface OnHillSelectedListener {
		void onHillSelected(int rowid);
	}

	private static class HillsClimbedTaskLoader extends AsyncTaskLoader<Cursor> {
		private Cursor data;
		private String where = null;
		private String orderBy;
		private String hilltype;
		private String countryClause;
		private int filterHills;
		private BritishHillsDatasource dbAdapter;
		private int allMarked;
		private Handler handler;

		public HillsClimbedTaskLoader(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		public HillsClimbedTaskLoader(Context context, String hilltype,
                                      String countryClause, String where, String orderBy,
                                      int filterHills, BritishHillsDatasource dbAdapter, int allMarked) {
			super(context);
			this.where = where;
			this.orderBy = orderBy;
			this.hilltype = hilltype;
			this.countryClause = countryClause;
			this.filterHills = filterHills;
			this.dbAdapter = dbAdapter;
			this.allMarked = allMarked;
		}

		@Override
		public Cursor loadInBackground() {
			//dbAdapter.open();

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
						.getColumnIndex("h_id"));
				if (allMarked == 1) {

					if (result.getString(result
							.getColumnIndex(Bagging.KEY_DATECLIMBED)) == null) {
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

	}




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
		super.onActivityCreated(savedInstanceState);
		myListView.setAdapter(cursorAdapter);

		HillDetailFragment fragment = (HillDetailFragment) getActivity()
				.getSupportFragmentManager().findFragmentById(
						R.id.hill_detail_fragment);

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			hillSelectedListener = (OnHillSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHillSelectedListener");
		}
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		viewModel = ViewModelProviders.of(getActivity()).get(HillDetailViewModel.class);

		final Observer<List<HillDetail>> nameObserver = new Observer<List<HillDetail>>() {
			@Override
			public void onChanged(@Nullable final List<HillDetail> newHills) {
				cursorAdapter = new HillDetailListAdapter(newHills,hillSelectedListener);
				myListView.setAdapter(cursorAdapter);
				hills = newHills;
				cursorAdapter.notifyDataSetChanged();
				Log.d("ChangedData","Hills returned:"+newHills.size());
				String updateTitle=hilllistType + " - " + String.valueOf(newHills.size())
						+ " hills found";
				getActivity().setTitle(updateTitle);
			}
		};

		viewModel.getHills().observe(getActivity(),nameObserver);

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

		View viewer = inflater.inflate(R.layout.list_hills, container, false);
		myListView = viewer
				.findViewById(R.id.myListView);
		myListView.setLayoutManager(new LinearLayoutManager(getActivity()));
		df3.isParseIntegerOnly();
		super.onCreate(savedInstanceState);

		registerForContextMenu(myListView);

		hilllistType = getActivity().getIntent().getExtras()
				.getString("hilllistType");
		if (!"".equals(hilllistType)) {
			getActivity().setTitle(hilllistType);
		} else
			getActivity().setTitle("Results");

		return viewer;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case android.R.id.home:{
            // app icon in Action Bar clicked; go home
            Intent intent = new Intent(getActivity(), Main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;

		}
		case (R.id.menu_list_alpha): {
			viewModel.orderHills(HillsOrder.NAME_ASC);
			return true;
		}

		case (R.id.menu_list_height): {
			viewModel.orderHills(HillsOrder.HEIGHT_DESC);
			return true;
		}

		case (R.id.menu_show_climbed): {

			int CLIMBED_HILLS = 1;
			filterHills = CLIMBED_HILLS;
			updateList();
			return true;
		}

		case (R.id.menu_show_not_climbed): {
			int UNCLIMBED_HILLS = 2;
			filterHills = UNCLIMBED_HILLS;
			updateList();
			return true;
		}

		case (R.id.menu_show_all): {
			int ALL_HILLS = 0;
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
					(dialog12, whichButton) -> {
                        String value = searchText.getText().toString();

                        if (!"".equals(value))
                            where = KEY_HILLNAME+" like "
                                    + DatabaseUtils.sqlEscapeString("%"
                                            + value + "%");
                        else
                            where = null;

                        updateList();

                    });

			alert.setNegativeButton("Cancel",
					(dialog1, whichButton) -> {

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


	private void refresh(final Cursor result) {

		//cursorAdapter.swapCursor(result);
		// populate detail view with first row

		if (result.getCount() != 0) {
			
			result.moveToPosition(0);
			int firstRow = result.getInt(result.getColumnIndexOrThrow("hill_id"));

			HillDetailFragment fragment = (HillDetailFragment) getActivity()
					.getSupportFragmentManager().findFragmentById(
							R.id.hill_detail_fragment);

			if (fragment != null && fragment.isInLayout()) {

				// populate detail view with first row
				currentRowId = firstRow;
				//hillSelectedListener.onHillSelected(firstRow);
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
			int allMarked = args.getInt("allMarked");
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			this.dialog.setMessage("Marking Hills Climbed");
			this.dialog.show();
			return new HillsClimbedTaskLoader(getActivity(), hilltype,
					countryClause, where, orderBy, filterHills, dbAdapter,
					allMarked);


	}

	public void onLoaderReset(Loader<Cursor> loader) {

		//cursorAdapter.swapCursor(null);

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
