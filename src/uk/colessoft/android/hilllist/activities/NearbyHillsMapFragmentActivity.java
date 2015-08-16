package uk.colessoft.android.hilllist.activities;

import com.google.android.maps.MapView;

import uk.colessoft.android.hilllist.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import uk.colessoft.android.hilllist.fragments.HillDetailFragment;
import uk.colessoft.android.hilllist.fragments.NearbyHillsFragment;
import uk.colessoft.android.hilllist.fragments.NearbyHillsFragment.MyLocationListener;
import uk.colessoft.android.hilllist.fragments.NearbyHillsFragment.OnHillSelectedListener;
import uk.colessoft.android.hilllist.fragments.NearbyHillsFragment.OnLocationFoundListener;
import uk.colessoft.android.hilllist.fragments.NearbyHillsMapFragment;
import uk.colessoft.android.hilllist.overlays.BalloonManyHillsOverlay.HillTappedListener;
import uk.colessoft.android.hilllist.overlays.BalloonManyHillsOverlay.MapOnHillSelectedListener;

public class NearbyHillsMapFragmentActivity extends FragmentActivity
		implements OnLocationFoundListener,AdamsSpecialInterface,MapOnHillSelectedListener,HillTappedListener,
		uk.colessoft.android.hilllist.fragments.NearbyHillsFragment.OnHillSelectedListener {
	boolean useMetricHeights;
	boolean useMetricDistances;

	double lat1;
	double lon1;
	private boolean locationSet = false;
	private LocationManager lm;
	private MyLocationListener locationListener;
	private ProgressDialog dialog;

	MenuItem dist;
	private MapView fView;
	private MapView mapView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.nearby_hills_map_fragment);
		LayoutInflater inflater = (LayoutInflater) (this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
		mapView = (MapView) inflater.inflate(R.layout.mapview, null);

		fView = mapView;

	}

	public void onHillSelected(int rowid) {
		NearbyHillsMapFragment fragment = (NearbyHillsMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.nearby_hills_map_fragment);

		
			fragment.moveMarker(rowid);
		

	}

	public void locationFound() {
		dialog.dismiss();
		
	}
	
	public void showDialog(){
		
		
		dialog = ProgressDialog.show(this, "Please wait...",

		"Acquiring Location ...", true);
		dialog.show();

	}

	public MapView getMapView() {
		// TODO Auto-generated method stub
		return fView;
	}

	public void mapOnHillSelected(int rowid) {
		// TODO Auto-generated method stub
		
	}

	public void hillTapped(int rowid) {

			Intent intent = new Intent(this, HillDetailFragmentActivity.class);
			intent.putExtra("rowid", rowid);
			startActivity(intent);

		
	}


}
