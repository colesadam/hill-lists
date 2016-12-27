package uk.colessoft.android.hilllist.fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.activities.Main;
import uk.colessoft.android.hilllist.activities.PreferencesActivity;
import uk.colessoft.android.hilllist.adapter.NearbyHillsAdapter;
import uk.colessoft.android.hilllist.model.TinyHill;
import uk.colessoft.android.hilllist.presenter.NearbyHillsPresenter;
import uk.colessoft.android.hilllist.views.NearbyHillsView;

public class LocationAwareFragment extends MvpFragment<NearbyHillsView, NearbyHillsPresenter> implements
        NearbyHillsView, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    //private Location mLastLocation;

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_LOCATION = 0x00001;
    private TextView mLatitudeText;
    private TextView mLongitudeText;
    private NearbyHillsAdapter nearbyHillsAdapter;
    private double nearRadius;
    private boolean useMetricHeights;
    private boolean useMetricDistances;
    private Comparator comparator;
    private final DecimalFormat df1 = new DecimalFormat("#,###,###,##0");


    @Override
    public NearbyHillsPresenter createPresenter() {
        return new NearbyHillsPresenter(mGoogleApiClient);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateFromPreferences();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


    }

    @Override
    public void onConnected(@Nullable Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                showMessageOKCancel("You need to allow access to the device location to view nearby hills",
                        (dialog, which) -> ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                MY_PERMISSIONS_REQUEST_ACCESS_LOCATION));

            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);

            }
        }
        presenter.startListening();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View viewer = inflater.inflate(R.layout.test_location, container,
                false);

        mLatitudeText = (TextView) viewer.findViewById(R.id.textView2);
        mLongitudeText = (TextView) viewer.findViewById(R.id.textView3);
        return viewer;
    }


    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.println("failed");
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .create()
                .show();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        //int index = hillListView.getSelectedItemPosition();

        switch (item.getItemId()) {
            case android.R.id.home: {
                Intent intent = new Intent(getActivity(), Main.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;

            }
            case (R.id.menu_range_10miles): {
                nearRadius = 16;
                if (useMetricDistances) {
                    getActivity().setTitle(
                            "Hills within " + df1.format(nearRadius) + "km");
                } else
                    getActivity().setTitle(
                            "Hills within " + df1.format(nearRadius / 1.601)
                                    + " miles");
                presenter.updateListOfHills(nearRadius);
                return true;
            }

            case (R.id.menu_range_25miles): {
                nearRadius = 40;
                if (useMetricDistances) {
                    getActivity().setTitle(
                            "Hills within " + df1.format(nearRadius) + "km");
                } else
                    getActivity().setTitle(
                            "Hills within " + df1.format(nearRadius / 1.601)
                                    + " miles");
                presenter.updateListOfHills(nearRadius);
                return true;

            }

            case (R.id.menu_range_50miles): {
                nearRadius = 80;
                if (useMetricDistances) {
                    getActivity().setTitle(
                            "Hills within " + df1.format(nearRadius) + "km");
                } else
                    getActivity().setTitle(
                            "Hills within " + df1.format(nearRadius / 1.601)
                                    + " miles");
                presenter.updateListOfHills(nearRadius);
                return true;

            }

            case (R.id.menu_show_climbed): {

                presenter.filter(true);
                return true;
            }

            case (R.id.menu_show_not_climbed): {
                presenter.filter(false);
                return true;
            }

            case (R.id.menu_show_all): {
                presenter.filter(null);
                return true;
            }

            case (R.id.menu_list_alpha): {

                comparator = new HillNameComparator();
                presenter.sort(comparator);
                return true;

            }
            case (R.id.menu_list_height): {

                comparator = new HillHeightComparator();
                presenter.sort(comparator);
                return true;

            }

            case (R.id.menu_by_distance): {

                comparator = new HillDistanceComparator();
                presenter.sort(comparator);
                return true;

            }
//            case (R.id.menu_show_map): {
//                Intent intent = new Intent(getActivity(),
//                        NearbyHillsMapFragmentActivity.class);
//                String[] rowIds;
//                int sindex = 0;
//                if (nearbyHills != null) {
//                    rowIds = new String[nearbyHills.size()];
//                    for (Map hs : nearbyHills) {
//                        String srow = hs.get("rowid").toString();
//                        rowIds[sindex] = srow;
//                        sindex++;
//                    }
//                } else {
//                    rowIds = new String[0];
//                }
//
//
//                intent.putExtra("rowids", rowIds);
//                intent.putExtra("title",
//                        "Hills within " + df2.format(nearRadius / 1.601)
//                                + " miles (" + nearRadius + "km)");
//
//                startActivity(intent);
//                return true;
//
//            }

            case (R.id.menu_search): {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

                alert.setTitle("Search");

                // Set an EditText view to get user input
                final EditText searchText = new EditText(getActivity());

                alert.setView(searchText);

                alert.setPositiveButton("Ok",
                        (dialog12, whichButton) -> {
                            String value = searchText.getText().toString()
                                    .toLowerCase();
                            presenter.search(value);

                        });

                alert.setNegativeButton("Cancel",
                        (dialog1, whichButton) -> {

                        });

                AlertDialog search = alert.create();
                search.show();
                return true;
            }
        }
        return false;

    }


    @Override
    public void listChanged(List<TinyHill> nearbyHills) {
        nearbyHillsAdapter.setHills(nearbyHills);
        nearbyHillsAdapter.notifyDataSetChanged();

        Fragment fragment2 = getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.hill_detail_fragment);

        HillDetailFragment fragment = (HillDetailFragment) getActivity()
                .getSupportFragmentManager().findFragmentById(
                        R.id.hill_detail_fragment);

        if (fragment != null && fragment.isInLayout()) {
            if (nearbyHills != null && nearbyHills.size() > 0) {
                // hillSelectedListener.onHillSelected((Integer) (nearbyHills
                //         .get(0)).get("rowid"));
            }
        }
    }

    private static class HillHeightComparator implements Comparator {

        public int compare(Object hill1, Object hill2) {

            Float height1 = ((TinyHill) hill1).getHeightM();
            Float height2 = ((TinyHill) hill2).getHeightM();
            return (height2.compareTo(height1));
        }

    }

    private static class HillDistanceComparator implements Comparator {

        public int compare(Object hill1, Object hill2) {

            Double distance1 = ((TinyHill) hill1).getDistance();
            Double distance2 = ((TinyHill) hill2).getDistance();
            return (distance2.compareTo(distance1));
        }

    }

    private static class HillNameComparator implements Comparator {

        public int compare(Object hill1, Object hill2) {

            String name1 = ((TinyHill) hill1).getHillname();
            String name2 = ((TinyHill) hill2).getHillname();
            return (name1.compareTo(name2));
        }

    }

}
