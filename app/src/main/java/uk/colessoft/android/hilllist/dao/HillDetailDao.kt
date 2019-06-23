package uk.colessoft.android.hilllist.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SupportSQLiteQuery
import uk.colessoft.android.hilllist.model.HillDetail


@Dao
interface HillDetailDao {

    @Transaction
    @Query("SELECT * FROM hills WHERE h_id = :hillId")
    fun getHillDetail(hillId: Long): LiveData<HillDetail>

    @RawQuery
    fun getHillsRaw(groupId: String?, orderBy: String? = "height desc"/*, countryClause: String?, moreFilters: String?, , filter: Int?*/): LiveData<List<HillDetail>>

    fun getHills(groupId: String?, orderBy: String? = "height desc"): LiveData<List<HillDetail>>{
        val query = new SupportSQLiteQuery("""SELECT h_id, latitude, longitude,Metres,Feet,
        name,notes,dateClimbed,b_id from hills
        JOIN typeslink ON typeslink.hill_id = h_id
        LEFT JOIN bagging ON b_id = h_id
        JOIN hilltypes ON typeslink.type_Id = ht_id
        WHERE hilltypes.title = :groupId
        ORDER BY
        CASE WHEN :orderBy="id asc" THEN h_id END ASC,
        CASE WHEN :orderBy="id desc" THEN h_id END DESC,
        CASE WHEN :orderBy="name asc" THEN LOWER(name) END ASC,
        CASE WHEN :orderBy="name desc" THEN LOWER(name) END DESC,
        CASE WHEN :orderBy="height asc" THEN Metres END ASC,
        CASE WHEN :orderBy="height desc" THEN Metres END DESC""",
        new Object[]{userId})
    }

}