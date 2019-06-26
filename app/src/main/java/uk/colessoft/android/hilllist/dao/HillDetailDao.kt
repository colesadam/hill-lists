package uk.colessoft.android.hilllist.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SimpleSQLiteQuery
import uk.colessoft.android.hilllist.entity.Bagging.Companion.KEY_DATECLIMBED
import uk.colessoft.android.hilllist.model.HillDetail


@Dao
abstract class HillDetailDao {

    val hillQuery: String
        get() {
            return """SELECT h_id, latitude, longitude,Metres,Feet,
            name,notes,dateClimbed,b_id from hills
            JOIN typeslink ON typeslink.hill_id = h_id
            LEFT JOIN bagging ON b_id = h_id
            JOIN hilltypes ON typeslink.type_Id = ht_id"""
        }

    @Transaction
    @Query("SELECT * FROM hills WHERE h_id = :hillId")
    abstract fun getHillDetail(hillId: Long): LiveData<HillDetail>

    @RawQuery
    abstract fun getHillsRaw(query: SimpleSQLiteQuery): LiveData<List<HillDetail>>


    fun getHills(groupId: String?, orderBy: HillsOrder = HillsOrder.HEIGHT_DESC, country: CountryClause?, climbed: IsHillClimbed?): LiveData<List<HillDetail>> {
        return getHillsRaw(
                SimpleSQLiteQuery("$hillQuery " + getWhereClause(groupId, country, null, climbed)
                        + " order by " + orderBy.sql, arrayOf()))
    }

    private fun getWhereClause(groupId: String?, country: CountryClause?, moreFilters: String?, climbed: IsHillClimbed?): String {
        if ("T100" == groupId) return getT100(moreFilters, climbed)

        var where = climbed?.sql ?: ""

        if (groupId != null) {
            val groups = groupId.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            var groupSelector = ""
            for (group in groups) {
                groupSelector += "title = '" + group + "' OR "
            }
            groupSelector = groupSelector.trim { it <= ' ' }.substring(0, groupSelector.length - 3)
            where = addToWhere("($groupSelector)", where)
        }

        where = country?.let {addToWhere(country?.sql, where)} ?: where
        where = addToWhere(moreFilters, where)

        return "where " + where
    }

    private fun getT100(moreFilters: String?, climbed: IsHillClimbed?): String {

        var where = "T100='1'"

        where = climbed?.let{addToWhere(climbed?.sql,where)} ?: where
        where = addToWhere(moreFilters, where)

        return "$hillQuery where " + where
    }

    private fun addToWhere(filter: String?, where: String): String {
        var where = where
        if ("" != where && filter != null && "" != filter)
            where = "$where AND "
        where = where + (filter ?: "")
        return where
    }


}