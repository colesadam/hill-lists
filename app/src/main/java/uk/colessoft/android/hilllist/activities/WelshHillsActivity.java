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
import android.widget.TextView;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.database.HillDbAdapter;

public class WelshHillsActivity extends Activity {

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

		
		}
		}
		return descDialog;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		// TODO Auto-generated method stub
		super.onPrepareDialog(id, dialog);
		dialog.setTitle("Main Welsh Hill Groups");
		
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
		setContentView(R.layout.welsh_hills_lists);

		TextView hewitts = (TextView) findViewById(R.id.welsh_hewitts);
		TextView nuttalls = (TextView) findViewById(R.id.welsh_nuttalls);
		TextView marilyns=(TextView)findViewById(R.id.welsh_marilyns);
		TextView allWelsh=(TextView)findViewById(R.id.allwelsh);

		
		((View) allWelsh.getParent())
		.setOnClickListener(v -> showHills(null, "All Welsh Hills"));


		((View) hewitts.getParent())
				.setOnClickListener(v -> showHills(HillDbAdapter.KEY_hewitt, "Welsh Hewitts"));

		((View) nuttalls.getParent())
				.setOnClickListener(v -> showHills(HillDbAdapter.KEY_nuttall, "Welsh Nuttalls"));
		((View) marilyns.getParent())
		.setOnClickListener(v -> showHills(HillDbAdapter.KEY_marilyn, "Welsh Marilyns"));

	

	}

	private void showHills(String hilltype, String hilllistType) {
		Intent intent = new Intent(WelshHillsActivity.this,
				DisplayHillListFragmentActivity.class);
		intent.putExtra("hilllistType", hilllistType);
		intent.putExtra("hilltype", hilltype);
		intent.putExtra("country", Main.WALES);
		startActivity(intent);
	}

}
