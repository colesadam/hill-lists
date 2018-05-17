package uk.colessoft.android.hilllist.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "hilltypes")
data class HillType(
        @PrimaryKey
        val _id: Long,
        val title: String = ""
)