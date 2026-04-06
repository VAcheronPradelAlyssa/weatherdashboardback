package com.vacheronalyssa.weatherdashboardback.service;

import com.vacheronalyssa.weatherdashboardback.dto.city.CityDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CitySearchService {

    private static final String OPENWEATHERMAP_GEO_URL = "https://api.openweathermap.org/geo/1.0/direct";

    private final RestTemplate restTemplate;

    @Value("${openweather.api.key:}")
    private String apiKey;

    public List<CityDto> searchCities(String query, int limit) {
        if (!StringUtils.hasText(apiKey)) {
            throw new IllegalStateException("OPENWEATHER_API_KEY est manquante. Configurez la variable d'environnement.");
        }

        if (!StringUtils.hasText(query) || query.trim().length() < 2) {
            return List.of();
        }

        int safeLimit = Math.max(1, Math.min(limit, 20));

        String url = UriComponentsBuilder
                .fromUriString(OPENWEATHERMAP_GEO_URL)
                .queryParam("q", query.trim())
                .queryParam("limit", safeLimit)
                .queryParam("appid", apiKey)
                .build()
                .toUriString();

        CityDto[] cities = restTemplate.getForObject(url, CityDto[].class);
        return cities != null ? Arrays.asList(cities) : List.of();
    }
}
