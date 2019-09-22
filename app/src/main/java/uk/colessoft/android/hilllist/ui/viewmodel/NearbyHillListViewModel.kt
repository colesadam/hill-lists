package uk.colessoft.android.hilllist.ui.viewmodel

import android.location.Location
import android.util.Pair
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import uk.colessoft.android.hilllist.dao.IsHillClimbed
import uk.colessoft.android.hilllist.database.BritishHillsDatasource
import uk.colessoft.android.hilllist.domain.HillDetail
import java.util.*
import javax.inject.Inject

class NearbyHillListViewModel @Inject constructor(override val repository: BritishHillsDatasource) : HillHoldingViewModel(), HillListingViewModel {

    override val selected: LiveData<HillDetail>? = null
    private var currentOrder = NearbyHillsOrder.DIST_ASC
    private var currentDistance = 16.093F
    private var filterClimbed: IsHillClimbed? = null
    private var searchString: String? = null

    var location = MutableLiveData<Location>()
    val hills: LiveData<List<Pair<Double, HillDetail>>?>


    private val filterLiveData = MutableLiveData<NearbySearchFilter>()
    private val selectLiveData = MutableLiveData<Long>()

    init {
        hills = Transformations.switchMap(DoubleTrigger(location,filterLiveData))
        { locFilterPair ->
            {
                Transformations.map<List<Pair<Double, HillDetail>>?, List<Pair<Double, HillDetail>>?>(
                        repository.getHills(locFilterPair!!.first!!.latitude, locFilterPair.first!!.longitude, currentDistance)) { result ->

                    result?.let{sortNearbyHills(result, currentOrder)}!!.filter {
                        when(filterClimbed) {
                            IsHillClimbed.YES -> it.second!!.bagging!!.isNotEmpty()
                            IsHillClimbed.NO -> it.second!!.bagging!!.isNullOrEmpty()
                            else -> true
                        } }.filter { if(searchString == null) true else it.second.hill.hillname!!.contains(searchString!!,true) }
                }
            }.invoke()
        }
    }


    fun markAllHillsClimbed() {
        hills.value?.forEach { pv -> repository.markHillClimbed(pv.second.hill.h_id, Date(), "") }
    }

    fun markAllHillsNotClimbed() {
        hills.value?.forEach({ pv -> repository.markHillNotClimbed(pv.second.hill.h_id) })
    }

    fun orderHills(order: NearbyHillsOrder) {
        currentOrder = order
        location.value = location.value
    }

    fun filterClimbed(climbed: IsHillClimbed?) {
        filterClimbed = climbed
        filterLiveData.value = NearbySearchFilter(filterClimbed, searchString, currentOrder, currentDistance)
    }

    fun searchHills(search: String?) {
        this.searchString = search
        filterLiveData.value = NearbySearchFilter(filterClimbed, searchString, currentOrder, currentDistance)
    }

    fun filterDistance(distance: Float){
        currentDistance = distance
        filterLiveData.value = NearbySearchFilter(filterClimbed, searchString, currentOrder, currentDistance)
    }


    fun select(hillId: Long) {
        selectLiveData.value = hillId
    }

    fun sortNearbyHills(hills: List<Pair<Double, HillDetail>>, currentOrder: NearbyHillsOrder): List<Pair<Double,HillDetail>>? {
        return when (currentOrder) {
            NearbyHillsOrder.HEIGHT_ASC -> hills.sortedBy { it.second.hill.heightm }
            NearbyHillsOrder.HEIGHT_DESC -> hills.sortedByDescending { it.second.hill.heightm }

            NearbyHillsOrder.ID_ASC -> hills.sortedBy { it.second.hill.h_id }
            NearbyHillsOrder.ID_DESC -> hills.sortedByDescending { it.second.hill.h_id }

            NearbyHillsOrder.NAME_ASC -> hills.sortedBy { it.second.hill.hillname }
            NearbyHillsOrder.NAME_DESC -> hills.sortedByDescending { it.second.hill.hillname }

            NearbyHillsOrder.DIST_ASC -> hills.sortedBy { it.first}
            NearbyHillsOrder.DIST_DESC -> hills.sortedByDescending { it.first }
        }
    }

}