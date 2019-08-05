package uk.colessoft.android.hilllist.domain


import androidx.room.Embedded
import androidx.room.Relation
import uk.colessoft.android.hilllist.domain.entity.Bagging
import uk.colessoft.android.hilllist.domain.entity.Hill
import uk.colessoft.android.hilllist.domain.entity.TypeLink

data class HillDetail
(
        @Embedded
        val hill: Hill
){

        @Relation(parentColumn = "h_id",
                entityColumn = "b_id",
                entity = Bagging::class)
        var bagging: List<Bagging>? = null

        @Relation(parentColumn = "h_id",
                entityColumn = "hill_id",
                entity = TypeLink::class,
                projection = arrayOf("type_Id"))
        var types: List<Long>? = null
}


