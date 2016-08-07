
package uk.colessoft.android.hilllist.model.hillsummits;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Image {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("width")
    @Expose
    private Integer width;
    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("hit")
    @Expose
    private Integer hit;
    @SerializedName("file")
    @Expose
    private String file;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("comment")
    @Expose
    private Object comment;
    @SerializedName("date_creation")
    @Expose
    private Object dateCreation;
    @SerializedName("date_available")
    @Expose
    private String dateAvailable;
    @SerializedName("page_url")
    @Expose
    private String pageUrl;
    @SerializedName("element_url")
    @Expose
    private String elementUrl;
    @SerializedName("derivatives")
    @Expose
    private Derivatives derivatives;

    /**
     * 
     * @return
     *     The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The width
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * 
     * @param width
     *     The width
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * 
     * @return
     *     The height
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * 
     * @param height
     *     The height
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     * 
     * @return
     *     The hit
     */
    public Integer getHit() {
        return hit;
    }

    /**
     * 
     * @param hit
     *     The hit
     */
    public void setHit(Integer hit) {
        this.hit = hit;
    }

    /**
     * 
     * @return
     *     The file
     */
    public String getFile() {
        return file;
    }

    /**
     * 
     * @param file
     *     The file
     */
    public void setFile(String file) {
        this.file = file;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The comment
     */
    public Object getComment() {
        return comment;
    }

    /**
     * 
     * @param comment
     *     The comment
     */
    public void setComment(Object comment) {
        this.comment = comment;
    }

    /**
     * 
     * @return
     *     The dateCreation
     */
    public Object getDateCreation() {
        return dateCreation;
    }

    /**
     * 
     * @param dateCreation
     *     The date_creation
     */
    public void setDateCreation(Object dateCreation) {
        this.dateCreation = dateCreation;
    }

    /**
     * 
     * @return
     *     The dateAvailable
     */
    public String getDateAvailable() {
        return dateAvailable;
    }

    /**
     * 
     * @param dateAvailable
     *     The date_available
     */
    public void setDateAvailable(String dateAvailable) {
        this.dateAvailable = dateAvailable;
    }

    /**
     * 
     * @return
     *     The pageUrl
     */
    public String getPageUrl() {
        return pageUrl;
    }

    /**
     * 
     * @param pageUrl
     *     The page_url
     */
    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    /**
     * 
     * @return
     *     The elementUrl
     */
    public String getElementUrl() {
        return elementUrl;
    }

    /**
     * 
     * @param elementUrl
     *     The element_url
     */
    public void setElementUrl(String elementUrl) {
        this.elementUrl = elementUrl;
    }

    /**
     * 
     * @return
     *     The derivatives
     */
    public Derivatives getDerivatives() {
        return derivatives;
    }

    /**
     * 
     * @param derivatives
     *     The derivatives
     */
    public void setDerivatives(Derivatives derivatives) {
        this.derivatives = derivatives;
    }

}
