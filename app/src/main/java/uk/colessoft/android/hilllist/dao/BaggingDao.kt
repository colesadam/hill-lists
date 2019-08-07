package uk.colessoft.android.hilllist.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uk.colessoft.android.hilllist.domain.entity.Bagging

@Dao
interface BaggingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBagging(vararg baggings: Bagging)

    @Query("DELETE FROM bagging WHERE b_id = :id")
    fun deleteByHillId(id: Long)



}