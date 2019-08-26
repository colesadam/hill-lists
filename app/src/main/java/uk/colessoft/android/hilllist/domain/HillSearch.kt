package uk.colessoft.android.hilllist.domain

import uk.colessoft.android.hilllist.dao.CountryClause

data class HillSearch(val groupId: String?, val country: CountryClause?, val moreFilters: String?, val distance: Int? = null)