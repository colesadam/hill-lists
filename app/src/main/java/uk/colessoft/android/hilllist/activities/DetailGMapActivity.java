package uk.colessoft.android.hilllist.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.List;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.database.HillDbAdapter;
import uk.colessoft.android.hilllist.model.Hill;
import uk.colessoft.android.hilllist.model.TinyHill;
import uk.colessoft.android.hilllist.overlays.BalloonManyHillsOverlay.HillTappedListener;
import uk.colessoft.android.hilllist.overlays.BalloonManyHillsOverlay.MapOnHillSelectedListener;
import uk.colessoft.android.hilllist.utility.DistanceCalculator;

public class DetailGMapActivity extends FragmentActivity implements GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback {
    static final private int SHOW_NEARBY = Menu.FIRST;

    private HillDbAdapter dbAdapter;
    private GoogleMap map;

    private Hill hill;
    private static double nearRadius = 16.09;
    private int rowid;
    private boolean firstRun = true;

    private String title;

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
        rowid = getIntent().getExtras().getInt("rowid");
        title = getIntent().getExtras().getString("title");
        setTitle(title);
        dbAdapter = new HillDbAdapter(this);
        dbAdapter.open();
        hill = dbAdapter.getHill(rowid);
        dbAdapter.close();


    }

    private void addNearHills() {
        dbAdapter.open();
        Cursor hillsCursor = dbAdapter
                .getAllHillsCursor();

        BitmapDescriptor marker = BitmapDescriptorFactory
                .fromResource(R.drawable.yellow_hill);
        BitmapDescriptor cmarker = BitmapDescriptorFactory
                .fromResource(R.drawable.green_hill);

        double smallestLat = 90.0;
        double largestLat = -90.0;
        double smallestLong = 90.0;
        double largestLong = -90.0;
        // iterate over cursor and get hill positions
        // Make sure there is at least one row.
        if (hillsCursor.moveToFirst()) {
            // Iterate over each cursor.
            do {
                Double lat = hillsCursor.getDouble(hillsCursor
                        .getColumnIndex(HillDbAdapter.KEY_LATITUDE));
                Double lng = hillsCursor.getDouble(hillsCursor
                        .getColumnIndex(HillDbAdapter.KEY_LONGITUDE));


                double distanceKm = DistanceCalculator.CalculationByDistance(hill.getLatitude(), lat, hill.getLongitude(), lng);
                int row_id = hillsCursor.getInt(hillsCursor
                        .getColumnIndex(HillDbAdapter.KEY_ID));
                if (distanceKm < nearRadius && row_id != rowid) {
                    if (lat < smallestLat)
                        smallestLat = lat;
                    if (lat > largestLat)
                        largestLat = lat;
                    if (lng < smallestLong)
                        smallestLong = lng;
                    if (lng > largestLong)
                        largestLong = lng;

                    String hillname = hillsCursor.getString(hillsCursor
                            .getColumnIndex(HillDbAdapter.KEY_HILLNAME));
                    TinyHill tinyHill = new TinyHill();
                    tinyHill._id = row_id;
                    tinyHill.setHillname(hillname);
                    tinyHill.setLatitude(lat);
                    tinyHill.setLongitude(lng);
                    if (hillsCursor.getString(hillsCursor
                            .getColumnIndex(HillDbAdapter.KEY_DATECLIMBED)) != null) {
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
                    ).setTag(tinyHill._id);
                }

            } while (hillsCursor.moveToNext());
            final LatLngBounds bounds = new LatLngBounds(new LatLng(
                    smallestLat, smallestLong), new LatLng(largestLat,
                    largestLong));
            map.animateCamera(
                    CameraUpdateFactory
                            .newLatLngBounds(
                                    bounds, 50));
        }
        hillsCursor.close();
        dbAdapter.close();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnInfoWindowClickListener(this);
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        LatLng hillPosition = new LatLng(hill.getLatitude(), hill.getLongitude());
        Marker marker = map.addMarker(new MarkerOptions()
                .draggable(false)
                .position(hillPosition)
                .title(hill.getHillname())
                .snippet(String.valueOf(hill.getHeightm())
                        + " m"
                )
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.purple_hill))
                .anchor(0.5F, 0.5F)
        );
        marker.setTag(hill.get_id());
        marker.showInfoWindow();

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(hillPosition, 7));

        final ToggleButton mapButton = (ToggleButton) findViewById(R.id.satellite_button);
        mapButton.setChecked(true);
        mapButton.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {
                if (mapButton.isChecked()) {
                    map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                } else {
                    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }

            }

        });

    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(DetailGMapActivity.this,
                HillDetailFragmentActivity.class);

        intent.putExtra("rowid", (Integer) marker.getTag());

        startActivity(intent);

    }
}
