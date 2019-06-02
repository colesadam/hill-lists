package uk.colessoft.android.hilllist.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.fragment.HillDetailFragment;
import uk.colessoft.android.hilllist.fragment.NearbyHillsFragment.MyLocationListener;
import uk.colessoft.android.hilllist.fragment.NearbyHillsFragment.OnLocationFoundListener;

public class NearbyHillsFragmentActivity extends AppCompatActivity
		implements
		OnLocationFoundListener,
		uk.colessoft.android.hilllist.fragment.NearbyHillsFragment.OnHillSelectedListener {
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
		dialog.setButton("Cancel", (dialog1, which) -> {
            // Use either finish() or return() to either close the activity
            // or just the dialog
            finish();
        });
		dialog.show();

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
