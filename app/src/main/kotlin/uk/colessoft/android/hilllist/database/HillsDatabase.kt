package uk.colessoft.android.hilllist.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import uk.colessoft.android.hilllist.dao.HillDao
import uk.colessoft.android.hilllist.dao.HillTypeDao
import uk.colessoft.android.hilllist.dao.TypeLinkDao
import uk.colessoft.android.hilllist.model.Hill
import uk.colessoft.android.hilllist.model.HillType
import uk.colessoft.android.hilllist.model.TypeLink
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.TypeConverters
import android.arch.persistence.room.migration.Migration
import uk.colessoft.android.hilllist.model.Bagging

@Database(entities = arrayOf(Hill::class, HillType::class, TypeLink::class, Bagging::class), version = 3)
@TypeConverters(DateConverters :: class)
abstract class HillsDatabase : RoomDatabase() {

    abstract fun hillDao(): HillDao
    abstract fun hillTypeDao(): HillTypeDao
    abstract fun typeLinkDao(): TypeLinkDao

    companion object {
        @JvmField
    val MIGRATION_2_3 = Migration2To3()
    }

}

class Migration2To3 : Migration(2,3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}