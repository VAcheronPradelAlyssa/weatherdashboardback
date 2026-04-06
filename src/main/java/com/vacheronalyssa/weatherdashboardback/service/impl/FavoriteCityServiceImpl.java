package com.vacheronalyssa.weatherdashboardback.service.impl;

import com.vacheronalyssa.weatherdashboardback.entity.FavoriteCity;
import com.vacheronalyssa.weatherdashboardback.repository.FavoriteCityRepository;
import com.vacheronalyssa.weatherdashboardback.service.FavoriteCityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteCityServiceImpl implements FavoriteCityService {

    private final FavoriteCityRepository favoriteCityRepository;

    @Override
    public FavoriteCity addFavoriteCity(Long userId, String nomVille) {
        validateInputs(userId, nomVille);

        FavoriteCity favoriteCity = FavoriteCity.builder()
                .userId(userId)
            .nomVille(nomVille.trim())
                .build();

        return favoriteCityRepository.save(favoriteCity);
    }

    @Override
    public Optional<FavoriteCity> updateFavoriteCity(Long userId, Long favoriteCityId, String nomVille) {
        validateInputs(userId, nomVille);
        if (favoriteCityId == null || favoriteCityId <= 0) {
            throw new IllegalArgumentException("favoriteCityId doit etre strictement positif");
        }

        return favoriteCityRepository.findByIdAndUserId(favoriteCityId, userId)
                .map(existing -> {
                    existing.setNomVille(nomVille.trim());
                    return favoriteCityRepository.save(existing);
                });
    }

    @Override
    public boolean removeFavoriteCity(Long userId, Long favoriteCityId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("userId doit etre strictement positif");
        }
        if (favoriteCityId == null || favoriteCityId <= 0) {
            throw new IllegalArgumentException("favoriteCityId doit etre strictement positif");
        }

        long deleted = favoriteCityRepository.deleteByIdAndUserId(favoriteCityId, userId);
        return deleted > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FavoriteCity> listFavoriteCities(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("userId doit etre strictement positif");
        }

        return favoriteCityRepository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FavoriteCity> getFavoriteCity(Long userId, Long favoriteCityId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("userId doit etre strictement positif");
        }
        if (favoriteCityId == null || favoriteCityId <= 0) {
            throw new IllegalArgumentException("favoriteCityId doit etre strictement positif");
        }

        return favoriteCityRepository.findByIdAndUserId(favoriteCityId, userId);
    }

    private void validateInputs(Long userId, String nomVille) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("userId doit etre strictement positif");
        }
        if (!StringUtils.hasText(nomVille)) {
            throw new IllegalArgumentException("nomVille est obligatoire");
        }
    }
}
