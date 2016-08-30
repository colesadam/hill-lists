package uk.colessoft.android.hilllist.overlays;

public class BusinessSearchOverlay  {
//	private Drawable marker;
//	private List<OverlayItem> items = new ArrayList();
//	private Context mContext;
//	private static MapView mapView;
//	private int previous = 0;
//
//	public BusinessSearchOverlay(Drawable marker) {
//		super(marker,mapView);
//
//		this.marker = marker;
//
//	}
//
//	public BusinessSearchOverlay(Drawable marker, Context context) {
//		super(marker,mapView);
//		this.mContext = context;
//		this.marker = marker;
//	}
//
//
//
//	private final int mRadius = 5;
//	private List<TinyHill> hillPoints;
//	private Paint paint;
//	private LinearLayout mapBubbleWrap;
//	private String search_string;
//	private double hillLatitude;
//	private double hillLongitude;
//
//	@Override
//	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
//
//		this.mapView = mapView;
//
//		boundCenter(marker);
//		marker.setAlpha(200);
//		super.draw(canvas, mapView, false);
//
//	}
//
//	public BusinessSearchOverlay(Drawable marker, MapView mapView) {
//		super(marker, mapView);
//		this.mContext = mapView.getContext();
//		this.marker = marker;
//	}
//
//	public void setHillPoints(List<OverlayItem> hillPoints) {
//		this.items=hillPoints;
//		populate();
//	}
//
//
//	public boolean onBalloonTap(int index, OverlayItem item) {
//
//		Intent intent = new Intent(mContext, BusinessDetailActivity.class);
//		intent.putExtra("result_number",
//				Integer.parseInt(items.get(index).getSnippet()));
//		intent.putExtra("latitude", hillLatitude);
//		intent.putExtra("longitude", hillLongitude);
//		intent.putExtra("search_string", search_string);
//
//
//		mContext.startActivity(intent);
//
//
//
//		return true;
//	}
//
//	public List<TinyHill> getHillPoints() {
//		return hillPoints;
//	}
//
//	@Override
//	protected OverlayItem createItem(int i) {
//		return (items.get(i));
//	}
//
//	@Override
//	public int size() {
//		// TODO Auto-generated method stub
//		return items.size();
//	}
//
//	public void setSearchString(String search_string) {
//		this.search_string=search_string;
//
//	}
//
//	public void setHillLatitude(double lat1) {
//		this.hillLatitude=lat1;
//
//	}
//
//	public void setHillLongitude(double lon1) {
//		this.hillLongitude=lon1;
//
//	}

}
