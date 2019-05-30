package uk.colessoft.android.hilllist.database

import android.content.ContentValues
import android.content.Context
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import uk.colessoft.android.hilllist.dao.HillDetailDao
import java.io.IOException


class HillsDatabaseTest {

    private lateinit var hillsDatabase: HillsDatabase

    private lateinit var hillDetailDao: HillDetailDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        hillsDatabase = Room.inMemoryDatabaseBuilder(
                context, HillsDatabase::class.java).build()
        hillDetailDao = hillsDatabase.hillDetailDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        hillsDatabase.close()
    }

    @Test
    @SmallTest
    fun getHillDetailShouldReturnCorrectHill() {
        val db = hillsDatabase.openHelper.writableDatabase
        var values = ContentValues()
        values.put("_id", 1)
        values.put("name","TestHill")

        db.insert("hills",OnConflictStrategy.REPLACE, values)

        assert("TestHill".equals(hillDetailDao.getHillDetail(1).hill.hillname))

    }

    @Test
    @SmallTest
    fun getHillDetailShouldReturnCorrectBagging() {
        val db = hillsDatabase.openHelper.writableDatabase
        var values = ContentValues()
        values.put("_id", 1)
        values.put("name","TestHill")

        db.insert("hills",OnConflictStrategy.REPLACE, values)
        var climbedValues = ContentValues()
        climbedValues.put("dateClimbed", "2012-10-10")
        climbedValues.put("_id", "1")
        climbedValues.put("notes", "this is fine")
        db.insert("bagging",OnConflictStrategy.REPLACE,climbedValues)

        assert("this is fine".equals(hillDetailDao.getHillDetail(1).bagging?.first()?.notes))

    }

}