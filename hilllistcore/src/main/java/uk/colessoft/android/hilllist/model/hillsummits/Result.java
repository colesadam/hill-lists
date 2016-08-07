
package uk.colessoft.android.hilllist.model.hillsummits;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Result {

    @SerializedName("paging")
    @Expose
    private Paging paging;
    @SerializedName("images")
    @Expose
    private List<uk.colessoft.android.hilllist.model.hillsummits.Image> images = new ArrayList<uk.colessoft.android.hilllist.model.hillsummits.Image>();

    /**
     * 
     * @return
     *     The paging
     */
    public Paging getPaging() {
        return paging;
    }

    /**
     * 
     * @param paging
     *     The paging
     */
    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    /**
     * 
     * @return
     *     The images
     */
    public List<uk.colessoft.android.hilllist.model.hillsummits.Image> getImages() {
        return images;
    }

    /**
     * 
     * @param images
     *     The images
     */
    public void setImages(List<uk.colessoft.android.hilllist.model.hillsummits.Image> images) {
        this.images = images;
    }

}
