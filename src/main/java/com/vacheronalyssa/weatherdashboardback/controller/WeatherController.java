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
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequiredArgsConstructor
@Validated
@Tag(name = "Météo", description = "Consultation des prévisions météo en temps réel.")
public class WeatherController {

    private final OpenWeatherService openWeatherService;
    private final FavoriteCityService favoriteCityService;

    @GetMapping("/api/weather/current")
    @Operation(summary = "Obtenir la météo actuelle", description = "Retourne la météo actuelle pour une ville donnée.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Météo récupérée avec succès",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = WeatherDto.class)))
    })
    public ResponseEntity<WeatherDto> getCurrentWeather(
            @Parameter(description = "Nom de la ville", example = "Paris")
            @RequestParam @NotBlank String city) {
        WeatherDto weather = openWeatherService.getCurrentWeatherByCity(city);
        return ResponseEntity.ok(weather);
    }

        @GetMapping({
            "/api/weather/favorite/{userId}/{favoriteCityId}",
            "/api/users/{userId}/favorite-cities/{favoriteCityId}/weather"
        })
        @Operation(summary = "Obtenir la météo d'une ville favorite", description = "Retourne la météo actuelle pour une ville favorite d'un utilisateur.")
        @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Météo récupérée avec succès",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = WeatherDto.class))),
        @ApiResponse(responseCode = "404", description = "Ville favorite introuvable", content = @Content)
        })
        public ResponseEntity<WeatherDto> getFavoriteWeather(
            @Parameter(description = "ID de l'utilisateur", example = "1") @PathVariable @Positive Long userId,
            @Parameter(description = "ID de la ville favorite", example = "10") @PathVariable @Positive Long favoriteCityId
        ) {
        String cityName = favoriteCityService.getFavoriteCity(userId, favoriteCityId)
            .map(city -> city.getNomVille())
            .orElseThrow(() -> new ResourceNotFoundException("Ville favorite introuvable"));

        WeatherDto weather = openWeatherService.getCurrentWeatherByCity(cityName);
        return ResponseEntity.ok(weather);
        }
}
