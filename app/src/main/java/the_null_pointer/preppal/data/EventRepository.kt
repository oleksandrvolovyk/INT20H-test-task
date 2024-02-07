package the_null_pointer.preppal.data

import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun observeEvents(): Flow<List<Event>>
    suspend fun getAllByType(type: String): List<Event>
    suspend fun getAllByIds(eventIds: LongArray): List<Event>
    suspend fun getAllBySummary(summary: String): List<Event>
    suspend fun getAllBySummaryAndType(summary: String, type: Event.Type): List<Event>
    suspend fun insert(vararg events: Event)
    suspend fun insertAll(events: List<Event>)
    suspend fun delete(event: Event)
}