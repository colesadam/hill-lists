package uk.colessoft.android.hilllist.ui.viewmodel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uk.colessoft.android.hilllist.database.BritishHillsDatasource
import uk.colessoft.android.hilllist.domain.HillDetail
import uk.colessoft.android.hilllist.domain.HillSearch
import javax.inject.Inject

class NearbyHillListViewModel @Inject constructor(override val repository: BritishHillsDatasource) : HillHoldingViewModel(),HillListingViewModel {

    override val selected : LiveData<HillDetail>? = null
    private var location = MutableLiveData<Location>()
    val hills: LiveData<List<HillDetail>>

    init {
            hills = repository.getHills(null,null,0F)
    }



}