package uk.colessoft.android.hilllist.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import uk.colessoft.android.hilllist.model.HillType

@Entity(tableName = "typeslink",
        foreignKeys = arrayOf(ForeignKey(entity = FullHill::class,
                parentColumns = arrayOf("_id"),
                childColumns = arrayOf("hill_id"),
                onDelete = ForeignKey.CASCADE),
                ForeignKey(entity = HillType::class,
                        parentColumns = arrayOf("_id"),
                        childColumns = arrayOf("type_Id"))
        ))
data class TypeLink(
        @PrimaryKey(autoGenerate = true)
        val _id: Long,
        val hill_id: Long,
        val type_Id: Long

)