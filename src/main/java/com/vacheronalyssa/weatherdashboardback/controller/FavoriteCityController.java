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
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/favorite-cities")
@RequiredArgsConstructor
@Validated
@Tag(name = "Villes favorites", description = "Gestion des villes favorites de l'utilisateur.")
public class FavoriteCityController {

    private final FavoriteCityService favoriteCityService;

    @GetMapping
    @Operation(summary = "Lister les villes favorites", description = "Retourne la liste des villes favorites d'un utilisateur.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des villes favorites récupérée avec succès",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = FavoriteCityResponseDto.class)))
    })
    public ResponseEntity<List<FavoriteCityResponseDto>> list(
            @Parameter(description = "ID de l'utilisateur", example = "1") @PathVariable @Positive Long userId) {
        return ResponseEntity.ok(
                favoriteCityService.listFavoriteCities(userId)
                        .stream()
                        .map(FavoriteCityMapper::toResponseDto)
                        .toList()
        );
    }

    @GetMapping("/{favoriteCityId}")
    @Operation(summary = "Obtenir une ville favorite", description = "Retourne une ville favorite par son identifiant.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ville favorite trouvée",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = FavoriteCityResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Ville favorite introuvable", content = @Content)
    })
    public ResponseEntity<FavoriteCityResponseDto> get(
            @Parameter(description = "ID de l'utilisateur", example = "1") @PathVariable @Positive Long userId,
            @Parameter(description = "ID de la ville favorite", example = "10") @PathVariable @Positive Long favoriteCityId
    ) {
        return favoriteCityService.getFavoriteCity(userId, favoriteCityId)
                .map(FavoriteCityMapper::toResponseDto)
                .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResourceNotFoundException("Ville favorite introuvable"));
    }

    @PostMapping
    @Operation(summary = "Ajouter une ville favorite", description = "Ajoute une nouvelle ville favorite pour l'utilisateur.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Ville favorite créée",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = FavoriteCityResponseDto.class)))
    })
    public ResponseEntity<FavoriteCityResponseDto> create(
            @Parameter(description = "ID de l'utilisateur", example = "1") @PathVariable @Positive Long userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Données de la ville favorite à ajouter", required = true,
                content = @Content(schema = @Schema(implementation = FavoriteCityRequestDto.class)))
            @Valid @RequestBody FavoriteCityRequestDto request
    ) {
        FavoriteCity created = favoriteCityService.addFavoriteCity(userId, request.nomVille());
        return ResponseEntity.status(HttpStatus.CREATED).body(FavoriteCityMapper.toResponseDto(created));
    }

    @PutMapping("/{favoriteCityId}")
    @Operation(summary = "Modifier une ville favorite", description = "Modifie une ville favorite existante.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ville favorite modifiée",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = FavoriteCityResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Ville favorite introuvable", content = @Content)
    })
    public ResponseEntity<FavoriteCityResponseDto> update(
            @Parameter(description = "ID de l'utilisateur", example = "1") @PathVariable @Positive Long userId,
            @Parameter(description = "ID de la ville favorite", example = "10") @PathVariable @Positive Long favoriteCityId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Données de la ville favorite à modifier", required = true,
                content = @Content(schema = @Schema(implementation = FavoriteCityRequestDto.class)))
            @Valid @RequestBody FavoriteCityRequestDto request
    ) {
        return favoriteCityService.updateFavoriteCity(userId, favoriteCityId, request.nomVille())
                .map(FavoriteCityMapper::toResponseDto)
                .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResourceNotFoundException("Ville favorite introuvable"));
    }

    @DeleteMapping("/{favoriteCityId}")
    @Operation(summary = "Supprimer une ville favorite", description = "Supprime une ville favorite de l'utilisateur.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Ville favorite supprimée"),
        @ApiResponse(responseCode = "404", description = "Ville favorite introuvable", content = @Content)
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de l'utilisateur", example = "1") @PathVariable @Positive Long userId,
            @Parameter(description = "ID de la ville favorite", example = "10") @PathVariable @Positive Long favoriteCityId
    ) {
        boolean deleted = favoriteCityService.removeFavoriteCity(userId, favoriteCityId);
        if (!deleted) {
            throw new ResourceNotFoundException("Ville favorite introuvable");
        }
        return ResponseEntity.noContent().build();
    }
}
