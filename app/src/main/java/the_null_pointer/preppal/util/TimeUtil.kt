package the_null_pointer.preppal.util

import java.util.Calendar

object TimeUtil {
    private val calendar: Calendar = Calendar.getInstance()

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
        return "${dayOfMonth.toString().padStart(2, '0')}.${month.toString().padStart(2, '0')}.$year"
    }
}