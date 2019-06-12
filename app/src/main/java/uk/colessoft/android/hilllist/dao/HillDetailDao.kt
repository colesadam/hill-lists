package uk.colessoft.android.hilllist.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import uk.colessoft.android.hilllist.model.HillDetail


@Dao
interface HillDetailDao {

    @Transaction
    @Query("SELECT * FROM hills WHERE _id = :hillId")
    fun getHillDetail(hillId: Long): LiveData<HillDetail>

    @Query("""SELECT hills._id, hills._id as hill__id, latitude as hill_latitude, longitude as hill_longitude,Metres as hill_Metres,Feet as hill_Feet,
        hills.name as hill__name,notes,dateClimbed from hills
        JOIN typeslink ON typeslink.hill_id = hill_id
        LEFT JOIN bagging ON bagging._id = hills._id
        JOIN hilltypes ON typeslink.type_Id = hilltypes._id
        WHERE hilltypes.title = :groupId
        ORDER BY :orderBy""")
    fun getHills(groupId: String?, orderBy: String? = "hills._id asc"/*, countryClause: String?, moreFilters: String?, , filter: Int?*/): LiveData<List<HillDetail>>

}