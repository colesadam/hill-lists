package uk.colessoft.android.hilllist.overlays;

import java.util.ArrayList;
import java.util.List;

import uk.colessoft.android.hilllist.objects.TinyHill;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.widget.LinearLayout;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class BalloonManyHillsOverlay extends BalloonItemizedOverlay {
	private Drawable marker;
	private List<OverlayItem> items = new ArrayList();
	private Context mContext;
	private Context aContext;
	private static MapView mapView;
	private MapOnHillSelectedListener hillSelectedListener;
	private HillTappedListener tappedListener;

	public interface MapOnHillSelectedListener {
		public void mapOnHillSelected(int rowid);
	}

	public interface HillTappedListener {
		public void hillTapped(int rowid);
	}

	public BalloonManyHillsOverlay(Drawable marker) {
		super(marker, mapView);

		this.marker = marker;

	}

	@Override
	public boolean onTap(int index) {
		super.onTap(index);
		OverlayItem item = items.get(index);
		item.getPoint();

		int rowid = Integer.parseInt(item.getSnippet());
		hillSelectedListener.mapOnHillSelected(rowid);
		

		return true;

	}

	public BalloonManyHillsOverlay(Drawable marker, MapView mapView) {
		super(marker, mapView);
		this.mContext = mapView.getContext();
		this.marker = marker;
	}

	private List<TinyHill> hillPoints;

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {

		this.mapView = mapView;

		boundCenter(marker);
		marker.setAlpha(200);
		super.draw(canvas, mapView, false);

	}

	public void setHillPoints(List<OverlayItem> hillPoints, Context context,
			int selectedIndex) {
		if (hillPoints!=null&&!hillPoints.isEmpty()) {
			this.items = hillPoints;
			this.aContext = context;
			try {
				hillSelectedListener = (MapOnHillSelectedListener) aContext;
			} catch (ClassCastException e) {
				throw new ClassCastException(mContext.toString()
						+ " must implement MapOnHillSelectedListener");
			}
			try {
				tappedListener = (HillTappedListener) aContext;
			} catch (ClassCastException e) {
				throw new ClassCastException(mContext.toString()
						+ " must implement HillTappedListener");
			}
			populate();
			onTap(selectedIndex);
		}

	}

	public boolean onBalloonTap(int index, OverlayItem item) {

		item.getPoint();

		int rowid = Integer.parseInt(item.getSnippet());
		tappedListener.hillTapped(rowid);

		return true;
	}

	public List<TinyHill> getHillPoints() {
		return hillPoints;
	}

	@Override
	protected OverlayItem createItem(int i) {
		return (items.get(i));
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return items.size();
	}

}
