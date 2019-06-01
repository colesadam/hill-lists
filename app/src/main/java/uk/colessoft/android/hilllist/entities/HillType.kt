package uk.colessoft.android.hilllist.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hilltypes")
data class HillType(
        @PrimaryKey
        val _id: Long,
        val title: String = ""
)