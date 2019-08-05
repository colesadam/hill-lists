package uk.colessoft.android.hilllist.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import uk.colessoft.android.hilllist.dao.*
import uk.colessoft.android.hilllist.domain.entity.Bagging
import uk.colessoft.android.hilllist.domain.entity.Hill
import uk.colessoft.android.hilllist.domain.entity.HillType
import uk.colessoft.android.hilllist.domain.entity.TypeLink

@Database(entities = arrayOf(Hill::class, HillType::class, TypeLink::class, Bagging::class), version = 3)
@TypeConverters(DateConverters :: class)
abstract class HillsDatabase : RoomDatabase() {

    abstract fun hillDao(): HillDao
    abstract fun hillTypeDao(): HillTypeDao
    abstract fun typeLinkDao(): TypeLinkDao
    abstract fun hillDetailDao(): HillDetailDao
    abstract fun baggingDao(): BaggingDao

    companion object {
        @JvmField
    val MIGRATION_2_3 = Migration2To3()

    }

}

class Migration2To3 : Migration(2,3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS `temp_hills` (`tl_id` INTEGER NOT NULL, `Name` TEXT, `Parent (SMC)` TEXT, `Parent name (SMC)` TEXT, `Section` TEXT, `Section name` TEXT, `Area` TEXT, `Island` TEXT, `Topo Section` TEXT, `County` TEXT, `Classification` TEXT, `Map 1:50k` TEXT, `Map 1:25k` TEXT, `Metres` REAL NOT NULL, `Feet` REAL NOT NULL, `Grid ref` TEXT, `Grid ref 10` TEXT, `Drop` REAL NOT NULL, `Col grid ref` TEXT, `Col height` REAL NOT NULL, `Feature` TEXT, `Observations` TEXT, `Survey` TEXT, `Climbed` TEXT, `Country` TEXT, `County Top` TEXT, `Revision` TEXT NOT NULL, `Comments` TEXT, `Streetmap/OSiViewer` TEXT, `Geograph/MountainViews` TEXT, `HillDetail-bagging` TEXT, `Xcoord` INTEGER NOT NULL, `Ycoord` INTEGER NOT NULL, `Latitude` REAL NOT NULL, `Longitude` REAL NOT NULL, `GridrefXY` TEXT, `_Section` TEXT, `Parent (Ma)` TEXT, `Parent name (Ma)` TEXT, `MVNumber` TEXT, `Ma` TEXT, `Ma=` TEXT, `Hu` TEXT, `Hu=` TEXT, `Tu` TEXT, `Tu=` TEXT, `Sim` TEXT, `M` TEXT, `MT` TEXT, `F` TEXT, `C` TEXT, `G` TEXT, `D` TEXT, `DT` TEXT, `Mur` TEXT, `CT` TEXT, `GT` TEXT, `Hew` TEXT, `N` TEXT, `5` TEXT, `5D` TEXT, `5H` TEXT, `4` TEXT, `3` TEXT, `2` TEXT, `1` TEXT, `1=` TEXT, `0` TEXT, `W` TEXT, `WO` TEXT, `B` TEXT, `CoH` TEXT, `CoH=` TEXT, `CoU` TEXT, `CoU=` TEXT, `CoA` TEXT, `CoA=` TEXT, `CoL` TEXT, `CoL=` TEXT, `SIB` TEXT, `sMa` TEXT, `sHu` TEXT, `sSim` TEXT, `s5` TEXT, `s5D` TEXT, `s5H` TEXT, `s5M` TEXT, `s4` TEXT, `Sy` TEXT, `Fel` TEXT, `BL` TEXT, `Bg` TEXT, `T100` TEXT, `xMT` TEXT, `xC` TEXT, `xG` TEXT, `xN` TEXT, `xDT` TEXT, `Dil` TEXT, `VL` TEXT, `A` TEXT, `5M` TEXT, `Ca` TEXT, `Bin` TEXT, `O` TEXT, `Un` TEXT, PRIMARY KEY(`tl_id`))")
        database.execSQL("CREATE TABLE IF NOT EXISTS `temp_bagging` (`tl_id` INTEGER NOT NULL, `dateClimbed` TEXT NOT NULL, `notes` TEXT NOT NULL, PRIMARY KEY(`tl_id`))")
        database.execSQL("INSERT INTO temp_hills SELECT * FROM hills")
        database.execSQL("INSERT INTO temp_bagging SELECT * from bagging")
        database.execSQL("DROP TABLE hills")
        database.execSQL("DROP TABLE hilltypes")
        database.execSQL("DROP TABLE typeslink")
        database.execSQL("DROP TABLE bagging")
        database.execSQL("CREATE TABLE IF NOT EXISTS `hills` (`tl_id` INTEGER NOT NULL, `Name` TEXT, `Parent (SMC)` TEXT, `Parent name (SMC)` TEXT, `Section` TEXT, `Section name` TEXT, `Area` TEXT, `Island` TEXT, `Topo Section` TEXT, `County` TEXT, `Classification` TEXT, `Map 1:50k` TEXT, `Map 1:25k` TEXT, `Metres` REAL NOT NULL, `Feet` REAL NOT NULL, `Grid ref` TEXT, `Grid ref 10` TEXT, `Drop` REAL NOT NULL, `Col grid ref` TEXT, `Col height` REAL NOT NULL, `Feature` TEXT, `Observations` TEXT, `Survey` TEXT, `Climbed` TEXT, `Country` TEXT, `County Top` TEXT, `Revision` TEXT NOT NULL, `Comments` TEXT, `Streetmap/OSiViewer` TEXT, `Geograph/MountainViews` TEXT, `HillDetail-bagging` TEXT, `Xcoord` INTEGER NOT NULL, `Ycoord` INTEGER NOT NULL, `Latitude` REAL NOT NULL, `Longitude` REAL NOT NULL, `GridrefXY` TEXT, `_Section` TEXT, `Parent (Ma)` TEXT, `Parent name (Ma)` TEXT, `MVNumber` TEXT, `Ma` TEXT, `Ma=` TEXT, `Hu` TEXT, `Hu=` TEXT, `Tu` TEXT, `Tu=` TEXT, `Sim` TEXT, `M` TEXT, `MT` TEXT, `F` TEXT, `C` TEXT, `G` TEXT, `D` TEXT, `DT` TEXT, `Mur` TEXT, `CT` TEXT, `GT` TEXT, `Hew` TEXT, `N` TEXT, `5` TEXT, `5D` TEXT, `5H` TEXT, `4` TEXT, `3` TEXT, `2` TEXT, `1` TEXT, `1=` TEXT, `0` TEXT, `W` TEXT, `WO` TEXT, `B` TEXT, `CoH` TEXT, `CoH=` TEXT, `CoU` TEXT, `CoU=` TEXT, `CoA` TEXT, `CoA=` TEXT, `CoL` TEXT, `CoL=` TEXT, `SIB` TEXT, `sMa` TEXT, `sHu` TEXT, `sSim` TEXT, `s5` TEXT, `s5D` TEXT, `s5H` TEXT, `s5M` TEXT, `s4` TEXT, `Sy` TEXT, `Fel` TEXT, `BL` TEXT, `Bg` TEXT, `T100` TEXT, `xMT` TEXT, `xC` TEXT, `xG` TEXT, `xN` TEXT, `xDT` TEXT, `Dil` TEXT, `VL` TEXT, `A` TEXT, `5M` TEXT, `Ca` TEXT, `Bin` TEXT, `O` TEXT, `Un` TEXT, PRIMARY KEY(`tl_id`))")
        database.execSQL("CREATE TABLE IF NOT EXISTS `hilltypes` (`tl_id` INTEGER NOT NULL, `title` TEXT NOT NULL, PRIMARY KEY(`tl_id`))")
        database.execSQL("CREATE TABLE IF NOT EXISTS `typeslink` (`tl_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `hill_id` INTEGER NOT NULL, `type_Id` INTEGER NOT NULL, FOREIGN KEY(`hill_id`) REFERENCES `hills`(`tl_id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`type_Id`) REFERENCES `hilltypes`(`tl_id`) ON UPDATE NO ACTION ON DELETE NO ACTION )")
        database.execSQL("CREATE TABLE IF NOT EXISTS `bagging` (`tl_id` INTEGER NOT NULL, `dateClimbed` TEXT NOT NULL, `notes` TEXT NOT NULL, PRIMARY KEY(`tl_id`))")


        database.execSQL("INSERT INTO hills SELECT * FROM temp_hills")
        database.execSQL("INSERT INTO bagging SELECT * from temp_bagging")


        database.execSQL("DROP TABLE temp_hills")
        database.execSQL("DROP TABLE temp_bagging")

        val startTime = System.currentTimeMillis()

        HillsTables.populateHillTypes(database,startTime)


    }

}