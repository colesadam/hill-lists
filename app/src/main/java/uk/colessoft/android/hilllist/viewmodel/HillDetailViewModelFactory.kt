package uk.colessoft.android.hilllist.viewmodel

import javax.inject.Inject
import javax.inject.Provider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uk.colessoft.android.hilllist.dao.CountryClause
import uk.colessoft.android.hilllist.dao.HillsOrder
import uk.colessoft.android.hilllist.dao.IsHillClimbed
import uk.colessoft.android.hilllist.database.BritishHillsDatasource

class HillDetailViewModelFactory @Inject
constructor(private val repositoryProvider: Provider<BritishHillsDatasource>) {
    private class HillDetailViewModelFactory(private val repository: BritishHillsDatasource, private val hillId: Long, private val groupId: String?,
                                             private val country: CountryClause?, private val climbed: IsHillClimbed?, private val moreFilters: String?,
                                             private val orderBy: HillsOrder) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HillDetailViewModel(repository, hillId, groupId, country, climbed, moreFilters, orderBy) as T
        }
    }

    fun create(hillId: Long, groupId: String?, country: CountryClause?, climbed: IsHillClimbed?, moreFilters: String?, orderBy: HillsOrder): ViewModelProvider.Factory {
        return HillDetailViewModelFactory(repositoryProvider.get(), hillId, groupId, country, climbed,
                moreFilters, orderBy)
    }
}
