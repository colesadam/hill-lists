package uk.colessoft.android.hilllist.model

import uk.colessoft.android.hilllist.dao.CountryClause

data class HillSearch(val groupId: String?, val country: CountryClause?, val moreFilters: String?)