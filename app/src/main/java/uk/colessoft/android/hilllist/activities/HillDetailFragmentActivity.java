package uk.colessoft.android.hilllist.activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.database.HillDbAdapter;
import uk.colessoft.android.hilllist.fragments.HillDetailFragment;
import uk.colessoft.android.hilllist.objects.Hill;
import uk.colessoft.android.hilllist.overlays.SingleHillOverlay;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class HillDetailFragmentActivity extends FragmentActivity {

	private HillDbAdapter dbAdapter;

	private String osLink;
	private boolean useMetricHeights;
	static final int DATE_DIALOG_ID = 0;
	static final int MARK_HILL_CLIMBED_DIALOG = 1;

	private Hill hill;
	private int mYear;
	private int mMonth;
	private int mDay;
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay();
		}
	};

	private TextView dateClimbed;

	private Date realDate = new Date();

	private TextView hillnameView;

	private TextView hillheight;



	

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID: {
			mYear = realDate.getYear() + 1900;
			mMonth = realDate.getMonth();
			mDay = realDate.getDate();
			DatePickerDialog d = new DatePickerDialog(this, mDateSetListener,
					mYear, mMonth, mDay);

			return d;
		}
		case MARK_HILL_CLIMBED_DIALOG: {

		}
		}
		return null;
	}



	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		updateFromPreferences();
		setContentView(R.layout.hill_detail_fragment);
		int rowid = getIntent().getExtras().getInt("rowid");

		HillDetailFragment fragment = (HillDetailFragment) getSupportFragmentManager()
				.findFragmentById(R.id.hill_detail_fragment);
		fragment.updateHill(rowid);

	}

	private void updateDisplay() {
		dateClimbed.setText(new StringBuilder().append(mDay).append("/")
				.append(mMonth + 1).append("/").append(mYear));
	}

	public static final HashMap<String, String> classesMap = new HashMap<String, String>() {
		{
			put("M", "Munro");
			put("MT", "Munro Top");
			put("Ma", "Marilyn");
			put("twinMa", "Marilyn twin-top");
			put("sMa", "Sub-Marilyn");
			put("Mur", "Murdo");
			put("sMur", "Sub-Murdo");
			put("ssMur", "double Sub-Murdo");
			put("C", "Corbett");
			put("CT", "Corbett Tops (all)");
			put("CTM", "Corbett Top of Munro");
			put("CTC", "Corbett Top of Corbett");
			put("G", "Graham");
			put("GT", "Graham Tops (all)");
			put("GTM", "Graham Top of Munro");
			put("GTC", "Graham Top of Corbett");
			put("GTG", "Graham Top of Graham");
			put("sG", "Sub-Graham");
			put("ssG", "double Sub-Graham");
			put("D", "Donald");
			put("DT", "Donald Top");
			put("N", "Nuttall");
			put("Hew", "Hewitt");
			put("sHew", "Sub-Hewitt");
			put("ssHew", "double Sub-Hewitt");
			put("W", "Wainwright");
			put("WO", "Wainwright Outlying Fell");
			put("Dewey", "Dewey");
			put("B", "Birkett");
			put("Hu", "HuMP");
			put("twinHu", "HuMP twin-top");
			put("CoH", "Historic County Top");
			put("CoU", "Current County/UA Top");
			put("CoA", "Administrative County Top");
			put("CoL", "London Borough Top");
			put("twinCoU", "Twin Current County/UA Top");
			put("twinCoA", "Twin Administrative County Top");
			put("twinCoL", "Twin London Borough Top");
			put("xMT", "Deleted Munro Top");
			put("xMa", "Deleted Marilyn");
			put("xsMa", "Deleted Sub-Marilyn");
			put("xC", "Deleted Corbett");
			put("xCT", "Deleted Corbett Top");
			put("xDT", "Deleted Donald Top");
			put("xN", "Deleted Nuttall");
			put("x5", "Deleted Dewey");
			put("xHu", "Deleted HuMP");
			put("xCoH", "Deleted County Top");
			put("BL", "Buxton & Lewis");
			put("Bg", "Bridge");
			put("T100", "Trail 100");

		}
	};

	private void updateFromPreferences() {
		Context context = getApplicationContext();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		useMetricHeights = prefs.getBoolean(
				PreferencesActivity.PREF_METRIC_HEIGHTS, false);
	}
}
