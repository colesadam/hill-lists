package uk.colessoft.android.hilllist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import uk.colessoft.android.hilllist.database.BritishHillsDatasource
import uk.colessoft.android.hilllist.model.HillDetail
import javax.inject.Inject


class HillDetailViewModel(application: Application): AndroidViewModel(application) {

    val hillDetail: LiveData<HillDetail>

    @Inject
    private lateinit var datasource: BritishHillsDatasource

    init {
        hillDetail = datasource.getHillReactive(1)
    }
}