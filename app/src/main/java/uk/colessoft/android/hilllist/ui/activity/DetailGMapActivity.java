package uk.colessoft.android.hilllist.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.ui.activity.dialogs.BaseActivity;
import uk.colessoft.android.hilllist.database.BritishHillsDatasource;
import uk.colessoft.android.hilllist.database.HillsTables;
import uk.colessoft.android.hilllist.domain.entity.Bagging;
import uk.colessoft.android.hilllist.domain.HillDetail;
import uk.colessoft.android.hilllist.domain.TinyHill;
import uk.colessoft.android.hilllist.utility.DistanceCalculator;
import uk.colessoft.android.hilllist.utility.LatLangBounds;

public class DetailGMapActivity extends BaseActivity implements GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback {
    static final private int SHOW_NEARBY = Menu.FIRST;

    @Inject
    BritishHillsDatasource dbAdapter;
    private GoogleMap map;

    private HillDetail hillDetail;
    private long rowid;
    private boolean firstRun = true;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu);
        MenuItem show_gmap = menu.add(0, SHOW_NEARBY, Menu.NONE,
                "Show Nearby Hills");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        super.onOptionsItemSelected(item);


        switch (item.getItemId()) {
            case (SHOW_NEARBY): {

                addNearHills();

                return true;
            }
            case android.R.id.home:
                // app icon in Action Bar clicked; go home
                Intent intent = new Intent(this, Main.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;

        }
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.many_hills_map_v2);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        rowid = getIntent().getExtras().getLong("rowid");
        String title = getIntent().getExtras().getString("title");
        setTitle(title);
        hillDetail = dbAdapter.getHill(rowid);


    }

    private void addNearHills() {
        Cursor hillsCursor = dbAdapter
                .getAllHillsCursor();

        BitmapDescriptor marker = BitmapDescriptorFactory
                .fromResource(R.drawable.yellow_hill);
        BitmapDescriptor cmarker = BitmapDescriptorFactory
                .fromResource(R.drawable.green_hill);
        LatLangBounds llb = new LatLangBounds();
        // iterate over cursor and get hillDetail positions
        // Make sure there is at least one row.
        if (hillsCursor.moveToFirst()) {
            // Iterate over each cursor.
            do {
                Double lat = hillsCursor.getDouble(hillsCursor
                        .getColumnIndex(HillsTables.KEY_LATITUDE));
                Double lng = hillsCursor.getDouble(hillsCursor
                        .getColumnIndex(HillsTables.KEY_LONGITUDE));


                double distanceKm = DistanceCalculator.calculationByDistance(hillDetail.getHill().getLatitude(), lat, hillDetail.getHill().getLongitude(), lng);
                int row_id = hillsCursor.getInt(hillsCursor
                        .getColumnIndex(HillsTables.KEY_HILL_ID));
                double nearRadius = 16.09;
                if (distanceKm < nearRadius && row_id != rowid) {
                    llb.addLatLong(lat, lng);

                    String hillname = hillsCursor.getString(hillsCursor
                            .getColumnIndex(HillsTables.KEY_HILLNAME));
                    TinyHill tinyHill = new TinyHill();
                    tinyHill.set_id(row_id);
                    tinyHill.setHillname(hillname);
                    tinyHill.setLatitude(lat);
                    tinyHill.setLongitude(lng);
                    if (hillsCursor.getString(hillsCursor
                            .getColumnIndex(Bagging.KEY_DATECLIMBED)) != null) {
                        tinyHill.setClimbed(true);
                    } else {
                        tinyHill.setClimbed(false);
                    }

                    BitmapDescriptor hillDescriptor;
                    if (tinyHill.isClimbed()) {
                        hillDescriptor = cmarker;
                    } else hillDescriptor = marker;
                    LatLng hillPosition = new LatLng(tinyHill.getLatitude(), tinyHill.getLongitude());
                    map.addMarker(new MarkerOptions()
                            .draggable(false)
                            .position(hillPosition)
                            .title(tinyHill.getHillname()

                            )
                            .icon(hillDescriptor)
                            .anchor(0.5F, 0.5F)
                    ).setTag(tinyHill.get_id());
                }

            } while (hillsCursor.moveToNext());
            final LatLngBounds bounds = new LatLngBounds(new LatLng(
                    llb.getSmallestLat(), llb.getSmallestLong()), new LatLng(llb.getLargestLat(),
                    llb.getLargestLong()));
            map.animateCamera(
                    CameraUpdateFactory
                            .newLatLngBounds(
                                    bounds, 50));
        }
        hillsCursor.close();


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnInfoWindowClickListener(this);
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        LatLng hillPosition = new LatLng(hillDetail.getHill().getLatitude(), hillDetail.getHill().getLongitude());
        Marker marker = map.addMarker(new MarkerOptions()
                .draggable(false)
                .position(hillPosition)
                .title(hillDetail.getHill().getHillname())
                .snippet(String.valueOf(hillDetail.getHill().getHeightm())
                        + " m"
                )
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.purple_hill))
                .anchor(0.5F, 0.5F)
        );
        marker.setTag(hillDetail.getHill().getH_id());
        marker.showInfoWindow();

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(hillPosition, 7));

        final ToggleButton mapButton = (ToggleButton) findViewById(R.id.satellite_button);
        mapButton.setChecked(true);
        mapButton.setOnClickListener(v -> {
            if (mapButton.isChecked()) {
                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            } else {
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }

        });

    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(DetailGMapActivity.this,
                HillDetailActivity.class);

        intent.putExtra("rowid",  (Long)marker.getTag());

        startActivity(intent);

    }
}
