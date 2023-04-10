package com.tmi.service;

import com.tmi.dto.Restaurant;
import com.tmi.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RestaurantService {

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
}
