package uk.colessoft.android.hilllist.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.View;

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
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import java.util.ArrayList;
import java.util.List;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.database.HillDbAdapter;
import uk.colessoft.android.hilllist.fragments.DisplayHillListFragment.OnHillSelectedListener;
import uk.colessoft.android.hilllist.model.TinyHill;

public class ListHillsMapFragment extends SupportMapFragment implements
        LoaderManager.LoaderCallbacks<Cursor>, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener,OnMapReadyCallback {

    private HillDbAdapter dbAdapter;
    //	private MapView mapView;
//	private MapController mapController;
//	private BalloonManyHillsOverlay manyHillsOverlay;
//	private ManyHillsOverlay cmanyHillsOverlay;
    private double lat;
    private double lng;
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

    private ArrayList<OverlayItem> items;
    private List<Overlay> overlays;

    private GoogleMap map;
    private BitmapDescriptor marker;
    private BitmapDescriptor cmarker;

    public interface HillTappedListener {
        public void hillTapped(int rowid);
    }
    public interface MapOnHillSelectedListener {
        public void mapOnHillSelected(int rowid);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dbAdapter = new HillDbAdapter(getActivity());
        try {
            hillSelectedListener = (OnHillSelectedListener) activity;
            mapOnHillSelectedListener = (MapOnHillSelectedListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHillSelectedListener");
        }
    }
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//	        Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		viewer =  inflater.inflate(R.layout.many_hills_map, container, false);
//		//return ((ListHillsMapFragmentActivity)getActivity()).fView;
//
//		return viewer;
//
//
//	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        //final MapView mapView=((AdamsSpecialInterface)getActivity()).getMapView();
        //((ViewGroup)viewer).addView(mapView,0);

		
		/*test*/

this.getMapAsync(this);
        dbAdapter = new HillDbAdapter(getActivity());
        dbAdapter.open();

        String title;


        hillType = getActivity().getIntent().getExtras().getString("groupId");
        where = getActivity().getIntent().getExtras().getString("moreWhere");
        orderBy = getActivity().getIntent().getExtras().getString("orderBy");
        countryClause = getActivity().getIntent().getExtras().getString("countryClause");
        title = getActivity().getIntent().getExtras().getString("title");
        passedRowId = getActivity().getIntent().getExtras().getInt("selectedHill");
        getActivity().setTitle(title);


//        final ToggleButton mapButton = (ToggleButton) ((ViewGroup) viewer).findViewById(R.id.satellite_button);
//        mapButton.setChecked(true);
//        mapButton.setOnClickListener(new Button.OnClickListener() {
//
//            public void onClick(View v) {
//                if (mapButton.isChecked()) {
//                    map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
//                } else {
//                    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//                }
//
//            }
//
//        });

//		mapController = mapView.getController();
//		mapController.setZoom(8);
//		mapController.setCenter(new GeoPoint(new Double(lat).intValue(),
//				new Double(lng).intValue()));
//		mapView.setBuiltInZoomControls(true);

        marker = BitmapDescriptorFactory
                .fromResource(R.drawable.yellow_hill);
        cmarker = BitmapDescriptorFactory
                .fromResource(R.drawable.green_hill);
//
//
//
//		// Add the Overlay
//		manyHillsOverlay = new BalloonManyHillsOverlay(marker, mapView);
//		items = new ArrayList<OverlayItem>();
//
//
//		overlays = mapView.getOverlays();


    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        (hillSelectedListener).onHillSelected((Integer)marker.getTag());
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
        ((MapOnHillSelectedListener)getActivity()).mapOnHillSelected((Integer)marker.getTag());
        return false;
    }


    private static class UpdateHillsTaskLoader extends AsyncTaskLoader<Cursor> {
        private Cursor data;
        private String where = null;
        private String orderBy;
        private String hilltype;
        private String countryClause;
        private int filterHills;
        private HillDbAdapter dbAdapter;

        public UpdateHillsTaskLoader(Context context) {
            super(context);
            // TODO Auto-generated constructor stub
        }

        public UpdateHillsTaskLoader(Context context, String hilltype,
                                     String countryClause, String where, String orderBy,
                                     int filterHills, HillDbAdapter dbAdapter) {
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
        protected void onStopLoading() {
            // TODO Auto-generated method stub
            super.onStopLoading();
        }

        @Override
        public Cursor loadInBackground() {
            dbAdapter.open();

            Cursor result = dbAdapter.getHillGroup(hilltype, countryClause,
                    where, orderBy, filterHills);
            // dbAdapter.close();
            data = result;
            return result;
        }

    }

    private void update(Cursor hillsCursor) {

        double smallestLat = 90.0;
        double largestLat = -90.0;
        double smallestLong = 90.0;
        double largestLong = -90.0;
        // iterate over cursor and get hill positions
        // Make sure there is at least one row.
        if (hillsCursor.moveToFirst()) {
            // Iterate over each cursor.
            do {
                lat = hillsCursor.getDouble(hillsCursor
                        .getColumnIndex(HillDbAdapter.KEY_LATITUDE));
                lng = hillsCursor.getDouble(hillsCursor
                        .getColumnIndex(HillDbAdapter.KEY_LONGITUDE));

                if (lat < smallestLat)
                    smallestLat = lat;
                if (lat > largestLat)
                    largestLat = lat;
                if (lng < smallestLong)
                    smallestLong = lng;
                if (lng > largestLong)
                    largestLong = lng;
                int row_id = hillsCursor.getInt(hillsCursor
                        .getColumnIndex(HillDbAdapter.KEY_ID));
                if (passedRowId == 0) passedRowId = row_id;
                String hillname = hillsCursor.getString(hillsCursor
                        .getColumnIndex(HillDbAdapter.KEY_HILLNAME));

                TinyHill tinyHill = new TinyHill();
                tinyHill._id = row_id;

//				if(passedRowId==row_id){
//					selectedIndex=hillPoints.size();
//				}

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


            } while (hillsCursor.moveToNext());
        }

        HillDetailFragment fragment = (HillDetailFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.hill_detail_fragment);

        //If we are in fragment layer update hill details in detail fragment for current hillid
        if (fragment != null && fragment.isInLayout()) {
            hillSelectedListener.onHillSelected(passedRowId);
        }

        final LatLngBounds bounds = new LatLngBounds(new LatLng(
                smallestLat, smallestLong), new LatLng(largestLat,
                largestLong));
        map.animateCamera(
                CameraUpdateFactory
                        .newLatLngBounds(
                                bounds, 50));
        dbAdapter.close();


    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        dialog = new ProgressDialog(getActivity());
        this.dialog.setMessage("Getting Hills...");
        this.dialog.show();
        return new UpdateHillsTaskLoader(getActivity(), hillType,
                countryClause, where, orderBy, 0, dbAdapter);
    }

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
