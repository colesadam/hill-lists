package uk.colessoft.android.hilllist.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.fragments.HillDetailFragment;
import uk.colessoft.android.hilllist.fragments.NearbyHillsFragment.MyLocationListener;
import uk.colessoft.android.hilllist.fragments.NearbyHillsFragment.OnLocationFoundListener;
import uk.colessoft.android.hilllist.overlays.BalloonManyHillsOverlay.HillTappedListener;

public class NearbyHillsFragmentActivity extends FragmentActivity
		implements
		OnLocationFoundListener,
		HillTappedListener,
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.nearby_hills_fragment);

	}



	public void locationFound() {
		dialog.dismiss();

	}

	public void showDialog() {

		dialog = new ProgressDialog(this);
		dialog.setTitle("Please wait...");
		dialog.setMessage("Acquiring Location ...");
		// dialog.setCancelable(true);
		dialog.setButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Use either finish() or return() to either close the activity
				// or just the dialog
				finish();
			}
		});
		dialog.show();

	}

	public void hillTapped(int rowid) {
		/*HillDetailFragment fragment = (HillDetailFragment) getSupportFragmentManager()
				.findFragmentById(R.id.hill_detail_fragment);

		if (fragment == null || !fragment.isInLayout()) {
			Intent intent = new Intent(this, HillDetailFragmentActivity.class);
			intent.putExtra("rowid", rowid);
			startActivity(intent);
		} else {

			fragment.updateHill(rowid);
		}*/

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

}
