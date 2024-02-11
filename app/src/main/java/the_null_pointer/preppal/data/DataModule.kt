package the_null_pointer.preppal.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import the_null_pointer.preppal.data.educational_resource.EducationalResourceDAO
import the_null_pointer.preppal.data.educational_resource.EducationalResourceRepository
import the_null_pointer.preppal.data.educational_resource.EducationalResourceRepositoryImpl
import the_null_pointer.preppal.data.event.EventDAO
import the_null_pointer.preppal.data.event.EventRepository
import the_null_pointer.preppal.data.event.EventRepositoryImpl
import the_null_pointer.preppal.data.location_name.LocationNameDatasource
import the_null_pointer.preppal.data.location_name.LocationNameDatasourceImpl
import the_null_pointer.preppal.data.location_name.NominatimApi
import the_null_pointer.preppal.data.location_name.NominatimApiClient
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
    fun provideEducationalResourceDAO(appDatabase: AppDatabase): EducationalResourceDAO {
        return appDatabase.educationalResourceDAO()
    }

    @Provides
    @Singleton
    fun provideEventRepository(
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        eventDAO: EventDAO
    ): EventRepository {
        return EventRepositoryImpl(ioDispatcher, eventDAO)
    }

    @Provides
    @Singleton
    fun provideEducationalResourceRepository(
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        educationalResourceDAO: EducationalResourceDAO
    ): EducationalResourceRepository {
        return EducationalResourceRepositoryImpl(ioDispatcher, educationalResourceDAO)
    }

    @Provides
    @Singleton
    fun provideNominatimApi(): NominatimApi {
        return NominatimApiClient.client.create(NominatimApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLocationNameDatasource(
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        nominatimApi: NominatimApi
    ): LocationNameDatasource {
        return LocationNameDatasourceImpl(ioDispatcher, nominatimApi)
    }
}