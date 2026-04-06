package com.vacheronalyssa.weatherdashboardback.service.impl;

import com.vacheronalyssa.weatherdashboardback.entity.FavoriteCity;
import com.vacheronalyssa.weatherdashboardback.repository.FavoriteCityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoriteCityServiceImplTest {

    @Mock
    private FavoriteCityRepository favoriteCityRepository;

    @InjectMocks
    private FavoriteCityServiceImpl favoriteCityService;

    // Vérifie qu'une ville favorite est bien persistée et renvoyée après création.
    @Test
    void addFavoriteCity_shouldSaveAndReturnEntity() {
        Long userId = 42L;
        String nomVille = "Paris";

        FavoriteCity saved = FavoriteCity.builder()
                .id(1L)
                .userId(userId)
                .nomVille(nomVille)
                .dateAjout(LocalDateTime.now())
                .build();

        when(favoriteCityRepository.save(any(FavoriteCity.class))).thenReturn(saved);

        FavoriteCity result = favoriteCityService.addFavoriteCity(userId, nomVille);

        assertEquals(saved, result);
        verify(favoriteCityRepository).save(any(FavoriteCity.class));
    }

    // Vérifie qu'une ville favorite existante peut être modifiée puis sauvegardée.
    @Test
    void updateFavoriteCity_shouldUpdateAndSaveWhenFound() {
        Long userId = 42L;
        Long favoriteCityId = 1L;
        String newNomVille = "Lyon";

        FavoriteCity existing = FavoriteCity.builder()
                .id(favoriteCityId)
                .userId(userId)
                .nomVille("Paris")
                .build();

        when(favoriteCityRepository.findByIdAndUserId(favoriteCityId, userId)).thenReturn(Optional.of(existing));
        when(favoriteCityRepository.save(existing)).thenReturn(existing);

        Optional<FavoriteCity> result = favoriteCityService.updateFavoriteCity(userId, favoriteCityId, newNomVille);

        assertTrue(result.isPresent());
        assertEquals(newNomVille, result.get().getNomVille());
        verify(favoriteCityRepository).findByIdAndUserId(favoriteCityId, userId);
        verify(favoriteCityRepository).save(existing);
    }

    // Vérifie qu'une mise à jour retourne vide si la ville favorite n'existe pas.
    @Test
    void updateFavoriteCity_shouldReturnEmptyWhenNotFound() {
        Long userId = 42L;
        Long favoriteCityId = 1L;

        when(favoriteCityRepository.findByIdAndUserId(favoriteCityId, userId)).thenReturn(Optional.empty());

        Optional<FavoriteCity> result = favoriteCityService.updateFavoriteCity(userId, favoriteCityId, "Lyon");

        assertTrue(result.isEmpty());
        verify(favoriteCityRepository).findByIdAndUserId(favoriteCityId, userId);
        verify(favoriteCityRepository, never()).save(any(FavoriteCity.class));
    }

    // Vérifie que la suppression renvoie true quand une ligne est supprimée.
    @Test
    void removeFavoriteCity_shouldReturnTrueWhenDeleteCountPositive() {
        Long userId = 42L;
        Long favoriteCityId = 1L;

        when(favoriteCityRepository.deleteByIdAndUserId(favoriteCityId, userId)).thenReturn(1L);

        boolean result = favoriteCityService.removeFavoriteCity(userId, favoriteCityId);

        assertTrue(result);
        verify(favoriteCityRepository).deleteByIdAndUserId(favoriteCityId, userId);
    }

    // Vérifie que la suppression renvoie false quand aucune ligne n'est supprimée.
    @Test
    void removeFavoriteCity_shouldReturnFalseWhenDeleteCountZero() {
        Long userId = 42L;
        Long favoriteCityId = 1L;

        when(favoriteCityRepository.deleteByIdAndUserId(favoriteCityId, userId)).thenReturn(0L);

        boolean result = favoriteCityService.removeFavoriteCity(userId, favoriteCityId);

        assertFalse(result);
        verify(favoriteCityRepository).deleteByIdAndUserId(favoriteCityId, userId);
    }

    // Vérifie que le listing retourne exactement les villes fournies par le repository.
    @Test
    void listFavoriteCities_shouldReturnRepositoryResult() {
        Long userId = 42L;
        List<FavoriteCity> cities = List.of(
                FavoriteCity.builder().id(1L).userId(userId).nomVille("Paris").build(),
                FavoriteCity.builder().id(2L).userId(userId).nomVille("Lyon").build()
        );

        when(favoriteCityRepository.findByUserId(userId)).thenReturn(cities);

        List<FavoriteCity> result = favoriteCityService.listFavoriteCities(userId);

        assertEquals(cities, result);
        verify(favoriteCityRepository).findByUserId(userId);
    }

    // Vérifie qu'une ville favorite peut être récupérée quand elle existe.
    @Test
    void getFavoriteCity_shouldReturnEntityWhenFound() {
        Long userId = 42L;
        Long favoriteCityId = 1L;
        FavoriteCity city = FavoriteCity.builder()
                .id(favoriteCityId)
                .userId(userId)
                .nomVille("Paris")
                .build();

        when(favoriteCityRepository.findByIdAndUserId(favoriteCityId, userId)).thenReturn(Optional.of(city));

        Optional<FavoriteCity> result = favoriteCityService.getFavoriteCity(userId, favoriteCityId);

        assertTrue(result.isPresent());
        assertEquals(city, result.get());
        verify(favoriteCityRepository).findByIdAndUserId(favoriteCityId, userId);
    }

    // Vérifie que la consultation retourne vide quand la ville favorite n'existe pas.
    @Test
    void getFavoriteCity_shouldReturnEmptyWhenNotFound() {
        Long userId = 42L;
        Long favoriteCityId = 1L;

        when(favoriteCityRepository.findByIdAndUserId(favoriteCityId, userId)).thenReturn(Optional.empty());

        Optional<FavoriteCity> result = favoriteCityService.getFavoriteCity(userId, favoriteCityId);

        assertTrue(result.isEmpty());
        verify(favoriteCityRepository).findByIdAndUserId(favoriteCityId, userId);
    }
}
