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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
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

public class DetailGMapActivity extends FragmentActivity implements OnMapReadyCallback, MapOnHillSelectedListener, HillTappedListener {
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

        List<TinyHill> hillPoints = new ArrayList<TinyHill>();
        Drawable marker = getResources().getDrawable(R.drawable.yellow_hill);
        Drawable cmarker = getResources().getDrawable(R.drawable.green_hill);
        marker.setBounds(0, 0, marker.getIntrinsicWidth(),
                marker.getIntrinsicHeight());
        cmarker.setBounds(0, 0, cmarker.getIntrinsicWidth(),
                cmarker.getIntrinsicHeight());

        // iterate over cursor and get hill positions
        // Make sure there is at least one row.
        if (hillsCursor.moveToFirst()) {
            // Iterate over each cursor.
            do {
                Double lat = hillsCursor.getDouble(hillsCursor
                        .getColumnIndex(HillDbAdapter.KEY_LATITUDE)) * 1E6;
                Double lng = hillsCursor.getDouble(hillsCursor
                        .getColumnIndex(HillDbAdapter.KEY_LONGITUDE)) * 1E6;

                double distanceKm = DistanceCalculator.CalculationByDistance(hill.getLatitude(), lat / 1E6, hill.getLongitude(), lng / 1E6);
                int row_id = hillsCursor.getInt(hillsCursor
                        .getColumnIndex(HillDbAdapter.KEY_ID));
                if (distanceKm < nearRadius && row_id != rowid) {


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

                    hillPoints.add(tinyHill);
                    LatLng hillPosition = new LatLng(tinyHill.getLatitude(), tinyHill.getLongitude());
                    map
                            .addMarker(
                                    new MarkerOptions()
                                            .draggable(false)
                                            .position(hillPosition)
                                            .title(tinyHill.getHillname()
                                                    + " m"
                                            )
                                            .icon(BitmapDescriptorFactory
                                                    .fromResource(R.drawable.yellow_hill))
                                            .anchor(0.5F, 0.5F)
                            );
                }

            } while (hillsCursor.moveToNext());
        }
        hillsCursor.close();
        dbAdapter.close();
        //BalloonManyHillsOverlay manyHillsOverlay = new BalloonManyHillsOverlay(marker,
        //       mapView);

        for (TinyHill gc : hillPoints) {
//            OverlayItem oi = new OverlayItem(new GeoPoint(gc.getLatitude()
//                    .intValue(), gc.getLongitude().intValue()),
//                    gc.getHillname(), String.valueOf(gc._id));
//            if (gc.isClimbed()) oi.setMarker(cmarker);
//            else oi.setMarker(marker);
//            items.add(oi);
        }
}


    public void mapOnHillSelected(int rowid) {


    }

    public void hillTapped(int rowid) {


        Intent intent = new Intent(DetailGMapActivity.this,
                HillDetailFragmentActivity.class);

        intent.putExtra("rowid", rowid);

        startActivity(intent);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        LatLng hillPosition = new LatLng(hill.getLatitude(), hill.getLongitude());
        map
                .addMarker(
                        new MarkerOptions()
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

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(hillPosition, 6));

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
}
