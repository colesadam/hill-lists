package uk.colessoft.android.hilllist.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import javax.inject.Inject;

import uk.colessoft.android.hilllist.BHApplication;
import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.database.DbHelper;

import static uk.colessoft.android.hilllist.database.HillsTables.KEY_HILLNAME;

public class Main extends AppCompatActivity {

	public static final int SCOTLAND = 1;
	public static final int WALES = 2;
	public static final int ENGLAND = 3;
	public static final int OTHER_GB = 4;

	@Inject
	DbHelper dbHelper;

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

		((BHApplication) getApplication()).getDbComponent().inject(this);		//setTitle(getTitle() + " - Choose an Option");
		boolean successful;
		


		View scotland = findViewById(R.id.menu_scotland);
		View wales = findViewById(R.id.menu_wales);
		View england = findViewById(R.id.menu_england);
		View otherGB = findViewById(R.id.menu_gb);
		View nearbyHills = findViewById(R.id.menu_nearby);
		View exportBagging = findViewById(R.id.menu_backup);
		View viewforecasts = findViewById(R.id.menu_forecasts);
		View searchAll = findViewById(R.id.menu_search);

		nearbyHills.setOnClickListener(v -> {
            //Intent intent = new Intent(Main.this, NearbyHillsFragmentActivity.class);
			Intent intent = new Intent(Main.this, LocationAwareFragmentActivity.class);
            startActivity(intent);

        });

		scotland.setOnClickListener(v -> showHills(SCOTLAND));

		wales.setOnClickListener(v -> showHills(WALES));

		england.setOnClickListener(v -> showHills(ENGLAND));

		otherGB.setOnClickListener(v -> showHills(OTHER_GB));

		exportBagging.setOnClickListener(v -> {
            Intent intent = new Intent(Main.this,
                    BaggingExportActivity.class);
            startActivity(intent);

        });

		viewforecasts.setOnClickListener(new View.OnClickListener() {

			private final String forecastLink = "http://www.metoffice.gov.uk/public/weather/mountain-forecast/#?tab=mountainHome";

			public void onClick(View v) {
				Intent intent = new Intent("android.intent.action.VIEW", Uri
						.parse(forecastLink));
				startActivity(intent);

			}

		});
		
		searchAll.setOnClickListener(v -> search());

	}

	private void showHills(int country) {
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

		alert.setPositiveButton("Ok", (dialog, whichButton) -> {
            String value = searchText.getText().toString();
            String where = "";
            if (!"".equals(value)) {
                where = KEY_HILLNAME+" like "
                        + DatabaseUtils.sqlEscapeString("%" + value + "%");

            } else
                where = "";
            intent.putExtra("search", where);
            intent.putExtra("hilllistType", "Search Results");

            startActivity(intent);

        });
		alert.setNegativeButton("Cancel",
				(dialog, whichButton) -> {

                });

		AlertDialog search = alert.create();
		search.show();

	}
}
