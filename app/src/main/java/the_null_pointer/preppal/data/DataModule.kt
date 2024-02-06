package the_null_pointer.preppal.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(appContext, AppDatabase::class.java, "prep-pal-database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideEventDAO(appDatabase: AppDatabase): EventDAO {
        return appDatabase.eventDAO()
    }

    @Provides
    @Singleton
    fun provideEventRepository(
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        eventDAO: EventDAO
    ): EventRepository {
        return EventRepositoryImpl(ioDispatcher, eventDAO)
    }
}