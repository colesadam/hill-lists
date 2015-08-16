package uk.colessoft.android.hilllist.activities;

import java.util.HashMap;
import java.util.Iterator;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.database.HillDbAdapter;
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
import android.widget.TableRow;
import android.widget.TextView;

public class OtherGBHillsActivity extends Activity {

	boolean countryFlag = false;
	Dialog descDialog;
	static final int ID_DESCDIALOG = 1;

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

		iomni.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				countryFlag = true;
				showHills(null, "NI/Isle of Man");
			}
		});

		hewitts.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showHills(HillDbAdapter.KEY_hewitt, "Hewitts");
			}
		});

		nuttalls.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showHills(HillDbAdapter.KEY_nuttall, "Nuttalls");
			}
		});
		marilyns.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showHills(HillDbAdapter.KEY_marilyn, "Marilyns");
			}
		});

		countyTops.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showHills(HillDbAdapter.KEY_countyTop, "County Tops");
			}
		});
		deweys.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showHills(HillDbAdapter.KEY_dewey, "Deweys");
			}
		});
		trail100.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showHills(HillDbAdapter.KEY_trail100, "Trail 100");
			}
		});

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
