package com.vacheronalyssa.weatherdashboardback.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "favorite_city")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteCity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom_ville", nullable = false)
    private String nomVille;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "date_ajout", nullable = false, updatable = false)
    private LocalDateTime dateAjout;

    @PrePersist
    public void prePersist() {
        if (dateAjout == null) {
            dateAjout = LocalDateTime.now();
        }
    }
}
