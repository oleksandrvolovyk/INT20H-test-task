package the_null_pointer.preppal.data.educational_resource.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import the_null_pointer.preppal.R
import the_null_pointer.preppal.data.event.model.Event

@Entity(tableName = "educational_resources")
data class EducationalResource(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val eventSummary: String,
    val eventType: Event.Type,

    val name: String,
    val type: Type,
    val link: String,
    val favourite: Boolean
) {
    enum class Type {
        Literature, OnlineCourse, Resource;

        companion object {
            val Type.stringResourceId: Int
                get() = when (this) {
                    Literature -> R.string.literature
                    OnlineCourse -> R.string.online_course
                    Resource -> R.string.resource
                }
        }
    }
}