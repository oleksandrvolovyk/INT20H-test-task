package the_null_pointer.preppal.data.location_name

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import the_null_pointer.preppal.data.location_name.model.ApiResponse

interface NominatimApi {
    @GET("reverse")
    fun reverseLookup(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("format") format: String = "json",
        @Header("User-Agent") userAgent: String = "PrepPal: Time management App for students v0.1"
    ): Call<ApiResponse>
}