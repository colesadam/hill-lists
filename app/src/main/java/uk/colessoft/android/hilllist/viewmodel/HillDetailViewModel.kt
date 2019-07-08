package uk.colessoft.android.hilllist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uk.colessoft.android.hilllist.dao.CountryClause
import uk.colessoft.android.hilllist.dao.HillsOrder
import uk.colessoft.android.hilllist.dao.IsHillClimbed
import uk.colessoft.android.hilllist.database.BritishHillsDatasource
import uk.colessoft.android.hilllist.entity.Hill
import uk.colessoft.android.hilllist.model.HillDetail
import javax.inject.Inject


class HillDetailViewModel : ViewModel() {

    @Inject
    lateinit var repository: BritishHillsDatasource

    private val selected= MutableLiveData<HillDetail>()
    lateinit var hills:LiveData<List<HillDetail>>


    fun getHills(hillId: Long?, groupId: String?, country: CountryClause?, climbed: IsHillClimbed?, moreFilters: String?, orderBy: HillsOrder){
        repository.getHills(groupId,country,climbed,moreFilters,orderBy)
    }

    fun select(hill: HillDetail) {
        selected.value=hill
    }
}