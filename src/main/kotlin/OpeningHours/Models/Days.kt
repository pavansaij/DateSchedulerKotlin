package OpeningHours.Models

import com.fasterxml.jackson.annotation.JsonCreator
import java.lang.IllegalArgumentException

enum class Days() {
    monday, tuesday, wednesday, thursday, friday, saturday, sunday;

    companion object {
        fun getCaseInsensitiveDay(inpDay: String): Days {
            if (Days.values().any { it.name == inpDay.toLowerCase() }) {
                return Days.valueOf(inpDay.toLowerCase())
            } else {
                throw IllegalArgumentException("Invalid day string passed in input")
            }
        }
    }
}