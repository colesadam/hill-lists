package uk.colessoft.android.hilllist.model

class GeographImageDetail {

    /**
     * @return The title
     */
    /**
     * @param title The title
     */
    var title: String? = null
    /**
     * @return The description
     */
    /**
     * @param description The description
     */
    var description: String? = null
    /**
     * @return The link
     */
    /**
     * @param link The link
     */
    var link: String? = null
    /**
     * @return The author
     */
    /**
     * @param author The author
     */
    var author: String? = null
    /**
     * @return The category
     */
    /**
     * @param category The category
     */
    var category: String? = null
    /**
     * @return The guid
     */
    /**
     * @param guid The guid
     */
    var guid: String? = null
    /**
     * @return The source
     */
    /**
     * @param source The source
     */
    var source: String? = null
    /**
     * @return The date
     */
    /**
     * @param date The date
     */
    var date: Int = 0
    /**
     * @return The imageTaken
     */
    /**
     * @param imageTaken The imageTaken
     */
    var imageTaken: String? = null
    /**
     * @return The lat
     */
    /**
     * @param lat The lat
     */
    var lat: String? = null
    /**
     * @return The _long
     */
    /**
     * @param _long The long
     */
    var long: String? = null
    /**
     * @return The thumb
     */
    /**
     * @param thumb The thumb
     */
    var thumb: String? = null
    /**
     * @return The thumbTag
     */
    /**
     * @param thumbTag The thumbTag
     */
    var thumbTag: String? = null
    /**
     * @return The licence
     */
    /**
     * @param licence The licence
     */
    var licence: String? = null

    /**
     * No args constructor for use in serialization
     */
    constructor() {}

    /**
     * @param licence
     * @param link
     * @param date
     * @param imageTaken
     * @param guid
     * @param author
     * @param title
     * @param category
     * @param source
     * @param _long
     * @param description
     * @param thumbTag
     * @param thumb
     * @param lat
     */
    constructor(title: String, description: String, link: String, author: String, category: String, guid: String, source: String, date: Int, imageTaken: String, lat: String, _long: String, thumb: String, thumbTag: String, licence: String) {
        this.title = title
        this.description = description
        this.link = link
        this.author = author
        this.category = category
        this.guid = guid
        this.source = source
        this.date = date
        this.imageTaken = imageTaken
        this.lat = lat
        this.long = _long
        this.thumb = thumb
        this.thumbTag = thumbTag
        this.licence = licence
    }

}