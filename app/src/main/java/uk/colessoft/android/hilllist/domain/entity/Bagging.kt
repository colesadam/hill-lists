package uk.colessoft.android.hilllist.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Bagging")
data class Bagging (@PrimaryKey
                     val b_id: Long,
                    val dateClimbed: Date,
                    val notes: String = ""){
    companion object {
        const val KEY_DATECLIMBED = "dateClimbed"
        const val KEY_NOTES = "notes"
        const val BAGGING_TABLE = "Bagging"
    }

}