package uk.colessoft.android.hilllist.model;

public class TinyHill {
	public int _id;
	private String hillname;
	private Double latitude;
	private Double longitude;
	private boolean climbed;
	
	public String getHillname() {
		return hillname;
	}
	public void setHillname(String hillname) {
		this.hillname = hillname;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public void setClimbed(boolean climbed) {
		this.climbed = climbed;
	}
	public boolean isClimbed() {
		return climbed;
	}

}
