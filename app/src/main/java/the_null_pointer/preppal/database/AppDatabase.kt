package the_null_pointer.preppal.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import java.util.concurrent.Executors

private const val DB_NAME = "preppal"

@Database(entities = [Event::class], exportSchema = false, version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDAO(): EventDAO

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                instance = databaseBuilder(
                    context,
                    AppDatabase::class.java, DB_NAME
                ).build()
            }
            return instance
        }
    }
}