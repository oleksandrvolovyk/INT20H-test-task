package the_null_pointer.preppal.data.location_name

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface LocationNameDatasource {
    suspend fun getDisplayNameForLocation(latitude: Double, longitude: Double): String?
}

class LocationNameDatasourceImpl(
    private val ioDispatcher: CoroutineDispatcher,
    private val nominatimApi: NominatimApi
) : LocationNameDatasource {
    override suspend fun getDisplayNameForLocation(latitude: Double, longitude: Double): String? =
        withContext(ioDispatcher) {
            try {
                val call = nominatimApi.reverseLookup(latitude, longitude)

                val response = call.execute()

                if (response.isSuccessful) {
                    return@withContext response.body()?.displayName
                } else {
                    return@withContext null
                }
            } catch (t: Throwable) {
                return@withContext null
            }
        }
}