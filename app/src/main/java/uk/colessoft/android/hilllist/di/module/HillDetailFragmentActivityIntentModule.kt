package uk.colessoft.android.hilllist.di.module

import dagger.Module
import dagger.Provides
import uk.colessoft.android.hilllist.ui.activity.HillListFragmentActivity
import uk.colessoft.android.hilllist.ui.activity.Main
import uk.colessoft.android.hilllist.dao.CountryClause
import uk.colessoft.android.hilllist.domain.HillSearch
import uk.colessoft.android.hilllist.ui.activity.HillDetailFragmentActivity

@Module
class HillDetailFragmentActivityIntentModule {
    @Provides
    fun providesHillId(activity: HillDetailFragmentActivity): Long {
        val rowid = activity.intent.getSerializableExtra("rowid") as Long


        return rowid
    }

}