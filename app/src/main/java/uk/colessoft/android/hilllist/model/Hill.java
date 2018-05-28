package uk.colessoft.android.hilllist.model;

import android.arch.persistence.room.ColumnInfo;

import java.util.Date;

public class Hill {

	private Date dateClimbed;
	private String notes;
    private int _id;
    @ColumnInfo(name="Name") private String hillname;
    @ColumnInfo(name="Section") private String hSection;
    @ColumnInfo(name="Area") private String area;
    @ColumnInfo(name="Classification") private String classification;
    @ColumnInfo(name="Map 1:50k") private String map;
    @ColumnInfo(name="Map 1:25k") private String map25;
    @ColumnInfo(name="Metres") private float heightm;
    @ColumnInfo(name="Feet") private float heightf;
    @ColumnInfo(name="Grid ref") private String gridref;
    @ColumnInfo(name="Grid ref 10") private String gridref10;
    @ColumnInfo(name="Drop") private float drop;
    @ColumnInfo(name="Col grid ref") private String colgridref;
    @ColumnInfo(name="Col height") private float colheight;
    @ColumnInfo(name="Feature") private String feature;
    @ColumnInfo(name="Observations") private String observations;
    @ColumnInfo(name="Survey") private String survey;
    @ColumnInfo(name="Climbed") private String climbed;
    @ColumnInfo(name="Revision") private Date revision;
    @ColumnInfo(name="Comments") private String comments;
    @ColumnInfo(name="Streetmap/OSiViewer") private String streetmap;
    @ColumnInfo(name="Hill-bagging") private String hillBagging;
    @ColumnInfo(name="Xcoord") private int xcoord;
    @ColumnInfo(name="Ycoord") private int ycoord;
    @ColumnInfo(name="Latitude") private double latitude;
    @ColumnInfo(name="Longitude") private double longitude;
    @ColumnInfo(name="_Section") private String _section;

	public Hill() {}

    public Hill(Date dateClimbed, String notes, int _id, String hillname, String hSection, String area, String classification, String map, String map25, float heightm, float heightf, String gridref, String gridref10, float drop, String colgridref, float colheight, String feature, String observations, String survey, String climbed, Date revision, String comments, String streetmap, String hillBagging, int xcoord, int ycoord, double latitude, double longitude, String _section) {
        this.dateClimbed = dateClimbed;
        this.notes = notes;
        this._id = _id;
        this.hillname = hillname;
        this.hSection = hSection;
        this.area = area;
        this.classification = classification;
        this.map = map;
        this.map25 = map25;
        this.heightm = heightm;
        this.heightf = heightf;
        this.gridref = gridref;
        this.gridref10 = gridref10;
        this.drop = drop;
        this.colgridref = colgridref;
        this.colheight = colheight;
        this.feature = feature;
        this.observations = observations;
        this.survey = survey;
        this.climbed = climbed;
        this.revision = revision;
        this.comments = comments;
        this.streetmap = streetmap;
        this.hillBagging = hillBagging;
        this.xcoord = xcoord;
        this.ycoord = ycoord;
        this.latitude = latitude;
        this.longitude = longitude;
        this._section = _section;
    }

    public Date getDateClimbed() {
        return dateClimbed;
    }

    public void setDateClimbed(Date dateClimbed) {
        this.dateClimbed = dateClimbed;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getHillname() {
        return hillname;
    }

    public void setHillname(String hillname) {
        this.hillname = hillname;
    }

    public String getHSection() {
        return hSection;
    }

    public void setHSection(String hSection) {
        this.hSection = hSection;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getMap25() {
        return map25;
    }

    public void setMap25(String map25) {
        this.map25 = map25;
    }

    public float getHeightm() {
        return heightm;
    }

    public void setHeightm(float heightm) {
        this.heightm = heightm;
    }

    public float getHeightf() {
        return heightf;
    }

    public void setHeightf(float heightf) {
        this.heightf = heightf;
    }

    public String getGridref() {
        return gridref;
    }

    public void setGridref(String gridref) {
        this.gridref = gridref;
    }

    public String getGridref10() {
        return gridref10;
    }

    public void setGridref10(String gridref10) {
        this.gridref10 = gridref10;
    }

    public float getDrop() {
        return drop;
    }

    public void setDrop(float drop) {
        this.drop = drop;
    }

    public String getColgridref() {
        return colgridref;
    }

    public void setColgridref(String colgridref) {
        this.colgridref = colgridref;
    }

    public float getColheight() {
        return colheight;
    }

    public void setColheight(float colheight) {
        this.colheight = colheight;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getSurvey() {
        return survey;
    }

    public void setSurvey(String survey) {
        this.survey = survey;
    }

    public String getClimbed() {
        return climbed;
    }

    public void setClimbed(String climbed) {
        this.climbed = climbed;
    }

    public Date getRevision() {
        return revision;
    }

    public void setRevision(Date revision) {
        this.revision = revision;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getStreetmap() {
        return streetmap;
    }

    public void setStreetmap(String streetmap) {
        this.streetmap = streetmap;
    }

    public String getHillBagging() {
        return hillBagging;
    }

    public void setHillBagging(String hillBagging) {
        this.hillBagging = hillBagging;
    }

    public int getXcoord() {
        return xcoord;
    }

    public void setXcoord(int xcoord) {
        this.xcoord = xcoord;
    }

    public int getYcoord() {
        return ycoord;
    }

    public void setYcoord(int ycoord) {
        this.ycoord = ycoord;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String get_section() {
        return _section;
    }

    public void set_section(String _section) {
        this._section = _section;
    }
}
