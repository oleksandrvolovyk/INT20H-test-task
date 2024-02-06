package the_null_pointer.preppal.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Event::class], exportSchema = false, version = 1)
@TypeConverters(TimestampListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDAO(): EventDAO
}