package uk.colessoft.android.hilllist.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "typeslink",
        foreignKeys = arrayOf(ForeignKey(entity = Hill::class,
                parentColumns = arrayOf("_id"),
                childColumns = arrayOf("hill_id"),
                onDelete = ForeignKey.CASCADE),
                ForeignKey(entity = HillType::class,
                        parentColumns = arrayOf("_id"),
                        childColumns = arrayOf("type_Id"))
        ))
data class TypeLink(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val ownerId: Long

)