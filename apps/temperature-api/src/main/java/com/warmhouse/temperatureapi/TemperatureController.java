package com.warmhouse.temperatureapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class TemperatureController {

    // GET /temperature?location=...
    @GetMapping("/temperature")
    public TemperatureResponse getByLocation(@RequestParam(required = false, defaultValue = "") String location, @RequestParam(required = false, defaultValue = "") String sensorId) {
        return buildResponse(location, sensorId);
    }

    // GET /temperature/{sensorId}
    @GetMapping("/temperature/{sensorId}")
    public TemperatureResponse getBySensorId(@PathVariable String sensorId) {
        return buildResponse("", sensorId);
    }

    private TemperatureResponse buildResponse(String location, String sensorId) {
        // Если нет location — определяем по sensorId
        if (location == null || location.isEmpty()) {
            location = switch (sensorId) {
                case "1" -> "Living Room";
                case "2" -> "Bedroom";
                case "3" -> "Kitchen";
                default -> "Unknown";
            };
        }

        // Если нет sensorId — определяем по location
        if (sensorId == null || sensorId.isEmpty()) {
            sensorId = switch (location) {
                case "Living Room" -> "1";
                case "Bedroom" -> "2";
                case "Kitchen" -> "3";
                default -> "0";
            };
        }

        double value = ThreadLocalRandom.current().nextDouble(15.0, 30.0);
        value = Math.round(value * 10.0) / 10.0;

        return new TemperatureResponse(value, "celsius",
                OffsetDateTime.now(), location,
                "active", sensorId, "temperature",
                "Random temperature reading from " + location);
    }

    public record TemperatureResponse(@JsonProperty("value") double value, @JsonProperty("unit") String unit,
                                      @JsonProperty("timestamp") OffsetDateTime timestamp,
                                      @JsonProperty("location") String location, @JsonProperty("status") String status,
                                      @JsonProperty("sensor_id") String sensorId,
                                      @JsonProperty("sensor_type") String sensorType,
                                      @JsonProperty("description") String description) {
    }
}