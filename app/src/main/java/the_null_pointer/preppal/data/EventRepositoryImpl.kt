package the_null_pointer.preppal.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class EventRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher,
    private val eventDAO: EventDAO
) : EventRepository {
    override fun observeEvents(): Flow<List<Event>> = eventDAO.observeAll()

    override suspend fun getAllByIds(eventIds: LongArray): List<Event> = withContext(ioDispatcher) {
        eventDAO.getAllByIds(eventIds)
    }

    override suspend fun getAllBySummary(summary: String): List<Event> = withContext(ioDispatcher) {
        eventDAO.getAllBySummary(summary)
    }

    override suspend fun insert(vararg events: Event) = withContext(ioDispatcher) {
        eventDAO.insertAll(*events)
    }

    override suspend fun delete(event: Event) {
        eventDAO.delete(event)
    }
}