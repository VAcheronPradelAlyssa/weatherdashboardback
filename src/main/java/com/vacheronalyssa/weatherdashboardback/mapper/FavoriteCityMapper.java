package com.vacheronalyssa.weatherdashboardback.mapper;

import com.vacheronalyssa.weatherdashboardback.dto.favorite.FavoriteCityResponseDto;
import com.vacheronalyssa.weatherdashboardback.entity.FavoriteCity;

public final class FavoriteCityMapper {

    private FavoriteCityMapper() {
    }

    public static FavoriteCityResponseDto toResponseDto(FavoriteCity favoriteCity) {
        return new FavoriteCityResponseDto(
                favoriteCity.getId(),
                favoriteCity.getNomVille(),
                favoriteCity.getUserId(),
                favoriteCity.getDateAjout()
        );
    }
}
