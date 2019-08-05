package uk.colessoft.android.hilllist.di.module

import dagger.Module
import dagger.Provides
import uk.colessoft.android.hilllist.ui.activity.DisplayHillListFragmentActivity
import uk.colessoft.android.hilllist.ui.activity.Main
import uk.colessoft.android.hilllist.dao.CountryClause
import uk.colessoft.android.hilllist.domain.HillSearch

@Module
class DisplayHillListFragmentActivityIntentModule {
    @Provides
    fun providesHillSearch(activity: DisplayHillListFragmentActivity): HillSearch {
        val hillType = activity.intent.getSerializableExtra("hilltype") as String
        val country = {
            val c: Int = activity.intent.getSerializableExtra("country") as Int
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