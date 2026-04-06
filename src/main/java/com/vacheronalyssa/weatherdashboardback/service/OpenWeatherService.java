package com.vacheronalyssa.weatherdashboardback.service;

import com.vacheronalyssa.weatherdashboardback.dto.weather.WeatherDto;

public interface OpenWeatherService {

    WeatherDto getCurrentWeatherByCity(String city);
}
