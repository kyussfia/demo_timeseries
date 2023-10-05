package com.reg.time_series.controller;

import com.reg.time_series.entity.TimeSeries;
import com.reg.time_series.entity.TimeSeriesRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/api/time-series")
public class TimeSeriesController {

    @Value("${app.time-series.safety-window:PT90M}")
    private Duration safetyWindow;

    private final TimeSeriesRepository timeSeriesRepository;

    public TimeSeriesController(TimeSeriesRepository timeSeriesRepository) {
        this.timeSeriesRepository = timeSeriesRepository;
    }

    @Operation(summary = "Uploading a TimeSeries.", description = "If another TimeSeries also exists in the database with the same power-station and date, the endpoint will merge it's series attribute with the actual one before persisting the new version. If no previous version found, the endpoint will simply insert it as the first version.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The uploaded TimeSeries instance, which just have got a new version.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = """
                                    {
                                       "id": 10,
                                       "date": "2021-06-28",
                                       "zone": "Europe/Budapest",
                                       "timestamp": "2021-06-28 03:30:04",
                                       "period": "PT15M",
                                       "series": [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,25701,31191,38496,47055,65508,86480,108096,131041,157709,186961,217450,246515,273840,301149,327512,353009,377330,401002,424365,445072,462962,478618,492000,492000,492000,492000,492000,492000,492000,492000,492000,492000,492000,492000,491008,479632,479883,486531,486581,492000,482488,462175,443604,416448,363034,349912,333416,312482,284440,252181,237786,203701,175479,137145,128228,118115,102348,83307,64047,47710,39699,32300,26080,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
                                       "version": 2,
                                       "power-station": "Solar Power Plant Kft. Nemesmedves"
                                     }"""
                            ))
            ),
            @ApiResponse(responseCode = "201", description = "The uploaded TimeSeries instance, which was newly created with its first version.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = """
                                    {
                                       "id": 10,
                                       "date": "2021-06-28",
                                       "zone": "Europe/Budapest",
                                       "timestamp": "2021-06-28 03:30:04",
                                       "period": "PT15M",
                                       "series": [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,25701,31191,38496,47055,65508,86480,108096,131041,157709,186961,217450,246515,273840,301149,327512,353009,377330,401002,424365,445072,462962,478618,492000,492000,492000,492000,492000,492000,492000,492000,492000,492000,492000,492000,491008,479632,479883,486531,486581,492000,482488,462175,443604,416448,363034,349912,333416,312482,284440,252181,237786,203701,175479,137145,128228,118115,102348,83307,64047,47710,39699,32300,26080,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
                                       "version": 1,
                                       "power-station": "Solar Power Plant Kft. Nemesmedves"
                                     }"""
                            ))
            ),
            @ApiResponse(responseCode = "400", description = "An erroneous attempt to upload a TimeSeries instance.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = """
                                    {
                                        "timestamp": 1696518605101,
                                        "status": 400,
                                        "error": "Bad Request",
                                        "path": "/api/time-series"
                                      }"""
                            ))
            )
    })
    @PutMapping
    public ResponseEntity<TimeSeries> uploadTimeSeries(
            @Parameter(name = "actualTimeSeries", description = "A JSON object representing a TimeSeries instance.",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = TimeSeries.class),
                                    examples = {
                                            @ExampleObject("{\"power-station\":\"Naperőmű 2021 Kft. Iborfia\",\"date\":\"2021-06-28\",\"zone\":\"Europe/Budapest\",\"period\":\"PT15M\",\"timestamp\":\"2021-06-28 05:29:55\",\"series\":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3233,7100,11799,16937,22874,33349,55666,80118,105579,131253,156958,182177,207351,232510,256735,280352,303047,324805,345681,365056,383061,399815,414929,428281,439972,449182,456992,461497,464096,465306,466606,464007,462817,457619,452420,448305,437404,424383,411258,397889,384082,368341,351585,333393,314305,294200,269827,243835,219179,192625,166633,137941,116004,91589,67790,45141,28813,22236,16138,10753,6197,2652,565,0,0,0,0,0,0,0,0,0,0,0,0,0]}")
                                    }
                            )
                    }
            ) @RequestBody TimeSeries actualTimeSeries
    ) {
        TimeSeries latest = this.timeSeriesRepository.findFirstByPowerStationAndDateOrderByVersionDesc(actualTimeSeries.getPowerStation(), actualTimeSeries.getDate());

        if (null == latest) {
            return ResponseEntity.status(HttpStatus.CREATED).body(this.timeSeriesRepository.save(actualTimeSeries));
        }

        return ResponseEntity.ok(this.timeSeriesRepository.save(actualTimeSeries.merge(latest, this.safetyWindow)));
    }

    @Operation(summary = "Get the power stations.", description = "Get all of the power stations from the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of the power stations.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(
                            value = """
                                    [
                                        "Naperőmű 2021 Kft. Iborfia",
                                        "Solar Power Plant Kft. Nemesmedves"
                                    ]
                                    """
                    ))
            )
    })
    @GetMapping(value = "/power-stations")
    public ResponseEntity<List<String>> getPowerStations() {
        return ResponseEntity.ok(this.timeSeriesRepository.findAllDistinctPowerStations());
    }

    @Operation(summary = "Get the available dates of a power station.", description = "Get all of the available dates from the database for a specific power station.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of the dates of the selected power station.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(
                            value = """
                                    [
                                        "2021-06-28"
                                    ]
                                    """
                    ))
            )
    })
    @GetMapping("/power-stations/{powerStation}/dates")
    public ResponseEntity<List<String>> getDatesByPowerStation(
            @Parameter(name = "powerStation", description = "The selected power station.", example = "Naperőmű 2021 Kft. Iborfia") @PathVariable String powerStation
    ) {
        return ResponseEntity.ok(timeSeriesRepository.findDistinctDatesByPowerStation(powerStation));
    }

    @Operation(summary = "Get every version of TimeSeries by the specified power station and date.", description = "Get all records from the database which has the selected power station and the desired date.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of the TimeSeries versions.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(
                            value = """
                                    [
                                        {
                                            "id": 7,
                                            "date": "2021-06-28",
                                            "zone": "Europe/Budapest",
                                            "timestamp": "2021-06-28 03:29:53",
                                            "period": "PT15M",
                                            "series": [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,25701,31191,38496,47055,65508,86480,108096,131041,157709,186961,217450,246515,273840,301149,327512,353009,377330,401002,424365,445072,462962,478618,492000,492000,492000,492000,492000,492000,492000,492000,492000,492000,492000,492000,491008,479632,479883,486531,486581,492000,482488,462175,443604,416448,363034,349912,333416,312482,284440,252181,237786,203701,175479,137145,128228,118115,102348,83307,64047,47710,39699,32300,26080,0,0,0,0,0,0,0,0,0,0,0,0,0,0],,
                                            "version": 1,
                                            "power-station": "Naperőmű 2021 Kft. Iborfia"
                                        }
                                    ]"""
                    ))
            )
    })
    @GetMapping("/power-stations/{powerStation}/dates/{date}")
    public ResponseEntity<List<TimeSeries>> getDatesByPowerStation(
            @Parameter(name = "powerStation", description = "The selected power station.", example = "Naperőmű 2021 Kft. Iborfia") @PathVariable String powerStation,
            @Parameter(name = "date", description = "The selected date.", example = "2021-06-28") @PathVariable String date
    ) {
        return ResponseEntity.ok(timeSeriesRepository.findByPowerStationAndDateOrderByVersionAsc(powerStation, date));
    }
}
