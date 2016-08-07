package uk.colessoft.android.hilllist.service;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;
import uk.colessoft.android.hilllist.model.hillsummits.Example;
import uk.colessoft.android.hilllist.model.hillsummits.Result;

public interface HillSummitsApi {

    @GET("/ws.php")
    Observable<Example> search(@Query("query") String query,@Query("format") String format,@Query("method") String method);

}
