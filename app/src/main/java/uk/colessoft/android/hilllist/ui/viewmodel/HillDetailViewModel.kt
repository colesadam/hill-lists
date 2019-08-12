package uk.colessoft.android.hilllist.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uk.colessoft.android.hilllist.dao.HillsOrder
import uk.colessoft.android.hilllist.dao.IsHillClimbed
import uk.colessoft.android.hilllist.database.BritishHillsDatasource
import uk.colessoft.android.hilllist.domain.HillDetail
import uk.colessoft.android.hilllist.domain.HillSearch
import uk.colessoft.android.hilllist.domain.entity.Bagging
import java.util.*
import javax.inject.Inject
import androidx.lifecycle.Transformations


class HillDetailViewModel @Inject constructor(override val repository: BritishHillsDatasource, val hillId: Long) :  HillHoldingViewModel() {


    override val selected : LiveData<HillDetail>


    init {
        selected = repository.getHillReactive(hillId)
    }


}