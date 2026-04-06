package com.vacheronalyssa.weatherdashboardback.service.impl;

import com.vacheronalyssa.weatherdashboardback.dto.weather.WeatherDto;
import com.vacheronalyssa.weatherdashboardback.mapper.OpenWeatherMapper;
import com.vacheronalyssa.weatherdashboardback.service.OpenWeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenWeatherServiceImpl implements OpenWeatherService {

    private final RestTemplate restTemplate;

    @Value("${openweather.api.key:}")
    private String apiKey;

    @Value("${openweather.api.base-url:https://api.openweathermap.org/data/2.5}")
    private String baseUrl;

    @Override
    public WeatherDto getCurrentWeatherByCity(String city) {
        if (!StringUtils.hasText(apiKey)) {
            throw new IllegalStateException("OPENWEATHER_API_KEY est manquante. Configurez la variable d'environnement.");
        }

        String url = UriComponentsBuilder
            .fromUriString(baseUrl)
                .path("/weather")
                .queryParam("q", city)
                .queryParam("appid", apiKey)
                .queryParam("units", "metric")
                .queryParam("lang", "fr")
                .build()
                .toUriString();

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {
            }
        );

        Map<String, Object> body = response.getBody();
        if (body == null || body.isEmpty()) {
            throw new IllegalStateException("Réponse OpenWeatherMap vide.");
        }

        return OpenWeatherMapper.toWeatherDto(body);
    }
}
