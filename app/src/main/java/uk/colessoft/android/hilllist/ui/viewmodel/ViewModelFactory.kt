package uk.colessoft.android.hilllist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import dagger.Lazy

class ViewModelFactory<VM : ViewModel> @Inject constructor(private val viewModel: Lazy<VM>) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModel.get() as T
    }
}