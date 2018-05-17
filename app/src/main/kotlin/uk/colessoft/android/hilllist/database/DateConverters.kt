package uk.colessoft.android.hilllist.database

import android.arch.persistence.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class DateConverters {

    val iso8601Format = SimpleDateFormat("yyyy-MM-dd")

    @TypeConverter
    fun dateFromString(value: String): Date? {
        return if (value == null) null else iso8601Format.parse(value)
    }

    @TypeConverter
    fun stringFromDate(value: Date): String? {
        return if (value == null) null else iso8601Format.format(value)
    }
}