package uk.colessoft.android.hilllist.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
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

import javax.inject.Inject;

import uk.colessoft.android.hilllist.BHApplication;
import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.database.BritishHillsDatasource;
import uk.colessoft.android.hilllist.database.HillsTables;
import uk.colessoft.android.hilllist.fragment.DisplayHillListFragment.OnHillSelectedListener;
import uk.colessoft.android.hilllist.entity.Bagging;
import uk.colessoft.android.hilllist.model.TinyHill;
import uk.colessoft.android.hilllist.utility.LatLangBounds;

public class ListHillsMapFragment extends SupportMapFragment implements
        LoaderManager.LoaderCallbacks<Cursor>, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener, OnMapReadyCallback {

    @Inject
    BritishHillsDatasource dbAdapter;

    private OnHillSelectedListener hillSelectedListener;
    private MapOnHillSelectedListener mapOnHillSelectedListener;
    private View viewer;
    private ProgressDialog dialog;
    private String hillType;
    private String where;
    private String orderBy;
    private String countryClause;
    private int passedRowId;
    private int selectedIndex = 0;

    private GoogleMap map;
    private BitmapDescriptor marker;
    private BitmapDescriptor cmarker;

    public interface MapOnHillSelectedListener {
        void mapOnHillSelected(int rowid);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BHApplication) getActivity().getApplication()).getDbComponent().inject(this);

        setHasOptionsMenu(true);


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            hillSelectedListener = (OnHillSelectedListener) activity;
            mapOnHillSelectedListener = (MapOnHillSelectedListener) activity;

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

        hillType = getActivity().getIntent().getExtras().getString("groupId");
        where = getActivity().getIntent().getExtras().getString("moreWhere");
        orderBy = getActivity().getIntent().getExtras().getString("orderBy");
        countryClause = getActivity().getIntent().getExtras().getString("countryClause");
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
        System.out.println("hill selected is "+marker.getTag());
        (hillSelectedListener).onHillSelected((Integer) marker.getTag());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        getLoaderManager().restartLoader(0, null, this);
        map = googleMap;
        map.setOnInfoWindowClickListener(this);
        map.setOnMarkerClickListener(this);
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mapOnHillSelectedListener.mapOnHillSelected((Integer) marker.getTag());
        return false;
    }


    private static class UpdateHillsTaskLoader extends AsyncTaskLoader<Cursor> {
        private Cursor data;
        private String where = null;
        private String orderBy;
        private String hilltype;
        private String countryClause;
        private int filterHills;
        private BritishHillsDatasource dbAdapter;

        public UpdateHillsTaskLoader(Context context) {
            super(context);

        }

        public UpdateHillsTaskLoader(Context context, String hilltype,
                                     String countryClause, String where, String orderBy,
                                     int filterHills, BritishHillsDatasource dbAdapter) {
            super(context);
            this.where = where;
            this.orderBy = orderBy;
            this.hilltype = hilltype;
            this.countryClause = countryClause;
            this.filterHills = filterHills;
            this.dbAdapter = dbAdapter;
        }

        @Override
        protected void onStartLoading() {
            if (data != null) {
                deliverResult(data);
            }

            if (takeContentChanged() || data == null) {
                forceLoad();
            }
        }

        @Override
        public Cursor loadInBackground() {

            Cursor result = dbAdapter.getHillGroup(hilltype, countryClause,
                    where, orderBy, filterHills);
            // dbAdapter.close();
            data = result;
            return result;
        }

    }

    private void update(Cursor hillsCursor) {

        LatLangBounds llb = new LatLangBounds();
        // iterate over cursor and get hill positions
        // Make sure there is at least one row.
        if (hillsCursor.moveToFirst()) {
            // Iterate over each cursor.
            do {
                double lat = hillsCursor.getDouble(hillsCursor
                        .getColumnIndex(HillsTables.KEY_LATITUDE));
                double lng = hillsCursor.getDouble(hillsCursor
                        .getColumnIndex(HillsTables.KEY_LONGITUDE));
                llb.addLatLong(lat, lng);
                int row_id = hillsCursor.getInt(hillsCursor
                        .getColumnIndex(HillsTables.KEY_HILL_ID));
                if (passedRowId == 0) passedRowId = row_id;
                String hillname = hillsCursor.getString(hillsCursor
                        .getColumnIndex(HillsTables.KEY_HILLNAME));

                TinyHill tinyHill = new TinyHill();
                tinyHill.set_id(row_id);
                System.out.println(row_id);

//				if(passedRowId==row_id){
//					selectedIndex=hillPoints.size();
//				}

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


            } while (hillsCursor.moveToNext());
        }

        HillDetailFragment fragment = (HillDetailFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.hill_detail_fragment);

        //If we are in fragment layer update hill details in detail fragment for current hillid
        if (fragment != null && fragment.isInLayout()) {
            hillSelectedListener.onHillSelected(passedRowId);
        }

        final LatLngBounds bounds = new LatLngBounds(new LatLng(
                llb.getSmallestLat(), llb.getSmallestLong()), new LatLng(llb.getLargestLat(),
                llb.getLargestLong()));
        map.animateCamera(
                CameraUpdateFactory
                        .newLatLngBounds(
                                bounds, 50));

    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        dialog = new ProgressDialog(getActivity());
        this.dialog.setMessage("Getting Hills...");
        this.dialog.show();
        return new UpdateHillsTaskLoader(getActivity(), hillType,
                countryClause, where, orderBy, 0, dbAdapter);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (this.dialog.isShowing()) {
            this.dialog.dismiss();
        }

        update(data);

    }

    public void onLoaderReset(Loader<Cursor> loader) {
        // TODO Auto-generated method stub

    }


}
