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

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class CityController {

    private final CitySearchService citySearchService;

    @GetMapping("/search-cities")
    public ResponseEntity<List<CityDto>> searchCities(
            @RequestParam("q") @NotBlank @Size(min = 2, max = 100) String q,
            @RequestParam(defaultValue = "5") @Min(1) @Max(20) int limit
    ) {
        return ResponseEntity.ok(citySearchService.searchCities(q, limit));
    }
}
