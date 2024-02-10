package the_null_pointer.preppal.data.educational_resource

import kotlinx.coroutines.flow.Flow
import the_null_pointer.preppal.data.educational_resource.model.EducationalResource
import the_null_pointer.preppal.data.event.model.Event

interface EducationalResourceRepository {
    suspend fun insert(educationalResource: EducationalResource)

    fun observeAll(): Flow<List<EducationalResource>>
    fun observeByEventSummaryAndEventType(
        eventSummary: String,
        eventType: Event.Type
    ): Flow<List<EducationalResource>>

    suspend fun setFavourite(educationalResourceId: Long, favourite: Boolean)

    suspend fun delete(educationalResource: EducationalResource)
    suspend fun deleteById(educationalResourceId: Long)
}