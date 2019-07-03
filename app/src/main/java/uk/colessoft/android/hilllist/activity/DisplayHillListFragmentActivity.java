package uk.colessoft.android.hilllist.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import javax.inject.Inject;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.dao.CountryClause;
import uk.colessoft.android.hilllist.database.BritishHillsDatasource;
import uk.colessoft.android.hilllist.fragment.DisplayHillListFragment;
import uk.colessoft.android.hilllist.fragment.HillDetailFragment;
import uk.colessoft.android.hilllist.viewmodel.HillDetailViewModel;
import uk.colessoft.android.hilllist.viewmodel.HillDetailViewModelFactory;

public class DisplayHillListFragmentActivity extends AppCompatActivity implements
		DisplayHillListFragment.OnHillSelectedListener {
	/** Called when the activity is first created. */

	boolean useMetricHeights;
	@Inject
	HillDetailViewModelFactory factory;

	ViewModel viewModel;


	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		String hilltype = getIntent().getExtras().getString("hilltype");
		String hilllistType = getIntent().getExtras()
				.getString("hilllistType");
		if (!"".equals(hilllistType)) {
			setTitle(hilllistType);
		} else
			setTitle("Results");
		int country = getIntent().getExtras().getInt("country");
		String where = getIntent().getExtras().getString("search");
		viewModel = ViewModelProviders.of(this,factory.create(0,hilltype, CountryClause.ENGLAND,null,where,null)).get(HillDetailViewModel.class);
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