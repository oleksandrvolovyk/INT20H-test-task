package the_null_pointer.preppal.data

import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun observeEvents(): Flow<List<Event>>
    fun observeEventById(eventId: Long): Flow<Event?>
    suspend fun getAllByType(type: String): List<Event>
    suspend fun getAllById(id: Long): Event
    suspend fun getAllByIds(eventIds: LongArray): List<Event>
    suspend fun getById(eventId: Long): Event?
    suspend fun getAllBySummary(summary: String): List<Event>
    suspend fun getAllBySummaryAndType(summary: String, type: Event.Type): List<Event>
    suspend fun setEventGrade(eventId: Long, newGrade: Double?)
    suspend fun setEventMaxGrade(eventId: Long, newMaxGrade: Double?)
    suspend fun setEventCompletion(eventId: Long, completed: Boolean?)
    suspend fun insert(vararg events: Event)
    suspend fun insertAll(events: List<Event>): Boolean // Returns true if inserted successfully
    suspend fun update(event: Event)
    suspend fun delete(event: Event)
    suspend fun delete(eventId: Long)
    suspend fun delete(eventIds: List<Long>)
}