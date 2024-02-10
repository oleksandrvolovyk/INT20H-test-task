package the_null_pointer.preppal.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import the_null_pointer.preppal.data.educational_resource.EducationalResourceDAO
import the_null_pointer.preppal.data.educational_resource.model.EducationalResource
import the_null_pointer.preppal.data.event.model.Event
import the_null_pointer.preppal.data.event.EventDAO
import the_null_pointer.preppal.data.event.TimestampListConverter

@Database(entities = [Event::class, EducationalResource::class], exportSchema = false, version = 1)
@TypeConverters(TimestampListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDAO(): EventDAO
    abstract fun educationalResourceDAO(): EducationalResourceDAO
}