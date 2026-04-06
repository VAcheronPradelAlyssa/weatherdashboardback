package com.vacheronalyssa.weatherdashboardback.controller;

import com.vacheronalyssa.weatherdashboardback.dto.favorite.FavoriteCityRequestDto;
import com.vacheronalyssa.weatherdashboardback.dto.favorite.FavoriteCityResponseDto;
import com.vacheronalyssa.weatherdashboardback.entity.FavoriteCity;
import com.vacheronalyssa.weatherdashboardback.exception.ResourceNotFoundException;
import com.vacheronalyssa.weatherdashboardback.mapper.FavoriteCityMapper;
import com.vacheronalyssa.weatherdashboardback.service.FavoriteCityService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/favorite-cities")
@RequiredArgsConstructor
@Validated
public class FavoriteCityController {

    private final FavoriteCityService favoriteCityService;

    @GetMapping
    public ResponseEntity<List<FavoriteCityResponseDto>> list(@PathVariable @Positive Long userId) {
        return ResponseEntity.ok(
                favoriteCityService.listFavoriteCities(userId)
                        .stream()
                        .map(FavoriteCityMapper::toResponseDto)
                        .toList()
        );
    }

    @GetMapping("/{favoriteCityId}")
    public ResponseEntity<FavoriteCityResponseDto> get(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long favoriteCityId
    ) {
        return favoriteCityService.getFavoriteCity(userId, favoriteCityId)
                .map(FavoriteCityMapper::toResponseDto)
                .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResourceNotFoundException("Ville favorite introuvable"));
    }

    @PostMapping
    public ResponseEntity<FavoriteCityResponseDto> create(
            @PathVariable @Positive Long userId,
            @Valid @RequestBody FavoriteCityRequestDto request
    ) {
        FavoriteCity created = favoriteCityService.addFavoriteCity(userId, request.nomVille());
        return ResponseEntity.status(HttpStatus.CREATED).body(FavoriteCityMapper.toResponseDto(created));
    }

    @PutMapping("/{favoriteCityId}")
    public ResponseEntity<FavoriteCityResponseDto> update(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long favoriteCityId,
            @Valid @RequestBody FavoriteCityRequestDto request
    ) {
        return favoriteCityService.updateFavoriteCity(userId, favoriteCityId, request.nomVille())
                .map(FavoriteCityMapper::toResponseDto)
                .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResourceNotFoundException("Ville favorite introuvable"));
    }

    @DeleteMapping("/{favoriteCityId}")
    public ResponseEntity<Void> delete(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long favoriteCityId
    ) {
        boolean deleted = favoriteCityService.removeFavoriteCity(userId, favoriteCityId);
        if (!deleted) {
            throw new ResourceNotFoundException("Ville favorite introuvable");
        }
        return ResponseEntity.noContent().build();
    }
}
