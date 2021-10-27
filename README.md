# Sample Kotlin Application

### Testing

```
Windows : gradlew clean test --tests WeekScheduleGeneratorTest --rerun-tasks
Linux : ./gradlew clean test --tests WeekScheduleGeneratorTest --rerun-tasks
```

### Running

```
Windows : gradlew bootRun
Linux : ./gradlew bootRun
```

### Algorithm

```
Pre-Processing : Sort the openHour Items in ascending order based on the day and the value of the openingHour.
                    This is to make sure that we can generate schedule properly.

This actual algorithm works in a linear time. Where it creates a schedule for every Closed Item associating it with previously encountered open item, So it expects a closed item for every open time.

The ways the input data could malformed are as follows :

- Duplicate days as we are supporting case insensitive day string.
- opening and closing hours may be not in pairs.
- values may be not sorted.
- ranges may be overlapping.
- next day may not have a closing hour for the previous one.

```

### Input Modification

```
At the current input state it is only possible to generate schedule for a single week. If we pass the complete epoc value in every openHour item
there is no need to pass the day specifically as we can get the date and time from the complete epoch. So the input would look like the below one,

{
    [1626592762, Open],
    [1626592776, Close]
}

As we can see the payload of the input is reduced and we can also generate schedule for multiple weeks with this format.

```


### Performing requests

Look into `curl_test` Shell script to get an idea on how to invoke requests
using `curl`. Try these:

```
./curl_test correctInp1
./test-j errorDup
./test-j incorrectDay
```

Example Python Client Stub for invoke request

```
import requests, json

url = "http://localhost:8080/getWeekSchedule"

data = {"monday" : [], "tuesday" : [
{ "type" : "open", "value" : 36000 },
{ "type" : "close", "value" : 64800 }
],
"wednesday" : [],
"thursday" : [
{ "type" : "open", "value" : 37800 },
{ "type" : "close", "value" : 64800 }
],
"friday" : [
{ "type" : "open", "value" : 36000 }
],
"saturday" : [
{ "type" : "close", "value" : 3600 },
{ "type" : "open", "value" : 36000 }
],
"sunday" : [
{ "type" : "close", "value" : 3600 },
{ "type" : "open", "value" : 43200 },
{ "type" : "close", "value" : 75600 }
]
}
resp = requests.post(url, data = json.dumps(data), headers={'Content-Type': 'application/json'})
```
