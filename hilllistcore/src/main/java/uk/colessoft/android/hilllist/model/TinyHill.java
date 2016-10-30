package uk.colessoft.android.hilllist.model;

import org.joda.time.LocalDate;

public class TinyHill {
	public int _id;
	private String hillname;
	private Double latitude;
	private Double longitude;
	private boolean climbed;
	private LocalDate dateClimbed;

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	private String notes;

	public float getHeightM() {
		return heightM;
	}

	public void setHeightM(float heightM) {
		this.heightM = heightM;
	}

	public float getHeightF() {
		return heightF;
	}

	public void setHeightF(float heightF) {
		this.heightF = heightF;
	}

	public LocalDate getDateClimbed() {
		return dateClimbed;
	}

	public void setDateClimbed(LocalDate dateClimbed) {
		this.dateClimbed = dateClimbed;
	}

	private float heightM;
	private float heightF;


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
