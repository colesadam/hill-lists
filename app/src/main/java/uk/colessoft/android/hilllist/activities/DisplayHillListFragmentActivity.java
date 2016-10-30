package uk.colessoft.android.hilllist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.fragments.HillListFragment;
import uk.colessoft.android.hilllist.fragments.HillDetailFragment;

public class DisplayHillListFragmentActivity extends AppCompatActivity implements
		HillListFragment.OnHillSelectedListener {
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