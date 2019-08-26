package uk.colessoft.android.hilllist.di.module

import dagger.Module
import dagger.Provides
import uk.colessoft.android.hilllist.ui.activity.HillListActivity
import uk.colessoft.android.hilllist.ui.activity.Main
import uk.colessoft.android.hilllist.dao.CountryClause
import uk.colessoft.android.hilllist.domain.HillSearch
import uk.colessoft.android.hilllist.ui.activity.NearbyHillsActivity

@Module
class NearbyHillsActivityIntentModule {
    @Provides
    fun providesHillSearch(activity: NearbyHillsActivity): HillSearch {

        val distance = 16
        return HillSearch(null, null, null, distance)
    }

}
