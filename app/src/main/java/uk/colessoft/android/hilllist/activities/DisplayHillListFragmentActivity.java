package uk.colessoft.android.hilllist.activities;

import uk.colessoft.android.hilllist.R;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import uk.colessoft.android.hilllist.fragments.DisplayHillListFragment;
import uk.colessoft.android.hilllist.fragments.HillDetailFragment;

public class DisplayHillListFragmentActivity extends FragmentActivity implements
		DisplayHillListFragment.OnHillSelectedListener {
	/** Called when the activity is first created. */

	boolean useMetricHeights;


	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.hill_list_fragment);

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