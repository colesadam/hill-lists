package uk.colessoft.android.hilllist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.fragments.HillDetailFragment;
import uk.colessoft.android.hilllist.mvp.HillsAdapter;

public class DisplayHillListFragmentActivity extends FragmentActivity implements
		HillsAdapter.RecyclerItemViewClick {
	/** Called when the activity is first created. */

	boolean useMetricHeights;


	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.hill_list_fragment);

	}


	@Override
	public void hillClicked(int id) {
		HillDetailFragment fragment = (HillDetailFragment) getSupportFragmentManager()
				.findFragmentById(R.id.hill_detail_fragment);

		if (fragment == null || !fragment.isInLayout()) {
			Intent intent = new Intent(this, HillDetailFragmentActivity.class);
			intent.putExtra("rowid", id);
			startActivity(intent);
		} else {

			fragment.updateHill(id);
		}
	}
}