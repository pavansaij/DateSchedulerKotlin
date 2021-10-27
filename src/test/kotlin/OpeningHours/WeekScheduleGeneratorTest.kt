package OpeningHours

import OpeningHours.Models.Days
import com.google.gson.Gson
import org.junit.jupiter.api.Test
import java.io.BufferedReader
import java.io.File

class WeekScheduleGeneratorTest {
    @Test
    fun testCorrectInput1() {
        var openingHours = readJsonFile("src\\test\\kotlin\\Resources\\correctInp1.json")

        var resWeekSchedule: Map<Days, String> = WeekScheduleGenerator().generateWeekSchedule(openingHours)

        assert(resWeekSchedule[Days.monday].equals("Closed"))
        assert(resWeekSchedule[Days.tuesday].equals("Closed"))
        assert(resWeekSchedule[Days.wednesday].equals("Closed"))
        assert(resWeekSchedule[Days.thursday].equals("Closed"))
        assert(resWeekSchedule[Days.friday].equals("6 PM - 1 AM"))
        assert(resWeekSchedule[Days.saturday].equals("9 AM - 11 AM, 4 PM - 11 PM"))
        assert(resWeekSchedule[Days.sunday].equals("Closed"))
    }

    @Test
    fun testCorrectInput2() {
        var openingHours = readJsonFile("src\\test\\kotlin\\Resources\\correctInp2.json")

        var resWeekSchedule: Map<Days, String> = WeekScheduleGenerator().generateWeekSchedule(openingHours)

        assert(resWeekSchedule[Days.monday].equals("Closed"))
        assert(resWeekSchedule[Days.tuesday].equals("10 AM - 6 PM"))
        assert(resWeekSchedule[Days.wednesday].equals("Closed"))
        assert(resWeekSchedule[Days.thursday].equals("10:30 AM - 6 PM"))
        assert(resWeekSchedule[Days.friday].equals("10 AM - 1 AM"))
        assert(resWeekSchedule[Days.saturday].equals("10 AM - 1 AM"))
        assert(resWeekSchedule[Days.sunday].equals("12 PM - 9 PM"))
    }

    @Test
    fun testDuplicateDatInput() {
        var openingHours = readJsonFile("src\\test\\kotlin\\Resources\\errorDup.json")
        var excepted: Boolean = false

        try {
            WeekScheduleGenerator().generateWeekSchedule(openingHours)
        } catch (ex: Exception) {
            excepted = true
        }

        assert(excepted)
    }

    @Test
    fun testIncorrectDayInput() {
        var openingHours = readJsonFile("src\\test\\kotlin\\Resources\\incorrectDay.json")
        var excepted: Boolean = false

        try {
            WeekScheduleGenerator().generateWeekSchedule(openingHours)
        } catch (ex: Exception) {
            excepted = true
        }

        assert(excepted)
    }

    @Test
    fun testIncorrectType() {
        var openingHours = readJsonFile("src\\test\\kotlin\\Resources\\incorrectType.json")
        var excepted: Boolean = false

        try {
            WeekScheduleGenerator().generateWeekSchedule(openingHours)
        } catch (ex: Exception) {
            excepted = true
        }

        assert(excepted)
    }

    @Test
    fun testMissingValueInput() {
        var openingHours = readJsonFile("src\\test\\kotlin\\Resources\\missingValue.json")
        var excepted: Boolean = false

        try {
            WeekScheduleGenerator().generateWeekSchedule(openingHours)
        } catch (ex: Exception) {
            excepted = true
        }

        assert(excepted)
    }

    @Test
    fun testErrorInpMissingClose() {
        var openingHours = readJsonFile("src\\test\\kotlin\\Resources\\errorInpMissingClose.json")
        var excepted: Boolean = false

        try {
            WeekScheduleGenerator().generateWeekSchedule(openingHours)
        } catch (ex: Exception) {
            excepted = true
        }

        assert(excepted)
    }

    private fun readJsonFile(filePath: String): Map<String, List<MutableMap<String, Any>>> {
        var bufferedReader: BufferedReader = try {
            File(filePath).bufferedReader()
        } catch (ex: Exception) {
            //To make path linux compatible
            File(filePath.replace("\\", "/")).bufferedReader()
        }

        val jsonString = bufferedReader.use { it.readText() }

        var map: MutableMap<String, List<MutableMap<String, Any>>> = mutableMapOf()
        map = Gson().fromJson(jsonString, map.javaClass)


        //Kotlin's buffered reader read's int as double. This logic is to convert it back to int
        for ((_, value) in map) {
            for (item in value) {
                if (item.containsKey("value")) {
                    var doubleVal: Double = item["value"] as Double
                    item["value"] = doubleVal.toInt()
                }
            }
        }

        return map
    }
}