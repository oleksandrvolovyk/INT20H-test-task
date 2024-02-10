package the_null_pointer.preppal.data.event

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import the_null_pointer.preppal.data.event.model.TimestampMillis

class TimestampListConverter {
    @TypeConverter
    fun fromTimestampList(value : List<TimestampMillis>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toTimestampList(value: String): List<TimestampMillis> {
        return Json.decodeFromString<List<TimestampMillis>>(value)
    }
}