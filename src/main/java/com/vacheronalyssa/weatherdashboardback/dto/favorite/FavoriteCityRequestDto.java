package com.vacheronalyssa.weatherdashboardback.dto.favorite;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FavoriteCityRequestDto(
	@NotBlank(message = "Le nom de ville est obligatoire")
	@Size(min = 2, max = 100, message = "Le nom de ville doit contenir entre 2 et 100 caracteres")
	String nomVille
) {
}
