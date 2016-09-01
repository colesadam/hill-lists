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
import android.widget.ScrollView;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.database.OldHillDbAdapter;

public class ScottishHillsActivity extends Activity {
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
			inflater.inflate(R.layout.munros_description, parent);
			inflater.inflate(R.layout.murdos_description,parent,true);
			inflater.inflate(R.layout.corbetts_description,parent);
			inflater.inflate(R.layout.donalds_description,parent);
			inflater.inflate(R.layout.grahams_description,parent);
			inflater.inflate(R.layout.marilyns_description,parent);
			ScrollView s=((ScrollView)parent.getParent());
			s.refreshDrawableState();
		
			

		}
		}
		return descDialog;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		// TODO Auto-generated method stub
		super.onPrepareDialog(id, dialog);
		dialog.setTitle("Main Scottish Hill Groups");
		
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
		setContentView(R.layout.scottish_hills_lists);

		View munros = findViewById(R.id.munros);
		View munro_tops = findViewById(R.id.munro_tops);
		View murdos = findViewById(R.id.murdos);
		View corbetts = findViewById(R.id.corbetts);
		View corbett_tops = findViewById(R.id.corbett_tops);
		View donalds = findViewById(R.id.donalds);
		View donald_tops = findViewById(R.id.donald_tops);
		View grahams = findViewById(R.id.grahams);
		View graham_tops = findViewById(R.id.graham_tops);
		View marilyns = findViewById(R.id.scottish_marilyns);
		View allScottish = findViewById(R.id.allscottish);

		allScottish.setOnClickListener(v -> showHills(null, "All Scottish Hills"));
		munros.setOnClickListener(v -> showHills(OldHillDbAdapter.KEY_munro, "Munros"));

		munro_tops.setOnClickListener(v -> showHills(OldHillDbAdapter.KEY_munroTop, "Munro Tops"));

		murdos.setOnClickListener(v -> showHills(OldHillDbAdapter.KEY_murdo, "Murdos"));

		corbetts.setOnClickListener(v -> showHills(OldHillDbAdapter.KEY_corbett, "Corbetts"));

		corbett_tops.setOnClickListener(v -> showHills(OldHillDbAdapter.KEY_corbettTopOfMunro + "='1' OR "
                + OldHillDbAdapter.KEY_corbettTopOfCorbett, "Corbett Tops"));

		donalds.setOnClickListener(v -> showHills(OldHillDbAdapter.KEY_donald, "Donalds"));

		donald_tops.setOnClickListener(v -> showHills(OldHillDbAdapter.KEY_donaldTop, "Donald Tops"));

		grahams.setOnClickListener(v -> showHills(OldHillDbAdapter.KEY_graham, "Grahams"));

		graham_tops.setOnClickListener(v -> showHills(OldHillDbAdapter.KEY_grahamTopOfMunro + "='1' OR "
                + OldHillDbAdapter.KEY_grahamTopOfCorbett + "='1' OR "
                + OldHillDbAdapter.KEY_grahamTopOfGraham, "Graham Tops"));

		marilyns.setOnClickListener(v -> showHills(OldHillDbAdapter.KEY_marilyn, "Scottish Marilyns"));

	}

	private void showHills(String hilltype, String hilllistType) {
		Intent intent = new Intent(ScottishHillsActivity.this,
				DisplayHillListFragmentActivity.class);
		intent.putExtra("hilllistType", hilllistType);
		intent.putExtra("hilltype", hilltype);
		intent.putExtra("country", Main.SCOTLAND);
		startActivity(intent);
	}

}
