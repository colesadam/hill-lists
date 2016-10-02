package uk.colessoft.android.hilllist.model;


import java.util.ArrayList;
import java.util.List;

public class GeographImageSearchResponse {

    public List<GeographImageDetail> getItems() {
        return items;
    }

    public void setItems(List<GeographImageDetail> items) {
        this.items = items;
    }

    private List<GeographImageDetail> items;

    public List<String> getThumbnailUrls() {

        List<String> thumbnailUrls = new ArrayList<>();

        for(GeographImageDetail detail:items){
            thumbnailUrls.add(detail.getThumb());
        }

        return thumbnailUrls;
    }
}
