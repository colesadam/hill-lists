package uk.colessoft.android.hilllist.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.MenuItem;
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

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import uk.colessoft.android.hilllist.BHApplication;
import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.activity.BusinessSearchMapActivity;
import uk.colessoft.android.hilllist.activity.DetailGMapActivity;
import uk.colessoft.android.hilllist.activity.HillImagesActivity;
import uk.colessoft.android.hilllist.activity.OsMapActivity;
import uk.colessoft.android.hilllist.activity.PreferencesActivity;
import uk.colessoft.android.hilllist.database.BritishHillsDatasource;
import uk.colessoft.android.hilllist.model.HillDetail;


public class HillDetailFragment extends DaggerFragment {
    @Inject
    BritishHillsDatasource dbAdapter;

    private boolean useMetricHeights;
    static final int DATE_DIALOG_ID = 0;
    static final int MARK_HILL_CLIMBED_DIALOG = 1;

    private HillDetail hillDetail;
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
    private CheckBox ctv;


    private void showMapSingle() {
        Intent intent = new Intent(getActivity(), DetailGMapActivity.class);
        intent.putExtra("rowid", thisId);
        intent.putExtra("title", "Map of " + hillDetail.getHill().getHillname());

        startActivity(intent);
    }

    private void showOSMap() {
//		Intent intent = new Intent("android.intent.action.VIEW",
//				Uri.parse(osLink));

        Intent intent = new Intent(getActivity(), OsMapActivity.class);
        intent.putExtra("x", String.valueOf(hillDetail.getHill().getXcoord()));
        intent.putExtra("y", String.valueOf(hillDetail.getHill().getYcoord()));
        startActivity(intent);
    }

    private void scootSearch() {
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
                            value + " near " + hillDetail.getHill().getHillname());
                    startActivity(intent);

                });
        alert.setNegativeButton("Cancel",
                (dialog, whichButton) -> {

                });

        AlertDialog search = alert.create();
        search.show();
    }


    private void noBagSenor(final HillDetail hillDetail) {


        ((View) dateClimbed.getParent()).setVisibility(View.GONE);
        // ((View) saveNotes.getParent().getParent()).setVisibility(View.GONE);
        hillnameView = (TextView) viewer
                .findViewById(R.id.detail_hill_name);
        hillnameView.setTextAppearance(getActivity(), R.style.hill_detail_title);
    }

    private void bagFeature(final HillDetail hillDetail) {


        hillnameView = (TextView) viewer
                .findViewById(R.id.detail_hill_name);
        hillnameView.setTextColor(getResources().getColor(R.color.light_green));
        dateClimbed = (TextView) getActivity().findViewById(
                R.id.detail_date_climbed);
        TextView saveNotes = (TextView) getActivity().findViewById(R.id.save_notes);
        final EditText notes = (EditText) getActivity().findViewById(
                R.id.detail_hill_notes);

        ((View) dateClimbed.getParent()).setVisibility(View.VISIBLE);
        // ((View)
        // saveNotes.getParent().getParent()).setVisibility(View.VISIBLE);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date realDate = hillDetail.getBagging().get(0).getDateClimbed();
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

            dbAdapter.markHillClimbed(hillDetail.getHill().getH_id(), d, notes.getText()
                    .toString());

            Toast climbed;
            climbed = Toast.makeText(getActivity().getApplication(),
                    "Saved", Toast.LENGTH_SHORT);
            climbed.show();

        });

        notes.setText(hillDetail.getBagging().get(0).getNotes());

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        HillDetailViewModel viewModel = ViewModelProviders.of(this, hillDetailViewModelFactory.create(userId))
//                .get(HillDetailViewModel.class);
//        long hillId = getActivity().get
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        updateFromPreferences();
        viewer = inflater.inflate(R.layout.no_table_hill_detail, container,
                false);

        Toolbar toolbar = (Toolbar) viewer.findViewById(R.id.hill_detail_toolbar);
        toolbar.inflateMenu(R.menu.hill_detail_menu);
        ctv = (CheckBox) MenuItemCompat.getActionView(toolbar.getMenu().findItem(R.id.menuShowDue));
        ctv.setOnClickListener(null);

        ctv.setOnClickListener(v -> {

            CheckBox xcv = (CheckBox) v;
            if (xcv.isChecked()) {
                Context mContext = getActivity().getApplicationContext();
                Dialog dialog = new Dialog(mContext);

                dialog.setContentView(R.layout.hill_climbed_dialog);
                dialog.setTitle("Mark HillDetail As Climbed");

                DatePicker datePicker = (DatePicker) dialog
                        .findViewById(R.id.dialog_climbed_date_picker);
                EditText notes = (EditText) dialog
                        .findViewById(R.id.dialog_climbed_notes);
                Button ok = (Button) dialog.findViewById(R.id.Button_ok);
                Button cancel = (Button) dialog.findViewById(R.id.Button_cancel);

                dbAdapter.markHillClimbed(hillDetail.getHill().getH_id(), new Date(), "");
                Toast climbed;
                climbed = Toast.makeText(getActivity().getApplication(),
                        "Marked as Climbed", Toast.LENGTH_SHORT);
                climbed.show();
                hillDetail = dbAdapter.getHill(hillDetail.getHill().getH_id());

                bagFeature(hillDetail);
            } else {

                dbAdapter.markHillNotClimbed(hillDetail.getHill().getH_id());

                Toast climbed;
                climbed = Toast.makeText(getActivity().getApplication(),
                        "Marked not Climbed", Toast.LENGTH_SHORT);
                climbed.show();
                noBagSenor(hillDetail);
            }

        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_show_map: {
                        showMapSingle();
                        return true;
                    }
                    case R.id.os_map: {
                        showOSMap();
                        return true;
                    }
                    case R.id.scoot: {
                        scootSearch();
                        return true;
                    }
                    case R.id.images: {
                        Intent intent = new Intent(getActivity(), HillImagesActivity.class);
                        intent.putExtra("hillId",Long.valueOf(thisId));
                        startActivity(intent);

                    }

                }
                return false;
            }
        });
        return viewer;
    }


    public void updateHill(int rowid) {

        thisId = rowid;
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
        System.out.println(rowid);
        hillDetail = dbAdapter.getHill(rowid);
        dateClimbed = (TextView) viewer
                .findViewById(R.id.detail_date_climbed);
        ((View) dateClimbed.getParent()).setVisibility(View.GONE);

        TextView saveNotes = (TextView) getActivity().findViewById(R.id.save_notes);
        // ((View) saveNotes.getParent().getParent()).setVisibility(View.GONE);
        hillnameView.setTextAppearance(getActivity(), R.style.hill_detail_title);
        if (hillDetail.getBagging().get(0).getDateClimbed() != null) {

            bagFeature(hillDetail);
        }
        String osLink = hillDetail.getHill().getHillBagging();

        hillnameView.setText(hillDetail.getHill().getHillname());
        if (useMetricHeights) {
            hillheight.setText(hillDetail.getHill().getHeightm() + "m");
        } else
            hillheight.setText(hillDetail.getHill().getHeightf() + "ft");
        hillLatitude.setText(String.valueOf(hillDetail.getHill().getLatitude()) + "N");
        String ll = "E";
        double absL = hillDetail.getHill().getLongitude();
        if (hillDetail.getHill().getLongitude() < 0) {
            ll = "W";
            absL = 0 - absL;
        }
        hillLongitude.setText(String.valueOf(absL) + ll);
        osgridref.setText(hillDetail.getHill().getGridref());
        hillsection.setText(hillDetail.getHill().getHSection());
        os50k.setText(hillDetail.getHill().getMap());
        os25k.setText(hillDetail.getHill().getMap25());
        colHeight.setText(String.valueOf(hillDetail.getHill().getColheight()));
        colGridRef.setText(hillDetail.getHill().getColgridref());
        drop.setText(String.valueOf(hillDetail.getHill().getDrop()));

        summitFeature.setText(hillDetail.getHill().getFeature());

        String[] sClassifications = hillDetail.getHill().getClassification().replace("\"", "").split(",");
        TextView classificationTextView = null;
        for (String classification : sClassifications) {
            String fullClassification = classesMap.get(classification);
            if (fullClassification != null) {
                classificationTextView = new TextView(getActivity());
                // classificationTextView.setBackgroundResource(R.color.white);
                classificationTextView.setText(fullClassification);
                classificationTextView.setPadding(5, 5, 5, 5);
                classificationTextView.setTextAppearance(getActivity(), R.style.hill_detail_text);

                classificationLayout.addView(classificationTextView);
            }
        }
        if (classificationTextView != null) {

            classificationTextView.setPadding(5, 5, 5, 5);
        }


        final long id = hillDetail.getHill().getH_id();
        if (hillDetail.getBagging().get(0).getDateClimbed() != null) {

            ctv.setChecked(true);

        } else
            ctv.setChecked(false);


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
