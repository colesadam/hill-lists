package uk.colessoft.android.hilllist.model


import androidx.room.Embedded
import androidx.room.Relation
import uk.colessoft.android.hilllist.entity.Bagging
import uk.colessoft.android.hilllist.entity.Hill
import uk.colessoft.android.hilllist.entity.TypeLink

data class HillDetail
(
        @Embedded
        val hill: Hill
){

        @Relation(parentColumn = "_id",
                entityColumn = "_id",
                entity = Bagging::class)
        var bagging: List<Bagging>? = null

        @Relation(parentColumn = "_id",
                entityColumn = "hill_id",
                entity = TypeLink::class,
                projection = arrayOf("type_Id"))
        var types: List<Long>? = null
}


