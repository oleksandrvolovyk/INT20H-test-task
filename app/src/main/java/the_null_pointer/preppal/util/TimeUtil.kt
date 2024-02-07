package the_null_pointer.preppal.util

import java.util.Calendar
import java.util.TimeZone

object TimeUtil {
    const val MILLISECONDS_IN_DAY = 86_400_000L
    const val MILLISECONDS_IN_HOUR = 3_600_000L
    const val MILLISECONDS_IN_MINUTE = 60_000L

    private val calendar: Calendar =
        Calendar.getInstance().apply { timeZone = TimeZone.getTimeZone("GMT") }

    /**
     * Gets hour of day from milliseconds.
     * @return                  Hour of day (24-hour format).
     */
    fun Long.getHour(): Int {
        calendar.timeInMillis = this
        return calendar.get(Calendar.HOUR_OF_DAY)
    }

    /**
     * Gets hour of day from milliseconds as String.
     * @return                  Hour of day (24-hour format).
     */
    fun Long.getHourAsString(): String {
        return this.getHour().toString().padStart(2, '0')
    }

    /**
     * Gets minute from milliseconds.
     * @return                  Minute.
     */
    fun Long.getMinute(): Int {
        calendar.timeInMillis = this
        return calendar.get(Calendar.MINUTE)
    }


    /**
     * Gets minute from milliseconds as String.
     * @return                  Hour of day (24-hour format).
     */
    fun Long.getMinuteAsString(): String {
        return this.getMinute().toString().padStart(2, '0')
    }

    /**
     * Gets readable date from milliseconds.
     * @return                  Date.
     */
    fun Long.getReadableDate(): String {
        calendar.timeInMillis = this
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        return "${dayOfMonth.toString().padStart(2, '0')}.${
            month.toString().padStart(2, '0')
        }.$year"
    }

    /**
     * Determines if timestamp(in millis) is a working day.
     * @return                  true, if working day
     */
    fun Long.isWorkingDay(): Boolean {
        calendar.timeInMillis = this
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return dayOfWeek == Calendar.MONDAY
                || dayOfWeek == Calendar.TUESDAY
                || dayOfWeek == Calendar.WEDNESDAY
                || dayOfWeek == Calendar.THURSDAY
                || dayOfWeek == Calendar.FRIDAY
    }

    /**
     * Determines if timestamp(in millis) is a weekend.
     * @return                  true, if weekend
     */
    fun Long.isWeekend(): Boolean {
        calendar.timeInMillis = this
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return dayOfWeek == Calendar.SATURDAY
                || dayOfWeek == Calendar.SUNDAY
    }
}