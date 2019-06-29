package uk.colessoft.android.hilllist.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SimpleSQLiteQuery
import uk.colessoft.android.hilllist.model.HillDetail


@Dao
abstract class HillDetailDao {

    val hillQuery: String
        get() {
            return """SELECT DISTINCT h_id, latitude, longitude,Metres,Feet,
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

        fun groupClause(groupId: String?): String {
            return groupId?.split(",")?.fold("") { currentValue, result ->
                currentValue + "title = '$result' OR "
            }?.trim { it <= ' ' }?.dropLast(3) ?: ""
        }

        return "WHERE " + addToWhere(moreFilters,
                addToWhere(country?.sql,
                        addToWhere(groupClause(groupId), climbed?.sql ?: "")
                )
        )
    }

    private fun getT100(moreFilters: String?, climbed: IsHillClimbed?): String {
        return "$hillQuery WHERE " + addToWhere(moreFilters, climbed?.let { addToWhere(climbed.sql, "T100='1'") }
                ?: "T100='1'")
    }

    private fun addToWhere(filter: String?, where: String): String {
        return (if ("" != where && !filter.isNullOrEmpty())
            "$where AND " else where) + (filter ?: "")
    }


}