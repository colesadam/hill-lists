package uk.colessoft.android.hilllist.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import uk.colessoft.android.hilllist.model.HillDetail


@Dao
interface HillDetailDao {

    @Transaction
    @Query("SELECT * FROM hills WHERE _id = :hillId")
    fun getHillDetail(hillId: Long): HillDetail

}