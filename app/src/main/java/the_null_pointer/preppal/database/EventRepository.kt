package the_null_pointer.preppal.database

import androidx.annotation.WorkerThread

class EventRepository(private val eventDAO: EventDAO) {
    fun getAll() = eventDAO.getAll()
    fun loadAllByIds(eventIds: LongArray) = eventDAO.loadAllByIds(eventIds)
    fun findAllBySummary(summary: String) = eventDAO.findAllBySummary(summary)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertAll(vararg events: Event) = eventDAO.insertAll(*events)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(event: Event) = eventDAO.delete(event)
}