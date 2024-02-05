package the_null_pointer.preppal.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface EventDAO {
    @Query("SELECT * FROM event")
    fun getAll(): List<Event>

    @Query("SELECT * FROM event WHERE id IN (:eventIds)")
    fun loadAllByIds(eventIds: LongArray): List<Event>

    @Query("SELECT * FROM event WHERE summary LIKE :summary")
    fun findAllBySummary(summary: String): List<Event>

    @Insert
    fun insertAll(vararg users: Event)

    @Delete
    fun delete(user: Event)
}
