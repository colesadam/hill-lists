package uk.colessoft.android.hilllist.model


import androidx.room.Embedded
import androidx.room.Relation
import uk.colessoft.android.hilllist.entities.Bagging
import uk.colessoft.android.hilllist.entities.FullHill
import uk.colessoft.android.hilllist.entities.TypeLink
import java.util.Date

data class Hill
(
        @Embedded
        val fullHill: FullHill,

        @Relation(parentColumn = "_id",
                entityColumn = "_id",
                entity = Bagging::class)
        val bagging: List<Bagging>,

        @Relation(parentColumn = "_id",
                entityColumn = "hill_id",
                entity = TypeLink::class,
                projection = arrayOf("type_Id"))
        val types: List<Long>
)


