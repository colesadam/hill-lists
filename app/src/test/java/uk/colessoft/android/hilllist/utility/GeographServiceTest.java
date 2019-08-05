package uk.colessoft.android.hilllist.utility;


import org.junit.Test;

import java.io.IOException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.colessoft.android.hilllist.domain.GeographImageSearchResponse;
import uk.colessoft.android.hilllist.service.GeographService;

import static org.junit.Assert.assertTrue;

public class GeographServiceTest {

    @Test
    public void test() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("").toString())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mockWebServer.enqueue(new MockResponse().setBody("{\n" +
                "  \"generator\": \"FeedCreator 1.7.10(BH)\",\n" +
                "  \"title\": \"Geograph Britain and Ireland\",\n" +
                "  \"description\": \"Images, within 10km of 55:0.0000N, 1:30.0000W (15468 in total)\",\n" +
                "  \"link\": \"http://www.geograph.org.uk/search.php?i=66231596\",\n" +
                "  \"syndicationURL\": \"http://www.geograph.org.uk/feed/results/66231596.json\",\n" +
                "  \"nextURL\": \"http://www.geograph.org.uk/feed/results/66231596/2.json\",\n" +
                "  \"icon\": \"http://s1.geograph.org.uk/templates/basic/img/logo.gif\",\n" +
                "  \"date\": \"2016-09-27T15:30:20+00:00\",\n" +
                "  \"items\": [\n" +
                "    {\n" +
                "      \"title\": \"NZ3267 : Wallsend Labour Club\",\n" +
                "      \"description\": \"Dist:0.2km<br/>\",\n" +
                "      \"link\": \"http://www.geograph.org.uk/photo/591479\",\n" +
                "      \"author\": \"Mac McCarron\",\n" +
                "      \"category\": \"Working Men's Club\",\n" +
                "      \"guid\": \"591479\",\n" +
                "      \"source\": \"http://www.geograph.org.uk/profile/15634\",\n" +
                "      \"date\": 1193079111,\n" +
                "      \"imageTaken\": \"2007-10-20\",\n" +
                "      \"lat\": \"55.001815\",\n" +
                "      \"long\": \"-1.499542\",\n" +
                "      \"thumb\": \"http://s3.geograph.org.uk/photos/59/14/591479_8976d34a_120x120.jpg\",\n" +
                "      \"thumbTag\": \"<img alt=\\\"NZ3267 : Wallsend Labour Club by Mac McCarron\\\" src=\\\"http://s3.geograph.org.uk/photos/59/14/591479_8976d34a_120x120.jpg\\\" width=\\\"120\\\" height=\\\"80\\\" />\",\n" +
                "      \"licence\": \"http://creativecommons.org/licenses/by-sa/2.0/\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"title\": \"NZ3167 : The Rosehill Tavern\",\n" +
                "      \"description\": \"Dist:0.3km<br/>\",\n" +
                "      \"link\": \"http://www.geograph.org.uk/photo/591466\",\n" +
                "      \"author\": \"Mac McCarron\",\n" +
                "      \"category\": \"Public house\",\n" +
                "      \"guid\": \"591466\",\n" +
                "      \"source\": \"http://www.geograph.org.uk/profile/15634\",\n" +
                "      \"date\": 1193078576,\n" +
                "      \"imageTaken\": \"2007-10-20\",\n" +
                "      \"lat\": \"54.998546\",\n" +
                "      \"long\": \"-1.504288\",\n" +
                "      \"thumb\": \"http://s2.geograph.org.uk/photos/59/14/591466_11e05189_120x120.jpg\",\n" +
                "      \"thumbTag\": \"<img alt=\\\"NZ3167 : The Rosehill Tavern by Mac McCarron\\\" src=\\\"http://s2.geograph.org.uk/photos/59/14/591466_11e05189_120x120.jpg\\\" width=\\\"120\\\" height=\\\"80\\\" />\",\n" +
                "      \"licence\": \"http://creativecommons.org/licenses/by-sa/2.0/\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"title\": \"NZ3167 : The Rosehill Tavern\",\n" +
                "      \"description\": \"Dist:0.3km<br/>\",\n" +
                "      \"link\": \"http://www.geograph.org.uk/photo/2973706\",\n" +
                "      \"author\": \"JThomas\",\n" +
                "      \"guid\": \"2973706\",\n" +
                "      \"source\": \"http://www.geograph.org.uk/profile/35313\",\n" +
                "      \"date\": 1338675376,\n" +
                "      \"imageTaken\": \"2012-06-02\",\n" +
                "      \"tags\": \"top:Business, Retail, Services?public house\",\n" +
                "      \"lat\": \"54.998240\",\n" +
                "      \"long\": \"-1.504135\",\n" +
                "      \"thumb\": \"http://s2.geograph.org.uk/geophotos/02/97/37/2973706_16853320_120x120.jpg\",\n" +
                "      \"thumbTag\": \"<img alt=\\\"NZ3167 : The Rosehill Tavern by JThomas\\\" src=\\\"http://s2.geograph.org.uk/geophotos/02/97/37/2973706_16853320_120x120.jpg\\\" width=\\\"120\\\" height=\\\"90\\\" />\",\n" +
                "      \"licence\": \"http://creativecommons.org/licenses/by-sa/2.0/\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"title\": \"NZ3267 : Tynemouth Road\",\n" +
                "      \"description\": \"Dist:0.3km<br/>Junction with Lisle Grove.\",\n" +
                "      \"link\": \"http://www.geograph.org.uk/photo/4866403\",\n" +
                "      \"author\": \"Richard Webb\",\n" +
                "      \"guid\": \"4866403\",\n" +
                "      \"source\": \"http://www.geograph.org.uk/profile/196\",\n" +
                "      \"date\": 1458161701,\n" +
                "      \"imageTaken\": \"2015-10-01\",\n" +
                "      \"tags\": \"top:Suburb, Urban fringe\",\n" +
                "      \"lat\": \"54.997262\",\n" +
                "      \"long\": \"-1.497895\",\n" +
                "      \"thumb\": \"http://s3.geograph.org.uk/geophotos/04/86/64/4866403_4dd1048d_120x120.jpg\",\n" +
                "      \"thumbTag\": \"<img alt=\\\"NZ3267 : Tynemouth Road by Richard Webb\\\" src=\\\"http://s3.geograph.org.uk/geophotos/04/86/64/4866403_4dd1048d_120x120.jpg\\\" width=\\\"120\\\" height=\\\"90\\\" />\",\n" +
                "      \"licence\": \"http://creativecommons.org/licenses/by-sa/2.0/\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"title\": \"NZ3267 : The Mauretania\",\n" +
                "      \"description\": \"Dist:0.4km<br/>Named after one of the most impressive ships to be built at Swan Hunter\",\n" +
                "      \"link\": \"http://www.geograph.org.uk/photo/591474\",\n" +
                "      \"author\": \"Mac McCarron\",\n" +
                "      \"category\": \"Public house\",\n" +
                "      \"guid\": \"591474\",\n" +
                "      \"source\": \"http://www.geograph.org.uk/profile/15634\",\n" +
                "      \"date\": 1193078876,\n" +
                "      \"imageTaken\": \"2007-10-20\",\n" +
                "      \"lat\": \"55.003015\",\n" +
                "      \"long\": \"-1.498433\",\n" +
                "      \"thumb\": \"http://s2.geograph.org.uk/photos/59/14/591474_25fcf6f1_120x120.jpg\",\n" +
                "      \"thumbTag\": \"<img alt=\\\"NZ3267 : The Mauretania by Mac McCarron\\\" src=\\\"http://s2.geograph.org.uk/photos/59/14/591474_25fcf6f1_120x120.jpg\\\" width=\\\"120\\\" height=\\\"80\\\" />\",\n" +
                "      \"licence\": \"http://creativecommons.org/licenses/by-sa/2.0/\"\n" +
                "    }\n" +
                "  ]\n" +
                "}"));

        GeographService service = retrofit.create(GeographService.class);

        Call<GeographImageSearchResponse> call = service.imageSearch("latlong");
        GeographImageSearchResponse response = call.execute().body();
        assertTrue(response.getItems().size()==5);

        mockWebServer.shutdown();
    }
}
