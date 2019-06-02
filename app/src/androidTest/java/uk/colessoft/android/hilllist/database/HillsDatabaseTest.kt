package uk.colessoft.android.hilllist.database

import android.content.ContentValues
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import androidx.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import uk.colessoft.android.hilllist.dao.HillDetailDao
import java.io.IOException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class HillsDatabaseTest {

    private lateinit var hillsDatabase: HillsDatabase
    private lateinit var hillDetailDao: HillDetailDao

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        hillsDatabase = Room.inMemoryDatabaseBuilder(
                context, HillsDatabase::class.java).allowMainThreadQueries().build()
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
        val values = ContentValues()
        values.put("_id", 1)
        values.put("name", "TestHill")

        db.insert("hills", OnConflictStrategy.REPLACE, values)

        assert("TestHill".equals(getValue(hillDetailDao.getHillDetail(1)).hill?.hillname))

    }

    @Test
    @SmallTest
    fun getHillDetailShouldReturnCorrectBagging() {
        val db = hillsDatabase.openHelper.writableDatabase
        val values = ContentValues()
        values.put("_id", 1)
        values.put("name", "TestHill")
        db.insert("hills", OnConflictStrategy.REPLACE, values)

        val climbedValues = ContentValues()
        climbedValues.put("dateClimbed", "2012-10-10")
        climbedValues.put("_id", 1)
        climbedValues.put("notes", "this is fine")
        db.insert("bagging", OnConflictStrategy.REPLACE, climbedValues)

        assert("this is fine".equals(getValue(hillDetailDao.getHillDetail(1)).bagging?.first()?.notes))

    }

    @Test
    @SmallTest
    fun getHillDetailShouldReturnCorrectTypeLinks() {
        val db = hillsDatabase.openHelper.writableDatabase

        val values = ContentValues()
        values.put("_id", 1)
        values.put("name", "TestHill")
        db.insert("hills", OnConflictStrategy.REPLACE, values)

        val typeValues1 = ContentValues()
        typeValues1.put("_id", 43)
        typeValues1.put("title", "Hill type 1")
        val typeValues2 = ContentValues()
        typeValues2.put("_id", 54)
        typeValues2.put("title", "Hill type 2")
        db.insert("hilltypes", OnConflictStrategy.REPLACE, typeValues1)
        db.insert("hilltypes", OnConflictStrategy.REPLACE, typeValues2)

        val typeLinkValues1 = ContentValues()
        typeLinkValues1.put("hill_id", 1)
        typeLinkValues1.put("type_id", 43)
        val typeLinkValues2 = ContentValues()
        typeLinkValues2.put("hill_id", 1)
        typeLinkValues2.put("type_id", 54)
        db.insert("typeslink", OnConflictStrategy.REPLACE, typeLinkValues1)
        db.insert("typeslink", OnConflictStrategy.REPLACE, typeLinkValues2)

        val types: List<Long>? = getValue(hillDetailDao.getHillDetail(1)).types
        assertEquals(2, types?.size)
        assertTrue(types?.contains(43) ?: false)
        assertTrue(types?.contains(54) ?: false)

    }

    @Throws(InterruptedException::class)
    fun <T> getValue(liveData: LiveData<T>): T {
        val data = arrayOfNulls<Any>(1)
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(t: T?) {
                data[0] = t
                latch.countDown()
                liveData.removeObserver(this)//To change body of created functions use File | Settings | File Templates.
            }

        }
        liveData.observeForever(observer)
        latch.await(2, TimeUnit.SECONDS)

        return data[0] as T
    }

}