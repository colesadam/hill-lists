package uk.colessoft.android.hilllist.ui.viewmodel

import android.location.Location
import android.util.Log
import android.util.Pair
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import uk.colessoft.android.hilllist.dao.HillsOrder
import uk.colessoft.android.hilllist.dao.IsHillClimbed
import uk.colessoft.android.hilllist.database.BritishHillsDatasource
import uk.colessoft.android.hilllist.domain.HillDetail
import uk.colessoft.android.hilllist.domain.HillSearch
import java.util.*
import javax.inject.Inject

class NearbyHillListViewModel @Inject constructor(override val repository: BritishHillsDatasource) : HillHoldingViewModel(),HillListingViewModel {

    override val selected : LiveData<HillDetail>? = null
    var location = MutableLiveData<Location>()
    val hills: LiveData<List<Pair<Double,HillDetail>>>

    init {
            hills = Transformations.switchMap<Location?, List<Pair<Double,HillDetail>>>(location
            ){
                loc -> repository.getHills(loc!!.latitude, loc.longitude, 16.0934F)
            }
    }


    fun markAllHillsClimbed() {
        hills.value?.forEach { pv -> repository.markHillClimbed(pv.second.hill.h_id, Date(), "") }
    }

    fun markAllHillsNotClimbed() {
        hills.value?.forEach({ pv -> repository.markHillNotClimbed(pv.second.hill.h_id) })
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