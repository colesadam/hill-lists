package uk.colessoft.android.hilllist.domain


import java.util.ArrayList

class GeographImageSearchResponse {

    var items: List<GeographImageDetail>? = null

    val thumbnailUrls: List<String>
        get() {

            val thumbnailUrls = ArrayList<String>()

            for (detail in items!!) {
                thumbnailUrls.add(detail.thumb.orEmpty())
            }

            return thumbnailUrls
        }
}
