package uk.colessoft.android.hilllist.database

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class DateConverters {

    val iso8601Format = SimpleDateFormat("yyyy-MM-dd")

    @TypeConverter
    fun dateFromString(value: String): Date? {
        return iso8601Format.parse(value)
    }

    @TypeConverter
    fun stringFromDate(value: Date): String? {
        return iso8601Format.format(value)
    }
}