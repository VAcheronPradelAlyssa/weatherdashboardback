package com.vacheronalyssa.weatherdashboardback.dto.favorite;

import java.time.LocalDateTime;

public record FavoriteCityResponseDto(
        Long id,
        String nomVille,
        Long userId,
        LocalDateTime dateAjout
) {
}
