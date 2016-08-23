package uk.colessoft.android.hilllist.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import java.util.ArrayList;
import java.util.List;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.database.HillDbAdapter;
import uk.colessoft.android.hilllist.model.Hill;
import uk.colessoft.android.hilllist.model.TinyHill;
import uk.colessoft.android.hilllist.overlays.BalloonManyHillsOverlay;
import uk.colessoft.android.hilllist.overlays.BalloonManyHillsOverlay.HillTappedListener;
import uk.colessoft.android.hilllist.overlays.BalloonManyHillsOverlay.MapOnHillSelectedListener;
import uk.colessoft.android.hilllist.overlays.SingleMarkerOverlay;
import uk.colessoft.android.hilllist.utility.DistanceCalculator;

public class DetailGMapActivity extends MapActivity implements MapOnHillSelectedListener,HillTappedListener{
	static final private int SHOW_NEARBY = Menu.FIRST;

	private HillDbAdapter dbAdapter;
	private MapView mapView;
	private MapController mapController;
	private SingleMarkerOverlay positionOverlay;
	private static double nearRadius=16.09;
	private double lat1;private double lon1;private int rowid;
	private boolean firstRun=true;

	private String title;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuItem show_gmap = menu.add(0, SHOW_NEARBY, Menu.NONE,
				"Show Nearby Hills");

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		super.onOptionsItemSelected(item);
		

		switch (item.getItemId()) {
		case (SHOW_NEARBY): {
			
			addNearHills();

			return true;
		}
		case android.R.id.home:
            // app icon in Action Bar clicked; go home
            Intent intent = new Intent(this, Main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;

		}
		return false;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.many_hills_map);
		
		Drawable marker=getResources().getDrawable(R.drawable.purple_hill);

		marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());

		dbAdapter = new HillDbAdapter(this);
		dbAdapter.open();
		rowid=getIntent().getExtras().getInt("rowid");
		title=getIntent().getExtras().getString("title");
		setTitle(title);
		Hill hill = dbAdapter.getHill(rowid);
		dbAdapter.close();
		LayoutInflater inflater = (LayoutInflater) (this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
		mapView = (MapView) inflater.inflate(R.layout.mapview, null);
		((ViewGroup)findViewById(R.id.many_map_rel)).addView(mapView,0);
		
		mapView.setSatellite(true);

		final ToggleButton mapButton = (ToggleButton) findViewById(R.id.satellite_button);
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
		lat1=hill.getLatitude();
		lon1=hill.getLongitude();
		Double lat = lat1 * 1E6;
		Double lng = lon1 * 1E6;
		GeoPoint point = new GeoPoint(lat.intValue(), lng.intValue());

		mapController = mapView.getController();
		mapController.setCenter(point);
		mapController.setZoom(8);
		mapView.setBuiltInZoomControls(true);
		

		// Add the PositionOverlay
		positionOverlay = new SingleMarkerOverlay();
		positionOverlay.setHillname(hill.getHillname());
		positionOverlay.setHillMarker(point);
		positionOverlay.setMarker(marker);
		//positionOverlay.drawSingleHill(canvas, mapView, shadow);
		//positionOverlay.setHillname(hill.getHillname());
		
		List<Overlay> overlays = mapView.getOverlays();
		overlays.add(positionOverlay);
		
		
		//hello

	}

	private void addNearHills() {
		dbAdapter.open();
		Cursor hillsCursor = dbAdapter
				.getAllHillsCursor();

		List<TinyHill> hillPoints = new ArrayList<TinyHill>();
		Drawable marker = getResources().getDrawable(R.drawable.yellow_hill);
		Drawable cmarker= getResources().getDrawable(R.drawable.green_hill);
		marker.setBounds(0, 0, marker.getIntrinsicWidth(),
				marker.getIntrinsicHeight());
		cmarker.setBounds(0, 0, cmarker.getIntrinsicWidth(),
				cmarker.getIntrinsicHeight());

		// iterate over cursor and get hill positions
		// Make sure there is at least one row.
		if (hillsCursor.moveToFirst()) {
			// Iterate over each cursor.
			do {
				Double lat = hillsCursor.getDouble(hillsCursor
						.getColumnIndex(HillDbAdapter.KEY_LATITUDE)) * 1E6;
				Double lng = hillsCursor.getDouble(hillsCursor
						.getColumnIndex(HillDbAdapter.KEY_LONGITUDE)) * 1E6;
				
				double distanceKm=DistanceCalculator.CalculationByDistance(lat1, lat/1E6, lon1, lng/1E6);
				int row_id = hillsCursor.getInt(hillsCursor
						.getColumnIndex(HillDbAdapter.KEY_ID));
				if (distanceKm<nearRadius&&row_id!=rowid) {
					

					String hillname = hillsCursor.getString(hillsCursor
							.getColumnIndex(HillDbAdapter.KEY_HILLNAME));
					TinyHill tinyHill = new TinyHill();
					tinyHill._id = row_id;
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
				}

			} while (hillsCursor.moveToNext());
		}
		hillsCursor.close();
		dbAdapter.close();
		BalloonManyHillsOverlay manyHillsOverlay = new BalloonManyHillsOverlay(marker,
				mapView);
		List<OverlayItem>items=new ArrayList();
		for (TinyHill gc : hillPoints) {
			OverlayItem oi = new OverlayItem(new GeoPoint(gc.getLatitude()
					.intValue(), gc.getLongitude().intValue()),
					gc.getHillname(), String.valueOf(gc._id));
			if(gc.isClimbed()) oi.setMarker(cmarker);
			else oi.setMarker(marker);
			items.add(oi);
		}
		manyHillsOverlay.setHillPoints(items, this, 0);
		int latSpan=manyHillsOverlay.getLatSpanE6();
		int lonSpan=manyHillsOverlay.getLonSpanE6();
		mapController.zoomToSpan(latSpan, lonSpan);

		List<Overlay> overlays = mapView.getOverlays();
		
		overlays.add(0,manyHillsOverlay);
		mapController.setCenter(new GeoPoint((new Double(lat1*1E6)).intValue(),new Double(lon1*1E6).intValue()));
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public void mapOnHillSelected(int rowid) {
		
		
	}

	public void hillTapped(int rowid) {


	Intent intent = new Intent(DetailGMapActivity.this,
			HillDetailFragmentActivity.class);

	intent.putExtra("rowid", rowid);

	startActivity(intent);

		
	}

}
