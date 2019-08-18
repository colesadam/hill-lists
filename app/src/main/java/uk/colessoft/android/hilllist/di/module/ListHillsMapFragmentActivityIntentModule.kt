package uk.colessoft.android.hilllist.di.module

import dagger.Module
import dagger.Provides
import uk.colessoft.android.hilllist.ui.activity.HillListFragmentActivity
import uk.colessoft.android.hilllist.ui.activity.Main
import uk.colessoft.android.hilllist.dao.CountryClause
import uk.colessoft.android.hilllist.domain.HillSearch
import uk.colessoft.android.hilllist.ui.activity.ListHillsMapFragmentActivity

@Module
class ListHillsMapFragmentActivityIntentModule {
    @Provides
    fun providesHillSearch(activity: ListHillsMapFragmentActivity): HillSearch {
        val hillType: String? = activity.intent.getSerializableExtra("hilltype") as String?
        val country = {
            val c: Int? = activity.intent.getSerializableExtra("country") as Int?
            val x: CountryClause = when (c) {
                Main.ENGLAND -> CountryClause.ENGLAND
                Main.SCOTLAND -> CountryClause.SCOTLAND
                Main.WALES -> CountryClause.WALES
                else -> CountryClause.UK
            }
            x
        }.invoke()
        val moreFilters = null

        return HillSearch(hillType, country, moreFilters)
    }

}