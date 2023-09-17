package com.tmi.service;

import com.tmi.dto.Restaurant;
import com.tmi.exception.CustomErrorCode;
import com.tmi.exception.CustomException;
import com.tmi.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantServiceImpl {

    private final RestaurantRepository restaurantRepository;
    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    public List<Restaurant> findRestaurantsByName(String name) {
        return restaurantRepository.findByName(name);
    }

    public List<Restaurant> getNearestRestaurants(Double lat, Double lon) {
        return restaurantRepository.findNearestRestaurants(lat, lon);
    }

    public List<Restaurant> getNearByLatAndLon(Double lat, Double lon, int limit) {
        // 유효성 검사: lat과 lon이 null이거나 범위를 벗어나면 예외 처리
        if (lat == null || lon == null || lat < -90.0 || lat > 90.0 || lon < -180.0 || lon > 180.0) {
            log.error("Invalid lat and lon: lat={}, lon={}", lat, lon);
            throw new CustomException(CustomErrorCode.INVALID_LATLON);
        }
        return restaurantRepository.findNearByLatAndLon(lat, lon, limit);
    }

}
