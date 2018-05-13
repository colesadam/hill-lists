package uk.colessoft.android.hilllist.model;

public class GeographImageDetail {

    private String title;
    private String description;
    private String link;
    private String author;
    private String category;
    private String guid;
    private String source;
    private int date;
    private String imageTaken;
    private String lat;
    private String _long;
    private String thumb;
    private String thumbTag;
    private String licence;

    /**
     * No args constructor for use in serialization
     */
    public GeographImageDetail() {
    }

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
    public GeographImageDetail(String title, String description, String link, String author, String category, String guid, String source, int date, String imageTaken, String lat, String _long, String thumb, String thumbTag, String licence) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.author = author;
        this.category = category;
        this.guid = guid;
        this.source = source;
        this.date = date;
        this.imageTaken = imageTaken;
        this.lat = lat;
        this._long = _long;
        this.thumb = thumb;
        this.thumbTag = thumbTag;
        this.licence = licence;
    }

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return The link
     */
    public String getLink() {
        return link;
    }

    /**
     * @param link The link
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * @return The author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author The author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return The category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category The category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return The guid
     */
    public String getGuid() {
        return guid;
    }

    /**
     * @param guid The guid
     */
    public void setGuid(String guid) {
        this.guid = guid;
    }

    /**
     * @return The source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source The source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return The date
     */
    public int getDate() {
        return date;
    }

    /**
     * @param date The date
     */
    public void setDate(int date) {
        this.date = date;
    }

    /**
     * @return The imageTaken
     */
    public String getImageTaken() {
        return imageTaken;
    }

    /**
     * @param imageTaken The imageTaken
     */
    public void setImageTaken(String imageTaken) {
        this.imageTaken = imageTaken;
    }

    /**
     * @return The lat
     */
    public String getLat() {
        return lat;
    }

    /**
     * @param lat The lat
     */
    public void setLat(String lat) {
        this.lat = lat;
    }

    /**
     * @return The _long
     */
    public String getLong() {
        return _long;
    }

    /**
     * @param _long The long
     */
    public void setLong(String _long) {
        this._long = _long;
    }

    /**
     * @return The thumb
     */
    public String getThumb() {
        return thumb;
    }

    /**
     * @param thumb The thumb
     */
    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    /**
     * @return The thumbTag
     */
    public String getThumbTag() {
        return thumbTag;
    }

    /**
     * @param thumbTag The thumbTag
     */
    public void setThumbTag(String thumbTag) {
        this.thumbTag = thumbTag;
    }

    /**
     * @return The licence
     */
    public String getLicence() {
        return licence;
    }

    /**
     * @param licence The licence
     */
    public void setLicence(String licence) {
        this.licence = licence;
    }

}