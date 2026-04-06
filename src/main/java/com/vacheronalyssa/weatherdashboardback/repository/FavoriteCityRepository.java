package com.vacheronalyssa.weatherdashboardback.repository;

import com.vacheronalyssa.weatherdashboardback.entity.FavoriteCity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteCityRepository extends JpaRepository<FavoriteCity, Long> {
}
