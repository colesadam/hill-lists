package uk.colessoft.android.hilllist.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.database.OldHillDbAdapter;

public class OtherGBHillsActivity extends Activity {

	private boolean countryFlag = false;
	private Dialog descDialog;
	private static final int ID_DESCDIALOG = 1;

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case (ID_DESCDIALOG): {
			descDialog = new Dialog(this);
			descDialog.setContentView(R.layout.hill_group_descriptions);
			ViewGroup parent = (ViewGroup) descDialog
					.findViewById(R.id.groups_layout);
			LayoutInflater inflater = descDialog.getLayoutInflater();
			inflater.inflate(R.layout.hewitts_description, parent);
			inflater.inflate(R.layout.nuttalls_description,parent,true);
			inflater.inflate(R.layout.marilyns_description,parent);
			inflater.inflate(R.layout.deweys_description,parent);
			inflater.inflate(R.layout.trail_100_description,parent);

		
		}
		}
		return descDialog;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		// TODO Auto-generated method stub
		super.onPrepareDialog(id, dialog);
		dialog.setTitle("Other Hill Groups");
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.groups_menu, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case android.R.id.home:{
            // app icon in Action Bar clicked; go home
            Intent intent = new Intent(this, Main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;

		}
		case (R.id.menu_descriptions): {

			showDialog(ID_DESCDIALOG);

			return true;

		}
		}
		return false;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.other_gb_hills_lists);

		View hewitts = findViewById(R.id.hewitts);
		View nuttalls = findViewById(R.id.nuttalls);
		View marilyns = findViewById(R.id.marilyns);
		View countyTops = findViewById(R.id.county_tops);
		View deweys = findViewById(R.id.deweys);
		View trail100 = findViewById(R.id.trail_100);
		View iomni = findViewById(R.id.iomni);

		iomni.setOnClickListener(v -> {
            countryFlag = true;
            showHills(null, "NI/Isle of Man");
        });

		hewitts.setOnClickListener(v -> showHills(OldHillDbAdapter.KEY_hewitt, "Hewitts"));

		nuttalls.setOnClickListener(v -> showHills(OldHillDbAdapter.KEY_nuttall, "Nuttalls"));
		marilyns.setOnClickListener(v -> showHills(OldHillDbAdapter.KEY_marilyn, "Marilyns"));

		countyTops.setOnClickListener(v -> showHills(OldHillDbAdapter.KEY_countyTop, "County Tops"));
		deweys.setOnClickListener(v -> showHills(OldHillDbAdapter.KEY_dewey, "Deweys"));
		trail100.setOnClickListener(v -> showHills(OldHillDbAdapter.KEY_trail100, "Trail 100"));

	}

	private void showHills(String hilltype, String hilllistType) {
		Intent intent = new Intent(OtherGBHillsActivity.this,
				DisplayHillListFragmentActivity.class);
		intent.putExtra("hilllistType", hilllistType);
		intent.putExtra("hilltype", hilltype);
		if (countryFlag)
			intent.putExtra("country", Main.OTHER_GB);
		countryFlag = false;

		startActivity(intent);
	}

}
