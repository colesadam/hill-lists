package uk.colessoft.android.hilllist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.database.HillDbAdapter;
import uk.colessoft.android.hilllist.fragments.DisplayHillListFragment;
import uk.colessoft.android.hilllist.fragments.HillDetailFragment;
import uk.colessoft.android.hilllist.overlays.BalloonManyHillsOverlay;
import uk.colessoft.android.hilllist.overlays.BalloonManyHillsOverlay.HillTappedListener;
import uk.colessoft.android.hilllist.overlays.ManyHillsOverlay;

public class ListHillsMapFragmentActivity extends FragmentActivity implements
		DisplayHillListFragment.OnHillSelectedListener, HillTappedListener,
		BalloonManyHillsOverlay.MapOnHillSelectedListener,
		AdamsSpecialInterface {

	private HillDbAdapter dbAdapter;
	private MapView mapView;
	private MapController mapController;
	private BalloonManyHillsOverlay manyHillsOverlay;
	private ManyHillsOverlay cmanyHillsOverlay;
	private double lat;
	private double lng;
	public MapView fView;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.hills_map_fragment);
		/*
		 * Horrendous hack as there is no support for MapFragment and I have to
		 * get a reference to mapview.
		 */

//		LayoutInflater inflater = (LayoutInflater) (this
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
//		mapView = (MapView) inflater.inflate(R.layout.mapview, null);
//
//		fView = mapView;

	}

	public void onHillSelected(int rowid) {

		HillDetailFragment fragment = (HillDetailFragment) getSupportFragmentManager()
				.findFragmentById(R.id.hill_detail_fragment);

		if (fragment == null || !fragment.isInLayout()) {
			Intent intent = new Intent(this, HillDetailFragmentActivity.class);
			intent.putExtra("rowid", rowid);
			startActivity(intent);
		} else {
			fragment.updateHill(rowid);
		}

	}


	public void mapOnHillSelected(int rowid) {
		HillDetailFragment fragment = (HillDetailFragment) getSupportFragmentManager()
				.findFragmentById(R.id.hill_detail_fragment);

		if (fragment == null || !fragment.isInLayout()) {
			/*Intent intent = new Intent(ListHillsMapFragmentActivity.this,
					HillDetailFragmentActivity.class);

			intent.putExtra("rowid", rowid);

			startActivity(intent);*/
		} else {
			fragment.updateHill(rowid);
		}
	}

	public MapView getMapView() {
		// TODO Auto-generated method stub
		return fView;
	}

	public void hillTapped(int rowid) {
		HillDetailFragment fragment = (HillDetailFragment) getSupportFragmentManager()
				.findFragmentById(R.id.hill_detail_fragment);

		if (fragment == null || !fragment.isInLayout()) {
			Intent intent = new Intent(ListHillsMapFragmentActivity.this,
					HillDetailFragmentActivity.class);

			intent.putExtra("rowid", rowid);

			startActivity(intent);
		} else {
			fragment.updateHill(rowid);
		}

	}

}
