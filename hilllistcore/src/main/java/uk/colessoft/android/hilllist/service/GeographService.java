package uk.colessoft.android.hilllist.service;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import uk.colessoft.android.hilllist.model.GeographImageSearchResponse;

public interface GeographService {

    @GET("syndicator.php?format=JSON&key=uk.colessoft.android.hilllist&perpage=100")
    Call<GeographImageSearchResponse> imageSearch(@Query("q") String latLong);
}
