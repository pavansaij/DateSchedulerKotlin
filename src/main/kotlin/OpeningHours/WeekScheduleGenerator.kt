package OpeningHours

import OpeningHours.Models.Days
import OpeningHours.Models.OpeningHour
import OpeningHours.Models.Type
import java.lang.IllegalArgumentException

class WeekScheduleGenerator {

    fun generateWeekSchedule(openingHours: Map<String, List<Map<String, Any>>>): Map<Days, String> {
        var openingHourObjects: List<OpeningHour> = validateAndGenerateOpeningHours(openingHours)
        return generateWeekSchedule(openingHourObjects)
    }

    private fun validateAndGenerateOpeningHours(openingHours: Map<String, List<Map<String, Any>>>): List<OpeningHour> {
        var resOpeningHours: MutableList<OpeningHour> = mutableListOf()
        var seenDays: MutableSet<Days> = mutableSetOf() //This set to identify duplicate days in json


        for ((day, openingList) in openingHours) {
            if (seenDays.contains(Days.getCaseInsensitiveDay(day))) {
                throw IllegalArgumentException("Found duplicate entries for " + Days.getCaseInsensitiveDay(day))
            }

            for (openingItem in openingList) {
                validateOpenHourItem(openingItem)
                resOpeningHours.add(OpeningHour(day, openingItem["type"] as String, openingItem["value"] as Int))
            }
            seenDays.add(Days.getCaseInsensitiveDay(day))
        }

        //Sort the entries with keys (Day and Value) to make sure the algo for scheduling works
        return resOpeningHours.sortedWith(compareBy({ it.day }, { it.value }))
    }

    private fun validateOpenHourItem(openingHourItem: Map<String, Any>) {
        if (openingHourItem.keys.size != 2) {
            throw IllegalArgumentException("Invalid OpenHourItem passed. OpenHourItem should only contain type and value")
        }

        if (!openingHourItem.containsKey("type")) {
            throw IllegalArgumentException("Invalid OpenHourItem passed. OpenHourItem must contain type")
        }

        if (!openingHourItem.containsKey("value")) {
            throw IllegalArgumentException("Invalid OpenHourItem passed. OpenHourItem must contain value")
        }
    }

    /*
        This algorithm works in a linear time.
        Where it creates a schedule for every Closed Item associating it with previously encountered open item,
        So it expects a closed item for every open time.
    */
    private fun generateWeekSchedule(openingHoursList: List<OpeningHour>): Map<Days, String> {
        var previousOpenHour: OpeningHour? = null

        var resWeekSchedule: Map<Days, MutableList<String>>? = Days.values().associate { it to mutableListOf<String>() }

        for (openingHourItem in openingHoursList) {
            if (openingHourItem.type == Type.close && previousOpenHour == null) {
                throw IllegalArgumentException("Invalid input passed. A closed hour found without preceding open hour")
            }

            if (openingHourItem.type == Type.open && previousOpenHour != null) {
                throw IllegalArgumentException("Invalid input passed. Multiple open hours found without closed hour in between")
            }

            if (openingHourItem.type == Type.close) {
                var openHoursRange: String = getReadableTime(previousOpenHour!!.value) + " - " +
                        getReadableTime(openingHourItem.value)

                resWeekSchedule!![previousOpenHour.day]!!.add(openHoursRange)
                previousOpenHour = null
            } else {
                previousOpenHour = openingHourItem
            }
        }

        if (previousOpenHour != null) {
            throw IllegalArgumentException("Invalid input passed, an open type found without following close type value")
        }

        return resWeekSchedule!!.entries.associate { it.key to if (it.value.size != 0) it.value.joinToString(separator = ", ") else "Closed" }
    }

    private fun getReadableTime(milliSecs: Int): String {
        val hr: Int = milliSecs / 3600
        val mn: Int = (milliSecs % 3600) / 60
        val amOrPm: String = if (hr > 11) " PM" else " AM"

        val hrStr: String = (if (hr % 12 != 0) hr % 12 else 12).toString()
        val mnStr: String = if (mn == 0) "" else ":%02d".format(mn)

        return hrStr + mnStr + amOrPm
    }
}