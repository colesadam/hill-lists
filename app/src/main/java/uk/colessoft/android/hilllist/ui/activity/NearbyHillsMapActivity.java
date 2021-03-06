package uk.colessoft.android.hilllist.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.ui.activity.dialogs.BaseActivity;
import uk.colessoft.android.hilllist.ui.fragment.NearbyHillsFragment;
import uk.colessoft.android.hilllist.ui.fragment.NearbyHillsMapFragment;


public class NearbyHillsMapActivity extends BaseActivity implements NearbyHillsFragment.OnLocationFoundListener, NearbyHillsFragment.OnHillSelectedListener, NearbyHillsMapFragment.HillTappedListener {
    boolean useMetricHeights;
    boolean useMetricDistances;

    double lat1;
    double lon1;
    private boolean locationSet = false;
    private LocationManager lm;
//    private NearbyHillsFragment.MyLocationListener locationListener;
    private ProgressDialog dialog;

    MenuItem dist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.nearby_hills_map_fragment);
        LayoutInflater inflater = (LayoutInflater) (this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        //mapView = (MapView) inflater.inflate(R.layout.mapview, null);

        //fView = mapView;

    }

    public void onHillSelected(long rowid) {
        NearbyHillsMapFragment fragment = (NearbyHillsMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nearby_hills_map_fragment);


        fragment.moveMarker(rowid);


    }

    public void locationFound() {
        dialog.dismiss();

    }

    public void showDialog() {


        dialog = ProgressDialog.show(this, "Please wait...",

                "Acquiring Location ...", true);
        dialog.show();

    }


    @Override
    public void hillTapped(long rowid) {

        Intent intent = new Intent(this, HillDetailActivity.class);
        intent.putExtra("rowid", rowid);
        startActivity(intent);


    }


}
