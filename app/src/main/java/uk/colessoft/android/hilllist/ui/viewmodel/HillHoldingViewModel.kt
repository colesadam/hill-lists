package uk.colessoft.android.hilllist.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import uk.colessoft.android.hilllist.domain.HillDetail

abstract class HillHoldingViewModel: ViewModel() {
    abstract val selected : LiveData<HillDetail>
}