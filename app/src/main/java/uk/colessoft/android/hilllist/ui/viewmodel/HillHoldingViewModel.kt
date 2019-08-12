package uk.colessoft.android.hilllist.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import uk.colessoft.android.hilllist.database.BritishHillsDatasource
import uk.colessoft.android.hilllist.domain.HillDetail
import uk.colessoft.android.hilllist.domain.entity.Bagging

abstract class HillHoldingViewModel: ViewModel() {
    abstract val selected : LiveData<HillDetail>
    abstract val repository: BritishHillsDatasource

    fun markHillClimbed(bagging: Bagging) {
        repository.markHillClimbedRoom(bagging.b_id, bagging.dateClimbed, bagging.notes)
    }

    fun markHillNotClimbed(hillId: Long) {
        repository.markHillNotClimbedRoom(hillId)
    }
}