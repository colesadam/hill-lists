package uk.colessoft.android.hilllist.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
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

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.database.BritishHillsDatasource;
import uk.colessoft.android.hilllist.domain.HillDetail;
import uk.colessoft.android.hilllist.domain.TinyHill;
import uk.colessoft.android.hilllist.utility.LatLangBounds;

public class NearbyHillsMapFragment extends SupportMapFragment implements GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener, OnMapReadyCallback {


    private Runnable showWaitDialog;
    private String[] rowids;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0x00001;
    private final List<Marker> markers = new ArrayList<>();

    public interface HillTappedListener {
        void hillTapped(long rowid);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);


        } else {
            map.setMyLocationEnabled(true);
        }


        map.setOnInfoWindowClickListener(this);
        map.setOnMarkerClickListener(this);
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        //dialog = ProgressDialog.show(getActivity(), "Please wait...",

        //        "Getting HillDetail Positions ...", true);
        //Thread timer = new Thread(showWaitDialog);

        //timer.start();

        Log.d(getActivity().getClass().getName(), "[NearbyHillsMapFragment][rowids.length: " + rowids.length + "]");
        LatLangBounds llb = new LatLangBounds();
        for (String rowid : rowids) {
            long lrow = Long.parseLong(rowid);
            HillDetail hillDetail = dbAdapter.getHill(lrow);
            TinyHill t = new TinyHill();
            t.set_id(hillDetail.getHill().getH_id());
            t.setHillname(hillDetail.getHill().getHillname());
            double lat = hillDetail.getHill().getLatitude();
            double lng = hillDetail.getHill().getLongitude();
            llb.addLatLong(lat, lng);
            t.setLatitude(lat);
            t.setLongitude(lng);
            if (hillDetail.getBagging().get(0).getDateClimbed() != null) {
                t.setClimbed(true);
            } else {
                t.setClimbed(false);
            }
            BitmapDescriptor hillDescriptor;

            if (t.isClimbed()) {
                hillDescriptor = cmarker;
            } else hillDescriptor = marker;
            LatLng hillPosition = new LatLng(t.getLatitude(), t.getLongitude());
            Marker marker = map.addMarker(new MarkerOptions()
                    .draggable(false)
                    .position(hillPosition)
                    .title(t.getHillname()

                    )
                    .icon(hillDescriptor)
                    .anchor(0.5F, 0.5F)
            );
            marker.setTag(t.get_id());
            markers.add(marker);
        }
        gotHills = true;
        final LatLngBounds bounds = new LatLngBounds(new LatLng(
                llb.getSmallestLat(), llb.getSmallestLong()), new LatLng(llb.getLargestLat(),
                llb.getLargestLong()));
        map.setOnMapLoadedCallback(() -> map.animateCamera(
                CameraUpdateFactory
                        .newLatLngBounds(
                                bounds, 50)));


    }

    private GoogleMap map;
    private View viewer;

    @Inject
    BritishHillsDatasource dbAdapter;

    private boolean gotHills;

    private ProgressDialog dialog;

    private BitmapDescriptor marker;
    private BitmapDescriptor cmarker;


    @Override
    public void onAttach(Activity activity) {
        AndroidSupportInjection.inject(this);
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        rowids = getActivity().getIntent().getExtras().getStringArray("rowids");

        this.getMapAsync(this);

        showWaitDialog = () -> {

            while (!gotHills) {

            }

            dialog.dismiss();

        };

        marker = BitmapDescriptorFactory
                .fromResource(R.drawable.yellow_hill);
        cmarker = BitmapDescriptorFactory
                .fromResource(R.drawable.green_hill);

        super.onActivityCreated(savedInstanceState);

        final ToggleButton mapButton = (ToggleButton) getActivity().findViewById(R.id.satellite_button);
        mapButton.setChecked(true);
        mapButton.setOnClickListener(v -> {
            if (mapButton.isChecked()) {
                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            } else {
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }

        });




        String title;
        int selectedIndex = 0;

        title = getActivity().getIntent().getExtras().getString("title");

        getActivity().setTitle(title);


    }


    public void moveMarker(long rowid) {

        int index = 0;
        for (Marker item : markers) {
            if (item.getTag().equals(rowid)) {
                item.showInfoWindow();
                map.animateCamera(CameraUpdateFactory.newLatLng(item.getPosition()), 250, null);
                return;
            }
            index++;
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        ((HillTappedListener) getActivity()).hillTapped((Long) marker.getTag());
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        map.setMyLocationEnabled(true);
                    }

                }

            }

        }
    }
}
