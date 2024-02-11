package the_null_pointer.preppal.data.location_name

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object NominatimApiClient {
    val client: Retrofit
        get() {
            val converterFactory = JacksonConverterFactory.create(
                ObjectMapper().configure(
                    DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                    false
                ).registerKotlinModule()
            )
            return Retrofit.Builder()
                .baseUrl("https://nominatim.openstreetmap.org/")
                .addConverterFactory(converterFactory)
                .build()
        }
}