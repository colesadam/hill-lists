package uk.colessoft.android.hilllist.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hilltypes")
data class HillType(
        @PrimaryKey
        val _id: Long,
        val title: String = ""
)