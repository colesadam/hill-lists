package uk.colessoft.android.hilllist.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Bagging")
data class Bagging ( @PrimaryKey
                     val _id: Long,
                     val dateClimbed: Date,
                     val notes: String = ""){
    companion object {
        const val KEY_DATECLIMBED = "dateClimbed"
        const val KEY_NOTES = "notes"
        const val BAGGING_TABLE = "Bagging"
    }

}