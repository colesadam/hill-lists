package uk.colessoft.android.hilllist.ui.viewmodel

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


class HillDetailViewModel @Inject constructor(private val repository: BritishHillsDatasource, val hillSearch: HillSearch) : ViewModel() {


    private val selected = MutableLiveData<HillDetail>()
    val hills = MediatorLiveData<List<HillDetail>>()
    private var currentOrder = HillsOrder.HEIGHT_DESC
    private var filterClimbed: IsHillClimbed? = null

    init {
        hills.addSource(repository.getHills(hillSearch.groupId, hillSearch.country, hillSearch.moreFilters)) { result: List<HillDetail>? ->
            result?.let { hills.value = sortHills(it, currentOrder) }
        }
    }


    private fun sortHills(hills: List<HillDetail>, currentOrder: HillsOrder): List<HillDetail>? {
        return when (currentOrder) {
            HillsOrder.HEIGHT_ASC -> hills.sortedBy { it.hill.heightm }
            HillsOrder.HEIGHT_DESC -> hills.sortedByDescending { it.hill.heightm }

            HillsOrder.ID_ASC -> hills.sortedBy { it.hill.h_id }
            HillsOrder.ID_DESC -> hills.sortedByDescending { it.hill.h_id }

            HillsOrder.NAME_ASC -> hills.sortedBy { it.hill.hillname }
            HillsOrder.NAME_DESC -> hills.sortedByDescending { it.hill.hillname }
        }
    }

    fun markHillClimbed(bagging: Bagging) {
        repository.markHillClimbedRoom(bagging.b_id, bagging.dateClimbed, bagging.notes)
    }

    fun markHillNotClimbed(hillId: Long) {
        repository.markHillNotClimbedRoom(hillId)
    }

    fun markAllHillsClimbed() {
        hills.value?.forEach { hill -> repository.markHillClimbedRoom(hill.hill.h_id, Date(), "") }
    }

    fun markAllHillsNotClimbed() {
        hills.value?.forEach({ hill -> repository.markHillNotClimbedRoom(hill.hill.h_id) })
    }

    fun orderHills(order: HillsOrder) = hills.value?.let {
        hills.setValue(sortHills(it, order))
        //hills.value = sortHills(it, order)
    }.also { currentOrder = order }

    fun filterClimbed(climbed: IsHillClimbed) {
        hills.value = hills.value?.filter {
            if (it.bagging == null)
                true
            else {
                if (climbed == IsHillClimbed.YES) {
                    it.bagging!!.isNotEmpty()
                } else it.bagging!!.isEmpty()
            }
        }

    }

    fun reset() {
        hills.value
    }

    fun select(hill: HillDetail) {
        selected.value = hill
    }

    fun setHills(newHills: List<HillDetail>) {
        hills.value = newHills
    }
}