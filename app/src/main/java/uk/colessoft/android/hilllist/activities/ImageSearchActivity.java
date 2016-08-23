package uk.colessoft.android.hilllist.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.Gallery;

import com.github.droidfu.adapters.WebGalleryAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;

import uk.colessoft.android.hilllist.R;

public class ImageSearchActivity extends FragmentActivity implements
		LoaderManager.LoaderCallbacks<ArrayList> {

	private String hillname;
	private String area;
	private Gallery gallery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		hillname = getIntent().getExtras().getString("hillname");
		area=getIntent().getExtras().getString("area");
		setTitle("Images of " + hillname);
		setContentView(R.layout.image_search_view);

		gallery = (Gallery) findViewById(R.id.gallery);
		getSupportLoaderManager().restartLoader(0, null,  this);

	}

	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						String baseAddress=inetAddress.getHostAddress().toString();
						return baseAddress.split("%")[0];
						
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("error", ex.toString());
		}
		return null;
	}

	private static class ImageSearchTaskLoader extends
			AsyncTaskLoader<ArrayList> {
		private ArrayList data;
		private String hillname;
		private String area;

		public ImageSearchTaskLoader(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		public ImageSearchTaskLoader(Context context, String hillname,String area) {
			super(context);
			this.hillname = hillname;
			this.area=area;
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
		public ArrayList<String> loadInBackground() {
			StringBuilder builder = null;
			try {
				hillname = URLEncoder.encode(hillname+area);
				URL url = new URL(
						"https://ajax.googleapis.com/ajax/services/search/images?"
								+ "rsz=8&v=1.0&imgsz=xlarge&as_filetype=jpg&imgtype=photo&q="
								+ hillname+"&key=ABQIAAAA2hkdT58r2f4nSvBf2_lCARTMcCrAZA3ClbcbbWnzYXyWjzcVbhTW9paqTGWpKk9CiJcKmMMJD_9Vhg"
								+ "&userip=" + getLocalIpAddress());
				URLConnection connection = url.openConnection();
				connection.addRequestProperty("Referer",
						"http://www.colessoft.com");

				String line;
				builder = new StringBuilder();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(connection.getInputStream()));
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ArrayList<String> urls = new ArrayList<String>();
			try {

				JSONObject json = new JSONObject(builder.toString());
				JSONObject responseData = json.getJSONObject("responseData");
				JSONArray responses = responseData.getJSONArray("results");
				int size = responses.length();
				for (int i = 0; i < size; i++) {
					urls.add(responses.getJSONObject(i).getString(
							"unescapedUrl"));
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			data=urls;
			return urls;
		}

	}

	public Loader<ArrayList> onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
		return new ImageSearchTaskLoader(this, hillname,area);
	}

	public void onLoadFinished(Loader<ArrayList> loader, ArrayList data) {
		WebGalleryAdapter adapter = new WebGalleryAdapter(
				ImageSearchActivity.this, data);

		gallery.setAdapter(adapter);

	}

	public void onLoaderReset(Loader<ArrayList> loader) {
		// TODO Auto-generated method stub

	}
}
