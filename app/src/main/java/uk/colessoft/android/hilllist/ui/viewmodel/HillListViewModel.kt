package uk.colessoft.android.hilllist.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uk.colessoft.android.hilllist.dao.HillsOrder
import uk.colessoft.android.hilllist.dao.IsHillClimbed
import uk.colessoft.android.hilllist.database.BritishHillsDatasource
import uk.colessoft.android.hilllist.domain.HillDetail
import uk.colessoft.android.hilllist.domain.HillSearch
import java.util.*
import javax.inject.Inject
import androidx.lifecycle.Transformations


class HillListViewModel @Inject constructor(override val repository: BritishHillsDatasource, val hillSearch: HillSearch) : HillHoldingViewModel(),HillListingViewModel {

    val hills: LiveData<List<HillDetail>>

    override val selected : LiveData<HillDetail>
    private var currentOrder = HillsOrder.HEIGHT_DESC
    private var filterClimbed: IsHillClimbed? = null
    var searchString: String? = null
    private val filterLiveData = MutableLiveData<SearchFilter>()
    private val selectLiveData = MutableLiveData<Long>()

    init {

        hills = Transformations.switchMap<SearchFilter?, List<HillDetail>>(filterLiveData
        ) { filter ->
            {
                val whereClause = {
                    val climbedSql = filter?.climbed?.sql ?: ""
                    val searchSql = {
                        if(hillSearch.moreFilters != null) {
                                    hillSearch.moreFilters
                                } else filter?.searchString?.let { "${filter?.searchString}" }
                            ?: ""}.invoke()
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


    fun markAllHillsClimbed() {
        hills.value?.forEach { hill -> repository.markHillClimbed(hill.hill.h_id, Date(), "") }
    }

    fun markAllHillsNotClimbed() {
        hills.value?.forEach({ hill -> repository.markHillNotClimbed(hill.hill.h_id) })
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


    fun select(hillId: Long) {
        selectLiveData.value = hillId
    }

}