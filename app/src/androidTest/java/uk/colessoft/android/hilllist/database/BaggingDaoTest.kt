package uk.colessoft.android.hilllist.database

import android.content.ContentValues
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import uk.colessoft.android.hilllist.dao.*
import uk.colessoft.android.hilllist.domain.entity.Bagging
import java.io.IOException
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class BaggingDaoTest {

    private lateinit var hillsDatabase: HillsDatabase
    private lateinit var baggingDao: BaggingDao

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        hillsDatabase = Room.inMemoryDatabaseBuilder(
                context, HillsDatabase::class.java).allowMainThreadQueries().build()
        baggingDao = hillsDatabase.baggingDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        hillsDatabase.close()
    }

    @Test
    @SmallTest
    fun insertBaggingShouldBeCorrect() {
        val db: SupportSQLiteDatabase = hillsDatabase.openHelper.writableDatabase
        val bagging = Bagging(99, GregorianCalendar(2014, 2, 11).time, "this is definitely a hill")
        baggingDao.insertBagging(bagging)

        val cursor = db.query("select * from bagging")
        if (cursor.moveToFirst()) {
            do {
                assert(1.equals(cursor.getLong(cursor.getColumnIndexOrThrow("b_id"))))
            } while (cursor.moveToNext());

        }
    }

    @Test
    @SmallTest
    fun deleteFromBaggingShouldDelete(){
        val db: SupportSQLiteDatabase = hillsDatabase.openHelper.writableDatabase
        val bagging = Bagging(99L, GregorianCalendar(2014, 2, 11).time, "this is definitely a hill")
        insertBagging(db,"2014-02-11",bagging.b_id.toInt(), bagging.notes)
        baggingDao.deleteByHillId(99L)
        val cursor = db.query("select * from bagging")
        assertFalse(cursor.moveToFirst())
    }

    private fun insertBagging(db: SupportSQLiteDatabase, dateClimbed: String, hillId: Int, notes: String) {
        val climbedValues = ContentValues()
        climbedValues.put("dateClimbed", dateClimbed)
        climbedValues.put("b_id", hillId)
        climbedValues.put("notes", notes)
        db.insert("bagging", OnConflictStrategy.REPLACE, climbedValues)
    }

}