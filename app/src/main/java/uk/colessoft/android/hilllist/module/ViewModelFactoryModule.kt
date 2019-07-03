package uk.colessoft.android.hilllist.module

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import uk.colessoft.android.hilllist.viewmodel.HillDetailViewModelFactory


@Module
abstract class ViewModelFactoryModule {
    @Binds
    @JvmSuppressWildcards(true)
    abstract fun bindViewModelFactory(viewModelFactory: HillDetailViewModelFactory): HillDetailViewModelFactory
}