package the_null_pointer.preppal.data

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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