package the_null_pointer.preppal.util

import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

object OpenStreetMapUtil {

    val ukraineBoundingBox = getBoundingBox(
        GeoPoint(52.617908, 21.946754),
        GeoPoint(44.298513, 41.339677)
    )

    fun getBoundingBox(start: GeoPoint, end: GeoPoint): BoundingBox {
        val north: Double
        val south: Double
        val east: Double
        val west: Double
        if (start.latitude > end.latitude) {
            north = start.latitude
            south = end.latitude
        } else {
            north = end.latitude
            south = start.latitude
        }
        if (start.longitude > end.longitude) {
            east = start.longitude
            west = end.longitude
        } else {
            east = end.longitude
            west = start.longitude
        }
        return BoundingBox(north, east, south, west)
    }

    fun MapView.addMarker(latitude: Double, longitude: Double, text: String? = null) {
        val marker = Marker(this)
        marker.position = GeoPoint(latitude, longitude)
        text?.let { marker.title = it }
        this.overlays.add(marker)
    }

    fun MapView.clearAllMarkers() {
        this.overlays.removeAll { it is Marker }
    }
}