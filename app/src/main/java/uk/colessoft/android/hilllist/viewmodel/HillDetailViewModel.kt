package uk.colessoft.android.hilllist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uk.colessoft.android.hilllist.dao.CountryClause
import uk.colessoft.android.hilllist.dao.HillsOrder
import uk.colessoft.android.hilllist.dao.IsHillClimbed
import uk.colessoft.android.hilllist.database.BritishHillsDatasource
import uk.colessoft.android.hilllist.entity.Hill
import uk.colessoft.android.hilllist.model.HillDetail
import uk.colessoft.android.hilllist.model.HillSearch
import javax.inject.Inject


class HillDetailViewModel @Inject constructor(private val repository: BritishHillsDatasource, val hillSearch: HillSearch) : ViewModel() {


    private val selected = MutableLiveData<HillDetail>()
    val hills = MediatorLiveData<List<HillDetail>>()
    private var currentOrder = HillsOrder.HEIGHT_DESC
    private var isClimbed = null

    init {
        hills.addSource(repository.getHills(hillSearch.groupId, hillSearch.country, hillSearch.moreFilters)) { result: List<HillDetail>? ->
            result?.let { hills.value = sortHills(it, currentOrder) }
        }
    }

    fun getSelectedHills(hillId: Long?, groupId: String?, country: CountryClause?, moreFilters: String?) {

        hills.addSource(repository.getHills(groupId, country, moreFilters)) { result: List<HillDetail>? ->
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

    fun orderHills(order: HillsOrder) = hills.value?.let {
        hills.value = sortHills(it, order)
    }.also { currentOrder = order }

    fun filterClimbed(climbed: IsHillClimbed) {
        hills.value = hills.value?.filter {

            if (climbed == IsHillClimbed.YES)
                it.bagging != null
            else it.bagging == null
        }

    }

    fun select(hill: HillDetail) {
        selected.value = hill
    }

    fun setHills(newHills: List<HillDetail>) {
        hills.value = newHills
    }
}