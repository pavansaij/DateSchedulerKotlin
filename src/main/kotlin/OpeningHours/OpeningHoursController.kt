package OpeningHours

import OpeningHours.Models.Days
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*


@RestController
class OpeningHoursController {

    @RequestMapping(value = ["getWeekSchedule"], headers = ["Accept=application/json"], method = [RequestMethod.POST],
            consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getWeekSchedule(@RequestBody openingHours: Map<String, List<Map<String, Any>>>): Map<Days, String> {
        return WeekScheduleGenerator().generateWeekSchedule(openingHours);
    }
}