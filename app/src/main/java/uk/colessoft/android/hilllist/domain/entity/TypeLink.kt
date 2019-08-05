package uk.colessoft.android.hilllist.domain.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "typeslink",
        foreignKeys = arrayOf(ForeignKey(entity = Hill::class,
                parentColumns = arrayOf("h_id"),
                childColumns = arrayOf("hill_id"),
                onDelete = ForeignKey.CASCADE),
                ForeignKey(entity = HillType::class,
                        parentColumns = arrayOf("ht_id"),
                        childColumns = arrayOf("type_Id"))
        ))
data class TypeLink(
        @PrimaryKey(autoGenerate = true)
        val tl_id: Long,
        val hill_id: Long,
        val type_Id: Long

)