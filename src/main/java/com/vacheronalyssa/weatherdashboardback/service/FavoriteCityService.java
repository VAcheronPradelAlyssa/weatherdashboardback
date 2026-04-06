package com.vacheronalyssa.weatherdashboardback.service;

import com.vacheronalyssa.weatherdashboardback.entity.FavoriteCity;

import java.util.List;
import java.util.Optional;

public interface FavoriteCityService {

    FavoriteCity addFavoriteCity(Long userId, String nomVille);

    Optional<FavoriteCity> updateFavoriteCity(Long userId, Long favoriteCityId, String nomVille);

    boolean removeFavoriteCity(Long userId, Long favoriteCityId);

    List<FavoriteCity> listFavoriteCities(Long userId);

    Optional<FavoriteCity> getFavoriteCity(Long userId, Long favoriteCityId);
}
