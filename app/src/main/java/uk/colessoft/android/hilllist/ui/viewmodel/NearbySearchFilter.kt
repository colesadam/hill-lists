package uk.colessoft.android.hilllist.ui.viewmodel

import uk.colessoft.android.hilllist.dao.HillsOrder
import uk.colessoft.android.hilllist.dao.IsHillClimbed


data class NearbySearchFilter(val climbed: IsHillClimbed?, val searchString: String?, val order: NearbyHillsOrder, val distance: Float) {
}