package com.tmi.repository;

import com.tmi.dto.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByName(String name);

//    @Query(value = "SELECT *, (6371 * acos(cos(radians(:lat)) * cos(radians(lat)) * cos(radians(lon) - radians(:lon)) + sin(radians(:lat)) * sin(radians(lat)))) AS distance FROM restaurant WHERE lat IS NOT NULL AND lon IS NOT NULL ORDER BY distance ASC LIMIT 1000", nativeQuery = true)
//    List<Restaurant> findNearestRestaurants(@Param("lat") Double lat, @Param("lon") Double lon);

//    @Query("SELECT r FROM Restaurant r ORDER BY SQRT((r.lat - :lat)*(r.lat - :lat) + (r.lon - :lon)*(r.lon - :lon))")
//    List<Restaurant> findNearestRestaurants(@Param("lat") Double lat, @Param("lon") Double lon);

//    @Query(value = "SELECT * FROM restaurant ORDER BY SQRT(POW(ABS(lat - :lat), 2) + POW(ABS(lon - :lon), 2)) LIMIT 100", nativeQuery = true)
//    List<Restaurant> findNearestRestaurants(@Param("lat") Double lat, @Param("lon") Double lon);

    @Query(value = "SELECT * FROM (SELECT *, SQRT(POW(ABS(lat - :lat), 2) + POW(ABS(lon - :lon), 2)) AS distance FROM restaurant WHERE lat IS NOT NULL AND lon IS NOT NULL ORDER BY distance) AS distances LIMIT 100", nativeQuery = true)
    List<Restaurant> findNearestRestaurants(@Param("lat") Double lat, @Param("lon") Double lon);

}
