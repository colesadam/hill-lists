package uk.colessoft.android.hilllist.fragments;

import java.util.ArrayList;
import java.util.List;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.activities.AdamsSpecialInterface;
import uk.colessoft.android.hilllist.database.HillDbAdapter;
import uk.colessoft.android.hilllist.fragments.DisplayHillListFragment.OnHillSelectedListener;
import uk.colessoft.android.hilllist.model.TinyHill;
import uk.colessoft.android.hilllist.overlays.BalloonManyHillsOverlay;
import uk.colessoft.android.hilllist.overlays.ManyHillsOverlay;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;

public class ListHillsMapFragment extends Fragment implements
LoaderManager.LoaderCallbacks<Cursor>{

	private HillDbAdapter dbAdapter;
	private MapView mapView;
	private MapController mapController;
	private BalloonManyHillsOverlay manyHillsOverlay;
	private ManyHillsOverlay cmanyHillsOverlay;
	private double lat;
	private double lng;
	private OnHillSelectedListener hillSelectedListener;
	private View viewer;
	private ProgressDialog dialog;
	private String hillType;
	private String where;
	private String orderBy;
	private String countryClause;
	private int passedRowId;
	private int selectedIndex = 0;
	private Drawable marker;
	private Drawable cmarker;
	private ArrayList<OverlayItem> items;
	private List<Overlay> overlays;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		viewer =  inflater.inflate(R.layout.many_hills_map, container, false);
		//return ((ListHillsMapFragmentActivity)getActivity()).fView;

		return viewer;
		

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		final MapView mapView=((AdamsSpecialInterface)getActivity()).getMapView();
		((ViewGroup)viewer).addView(mapView,0);
		
		
		/*test*/
		
		dbAdapter = new HillDbAdapter(getActivity());
		dbAdapter.open();

		String title;
		

		hillType = getActivity().getIntent().getExtras().getString("groupId");
		where = getActivity().getIntent().getExtras().getString("moreWhere");
		orderBy = getActivity().getIntent().getExtras().getString("orderBy");
		countryClause = getActivity().getIntent().getExtras().getString("countryClause");
		title = getActivity().getIntent().getExtras().getString("title");
		passedRowId=getActivity().getIntent().getExtras().getInt("selectedHill");
		getActivity().setTitle(title);

		
		mapView.setSatellite(true);

		final ToggleButton mapButton = (ToggleButton) ((ViewGroup)viewer).findViewById(R.id.satellite_button);
		mapButton.setChecked(true);
		mapButton.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				if (mapButton.isChecked()) {
					mapView.setSatellite(true);
				} else {
					mapView.setSatellite(false);
				}

			}

		});
		
		mapController = mapView.getController();
		mapController.setZoom(8);
		mapController.setCenter(new GeoPoint(new Double(lat).intValue(),
				new Double(lng).intValue()));
		mapView.setBuiltInZoomControls(true);

		marker = getResources().getDrawable(R.drawable.yellow_hill);
		cmarker = getResources().getDrawable(R.drawable.green_hill);
		cmarker.setBounds(0 - (cmarker.getIntrinsicWidth() / 2),
				0 - (cmarker.getIntrinsicHeight() / 2),
				cmarker.getIntrinsicWidth() / 2,
				(cmarker.getIntrinsicHeight() / 2));
		


		// Add the Overlay
		manyHillsOverlay = new BalloonManyHillsOverlay(marker, mapView);
		items = new ArrayList<OverlayItem>();
		

		overlays = mapView.getOverlays();
		
		getLoaderManager().restartLoader(0, null, this);
		
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

		@Override
		public Cursor loadInBackground() {
			dbAdapter.open();

			Cursor result = dbAdapter.getHillGroup(hilltype, countryClause,
					where, orderBy, filterHills);
			// dbAdapter.close();
			data=result;
			return result;
		}

	}

	private void update(Cursor hillsCursor){
		//Cursor hillsCursor = dbAdapter.getHillGroup(hillType, countryClause,
			//	where, orderBy, 0);

		
		List<TinyHill> hillPoints = new ArrayList<TinyHill>();
		// iterate over cursor and get hill positions
		// Make sure there is at least one row.
		if (hillsCursor.moveToFirst()) {
			// Iterate over each cursor.
			do {
				lat = hillsCursor.getDouble(hillsCursor
						.getColumnIndex(HillDbAdapter.KEY_LATITUDE)) * 1E6;
				lng = hillsCursor.getDouble(hillsCursor
						.getColumnIndex(HillDbAdapter.KEY_LONGITUDE)) * 1E6;
				int row_id = hillsCursor.getInt(hillsCursor
						.getColumnIndex(HillDbAdapter.KEY_ID));
				if(passedRowId==0)passedRowId=row_id;
				String hillname = hillsCursor.getString(hillsCursor
						.getColumnIndex(HillDbAdapter.KEY_HILLNAME));

				TinyHill tinyHill = new TinyHill();
				tinyHill._id = row_id;
				
				if(passedRowId==row_id){
					selectedIndex=hillPoints.size();
				}
				
				tinyHill.setHillname(hillname);
				tinyHill.setLatitude(lat);
				tinyHill.setLongitude(lng);
				if (hillsCursor.getString(hillsCursor
						.getColumnIndex(HillDbAdapter.KEY_DATECLIMBED)) != null) {
					tinyHill.setClimbed(true);
				} else {
					tinyHill.setClimbed(false);
				}
				hillPoints.add(tinyHill);

			} while (hillsCursor.moveToNext());
		}

		HillDetailFragment fragment = (HillDetailFragment) getActivity().getSupportFragmentManager()
				.findFragmentById(R.id.hill_detail_fragment);

		//If we are in fragment layer update hill details in detail fragment for current hillid
		if (fragment != null && fragment.isInLayout()) {
				hillSelectedListener.onHillSelected(passedRowId);
		}



		for (TinyHill gc : hillPoints) {
			OverlayItem oi = new OverlayItem(new GeoPoint(gc.getLatitude()
					.intValue(), gc.getLongitude().intValue()),
					gc.getHillname(), String.valueOf(gc._id));
			if (gc.isClimbed())
				oi.setMarker(cmarker);
			else
				oi.setMarker(marker);
			items.add(oi);
		}
		manyHillsOverlay
				.setHillPoints(items, getActivity(),selectedIndex);

		int latSpan = manyHillsOverlay.getLatSpanE6();
		int lonSpan = manyHillsOverlay.getLonSpanE6();
		mapController.zoomToSpan(latSpan, lonSpan);

		overlays.add(manyHillsOverlay);
		dbAdapter.close();

		
	}
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		dialog = new ProgressDialog(getActivity());
		this.dialog.setMessage("Getting Hills...");
		this.dialog.show();
		return new UpdateHillsTaskLoader(getActivity(), hillType,
				countryClause, where, orderBy, 0, dbAdapter);
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (this.dialog.isShowing()) {
			this.dialog.dismiss();
		}
		update(data);
		
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub
		
	}


	
	
}
