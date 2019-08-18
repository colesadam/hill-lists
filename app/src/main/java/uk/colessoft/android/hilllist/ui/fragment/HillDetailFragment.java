package uk.colessoft.android.hilllist.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import dagger.android.support.DaggerFragment;
import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.domain.entity.Bagging;
import uk.colessoft.android.hilllist.ui.activity.BusinessSearchMapActivity;
import uk.colessoft.android.hilllist.ui.activity.DetailGMapActivity;
import uk.colessoft.android.hilllist.ui.activity.HillDetailFragmentActivity;
import uk.colessoft.android.hilllist.ui.activity.HillImagesActivity;
import uk.colessoft.android.hilllist.ui.activity.OsMapActivity;
import uk.colessoft.android.hilllist.ui.activity.PreferencesActivity;
import uk.colessoft.android.hilllist.domain.HillDetail;
import uk.colessoft.android.hilllist.ui.viewmodel.HillDetailViewModel;
import uk.colessoft.android.hilllist.ui.viewmodel.HillHoldingViewModel;
import uk.colessoft.android.hilllist.ui.viewmodel.HillListViewModel;
import uk.colessoft.android.hilllist.utility.Util;


public class HillDetailFragment extends DaggerFragment {

    static final int DATE_DIALOG_ID = 0;
    static final int MARK_HILL_CLIMBED_DIALOG = 1;

    private HillHoldingViewModel viewModel;
    private boolean useMetricHeights;
    private HillDetail selectedHill;
    private int mYear;
    private int mMonth;
    private int mDay;
    private TextView dateClimbed;
    private TextView hillnameView;
    private View viewer;
    private long thisId;
    private CheckBox ctv;

    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay();
        }
    };


    private void showMapSingle() {
        Intent intent = new Intent(getActivity(), DetailGMapActivity.class);
        intent.putExtra("rowid", thisId);
        intent.putExtra("title", "Map of " + selectedHill.getHill().getHillname());

        startActivity(intent);
    }

    private void showOSMap() {

        Intent intent = new Intent(getActivity(), OsMapActivity.class);
        intent.putExtra("x", String.valueOf(selectedHill.getHill().getXcoord()));
        intent.putExtra("y", String.valueOf(selectedHill.getHill().getYcoord()));
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
                            value + " near " + selectedHill.getHill().getHillname());
                    startActivity(intent);

                });
        alert.setNegativeButton("Cancel",
                (dialog, whichButton) -> {

                });

        AlertDialog search = alert.create();
        search.show();
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
                e.printStackTrace();
            }

            observeHillOnce("Saved");

            viewModel.markHillClimbed(new Bagging(hillDetail.getHill().getH_id(), d, notes.getText().toString()));
        });

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

                observeHillOnce("Marked hill as climbed");
                viewModel.markHillClimbed(new Bagging(selectedHill.getHill().getH_id(), new Date(), ""));

            } else {

                observeHillOnce("Marked not climbed");
                viewModel.markHillNotClimbed(selectedHill.getHill().getH_id());

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

    private void observeHillOnce(String toastMessage) {
        viewModel.getSelected().observe(getViewLifecycleOwner(), new Observer<HillDetail>() {
            @Override
            public void onChanged(HillDetail hillDetail) {
                Toast climbed;
                climbed = Toast.makeText(getActivity().getApplication(),
                        toastMessage, Toast.LENGTH_SHORT);
                climbed.show();
                viewModel.getSelected().removeObserver(this);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity parent = getActivity();
        if(parent instanceof HillDetailFragmentActivity) {
            viewModel = ViewModelProviders.of(getActivity()).get(HillDetailViewModel.class);
        }else{
            viewModel = ViewModelProviders.of(getActivity()).get(HillListViewModel.class);
        }

        final Observer<HillDetail> hillDetailObserver = new Observer<HillDetail>() {
            @Override
            public void onChanged(@Nullable final HillDetail hillDetail) {
                selectedHill = hillDetail;
                thisId = hillDetail.getHill().getH_id();
                updateHill(hillDetail);
                Log.d("ChangedData", "Hill returned:" + hillDetail.getHill().getHillname());

            }
        };

        viewModel.getSelected().observe(getActivity(), hillDetailObserver);
    }

    public void updateHill(HillDetail hillDetail) {

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


        dateClimbed = (TextView) viewer
                .findViewById(R.id.detail_date_climbed);
        ((View) dateClimbed.getParent()).setVisibility(View.GONE);

        hillnameView.setTextAppearance(getActivity(), R.style.hill_detail_title);
        if (hillDetail.getBagging() != null && !hillDetail.getBagging().isEmpty()) {

            bagFeature(hillDetail);
        }

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
            String fullClassification = Util.classesMap.get(classification);
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

        if (hillDetail.getBagging() != null && !hillDetail.getBagging().isEmpty()) {

            ctv.setChecked(true);

        } else
            ctv.setChecked(false);

    }

    private void updateDisplay() {
        dateClimbed.setText(new StringBuilder().append(mDay).append("/")
                .append(mMonth + 1).append("/").append(mYear));
    }


    private void updateFromPreferences() {
        Context context = getActivity().getApplicationContext();
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        useMetricHeights = prefs.getBoolean(
                PreferencesActivity.PREF_METRIC_HEIGHTS, false);
    }

}
