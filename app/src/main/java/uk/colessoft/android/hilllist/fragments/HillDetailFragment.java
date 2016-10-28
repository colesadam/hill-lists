package uk.colessoft.android.hilllist.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import uk.colessoft.android.hilllist.BHApplication;
import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.activities.BusinessSearchMapActivity;
import uk.colessoft.android.hilllist.activities.DetailGMapActivity;
import uk.colessoft.android.hilllist.activities.HillImagesActivity;
import uk.colessoft.android.hilllist.activities.OsMapActivity;
import uk.colessoft.android.hilllist.activities.PreferencesActivity;
import uk.colessoft.android.hilllist.components.DaggerHillDetailComponent;
import uk.colessoft.android.hilllist.components.HillDetailComponent;
import uk.colessoft.android.hilllist.database.DbHelper;
import uk.colessoft.android.hilllist.model.Hill;
import uk.colessoft.android.hilllist.presenter.HillDetailPresenter;
import uk.colessoft.android.hilllist.presenter.HillDetailPresenter_Factory;
import uk.colessoft.android.hilllist.views.HillDetailView;

import static android.content.ContentValues.TAG;


public class HillDetailFragment extends MvpFragment<HillDetailView, HillDetailPresenter> implements HillDetailView {

    static final int DATE_DIALOG_ID = 0;
    static final int MARK_HILL_CLIMBED_DIALOG = 1;
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


    private boolean useMetricHeights;
    private Hill hill;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int thisId;

    @BindView(R.id.detail_date_climbed)
    public TextView dateClimbed;
    @BindView(R.id.detail_hill_name)
    public TextView hillnameView;
    private CheckBox ctv;
    @BindView(R.id.detail_hill_height)
    public TextView hillheight;
    @BindView(R.id.detail_hill_latitude)
    public TextView hillLatitude;
    @BindView(R.id.detail_hill_longitude)
    public TextView hillLongitude;
    @BindView(R.id.detail_hill_osgrid)
    public TextView osgridref;
    @BindView(R.id.detail_hill_section)
    public TextView hillsection;
    @BindView(R.id.detail_os50k)
    public TextView os50k;
    @BindView(R.id.detail_os25k)
    public TextView os25k;
    @BindView(R.id.detail_hill_summit_feature)
    public TextView summitFeature;
    @BindView(R.id.detail_colheight)
    public TextView colHeight;
    @BindView(R.id.detail_colgridref)
    public TextView colGridRef;
    @BindView(R.id.detail_drop)
    public TextView drop;
    @BindView(R.id.hill_classifications)
    public ViewGroup classificationLayout;
    @BindView(R.id.hill_detail_toolbar)
    public Toolbar toolbar;
    @BindView(R.id.save_notes)
    public TextView saveNotes;
    @BindView(R.id.detail_hill_notes)
    public EditText notesText;

    @OnClick(R.id.save_notes)
    public void saveNotes(Button notes) {
        Date d = new Date();
        try {
            d = iso8601Format.parse(dateClimbed.getText().toString());
        } catch (ParseException e) {
            Log.e(TAG, "Couldn't parse date", e);
        }
        hill.setNotes(notesText.getText().toString());
        presenter.markHillClimbed(hill.get_id(), d, notesText.getText()
                .toString(), "Saved");
    }

    @OnClick(R.id.detail_date_climbed)
    public void date(View view) {
        DatePickerDialog d = new DatePickerDialog(getActivity(), mDateSetListener,
                mYear, mMonth, mDay);

        d.show();
    }

    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay();
        }
    };
    private SimpleDateFormat iso8601Format = new SimpleDateFormat(
            "dd/MM/yyyy");


    private void showMapSingle() {
        Intent intent = new Intent(getActivity(), DetailGMapActivity.class);
        intent.putExtra("rowid", thisId);
        intent.putExtra("title", "Map of " + hill.getHillname());

        startActivity(intent);
    }

    private void showOSMap() {

        Intent intent = new Intent(getActivity(), OsMapActivity.class);
        intent.putExtra("x", String.valueOf(hill.getXcoord()));
        intent.putExtra("y", String.valueOf(hill.getYcoord()));
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
        hillnameView.setTextAppearance(getActivity(), R.style.hill_detail_title);
    }

    private void bagFeature(final Hill hill) {

        hillnameView.setTextColor(getResources().getColor(R.color.light_green));

        ((View) dateClimbed.getParent()).setVisibility(View.VISIBLE);

        Date realDate = hill.getHillClimbed();
        dateClimbed.setText(iso8601Format.format(realDate));
        mYear = realDate.getYear() + 1900;
        mMonth = realDate.getMonth();
        mDay = realDate.getDate();
        notesText.setText(hill.getNotes());

    }

    @Override
    public HillDetailPresenter createPresenter() {
        return ((BHApplication) getActivity().getApplication()).getHillDetailComponent().presenter();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BHApplication) getActivity().getApplication()).getDbComponent().inject(this);
        updateFromPreferences();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewer = inflater.inflate(R.layout.no_table_hill_detail, container,
                false);

        ButterKnife.bind(this, viewer);
        classificationLayout.removeAllViews();
        toolbar.inflateMenu(R.menu.hill_detail_menu);
        ctv = (CheckBox) MenuItemCompat.getActionView(toolbar.getMenu().findItem(R.id.menuShowDue));
        ctv.setOnCheckedChangeListener(hillClimbedOnCheckedChangeListener());

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
                        intent.putExtra("hillId", Long.valueOf(thisId));
                        startActivity(intent);
                    }
                }
                return false;
            }
        });
        return viewer;
    }

    @NonNull
    private CompoundButton.OnCheckedChangeListener hillClimbedOnCheckedChangeListener() {
        return (compoundButton, isChecked) -> {
            if (isChecked) {
                Date now = new Date();
                String notes = "";
                hill.setDateClimbed(now);
                presenter.markHillClimbed(hill.get_id(), now, notes, "Marked as Climbed");
            } else {
                presenter.markHillNotClimbed(hill.get_id());
            }
        };
    }

    public void updateHill(int rowid) {
        thisId = rowid;
        presenter.getHill(rowid);
    }


    @Override
    public void updateHill(Hill hill) {

        this.hill = hill;
        ((View) dateClimbed.getParent()).setVisibility(View.GONE);

        hillnameView.setTextAppearance(getActivity(), R.style.hill_detail_title);
        if (hill.getHillClimbed() != null) {

            bagFeature(hill);
        }

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

        String[] sClassifications = hill.getClassification().replace("\"", "").split(",");
        TextView classificationTextView = null;
        for (String classification : sClassifications) {
            String fullClassification = classesMap.get(classification);
            if (fullClassification != null) {
                classificationTextView = new TextView(getActivity());
                classificationTextView.setText(fullClassification);
                classificationTextView.setPadding(5, 5, 5, 5);
                classificationTextView.setTextAppearance(getActivity(), R.style.hill_detail_text);

                classificationLayout.addView(classificationTextView);
            }
        }
        if (classificationTextView != null) {

            classificationTextView.setPadding(5, 5, 5, 5);
        }

        if (hill.getHillClimbed() != null) {
            ctv.setOnCheckedChangeListener(null);
            ctv.setChecked(true);
            ctv.setOnCheckedChangeListener(hillClimbedOnCheckedChangeListener());
            bagFeature(hill);

        } else {
            ctv.setOnCheckedChangeListener(null);
            ctv.setChecked(false);
            ctv.setOnCheckedChangeListener(hillClimbedOnCheckedChangeListener());
        }
    }

    @Override
    public void hillMarkedClimbed(boolean succeeded, String message) {
        Toast climbed = Toast.makeText(getActivity().getApplication(),
                message, Toast.LENGTH_SHORT);
        climbed.show();
        bagFeature(hill);
    }

    @Override
    public void hillMarkedUnclimbed(boolean succeeded) {
        Toast climbed = Toast.makeText(getActivity().getApplication(),
                "Marked not Climbed", Toast.LENGTH_SHORT);
        climbed.show();
        noBagSenor(hill);
    }

    private void updateDisplay() {
        dateClimbed.setText(new StringBuilder().append(mDay).append("/")
                .append(mMonth + 1).append("/").append(mYear));
        Date d = new Date();
        try {
            d = iso8601Format.parse(dateClimbed.getText().toString());
        } catch (ParseException e) {
            Log.e(TAG, "Couldn't parse date", e);
        }
        hill.setDateClimbed(d);
        presenter.markHillClimbed(hill.get_id(),d,notesText.getText().toString(),"Date Updated");

    }

    private void updateFromPreferences() {
        Context context = getActivity().getApplicationContext();
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        useMetricHeights = prefs.getBoolean(
                PreferencesActivity.PREF_METRIC_HEIGHTS, false);
    }

}
