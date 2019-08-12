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


class HillListViewModel @Inject constructor(override val repository: BritishHillsDatasource, val hillSearch: HillSearch) : HillHoldingViewModel() {


    override val selected : LiveData<HillDetail>
    val hills: LiveData<List<HillDetail>>
    private var currentOrder = HillsOrder.HEIGHT_DESC
    private var filterClimbed: IsHillClimbed? = null
    private var searchString: String? = null
    private val filterLiveData = MutableLiveData<SearchFilter>()
    private val selectLiveData = MutableLiveData<Long>()

    init {

        hills = Transformations.switchMap<SearchFilter?, List<HillDetail>>(filterLiveData
        ) { filter ->
            {
                val whereClause = {
                    val climbedSql = filter?.climbed?.sql ?: ""
                    val searchSql = filter?.searchString?.let { "${filter?.searchString}" }
                            ?: ""
                    val and = filter?.climbed?.sql?.let { " AND " } ?: ""
                    "$climbedSql$and$searchSql"
                }.invoke()

                Transformations.map<MutableList<HillDetail>?, List<HillDetail>?>(
                        repository.getHills(hillSearch.groupId, hillSearch.country, whereClause)) { result -> result?.let {
                    sortHills(it, currentOrder) }.also {
                    if(selectLiveData.value == null) selectLiveData.postValue(result!!.get(0).hill.h_id)}
                }
            }.invoke()
        }
        filterLiveData.postValue(SearchFilter(null, null, HillsOrder.HEIGHT_DESC))

        selected = Transformations.switchMap(selectLiveData){
            hillId -> repository.getHillReactive(hillId)
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


    fun markAllHillsClimbed() {
        hills.value?.forEach { hill -> repository.markHillClimbedRoom(hill.hill.h_id, Date(), "") }
    }

    fun markAllHillsNotClimbed() {
        hills.value?.forEach({ hill -> repository.markHillNotClimbedRoom(hill.hill.h_id) })
    }

    fun orderHills(order: HillsOrder) {
        Log.d("order=", order.sql)
        currentOrder = order
        filterLiveData.value = SearchFilter(filterClimbed, searchString, currentOrder)
    }

    fun filterClimbed(climbed: IsHillClimbed?) {
        filterClimbed = climbed
        filterLiveData.value = SearchFilter(filterClimbed, searchString, currentOrder)
    }

    fun searchHills(search: String?) {
        this.searchString = search
        filterLiveData.value = SearchFilter(filterClimbed, searchString, currentOrder)
    }

    fun reset() {
        hills.value
    }

    fun select(hill: HillDetail) {
        selectLiveData.value = hill.hill.h_id
    }

}