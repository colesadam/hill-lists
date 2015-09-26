package uk.colessoft.android.hilllist.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.database.HillDbAdapter;

public class Main extends Activity {

	public static final String SCOTLAND = "Scotland";
	public static final String WALES = "Wales";
	public static final String ENGLAND = "England";
	public static final String OTHER_GB = "GB";

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case (R.id.menu_preferences): {
			Intent intent = new Intent(Main.this, PreferencesActivity.class);
			startActivity(intent);

			return true;

		}
		case (R.id.about_option):{
			Intent intent = new Intent(Main.this, AboutActivity.class);
			startActivity(intent);

			return true;
		}

		}
		return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);

		HillDbAdapter dbAdapter = new HillDbAdapter(this);
		//HillsDatabaseHelper newDbHelper=new HillsDatabaseHelper(this);
		//newDbHelper.getReadableDatabase();
		setTitle(getTitle() + " - Choose an Option");
		boolean successful;
		
		try {

			successful = dbAdapter.createDatabase();


		} catch (IOException ioe) {

			ioe.printStackTrace();
			throw new Error("Unable to create database", ioe);

		}
		if (!successful) {
			CharSequence text = "No External Storage Available for Database - Please insert an SD Card and restart.";
			int duration = Toast.LENGTH_LONG;

			Toast toast = Toast.makeText(getApplicationContext(), text,
					duration);
			toast.show();

			//
		}

		View scotland = findViewById(R.id.menu_scotland);
		View wales = (View) findViewById(R.id.menu_wales);
		View england = (View) findViewById(R.id.menu_england);
		View otherGB = (View) findViewById(R.id.menu_gb);
		View nearbyHills = (View) findViewById(R.id.menu_nearby);
		View exportBagging = (View) findViewById(R.id.menu_backup);
		View viewforecasts = (View) findViewById(R.id.menu_forecasts);
		View searchAll = findViewById(R.id.menu_search);

		nearbyHills.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(Main.this, NearbyHillsFragmentActivity.class);
				startActivity(intent);

			}
		});

		scotland.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showHills(SCOTLAND);
			}
		});

		wales.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showHills(WALES);
			}
		});

		england.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showHills(ENGLAND);
			}
		});

		otherGB.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showHills(OTHER_GB);
			}
		});

		exportBagging.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(Main.this,
						BaggingExportActivity.class);
				startActivity(intent);

			}

		});

		viewforecasts.setOnClickListener(new View.OnClickListener() {

			private String forecastLink = "http://www.metoffice.gov.uk/mobile/mountainareaforecasts";

			public void onClick(View v) {
				Intent intent = new Intent("android.intent.action.VIEW", Uri
						.parse(forecastLink));
				startActivity(intent);

			}

		});
		
		searchAll.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				search();
				
			}
		});

	}

	private void showHills(String country) {
		Intent intent = null;
		switch (country) {

		case SCOTLAND: {
			intent = new Intent(Main.this, ScottishHillsActivity.class);
			break;

		}
		case WALES: {
			intent = new Intent(Main.this, WelshHillsActivity.class);
			break;

		}
		case ENGLAND: {
			intent = new Intent(Main.this, EnglishHillsActivity.class);
			break;

		}
		case OTHER_GB: {
			intent = new Intent(Main.this, OtherGBHillsActivity.class);
			break;

		}

		}
		// intent.putExtra("country", country);

		startActivity(intent);
	}

	private void search() {
		final Intent intent = new Intent(Main.this,
				DisplayHillListFragmentActivity.class);

		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Search for hill");

		// Set an EditText view to get user input
		final EditText searchText = new EditText(this);

		alert.setView(searchText);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = searchText.getText().toString();
				String where = "";
				if (!"".equals(value)) {
					where = "hillname like "
							+ DatabaseUtils.sqlEscapeString("%" + value + "%");

				} else
					where = "";
				intent.putExtra("search", where);
				intent.putExtra("hilllistType", "Search Results");

				startActivity(intent);

			}
		});
		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				});

		AlertDialog search = alert.create();
		search.show();

	}
}
