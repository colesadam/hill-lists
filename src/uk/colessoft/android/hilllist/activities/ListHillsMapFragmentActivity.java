package uk.colessoft.android.hilllist.activities;

import java.util.ArrayList;
import java.util.List;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.database.HillDbAdapter;
import uk.colessoft.android.hilllist.fragments.DisplayHillListFragment;
import uk.colessoft.android.hilllist.fragments.HillDetailFragment;
import uk.colessoft.android.hilllist.objects.TinyHill;
import uk.colessoft.android.hilllist.overlays.BalloonManyHillsOverlay;
import uk.colessoft.android.hilllist.overlays.BalloonManyHillsOverlay.HillTappedListener;
import uk.colessoft.android.hilllist.overlays.ManyHillsOverlay;
import uk.colessoft.android.hilllist.overlays.SingleHillOverlay;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

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

		LayoutInflater inflater = (LayoutInflater) (this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
		mapView = (MapView) inflater.inflate(R.layout.mapview, null);

		fView = mapView;

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

/*	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}*/

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
