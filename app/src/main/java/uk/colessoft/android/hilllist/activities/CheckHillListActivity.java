package uk.colessoft.android.hilllist.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Date;

import javax.inject.Inject;

import uk.colessoft.android.hilllist.BHApplication;
import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.database.BritishHillsDatasource;
import uk.colessoft.android.hilllist.database.HillsTables;
import uk.colessoft.android.hilllist.entities.Bagging;

public class CheckHillListActivity extends AppCompatActivity {
    /**
     * Called when the activity is first created.
     */

    private final int ALL_CLIMBED = 1;
    private final int ALL_HILLS = 0;
    private final int ALL_NOT_CLIMBED = 2;

    @Inject
    BritishHillsDatasource dbAdapter;

    private String where = null;
    private String orderBy;
    private ListView myListView;

    private boolean useMetricHeights;

    private String hilltype;
    private String countryClause;
    private final DecimalFormat df3 = new DecimalFormat();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.select_hills_menu, menu);

        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        df3.isParseIntegerOnly();
        super.onCreate(savedInstanceState);
        updateFromPreferences();
        setContentView(R.layout.list_hills);
        myListView = (ListView) findViewById(R.id.myListView);
        registerForContextMenu(myListView);

        hilltype = getIntent().getExtras().getString("hilltype");

        setTitle("Mark Hills as Climbed");
        int country = getIntent().getExtras().getInt("country");
        where = getIntent().getExtras().getString("search");

        switch (country) {
            case Main.SCOTLAND: {
                countryClause = "cast(_Section as float) between 1 and 28.9";
                break;

            }
            case Main.WALES: {
                countryClause = "cast(_Section as float) between 30 and 32.9";
                break;

            }
            case Main.ENGLAND: {
                countryClause = "cast(_Section as float) between 33 and 42.9";
                break;

            }
            case Main.OTHER_GB: {
                countryClause = "_Section='29' OR (cast(_Section as float) between 43 and 43.2)";
                break;

            }

        }

        ((BHApplication) getApplication()).getDbComponent().inject(this);

        orderBy = "cast(" + HillsTables.KEY_HEIGHTM + " as float)" + " desc";
        updateList(2);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "Mark as climbed");
        menu.add(0, v.getId(), 0, "Mark as not climbed");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Mark as climbed") {
            AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                    .getMenuInfo();
            dbAdapter.markHillClimbed((int) info.id, new Date(), "");
            Toast climbed;
            climbed = Toast.makeText(getApplication(), "Marked as Climbed",
                    Toast.LENGTH_SHORT);
            climbed.show();
            TextView hname = (TextView) ((RelativeLayout) info.targetView)
                    .getChildAt(0);
            hname.setTextColor(Color.GREEN);

        } else if (item.getTitle() == "Mark as not climbed") {
            AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                    .getMenuInfo();
            dbAdapter.markHillNotClimbed((int) info.id);
            Toast climbed;
            climbed = Toast.makeText(getApplication(), "Marked as Not Climbed",
                    Toast.LENGTH_SHORT);
            climbed.show();
            TextView hname = (TextView) ((RelativeLayout) info.targetView)
                    .getChildAt(0);
            hname.setTextColor(Color.WHITE);
        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        super.onOptionsItemSelected(item);

        int index = myListView.getSelectedItemPosition();

        switch (item.getItemId()) {
            case (R.id.menu_list_alpha): {

                orderBy = HillsTables.KEY_HILLNAME;
                updateList(2);
                return true;

            }
            case (R.id.menu_list_height): {

                orderBy = "cast(" + HillsTables.KEY_HEIGHTM + " as float)" + " desc";
                updateList(2);
                return true;

            }
            case (R.id.menu_all_climbed): {
                updateList(ALL_CLIMBED);
                return true;
            }

            case (R.id.menu_none_climbed): {
                updateList(0);
                return true;
            }

        }
        return false;
    }

    private String convText(TextView v, String text) {

        double dblAmt;
        dblAmt = Double.valueOf(text);
        return df3.format(dblAmt);

    }

    private void updateList(int allMarked) {

        final Cursor result = dbAdapter.getHillGroup(hilltype, countryClause,
                where, orderBy, 0);

        startManagingCursor(result);

        if (allMarked == 1) {
            if (result.moveToFirst()) {
                // Iterate over each cursor.
                do {

                    int row_id = result.getInt(result
                            .getColumnIndex(HillsTables.KEY_ID));
                    dbAdapter.markHillClimbed(row_id, new Date(), "");

                } while (result.moveToNext());
            }
            updateList(2);
            return;

        }
        if (allMarked == 0) {
            if (result.moveToFirst()) {
                // Iterate over each cursor.
                do {

                    int row_id = result.getInt(result
                            .getColumnIndex(HillsTables.KEY_ID));
                    dbAdapter.markHillNotClimbed(row_id);

                } while (result.moveToNext());
            }
            updateList(2);
            return;
        }
        SimpleCursorAdapter cursorAdapter;
        String[] qstrings;
        myListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        if (useMetricHeights) {
            qstrings = new String[]{"Hillname", "Metres"};
        } else {
            qstrings = new String[]{"Hillname", "dateClimbed"};
        }
        cursorAdapter = new SimpleCursorAdapter(this, R.layout.check_hills,
                result, qstrings, new int[]{R.id.hill_name_checked,
                R.id.check_hill_climbed});
        cursorAdapter.setViewBinder((view, cursor, columnIndex) -> {

            String vtext = cursor.getString(columnIndex);
            switch (view.getId()) {
                case R.id.hill_name_checked: {
                    TextView ttv = (TextView) view;
                    ttv.setText(vtext);

                    return true;
                }
                case R.id.check_hill_climbed: {
                    CheckBox ctv = (CheckBox) view;
                    final int id = cursor.getInt(cursor
                            .getColumnIndex(HillsTables.KEY_ID));
                    if (cursor.getString(cursor
                            .getColumnIndex(Bagging.KEY_DATECLIMBED)) != null) {


                        ctv.setChecked(true);

                    } else
                        ctv.setChecked(false);
                    ctv.setOnClickListener(null);
                    ctv.setOnClickListener(v -> {
                        CheckBox xcv = (CheckBox) v;
                        if (xcv.isChecked()) {
                            dbAdapter.markHillClimbed(id, new Date(), "");
                        } else
                            dbAdapter.markHillNotClimbed(id);

                    });

                    return true;
                }
            }
            return false;
        });
        myListView.setAdapter(cursorAdapter);

    }

    private void updateFromPreferences() {
        Context context = getApplicationContext();
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        useMetricHeights = prefs.getBoolean(
                PreferencesActivity.PREF_METRIC_HEIGHTS, false);
    }
}