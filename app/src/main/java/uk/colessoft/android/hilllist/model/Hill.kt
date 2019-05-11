package uk.colessoft.android.hilllist.model

import androidx.room.ColumnInfo

import java.util.Date

class Hill {

    var dateClimbed: Date? = null
    var notes: String? = null
    var _id: Int = 0
    @ColumnInfo(name = "Name")
    var hillname: String? = null
    @ColumnInfo(name = "Section")
    var hSection: String? = null
    @ColumnInfo(name = "Area")
    var area: String? = null
    @ColumnInfo(name = "Classification")
    var classification: String? = null
    @ColumnInfo(name = "Map 1:50k")
    var map: String? = null
    @ColumnInfo(name = "Map 1:25k")
    var map25: String? = null
    @ColumnInfo(name = "Metres")
    var heightm: Float = 0.toFloat()
    @ColumnInfo(name = "Feet")
    var heightf: Float = 0.toFloat()
    @ColumnInfo(name = "Grid ref")
    var gridref: String? = null
    @ColumnInfo(name = "Grid ref 10")
    var gridref10: String? = null
    @ColumnInfo(name = "Drop")
    var drop: Float = 0.toFloat()
    @ColumnInfo(name = "Col grid ref")
    var colgridref: String? = null
    @ColumnInfo(name = "Col height")
    var colheight: Float = 0.toFloat()
    @ColumnInfo(name = "Feature")
    var feature: String? = null
    @ColumnInfo(name = "Observations")
    var observations: String? = null
    @ColumnInfo(name = "Survey")
    var survey: String? = null
    @ColumnInfo(name = "Climbed")
    var climbed: String? = null
    @ColumnInfo(name = "Revision")
    var revision: Date? = null
    @ColumnInfo(name = "Comments")
    var comments: String? = null
    @ColumnInfo(name = "Streetmap/OSiViewer")
    var streetmap: String? = null
    @ColumnInfo(name = "Hill-bagging")
    var hillBagging: String? = null
    @ColumnInfo(name = "Xcoord")
    var xcoord: Int = 0
    @ColumnInfo(name = "Ycoord")
    var ycoord: Int = 0
    @ColumnInfo(name = "Latitude")
    var latitude: Double = 0.toDouble()
    @ColumnInfo(name = "Longitude")
    var longitude: Double = 0.toDouble()
    @ColumnInfo(name = "_Section")
    var _section: String? = null

    constructor() {}

    constructor(dateClimbed: Date, notes: String, _id: Int, hillname: String, hSection: String, area: String, classification: String, map: String, map25: String, heightm: Float, heightf: Float, gridref: String, gridref10: String, drop: Float, colgridref: String, colheight: Float, feature: String, observations: String, survey: String, climbed: String, revision: Date, comments: String, streetmap: String, hillBagging: String, xcoord: Int, ycoord: Int, latitude: Double, longitude: Double, _section: String) {
        this.dateClimbed = dateClimbed
        this.notes = notes
        this._id = _id
        this.hillname = hillname
        this.hSection = hSection
        this.area = area
        this.classification = classification
        this.map = map
        this.map25 = map25
        this.heightm = heightm
        this.heightf = heightf
        this.gridref = gridref
        this.gridref10 = gridref10
        this.drop = drop
        this.colgridref = colgridref
        this.colheight = colheight
        this.feature = feature
        this.observations = observations
        this.survey = survey
        this.climbed = climbed
        this.revision = revision
        this.comments = comments
        this.streetmap = streetmap
        this.hillBagging = hillBagging
        this.xcoord = xcoord
        this.ycoord = ycoord
        this.latitude = latitude
        this.longitude = longitude
        this._section = _section
    }
}
