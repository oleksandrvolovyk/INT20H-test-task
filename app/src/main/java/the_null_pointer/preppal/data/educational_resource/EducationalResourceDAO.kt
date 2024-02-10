package the_null_pointer.preppal.data.educational_resource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import the_null_pointer.preppal.data.educational_resource.model.EducationalResource
import the_null_pointer.preppal.data.event.model.Event

@Dao
interface EducationalResourceDAO {
    @Query("SELECT * FROM educational_resources")
    fun observeAll(): Flow<List<EducationalResource>>

    @Insert
    fun insert(educationalResource: EducationalResource)

    @Query("SELECT * FROM educational_resources WHERE eventSummary = :eventSummary AND eventType = :eventType")
    fun observeByEventSummaryAndEventType(
        eventSummary: String,
        eventType: Event.Type
    ): Flow<List<EducationalResource>>

    @Query("UPDATE educational_resources SET favourite = :favourite WHERE id = :educationalResourceId")
    fun setFavourite(educationalResourceId: Long, favourite: Boolean)

    @Delete
    fun delete(educationalResource: EducationalResource)

    @Query("DELETE FROM educational_resources WHERE id = :educationalResourceId")
    fun deleteById(educationalResourceId: Long)
}
