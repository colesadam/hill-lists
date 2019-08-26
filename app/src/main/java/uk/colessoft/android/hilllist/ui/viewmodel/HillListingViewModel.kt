package uk.colessoft.android.hilllist.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import uk.colessoft.android.hilllist.dao.HillsOrder
import uk.colessoft.android.hilllist.dao.IsHillClimbed
import uk.colessoft.android.hilllist.domain.HillDetail
import java.util.*

interface HillListingViewModel {



    open fun sortHills(hills: List<HillDetail>, currentOrder: HillsOrder): List<HillDetail>? {
        return when (currentOrder) {
            HillsOrder.HEIGHT_ASC -> hills.sortedBy { it.hill.heightm }
            HillsOrder.HEIGHT_DESC -> hills.sortedByDescending { it.hill.heightm }

            HillsOrder.ID_ASC -> hills.sortedBy { it.hill.h_id }
            HillsOrder.ID_DESC -> hills.sortedByDescending { it.hill.h_id }

            HillsOrder.NAME_ASC -> hills.sortedBy { it.hill.hillname }
            HillsOrder.NAME_DESC -> hills.sortedByDescending { it.hill.hillname }
        }
    }




}