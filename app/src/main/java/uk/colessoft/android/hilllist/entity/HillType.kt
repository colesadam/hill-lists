package uk.colessoft.android.hilllist.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hilltypes")
data class HillType(
        @PrimaryKey
        val ht_id: Long,
        val title: String = ""
)