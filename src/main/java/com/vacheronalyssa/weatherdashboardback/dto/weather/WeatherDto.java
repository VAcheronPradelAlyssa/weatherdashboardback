package com.vacheronalyssa.weatherdashboardback.dto.weather;

public record WeatherDto(
        String city,
        String description,
        Double temperature,
        Double feelsLike,
        Integer humidity,
        Double windSpeed,
        String icon
) {
}
