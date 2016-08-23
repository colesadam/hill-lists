package uk.colessoft.android.hilllist.model;

import java.util.Date;

public class Hill {
	int _id;
	private String _section;
	private String hillname;
	private String section;
	private String region;
	private String area;
	private float heightm;
	private float heightf;
	private String map;
	private String map25;
	private String gridref;
	private String colgridref;
	private float colheight;
	private float drop;
	private String gridref10;
	private String feature;
	private String observations;
	private String survey;
	private String climbed;
	private String classification;
	private Date revision;
	private String comments;
	private int xcoord;
	private int ycoord;
	private double latitude;
	private double longitude;
	private String streetmap;
	private String getamap;
	private String hillBagging;
	private Date hillClimbed;
	private String notes;

	public Hill(int id, String section, String hillname, String section2,
			String region, String area, float heightm, float heightf,
			String map, String map25, String gridref, String colgridref,
			float colheight, float drop, String gridref10, String feature,
			String observations, String survey, String climbed,
			String classification, Date revision, String comments, int xcoord,
			int ycoord, double latitude, double longitude, String streetmap,
			String getamap, String hillBagging,Date hillClimbed,String notes) {
		this._id = id;
		this._section = section;
		this.hillname = hillname;
		this.section = section2;
		this.region = region;
		this.area = area;
		this.heightm = heightm;
		this.heightf = heightf;
		this.map = map;
		this.map25 = map25;
		this.gridref = gridref;
		this.colgridref = colgridref;
		this.colheight = colheight;
		this.drop = drop;
		this.gridref10 = gridref10;
		this.feature = feature;
		this.observations = observations;
		this.survey = survey;
		this.climbed = climbed;
		this.classification = classification;
		this.revision = revision;
		this.comments = comments;
		this.xcoord = xcoord;
		this.ycoord = ycoord;
		this.latitude = latitude;
		this.longitude = longitude;
		this.streetmap = streetmap;
		this.getamap = getamap;
		this.hillBagging = hillBagging;
		this.setHillClimbed(hillClimbed);
		this.setNotes(notes);

	}

	public int get_id() {
		return _id;
	}

	public String get_section() {
		return _section;
	}

	public String getHillname() {
		return hillname;
	}

	public String getSection() {
		return section;
	}

	public String getRegion() {
		return region;
	}

	public String getArea() {
		return area;
	}

	public float getHeightm() {
		return heightm;
	}

	public float getHeightf() {
		return heightf;
	}

	public String getMap() {
		return map;
	}

	public String getMap25() {
		return map25;
	}

	public String getGridref() {
		return gridref;
	}

	public String getColgridref() {
		return colgridref;
	}

	public float getColheight() {
		return colheight;
	}

	public float getDrop() {
		return drop;
	}

	public String getGridref10() {
		return gridref10;
	}

	public String getFeature() {
		return feature;
	}

	public String getObservations() {
		return observations;
	}

	public String getSurvey() {
		return survey;
	}

	public String getClimbed() {
		return climbed;
	}

	public String getClassification() {
		return classification;
	}

	public Date getRevision() {
		return revision;
	}

	public String getComment() {
		return comments;
	}

	public int getXcoord() {
		return xcoord;
	}

	public int getYcoord() {
		return ycoord;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public String getStreetmap() {
		return streetmap;
	}

	public String getGetamap() {
		return getamap;
	}

	public String getHillBagging() {
		return hillBagging;
	}

	public void setHillClimbed(Date hillClimbed) {
		this.hillClimbed = hillClimbed;
	}

	public Date getHillClimbed() {
		return hillClimbed;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getNotes() {
		return notes;
	}

}
