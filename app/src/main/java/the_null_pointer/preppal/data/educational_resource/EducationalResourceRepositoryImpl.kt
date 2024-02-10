package the_null_pointer.preppal.data.educational_resource

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import the_null_pointer.preppal.data.educational_resource.model.EducationalResource
import the_null_pointer.preppal.data.event.model.Event

class EducationalResourceRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher,
    private val educationalResourceDAO: EducationalResourceDAO
) : EducationalResourceRepository {
    override suspend fun insert(educationalResource: EducationalResource) =
        withContext(ioDispatcher) {
            educationalResourceDAO.insert(educationalResource)
        }

    override fun observeAll(): Flow<List<EducationalResource>> =
        educationalResourceDAO.observeAll()

    override fun observeByEventSummaryAndEventType(
        eventSummary: String,
        eventType: Event.Type
    ): Flow<List<EducationalResource>> =
        educationalResourceDAO.observeByEventSummaryAndEventType(eventSummary, eventType)

    override suspend fun setFavourite(educationalResourceId: Long, favourite: Boolean) =
        withContext(ioDispatcher) {
            educationalResourceDAO.setFavourite(educationalResourceId, favourite)
        }

    override suspend fun delete(educationalResource: EducationalResource) =
        withContext(ioDispatcher) {
            educationalResourceDAO.delete(educationalResource)
        }

    override suspend fun deleteById(educationalResourceId: Long) = withContext(ioDispatcher) {
        educationalResourceDAO.deleteById(educationalResourceId)
    }
}