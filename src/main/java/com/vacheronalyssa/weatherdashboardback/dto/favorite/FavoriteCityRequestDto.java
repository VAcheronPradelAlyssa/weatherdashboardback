package com.vacheronalyssa.weatherdashboardback.dto.favorite;

import jakarta.validation.constraints.NotBlank;

public record FavoriteCityRequestDto(@NotBlank String nomVille) {
}
