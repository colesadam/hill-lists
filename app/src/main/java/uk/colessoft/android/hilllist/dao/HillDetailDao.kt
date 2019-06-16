package uk.colessoft.android.hilllist.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import uk.colessoft.android.hilllist.model.HillDetail


@Dao
interface HillDetailDao {

    @Transaction
    @Query("SELECT * FROM hills WHERE h_id = :hillId")
    fun getHillDetail(hillId: Long): LiveData<HillDetail>

    @Query("""SELECT h_id, latitude, longitude,Metres,Feet,
        name,notes,dateClimbed,b_id from hills
        JOIN typeslink ON typeslink.hill_id = h_id
        LEFT JOIN bagging ON b_id = h_id
        JOIN hilltypes ON typeslink.type_Id = ht_id
        WHERE hilltypes.title = :groupId
        ORDER BY :orderBy""")
    fun getHills(groupId: String?, orderBy: String? = "h_id asc"/*, countryClause: String?, moreFilters: String?, , filter: Int?*/): LiveData<List<HillDetail>>

}