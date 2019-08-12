package uk.colessoft.android.hilllist.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import uk.colessoft.android.hilllist.R;

public class EnglishHillsActivity extends AppCompatActivity {

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
			inflater.inflate(R.layout.wainwrights_description,parent);
			inflater.inflate(R.layout.wainwrights_outlying_description,parent);
			inflater.inflate(R.layout.birketts_description,parent);

		
		}
		}
		return descDialog;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		// TODO Auto-generated method stub
		super.onPrepareDialog(id, dialog);
		dialog.setTitle("Main English HillDetail Groups");
		
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
		setContentView(R.layout.english_hills_lists);
		

		View hewitts =  findViewById(R.id.english_hewitts);
		View nuttalls =  findViewById(R.id.english_nuttalls);
		View marilyns=findViewById(R.id.english_marilyns);
		View wainwrights=findViewById(R.id.wainwrights);
		View wainwrightFells=findViewById(R.id.wainrights_outlying_fells);
		View birketts=findViewById(R.id.birketts);
		
		View allEnglishHills=findViewById(R.id.allenglish);
		
		allEnglishHills.setOnClickListener(v -> showHills(null, "All English Hills"));


		hewitts.setOnClickListener(v -> showHills("Hew", "English Hewitts"));

		nuttalls.setOnClickListener(v -> showHills("N", "English Nuttalls"));
		marilyns.setOnClickListener(v -> showHills("Ma", "English Marilyns"));
		
		wainwrights.setOnClickListener(v -> showHills("W", "Wainwrights"));
		
		wainwrightFells.setOnClickListener(v -> showHills("WO", "Wainwright Outlying Fells"));
		
		birketts.setOnClickListener(v -> showHills("B", "Birketts"));

	}

	private void showHills(String hilltype, String hilllistType) {
		Intent intent = new Intent(EnglishHillsActivity.this,
				HillListFragmentActivity.class);
		intent.putExtra("hilllistType", hilllistType);
		intent.putExtra("hilltype", hilltype);
		intent.putExtra("country", Main.ENGLAND);
		startActivity(intent);
	}

}
