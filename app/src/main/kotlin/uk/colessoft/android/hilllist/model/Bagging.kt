package uk.colessoft.android.hilllist.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "Bagging")
data class Bagging ( @PrimaryKey
                     val _id: Long,
                     val dateClimbed: Date,
                     val notes: String = "")