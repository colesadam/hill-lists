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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import uk.colessoft.android.hilllist.dao.CountryClause
import uk.colessoft.android.hilllist.dao.HillDetailDao
import uk.colessoft.android.hilllist.dao.HillsOrder
import uk.colessoft.android.hilllist.dao.IsHillClimbed
import java.io.IOException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class HillDetailDaoTest {

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
        insertHill(db, 1, "TestHill")

        assert("TestHill".equals(getValue(hillDetailDao.getHillDetail(1)).hill?.hillname))
    }

    @Test
    @SmallTest
    fun getHillDetailShouldReturnCorrectBagging() {
        val db = hillsDatabase.openHelper.writableDatabase
        insertHill(db, 1, "TestHill")
        insertBagging(db, "2012-10-10", 1, "this is fine")

        assert("this is fine".equals(getValue(hillDetailDao.getHillDetail(1)).bagging?.first()?.notes))

    }

    @Test
    @SmallTest
    fun getHillDetailShouldReturnCorrectTypeLinks() {
        val db = hillsDatabase.openHelper.writableDatabase

        insertHill(db, 1, "TestHill")
        insertTypeValues(db, 43, "Hill type 1")
        insertTypeValues(db, 54, "Hill type 2")
        insertTypeLinks(db, 1, 43)
        insertTypeLinks(db, 1, 54)

        val types: List<Long>? = getValue(hillDetailDao.getHillDetail(1)).types
        assertEquals(2, types?.size)
        assertTrue(types?.contains(43) ?: false)
        assertTrue(types?.contains(54) ?: false)

    }

    @Test
    @SmallTest
    fun getHillsWithGroupIdShouldReturnCorrectly() {
        val db = hillsDatabase.openHelper.writableDatabase
        insertHill(db, 1, "TestHill")
        insertHill(db, 2, "another hill")
        insertBagging(db, "2012-10-10", 1, "this is fine")
        insertTypeValues(db, 43, "Hill type 1")
        insertTypeValues(db, 54, "Hill type 2")
        insertTypeLinks(db, 1, 43)
        insertTypeLinks(db, 1, 54)
        insertTypeLinks(db, 2, 54)
        val groupId = "Hill type 1"

        val hills = hillDetailDao.getHills(groupId, null, null)

        assertEquals(1, getValue(hills)?.size)
    }

    @Test
    @SmallTest
    fun getHillsWithGroupIdShouldReturnCorrectlyWithMultipleGroups() {
        val db = hillsDatabase.openHelper.writableDatabase
        insertHill(db, 1, "TestHill")
        insertHill(db, 2, "another hill")
        insertBagging(db, "2012-10-10", 1, "this is fine")
        insertTypeValues(db, 43, "Hill type 1")
        insertTypeValues(db, 54, "Hill type 2")
        insertTypeLinks(db, 1, 43)
        insertTypeLinks(db, 1, 54)
        insertTypeLinks(db, 2, 54)
        val groupId = "Hill type 1,Hill type 2"

        val hills = hillDetailDao.getHills(groupId, null, null)

        val h = getValue(hills)
        assertEquals(2, h?.size)
        assertEquals(2, h?.first().types?.size)
    }

    @Test
    @SmallTest
    fun getHillsWithGroupIdShouldReturnCorrectlyWithCountryFilter() {
        val db = hillsDatabase.openHelper.writableDatabase
        insertHill(db, 1, "TestHill", "S")
        insertHill(db, 2, "another hill", "E")
        insertBagging(db, "2012-10-10", 1, "this is fine")
        insertTypeValues(db, 43, "Hill type 1")
        insertTypeValues(db, 54, "Hill type 2")
        insertTypeLinks(db, 1, 43)
        insertTypeLinks(db, 1, 54)
        insertTypeLinks(db, 2, 54)
        val groupId = "Hill type 2"

        val hills = getValue(hillDetailDao.getHills(groupId, CountryClause.SCOTLAND, null))

        assertEquals(1, hills?.size)
    }

    @Test
    @SmallTest
    fun getHillsWithGroupIdShouldReturnCorrectlyWithNoIreland() {
        val db = hillsDatabase.openHelper.writableDatabase
        insertHill(db, 1, "TestHill", "M")
        insertHill(db, 2, "another hill", "I")
        insertBagging(db, "2012-10-10", 1, "this is fine")
        insertTypeValues(db, 43, "Hill type 1")
        insertTypeValues(db, 54, "Hill type 2")
        insertTypeLinks(db, 1, 43)
        insertTypeLinks(db, 1, 54)
        insertTypeLinks(db, 2, 54)
        val groupId = "Hill type 2"

        val hills = getValue(hillDetailDao.getHills(groupId, CountryClause.UK, null))

        assertEquals(1, hills?.size)
    }

    @Test
    @SmallTest
    fun getHillsWithGroupIdShouldReturnCorrectlyWithSearchFilter() {
        val db = hillsDatabase.openHelper.writableDatabase
        insertHill(db, 1, "TestHill")
        insertHill(db, 2, "another hill")
        insertBagging(db, "2012-10-10", 1, "this is fine")
        insertTypeValues(db, 43, "Hill type 1")
        insertTypeValues(db, 54, "Hill type 2")
        insertTypeLinks(db, 1, 43)
        insertTypeLinks(db, 1, 54)
        insertTypeLinks(db, 2, 54)

        val hills = hillDetailDao.getHills(null, null, "name LIKE '%another%'")

        assertEquals(1, getValue(hills)?.size)
    }

    private fun insertTypeLinks(db: SupportSQLiteDatabase, hillId: Int, typeId: Int) {
        val typeLinkValues1 = ContentValues()
        typeLinkValues1.put("hill_id", hillId)
        typeLinkValues1.put("type_id", typeId)
        db.insert("typeslink", OnConflictStrategy.REPLACE, typeLinkValues1)
    }

    private fun insertTypeValues(db: SupportSQLiteDatabase, typeId: Int, typeName: String) {
        val typeValues1 = ContentValues()
        typeValues1.put("ht_id", typeId)
        typeValues1.put("title", typeName)
        db.insert("hilltypes", OnConflictStrategy.REPLACE, typeValues1)
    }

    private fun insertBagging(db: SupportSQLiteDatabase, dateClimbed: String, hillId: Int, notes: String) {
        val climbedValues = ContentValues()
        climbedValues.put("dateClimbed", dateClimbed)
        climbedValues.put("b_id", hillId)
        climbedValues.put("notes", notes)
        db.insert("bagging", OnConflictStrategy.REPLACE, climbedValues)
    }

    private fun insertHill(db: SupportSQLiteDatabase, id: Int, name: String, country: String = "S") {
        val values = ContentValues()
        values.put("h_id", id)
        values.put("name", name)
        values.put("country", country)
        db.insert("hills", OnConflictStrategy.REPLACE, values)
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