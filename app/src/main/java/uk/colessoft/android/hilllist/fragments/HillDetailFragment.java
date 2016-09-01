package uk.colessoft.android.hilllist.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.activities.BusinessSearchMapActivity;
import uk.colessoft.android.hilllist.activities.DetailGMapActivity;
import uk.colessoft.android.hilllist.activities.OsMapActivity;
import uk.colessoft.android.hilllist.activities.PreferencesActivity;
import uk.colessoft.android.hilllist.database.OldHillDbAdapter;
import uk.colessoft.android.hilllist.model.Hill;


public class HillDetailFragment extends Fragment {
	private OldHillDbAdapter dbAdapter;

	private boolean useMetricHeights;
	static final int DATE_DIALOG_ID = 0;
	static final int MARK_HILL_CLIMBED_DIALOG = 1;

	private Hill hill;
	private int mYear;
	private int mMonth;
	private int mDay;
	private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay();
		}
	};

	private TextView dateClimbed;

	private TextView hillnameView;

	private View viewer;
	
	private int thisId;


	private void showMapSingle() {
		Intent intent = new Intent(getActivity(), DetailGMapActivity.class);
		intent.putExtra("rowid",thisId);
		intent.putExtra("title", "Map of " + hill.getHillname());

		startActivity(intent);
	}
	
	private void showOSMap(){
//		Intent intent = new Intent("android.intent.action.VIEW",
//				Uri.parse(osLink));
		
		Intent intent=new Intent(getActivity(),OsMapActivity.class);
		intent.putExtra("x", String.valueOf(hill.getXcoord()));
		intent.putExtra("y", String.valueOf(hill.getYcoord()));
		startActivity(intent);
	}

	private void scootSearch(){
		final Intent intent = new Intent(getActivity(),
				BusinessSearchMapActivity.class);

		intent.putExtra("rowid", thisId);

		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

		alert.setTitle("Search");

		// Set an EditText view to get user input
		final EditText searchText = new EditText(getActivity());

		alert.setView(searchText);

		alert.setPositiveButton("Ok",
				(dialog, whichButton) -> {
                    String value = searchText.getText().toString();

                    intent.putExtra("search_string", value);
                    intent.putExtra("title",
                            value + " near " + hill.getHillname());
                    startActivity(intent);

                });
		alert.setNegativeButton("Cancel",
				(dialog, whichButton) -> {

                });

		AlertDialog search = alert.create();
		search.show();
	}


	private void noBagSenor(final Hill hill) {

		
		

		((View) dateClimbed.getParent()).setVisibility(View.GONE);
		// ((View) saveNotes.getParent().getParent()).setVisibility(View.GONE);
		hillnameView = (TextView) viewer
				.findViewById(R.id.detail_hill_name);
		hillnameView.setTextColor(getResources().getColor(R.color.white));
	}

	private void bagFeature(final Hill hill) {


		hillnameView = (TextView) viewer
				.findViewById(R.id.detail_hill_name);
		hillnameView.setTextColor(Color.GREEN);
		dateClimbed = (TextView) getActivity().findViewById(
				R.id.detail_date_climbed);
		TextView saveNotes = (TextView) getActivity().findViewById(R.id.save_notes);
		final EditText notes = (EditText) getActivity().findViewById(
				R.id.detail_hill_notes);

		((View) dateClimbed.getParent()).setVisibility(View.VISIBLE);
		// ((View)
		// saveNotes.getParent().getParent()).setVisibility(View.VISIBLE);
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date realDate = hill.getHillClimbed();
		dateClimbed.setText(format.format(realDate));
		mYear = realDate.getYear() + 1900;
		mMonth = realDate.getMonth();
		mDay = realDate.getDate();
		
	
		dateClimbed.setOnClickListener(v -> {
            DatePickerDialog d = new DatePickerDialog(getActivity(), mDateSetListener,
                    mYear, mMonth, mDay);

            d.show();
        });

		saveNotes.setOnClickListener(arg0 -> {
            SimpleDateFormat iso8601Format = new SimpleDateFormat(
                    "dd/MM/yyyy");
            Date d = new Date();
            try {
                d = iso8601Format.parse(dateClimbed.getText().toString());
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            dbAdapter.open();
            dbAdapter.markHillClimbed(hill.get_id(), d, notes.getText()
                    .toString());
            dbAdapter.close();
            Toast climbed;
            climbed = Toast.makeText(getActivity().getApplication(),
                    "Saved", Toast.LENGTH_SHORT);
            climbed.show();

        });

		notes.setText(hill.getNotes());

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		dbAdapter = new OldHillDbAdapter(getActivity());

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		dbAdapter = new OldHillDbAdapter(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		updateFromPreferences();
		viewer = inflater.inflate(R.layout.no_table_hill_detail, container,
				false);
		View gMapButton = viewer.findViewById(R.id.mapButton);
		View osMapButton = viewer.findViewById(R.id.osMapButton);
		View scootButton = viewer.findViewById(R.id.bsSearchButton);
		
		
		gMapButton.setOnClickListener(v -> showMapSingle());
		
		osMapButton.setOnClickListener(v -> showOSMap());
		
		scootButton.setOnClickListener(v -> scootSearch());

		
		
		
		return viewer;
	}



	public void updateHill(int rowid) {

		thisId=rowid;
		hillnameView = (TextView) viewer
				.findViewById(R.id.detail_hill_name);
		TextView hillheight = (TextView) viewer
				.findViewById(R.id.detail_hill_height);

		TextView hillLatitude = (TextView) viewer
				.findViewById(R.id.detail_hill_latitude);
		TextView hillLongitude = (TextView) viewer
				.findViewById(R.id.detail_hill_longitude);
		TextView osgridref = (TextView) viewer
				.findViewById(R.id.detail_hill_osgrid);
		TextView hillsection = (TextView) viewer
				.findViewById(R.id.detail_hill_section);

		TextView os50k = (TextView) viewer.findViewById(R.id.detail_os50k);

		TextView os25k = (TextView) viewer.findViewById(R.id.detail_os25k);

		TextView summitFeature = (TextView) viewer
				.findViewById(R.id.detail_hill_summit_feature);
		TextView colHeight = (TextView) viewer
				.findViewById(R.id.detail_colheight);
		TextView colGridRef = (TextView) viewer
				.findViewById(R.id.detail_colgridref);
		TextView drop = (TextView) viewer.findViewById(R.id.detail_drop);
		ViewGroup classificationLayout = (LinearLayout) viewer
				.findViewById(R.id.hill_classifications);
		classificationLayout.removeAllViews();

		dbAdapter.open();
		hill = dbAdapter.getHill(rowid);
		dateClimbed = (TextView) viewer
				.findViewById(R.id.detail_date_climbed);
		((View) dateClimbed.getParent()).setVisibility(View.GONE);

		TextView saveNotes = (TextView) getActivity().findViewById(R.id.save_notes);
		// ((View) saveNotes.getParent().getParent()).setVisibility(View.GONE);
		hillnameView.setTextColor(getResources().getColor(R.color.white));
		if (hill.getHillClimbed() != null) {

			bagFeature(hill);
		}
		String osLink = hill.getHillBagging();

		hillnameView.setText(hill.getHillname());
		if (useMetricHeights) {
			hillheight.setText(hill.getHeightm() + "m");
		} else
			hillheight.setText(hill.getHeightf() + "ft");
		hillLatitude.setText(String.valueOf(hill.getLatitude()) + "N");
		String ll = "E";
		double absL = hill.getLongitude();
		if (hill.getLongitude() < 0) {
			ll = "W";
			absL = 0 - absL;
		}
		hillLongitude.setText(String.valueOf(absL) + ll);
		osgridref.setText(hill.getGridref());
		hillsection.setText(hill.getSection());
		os50k.setText(hill.getMap());
		os25k.setText(hill.getMap25());
		colHeight.setText(String.valueOf(hill.getColheight()));
		colGridRef.setText(hill.getColgridref());
		drop.setText(String.valueOf(hill.getDrop()));

		summitFeature.setText(hill.getFeature());

		String[] sClassifications = hill.getClassification().split(",");
		TextView classificationTextView = null;
		for (String classification : sClassifications) {
			String fullClassification = classesMap.get(classification);
			classificationTextView = new TextView(getActivity());
			// classificationTextView.setBackgroundResource(R.color.white);
			classificationTextView.setText(fullClassification);
			classificationTextView.setPadding(5, 5, 5, 5);
			classificationTextView.setTextColor(getResources().getColor(
					R.color.pale_blue));

			classificationLayout.addView(classificationTextView);
		}
		if (classificationTextView != null) {

			classificationTextView.setPadding(5, 5, 5, 5);
		}
		
		
		
		CheckBox ctv = (CheckBox) viewer.findViewById(R.id.dClimbed);
		final int id = hill.get_id();
		if (hill.getHillClimbed()!=null) {

			ctv.setChecked(true);

		} else
			ctv.setChecked(false);
		ctv.setOnClickListener(null);
		ctv.setOnClickListener(v -> {
            dbAdapter.open();
            CheckBox xcv = (CheckBox) v;
            if (xcv.isChecked()) {
                Context mContext = getActivity().getApplicationContext();
                Dialog dialog = new Dialog(mContext);

                dialog.setContentView(R.layout.hill_climbed_dialog);
                dialog.setTitle("Mark Hill As Climbed");

                DatePicker datePicker = (DatePicker) dialog
                        .findViewById(R.id.dialog_climbed_date_picker);
                EditText notes = (EditText) dialog
                        .findViewById(R.id.dialog_climbed_notes);
                Button ok = (Button) dialog.findViewById(R.id.Button_ok);
                Button cancel = (Button) dialog.findViewById(R.id.Button_cancel);
                dbAdapter.open();
                dbAdapter.markHillClimbed(hill.get_id(), new Date(), "");
                Toast climbed;
                climbed = Toast.makeText(getActivity().getApplication(),
                        "Marked as Climbed", Toast.LENGTH_SHORT);
                climbed.show();
                hill = dbAdapter.getHill(hill.get_id());

                bagFeature(hill);
            } else {

                dbAdapter.markHillNotClimbed(hill.get_id());

                Toast climbed;
                climbed = Toast.makeText(getActivity().getApplication(),
                        "Marked not Climbed", Toast.LENGTH_SHORT);
                climbed.show();
                noBagSenor(hill);
            }
            dbAdapter.close();

        });

		
		
		
		
		
		dbAdapter.close();
	}

	private void updateDisplay() {
		dateClimbed.setText(new StringBuilder().append(mDay).append("/")
				.append(mMonth + 1).append("/").append(mYear));
	}

	private static final HashMap<String, String> classesMap = new HashMap<String, String>() {
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
		Context context = getActivity().getApplicationContext();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		useMetricHeights = prefs.getBoolean(
				PreferencesActivity.PREF_METRIC_HEIGHTS, false);
	}

}
