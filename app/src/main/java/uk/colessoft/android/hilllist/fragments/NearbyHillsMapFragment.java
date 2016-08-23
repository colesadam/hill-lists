package uk.colessoft.android.hilllist.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import java.util.ArrayList;
import java.util.List;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.activities.AdamsSpecialInterface;
import uk.colessoft.android.hilllist.database.HillDbAdapter;
import uk.colessoft.android.hilllist.model.Hill;
import uk.colessoft.android.hilllist.model.TinyHill;
import uk.colessoft.android.hilllist.overlays.BalloonManyHillsOverlay;
import uk.colessoft.android.hilllist.overlays.ManyHillsOverlay;

public class NearbyHillsMapFragment extends Fragment {

	@Override
	public void onDestroy() {

		super.onDestroy();
	}

	private View viewer;
	private HillDbAdapter dbAdapter;
	private MapController mapController;
	private BalloonManyHillsOverlay manyHillsOverlay;
	private ManyHillsOverlay cmanyHillsOverlay;
	private double lat;
	private double lng;
	private boolean gotHills;
	private MyLocationOverlay myLocOverlay;
	private ProgressDialog dialog;
	private MapView mapView;
	private ArrayList<OverlayItem> items;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
    	dbAdapter = new HillDbAdapter(getActivity());
       

	}
	
	/**
	 * Initialises the MyLocationOverlay and adds it to the overlays of the map
	 */
	private void initMyLocation() {
		
		myLocOverlay.enableMyLocation();
		myLocOverlay.enableCompass();
		
		
		mapView.getOverlays().add(myLocOverlay);
		//mapController.setCenter(myLocOverlay.getMyLocation());
 
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		myLocOverlay.disableCompass();
		myLocOverlay.disableMyLocation();

		super.onPause();
		
	}

	@Override
	public void onResume() {
		
		initMyLocation();
		super.onResume();
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		viewer = inflater.inflate(R.layout.many_hills_map, container, false);

		return viewer;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mapView = ((AdamsSpecialInterface) getActivity())
				.getMapView();
		((ViewGroup) viewer).addView(mapView, 0);
		dbAdapter = new HillDbAdapter(getActivity());
		if(!dbAdapter.isOpen())
			dbAdapter.open();

		String[] rowids;
		String title;
		int passedRowId;
		int selectedIndex = 0;

		title = getActivity().getIntent().getExtras().getString("title");
		rowids = getActivity().getIntent().getExtras().getStringArray("rowids");
		getActivity().setTitle(title);

		Runnable showWaitDialog = new Runnable() {

			public void run() {

				while (!gotHills) {

				}

				dialog.dismiss();

			}

		};
		dialog = ProgressDialog.show(getActivity(), "Please wait...",

		"Getting Hill Positions ...", true);
		Thread timer = new Thread(showWaitDialog);

		timer.start();

		List<TinyHill> hillPoints = new ArrayList<TinyHill>();
		List<TinyHill> climbedHillPoints = new ArrayList<TinyHill>();

		for (String rowid : rowids) {
			long lrow = Long.parseLong(rowid);
			Hill hill = dbAdapter.getHill(lrow);
			TinyHill t = new TinyHill();
			t._id = hill.get_id();
			t.setHillname(hill.getHillname());
			lat = hill.getLatitude() * 1E6;
			lng = hill.getLongitude() * 1E6;
			t.setLatitude(lat);
			t.setLongitude(lng);
			if (hill.getHillClimbed() != null) {
				t.setClimbed(true);
			} else {
				t.setClimbed(false);
			}
			hillPoints.add(t);
		}
		gotHills = true;

		mapView.setSatellite(true);

		final ToggleButton mapButton = (ToggleButton) ((ViewGroup) viewer)
				.findViewById(R.id.satellite_button);
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

		Drawable marker = getResources().getDrawable(R.drawable.yellow_hill);
		Drawable cmarker = getResources().getDrawable(R.drawable.green_hill);

		marker.setBounds(0, 0, marker.getIntrinsicWidth(),
				marker.getIntrinsicHeight());
		cmarker.setBounds(0, 0, cmarker.getIntrinsicWidth(),
				cmarker.getIntrinsicHeight());
		cmarker.setBounds(0 - (cmarker.getIntrinsicWidth() / 2),
				0 - (cmarker.getIntrinsicHeight() / 2),
				cmarker.getIntrinsicWidth() / 2,
				(cmarker.getIntrinsicHeight() / 2));

		// Add the Overlay
		manyHillsOverlay = new BalloonManyHillsOverlay(marker, mapView);

		items = new ArrayList();
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
		manyHillsOverlay.setHillPoints(items, getActivity(), selectedIndex);

		int latSpan = manyHillsOverlay.getLatSpanE6();
		int lonSpan = manyHillsOverlay.getLonSpanE6();
		mapController.zoomToSpan(latSpan, lonSpan);

		List<Overlay> overlays = mapView.getOverlays();
		overlays.add(manyHillsOverlay);
		// overlays.add(cmanyHillsOverlay);
		myLocOverlay = new MyLocationOverlay(getActivity(), mapView);

	}
	
	public void moveMarker(int rowid){
		int index=0;
		for(OverlayItem item:items){
			if(item.getSnippet().equals(Integer.toString(rowid))){
				manyHillsOverlay.onTap(index);
				return;
			}
			index++;
		}
	}

}
