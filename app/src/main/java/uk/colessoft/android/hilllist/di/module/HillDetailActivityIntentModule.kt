package uk.colessoft.android.hilllist.di.module

import dagger.Module
import dagger.Provides
import uk.colessoft.android.hilllist.ui.activity.HillDetailActivity

@Module
class HillDetailActivityIntentModule {
    @Provides
    fun providesHillId(activity: HillDetailActivity): Long {
        val rowid = activity.intent.getSerializableExtra("rowid") as Long
        return rowid
    }

}