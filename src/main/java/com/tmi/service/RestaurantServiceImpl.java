package com.tmi.service;

import com.tmi.dto.Restaurant;
import com.tmi.exception.CustomErrorCode;
import com.tmi.exception.CustomException;
import com.tmi.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
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
        return restaurantRepository.findNearByLatAndLon(lat, lon, limit);
    }

    public void test(int num){
        if(num == 0){
            throw new CustomException(CustomErrorCode.INVALID_INPUT_VALUE);
        }

    }
}
