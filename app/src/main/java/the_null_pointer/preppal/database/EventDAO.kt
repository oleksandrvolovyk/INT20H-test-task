package the_null_pointer.preppal.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDAO {
    @Query("SELECT * FROM event")
    fun getAll(): Flow<List<Event>>

    @Query("SELECT * FROM event WHERE id IN (:eventIds)")
    fun loadAllByIds(eventIds: LongArray): Flow<List<Event>>

    @Query("SELECT * FROM event WHERE summary LIKE :summary")
    fun findAllBySummary(summary: String): Flow<List<Event>>

    @Insert
    suspend fun insertAll(vararg events: Event)

    @Delete
    suspend fun delete(event: Event)
}
