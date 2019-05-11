package uk.colessoft.android.hilllist.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Display;
import android.webkit.WebView;

import uk.colessoft.android.hilllist.R;

public class OsMapActivity extends AppCompatActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.osview);

		String x = getIntent().getExtras().getString("x");
		String y = getIntent().getExtras().getString("y");

		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth(); // deprecated
		int height = display.getHeight() - 100;

		WebView webView = (WebView) findViewById(R.id.osview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.setInitialScale(100);

		String htmlData = "\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n  \n<head>\n  \n<title>OS OpenSpace Example - Basic Map</title>  \n<meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\" />\n  \n<!-- Remember to insert your own API key in the URL below -->\n  \n<script type=\"text/javascript\" src=\"http://openspace.ordnancesurvey.co.uk/osmapapi/openspace.js?key=C6FAFEFDD14D64C3E0405F0ACA6031E5\"></script>\n<script type=\"text/javascript\">\n//define the osMap variable\n\nvar osMap;\n\n//this function creates the map and is called by the div in the HTML\n\n"
				+ "function init()\n\n  {\n    \n//create new map\n    \n    osMap = new OpenSpace.Map(\'map\');\n    \n//set map centre in National Grid Eastings and Northings and select zoom level 0\n\n    osMap.setCenter(new OpenSpace.MapPoint("
				+ x
				+ ","
				+ y
				+ "), 7);\n "
				+ "var markers = new OpenLayers.Layer.Markers(\"Markers\");\n\n//add the layer to the map    \n    \n    osMap.addLayer(markers);\n\n    \n//create a variable to hold a map position\n    \n    var pos = new OpenSpace.MapPoint("+x+", "+y+");\n\n//add a marker with that position \n    \n    var marker = new OpenLayers.Marker(pos);\n    markers.addMarker(marker);           \n  }\n\n</script>\n</head>\n  \n<body onload=\"init()\">\n    \n<!-- The div below holds the map -->\n  \n<div id=\"map\" style=\"width: "
				+ width
				+ "px; height: "
				+ height
				+ "px;\"></div>\n  \n</body>\n</html>";
		webView.loadDataWithBaseURL("http://localhost", htmlData, "text/html",
				"UTF-8", null);

	}
}
