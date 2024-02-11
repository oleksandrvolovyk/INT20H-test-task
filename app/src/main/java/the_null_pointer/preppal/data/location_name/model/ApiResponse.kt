package the_null_pointer.preppal.data.location_name.model

import com.fasterxml.jackson.annotation.JsonProperty

class ApiResponse(
    @JsonProperty("display_name")
    val displayName: String

    // Add other response field, if needed
)