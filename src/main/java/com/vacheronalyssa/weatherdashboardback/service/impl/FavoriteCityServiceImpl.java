package com.vacheronalyssa.weatherdashboardback.service.impl;

import com.vacheronalyssa.weatherdashboardback.entity.FavoriteCity;
import com.vacheronalyssa.weatherdashboardback.repository.FavoriteCityRepository;
import com.vacheronalyssa.weatherdashboardback.service.FavoriteCityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteCityServiceImpl implements FavoriteCityService {

    private final FavoriteCityRepository favoriteCityRepository;

    @Override
    public FavoriteCity addFavoriteCity(Long userId, String nomVille) {
        FavoriteCity favoriteCity = FavoriteCity.builder()
                .userId(userId)
                .nomVille(nomVille)
                .build();

        return favoriteCityRepository.save(favoriteCity);
    }

    @Override
    public boolean removeFavoriteCity(Long userId, Long favoriteCityId) {
        long deleted = favoriteCityRepository.deleteByIdAndUserId(favoriteCityId, userId);
        return deleted > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FavoriteCity> listFavoriteCities(Long userId) {
        return favoriteCityRepository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FavoriteCity> getFavoriteCity(Long userId, Long favoriteCityId) {
        return favoriteCityRepository.findByIdAndUserId(favoriteCityId, userId);
    }
}
