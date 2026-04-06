package com.vacheronalyssa.weatherdashboardback.repository;

import com.vacheronalyssa.weatherdashboardback.entity.FavoriteCity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteCityRepository extends JpaRepository<FavoriteCity, Long> {

	List<FavoriteCity> findByUserId(Long userId);

	Optional<FavoriteCity> findByIdAndUserId(Long id, Long userId);

	long deleteByIdAndUserId(Long id, Long userId);
}
