package com.vacheronalyssa.weatherdashboardback.service;

import java.util.Map;

public interface OpenWeatherService {

    Map<String, Object> getCurrentWeatherByCity(String city);
}
