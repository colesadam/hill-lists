package uk.colessoft.android.hilllist.activities;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.database.HillDbAdapter;
import uk.colessoft.android.hilllist.model.Business;
import uk.colessoft.android.hilllist.model.Hill;
import uk.colessoft.android.hilllist.model.ScootXMLHandler;
import uk.colessoft.android.hilllist.model.TinyHill;
import uk.colessoft.android.hilllist.overlays.BusinessSearchOverlay;
import uk.colessoft.android.hilllist.overlays.SingleMarkerOverlay;

public class BusinessSearchMapActivity extends FragmentActivity implements
		LoaderManager.LoaderCallbacks<ArrayList> {

	private HillDbAdapter dbAdapter;
	private int rowid;
	private String title;
	private MapView mapView;
	private double lat1;
	private double lon1;
	private MapController mapController;
	private SingleMarkerOverlay positionOverlay;
	private ScootXMLHandler searchHandler;
	private String search_string;
	private ArrayList<TinyHill> hillPoints;

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.many_hills_map);

		Drawable marker = getResources().getDrawable(R.drawable.purple_hill);

		marker.setBounds(0, 0, marker.getIntrinsicWidth(),
				marker.getIntrinsicHeight());

		dbAdapter = new HillDbAdapter(this);
		dbAdapter.open();
		rowid = getIntent().getExtras().getInt("rowid");
		title = getIntent().getExtras().getString("title");
		search_string = getIntent().getExtras().getString("search_string");
		setTitle(title);
		Hill hill = dbAdapter.getHill(rowid);

		LayoutInflater inflater = (LayoutInflater) (this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
		mapView = (MapView) inflater.inflate(R.layout.mapview, null);
		((ViewGroup) findViewById(R.id.many_map_rel)).addView(mapView, 0);
		mapView.setSatellite(true);

		final ToggleButton mapButton = (ToggleButton) findViewById(R.id.satellite_button);
		mapButton.setChecked(true);
		mapButton.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				if (mapButton.isChecked()) {
					mapView.setSatellite(true);
				} else {
					mapView.setSatellite(false);
				}

			}

		});
		lat1 = hill.getLatitude();
		lon1 = hill.getLongitude();
		Double lat = lat1 * 1E6;
		Double lng = lon1 * 1E6;
		GeoPoint point = new GeoPoint(lat.intValue(), lng.intValue());

		mapController = mapView.getController();
		mapController.setCenter(point);
		mapController.setZoom(8);
		mapView.setBuiltInZoomControls(true);

		// Add the PositionOverlay
		positionOverlay = new SingleMarkerOverlay();
		positionOverlay.setHillname(hill.getHillname());
		positionOverlay.setHillMarker(point);
		positionOverlay.setMarker(marker);
		// positionOverlay.drawSingleHill(canvas, mapView, shadow);
		// positionOverlay.setHillname(hill.getHillname());

		List<Overlay> overlays = mapView.getOverlays();
		overlays.add(positionOverlay);

		getSupportLoaderManager().restartLoader(0, null,  this);
	}



	private void addBusinesses() {

		Drawable marker = getResources().getDrawable(R.drawable.pushpin);
		Drawable cmarker = getResources().getDrawable(R.drawable.pushpin);
		marker.setBounds(0, 0, marker.getIntrinsicWidth(),
				marker.getIntrinsicHeight());
		cmarker.setBounds(0, 0, cmarker.getIntrinsicWidth(),
				cmarker.getIntrinsicHeight());


		BusinessSearchOverlay businessSearchOverlay = new BusinessSearchOverlay(
				marker, mapView);
		businessSearchOverlay.setSearchString(search_string);
		businessSearchOverlay.setHillLatitude(lat1);
		businessSearchOverlay.setHillLongitude(lon1);
		List<OverlayItem> items = new ArrayList();
		for (TinyHill gc : hillPoints) {
			OverlayItem oi = new OverlayItem(new GeoPoint(gc.getLatitude()
					.intValue(), gc.getLongitude().intValue()),
					gc.getHillname(), String.valueOf(gc._id));
			if (gc.isClimbed())
				oi.setMarker(cmarker);
			else
				oi.setMarker(marker);
			items.add(oi);
		}
		businessSearchOverlay.setHillPoints(items);
		int latSpan = businessSearchOverlay.getLatSpanE6();
		int lonSpan = businessSearchOverlay.getLonSpanE6();
		mapController.zoomToSpan(latSpan, lonSpan);

		List<Overlay> overlays = mapView.getOverlays();

		overlays.add(0, businessSearchOverlay);
		mapController.setCenter(new GeoPoint((new Double(lat1 * 1E6))
				.intValue(), new Double(lon1 * 1E6).intValue()));
	}

	public void setSearch_string(String search_string) {
		this.search_string = search_string;
	}

	public String getSearch_string() {
		return search_string;
	}

	public android.support.v4.content.Loader<ArrayList> onCreateLoader(int id, Bundle args) {
		return new ScootSearchTaskLoader(this,hillPoints,search_string, searchHandler,lat1,lon1);
	}

	public void onLoadFinished(android.support.v4.content.Loader<ArrayList> loader, ArrayList data) {
		hillPoints=data;
		addBusinesses();

	}

	public void onLoaderReset(android.support.v4.content.Loader<ArrayList> loader) {
		// TODO Auto-generated method stub

	}

	private static class ScootSearchTaskLoader extends
			android.support.v4.content.AsyncTaskLoader<ArrayList> {
		String searchString;
		ArrayList<TinyHill> hillPoints;
		ScootXMLHandler searchHandler;
		double lat1,lon1;

		public ScootSearchTaskLoader(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}
		

		public ScootSearchTaskLoader(Context context, ArrayList<TinyHill>hillPoints, String searchString,
				ScootXMLHandler searchHandler,double lat1,double lon1) {
			super(context);
			this.searchString = searchString;
			this.searchHandler = searchHandler;
			this.lat1=lat1;
			this.lon1=lon1;
			this.hillPoints=hillPoints;
		}

		@Override
		protected void onStartLoading() {
			if (hillPoints != null) {
				deliverResult(hillPoints);
			}

			if (takeContentChanged() || hillPoints == null) {
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
					Log.e("Error", "error",e);
				}

				/* Get the XMLReader of the SAXParser we created. */
				XMLReader xr = sp.getXMLReader();
				/* Create a new ContentHandler and apply it to the XML-Reader */
				searchHandler = new ScootXMLHandler();
				xr.setContentHandler(searchHandler);

				/* Parse the xml-data from our URL. */
				xr.parse(new InputSource(url.openStream()));
				/* Parsing has finished. */
				hillPoints=new ArrayList();

				// iterate over cursor and get hill positions
				// Make sure there is at least one row.
				if (searchHandler.getScootBusinesses().getBusinesses() != null) {
					List<Business> businesses = searchHandler.getScootBusinesses()
							.getBusinesses();
					for (Business bs : businesses) {
						Double lat = bs.getLatitude() * 1E6;
						Double lng = bs.getLongitude() * 1E6;

						String hillname = bs.getCompanyname();
						TinyHill tinyHill = new TinyHill();
						tinyHill._id = bs.getResultNumber();
						tinyHill.setHillname(hillname);
						tinyHill.setLatitude(lat);
						tinyHill.setLongitude(lng);
						tinyHill.setClimbed(false);
						hillPoints.add(tinyHill);
					}
					return hillPoints;

				}
			} catch (MalformedURLException e) {
				Log.e("Error", "error",e);
				return null;
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				Log.e("Error", "error",e);
				return null;
			} catch (IOException e) {
				Log.e("Error", "error",e);
				return null;
			}
			return null;
		}

	}
}
