package uk.colessoft.android.hilllist.viewmodel

import javax.inject.Inject
import javax.inject.Provider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HillDetailViewModelFactory @Inject
constructor(private val repositoryProvider: Provider<uk.colessoft.android.hilllist.database.BritishHillsDatasource>) {
    private class HillDetailViewModelFactory(private val repository: uk.colessoft.android.hilllist.database.BritishHillsDatasource, private val hillId: Long) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HillDetailViewModel(repository, hillId) as T
        }
    }

    fun create(hillId: Long): ViewModelProvider.Factory {
        return HillDetailViewModelFactory(repositoryProvider.get(), hillId)
    }
}
