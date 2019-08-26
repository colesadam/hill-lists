package uk.colessoft.android.hilllist.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SimpleSQLiteQuery
import uk.colessoft.android.hilllist.domain.HillDetail


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


    fun getHills(groupId: String?, country: CountryClause?, moreFilters: String?): LiveData<List<HillDetail>> {
        return getHillsRaw(
                SimpleSQLiteQuery("$hillQuery " + whereClause(groupId, country, moreFilters)
                        + " order by " + HillsOrder.HEIGHT_DESC.sql, arrayOf()))
    }

    private fun whereClause(groupId: String?, country: CountryClause?, moreFilters: String?): String {
        if ("T100" == groupId) return "WHERE " + getT100(moreFilters)

        fun groupClause(groupId: String?): String {
            return groupId?.split(",")?.fold("") { currentValue, result ->
                currentValue + "title = '$result' OR "
            }?.trim { it <= ' ' }?.dropLast(3) ?: ""
        }

        if (groupId != null || country != null || moreFilters != null) {
            return "WHERE " + addToWhere(moreFilters,
                    addToWhere(country?.sql,
                            addToWhere(groupClause(groupId), "")
                    )
            ).removeSuffix(" AND ")
        } else return ""

    }

    private fun getT100(moreFilters: String?): String {
        return addToWhere(moreFilters, "T100='1'")
    }

    private fun addToWhere(filter: String?, where: String): String {
        return (if ("" != where && !filter.isNullOrEmpty())
            "$where AND " else where) + (filter ?: "")
    }


}