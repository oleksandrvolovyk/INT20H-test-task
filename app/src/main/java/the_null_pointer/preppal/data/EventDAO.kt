package the_null_pointer.preppal.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDAO {
    @Query("SELECT * FROM event")
    fun observeAll(): Flow<List<Event>>

    @Query("SELECT * FROM event WHERE id = :eventId")
    fun getById(eventId: Long): Event?

    @Query("SELECT * FROM event WHERE id IN (:eventIds)")
    fun getAllByIds(eventIds: LongArray): List<Event>

    @Query("SELECT * FROM event WHERE type = :type")
    fun getAllByType(type: String): List<Event>

    @Query("SELECT * FROM event WHERE summary LIKE :summary")
    fun getAllBySummary(summary: String): List<Event>

    @Query("SELECT * FROM event WHERE summary = :summary AND type = :type")
    fun getAllBySummaryAndType(summary: String, type: Event.Type): List<Event>

    @Insert
    fun insert(vararg events: Event)

    @Insert
    fun insertAll(events: List<Event>): List<Long>

    @Delete
    fun delete(event: Event)

    @Query("DELETE FROM event WHERE id = :eventId")
    fun delete(eventId: Long)

    @Query("DELETE FROM event WHERE id IN (:eventIds)")
    fun delete(eventIds: List<Long>)
}
