package uk.colessoft.android.hilllist.overlays;

import java.util.ArrayList;
import java.util.List;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.activities.HillDetailFragmentActivity;
import uk.colessoft.android.hilllist.model.TinyHill;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.MapView.LayoutParams;
import com.google.android.maps.OverlayItem;

public class ManyHillsOverlay extends ItemizedOverlay {
	private Drawable marker;
	private List<OverlayItem> items = new ArrayList();
	private Context mContext;
	private MapView mapView;
	private int previous = 0;
	private MapOnHillSelectedListener hillSelectedListener;

	public interface MapOnHillSelectedListener {
		public void mapOnHillSelected(int rowid);
	}

	public ManyHillsOverlay(Drawable marker) {
		super(marker);

		this.marker = marker;

	}

	public ManyHillsOverlay(Drawable marker, Context context) {
		super(marker);
		this.mContext = context;
		this.marker = marker;
	}

	private final int mRadius = 5;
	private List<TinyHill> hillPoints;
	private Paint paint;
	private LinearLayout mapBubbleWrap;

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {

		this.mapView = mapView;
		try {
			hillSelectedListener = (MapOnHillSelectedListener) mContext;
		} catch (ClassCastException e) {
			throw new ClassCastException(mContext.toString()
					+ " must implement MapOnHillSelectedListener");
		}
		boundCenter(marker);
		marker.setAlpha(200);
		super.draw(canvas, mapView, false);

	}

	public void setHillPoints(List<OverlayItem> hillPoints) {
		this.items = hillPoints;
		populate();
	}

	public boolean onTap(final int index) {
		if (previous != 0) {
			mapView.removeView(mapBubbleWrap);
		}
		OverlayItem item = items.get(index);
		GeoPoint p = item.getPoint();

		mapView.getController().animateTo(p);
		LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, p, 0,
				0 - (marker.getIntrinsicHeight() * 2), LayoutParams.CENTER);

		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mapBubbleWrap = (LinearLayout) inflater.inflate(R.layout.popup, null);
		TextView bubble = (TextView) mapBubbleWrap.findViewById(R.id.mapBubble);
		bubble.setText(item.getTitle());
		previous = index;
		mapView.addView(mapBubbleWrap, lp);
		int rowid = Integer.parseInt(items.get(index).getSnippet());
		hillSelectedListener.mapOnHillSelected(rowid);

		mapBubbleWrap.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(mContext, HillDetailFragmentActivity.class);
				int rowid = Integer.parseInt(items.get(index).getSnippet());
				intent.putExtra("rowid", rowid);

				mContext.startActivity(intent);

			}
		});

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
