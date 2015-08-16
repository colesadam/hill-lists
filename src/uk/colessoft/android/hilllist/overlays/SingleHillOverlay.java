package uk.colessoft.android.hilllist.overlays;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.location.Location;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class SingleHillOverlay extends Overlay{
	
	private final int mRadius = 5;

	private GeoPoint hillLocation;
	public GeoPoint getHillLocation() {
		return hillLocation;
	}


	public void setHillLocation(GeoPoint hillLocation) {
		this.hillLocation = hillLocation;
	}


	private String hillname;

	@Override
	  public void draw(Canvas canvas, MapView mapView, boolean shadow) {
	    Projection projection = mapView.getProjection();

	    if (shadow == false) {
	      // Get the current location    


	      // Convert the location to screen pixels     
	      Point point = new Point();
	      projection.toPixels(hillLocation, point);

	
	      
	      Path triPath=new Path();
	      triPath.setFillType(Path.FillType.EVEN_ODD);
	      triPath.moveTo(point.x-mRadius, point.y+mRadius);
	      triPath.lineTo(point.x, point.y-mRadius);
	      triPath.lineTo(point.x+mRadius, point.y+mRadius);
	      triPath.lineTo(point.x-mRadius, point.y+mRadius);
	      
	      

	      // Setup the paint
	      Paint paint = new Paint();
	      paint.setARGB(250, 0, 255, 0);
	      paint.setAntiAlias(true);
	      paint.setFakeBoldText(true);
	      

	      Paint backPaint = new Paint();
	      backPaint.setARGB(175, 50, 50, 50);
	      backPaint.setAntiAlias(true);


	      // Draw the marker    
	      canvas.drawPath(triPath, paint);

	      canvas.drawText(hillname, 
                  point.x + 2*mRadius, point.y+mRadius, 
                  paint);


	    }
	    super.draw(canvas, mapView, shadow);
	  }


	@Override
	public boolean onTap(GeoPoint arg0, MapView arg1) {
		// TODO Auto-generated method stub
		return super.onTap(arg0, arg1);
	}



	public void setHillname(String hillname) {
		this.hillname = hillname;
	}


	public String getHillname() {
		return hillname;
	}

}
