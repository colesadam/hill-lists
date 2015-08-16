package uk.colessoft.android.hilllist.overlays;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class SingleMarkerOverlay extends Overlay{

	private GeoPoint hillMarker;
	private Drawable marker;
	private String hillname;
	public String getHillname() {
		return hillname;
	}
	public void setHillname(String hillname) {
		this.hillname = hillname;
	}
	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
			long when) {
		// TODO Auto-generated method stub
		super.draw(canvas, mapView, false, when);
		 Projection projection = mapView.getProjection();
		// Convert the location to screen pixels     
	      Point point = new Point();
	      projection.toPixels(hillMarker, point);
	      int w=marker.getIntrinsicWidth();
	      int h=marker.getIntrinsicHeight();
	      marker.setAlpha(200);
	      marker.setBounds(point.x-w/2, point.y-h/2, point.x+w/2, point.y+h/2);
	      marker.draw(canvas);
	      
	      //drawAt(canvas, marker, point.x-(marker.getIntrinsicWidth()/2), point.y+(marker.getIntrinsicHeight()/2), false);
	      


		
		
		
		
		return true;
	}
	public void setHillMarker(GeoPoint hillMarker) {
		this.hillMarker = hillMarker;
	}
	public GeoPoint getHillMarker() {
		return hillMarker;
	}
	public void setMarker(Drawable marker) {
		this.marker = marker;
	}
	public Drawable getMarker() {
		return marker;
	}

}
