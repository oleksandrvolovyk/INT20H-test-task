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

    @Query("SELECT * FROM event WHERE id IN (:eventIds)")
    fun getAllByIds(eventIds: LongArray): List<Event>

    @Query("SELECT * FROM event WHERE type = :type")
    fun getAllByType(type: String): List<Event>

    @Query("SELECT * FROM event WHERE summary LIKE :summary")
    fun getAllBySummary(summary: String): List<Event>

    @Insert
    suspend fun insertAll(vararg events: Event)

    @Delete
    suspend fun delete(event: Event)
}
