package uk.colessoft.android.hilllist.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.core.content.ContextCompat;
import androidx.loader.content.Loader;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.dao.IsHillClimbed;
import uk.colessoft.android.hilllist.domain.HillDetail;
import uk.colessoft.android.hilllist.ui.activity.Main;
import uk.colessoft.android.hilllist.ui.activity.NearbyHillsMapActivity;
import uk.colessoft.android.hilllist.ui.activity.PreferencesActivity;
import uk.colessoft.android.hilllist.database.BritishHillsDatasource;
import uk.colessoft.android.hilllist.database.HillsTables;
import uk.colessoft.android.hilllist.ui.adapter.HillDetailListAdapter;
import uk.colessoft.android.hilllist.ui.viewmodel.HillListViewModel;
import uk.colessoft.android.hilllist.ui.viewmodel.NearbyHillListViewModel;
import uk.colessoft.android.hilllist.ui.viewmodel.NearbyHillsOrder;
import uk.colessoft.android.hilllist.utility.DistanceCalculator;

import static android.content.ContentValues.TAG;

public class NearbyHillsFragment extends DaggerFragment {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0x00001;
    private SimpleAdapter nearbyHillsAdapter;
    private NearbyHillListViewModel viewModel;


    public interface OnHillSelectedListener {
        void onHillSelected(long rowid);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private int firstRow;

    @Inject
    BritishHillsDatasource dbAdapter;

    private View viewer;
    private ListView hillListView;
    private LocationManager lm;
    private List<Map<String, ?>> nearbyHills;
    private double lat1;
    private double lon1;
    private float nearRadius = 16.093F;
    private final DecimalFormat df2 = new DecimalFormat("#,###,###,##0.0");
    private final DecimalFormat df1 = new DecimalFormat("#,###,###,##0");
    private final DecimalFormat df3 = new DecimalFormat();
    private MenuItem dist;
    private boolean useMetricHeights;
    private boolean useMetricDistances;
    private String orderBy = "distance";
    private ProgressDialog dialog;
    private final int ALL_HILLS = 0;
    private int filterHills = ALL_HILLS;
    private boolean searched = false;
    private boolean locationSet = false;
    private ArrayList<Map<String, ?>> backedUpHills;
    private OnHillSelectedListener hillSelectedListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = ViewModelProviders.of(getActivity()).get(NearbyHillListViewModel.class);

        final Observer<List<Pair<Double,HillDetail>>> nameObserver = newHills -> {

            Log.d("ChangedData", "Nearby Hills returned:" + newHills.size());
            nearbyHills = newHills.stream().map(hillDetail -> {
                HashMap<String, Object> hillExtract = new HashMap();
                hillExtract.put("hillname", hillDetail.second.getHill().getHillname());
                if (useMetricHeights) {
                    hillExtract.put("height", hillDetail.second.getHill().getHeightm());
                } else {
                    hillExtract.put("height", hillDetail.second.getHill().getHeightf());
                }
                hillExtract.put("rowid", hillDetail.second.getHill().getH_id());

                if (useMetricDistances) {
                    hillExtract.put("distance",
                            Double.parseDouble(df2.format(hillDetail.first)));
                } else {
                    hillExtract.put("distance",
                            Double.parseDouble(df2.format(hillDetail.first / 1.601)));
                }
                return hillExtract;
            }).collect(Collectors.toList());
            updateList();

        };

        viewModel.getHills().observe(getActivity(), nameObserver);



        try {
            hillSelectedListener = (OnHillSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHillSelectedListener");
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.hill_lists_menu, menu);
        dist = menu.findItem(R.id.menu_by_distance);
        dist.setVisible(true);
        menu.findItem(R.id.menu_set_range).setVisible(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        int index = hillListView.getSelectedItemPosition();

        switch (item.getItemId()) {
            case android.R.id.home: {
                // app icon in Action Bar clicked; go home
                Intent intent = new Intent(getActivity(), Main.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;

            }
            case (R.id.menu_range_10miles): {
                nearRadius = 16.093F;
                if (useMetricDistances) {
                    getActivity().setTitle(
                            "Hills within " + df1.format(nearRadius) + "km");
                } else
                    getActivity().setTitle(
                            "Hills within " + df1.format(nearRadius / 1.601)
                                    + " miles");
                viewModel.filterDistance(nearRadius);
                return true;
            }

            case (R.id.menu_range_25miles): {
                nearRadius = 40.234F;
                if (useMetricDistances) {
                    getActivity().setTitle(
                            "Hills within " + df1.format(nearRadius) + "km");
                } else
                    getActivity().setTitle(
                            "Hills within " + df1.format(nearRadius / 1.601)
                                    + " miles");
                viewModel.filterDistance(nearRadius);
                return true;

            }

            case (R.id.menu_range_50miles): {
                nearRadius = 80.467F;
                if (useMetricDistances) {
                    getActivity().setTitle(
                            "Hills within " + df1.format(nearRadius) + "km");
                } else
                    getActivity().setTitle(
                            "Hills within " + df1.format(nearRadius / 1.601)
                                    + " miles");
                viewModel.filterDistance(nearRadius);
                return true;
            }

            case (R.id.menu_show_climbed): {
                viewModel.filterClimbed(IsHillClimbed.YES);
                return true;
            }

            case (R.id.menu_show_not_climbed): {
                viewModel.filterClimbed(IsHillClimbed.NO);
                return true;
            }

            case (R.id.menu_show_all): {
                viewModel.filterClimbed(null);
                return true;
            }

            case (R.id.menu_list_alpha): {
                viewModel.orderHills(NearbyHillsOrder.NAME_ASC);
                return true;
            }

            case (R.id.menu_list_height): {
                viewModel.orderHills(NearbyHillsOrder.HEIGHT_DESC);
                return true;
            }

            case (R.id.menu_by_distance): {
                viewModel.orderHills(NearbyHillsOrder.DIST_ASC);
                return true;
            }
            case (R.id.menu_show_map): {
                Intent intent = new Intent(getActivity(),
                        NearbyHillsMapActivity.class);
                String[] rowIds;
                int sindex = 0;
                if (nearbyHills != null) {
                    rowIds = new String[nearbyHills.size()];
                    for (Map hs : nearbyHills) {
                        String srow = hs.get("rowid").toString();
                        rowIds[sindex] = srow;
                        sindex++;
                    }
                } else {
                    rowIds = new String[0];
                }


                intent.putExtra("rowids", rowIds);
                intent.putExtra("title",
                        "Hills within " + df2.format(nearRadius / 1.601)
                                + " miles (" + nearRadius + "km)");

                startActivity(intent);
                return true;

            }

            case (R.id.menu_search): {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

                alert.setTitle("Search");

                final EditText searchText = new EditText(getActivity());
                alert.setView(searchText);

                alert.setPositiveButton("Ok",
                        (dialog12, whichButton) -> {
                            String value = searchText.getText().toString()
                                    .toLowerCase();
                            viewModel.searchHills(value);

                        });

                alert.setNegativeButton("Cancel",
                        (dialog1, whichButton) -> {

                        });

                AlertDialog search = alert.create();
                search.show();
                return true;
            }

            case (R.id.menu_all_climbed): {
                new AlertDialog.Builder(getContext())
                        .setTitle("Mark all hills in list as climbed?")
                        .setMessage("THIS WILL OVERWRITE ALL BAGGING DATA FOR THESE HILLS.")
                        .setPositiveButton("Go ahead", (dialog, which) -> {
                            viewModel.markAllHillsClimbed();
                            Log.d("HillListFragment", "Marking all hills climbed");
                        })
                        .setNegativeButton("Don't do it", (dialog, which) ->
                                Log.d("HillListFragment", "Aborting bagger all..."))
                        .show();
                return true;
            }

            case (R.id.menu_none_climbed): {
                new AlertDialog.Builder(getContext())
                        .setTitle("Mark all hills in list as unclimbed?")
                        .setMessage("THIS WILL DELETE ALL BAGGING DATA FOR THESE HILLS.")
                        .setPositiveButton("Go ahead", (dialog, which) -> {
                            viewModel.markAllHillsNotClimbed();
                            Log.d("HillListFragment", "Marking all hills unclimbed");
                        })
                        .setNegativeButton("Don't do it", (dialog, which) ->
                                Log.d("HillListFragment", "Aborting unbagger all..."))
                        .show();
                return true;
            }
        }
        return false;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewer = inflater.inflate(R.layout.list_hills_legacy, container, false);
        hillListView = (ListView) viewer
                .findViewById(R.id.myListViewLegacy);

        return viewer;
    }


    private final Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            locationProgress.setVisibility(View.GONE);


        }
    };
    private View locationProgress;

    @Override
    public void onResume() {

        super.onResume();
        updateFromPreferences();
    }

    private void updateList() {
        nearbyHillsAdapter = new SimpleAdapter(getActivity(),
                nearbyHills,
                R.layout.nearby_hill_item, new String[]{"hillname",
                "distance", "height",}, new int[]{
                R.id.hillname_entry, R.id.distance_entry,
                R.id.height_entry}) {

            @Override
            public void setViewText(TextView v, String text) {
                switch (v.getId()) {
                    case R.id.distance_entry: {
                        if (useMetricDistances) {
                            text = convText(v, text, df2) + " km";
                        } else {
                            text = convText(v, text, df2) + " miles";
                        }
                        break;
                    }
                    case R.id.height_entry: {
                        if (useMetricHeights) {
                            text = convText(v, text, df3) + "m";
                        } else
                            text = convText(v, text, df3) + "ft";
                        break;
                    }
                }
                super.setViewText(v, text);
            }

        };
        hillListView.setAdapter(nearbyHillsAdapter);
        hillListView.setOnItemClickListener((parent, view, pos, id) -> {
            HashMap entry = (HashMap) nearbyHills.get(pos);
            long rowId = (Long) entry.get("rowid");

            hillSelectedListener.onHillSelected(rowId);

        });


        HillDetailFragment fragment = (HillDetailFragment) getActivity()
                .getSupportFragmentManager().findFragmentById(
                        R.id.hill_detail_fragment);

        if (fragment != null && fragment.isInLayout()) {
            if (nearbyHills != null && nearbyHills.size() > 0) {
                hillSelectedListener.onHillSelected((Long) (nearbyHills
                        .get(0)).get("rowid"));
            }
        }
    }

    public interface OnLocationFoundListener {
        void locationFound();

        void showDialog();
    }


    private String convText(TextView v, String text, DecimalFormat df) {

        double dblAmt;
        dblAmt = Double.valueOf(text);
        return df.format(dblAmt);

    }

    private void updateFromPreferences() {
        Context context = getActivity().getApplicationContext();
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        useMetricHeights = prefs.getBoolean(
                PreferencesActivity.PREF_METRIC_HEIGHTS, false);
        useMetricDistances = prefs.getBoolean(
                PreferencesActivity.PREF_METRIC_DISTANCES, false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {

                Intent intent = getActivity().getIntent();
                getActivity().finish();
                startActivity(intent);
                break;

            }
            default:{
                Intent intent = new Intent(getActivity(),Main.class);
                startActivity(intent);
            }

        }

    }
}


