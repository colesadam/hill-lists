package uk.colessoft.android.hilllist.module

import dagger.Module
import dagger.Provides
import uk.colessoft.android.hilllist.activity.DisplayHillListFragmentActivity
import uk.colessoft.android.hilllist.model.HillSearch

@Module
class DisplayHillListFragmentActivityIntentModule {
    @Provides
    fun providesHillSearch(activity: DisplayHillListFragmentActivity): HillSearch {
        val hillType = activity.intent.getSerializableExtra("hilltype") as String
        val country = null
        val moreFilters = null

        return HillSearch(hillType, country, moreFilters)
    }
}