package uk.colessoft.android.hilllist.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import uk.colessoft.android.hilllist.BHApplication;
import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.database.BritishHillsDatasource;
import uk.colessoft.android.hilllist.model.Business;
import uk.colessoft.android.hilllist.model.HillDetail;
import uk.colessoft.android.hilllist.model.ScootXMLHandler;
import uk.colessoft.android.hilllist.utility.LatLangBounds;

public class BusinessSearchMapActivity extends AppCompatActivity implements GoogleMap.OnInfoWindowClickListener,LoaderManager.LoaderCallbacks<ArrayList>, OnMapReadyCallback {

    //private MapView mapView;
    private double lat;
    private double lon;
    //private MapController mapController;
    //private SingleMarkerOverlay positionOverlay;
    private ScootXMLHandler searchHandler;
    private String search_string;

    @Inject
    BritishHillsDatasource dbAdapter;

    private GoogleMap map;
    private HillDetail hillDetail;
    private static ArrayList<Business> businesses = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.many_hills_map_v2);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ((BHApplication) getApplication()).getDbComponent().inject(this);
        int rowid = getIntent().getExtras().getInt("rowid");
        String title = getIntent().getExtras().getString("title");
        search_string = getIntent().getExtras().getString("search_string");
        setTitle(title);
        hillDetail = dbAdapter.getHill(rowid);


        final ToggleButton mapButton = (ToggleButton) findViewById(R.id.satellite_button);
        mapButton.setChecked(true);
        mapButton.setOnClickListener(v -> {
            if (mapButton.isChecked()) {
                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            } else {
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }

        });
        lat = hillDetail.getHill().getLatitude();
        lon = hillDetail.getHill().getLongitude();


    }


    private void addBusinesses() {


        LatLangBounds llb = new LatLangBounds();
        llb.addLatLong(hillDetail.getHill().getLatitude(), hillDetail.getHill().getLongitude());
        for (Business business : businesses) {
            Double lat = (double) business.getLatitude();
            Double lng = (double) business.getLongitude();

            llb.addLatLong(lat, lng);

            LatLng businessPosition = new LatLng(lat, lng);
            map.addMarker(new MarkerOptions()
                    .draggable(false)
                    .position(businessPosition)
                    .title(business.getCompanyname())
            ).setTag(business.getResultNumber());
        }


        final LatLngBounds bounds = new LatLngBounds(new LatLng(
                llb.getSmallestLat(), llb.getSmallestLong()), new LatLng(llb.getLargestLat(),
                llb.getLargestLong()));
        map.animateCamera(
                CameraUpdateFactory
                        .newLatLngBounds(
                                bounds, 50));
    }

    public void setSearch_string(String search_string) {
        this.search_string = search_string;
    }

    public String getSearch_string() {
        return search_string;
    }

    public Loader<ArrayList> onCreateLoader(int id, Bundle args) {
        return new ScootSearchTaskLoader(this, businesses, search_string, searchHandler, lat, lon);
    }

    public void onLoadFinished(Loader<ArrayList> loader, ArrayList data) {
        businesses = data;
        addBusinesses();

    }

    public void onLoaderReset(Loader<ArrayList> loader) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng hillPosition = new LatLng(lat, lon);
        map = googleMap;
        map.setOnInfoWindowClickListener(this);
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

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
        marker.setTag(hillDetail.getHill().get_id());
        marker.showInfoWindow();

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(hillPosition, 7));
        getSupportLoaderManager().restartLoader(0, null, this);

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if(!marker.getTitle().equals(hillDetail.getHill().getHillname())){
            Intent intent = new Intent(this, BusinessDetailActivity.class);
		intent.putExtra("result_number",(Integer)marker.getTag());
		intent.putExtra("latitude", hillDetail.getHill().getLatitude());
		intent.putExtra("longitude", hillDetail.getHill().getLongitude());
		intent.putExtra("search_string", search_string);


		startActivity(intent);
        }
    }

    private static class ScootSearchTaskLoader extends
            AsyncTaskLoader<ArrayList> {
        String searchString;
        ArrayList<Business> foundBusinesses;
        ScootXMLHandler searchHandler;
        double lat1, lon1;

        public ScootSearchTaskLoader(Context context) {
            super(context);
            // TODO Auto-generated constructor stub
        }


        public ScootSearchTaskLoader(Context context, ArrayList<Business> businesses, String searchString,
                                     ScootXMLHandler searchHandler, double lat1, double lon1) {
            super(context);
            this.searchString = searchString;
            this.searchHandler = searchHandler;
            this.lat1 = lat1;
            this.lon1 = lon1;

        }

        @Override
        protected void onStartLoading() {
            if (foundBusinesses != null) {
                deliverResult(foundBusinesses);
            }

            if (takeContentChanged() || foundBusinesses == null) {
                forceLoad();
            }
        }

        @Override
        public ArrayList loadInBackground() {
            /* Create a URL we want to load some xml-data from. */
            try {
                URL url = new URL("http://www.scoot.co.uk/api/find.php?lat=" + lat1
                        + "&long=" + lon1 + "&format=xml&what=" + searchString);

				/* Get a SAXParser from the SAXPArserFactory. */
                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser sp = null;
                try {
                    sp = spf.newSAXParser();
                } catch (ParserConfigurationException e) {
                    Log.e("Error", "error", e);
                }

				/* Get the XMLReader of the SAXParser we created. */
                XMLReader xr = sp.getXMLReader();
                /* Create a new ContentHandler and apply it to the XML-Reader */
                searchHandler = new ScootXMLHandler();
                xr.setContentHandler(searchHandler);

				/* Parse the xml-data from our URL. */
                xr.parse(new InputSource(url.openStream()));
				/* Parsing has finished. */


                // Make sure there is at least one row.
                if (searchHandler.getScootBusinesses().getBusinesses() != null) {
                    foundBusinesses = searchHandler.getScootBusinesses()
                            .getBusinesses();
                return foundBusinesses;

                }
            } catch (MalformedURLException e) {
                Log.e("Error", "error", e);
                return null;
            } catch (SAXException e) {
                // TODO Auto-generated catch block
                Log.e("Error", "error", e);
                return null;
            } catch (IOException e) {
                Log.e("Error", "error", e);
                return null;
            }
            return null;
        }

    }
}
