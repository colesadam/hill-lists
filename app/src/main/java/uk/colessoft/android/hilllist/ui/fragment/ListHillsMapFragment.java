package uk.colessoft.android.hilllist.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

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

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;
import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.database.BritishHillsDatasource;
import uk.colessoft.android.hilllist.database.HillsTables;
import uk.colessoft.android.hilllist.domain.HillDetail;
import uk.colessoft.android.hilllist.domain.entity.Hill;
import uk.colessoft.android.hilllist.ui.fragment.HillListFragment.OnHillSelectedListener;
import uk.colessoft.android.hilllist.domain.entity.Bagging;
import uk.colessoft.android.hilllist.domain.TinyHill;
import uk.colessoft.android.hilllist.ui.viewmodel.HillListViewModel;
import uk.colessoft.android.hilllist.utility.LatLangBounds;

public class ListHillsMapFragment extends SupportMapFragment implements
        GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener, OnMapReadyCallback, HasSupportFragmentInjector {

    @Inject
    BritishHillsDatasource dbAdapter;
    @Inject
    DispatchingAndroidInjector<Fragment> childFragmentInjector;


    private OnHillSelectedListener hillSelectedListener;
    private View viewer;
    private ProgressDialog dialog;
    private String hillType;
    private String where;
    private String orderBy;
    private String countryClause;
    private long passedRowId;
    private int selectedIndex = 0;
    private HillListViewModel viewModel;


    private GoogleMap map;
    private BitmapDescriptor marker;
    private BitmapDescriptor cmarker;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);


    }

    @Override
    public void onAttach(Activity activity) {
        AndroidSupportInjection.inject(this);
        super.onAttach(activity);
        viewModel = ViewModelProviders.of(getActivity()).get(HillListViewModel.class);

        try {
            hillSelectedListener = (OnHillSelectedListener) activity;


        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHillSelectedListener");
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
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

        this.getMapAsync(this);

        String title;

        title = getActivity().getIntent().getExtras().getString("title");
        passedRowId = getActivity().getIntent().getExtras().getInt("selectedHill");
        getActivity().setTitle(title);

        marker = BitmapDescriptorFactory
                .fromResource(R.drawable.yellow_hill);
        cmarker = BitmapDescriptorFactory
                .fromResource(R.drawable.green_hill);


    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        System.out.println("hill selected is " + marker.getTag());
         (hillSelectedListener).onHillSelected((Long) marker.getTag());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        final Observer<List<HillDetail>> nameObserver = new Observer<List<HillDetail>>() {
            @Override
            public void onChanged(@Nullable final List<HillDetail> newHills) {
                update(newHills);
            }
        };
        viewModel.getHills().observe(getActivity(), nameObserver);

        map = googleMap;
        map.setOnInfoWindowClickListener(this);
        map.setOnMarkerClickListener(this);
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        viewModel.select((Long) marker.getTag());
        return false;
    }

    private void update(List<HillDetail> hills) {

        LatLangBounds llb = new LatLangBounds();
        // iterate over cursor and get hill positions
        // Make sure there is at least one row.

        // Iterate over each cursor.
        for (HillDetail hillDetail : hills) {
            double lat = hillDetail.getHill().getLatitude();
            double lng = hillDetail.getHill().getLongitude();
            llb.addLatLong(lat, lng);
            long row_id = hillDetail.getHill().getH_id();
            if (passedRowId == 0) passedRowId = row_id;
            String hillname = hillDetail.getHill().getHillname();

            TinyHill tinyHill = new TinyHill();
            tinyHill.set_id(row_id);
            System.out.println(row_id);

            tinyHill.setHillname(hillname);
            tinyHill.setLatitude(lat);
            tinyHill.setLongitude(lng);
            if (hillDetail.getBagging() != null && !hillDetail.getBagging().isEmpty()) {
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


        final LatLngBounds bounds = new LatLngBounds(new LatLng(
                llb.getSmallestLat(), llb.getSmallestLong()), new LatLng(llb.getLargestLat(),
                llb.getLargestLong()));
        map.animateCamera(
                CameraUpdateFactory
                        .newLatLngBounds(
                                bounds, 50));

    }


    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return childFragmentInjector;
    }
}
