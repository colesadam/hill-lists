package uk.colessoft.android.hilllist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import uk.colessoft.android.hilllist.database.BritishHillsDatasource
import uk.colessoft.android.hilllist.model.HillDetail


class HillDetailViewModel(private val repository: BritishHillsDatasource, private val hillId: Long): ViewModel() {

    public val hillDetail:LiveData<HillDetail>

    init {
        hillDetail = repository.getHillReactive(hillId)
    }
}