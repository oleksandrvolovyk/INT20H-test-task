package the_null_pointer.preppal.data.event

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import the_null_pointer.preppal.data.event.model.Event

@Dao
interface EventDAO {
    @Query("SELECT * FROM event")
    fun observeAll(): Flow<List<Event>>

    @Query("SELECT * FROM event WHERE id = :eventId")
    fun observeEventById(eventId: Long): Flow<Event?>

    @Query("SELECT * FROM event WHERE id = :eventId")
    fun getById(eventId: Long): Event?

    @Query("SELECT * FROM event WHERE id IN (:eventIds)")
    fun getAllByIds(eventIds: LongArray): List<Event>

    @Query("SELECT * FROM event WHERE type = :type")
    fun getAllByType(type: String): List<Event>

    @Query("SELECT * FROM event WHERE id = :id")
    fun getAllById(id: Long): Event

    @Query("SELECT * FROM event WHERE summary LIKE :summary")
    fun getAllBySummary(summary: String): List<Event>

    @Query("SELECT * FROM event WHERE summary = :summary AND type = :type")
    fun getAllBySummaryAndType(summary: String, type: Event.Type): List<Event>

    @Query("UPDATE event SET grade = :newGrade WHERE id = :eventId")
    fun setEventGrade(eventId: Long, newGrade: Double?)

    @Query("UPDATE event SET max_score = :newMaxGrade WHERE id = :eventId")
    fun setEventMaxGrade(eventId: Long, newMaxGrade: Double?)

    @Query("UPDATE event SET completed = :completed WHERE id = :eventId")
    fun setEventCompletion(eventId: Long, completed: Boolean?)

    @Insert
    fun insert(vararg events: Event)

    @Insert
    fun insertAll(events: List<Event>): List<Long>

    @Update
    fun update(event: Event)

    @Delete
    fun delete(event: Event)

    @Query("DELETE FROM event WHERE id = :eventId")
    fun delete(eventId: Long)

    @Query("DELETE FROM event WHERE id IN (:eventIds)")
    fun delete(eventIds: List<Long>)
}
