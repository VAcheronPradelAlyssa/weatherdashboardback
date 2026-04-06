package com.vacheronalyssa.weatherdashboardback.controller;

import com.vacheronalyssa.weatherdashboardback.dto.city.CityDto;
import com.vacheronalyssa.weatherdashboardback.service.CitySearchService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
@Tag(name = "Villes", description = "Recherche de villes pour l'autocomplétion et la sélection.")
public class CityController {

    private final CitySearchService citySearchService;

    @GetMapping("/search-cities")
    @Operation(summary = "Rechercher des villes", description = "Recherche des villes correspondant à une chaîne pour l'autocomplétion.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des villes trouvée",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CityDto.class)))
    })
    public ResponseEntity<List<CityDto>> searchCities(
            @Parameter(description = "Chaîne de recherche (au moins 2 caractères)", example = "Paris")
            @RequestParam("q") @NotBlank @Size(min = 2, max = 100) String q,
            @Parameter(description = "Nombre maximum de résultats (1 à 20)", example = "5")
            @RequestParam(defaultValue = "5") @Min(1) @Max(20) int limit
    ) {
        return ResponseEntity.ok(citySearchService.searchCities(q, limit));
    }
}
