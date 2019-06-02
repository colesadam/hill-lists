package uk.colessoft.android.hilllist.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.appcompat.app.AppCompatActivity;
import android.text.util.Linkify;
import android.util.Log;
import android.widget.TextView;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.model.Business;
import uk.colessoft.android.hilllist.model.ScootXMLHandler;

public class BusinessDetailActivity extends AppCompatActivity implements
		LoaderManager.LoaderCallbacks<Business> {

	private ScootXMLHandler searchHandler;

	private String search_string;
	private String lat;
	private String lon;

	private int resultNumber;

	private TextView name;

	private TextView telephone;

	private TextView address;

	private TextView postcode;

	private TextView scootLink;
	private Business business;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.business_detail);
		search_string = getIntent().getExtras().getString("search_string");
		lat = String.valueOf(getIntent().getExtras().getDouble("latitude"));
		lon = String.valueOf(getIntent().getExtras().getDouble("longitude"));
		resultNumber = getIntent().getExtras().getInt("result_number");

		name = (TextView) findViewById(R.id.bdetail_name);
		telephone = (TextView) findViewById(R.id.bdetail_telephone);
		address = (TextView) findViewById(R.id.bdetail_address);
		postcode = (TextView) findViewById(R.id.bdetail_postcode);
		scootLink = (TextView) findViewById(R.id.scootLink);
		setTitle("Business Details");

		getSupportLoaderManager().restartLoader(0, null,  this);

	}


	private void displayBusiness(Business business) {
		name.setText(business.getCompanyname());
		telephone.setText(business.getTelephone());
		postcode.setText(business.getPostCode());
		scootLink.setText("http://www.scoot.co.uk"+business.getScootLink());
		Linkify.addLinks(scootLink,Linkify.WEB_URLS);
		String tempString = "";
		for (String al : business.getAddress()) {
			if (!"".equals(al))
				tempString = tempString + al + "\n";

		}
		address.setText(tempString);

	}

	private static class ScootSearchTaskLoader extends
			AsyncTaskLoader<Business> {
		String searchString;
		Business business;
		ScootXMLHandler searchHandler;
		String lat, lon;
		private int resultNumber;

		public ScootSearchTaskLoader(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		public ScootSearchTaskLoader(Context context,Business business, String searchString,
				ScootXMLHandler searchHandler, String lat, String lon,int resultNumber) {
			super(context);
			this.searchString = searchString;
			this.searchHandler = searchHandler;
			this.lat = lat;
			this.lon = lon;
			this.business = business;
			this.resultNumber=resultNumber;
		}

		@Override
		protected void onStartLoading() {
			if (business != null) {
				deliverResult(business);
			}

			if (takeContentChanged() || business == null) {
				forceLoad();
			}
		}

		@Override
		public Business loadInBackground() {
			try{
			/* Create a URL we want to load some xml-data from. */
			URL url = new URL("http://www.scoot.co.uk/api/find.php?lat=" + lat
					+ "&long=" + lon + "&format=xml&what=" + searchString);

			/* Get a SAXParser from the SAXPArserFactory. */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = null;
			try {
				sp = spf.newSAXParser();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			/* Get the XMLReader of the SAXParser we created. */
			XMLReader xr = sp.getXMLReader();
			/* Create a new ContentHandler and apply it to the XML-Reader */
			searchHandler = new ScootXMLHandler();
			xr.setContentHandler(searchHandler);

			/* Parse the xml-data from our URL. */
			xr.parse(new InputSource(url.openStream()));
			/* Parsing has finished. */
			ArrayList businesses = (ArrayList) searchHandler.getScootBusinesses()
					.getBusinesses();
					return (Business) businesses.get(resultNumber - 1);
			
				
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
		}

	}

	public void onLoadFinished(Loader<Business> loader, Business data) {
		business=data;
		displayBusiness(business);
		

	}

	public void onLoaderReset(Loader<Business> loader) {
		// TODO Auto-generated method stub

	}

	public Loader<Business> onCreateLoader(int id, Bundle args) {
		return new ScootSearchTaskLoader(this,business,search_string, searchHandler,lat,lon, resultNumber);
	}

}
