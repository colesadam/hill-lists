package uk.colessoft.android.hilllist.di.module

import dagger.Module
import dagger.Provides
import uk.colessoft.android.hilllist.ui.activity.Main
import uk.colessoft.android.hilllist.dao.CountryClause
import uk.colessoft.android.hilllist.domain.HillSearch
import uk.colessoft.android.hilllist.ui.activity.HillListMapActivity

@Module
class HillListMapActivityIntentModule {
    @Provides
    fun providesHillSearch(activity: HillListMapActivity): HillSearch {
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
        val moreFilters = activity.intent.getSerializableExtra("moreWhen") as String?

        return HillSearch(hillType, country, moreFilters)
    }

}