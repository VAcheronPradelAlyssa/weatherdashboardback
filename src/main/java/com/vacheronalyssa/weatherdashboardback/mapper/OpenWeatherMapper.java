package com.vacheronalyssa.weatherdashboardback.mapper;

import com.vacheronalyssa.weatherdashboardback.dto.weather.WeatherDto;

import java.util.List;
import java.util.Map;

public final class OpenWeatherMapper {

    private OpenWeatherMapper() {
    }

    @SuppressWarnings("unchecked")
    public static WeatherDto toWeatherDto(Map<String, Object> raw) {
        String city = asString(raw.get("name"));

        Map<String, Object> main = raw.get("main") instanceof Map<?, ?>
                ? (Map<String, Object>) raw.get("main")
                : Map.of();

        Map<String, Object> wind = raw.get("wind") instanceof Map<?, ?>
                ? (Map<String, Object>) raw.get("wind")
                : Map.of();

        List<Map<String, Object>> weatherList = raw.get("weather") instanceof List<?>
                ? (List<Map<String, Object>>) raw.get("weather")
                : List.of();

        Map<String, Object> weather0 = weatherList.isEmpty() ? Map.of() : weatherList.getFirst();

        return new WeatherDto(
                city,
                asString(weather0.get("description")),
                asDouble(main.get("temp")),
                asDouble(main.get("feels_like")),
                asInteger(main.get("humidity")),
                asDouble(wind.get("speed")),
                asString(weather0.get("icon"))
        );
    }

    private static String asString(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    private static Double asDouble(Object value) {
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        return null;
    }

    private static Integer asInteger(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        return null;
    }
}
