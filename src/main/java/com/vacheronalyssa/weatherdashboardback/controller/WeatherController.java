package com.vacheronalyssa.weatherdashboardback.controller;

import com.vacheronalyssa.weatherdashboardback.dto.weather.WeatherDto;
import com.vacheronalyssa.weatherdashboardback.exception.ResourceNotFoundException;
import com.vacheronalyssa.weatherdashboardback.service.FavoriteCityService;
import com.vacheronalyssa.weatherdashboardback.service.OpenWeatherService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class WeatherController {

    private final OpenWeatherService openWeatherService;
    private final FavoriteCityService favoriteCityService;

    @GetMapping("/api/weather/current")
    public ResponseEntity<WeatherDto> getCurrentWeather(@RequestParam @NotBlank String city) {
        WeatherDto weather = openWeatherService.getCurrentWeatherByCity(city);
        return ResponseEntity.ok(weather);
    }

    @GetMapping({
            "/api/weather/favorite/{userId}/{favoriteCityId}",
            "/api/users/{userId}/favorite-cities/{favoriteCityId}/weather"
    })
    public ResponseEntity<WeatherDto> getFavoriteWeather(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long favoriteCityId
    ) {
        String cityName = favoriteCityService.getFavoriteCity(userId, favoriteCityId)
                .map(city -> city.getNomVille())
                .orElseThrow(() -> new ResourceNotFoundException("Ville favorite introuvable"));

        WeatherDto weather = openWeatherService.getCurrentWeatherByCity(cityName);
        return ResponseEntity.ok(weather);
    }
}
