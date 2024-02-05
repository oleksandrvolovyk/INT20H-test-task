package the_null_pointer.preppal.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import java.util.concurrent.Executors

@Database(entities = [Event::class], exportSchema = false, version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDAO(): EventDAO?

    companion object {
        private const val DB_NAME = "preppal"

        @Volatile
        private var instance: AppDatabase? = null
        private const val NUMBER_OF_THREADS = 4
        val databaseWriteExecutor = Executors.newFixedThreadPool(
            NUMBER_OF_THREADS
        )

        @Synchronized
        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                instance = databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, DB_NAME
                ).build()
            }
            return instance
        }
    }
}